package application;

import java.io.IOException;

public class DownloadThread extends Thread{
	
	private String songTitle, youtubeReference;

	public DownloadThread(String songTitle, String youtubeReference)
	{
		this.songTitle = songTitle;
		this.youtubeReference = youtubeReference;
	}

	@Override
	public void run()
	{
		Process ytd = null;
		try {
			ytd = Runtime.getRuntime().exec(new String[] { "/usr/local/bin/youtube-dl", "--audio-quality", "0", "--output", "/Users/stephen/Desktop/temp.mp4", "https://www.youtube.com/watch?v=" + youtubeReference});
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		// wait for .mp4 file to be created
		try {
			ytd.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// begin ffmpeg conversion to .mp3
		Process ffmp = null;
		try {
			ffmp = Runtime.getRuntime().exec(new String[] {"/usr/local/bin/ffmpeg", "-i", "/Users/stephen/Desktop/temp.mp4", "-vn", "-acodec", "libmp3lame", "-ac", "2", "-qscale:a", "4", "-ar", "48000", "/Users/stephen/Desktop/temp.mp3"});
		} catch (IOException e) {
			e.printStackTrace();
		}
		// wait for .mp4 file to be created
		try {
			ffmp.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			Runtime.getRuntime().exec(new String[] {"rm", "/Users/stephen/Desktop/temp.mp4"});
		} catch (IOException e) {
			e.printStackTrace();
		}
		Process ffca = null;
		try {
			//ffmpeg32 -i in.mp3 -metadata title="The Title You Want" -metadata artist="Artist Name" -metadata album="Name of the Album" out.mp3
			ffca = Runtime.getRuntime().exec(new String[] {"/usr/local/bin/ffmpeg", "-i", "/Users/stephen/Desktop/temp.mp3", "-i" , FXController.googleImgURLResults.get(0), "-map", "0:0" ,"-map", "1:0", "-c", "copy", "-id3v2_version", "3", "/Users/stephen/Desktop/" + songTitle +".mp3",});
		} catch (IOException e) {
			e.printStackTrace();
		}
		// wait for .mp4 file to be created
		try {
			ffca.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		try {
			Runtime.getRuntime().exec(new String[] {"rm", "/Users/stephen/Desktop/temp.mp3"});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
