

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;

import com.jcg.hibernate.maven.Artist;
import com.jcg.hibernate.maven.RemoteDAO;

public class ArtistCreation {
	private RemoteDAO rDAO = new RemoteDAO();
	private String givenArtist1 = "Test Artist";

	private String givenArtist2 = "Test Artist2";

	private String givenArtist3 = "Test Artist3";
	
	@Test
	@DisplayName("Adding an artist into the database")
	public void createArtist() throws Exception {
		System.out.println("Adding artist");
		Artist testArtist = new Artist();
		testArtist.setArtistName(givenArtist1);
		testArtist.setArtistBio("A test bio");
		rDAO.createArtist(testArtist);
		assertEquals(givenArtist1, rDAO.searchArtist(givenArtist1).getArtistName(), "Expected to find Test Artist");
		rDAO.removeArtist(rDAO.searchArtist(givenArtist1).getArtistID());
	}
	
	@Test
	@DisplayName("Searching a specific artist from the database")
	public void searchArtist() throws Exception {
		System.out.println("Searching artist");
		Artist testArtist = new Artist();
		testArtist.setArtistName(givenArtist2);
		testArtist.setArtistBio("A test bio");
		rDAO.createArtist(testArtist);
		assertEquals(givenArtist2, rDAO.searchArtist(givenArtist2).getArtistName());
		rDAO.removeArtist(rDAO.searchArtist(givenArtist2).getArtistID());
	}
	
	@Test
	@DisplayName("Deleting artist from the database")
	public void deleteArtist() throws Exception {
		System.out.println("Deleting artist");
		Artist testArtist = new Artist();
		testArtist.setArtistName(givenArtist3);
		testArtist.setArtistBio("A test bio");
		rDAO.createArtist(testArtist);
		System.out.println("This is the artist we are removing -> "+rDAO.searchArtist(givenArtist3).getArtistName());
		rDAO.removeArtist(rDAO.searchArtist(givenArtist3).getArtistID());
		assertThrows(Exception.class, () -> {
			rDAO.searchArtist(givenArtist3).getArtistName();
		});
		
	}

}
