import java.io.*;
import java.net.*;

public class DownloadSong {

	static String url= "https://www.youtube.com/watch?v=nWCPhAVMh6g";
	public static void main(String[] args){
		/*String command = "cd /Users/RichSharma/Desktop/PersonalSongs ; youtube-dl -x --audio-format mp3 " + url;
		final String[] wrappedCommand = new String[]{"osascript",
	                "-e", "tell application \"Terminal\" to do script \"" + command + ";exit\""};
	    try {
			Process process = new ProcessBuilder(wrappedCommand)
			        .redirectErrorStream(true)
			        .start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		String command = "cd /Users/RichSharma/Desktop/PersonalSongs ; youtube-dl -x --audio-format mp3 --output \"" + "Something" + " " + "Another" + ".%(ext)s\" " + url;
		System.out.println(command);
		
		/*final String[] wrappedCommand = new String[]{"osascript",
                "-e", "tell application \"Terminal\" to activate",
	                "-e", "tell application \"Terminal\" to do script \"" + command + ";exit\""};
	    try {
			Process process = new ProcessBuilder(wrappedCommand)
			        .redirectErrorStream(true)
			        .start();
	    } catch (IOException e) {
		}*/
			
	}

}