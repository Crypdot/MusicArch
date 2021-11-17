package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.LocalAlbum;
import model.LocalArtist;
import model.LocalGenre;
import model.LocalSong;
import view.View;


import com.jcg.hibernate.maven.Genre;

import java.util.List;


public class UserCollectionController {
	
	private Controller controller;
	private View view;

    @FXML
    private Label UserLabel;

    @FXML
    private AnchorPane UserCategories;
    
    @FXML
    private TitledPane UserAlbumDrop;
    
    @FXML
    private TabPane UserTabPane; 

    @FXML
    private ListView<LocalAlbum> GridListView;
    
    @FXML
    private GridPane UserGrid;
    
    @FXML
    private ListView<LocalSong> SongListView;
    

    @FXML
    private Label AlbumNameLabel;

    @FXML
    private Label AlbumArtistLabel;

    @FXML
    private Label AlbumGenreLabel;
    
    public UserCollectionController(Controller controller) {

    	this.controller = controller;
    }
    
    /**
	 * This initialize method is for setting up the OmaKokoelma2 with data from then local database.
	 * It also gives all the list items clickEvenetListeners for album info.
	 * **/
    @FXML
    protected void initialize() {
    	
    	LocalAlbum[] albumList;
    	try {
    		
    		albumList = controller.getLocalAlbums();
    		ObservableList<LocalAlbum> choices = FXCollections.observableArrayList(albumList);
    		for(LocalAlbum album : albumList) {
    			System.out.println(album);
    		}
    		
    		GridListView.setCellFactory(lv -> new ListCell<LocalAlbum>() {
    			@Override
    			protected void updateItem(LocalAlbum localAlbum, boolean empty) {
    				super.updateItem(localAlbum, empty);
    				setText(empty || localAlbum == null || albumList.length == 0 ? "" : localAlbum.getAlbumName());
    			}
    		});			
    		
    		GridListView.setItems(choices);
    		
    		GridListView.setOnMouseClicked(me -> {
    			
    			LocalAlbum listLocalAlbum = GridListView.getSelectionModel().getSelectedItem();
    			List<LocalGenre> localGenre = controller.getLocalAlbumGenres(listLocalAlbum.getAlbumID());
    			List<LocalArtist> localArtist = controller.getLocalAlbumArtists(listLocalAlbum.getAlbumID());
    			if(listLocalAlbum != null) {
    				AlbumNameLabel.setText(listLocalAlbum.getAlbumName()); 				
    			}
    			if(localGenre.size() > 0) {
    				AlbumGenreLabel.setText(localGenre.get(0).getGenreName());
    				System.out.println(listLocalAlbum.getAlbumName());
    			} else {
    				AlbumGenreLabel.setText("Ei löytynyt genrejä");
    			}
    			if(localArtist.size() > 0) {
    				AlbumArtistLabel.setText(localArtist.get(0).getArtistName());
    			} else {
    				AlbumArtistLabel.setText("Ei löytynyt artisteja");
    			}
    			List<LocalSong> localSongs = controller.getLocalAlbumSongs(listLocalAlbum.getAlbumID());
    			ObservableList<LocalSong> observableSongs = FXCollections.observableArrayList(localSongs);
    			
        		SongListView.setCellFactory(lv -> new ListCell<LocalSong>() {
        			@Override
        			protected void updateItem(LocalSong localSong, boolean empty) {
        				super.updateItem(localSong, empty);
        				setText(empty || localSong == null || localSongs.size() == 0 ? "" : localSong.getSongName());
        			}
        		});		
    			SongListView.setItems(observableSongs);
    		});
    		
    		
    	} catch(Exception e) {
    		System.out.println(e.getMessage() + " usercollection init");
    	}

    }
    @FXML
    void getDetails() {
    	LocalAlbum localAlbum = GridListView.getSelectionModel().getSelectedItem();
    	System.out.println("Album id from listItem" + localAlbum.getAlbumID());
    }

    @FXML
    void Album(MouseEvent event) {
	UserLabel.setText("Muutu");
    	
    	try {
    		UserGrid.getChildren().clear();
    		Genre[] genreList = controller.getGenres();
    		int counter = 0;
    		System.out.println(UserGrid + " UserGrid????");
    		for(Genre genre : genreList) {
    			System.out.println(genre.getGenreName());
    			Text text = new Text(genre.getGenreName());
    			UserGrid.add(text, 0, counter);
    			counter++;
    		}
    	}catch (Exception e) {
    		System.out.println("Miksiiii" + e.getMessage());
    	}
    }


}
