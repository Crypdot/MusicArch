package controller;

import com.jcg.hibernate.maven.Album;
import com.jcg.hibernate.maven.Artist;
import com.jcg.hibernate.maven.Genre;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class SearchController{
	
	private String search;
	private Controller controller;
	
	@FXML
	private Label SearchLabel;
	
	@FXML
	private GridPane SearchGrid;
	
    @FXML
    private Label ArtistLabel;

    @FXML
    private Label GenreLabel;

    @FXML
    private Label AlbumLabel;
	
	public SearchController (String search, Controller controller) {
		this.search = search;
		this.controller = controller;
	}
	
	
	/**
	 * This initialize is for setting up the SearchPage with the search results
	 * **/
	@FXML
	protected void initialize() {
		try {
			Artist artistResults = controller.searchArtist(search);
			SearchGrid.add(new Text(artistResults.getArtistName() + " löytyi"), 0, 0);
			
		}catch (Exception e) {
			SearchGrid.add(new Text("Artistia " + this.search + " ei löytynyt"), 0, 0);
			System.out.println(e.getMessage());
		}
		
		try {
			Genre genreResults = controller.searchGenre(search);
			SearchGrid.add(new Text(genreResults.getGenreName() + " löytyi"), 0, 1);
		 
		}catch (Exception e) {
			SearchGrid.add(new Text("Genreä " + this.search + " ei löytynyt"), 0, 1);
			System.out.println(e.getMessage());
		}
		try {
			
			Album albumResults = controller.searchAlbum(search);
			SearchGrid.add(new Text(albumResults.getAlbumName() + " löytyi"), 0, 2);
			
			System.out.println("Search = " + search);
		}catch (Exception e) {
			SearchGrid.add(new Text("Albumia " + this.search + " ei löytynyt"), 0, 2);
			System.out.println(e.getMessage());
		}
		
		
	}


}
