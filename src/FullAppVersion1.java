import java.awt.Paint;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
import javafx.geometry.Rectangle2D;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.layout.BackgroundImage;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FullAppVersion1 extends Application {
	static Scene menu, editSongs, addSongs, playSongs;
	static String songsDirectory = "/Users/RichSharma/Desktop/PersonalSongs/";
	static File playlist = new File("/Users/RichSharma/Desktop/PersonalSongs/playlist.txt");
	static ArrayList<String> songNames = getCurrentSongs();
	static double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
	static double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
	
	public static void makeMenu(Stage window){
		int height = (int) screenHeight/2;
		int width = (int) screenWidth/2;
		
		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
	    hbox.setSpacing(width/4);
	    //hbox.setStyle("-fx-background-color: #336699;");

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
	    
	    ImageView title = new ImageView(new Image("file:music_title.png"));
	    title.setFitWidth(screenWidth/2);
	    title.setFitHeight(screenHeight/4);
	    
	    BorderPane background = new BorderPane();
	    
	    BackgroundImage image = new BackgroundImage(new Image("file:MountainBackground.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(screenWidth/2, screenHeight/2, false, false, true, true));
	    Background backImage = new Background(image);
	    //Background backImage = new Background(new BackgroundImage(new Image("MountainBackground.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.AUTO);
	    
	    background.setBackground(backImage);
	    background.setCenter(hbox);
	    background.setTop(title);
	    
	    menu = new Scene(background, (1*screenWidth/2), (1*screenHeight/2));
	    
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
		int height = (int) (screenHeight * 0.5);
		int width = (int) (screenWidth * 0.4);
		
		ObservableList<String> copiedSongs = FXCollections.observableArrayList();
		for(int i = 0; i < songNames.size(); i++) copiedSongs.add(songNames.get(i));
		ListView<String> allSongs = new ListView<String>(copiedSongs);
		
		ScrollPane scrollSongs = new ScrollPane();
		scrollSongs.setContent(allSongs);
		scrollSongs.setPrefHeight(0.8 * height);
		scrollSongs.setPrefWidth(0.4 * width);
	    scrollSongs.setPadding(new Insets(10, 0, 10, 0));
		
		Button delete = new Button("Delete");
		delete.setOnAction(e -> removeSong(allSongs, allSongs.getSelectionModel().getSelectedIndex()));
		
		Button back = new Button("Back");
	    back.setPrefSize(40, 20);
	    back.setOnAction(e -> window.setScene(menu));
	    back.setAlignment(Pos.TOP_LEFT);
	    back.setPadding(new Insets(10, 0, 10, 0));
		
		HBox middleView = new HBox();
		middleView.getChildren().addAll(scrollSongs, delete);
		
		VBox wholeView = new VBox();
		wholeView.getChildren().addAll(middleView);
		
		BackgroundImage image = new BackgroundImage(new Image("file:jasper-lake.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(screenWidth/2, screenHeight/2, false, false, true, true));
		Background backImage = new Background(image);
			
		//BorderPane background = new BorderPane(wholeView);
		BorderPane background = new BorderPane();
		background.setTop(back);
	    background.setBackground(backImage);
		background.setCenter(wholeView);
	    
	    editSongs = new Scene(background, width, height);
	}
	
	public static void makeAddSongs(Stage window){
		int height = (int) (0.42 * screenHeight), width = (int) (0.42 * screenWidth);
		
		Button back = new Button("Back");
	    back.setPrefSize(50, 20);
	    back.setAlignment(Pos.TOP_LEFT);
	    back.setOnAction(e -> window.setScene(menu));
	    
	    Label instruction1 = new Label("Enter the song name here:");
		instruction1.setTextFill(Color.WHITE);
		TextField songName = new TextField("");
		
		Label instruction2 = new Label("Enter the artist name here:");
		instruction2.setTextFill(Color.WHITE);
		TextField artistName = new TextField("");

		Button add = new Button("Add");
	    add.setPrefSize(80, 20);
	    add.setOnAction(e -> addToPlaylist(songName, artistName, window));
		
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(20, 20, 20, 20));
	    vbox.setSpacing(height/10);
	    vbox.getChildren().addAll(instruction1, songName, instruction2, artistName, add);
	    
	    BackgroundImage image = new BackgroundImage(new Image("file:Another_good_image.jpg"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(screenWidth/2, screenHeight/2, false, false, true, true));
	    Background backImage = new Background(image);
	    
	    BorderPane background = new BorderPane();
	    background.setBackground(backImage);
	    background.setTop(back);
	    background.setCenter(vbox);
	    
	    addSongs = new Scene(background, width, height);
	}
	
	public static void makePlaySongs(Stage window){
		
		int height = (int) screenHeight/2, width = (int) screenWidth/2;
		
		Button backToMenu = new Button("Back");
		backToMenu.setPrefSize(60, 30);
		backToMenu.setAlignment(Pos.TOP_LEFT);
		backToMenu.setOnAction(e->window.setScene(menu));
		
		Label songName = new Label("Song Name Artist Name");
		songName.setTextFill(Color.WHITE);
		
		ProgressBar songProgress = new ProgressBar();
		songProgress.setProgress(0.0);
		songProgress.setPrefWidth(0.6*width);
		songProgress.setPadding(new Insets(height/20, 0, height/60, 0));
		
		Label progressTime = new Label("0:00");
		progressTime.setTextFill(Color.WHITE);
		progressTime.setPadding(new Insets(0, 0, 0, 0));
		
		Button skip = new Button("Skip");
		Button pause = new Button("Pause");
		Button resume = new Button("Resume");
		
		HBox buttons = new HBox(width/8);
		buttons.setPadding(new Insets(height/40, 0, 0, 0));
	    buttons.setAlignment(Pos.BASELINE_CENTER);
		buttons.getChildren().addAll(skip, pause, resume);
		
		VBox vbox = new VBox(height/20);
		vbox.setPadding(new Insets(15, 12, 15, 12));
	    vbox.setSpacing(10);
	    vbox.setAlignment(Pos.BASELINE_CENTER);
		vbox.getChildren().addAll(songName, songProgress, progressTime, buttons);

	    BackgroundImage image = new BackgroundImage(new Image("file:amazing_background.png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(screenWidth/2, screenHeight/2, false, false, true, true));
	    Background backImage = new Background(image);
		
		BorderPane background = new BorderPane();
		background.setTop(backToMenu);
	    background.setBackground(backImage);
		background.setCenter(vbox);
		
		playSongs = new Scene(background, width, height);
		
		play(skip, pause, resume, songProgress, progressTime, songName);
 	}
	
	public static void play(Button skip, Button pause, Button resume, ProgressBar songProgress, Label progressTime, Label songName){
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
			int minutes = (int) Math.floor(song.getCurrentTime().toMinutes());
			int seconds = (int) Math.floor(song.getCurrentTime().toSeconds()) % 60;
			progressTime.setText("" + minutes + ":" + (seconds < 10 ? "0" : "") + seconds);
		});
		
		// Button event handlers

		skip.setOnMouseClicked(new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent arg0) {
				song.stop();
            	play(skip, pause, resume, songProgress, progressTime, songName);
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
            	play(skip, pause, resume, songProgress, progressTime, songName);
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
	
	public static String getUrl(String song_name, String artist_name){
		String combined_strings = song_name + " by " + artist_name + " lyrics";
		combined_strings = combined_strings.replaceAll(" ", "+");
		String search_url = "https://www.youtube.com/results?search_query=" + combined_strings;
		System.out.println(combined_strings + " " + search_url);
		Document doc;
		try {
			doc = Jsoup.connect(search_url).get();
			String html = doc.html();
			return "https://www.youtube.com/" + html.substring(html.indexOf("/watch?"), html.indexOf("\"", html.indexOf("/watch?")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static void addToPlaylist(TextField songName, TextField artistName, Stage window){
		String url = getUrl(songName.getText(), artistName.getText());
		String command = "cd /Users/RichSharma/Desktop/PersonalSongs ; youtube-dl -x --audio-format mp3 " + url;

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
			
			String combined_names = songName.getText() + " " + artistName.getText();
			
			addToPlaylist.write(combined_names + ".mp3\n");
			addToPlaylist.close();
			
			songNames.add(combined_names);
			
			songName = new TextField("Enter song name here");
			artistName = new TextField("Enter artist name here");
			
			makeEditList(window);
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
