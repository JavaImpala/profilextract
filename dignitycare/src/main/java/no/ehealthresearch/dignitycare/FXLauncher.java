package no.ehealthresearch.dignitycare;

import java.io.File;


import com.google.inject.Guice;
import com.google.inject.Injector;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class FXLauncher extends Application {//ekstendere Application for at String args skal defineres, har noe med JavaFX å gjøre?	
		
		public FXLauncher(){
			
		}
		
		public static void main (String[] args) { 
			Application.launch(args);
		}

		@Override
		public void start(Stage mainStage){ 
			
			//launch("2-crypt.ffhir","password");
			
			
			StackPane root=new StackPane();
			root.setPrefSize(650, 150);
			
			VBox formContainer=new VBox();
			formContainer.setFillWidth(true);
			formContainer.setSpacing(10);
			formContainer.setPadding(new Insets(10,20,10,20));
			
			formContainer.setMaxSize(650, 120);
			formContainer.setPrefSize(650, 120);
			
			root.getChildren().add(formContainer);
			
			HBox topRow=new HBox();
			
			
			Button selectFile=new Button("Velg fil");
			selectFile.setMinWidth(100);
			
			TextField pathContainer=new TextField();
			
			Label selectFileLegend=new Label("Velg pasient:");
			selectFileLegend.setMinWidth(120);
			
			selectFile.setOnMouseClicked(m->{
				FileChooser fileChooser = new FileChooser();
				//fileChooser.setInitialDirectory(new File("src/main/resources/"));
				fileChooser.setTitle("Open Resource File");
				fileChooser.getExtensionFilters().addAll(
					new ExtensionFilter(
						"FlatFHIR",
						"*.ffhir"));
				
				
			});
			
			topRow.getChildren().addAll(selectFileLegend,selectFile,pathContainer);
			
			HBox bottomRow=new HBox();
			Label passwordLegend=new Label("Velg passord:");
			passwordLegend.setMinWidth(120);
			PasswordField password=new PasswordField();
			
			
			formContainer.widthProperty().addListener((a,b,c)->{
				password.setPrefWidth(c.doubleValue());
				pathContainer.setPrefWidth(c.doubleValue());
			});
			
			
			bottomRow.getChildren().addAll(passwordLegend,password);
			
			Button submit=new Button("Åpne program");
			
			submit.setOnMouseClicked(m->{
				launch(pathContainer.getText(),password.getText());
			});
			
			formContainer.getChildren().addAll(topRow,bottomRow,submit);		
			Scene scene=new Scene(root);
			
			scene.setOnKeyPressed(m->{
				if(m.getCode().equals(KeyCode.ENTER)) {
					launch(pathContainer.getText(),password.getText());
					mainStage.hide();
				}
			});
			
			mainStage.setScene(scene);
			
			mainStage.show();
			
			
		}
		
		private void launch(String path,String pass) {
			
			try {
				
				
				
			}catch(Exception e) {
				e.printStackTrace();
				return;
			}
			
		}
		

		@Override 
		public void stop() {
			
		}
	}
}
