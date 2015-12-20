package application;
	
import java.awt.ScrollPane;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	
	
//	@FXML private TextArea lyricBox;
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			

//			lyricBox = new TextArea();
//			lyricBox.setText("HEY");
//		
			
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("FX.fxml"));
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {

		
		
		
		launch(args);
	}
	
	
	
	
	

	
	
	
	
	
	
	
}
