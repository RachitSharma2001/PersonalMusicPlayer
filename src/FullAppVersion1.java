import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FullAppVersion1 extends Application {
	static Scene menu, songList, addSongs, playSongs;
	static String songsDirectory = "/Users/RichSharma/Desktop/PersonalSongs/";
	static File playlist = new File("/Users/RichSharma/Desktop/PersonalSongs/playlist.txt");
	
	public static void makeMenu(Stage window){
		
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
	    hbox.setSpacing(10);
	    hbox.setStyle("-fx-background-color: #336699;");

	    Button playS = new Button("Play Songs");
	    playS.setPrefSize(100, 20);

	    Button addS = new Button("Add Songs");
	    addS.setPrefSize(100, 20);
	    addS.setOnAction(e -> window.setScene(addSongs));
	    
	    Button editList = new Button("Edit List");
	    editList.setPrefSize(100, 20);
	    
	    hbox.getChildren().addAll(addS, playS, editList);
	    
	    BorderPane background = new BorderPane(hbox);
	    
	    menu = new Scene(background, 500, 500);
	    
	}
	
	public static void makeAddSongs(Stage window){
		Button back = new Button("Back");
	    back.setPrefSize(50, 20);
	    back.setOnAction(e -> window.setScene(menu));
	    
		TextField url = new TextField("Enter url here");
		TextField songName = new TextField("Enter song name here");
		TextField artistName = new TextField("Enter artist name here");
		
		Button add = new Button("Add");
	    add.setPrefSize(80, 20);
	    add.setOnAction(e -> addToPlaylist(url, songName, artistName));
		
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(15, 12, 15, 12));
	    vbox.setSpacing(10);
	    vbox.setStyle("-fx-background-color: #336699;");
	    vbox.getChildren().addAll(back, url, songName, artistName, add);
	    
	    BorderPane background = new BorderPane(vbox);
	    
	    addSongs = new Scene(background, 200, 200);
	}
	
	public static ArrayList<String> returnCurrentFiles(){
		try {
			Scanner in = new Scanner(new File(songsDirectory + "Playlist.txt"));
			ArrayList<String> songList = new ArrayList<String>();
			while(in.hasNextLine()){
				songList.add(in.nextLine());
			}
			
			return songList;
		} catch (FileNotFoundException e) {
			return null;
		}
	}
	
	public static void changeFileName(String songName, String artistName){
		ArrayList<String> songsList = returnCurrentFiles();
		
		File songsD = new File(songsDirectory);
		File[] allFiles = songsD.listFiles();
		
		for(int i = 0; i < allFiles.length; i++){
			if(allFiles[i].getName().equals("Playlist.txt") || allFiles[i].getName().equals("youtube-dl") || songsList.contains(allFiles[i].getName())) continue;
			File file = new File(songsDirectory + songName + " " + artistName + ".mp3");
			try {
				InputStream input = new BufferedInputStream(new FileInputStream(allFiles[i]));
				FileOutputStream output = new FileOutputStream(file);
				byte[] specific_bytes = new byte[1024];
				while(true){
					int length;
					try {
						length = input.read(specific_bytes);
						if(length <= 0) break;
						output.write(specific_bytes, 0, length);
						//for(int j = 0; j < length; j++) System.out.print(specific_bytes[j]);
						System.out.println();
						output.flush();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			allFiles[i].delete();
		}
	}
	
	public static void addToPlaylist(TextField url, TextField songName, TextField artistName){
		String command = "cd /Users/RichSharma/Desktop/PersonalSongs ; youtube-dl -x --audio-format mp3 " + url.getText();
		final String[] wrappedCommand = new String[]{"osascript",
	                "-e", "tell application \"Terminal\" to do script \"" + command + ";exit\""};
	    try {
			Process process = new ProcessBuilder(wrappedCommand)
			        .redirectErrorStream(true)
			        .start();
			
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			changeFileName(songName.getText(), artistName.getText());
			
			FileWriter addToPlaylist = new FileWriter(playlist, true);
			addToPlaylist.write(songName.getText() + " " + artistName.getText() + ".mp3\n");
			addToPlaylist.close();
			
			url.setText("");
			songName.setText("");
			artistName.setText("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			url.setText("The given url was invalid");
			songName.setText("");
			artistName.setText("");
		}
	}
	
	public static void makePlaySongs(Stage window){
		Button backToMenu = new Button("Back to Menu");
		backToMenu.setPrefSize(100, 30);
		
		
	}
	
	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage mainWindow) throws Exception {
		makeAddSongs(mainWindow);
		makeMenu(mainWindow);
		
		mainWindow.setScene(menu);
		mainWindow.show();
	}

}
