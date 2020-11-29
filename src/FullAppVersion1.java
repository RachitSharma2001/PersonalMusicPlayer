
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FullAppVersion1 extends Application {
	static Scene menu, editSongs, addSongs, playSongs;
	static String songsDirectory = "/Users/RichSharma/Desktop/PersonalSongs/";
	static File playlist = new File("/Users/RichSharma/Desktop/PersonalSongs/playlist.txt");
	
	public static void makeMenu(Stage window){
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
	    hbox.setSpacing(10);
	    hbox.setStyle("-fx-background-color: #336699;");

	    Button playS = new Button("Play Songs");
	    playS.setPrefSize(100, 20);
	    playS.setOnAction(e -> window.setScene(playSongs));

	    Button addS = new Button("Add Songs");
	    addS.setPrefSize(100, 20);
	    addS.setOnAction(e -> window.setScene(addSongs));
	    
	    Button editList = new Button("Edit List");
	    editList.setPrefSize(100, 20);
	    editList.setOnAction(e -> window.setScene(editSongs));
	    
	    hbox.getChildren().addAll(addS, playS, editList);
	    
	    BorderPane background = new BorderPane(hbox);
	    
	    menu = new Scene(background, 500, 500);
	    
	}
	
	public static void removeSong(ListView<String> allSongs, int id){
		allSongs.getItems().remove(id);
		ArrayList<String> currentSongList = getCurrentSongs();
		currentSongList.remove(id);
		FileWriter write;
		try {
			write = new FileWriter(new File(songsDirectory + "Playlist.txt"));
			
			for(int i = 0; i < currentSongList.size(); i++){
				write.write(currentSongList.get(i) + "\n");
			}
			
			write.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File songsD = new File(songsDirectory);
		File[] allFiles = songsD.listFiles();
		
		for(int i = 0; i < allFiles.length; i++){
			System.out.println(allFiles[i].getName());
			if(allFiles[i].getName().equals("Playlist.txt") || allFiles[i].getName().equals("youtube-dl") || currentSongList.contains(allFiles[i].getName())) continue;
			allFiles[i].delete();
			return;
		}
	}
	
	public static void makeEditList(Stage window){
		int height = 400;
		int width = 300;
		
		ArrayList<String> songNames = getCurrentSongs();
		ObservableList<String> copiedSongs = FXCollections.observableArrayList();
		for(int i = 0; i < songNames.size(); i++) copiedSongs.add(songNames.get(i));
		ListView<String> allSongs = new ListView<String>(copiedSongs);
		System.out.println(allSongs.getSelectionModel().getSelectedIndex());
		ScrollPane scrollSongs = new ScrollPane();
		scrollSongs.setContent(allSongs);
		
		scrollSongs.setPrefHeight(0.9 * height);
		scrollSongs.setPrefWidth(0.80 * width);
		
		Button delete = new Button("Delete");
		delete.setOnAction(e -> removeSong(allSongs, allSongs.getSelectionModel().getSelectedIndex()));
		
		Button back = new Button("Back");
	    back.setPrefSize(50, 20);
	    back.setOnAction(e -> window.setScene(menu));
	    back.setAlignment(Pos.TOP_LEFT);
		
		HBox middleView = new HBox();
		middleView.getChildren().addAll(scrollSongs, delete);
		
		VBox wholeView = new VBox();
		wholeView.getChildren().addAll(back, middleView);
		
		BorderPane background = new BorderPane(wholeView);
	    
	    editSongs = new Scene(background, width, height);
	}
	
	public static void makeAddSongs(Stage window){
		Button back = new Button("Back");
	    back.setPrefSize(50, 20);
	    back.setOnAction(e -> window.setScene(menu));
	    
	    TextField url = new TextField("Url");
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
	
	public static void makePlaySongs(Stage window){
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(15, 12, 15, 12));
	    vbox.setSpacing(10);
	    
		Button backToMenu = new Button("Back to Menu");
		backToMenu.setPrefSize(150, 30);
		backToMenu.setOnAction(e->window.setScene(menu));
		
		Label songName = new Label("Song Name Artist Name");
		HBox names = new HBox();
		names.setPadding(new Insets(15, 12, 15, 12));
	    names.setSpacing(50);
		names.getChildren().addAll(songName);
		
		ProgressBar songProgress = new ProgressBar();
		songProgress.setScaleX(1.3);
		songProgress.setProgress(0.0);
		
		Button skip = new Button("Skip");
		Button pause = new Button("Pause");
		Button resume = new Button("Resume");
		HBox buttons = new HBox();
		buttons.setPadding(new Insets(15, 12, 15, 12));
	    buttons.setSpacing(10);
		buttons.getChildren().addAll(skip, pause, resume);
		
		vbox.getChildren().addAll(backToMenu, names, songProgress, buttons);
		BorderPane background = new BorderPane();
		background.setCenter(vbox);
		
		playSongs = new Scene(background, 500, 500);
		
		play(skip, pause, resume, songProgress, songName);
 	}
	
	public static void play(Button skip, Button pause, Button resume, ProgressBar songProgress, Label songName){
		// First, get an updated version of the song list
		ArrayList<String> list_of_songs = getCurrentSongs();
		
		// Pick a song
		int currentSong = (int) (Math.random() * list_of_songs.size());
		String currName = list_of_songs.get(currentSong);
		songName.setText(list_of_songs.get(currentSong).substring(0, currName.length() - 3));
		File f1 = new File(songsDirectory + "/" + list_of_songs.get(currentSong));
		
		// Play Song
		Media m = new Media(f1.toURI().toString());
		MediaPlayer song = new MediaPlayer(m);
		song.play();
		song.currentTimeProperty().addListener((ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) -> {
			songProgress.setProgress(song.getCurrentTime().toMillis()/song.getTotalDuration().toMillis());
		});
		
		// Button event handlers

		skip.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				song.stop();
            	play(skip, pause, resume, songProgress, songName);
			}
			
		});
		
		pause.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				song.pause();
			}
			
		});
		
		resume.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				song.setStartTime(song.getCurrentTime());
				song.play();
			}
			
		});
		
		// When song ends, play new song
		song.setOnEndOfMedia(new Runnable()
        {
            public void run() 
            {
            	// Play new song
            	play(skip, pause, resume, songProgress, songName);
            }
        });
	}
	
	public static ArrayList<String> getCurrentSongs(){
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
		ArrayList<String> songsList = getCurrentSongs();
		
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
		//String command = "cd /Users/RichSharma/Desktop/PersonalSongs ; youtube-dl -x --audio-format mp3 \"ytsearch: " + songName.getText() + " by " + artistName.getText() + " lyrics video\"";
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
			url.setText("");
			songName.setText("The given song name was not found");
			artistName.setText("");
		}
	}
	
	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage mainWindow) throws Exception {
		makeEditList(mainWindow);
		makeAddSongs(mainWindow);
		makePlaySongs(mainWindow);
		makeMenu(mainWindow);
		
		mainWindow.setScene(playSongs);
		mainWindow.show();
	}

}
