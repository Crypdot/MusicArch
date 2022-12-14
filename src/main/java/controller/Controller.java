package controller;

import java.util.*;

import com.jcg.hibernate.maven.Album;
import com.jcg.hibernate.maven.Artist;
import com.jcg.hibernate.maven.Genre;
import com.jcg.hibernate.maven.LocalDAO;
import com.jcg.hibernate.maven.RemoteDAO;
import com.jcg.hibernate.maven.Song;
import com.jcg.hibernate.maven.UserRequests;

import model.*;

/**
 * Controller is used to build objects and relay them between the View-layer and both the local- and remote databases.
 * @author Alex, Jani, Jemila
 */
public class Controller {

	private RemoteDAO remoteDAO = RemoteDAO.getInstance();
	private LocalDAO localDAO = new LocalDAO();
	private GUIController GUIController = new GUIController();

    public Controller() {}
    /**
     * Creates a Genre inside the database with a given String as it's name
     * @param genreName The name of a Genre to be created
     * @throws Exception In case the operation was unsuccessful, the method will throw an exception and interrupt the operation
     */
    public void createGenre(String genreName) throws Exception {
    	Genre newGenre = new Genre();
    	newGenre.setGenreName(genreName);
    	try {
			remoteDAO.createGenre(newGenre);
		} catch (Exception e) {
			System.out.println("Failed to create a genre! ");
			e.printStackTrace();
			throw e;

		}
    }
    /**
     * Creates an Artist object, which it sends to the remote database.
     * @param artistName The name of the Artist to be created
     * @param artistBio The biography of the Artist to be created
     * @throws Exception In case the operation was unsuccessful, the method will throw an exception and interrupt the operation
     */
    public void createArtist(String artistName, String artistBio) throws Exception {
    	Artist newArtist = new Artist();
    	newArtist.setArtistName(artistName);
    	newArtist.setArtistBio(artistBio);
    	try {
			remoteDAO.createArtist(newArtist);
		} catch (Exception e) {
			System.out.println("Failed to create an artist! ");
			e.printStackTrace();
			throw e;
		}
    }
    /**
     * Creates an Album object with the given parameters, which it sends to the remote database. 
     * @param albumName The name of the Album to be created
     * @param albumYear The release year of the Album to be created
     * @param genreListGiven A list of Genres related to the Album
     * @param artistListGiven A list of Artists related to the Album
     * @param songListGiven A list of Songs related to the Album
     * @throws Exception In case the operation was unsuccessful, the method will throw an exception and interrupt the operation
     */
    public void createAlbum(String albumName, int albumYear, String[] genreListGiven, String[] artistListGiven, String[] songListGiven) throws Exception {
    	
    	Album newAlbum = new Album();
    	newAlbum.setAlbumName(albumName);
    	newAlbum.setAlbumYear(albumYear);
    	
    	Genre genre = new Genre();
    	Artist artist = new Artist();
    	Set<Artist> linkArtist = new HashSet<>();
    	Set<Genre> linkGenre = new HashSet<>();
    	Set<Song> linkSong = new HashSet<>();

    	if(genreListGiven.length != 0 || artistListGiven.length != 0) {
    		for(int i = 0; i<genreListGiven.length; i++) {
				try {			
					genre = (Genre) remoteDAO.searchGenre(genreListGiven[i]);
					if(genre != null) {
						linkGenre.add(genre);
					}
				} catch (Exception e) {
					System.out.println("Failed to add a Genre to Album!");
					System.out.println(e.getMessage());
				}
        	}
    		
    		for(int i = 0; i<artistListGiven.length; i++) {
				try {
					artist = (Artist) remoteDAO.searchArtist(artistListGiven[i]);
					if(artist != null) {
						System.out.println("ArtistID " + artist.getArtistID());
						linkArtist.add(artist);
					}
				} catch (Exception e) {
					System.out.println("Failed to add an Artist to Album!");
					e.printStackTrace();
				}
    			
    		}
    		
    		for(int i = 0; i<songListGiven.length; i++) {
					Song song = new Song();
					song.setSongName(songListGiven[i]);
					linkSong.add(song);    		
					System.out.println("Added song IN ORDER > "+song.getSongName());
    		}
    		try {
    			remoteDAO.createAlbum(newAlbum, linkArtist, linkGenre, linkSong);	
			} catch (Exception e) {
				System.out.println("Failed to create an Album!");
				e.printStackTrace();
				throw e;
			}
    	}
    	else {
    		System.out.println("Can't create album without artists or genres!");
    	}
    	
    }
    /**
     * Creates a Local Artist object with the given parameters, and sends it to the local database
     * @param ArtistID The ID of an Artist used to locate it from the database
     * @param artistName The name of an Artist
     * @param artistBio The biography of an Artist
     * @throws Exception In case the operation was unsuccessful, the method will throw an exception and interrupt the operation
     */ 
    public void createLocalArtist(int ArtistID, String artistName, String artistBio) throws Exception {
    	LocalArtist localArtist = new LocalArtist();
    	Artist remoteartist = remoteDAO.searchArtist(artistName);
    	
    	localArtist.setArtistID(ArtistID);
    	localArtist.setArtistName(artistName);
    	localArtist.setArtistBio(artistBio);

    	localDAO.createArtist(localArtist);
    }
    /**
     * Creates a Local Genre object with the given parameters, and sends it to the local database
     * @param genreID The ID of a Genre used to locate it from the database
     * @param genreName The name of a Genre
     * @throws Exception In case the operation was unsuccessful, the method will throw an exception and interrupt the operation
     */
    public void createLocalGenre(int genreID, String genreName) throws Exception {
    	LocalGenre localGenre = new LocalGenre();
    	LocalGenre remoteGenre = localDAO.searchGenre(genreName);
    	
    	localGenre.setGenreID(genreID);
    	localGenre.setGenreName(genreName);

    	localDAO.createGenre(localGenre);
    }
    /**
     * Creates a Local Album object with the given parameters, which it sends to the local database. 
     * @param albumName The name of the Album to be created
     * @param albumYear The release year of the Album to be created
     * @param genreListGiven A list of Genres related to the Album
     * @param artistListGiven A list of Artists related to the Album
     * @param songListGiven A list of Songs related to the Album
     * @throws Exception In case the operation was unsuccessful, the method will throw an exception and interrupt the operation
     */
    public void createLocalAlbum(int albumID, String albumName, Set<Song> songListGiven, int albumYear, Set<Genre> genreListGiven, Set<Artist> artistListGiven ) throws Exception {
    	LocalAlbum newAlbum = new LocalAlbum();
    	LocalSong[] songList = new LocalSong[songListGiven.size()];
    	LocalArtist[] artistList = new LocalArtist[artistListGiven.size()];
    	LocalGenre[] genreList = new LocalGenre[genreListGiven.size()];
    	System.out.println("Before songListGiven");
    	int counter = 0;
    	for(Song song : songListGiven) {
    		LocalSong localSong = new LocalSong();
    		localSong.setSongName(song.getSongName());
    		localSong.setSongID(song.getSongID());
    		songList[counter] = localSong;
    		counter++;
    	}
    	
    	counter = 0;
    	System.out.println("before ArtistListGiven");
    	for(Artist artist : artistListGiven) {
    		System.out.println("items: " + artist.getArtistName());
    		LocalArtist localArtist = new LocalArtist();
    		localArtist.setArtistName(artist.getArtistName());
    		localArtist.setArtistID(artist.getArtistID());
    		localArtist.setArtistBio(artist.getArtistBio());
    		artistList[counter] = localArtist;
    		counter++;
    	}
    	
    	counter = 0;
    	for(Genre genre : genreListGiven) {
    		LocalGenre localGenre = new LocalGenre();
    		localGenre.setGenreName(genre.getGenreName());
    		localGenre.setGenreID(genre.getGenreID());
    		genreList[counter] = localGenre;
    		counter++;
    	}
    	for(LocalArtist localArtist : artistList) {
    		System.out.println(localArtist.getArtistName());
    	}
    	newAlbum.setAlbumName(albumName);
    	newAlbum.setAlbumYear(albumYear);
    	newAlbum.setAlbumID(albumID);
    	newAlbum.setAlbumDescription("");
    	System.out.println("before localDAO.createAlbum ");
    	localDAO.createAlbum(newAlbum, songList, artistList, genreList);
    }
    /**
     * Returns an Album based on a given albumID search from the remote database
     * @param albumID an ID used to search for a specific Album
     * @return
     */
    public Album getAlbum(int albumID) {
    	return remoteDAO.readAlbum(albumID);
    }
    /**
     * Returns a set of Artists related to a singular Album based on the Album's ID
     * @param albumID an ID used to locate an Album's Artists
     * @return list
     */
    public Set<Artist> getAlbumArtistList(int albumID) {
    	Set<Artist> list = remoteDAO.albumArtistList(albumID);
    	if(list != null) {
    		
    		System.out.println("Lista artisteista" + list.size());
    	} else {
    		System.out.println("Ei ole null???");
    	}
    	return list;
//    	return remoteDAO.albumArtistList(albumID);
    }
    /**
     * Returns a set of Genres related to a singular Album based on the Album's ID
     * @param albumID ID used to locate the Album's Genres
     * @return 
     */
    public Set<Genre> getAlbumGenreList(int albumID){
    	return remoteDAO.albumGenreList(albumID);
    }
    /**
     * Creates a Genre object based on given parameters, and sends an edit request to the remote database
     * @param genreID ID used to locate the editable Genre
     * @param genreName The new name of a Genre
     */
    public void editGenre(int genreID, String genreName) {
    	Genre editGenre = new Genre();
    	editGenre.setGenreName(genreName);
    	remoteDAO.editGenre(editGenre, genreID);
    }
    /**
     * Creates an Artist object based on given parameters, and sends an edit request to the remote database
     * @param artistID ID used to locate the editable Artist
     * @param artistName The new name of an Artist
     */
    public void editArtist(int artistID, String artistName) {
    	Artist editArtist = new Artist();
    	editArtist.setArtistName(artistName);
    	remoteDAO.editArtist(editArtist, artistID);
    }
    /**
     * Creates an Album object based on given parameters, and sends an edit request to the remote database
     * @param albumID ID used to locate the editable Album
     * @param albumName New Album's name
     * @param albumYear New Album's release year
     * @param artistListEdit New Album's Artists
     * @param genreListEdit New Album's Genres
     */
    public void editAlbum(int albumID, String albumName, int albumYear, String[] artistListEdit, String[] genreListEdit) {
    	Album editAlbum = new Album();
    	
    	editAlbum.setAlbumName(albumName);
    	editAlbum.setAlbumYear(albumYear);
    	
    	if(genreListEdit.length != 0 || artistListEdit.length != 0) {
    		for(int i = 0; i < genreListEdit.length; i++) {
    			try {
    				Genre genre = remoteDAO.searchGenre(genreListEdit[i]);
    				if(genre != null) {
    					editAlbum.addGenre(genre);
    				}
    			}catch(Exception e) {
    				System.out.println("Failed to add a genre to the editable list! (in Controller's editAlbum() method! Error message -> "+e.getMessage());
    			}
    		}
    		for(int i = 0; i < artistListEdit.length; i++) {
    			try {
    				Artist artist = remoteDAO.searchArtist(artistListEdit[i]);
    				if(artist != null) {
    					editAlbum.addArtist(artist);
    				}
    			}catch(Exception e) {
    				System.out.println("Failed to add an artist to the editable list! (In Controller's editAlbum() method! Error message -> "+e.getMessage());
    			}
    		}
    		
    		
    	}
    	try {
			remoteDAO.editAlbum(albumID, editAlbum);
		} catch (Exception e) {
			System.out.println("Whoops");
			e.printStackTrace();
		}
    }
    /**
     * Creates a Local Genre object based on given parameters, and sends an edit request to the local database
     * @param genreID ID used to locate the Genre from the local Genre
     * @param genreName The name of the Genre-object
     */
    public void editLocalGenre(String genreID, String genreName) {
    	LocalGenre editLocalGenre = new LocalGenre();
    	int editID = Integer.parseInt(genreID);
    	editLocalGenre.setGenreName(genreName);
    	localDAO.editGenre(editLocalGenre, editID);
    }

    /**
     * Creates a Local Album object based on given parameters, and sends an edit request to the local database
     * @param albumID ID set for the Album within the local database
     * @param albumName The name to be set for the Album
     * @param songListGiven The list of Songs to be set for the Album
     * @param albumYear The Album's year
     */
    public void editLocalAlbum(String albumID, String albumName, Song[] songListGiven, int albumYear) {
    	LocalAlbum editLocalAlbum = new LocalAlbum();
    	int editID = Integer.parseInt(albumID);
    	editLocalAlbum.setAlbumName(albumName);
    	editLocalAlbum.setAlbumYear(albumYear);
    	localDAO.editAlbum(editLocalAlbum, null, editID);
    }
    /**
     * Edits a Local Album's description field using a given Local Album
     * @param localAlbum Local Album to be edited
     */
    public void editLocalAlbumDescription(LocalAlbum localAlbum) {
    	localDAO.editLocalAlbumDescription(localAlbum);
    }
    /**
     * Returns a Local Album's description based on a given Local Album's ID.
     * @param id The ID used to locate a specific Local Album's data
     * @return
     */
    public String getLocalAlbumDescription(int id) {
    	return localDAO.getLocalAlbumDescription(id);
    	
    }
    /**
     * Sends a Genre removal request to the remote database with a given Genre's ID as its parameter
     * @param genreID ID used to locate the Genre to be removed
     */
    public void removeGenre(int genreID) {
    	remoteDAO.removeGenre(genreID);
    }
    /**
     * Sends a Artist removal request to the remote database with a given Artist's ID as its parameter
     * @param artistID ID used to locate the Artist to be removed
     */
    public void removeArtist(int artistID) {
    	remoteDAO.removeArtist(artistID);
    }
    /**
     * Sends a Album removal request to the remote database with a given Album's ID as its parameter
     * @param albumID ID used to locate the Album to be removed
     */
    public void removeAlbum(int albumID) {
    	remoteDAO.removeAlbum(albumID);
    }
    /**
     * Sends a Local Genre removal request to the remote database with a given Genre's ID as its parameter
     * @param genreID ID used to locate the Local Genre to be removed
     */
    public void removeLocalGenre(int genreID) {
    	localDAO.removeGenre(genreID);
    }
    /**
     * Sends a Local Artist removal request to the remote database with a given Artist's ID as its parameter
     * @param artistID ID used to locate the Local Artist to be removed
     */
    public void removeLocalArtist(int artistID) {
    	localDAO.removeArtist(artistID);
    }
    /**
     * Sends a Local Album removal request to the remote database with a given Album's ID as its parameter
     * @param albumID ID used to locate the Local Album to be removed
     */
    public void removeLocalAlbum(int albumID) {
    	localDAO.removeAlbum(albumID);
    }
    /**
     * Returns a remote database search on Genres based on a given parameter as the search key
     * @param genreName Search parameter for searching the remote database
     * @return
     * @throws Exception In case the operation was unsuccessful, the method will throw an exception and interrupt the operation
     */
    public Genre searchGenre(String genreName) throws Exception {
			 return remoteDAO.searchGenre(genreName);
    }
    /**
     * Returns a remote database search on Artists based on a given parameter as the search key
     * @param artistName Search parameter for searching the remote database
     * @return
     * @throws Exception In case the operation was unsuccessful, the method will throw an exception and interrupt the operation
     */
    public Artist searchArtist(String artistName) throws Exception { 	
    		return remoteDAO.searchArtist(artistName);
	
    }
    /**
     * Returns a remote database search on Albums based on a given parameter as the search key
     * @param albumName Search parameter for searching the remote database
     * @return
     * @throws Exception In case the operation was unsuccessful, the method will throw an exception and interrupt the operation
     */
    public Album searchAlbum(String albumName) throws Exception {
			return remoteDAO.searchAlbum(albumName);
    }
    /**
     * Returns a table of every Genre found within the database
     * @return
     */
    public Genre[] getGenres() {
    	return remoteDAO.readGenres();
    }
    /**
     * Returns a table of every Artist found within the database
     * @return
     */
    public Artist[] getArtists() {
    	return remoteDAO.readArtists();
    }
    /**
     * Returns a table of every Album found within the database
     * @return
     */
    public Album[] getAlbums() {
    	return remoteDAO.readAlbums();
    }
    /**
     * Returns a table of every Song found within the database
     * @return
     */
    public Song[] getSongs() {
    	return remoteDAO.readSongs();
    }
    /**
     * Returns a list of Local Artists based on a given parameter as the search key
     * @param search Search parameter for searching the local database
     * @return
     * @throws Exception In case the operation was unsuccessful, the method will throw an exception and interrupt the operation
     */
    public List<LocalArtist> getLocalArtist(String search) throws Exception {
    	return localDAO.searchArtist(search);
    }
    
    /**
     * Returns a list of Local Albums based on a given parameter as the search key
     * @param albumName Search parameter for searching the local database
     * @return
     * @throws Exception In case the operation was unsuccessful, the method will throw an exception and interrupt the operation
     */
    public LocalAlbum searchLocalAlbum(String albumName) throws Exception {
		return localDAO.searchAlbum(albumName);
    }
    /**
     * Returns a table of Local Genres based on a given parameter as the search 
     * @return
     */
    public LocalGenre[] getLocalGenres() {
    	return localDAO.readGenres();
    }
    /**
     * Returns a table of Local Albums based on a given parameter as the search 
     * @return
     */
    public LocalAlbum[] getLocalAlbums() {
    	return localDAO.readAlbums();
    }
    /**
     * Returns a table of Local Artists based on a given parameter as the search 
     * @return
     */
    public LocalArtist[] readArtists() {
    	return localDAO.readArtists();
    }
    /**
     * Returns a Local Album from the local database based on a given Album ID
     * @param id ID used to find a Local Album from the local database
     * @return
     * @throws Exception In case the operation was unsuccessful, the method will throw an exception and interrupt the operation
     */
    public LocalAlbum readLocalAlbum(int id) throws Exception {
    	return localDAO.readAlbum(id);
    }

    /**
     * Returns a list of all Genre- Artist- Album and Song names found within the remote database, to implement predictive searching.
     * @return
     */

    public List<String> getSearchable() {
    	return remoteDAO.getSearchable();
    }
    /**
     * Returns a list of all Artist names found within the remote database, to further implement focused searching
     * @return
     */
    public List<String>getSearchableArtists() {
    	return remoteDAO.existingArtists();
    }
    /**
     * Returns a list of all Album names found within the remote database, to further implement focused searching
     * @return
     */
    public List<String> getSearchableAlbums(){
    	return remoteDAO.existingAlbums();
    }
    /**
     * Returns a list of all Genre names found within the remote database, to further implement focused searching
     * @return
     */
    public List<String> getSearchableGenres(){
    	return remoteDAO.existingGenres();
    }
    /**
     * Returns a list of all Song names found within the remote database, to further implement focused searching
     * @return
     */
    public List<String> getSearchableSongs(){
    	return remoteDAO.existingSongs();
    }
    /**
     * Returns a list of all Albums related to a given Genre's ID from the remote database
     * @param genreID ID used to find all Albums related to the Genre from the database
     * @return
     */
    public List<Album> getGenreAlbums(int genreID){
    	return remoteDAO.genreAlbums(genreID);
    }
    /**
     * Returns a list of all Albums related to a given Artist's ID from the remote database
     * @param artistID ID used to find all Albums related to the Artist from the remote database
     * @return
     */
    public List<Album> getArtistAlbums(int artistID){
    	return remoteDAO.artistAlbums(artistID);
    }
    /**
     * Returns a list of all Songs related to a given Album's ID from the remote database
     * @param albumID ID used to find all Songs related to the Album from the remote database
     * @return
     */
    public Set<Song> getAlbumSong(int albumID){
    	return remoteDAO.albumSongs(albumID);
    }
    /**
     * Returns a list of all Local Genres related to a given Album's ID from the local database
     * @param albumID ID used to find all Genres related to the Album in the local database
     * @return 
     */
    public List<LocalGenre> getLocalAlbumGenres(int albumID) {
    	return localDAO.getLocalAlbumGenres(albumID);
    }
    /**
     * Returns a list of Local Artists related to a given Album's ID from the local database
     * @param albumID ID used to find all Artists related to the Album in the local database
     * @return
     */
    public List<LocalArtist> getLocalAlbumArtists(int albumID) {
    	return localDAO.getLocalAlbumArtists(albumID);
    }
    /**
     * Returns a list of Local Song related to a given Album's ID from the local database
     * @param albumID ID used to find all Songs related to the Album in the local database
     * @return
     */
    public List<LocalSong> getLocalAlbumSongs(int albumID) {
    	return localDAO.localAlbumSongs(albumID);
    }
    /**
     * Adds a given Album to the user's local database
     * @param albumID ID set for the Wishlist's Album
     * @param albumName name set for the Wishlist's Album
     * @param albumYear release year set for the Wishlist's Album
     * @return
     */
    public boolean addToWishlist(int albumID, String albumName, int albumYear) {
    	
    	return localDAO.addToWishlist(albumID, albumName, albumYear);
    }
    /**
     * Returns a list of Wish List from the local database
     * @return
     */
    public List<WishList> readWishList() {
    	return localDAO.readWishList();
    }
    /**
     * Removes an Album from a user's own WishList given a specific ID
     * @param id ID used to remove a specific Wishlist from the local database
     */
    public boolean removeFromWishlist(int id) {
    	return localDAO.removeFromWishlist(id);
    }
    /**
     * Returns a WishList item from the local database with a given Album ID as the search key
     * @param albumID ID used to search a specific Wishlist item from the local database
     * @return
     */
    public boolean searchWishlist(int albumID) {
    	return localDAO.searchWishlist(albumID);
    }
    /**
     * Creates a User Reqest into the remote database with given parameters as both the title and contents respectively 
     * @param rTitle The title for a new User Request
     * @param rContents The contents for a new User Request
     */
    public void createRequest(String rTitle, String rContents) {
    	UserRequests newRequest = new UserRequests();
    	newRequest.setRequestTitle(rTitle);
    	newRequest.setRequestContents(rContents);
    	try {
    		remoteDAO.createRequest(newRequest);
    	}catch(Exception e) {
    		System.out.println("Failed to create a user request!");
    		e.printStackTrace();
    	}
    }
    /**
     * Returns a User Request from the remote database based on a request's ID
     * @param id ID used to locate a specific User Request from the database
     * @return
     */
    public UserRequests getRequest(int id) {
    	return remoteDAO.readRequest(id);
    }
    /**
     * Returns a table of User Requests of all requests found from the remote database
     * @return
     */
    public UserRequests[] getRequests() {
    	return remoteDAO.readRequests();
    }
    /**
     * Searches for a specific User Request with a given parameter as the search key
     * @param rTitle The search paramter for a given User Request's title
     * @return
     * @throws Exception In case the operation was unsuccessful, the method will throw an exception and interrupt the operation
     */
    public UserRequests searchRequestTitle(String rTitle) throws Exception {
    	return remoteDAO.searchRequestTitle(rTitle);
    }
    /**
     * Removes a User Request from the remote database with a given request ID as the parameter
     * @param id ID of User Request to be removed
     * @return
     */
    public boolean removeRequest(int id) {
    	return remoteDAO.removeRequest(id);
    }

}
