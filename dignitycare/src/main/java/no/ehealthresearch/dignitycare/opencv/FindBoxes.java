package no.ehealthresearch.dignitycare.opencv;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
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
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class FindBoxes {
	private static double dilate=4;
	
	public FindBoxes(){
		
	}
	
    public static List<Rectangle2D> proccess(String path) {
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
        
       // Imgproc.threshold(gray,bw, 150, 255,Imgproc.THRESH_OTSU);
        //showWaitDestroy("no lines",bw);
        
        Imgproc.adaptiveThreshold(gray, bw, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, -2);
        //showWaitDestroy("bw",bw);
        
        // Show binary image
        
        //showWaitDestroy("no lines",bw);
        
        
        //copy of src. we will paint on this after finding lines
        Mat copy = new Mat(src.size(),src.type(),new Scalar(255,255,255));
        src.copyTo(copy);
        
        mats.add(copy);
       
        /*
         * horisontal
         */
        
        List<Line2D> horisontalLines = findHorisontal(
        		Imgproc.getStructuringElement(
                		Imgproc.MORPH_RECT, 
                		new Size(
                				bw.rows() / 30,
                				4)),
        		Imgproc.getStructuringElement(
                		Imgproc.MORPH_RECT, 
                		new Size(
                				bw.rows() / 22,
                				4)),
        		bw,
        		copy);
        
        /*
         * vertical
         */
        
        List<Line2D> verticalLines = findVertical(
        		Imgproc.getStructuringElement(
                		Imgproc.MORPH_RECT, 
                		new Size(
                				2,
                				90)),
        		bw,
        		copy,
        		horisontalLines);
        
       copy.release();
       bw.release();
       src.release();
       gray.release();
       
       //showWaitDestroy("only lines",grayCopy);
       
       List<Rectangle2D> rects = FindRectanglesFromLines.get(horisontalLines, verticalLines);
       
       return rects;
    }
    
    private static List<Line2D> findHorisontal(Mat kernel,Mat kernel2,Mat bwSource,Mat canvas) {
    	
        // Specify size on horizontal axis
        List<Mat> mats=new ArrayList<>();
        
        // Create structure element for extracting horizontal lines through morphology operations
    	Mat bw = bwSource.clone();
    	mats.add(bw);
        
        // Apply morphology operations
        Imgproc.erode(bw,bw,kernel);
        Imgproc.dilate(bw,bw,kernel2);
        
        List<MatOfPoint> contours = new ArrayList<>();
        List<Line2D> rects=new ArrayList<>();
        
        Mat hierarchy = new Mat();
        mats.add(hierarchy);
        
        Imgproc.findContours(bw, contours,hierarchy , Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        
        
        
        for (int i = 0; i < contours.size(); i++) {
        	MatOfPoint2f poly = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), poly, 1, true);
            
            MatOfPoint matOfPoints = new MatOfPoint(poly.toArray());
            
            Rect rect = Imgproc.boundingRect(matOfPoints);
            
            poly.release();
            matOfPoints .release();
           
            rects.add(new Line2D.Double(
    				rect.x- dilate,
    				rect.y,
    				rect.width+rect.x+ dilate,
    				rect.y));
            
            contours.get(i).release();
        }
        
        mats.forEach(m->m.release());
      
        return rects;
    }
    
    private static List<Line2D> findVertical(Mat kernel,Mat bwSource,Mat canvas,List<Line2D> rects) {
    	
        // Specify size on horizontal axis
    	List<Mat> mats=new ArrayList<>();
    	
        List<Line2D> lines=new ArrayList<>();
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
       
        for (int i = 0; i < contours.size(); i++) {
        	MatOfPoint2f poly = new MatOfPoint2f();
            Imgproc.approxPolyDP(new MatOfPoint2f(contours.get(i).toArray()), poly, 1, true);
            
            MatOfPoint matOfPoints = new MatOfPoint(poly.toArray());
            
            Rect candidateRect = Imgproc.boundingRect(matOfPoints);
            
            Line2D candidateLine=new Line2D.Double(
    				candidateRect.x,
    				candidateRect.y- dilate,
    				candidateRect.x,
    				candidateRect.y+candidateRect.height+ dilate);
           
            poly.release();
            matOfPoints.release();
            
            lines.add(candidateLine);
            
            contours.get(i).release();
        }
        
        mats.forEach(m->m.release());
        
        return lines;
    }
}
