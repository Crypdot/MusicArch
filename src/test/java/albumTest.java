

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.jcg.hibernate.maven.Album;
import com.jcg.hibernate.maven.Artist;
import com.jcg.hibernate.maven.Genre;
import com.jcg.hibernate.maven.RemoteDAO;

import controller.Controller;

public class albumTest {
	private RemoteDAO rDAO = new RemoteDAO();
	private String givenAlbumTitle = "Test Album";
	private String artistName = "Jenkins Artist";
	private String artistBio = "Jenkins' personal little artist!";
	private String genreName = "A really cool Jenkins Genre!";
	
	@Test
	@Disabled
	@DisplayName("Adding an album to the database")
	public void createAlbum() throws Exception {
		Controller controller = new Controller();
		
		controller.createArtist(artistName, artistBio);
		controller.createGenre(genreName);
		
		String[] testGenreList = {genreName};
		String[] testArtistList = {artistName};
		
		Album testAlbum = new Album();
		testAlbum.setAlbumName(givenAlbumTitle);
		testAlbum.setAlbumYear(1234);
		
		controller.createAlbum(givenAlbumTitle, 1234, testGenreList, testArtistList, testArtistList);
		assertEquals(givenAlbumTitle, rDAO.searchAlbum(givenAlbumTitle).getAlbumName(), "Expected to find Test Album");	
		rDAO.removeAlbum(rDAO.searchAlbum(givenAlbumTitle).getAlbumID());
	}
	
	@Test
	@DisplayName("Searching for a specific album from the database")
	public void searchAlbum() throws Exception{
		Controller controller = new Controller();
		
		controller.createArtist(artistName, artistBio);
		controller.createGenre(genreName);
		
		String[] testGenreList = {genreName};
		String[] testArtistList = {artistName};
		
		Album testAlbum = new Album();
		testAlbum.setAlbumName(givenAlbumTitle);
		testAlbum.setAlbumYear(1234);
		
		controller.createAlbum(givenAlbumTitle, 1234, testGenreList, testArtistList, testArtistList);
		assertEquals(givenAlbumTitle, rDAO.searchAlbum(givenAlbumTitle).getAlbumName(), "Expected to find Test Album");
		rDAO.removeAlbum(rDAO.searchAlbum(givenAlbumTitle).getAlbumID());
	}
	
	@Test
	@DisplayName("Deleting a specific album from the database")
	public void deleteArtist() throws Exception {
		Controller controller = new Controller();
		
		controller.createArtist(artistName, artistBio);
		controller.createGenre(genreName);
		
		String[] testGenreList = {genreName};
		String[] testArtistList = {artistName};
		
		Album testAlbum = new Album();
		testAlbum.setAlbumName(givenAlbumTitle);
		testAlbum.setAlbumYear(1234);
		
		controller.createAlbum(givenAlbumTitle, 1234, testGenreList, testArtistList, testArtistList);
		System.out.println("This is the Album we are deleting -> "+rDAO.searchAlbum(givenAlbumTitle).getAlbumName());
		rDAO.removeAlbum(rDAO.searchAlbum(givenAlbumTitle).getAlbumID());
		assertThrows(Exception.class, () -> {
			rDAO.searchAlbum(givenAlbumTitle).getAlbumName();
		});
	}

}
