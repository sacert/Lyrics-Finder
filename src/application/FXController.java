package application;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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

	private final static String azlyrics = "azlyrics.com";
	private final static String youtube = "www.youtube.com";

	@FXML private TextArea lyricBox;
	@FXML private TextField getLyricsField;
	@FXML private Button getLyricsButton;
	
	private static String songTitle = "";
	private static String albumTitle = "";
	private static String bandArtist = "";
	
	public static List<String> googleImgURLResults = null;

	@FXML
	private void handleButtonAction(ActionEvent event) throws IOException, InterruptedException  {
		lyricBox.clear();

		String query = getLyricsField.getText();

		List<String> googleURLResults = null;
		
		// get lyrics for song
		googleURLResults = googleSearchQueryResults(azlyrics,query);
		List<String> lyricsURL;
		lyricsURL = getSongLyricsFromAZLyrics(googleURLResults.get(0)); // Parse from the FIRST result.
		printLyricsToUI(lyricsURL);
		
		// get youtube link for song
		googleURLResults = googleSearchQueryResults(youtube,query);
		List<String> YoutubeURL = new ArrayList<String>(); // leave in a List so that in the future, we can look at which to download
		YoutubeURL.add(googleURLResults.get(0));
		YoutubeURL.set(0, YoutubeURL.get(0).replace("https://www.youtube.com/watch%3Fv%3D", "")); 
		googleImgURLResults = googleImageSearchQueryResults();
		downloadSong(YoutubeURL.get(0));
		System.out.println(googleImgURLResults);
	}

	// if not using, delete
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}
	
	static void downloadSong(String youtubeReference) throws IOException, InterruptedException {
		
		DownloadThread dt = new DownloadThread(songTitle, youtubeReference);
		dt.start();
		
		// do you need to join threads in Java? 
		// look into it later
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
		}
		return azLyricsWebsites;
	}
	
	static List<String> googleImageSearchQueryResults () throws IOException{

		albumTitle = albumTitle.replace("\"", "");
		albumTitle = albumTitle.substring(0, albumTitle.length()-7);
		System.out.println(albumTitle + "!");
		List<String> imageURLs = new ArrayList<String>();
		
		
		URL url = new URL(
				"https://www.googleapis.com/customsearch/v1?key=AIzaSyCHoGF1u8RytvaNeCk69iyD9ouwFBmsndQ&cx=007547444199860528947:flga7fapbrm&q="
						+ bandArtist.replace(" ", "%20")
						+ albumTitle.replace(" ", "%20")
						+ "%20album%20cover%20art"
						+ "&userip=&searchType=image&alt=json");
		
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (conn.getInputStream())));

        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            if(output.contains("\"link\":")) {
            	imageURLs.add(output.substring(12, output.length()-2));
            }
        }

        conn.disconnect();

		return imageURLs;
	}

	public static List<String> getSongLyricsFromAZLyrics(String fullURLPath) throws IOException {

		List<String> lyrics = new ArrayList<String>();		
		Document doc = Jsoup.connect(fullURLPath).get();
		String title = titleFixer(doc.title());
		
		songTitle = title;
		
		System.out.println(title);
		
		String[] breakTitle = songTitle.split("-");
		bandArtist = breakTitle[0];
		
		System.out.println(bandArtist);
		
		Element q = doc.select("div.album-panel").get(0).child(1);
		albumTitle = q.text();
		System.out.println(albumTitle);

		// get each line
		Element p = doc.select("div").get(22);
		for(Node e: p.childNodes()) {
			if(e instanceof TextNode) {
				lyrics.add(((TextNode)e).text() + "\n");
			}
		}
		// first line is a white space, remove it
		lyrics.remove(0);

		return lyrics;
	}


	public  void printLyricsToUI(List<String> lyrics) {

		for(int i = 0; i < lyrics.size(); i++) {
			lyricBox.setText(lyricBox.getText() + lyrics.get(i));
		}
	}


	// Put into another class
	private static String fetchIpFromAmazon() throws IOException {
		URL url = null;
		url = new URL("http://checkip.amazonaws.com/");
		BufferedReader br = null;
		br = new BufferedReader(new InputStreamReader(url.openStream()));
		try {
			return br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// shouldn't this be within the catch statement?
		return "Failed to get IP from Amazon";
	}
	
	private static String toTitleCase(String givenString) {
	    String[] arr = givenString.split(" ");
	    StringBuffer sb = new StringBuffer();

	    for (int i = 0; i < arr.length; i++) {
	        sb.append(Character.toUpperCase(arr[i].charAt(0)))
	            .append(arr[i].substring(1)).append(" ");
	    }          
	    return sb.toString().trim();
	}  
	
	private static String titleFixer(String title) {
		title = title.substring(0, title.length());
		title = title.toLowerCase();
		title = toTitleCase(title);
		
		title = title.replace("Lyrics", "");
		title = title.replace("  ", " ");
		
		return title;
	}
	
}


