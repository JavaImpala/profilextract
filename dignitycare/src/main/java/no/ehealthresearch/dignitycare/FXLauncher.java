package no.ehealthresearch.dignitycare;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.apache.commons.lang3.mutable.MutableInt;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import no.ehealthresearch.dignitycare.fastlege.GPToFHIR;
import no.ehealthresearch.dignitycare.opencv.ImageAnalyzer;
import no.ehealthresearch.dignitycare.pdf.SplitPDFIntoImages;
import no.ehealthresearch.dignitycare.tesseract.ImageToText;

public class FXLauncher extends Application {//ekstendere Application for at String args skal defineres, har noe med JavaFX å gjøre?	
	
	private final TextArea textField=new TextArea();
	
	public FXLauncher(){
		textField.setPrefHeight(1000);
	}
	
	public static void main (String[] args) { 
		Application.launch(args);
	}
	
	public void appendText(String str) {
		
		Platform.runLater(() -> textField.appendText(str));
	}
	
	@Override
	public void init() {
	    OutputStream out = new OutputStream() {
	        @Override
	        public void write(int b) throws IOException {
	            appendText(String.valueOf((char) b));
	        }
	        
	        @Override
	        public void write(byte[] b, int off, int len) throws IOException {
	        	appendText(new String(b, off, len));
	        }
	        
	        @Override
	        public void write(byte[] b) throws IOException {
	        	write(b, 0, b.length);
	        }
	    };
	    
	    System.setOut(new PrintStream(out, true));
	    //System.setErr(new PrintStream(out, true));
	}

	@Override
	public void start(Stage mainStage){ 
		
		//launch("2-crypt.ffhir","password");
		
		
		StackPane root=new StackPane();
		root.setPrefSize(2000,1000);
		
		VBox formContainer=new VBox();
		formContainer.setFillWidth(true);
		formContainer.setSpacing(10);
		formContainer.setPadding(new Insets(10,20,10,20));
		formContainer.setPrefHeight(2000);
		
		
		
		root.getChildren().add(formContainer);
		
		HBox topRow=new HBox();
		
		
		Button selectFile=new Button("Velg fil");
		selectFile.setMinWidth(100);
		
		TextField pathContainer=new TextField();
		
		Label selectFileLegend=new Label("Velg pasient:");
		selectFileLegend.setMinWidth(120);
		
		Button submit=new Button("Analyser fil");
		
		selectFile.setOnMouseClicked(m->{
			FileChooser fileChooser = new FileChooser();
			//fileChooser.setInitialDirectory(new File("src/main/resources/"));
			fileChooser.setTitle("Open Resource File");
			
			fileChooser.getExtensionFilters().addAll(
					new ExtensionFilter(
						"PDF",
						"*.pdf"));
			
			File selectedFile = fileChooser.showOpenDialog(mainStage);
			
			if (selectedFile != null) {
				pathContainer.textProperty().set(selectedFile.getAbsolutePath().toString());
				submit.setDisable(false);
			}
		});
		
		topRow.getChildren().addAll(selectFileLegend,selectFile,pathContainer);
		
		
		
		
		formContainer.widthProperty().addListener((a,b,c)->{
			
			pathContainer.setPrefWidth(c.doubleValue());
		});
		
		
		
		
		
		
		submit.setDisable(true);
		
		submit.setOnMouseClicked(m->{
			submit.setDisable(true);
			launch(pathContainer.getText());
		});
		
		formContainer.getChildren().addAll(
				topRow,
				//bottomRow,
				
				submit,
				new Separator(),
				textField);	
		
		Scene scene=new Scene(root);
		
		
		mainStage.setScene(scene);
		
		mainStage.show();
		
		
	}
	
	private void launch(String path) {
		
		try {
			File file=new File(path);
			String dirName=file.getParent()+"/"+file.getName()+"-extract";
	    	
    	    new File(dirName).mkdirs();
    	    
    	    ImageAnalyzer imageAnalyzer=new ImageAnalyzer();
    	    ImageToText imageToText=new ImageToText();
    	    
    	    MutableInt imageCount=new MutableInt(Integer.MAX_VALUE);
    	    MutableInt imageProcCount=new MutableInt(0);
    	    
    	    Runnable getFHIR=()->{
    	    	GPToFHIR.extract(dirName);
    	    };
    	    
    	    SplitPDFIntoImages.extract(
    	    		path,
    	    		dirName, 
    	    		p->{
    	    			imageAnalyzer.analyzeImage(
	    					p,
	    					w->{
	    						imageToText.extract(
	    								w,
	    								()->{
	    									System.out.println("ferdig å analysere "+imageProcCount.incrementAndGet()+"/"+imageCount.getValue());
	    									
	    									if(imageProcCount.getValue()>=imageCount.getValue()) {
	    										System.out.println();
	    										System.out.println("FERDIG Å ANALYSERE BILDER ");
	    										System.out.println();
	    										
	    										getFHIR.run();
	    									}
	    								});
	    					});
    	    			
    	    			
    	    		},
    	    		i->{
    	    			imageCount.setValue(i);
    	    			
    	    			if(imageProcCount.getValue()>=imageCount.getValue()) {
							System.out.println();
							System.out.println("FERDIG Å ANALYSERE BILDER ");
							System.out.println();
							
							getFHIR.run();
						}
    	    		});
			
		}catch(Exception e) {
			e.printStackTrace(System.out);
			return;
		}
		
	}
	
	@Override 
	public void stop() {
		GlobalShutdown.INSTANCE.shutDown();
		
		new Thread(()->{
			try {
				//program får 5 sek på å avslutte pyntelig. Deretter er det ingen nåde.
	            Thread.sleep(5000);
	            System.exit(0);
	         } catch (Exception e) {
	            System.out.println(e);
	         }
		}).start();	
	}
}

