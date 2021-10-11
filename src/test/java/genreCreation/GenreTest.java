package genreCreation;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import com.jcg.hibernate.maven.Genre;
import com.jcg.hibernate.maven.RemoteDAO;


public class GenreTest {

	private RemoteDAO rDAO = new RemoteDAO();
	private String givenGenre = "TestGenre";
	
	@Test
	@Order(3)
	@DisplayName("Delete a specific Genre from database")
	public void deleteGenre() throws Exception{
		System.out.println("NOW DOING THE DELETION TEST");
		Genre testGenre = new Genre();
		testGenre.setGenreName(givenGenre);
		rDAO.createGenre(testGenre);
		int id = rDAO.searchGenre(givenGenre).getGenreID();
		rDAO.removeGenre(id);
		assertThrows(Exception.class, () ->{
			rDAO.searchGenre(givenGenre).getGenreName();
		});
	}
	
    @Test
    @Order(2)
	@DisplayName("Searching a specific Genre from database")
	public void searchGenre() throws Exception {
		System.out.println("NOW DOING THE SEARCH TEST");
		Genre testGenre = new Genre();
		testGenre.setGenreName("TestGenre");
		rDAO.createGenre(testGenre);
		assertEquals(givenGenre, rDAO.searchGenre(givenGenre).getGenreName());
		rDAO.removeGenre(rDAO.searchGenre(givenGenre).getGenreID());
    }
	
	@Test
	@Order(1)
	@DisplayName("Add Genre into database")
	public void createGenre() throws Exception {
		System.out.println("NOW DOING THE CREATION TEST");
		Genre testGenre = new Genre();
		testGenre.setGenreName("TestGenre");
		rDAO.createGenre(testGenre);
		assertEquals(givenGenre, rDAO.searchGenre(givenGenre).getGenreName(), "Expected to find TestGenre!");
		rDAO.removeGenre(rDAO.searchGenre(givenGenre).getGenreID());
	}
	
}
