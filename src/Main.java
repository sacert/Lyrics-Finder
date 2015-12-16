import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class Main {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		List<String> lyrics = getSongLyrics("Journey","Dont stop believin");
		printLyrics(lyrics);
	}
	
	public static void printLyrics(List<String> lyrics) {
		
		for(int i = 0; i < lyrics.size(); i++) {
			System.out.print(lyrics.get(i));
			if(lyrics.get(i).contains(" ")) {
				System.out.print(",");
			}
		}
	}
	
	private final static String lyricsURL = "http://www.metroLyrics.com";
	
	public static List<String> getSongLyrics(String artist, String song) throws IOException {
		
		List<String> lyrics = new ArrayList<String>();
		
		Document doc = Jsoup.connect(lyricsURL + "/" + song.replace(" ", "-").toLowerCase() + "-lyrics-" + artist.replace(" ", "-").toLowerCase()).get();
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

}
