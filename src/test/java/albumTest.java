

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jcg.hibernate.maven.Album;
import com.jcg.hibernate.maven.Artist;
import com.jcg.hibernate.maven.Genre;
import com.jcg.hibernate.maven.RemoteDAO;

import controller.Controller;

public class albumTest {
	private String givenAlbumTitle = "Test Album";
	private String artistName = "Jenkins Artist";
	private String artistBio = "Jenkins' personal little artist!";
	private String genreName = "A really cool Jenkins Genre!";
	private String[] songNames = {"Song 1", "Song 2", "Song 3"};
	private Controller controller = new Controller();
	
	@BeforeEach
	public void beforeEach() {
		try {
			controller.removeAlbum(controller.searchAlbum(givenAlbumTitle).getAlbumID());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	@DisplayName("Adding an album to the database")
	public void createAlbum() throws Exception {
		controller.createArtist(artistName, artistBio);
		controller.createGenre(genreName);
		
		String[] testGenreList = {genreName};
		String[] testArtistList = {artistName};
		
		Album testAlbum = new Album();
		testAlbum.setAlbumName(givenAlbumTitle);
		testAlbum.setAlbumYear(1234);
		
		controller.createAlbum(givenAlbumTitle, 1234, testGenreList, testArtistList, songNames);
		assertEquals(givenAlbumTitle, controller.searchAlbum(givenAlbumTitle).getAlbumName(), "Expected to find Test Album");	
		controller.removeAlbum(controller.searchAlbum(givenAlbumTitle).getAlbumID());
	}
	
	@Test
	@DisplayName("Searching for a specific album from the database")
	public void searchAlbum() throws Exception{
		controller.createArtist(artistName, artistBio);
		controller.createGenre(genreName);
		
		String[] testGenreList = {genreName};
		String[] testArtistList = {artistName};
		
		Album testAlbum = new Album();
		testAlbum.setAlbumName(givenAlbumTitle);
		testAlbum.setAlbumYear(1234);
		
		controller.createAlbum(givenAlbumTitle, 1234, testGenreList, testArtistList, songNames);
		assertEquals(givenAlbumTitle, controller.searchAlbum(givenAlbumTitle).getAlbumName(), "Expected to find Test Album");
		controller.removeAlbum(controller.searchAlbum(givenAlbumTitle).getAlbumID());
	}
	
	@Test
	@DisplayName("Deleting a specific album from the database")
	public void deleteArtist() throws Exception {
		controller.createArtist(artistName, artistBio);
		controller.createGenre(genreName);
		
		String[] testGenreList = {genreName};
		String[] testArtistList = {artistName};
		
		Album testAlbum = new Album();
		testAlbum.setAlbumName(givenAlbumTitle);
		testAlbum.setAlbumYear(1234);
		
		controller.createAlbum(givenAlbumTitle, 1234, testGenreList, testArtistList, songNames);
		controller.removeAlbum(controller.searchAlbum(givenAlbumTitle).getAlbumID());
		assertThrows(Exception.class, () -> {
			controller.searchAlbum(givenAlbumTitle).getAlbumName();
		});
	}

}
