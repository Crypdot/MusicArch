package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import com.jcg.hibernate.maven.Artist;
import com.jcg.hibernate.maven.Album;
import com.jcg.hibernate.maven.Genre;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.jcg.hibernate.maven.Song;
import view.*;

public class GUIController {
	private static Controller controller;
	private View view;
	private double pauseDuration = 0.2;
	private Genre genreResults;
	private Artist artistResults;
	private Album albumResults;
	private Song songResults;
	List<String> everythingFound;

	@FXML
    private ContextMenu searchContext;
	@FXML
	private AnchorPane UserCategories;
	@FXML
	public TitledPane UserGenreDrop;
	@FXML
	private TitledPane UserAlbumDrop;
	@FXML
	private TitledPane UserArtistDrop;
	@FXML
	private Button SearchButton;
	@FXML 
	private TextField SearchBox;
	@FXML
	private GridPane gridView;
	@FXML 
	private Button gridButton;
	@FXML
	private AnchorPane Keskikohta;
	@FXML
	private Button Requests;
	@FXML
	private Button Help;
	@FXML
	private Button FrontPage;
	@FXML
	private Button UserCollection;

	
	public GUIController() {}
	
	public GUIController(View view, Controller controller) {
		this.view = view;
		this.controller = controller;
	}
	@FXML
	void SearchTxt(ActionEvent event) {

	}
	
	//-----Page changes, window creation calls and page updates---
	//RootLayout2s menubuttons	
	public void goFrontPage() throws IOException {
		View.showFrontPage();
	}

	@FXML
	void ShowRequests(ActionEvent event) throws IOException {
		view.showRequestsWindow();
	}

	@FXML
	void GoHelpPage(ActionEvent event) throws IOException {
		view.showHelpPage();

	}
	
	@FXML
	void goUserCollection(ActionEvent event) throws IOException {
		
		
		System.out.println("UserGenreDrop = "+UserGenreDrop);
		view.showUserCollectionPage();
	}
	
	@FXML
	void goSearchPage(ActionEvent event) throws IOException {
		String searchText = SearchBox.getText();
		view.showSearchPage(searchText);
	}
	/*
	 * Upon clicking the search bar, this method is triggered, and will fetch
	 * all names found within the database.
	 */

	
	 @FXML
	 void getSearchable(MouseEvent event) {
		 if(everythingFound == null) {
			 everythingFound = new ArrayList<>();
		 }
		 everythingFound = controller.getSearchable();
		 System.out.println("Fetched all the searchable values");
    }
	/*
	 * refreshSearchList-method is triggered by keypresses in the search-bar.
	 * Each time a new key is pressed, the method will compare the user's input
	 * against a list of all names in the database. 
	 * Upon finding a match, it will create a list of MenuItems inside the ContextMenu,
	 * which will autofill the search bar upon being clicked, and automatically
	 * trigger a search inside the program
	 */
	 @FXML
	 void refreshSearchList(KeyEvent event) {
		 
		int menuCounter = 0;
		List<String> strippedList = new ArrayList<String>();
		SearchBox.setContextMenu(searchContext);	
		searchContext.show(SearchBox, null, pauseDuration, 50);	
		
		for(int i = 0; i<everythingFound.size(); i++) {		
			if(everythingFound.get(i).toLowerCase().contains(SearchBox.getText().toLowerCase())) {
				strippedList.add(everythingFound.get(i));
				menuCounter++;
			}
		}
		searchContext.getItems().clear();
		if(menuCounter > 5) {
			menuCounter = 4;
		}
		for(int i = 0; i<menuCounter; i++) {
			if(SearchBox.getText().isEmpty()) {
				break;
			}
			String testString = strippedList.get(i);
			MenuItem searchItem = new MenuItem(testString);
			searchItem.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent t) {
					SearchBox.setText(searchItem.getText());
					try {
						view.showSearchPage(SearchBox.getText());
						SearchBox.clear();
					}catch(IOException e1) {
						System.out.println("Failed to commit a search in GUIController's refreshSearchList method!");
						e1.printStackTrace();
					}	
				}
			});
			System.out.println("Added a new menu item -> "+searchItem.getText());
			searchContext.getItems().add(searchItem);
		}
	 }
	
	//----Search results from controller------
	
	public void setGenreResults(Genre genre) {
		this.genreResults = genre;
	}
	public Genre getGenreResults() {
		return genreResults;
	}
	public void setArtistResults(Artist artistResults) {
		this.artistResults = artistResults;
	}
	public Artist getArtistResults(){
		return artistResults;
	}
	public void setAlbumResults(Album album) {
		this.albumResults = album;
	}
	public Album getAlbumResults(){
		return albumResults;
	}
	public void setSongResults(Song song) {
		this.songResults = song;
	}
	public Song getSongResults(){
		return songResults;
	}
 
}

