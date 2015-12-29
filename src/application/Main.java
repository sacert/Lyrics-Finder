package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
	
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("FX.fxml"));
			Scene scene = new Scene(root,400,400);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		// for testing purposes 
		
		// start youtube-dl - may want to put this is a folder 
//		Process ytd = Runtime.getRuntime().exec("/usr/local/bin/youtube-dl --audio-quality 0 --output /Users/stephen/Desktop/doctor\\ who.mp4 https://www.youtube.com/watch?v=dLzC7lnyiZc");
//		// wait for .mp4 file to be created
//		ytd.waitFor();
//		// begin ffmpeg conversion to .mp3
//		Process ffmp = Runtime.getRuntime().exec("/usr/local/bin/ffmpeg -i /Users/stephen/Desktop/doctor who.mp4 -vn -acodec libmp3lame -ac 2 -qscale:a 4 -ar 48000 /Users/stephen/Desktop/doctor\\ who.mp3");
//		// wait for .mp4 file to be created
//		ffmp.waitFor();
//		Runtime.getRuntime().exec("rm /Users/stephen/Desktop/doctor\\ who.mp4");
		

		launch(args);
		
	}
	
	
	
	
	

	
	
	
	
	
	
	
}
