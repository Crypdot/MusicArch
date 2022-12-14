package com.jcg.hibernate.maven;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


import org.hibernate.*;
import org.hibernate.cfg.Configuration;

/**
 * The purpose of RemoteDAO is to act as a intermediary between the application and the remote database. It sends creation-, search- and deletion requests to the MySQL database defined in Hibernate.cfg
 * @author Alex, Jani
 *
 */
public class RemoteDAO {
	static Session session;
	static SessionFactory sessionFactory;
	private static RemoteDAO rDAO;
	/**
	 * Returns the instance of RemoteDAO.
	 * @return
	 */
	public static RemoteDAO getInstance() {
		if(rDAO == null) {
			rDAO = new RemoteDAO();
		}
		return rDAO;
	}
	
	private RemoteDAO() {
		try {
			sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
		} catch (Exception e) {
			System.err.println("Istuntotehtaan luonti ei onnistunut: " + e.getMessage());
			System.exit(-1);
		}
	}
	/**
	 * Method used to create a new genre in the database. Will first iterate through
	 * all found genreNames, to ensure that it will not allow the creation of a
	 * genre that already exists. 
	 * @param genre Given genre that is created
	 * @return true if creation went through, false if not
	 * @throws Exception In case the operation was unsuccessful, the method will throw an exception and roll back the transaction
	 */
	public boolean createGenre(Genre genre) throws Exception {
		Genre[] genreSearch = readGenres();
		for (int i = 0; i < genreSearch.length; i++) {
			if (genreSearch[i].getGenreName().equals(genre.getGenreName())) {
				throw new Exception("This Genre already exists!");
			}
		}
		Transaction transAct = null;
		try (Session session = sessionFactory.openSession()) {
			transAct = session.beginTransaction();
			session.saveOrUpdate(genre);
			System.out.println("Got a creation request!");
			transAct.commit();
			return true;
		} catch (Exception e) {
			if (transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 * readGenre() will return a singular Genre-object from the remote database, based on the given genreID
	 * @param id an ID with which the Genre is found from the database
	 * @return genre Genre-object that was found.
	 */
	public Genre readGenre(int id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Genre genre = (Genre) session.get(Genre.class, id);
		System.out.println("Found this thing -> \"" + genre.getGenreName() + "\"");
		session.getTransaction().commit();
//		session.close();
		return genre;
	}
	/**
	 * readGenres() will return a list of all Genres found within the database.
	 * @return Genre[] returns the results in table form.
	 */
	public Genre[] readGenres() {
		Transaction transAct = null;
		try (Session session = sessionFactory.openSession()) {
			transAct = session.beginTransaction();

			@SuppressWarnings("unchecked")
			List<Genre> result = (List<Genre>) session.createQuery("from Genre order by genreName").list();

			transAct.commit();

			Genre[] array = new Genre[result.size()];
//			session.close();
			return (Genre[]) result.toArray(array);
		} catch (Exception e) {
			if (transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 *  searchGenre() will return a single Genre-object based on a simple String input, used to search the database for the Genre
	 * @param genreSearch A String used to set the search's parameters
	 * @return genre A found Genre
	 * @throws Exception In case the operation was unsuccessful, the method will throw an exception
	 */
	public Genre searchGenre(String genreSearch) throws Exception {
		Transaction transAct = null;
		try (Session session = sessionFactory.openSession()) {
			transAct = session.beginTransaction();
			Query query = session.createQuery("From Genre where genreName =:name");
			List<Genre> genreList = query.setParameter("name", genreSearch).list();
			System.out.println("The genre search result was -> "+genreList.get(0).getGenreName());
			
			transAct.commit();
			
			if (genreList.size() == 0) {
				System.out.println("Genrelist size == 0");
				session.close();
				throw new Exception("Nothing found!"); // Is this the problem? We might not be handling the errors correctly!
				
			}
			//session.close();
			return genreList.get(0);

		} catch (Exception e) {
			if (transAct != null)
			transAct.rollback();
			throw e;
		}
	}
	/**
	 * editGenre() will update a given Genre based on its ID. This is used to find the Genre from the database, which will then be 
	 * updated based on a given Genre-object
	 * @param genreEdit The new Genre-object used to replace the original
	 * @param id an ID used to locate the editable Genre from the database
	 * @return boolean true if editing successful.
	 */
	public boolean editGenre(Genre genreEdit, int id) {
		Transaction transAct = null;
		try (Session session = sessionFactory.openSession()) {
			transAct = session.beginTransaction();
			Genre editGenre = (Genre) session.load(Genre.class, id);
			editGenre.setGenreName(genreEdit.getGenreName());
			session.update(editGenre);
			transAct.commit();
			return true;
		} catch (Exception e) {
			if (transAct != null)
				transAct.rollback();
			throw e; 
		}
	}
	/**
	 * removeGenre() will remove a single Genre based on the given genreID
	 * @param id an ID used to locate the Genre to be removed
	 * @return boolean true if removal successful
	 */
	public boolean removeGenre(int id) {
		List<Album> albumsToRemove = genreAlbums(id);
		
		for(Album a : albumsToRemove) {
			removeAlbum(a.getAlbumID());
		}
		Transaction transAct = null;
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();		
			Genre removeGenre = (Genre)session.load(Genre.class, id);		
			session.delete(removeGenre);
			transAct.commit();
			return true;
			}catch(Exception e) {
				if(transAct != null)
					transAct.rollback();
				throw e;
			}
	}
	/**
	 * Method used to create a new artist in the database. Will first iterate through
	 * all found artistNames, to ensure that it will not allow the creation of a
	 * artist that already exists.
	 * @param artist Given artist that is created
	 * @return boolean true if creation successful
	 * @throws Exception In case the operation was unsuccessful, the method will throw an exception and roll back the transaction
	 */
	public boolean createArtist(Artist artist) throws Exception {
		Artist[] artistSearch = readArtists();
		for (int i = 0; i < artistSearch.length; i++) {
			if (artistSearch[i].getArtistName().equals(artist.getArtistName())) {
				throw new Exception("This Artist already exists!");
			}
		}

		Transaction transAct = null;
		try (Session session = sessionFactory.openSession()) {
			transAct = session.beginTransaction();
			session.saveOrUpdate(artist);
			transAct.commit();
			return true;
		} catch (Exception e) {
			if (transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 * readArtist() will return a singular Artist-object from the remote database, based on the given artistID
	 * @param id an ID used to locate a given Artist from the database
	 * @return artist Artist object found from the database
	 */
	public Artist readArtist(int id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Artist artist = (Artist) session.get(Artist.class, id);
		System.out.println("Found this thing -> " + artist.getArtistName());
		session.getTransaction().commit();
		//session.close();
		return artist;
	}
	/**
	 * readArtists() will return a list of all artists found within the database
	 * @return array A table of all Artists found from the database
	 */
	public Artist[] readArtists() {
		System.out.println("Got a readartist request");
		Transaction transAct = null;
		try (Session session = sessionFactory.openSession()) {
			transAct = session.beginTransaction();

			@SuppressWarnings("unchecked")
			List<Artist> result = (List<Artist>) session.createQuery("from Artist order by artistName").list();
			transAct.commit();
			Artist[] array = new Artist[result.size()];

			//session.close();
			return (Artist[]) result.toArray(array);
		} catch (Exception e) {
			if (transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 * searchArtist() will return a single Artist-object based on a simple String input, used to search the database for the Artist
	 * @param artistSearch The search parameters used to find an Artist from the database
	 * @return artist Found Artist from the database
	 * @throws Exception In case the operation was unsuccessful, the method will throw an exception and roll back the transaction
	 */
	public Artist searchArtist(String artistSearch) throws Exception {
		Transaction transAct = null;
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();	
			Query query = session.createQuery("From Artist where ArtistName =:name");
			List<Artist> artistList = query.setParameter("name", artistSearch).list();
			
			transAct.commit();
			
			if (artistList.size() == 0) {
				System.out.println("Nothing found!");
				session.close();
				throw new Exception("Nothing found!");
			}
			
			//session.close();
			
			return artistList.get(0);
		}catch(Exception e){
			System.out.println("exception why??");
			if(transAct != null)
				transAct.rollback();
			throw e;
		}
		
	}
	/**
	 * editArtist() will update a given Artist based on its ID. This is used to find the Artist from the database, which will then be 
	 * updated based on a given Artist-object
	 * @param artistEdit The new Artist-object used to replace the original in the database
	 * @param id An ID used to locate the original Artist to be edited
	 * @return boolean true if editing successful
	 */
	public boolean editArtist(Artist artistEdit, int id) {
		Transaction transAct = null;		
		try(Session session = sessionFactory.openSession()){
		transAct = session.beginTransaction();		
		Artist editArtist = (Artist)session.load(Artist.class, id);
		editArtist.setArtistName(artistEdit.getArtistName());
		editArtist.setArtistBio(editArtist.getArtistBio());
		session.update(editArtist);
		transAct.commit();
		return true;
		
		}catch(Exception e) {
			if(transAct != null)
				transAct.rollback();
			throw e;
		}
	}		
	/**
	 * removeArtist() will remove a single Artist based on the given artistID
	 * @param id An ID used to locate the original Artist to be edited
	 * @return boolean true if removal successful
	 */
	public boolean removeArtist(int id) {
		Transaction transAct = null;	
		
		List<Album> albumsToRemove = artistAlbums(id);
		
		for(Album a : albumsToRemove) {
			removeAlbum(a.getAlbumID());
		}
		
		try(Session session = sessionFactory.openSession()){
		transAct = session.beginTransaction();		
		Artist removeArtist = (Artist)session.load(Artist.class, id);		
		session.delete(removeArtist);
		transAct.commit();
		return true;
		}catch(Exception e) {
			if(transAct != null)
				transAct.rollback();
			throw e;
		}
	}		
	/**
	 * createAlbum creates an album inside the database using a Hibernate-session.
	 * @param album The Album-object to be created in the database
	 * @param artistList A set of Artists to be linked to the new Album
	 * @param genreList A set of Genres to be linked to the new Album
	 * @param songList A set of Songs to be linked to the new Album
	 * @return boolean true if successful
	 * @throws Exception In case the operation was unsuccessful, the method will throw an exception and roll back the transaction
	 */
	public boolean createAlbum(Album album, Set<Artist> artistList, Set<Genre> genreList, Set<Song> songList) throws Exception {
		Album[] albumSearch = readAlbums();	
		List<Song> newSongs = new ArrayList<Song>();
		//This is placeholder code, just for testing-purposes, for now. Could maybe place the adding inside this loop, if adding the song here works?
		for(Song song : songList) {
			if(!existingSongs().contains(song.getSongName())) {
				System.out.println("This song doesn't exist! So now we're making a song with the title: "+song.getSongName());
				createSong(song);
				newSongs.add(searchSong(song.getSongName())); //This'd be making a list of songs that have now been created? 
			}else {
				newSongs.add(searchSong(song.getSongName()));
				System.out.println("This song"+song.getSongName()+" DID exist! Adding it to the reference list! Moving on!");
			}
			
		}
		for(int i = 0; i < albumSearch.length; i++) {
			if(albumSearch[i].getAlbumName().equals(album.getAlbumName())) {
				session.close();
				throw new Exception("This Album already exists!");
			}
		}
		Transaction transAct = null;	
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();
			session.save(album);
			for(Artist artist : artistList) {				
				album.addArtist(artist);
				session.saveOrUpdate(artist);
//				session.update(artist2);
			}
			for(Genre genre : genreList) {
				album.addGenre(genre);
				session.saveOrUpdate(genre);	
			}			
			//This'd be adding songs, but it requires the song to already exist, so...
			for(Song song : newSongs) {
				album.addSong(song);
				session.saveOrUpdate(song);
			}
			transAct.commit();
			//session.close();
			return true;
		}catch(Exception e) {
			System.out.println(e.getMessage());
			if(transAct != null) 
				transAct.rollback();
			throw e;			
		}
	}
	
	/**
	 * Method used to add an album to a single genre.
	 * @param album The Album that will receive a new Genre
	 * @param genre The Genre that will be added to the Album
	 * @return boolean true if adding a genre successful
	 */
	public boolean addAlbumGenre(Album album, Genre genre) {
		Transaction transAct = null;	
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();
			genre.addAlbum(album);
			session.saveOrUpdate(genre);
			transAct.commit();
			System.out.println("After Commit");
			//session.close();
			return true;
		}catch(Exception e) {
			System.out.println(e.getMessage());
			if(transAct != null) 
				transAct.rollback();
			throw e;			
		}
	}
	/**
	 * readAlbum() will return a singular Album-object from the remote database, based on the given albumID
	 * @param id ID used to locate the specific Album
	 * @return album Album-object found in the database
	 */
	public Album readAlbum(int id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Album album = (Album)session.get(Album.class, id);
		System.out.println("Found this thing -> "+album.getAlbumName());
		session.getTransaction().commit();
		//session.close();
		return album;	
	}
	/**
	 * readAlbums() will return a list of all albums found within the database
	 * @return arrayAlbum A table of Albums found within the database
	 */
	public Album[] readAlbums() {
		Transaction transAct = null;
		try (Session session = sessionFactory.openSession()) {
			transAct = session.beginTransaction();
			
			@SuppressWarnings("unchecked")
			List<Album> result = (List<Album>) session.createQuery("from Album").list();
			System.out.println("After album query ");
			transAct.commit();
			Album[] array = new Album[result.size()];
			
			return (Album[])result.toArray(array);
		} catch (Exception e) {
			if (transAct != null)
				transAct.rollback();
			return new Album[0];
		}
	}
	/**
	 * searchAlbum() will return a single Album-object based on a simple String input, used to search the database for the Album
	 * @param albumSearch A String used to set the search's parameters
	 * @return album Album found with the search parameter
	 * @throws Exception In case the operation was unsuccessful, the method will throw an exception and roll back the transaction
	 */
	public Album searchAlbum(String albumSearch) throws Exception{
		Transaction transAct = null;
		try(Session session = sessionFactory.openSession()){
//			transAct = session.beginTransaction();	
			Query query = session.createQuery("From Album where albumName like:name");
			List<Album> albumList = query.setParameter("name", albumSearch).list();

			
//			transAct.commit();
			//session.close();
			
			if (albumList.size() == 0) {
				System.out.println("Nothing found in the albums with this search -> "+albumSearch); //This is the one that bugs out! Jumps over to the catch -> "Cannot rollback transaction in current status [COMMITTED]
				session.close();
				throw new Exception("Nothing found!");
			}
			return albumList.get(0);
		}catch(Exception e){
			if(transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 * editAlbum() will update a given Album based on its ID. This is used to find the Album from the database, which will then be 
	 * updated based on a given Album-object
	 * @param id ID used to locate the original Album within the database
	 * @param albumEdit The new Album that will replace the original
	 * @return boolean true if editing successful
	 * @throws Exception In case the operation was unsuccessful, the method will throw an exception and roll back the transaction
	 */
	public boolean editAlbum(int id, Album albumEdit) throws Exception {
		Set<Genre> genreList = albumEdit.getAlbumGenres();
		Set<Artist> artistList = albumEdit.getAlbumArtists();
		
		Transaction transAct = null;
		try(Session session = sessionFactory.openSession()){
			Album editable = (Album)session.load(Album.class, id);
			editable.setAlbumName(albumEdit.getAlbumName());
			editable.setAlbumYear(albumEdit.getAlbumYear());
			
			editable.clearAlbum();
			session.update(editable);
			
			transAct = session.beginTransaction();
			for(Artist artist : artistList) {
				Artist artist2 = (Artist)session.load(Artist.class, artist.getArtistID());
				editable.addArtist(artist2);
				session.update(editable);
			}
			for(Genre genre : genreList) {
				Genre genre2 = (Genre)session.load(Genre.class, genre.getGenreID());
				editable.addGenre(genre2);
				session.update(editable);	
			}		
			transAct.commit();
			//session.close();
			return true;
		}catch(Exception e) {
			System.out.println(e.getMessage());
			if(transAct != null) 
				transAct.rollback();
			throw e;			
		}
	}
	/**
	 * removeAlbum() will remove a single Album based on the given albumID
	 * @param id ID used to locate the Album to be removed from the database
	 * @return boolean true if operation was successful
	 */
	public boolean removeAlbum(int id) {
		
		Transaction transAct = null;
		try (Session session = sessionFactory.openSession()) {
			transAct = session.beginTransaction();
			Album removeAlbum = (Album) session.load(Album.class, id);
			session.delete(removeAlbum);
			transAct.commit();
			return true;

		} catch (Exception e) {
			if (transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 * Method used to create a new song in the database. Will first iterate through
	 * all found songNames, to ensure that it will not allow the creation of a
	 * song that already exists. 
	 * @param song Song-object that will be used to create a new Song in the database
	 * @return boolean true if editing successful
	 * @throws Exception In case the operation was unsuccessful, the method will throw an exception and roll back the transaction
	 */
	public boolean createSong(Song song) throws Exception {
		Song[] songSearch = readSongs();
		System.out.println("Before the song Search!");
		for(int i = 0; i < songSearch.length; i++) {			
			if(songSearch[i].getSongName().equals(song.getSongName())) {
				return true;
			}
		}
		
		System.out.println("After the song search! Going into the transaction!");
		Transaction transAct = null;	
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();
			session.saveOrUpdate(song);
			transAct.commit();
			return true;
		}catch(Exception e) {
			if(transAct != null) 
				transAct.rollback();
			throw e;			
		}
	}
	/**
	 * readSong() will return a singular Song-object from the remote database, based on the given songID
	 * @param id ID used to locate an individual Song from the database
	 * @return song Song-object found in the database
	 */
	public Song readSong(int id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Song song = (Song)session.get(Song.class, id);
		System.out.println("Found this thing -> "+song.getSongName());
		session.getTransaction().commit();
		//session.close();
		return song;	
	}
	/**
	 * readSongs() will return a list of all albums found within the database
	 * @return array An array of all Songs inside the database
	 */
	public Song[] readSongs() {
		Transaction transAct = null;
		try (Session session = sessionFactory.openSession()) {
			transAct = session.beginTransaction();

			@SuppressWarnings("unchecked")
			List<Song> result = (List<Song>) session.createQuery("from Song order by songName").list();

			transAct.commit();
			Song[] array = new Song[result.size()];
			return (Song[]) result.toArray(array);
		} catch (Exception e) {
			if (transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 * searchSong() will return a single Song-object based on a simple String input, used to search the database for the Song
	 * @param songSearch A search parameter used to find a single Song from the database
	 * @return songList.get(0) The first found object in the database
	 * @throws Exception In case the operation was unsuccessful, the method will throw an exception and roll back the transaction
	 */
	public Song searchSong(String songSearch) throws Exception{
		Transaction transAct = null;
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();	
			Query query = session.createQuery("From Song where SongName like:name");
			List<Song> songList = query.setParameter("name", songSearch).list();
			
			transAct.commit();
			//session.close();
			
			if (songList.size() == 0) {
				session.close();
				throw new Exception("Nothing found!");
			}
			return songList.get(0);
		}catch(Exception e){
			if(transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 * editSong() will update a given Song based on its ID. This is used to find the Song from the database, which will then be 
	 * updated based on a given Song-object
	 * @param songEdit A Song object that will replace the original in the database
	 * @param id ID used to locate the original Song within the database
	 * @return boolean true if editing successful
	 */
	public boolean editSong(Song songEdit, int id) {
		Transaction transAct = null;
		try (Session session = sessionFactory.openSession()) {
			transAct = session.beginTransaction();
			Song editSong = (Song) session.load(Song.class, id);
			session.saveOrUpdate(editSong);
			transAct.commit();
			return true;

		} catch (Exception e) {
			if (transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 * removeSong() will remove a single Song based on the given songID
	 * @param id ID used to locate the original Song within the database
	 * @return boolean true if removal is successful
	 */
	public boolean removeSong(int id) {
		Transaction transAct = null;
		try (Session session = sessionFactory.openSession()) {
			transAct = session.beginTransaction();
			Song removeSong= (Song) session.load(Song.class, id);
			session.delete(removeSong);
			transAct.commit();
			return true;

		} catch (Exception e) {
			if (transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 * getSearchable() will return a list of every single name within the database
	 * @return results A list of all names found within the database
	 */
	public List<String> getSearchable(){
		Transaction transAct = null;
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();
			String sql = "select ArtistName from Artist union select AlbumName from Album union select GenreName from Genre union select SongName from Song";
			SQLQuery query = session.createSQLQuery(sql);
			List<String> results = query.list();
			transAct.commit();
			//session.close();
			return results;
		}catch(Exception e) {
			if(transAct != null)
				transAct.rollback();
			throw e;
		}		
	}	
	/**
	 * existingGenres() will return a list of every genreName found within the database
	 * @return results A list of all Genre-names found within the database
	 */
	public List<String> existingGenres(){
		Transaction transAct = null;
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();
			String sql = "select GenreName from Genre";
			SQLQuery query = session.createSQLQuery(sql);
			List<String> results = query.list();
			transAct.commit();
			return results;
		}catch(Exception e) {
			if(transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 * existingArtists() will return a list of every artistName found within the database
	 * @return results A list of all Artist-names found within the database
	 */
	public List<String> existingArtists(){
		Transaction transAct = null;
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();
			String sql = "select ArtistName from Artist";
			SQLQuery query = session.createSQLQuery(sql);
			List<String> results = query.list();
			transAct.commit();
			return results;
		}catch(Exception e) {
			if(transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 * existingAlbums() will return a list of every albumName found within the database
	 * @return results A list of all Album-names found within the database
	 */
	public List<String> existingAlbums(){
		Transaction transAct = null;
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();
			String sql = "select AlbumName from Album";
			SQLQuery query = session.createSQLQuery(sql);
			List<String> results = query.list();
			transAct.commit();
			return results;
		}catch(Exception e) {
			if(transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 * existingSongs() will return a list of every songName found within the database
	 * @return result A list of all Song-names found within the database
	 */
	public List<String> existingSongs(){
		Transaction transAct = null;
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();
			String sql = "select SongName from Song";
			SQLQuery query = session.createSQLQuery(sql);
			List<String> results = query.list();
			transAct.commit();
			return results;
		}catch(Exception e) {
			if(transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 * Method takes in a given genre's ID, and then opens a session with it. During the session, an album-list can be created based on the instance
	 * After loading the list, the session is closed and the method returns a list of Albums based on the genre
	 * @param genreID The ID of the Genre used to find the Albums related
	 * @return array List of Albums found, related to the Genre given
	 */
	public List<Album> genreAlbums(int genreID){
		Transaction transAct = null;
		try (Session session = sessionFactory.openSession()) {
			transAct = session.beginTransaction();
			
			Genre genre = (Genre) session.load(Genre.class, genreID);
			List<Album> array = genre.getGenreAlbums();
			transAct.commit();
			//session.close();
			return array;
		} catch (Exception e) {
			if (transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 * Method takes in a given artist's ID, and then opens a session with it. During the session, an album-list can be created based on the instance
	 * After loading the list, the session is closed and the method returns a list of Albums based on the artist
	 * @param artistID The ID of the Artist used to find the Albums related
	 * @return array List of Albums found, related to the Genre given
	 */
	public List<Album> artistAlbums(int artistID){
		Transaction transAct = null;
		try (Session session = sessionFactory.openSession()) {
			transAct = session.beginTransaction();

			Artist artist = (Artist) session.load(Artist.class, artistID);
			List<Album> array = artist.getArtistAlbums();
			transAct.commit();
//			session.close();
			return array;
		} catch (Exception e) {
			if (transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 * Method takes in an album's ID and then opens a session with it. During the session, a song-list can be created based on the instance
	 * After loading the list, the session is closed and the method returns a list of Songs based on the album.
	 * @param albumID The ID of the Album used to find the Songs related
	 * @return array List of Songs found, related to the Album given
	 */
	public Set<Song> albumSongs(int albumID){
		Transaction transAct = null;
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();
			Album album = (Album) session.load(Album.class, albumID);
			Set<Song> array = album.getAlbumSongs();
			transAct.commit();
//			session.close();
			return array;
		}catch(Exception e) {
			if(transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 * Method takes in an album's ID and then opens a session using it. A list of artists is created based on the Album, 
	 * and the method thus returns a list of Artists related to the given album
	 * @param albumID The ID of the Album used to find the Artists related 
	 * @return array List of Artists found, related to the Album given
	 */
	public Set<Artist> albumArtistList(int albumID){
		Transaction transAct = null;
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();
			Album album = (Album) session.load(Album.class, albumID);
			Set<Artist> array = album.getAlbumArtists();
			
			transAct.commit();
//			session.close();
			return array;
		}catch(Exception e) {
			if(transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 * Method takes in an album's ID and then opens a session using it. A list of genres is created based on the Album,
	 * and the method thus returns a list of Genres related to the given album
	 * @param albumID The ID of the Album used to find the Genres related
	 * @return array List of Genres found, related to the Album given
	 */
	public Set<Genre> albumGenreList(int albumID){
		Transaction transAct = null;
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();
			Album album = (Album) session.load(Album.class, albumID);
			Set<Genre> array = album.getAlbumGenres();
			transAct.commit();
//			session.close();
			return array;
		}catch(Exception e) {
			if(transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 * Creates a user submitted request into the database
	 * @param request The UserRequest object to be saved in the database
	 * @return boolean true if request went through
	 * @throws Exception In case the operation was unsuccessful, the method will throw an exception and roll back the transaction
	 */
	public boolean createRequest(UserRequests request) throws Exception {
		Transaction transAct = null;
		System.out.println("Got a create request for requests");
		try (Session session = sessionFactory.openSession()) {
			System.out.println("Inside the try block");
			transAct = session.beginTransaction();
			System.out.println("Right after session.beginTransaction()");
			session.saveOrUpdate(request);
			System.out.println("Creating a user request!");
			transAct.commit();
			return true;
		} catch (Exception e) {
			if (transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 * Returns a specific UserRequest from the database using a request id
	 * @param id ID used to locate a specific User Request within the database
	 * @return returnable The User Request found in the database
	 */
	public UserRequests readRequest(int id) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		UserRequests returnable = (UserRequests)session.get(UserRequests.class, id);
		System.out.println("Found title -> "+returnable.getRequestTitle());
		session.getTransaction().commit();
		//session.close();
		return returnable;	
	}
	/**
	 * Returns a table of all User Requests from the database
	 * @return result An array of all User Requests
	 */
	public UserRequests[] readRequests() {
		Transaction transAct = null;
		try (Session session = sessionFactory.openSession()) {
			transAct = session.beginTransaction();

			@SuppressWarnings("unchecked")
			List<UserRequests> result = (List<UserRequests>) session.createQuery("from UserRequests order by RequestID").list();

			transAct.commit();
			UserRequests[] array = new UserRequests[result.size()];
			return (UserRequests[]) result.toArray(array);
		} catch (Exception e) {
			if (transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 * Returns a User Request based on a search from the database.
	 * @param rTitle The search parameter
	 * @return requestList.get(0) The first found User Request
	 * @throws Exception In case the operation was unsuccessful, the method will throw an exception and roll back the transaction
	 */
	public UserRequests searchRequestTitle(String rTitle) throws Exception {
		Transaction transAct = null;
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();	
			Query query = session.createQuery("From UserRequests where RequestTitle like:name");
			List<UserRequests> requestList = query.setParameter("name", rTitle).list();
			
			transAct.commit();
			//session.close();
			
			if (requestList.size() == 0) {
				session.close();
				throw new Exception("Nothing found!");
			}
			return requestList.get(0);
		}catch(Exception e){
			if(transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	/**
	 * Removes a User Request frmo the database based on a id search
	 * @param id ID used to locate specific request to be deleted
	 * @return boolean true if removal was successful
	 */
	public boolean removeRequest(int id) {
		Transaction transAct = null;
		try (Session session = sessionFactory.openSession()) {
			transAct = session.beginTransaction();
			UserRequests removable = (UserRequests) session.load(UserRequests.class, id);
			session.delete(removable);
			transAct.commit();
			return true;

		} catch (Exception e) {
			if (transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	
	public void finalize() {
		try {
			if (sessionFactory != null)
				System.out.println("Sessiotehdas suljettu");
				sessionFactory.close();
		} catch (Exception e) {
			System.err.println("Session factory couldn't be closed: " + e.getMessage());
		}
	}

}