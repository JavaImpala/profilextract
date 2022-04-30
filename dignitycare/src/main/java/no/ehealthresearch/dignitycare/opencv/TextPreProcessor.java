package no.ehealthresearch.dignitycare.opencv;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * gjør det lettere å lese tekst ved å rense bort rette linjer
 * 
 * @author tor003
 *
 */

public class TextPreProcessor {
	
	
	public TextPreProcessor(){
		
	}
	
    public void proccess(String path) {
        // Load the image
        List<Mat> mats=new ArrayList<>();
    	
    	Mat src = Imgcodecs.imread(path);
        
    	mats.add(src);
        
        // Transform source image to gray if it is not already
        Mat gray = new Mat();
        mats.add(gray);
        
        if (src.channels() == 3){
            Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        }else{
            gray = src;
        }
        
        // Show gray image
        //showWaitDestroy("gray" , gray);
        // Apply adaptiveThreshold at the bitwise_not of gray
        Mat bw = new Mat();
        mats.add(bw);
        
        Core.bitwise_not(gray, gray);
        Imgproc.adaptiveThreshold(gray, bw, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, -2);
        // Show binary image

        //copy of src. we will paint on this after finding lines
        Mat copy = new Mat(src.size(),src.type(),new Scalar(255,255,255));
        src.copyTo(copy);
        
        mats.add(copy);
       
        /*
         * horisontal
         */
        
        List<Rect> horisontalLines = findAndPaintHorisontal(
        		Imgproc.getStructuringElement(
                		Imgproc.MORPH_RECT, 
                		new Size(
                				bw.rows() / 30,
                				7)),
        		bw,
        		copy);
        
        /*
         * vertical
         */
        
        findAndPaintVertical(
        		Imgproc.getStructuringElement(
                		Imgproc.MORPH_RECT, 
                		new Size(
                				2,
                				90)),
        		bw,
        		copy,
        		horisontalLines);
        
        //scale down copy
        
       
       
        
        //showWaitDestroy("no lines",copy);
        
        //threshold til sist
        Mat threshold = new Mat();
        mats.add(threshold);
        
        Mat grayCopy = new Mat(copy.size(),copy.type(),new Scalar(255,255,255));
        mats.add(grayCopy);
        
        Imgproc.cvtColor(copy, grayCopy, Imgproc.COLOR_BGR2GRAY);
        
        //Imgproc.adaptiveThreshold(grayCopy, threshold, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 501, -2);
        Imgproc.threshold(grayCopy,threshold, 150, 255,Imgproc.THRESH_OTSU);
        
        
        //showWaitDestroy("result",threshold);
        
        System.out.println("saving "+path.substring(0, path.lastIndexOf('.'))+"-proc.tif");
        
        Imgcodecs.imwrite(path.substring(0, path.lastIndexOf('.'))+"-proc.tif", threshold);
        
        mats.forEach(m->m.release());
    }
    
    private  List<Rect> findAndPaintHorisontal(Mat kernel,Mat bwSource,Mat canvas) {
    	
        // Specify size on horizontal axis
        List<Mat> mats=new ArrayList<>();
        
        // Create structure element for extracting horizontal lines through morphology operations
    	Mat bw = bwSource.clone();
    	mats.add(bw);
        
        // Apply morphology operations
        Imgproc.erode(bw,bw,kernel);
        Imgproc.dilate(bw,bw,kernel);
         
        List<MatOfPoint> contours = new ArrayList<>();
        List<Rect> rects=new ArrayList<>();
        
        Mat hierarchy = new Mat();
        mats.add(hierarchy);
        
        Imgproc.findContours(bw, contours,hierarchy , Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        
        //showWaitDestroy("faewf",bw);
        
        double padding=1;
        
        for (int i = 0; i < contours.size(); i++) {
        	MatOfPoint2f poly = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), poly, 1, true);
            
            MatOfPoint matOfPoints = new MatOfPoint(poly.toArray());
            
            Rect rect = Imgproc.boundingRect(matOfPoints);
            
            poly.release();
            matOfPoints .release();
           
            rects.add(rect);
            
            Imgproc.rectangle(
            		canvas,
            		new Point(rect.tl().x-padding,rect.tl().y-padding),
            		new Point(rect.br().x+padding,rect.br().y+padding),
            		new Scalar(255,255,255),-1);
            
            contours.get(i).release();
        }
        
        mats.forEach(m->m.release());
      
        return rects;
    }
    
    private List<Rect> findAndPaintVertical(Mat kernel,Mat bwSource,Mat canvas,List<Rect> rects) {
    	
        // Specify size on horizontal axis
    	 List<Mat> mats=new ArrayList<>();
    	System.out.println(rects.size());
        
        // Create structure element for extracting horizontal lines through morphology operations
    	Mat bw = bwSource.clone();
        mats.add(bw);
    	
        // Apply morphology operations
        Imgproc.erode(bw,bw,kernel);
        Imgproc.dilate(bw,bw,kernel);
        
        List<MatOfPoint> contours = new ArrayList<>();
        
        Mat hierarchy=new Mat();
        mats.add(hierarchy);
        
        Imgproc.findContours(bw, contours, new Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        
        double padding=1;
        
        for (int i = 0; i < contours.size(); i++) {
        	MatOfPoint2f poly = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), poly, 1, true);
            
            MatOfPoint matOfPoints = new MatOfPoint(poly.toArray());
            
            Rect candidateRect = Imgproc.boundingRect(matOfPoints);
            
            poly.release();
            matOfPoints.release();
            
            for(Rect testRect:rects) {
            	if(		candidateRect.tl().x>testRect.br().x ||
            			candidateRect.br().x<testRect.tl().x ||
            			candidateRect.br().y<testRect.tl().y ||
            			candidateRect.tl().y>testRect.br().y) {
            		continue;
            	}else {
            		Imgproc.rectangle(
                     		canvas,
                     		new Point(candidateRect.tl().x-padding,candidateRect.tl().y-padding),
                     		new Point(candidateRect.br().x+padding,candidateRect.br().y+padding),
                     		new Scalar(255,255,255),
                     		-1);
            		break;
            	}
            }
            
            contours.get(i).release();
        }
        
        mats.forEach(m->m.release());
        
        return rects;
    }
    
    private  void showWaitDestroy(String winname, Mat src) {
    	
    	Mat scaled = new Mat();

         //Scaling the Image
         
        double scale=0.2;
         
        Imgproc.resize(src, scaled, new Size(src.cols()*scale, src.rows()*scale), 0, 0, Image.SCALE_DEFAULT);
    	
        HighGui.imshow(winname, scaled );
        HighGui.moveWindow(winname, 500, 0);
        HighGui.waitKey(0);
        HighGui.destroyWindow(winname);
    }

	
}
