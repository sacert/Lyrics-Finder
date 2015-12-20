package application;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import org.json.*;

public class FXController implements Initializable {

	private final static String metrolyrics = "metrolyrics.com";

	@FXML private TextArea lyricBox;
	@FXML private TextField getLyricsField;
	@FXML private Button getLyricsButton;




	@FXML
	private void handleButtonAction(ActionEvent event)  {
		lyricBox.clear();

		String query = getLyricsField.getText();

		List<String> googleURLResults = null;
		try {
			googleURLResults = googleSearchQueryResults(metrolyrics,query);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//		
		List<String> lyricsURL;
		try {		
			lyricsURL = getSongLyricsFromMetroLyrics(googleURLResults.get(0)); // Parse from the FIRST result.
			printLyricsToUI(lyricsURL);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}




	static List<String> googleSearchQueryResults (String websiteToGetLyricsFrom, String lyricsQuery) throws IOException{

		final String usersIPAddres = fetchIpFromAmazon();
		URL url = new URL(
				"https://ajax.googleapis.com/ajax/services/search/web?v=1.0&"
						+ "q="
						+ lyricsQuery.replace(" ", "%20")
						+ "%20site:"
						+ websiteToGetLyricsFrom
						+ "&userip="
						+ usersIPAddres);

		System.out.println(url.toString());

		URLConnection connection = url.openConnection();
		connection.addRequestProperty("Referer", "www.referrer.com");

		String line;
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		while((line = reader.readLine()) != null) {
			builder.append(line);
		}

		JSONObject json = new JSONObject(builder.toString()).getJSONObject("responseData");


		List<String> azLyricsWebsites = new ArrayList<String>();

		JSONArray websiteLinksArray = (JSONArray) json.get("results");

		for(int i = 0 ; i < websiteLinksArray.length() ; i++){
			azLyricsWebsites.add(websiteLinksArray.getJSONObject(i).getString("url"));

			System.out.println("Link : " + azLyricsWebsites.get(i));
		}
		return azLyricsWebsites;
	}



	public static List<String> getSongLyricsFromMetroLyrics(String fullURLPath) throws IOException {

		System.out.println("FULL PATH = " + fullURLPath);

		List<String> lyrics = new ArrayList<String>();		
		//		Document doc = Jsoup.connect(lyricsURL + "/" + song.replace(" ", "-").toLowerCase() + "-lyrics-" + artist.replace(" ", "-").toLowerCase()).get();
		Document doc = Jsoup.connect(fullURLPath).get();
		String title = doc.title();
		title = title.substring(0, title.length()-20);
		System.out.println(title);
		System.out.println("===============");

		// get the total number of verses 
		int verseSize = doc.select("p.verse").size();
		for(int i = 0; i < verseSize; i++) {
			// get each verse
			Element p = doc.select("div.lyrics-body").get(0).child(0).child(i);
			for(Node e: p.childNodes()) {
				if(e instanceof TextNode) {
					lyrics.add(((TextNode)e).getWholeText());
				}
			}
			lyrics.add("\n\n");
		}

		return lyrics;
	}


	public  void printLyricsToUI(List<String> lyrics) {

		for(int i = 0; i < lyrics.size(); i++) {
			System.out.print(lyrics.get(i));
			lyricBox.setText(lyricBox.getText() + lyrics.get(i));
			if(lyrics.get(i).contains(" ")) {
				System.out.print(",");

			}
		}
	}


	private static String fetchIpFromAmazon() {
		URL url = null;
		try {
			url = new URL("http://checkip.amazonaws.com/");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(url.openStream()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			//			System.out.println(br.readLine());
			return br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Failed to get IP from Amazon";
	}

}


