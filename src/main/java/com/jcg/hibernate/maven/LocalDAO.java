package com.jcg.hibernate.maven;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import model.LocalArtist;
import model.LocalGenre;
import model.LocalAlbum;
import model.LocalSong;
import model.WishList;

public class LocalDAO {
	
//	static Session session;
	static SessionFactory sessionFactory;
	
	public LocalDAO() {
		try {
			sessionFactory = new Configuration().configure("localDAOhibernate.cfg.xml").buildSessionFactory();
			
		} catch (Exception e) {
			System.out.println("Istuntotehtaan luonti ei onnistunut: " + e.getMessage());
			System.exit(-1);
		}
//		session = sessionFactory.openSession();
	}
	
	
	
	public boolean createGenre(LocalGenre genre, LocalAlbum localAlbum) throws Exception {
		LocalGenre[] genreSearch;
		genreSearch = readGenres();
		
		//First loop to check whether a given genre is already found within the database
		for(int i = 0; i < genreSearch.length; i++) {			
			if(genreSearch[i].getGenreName().equals(genre.getGenreName())) {
				throw new Exception("This Genre already exists!");
			}
		}
				
		Transaction transAct = null;
		
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();
			
			
			genre.addAlbum(localAlbum);
			session.saveOrUpdate(genre);
			
//			session.saveOrUpdate(genre);
			
			transAct.commit();
			return true;
		}catch(Exception e) {
			System.out.println("exception rollback");
			if(transAct != null) 
				transAct.rollback();
			throw e;			
		}
	}
	//These are missing a simple text search!
	public LocalGenre readGenre(int id) {	
		Session session = sessionFactory.openSession();
		session.beginTransaction();			
		LocalGenre genre = (LocalGenre)session.get(LocalGenre.class, id);
		System.out.println("Found this thing -> \""+genre.getGenreName()+"\"");
		session.getTransaction().commit();
		session.close();
		return genre;		
	}
	
	public LocalGenre[] readGenres() {
		Transaction transAct = null;
		try (Session session = sessionFactory.openSession()) {
			transAct = session.beginTransaction();
			
			@SuppressWarnings("unchecked")
			List<LocalGenre> result = (List<LocalGenre>) session.createQuery("from LocalGenre").list();
			
			transAct.commit();
			LocalGenre[] array = new LocalGenre[result.size()];
			return (LocalGenre[]) result.toArray(array);
		} catch (Exception e) {
			if (transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	
	//TESTED! Works
	public LocalGenre searchGenre(String genreSearch) {
		Transaction transAct = null;
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();	
			Query query = session.createQuery("From Genre where genreNimi like:name");
			List<LocalGenre> genreList = query.setParameter("name", genreSearch).list();

			transAct.commit();
			session.close();
			return genreList.get(0);			
		}
	}
	
	//To be tested
	public boolean editGenre(LocalGenre genreEdit, int id) {
		Transaction transAct = null;		
		try(Session session = sessionFactory.openSession()){
		transAct = session.beginTransaction();		
		Genre editGenre = (Genre)session.load(Genre.class, id);		
		session.saveOrUpdate(editGenre);
		transAct.commit();
		return true;
		
		}catch(Exception e) {
			if(transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	
	//Working! Still needs extra logic for checking if a genre can be removed
	public boolean removeGenre(int id) {
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
	
	public boolean createArtist(LocalArtist artist) {
		LocalArtist[] artistSearch = readArtists();
		
		//First loop to check whether a given artist is already found within the database
		for(int i = 0; i < artistSearch.length; i++) {			
			if(artistSearch[i].getArtistName().equals(artist.getArtistName())) {
				System.out.println("This one already exists! Can't add it!");
				return false;
			}
		}
			
		Transaction transAct = null;	
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();
			session.saveOrUpdate(artist);
			session.save(artist);			
			transAct.commit();
			return true;
		}catch(Exception e) {
			if(transAct != null) 
				transAct.rollback();
			throw e;			
		}
	}
	
	public LocalArtist readArtist(int id) {		
		Session session = sessionFactory.openSession();
		session.beginTransaction();			
		LocalArtist artist = (LocalArtist)session.get(LocalArtist.class, id);	
		System.out.println("Found this thing -> "+artist.getArtistName());
		session.getTransaction().commit();
		session.close();
		return artist;
	}
	
	public LocalArtist[] readArtists() {
		Transaction transAct = null;
		try (Session session = sessionFactory.openSession()) {
			transAct = session.beginTransaction();
			System.out.println("Local readArtists exception 0 ");
			@SuppressWarnings("unchecked")
			List<LocalArtist> result = (List<LocalArtist>) session.createQuery("from LocalArtist order by artistName").list();
			System.out.println("Local readArtists exception 1");
			transAct.commit();
			session.close();
			System.out.println("Local readArtists exception 2");
			LocalArtist[] array = new LocalArtist[result.size()];
			array = (LocalArtist[]) result.toArray(array);
			return array;
		} catch (Exception e) {
			System.out.println("Local readArtists exception 3");
			if (transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	
//	public LocalArtist[] readArtists() {
//		Transaction transAct = null;
//		try (Session session = sessionFactory.openSession()) {
//			transAct = session.beginTransaction();
//			System.out.println("readArtists 1");
//			
//			
//			@SuppressWarnings("unchecked")
//			List<LocalArtist> result = (List<LocalArtist>) session.createQuery("from LocalArtist").list();
//			System.out.println("readArtists 2");
//			transAct.commit();
//			LocalArtist[] array = new LocalArtist[result.size()];
//			return (LocalArtist[]) result.toArray(array);
//		} catch (Exception e) {
//			if (transAct != null)
//				transAct.rollback();
//			throw e;
//		}
//	}
//	
	public List<LocalArtist> searchArtist(String artistSearch) throws Exception {
		Transaction transAct = null;
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();	
			Query query = session.createQuery("From Artist where artistName =:name");
			List<LocalArtist> artistList = query.setParameter("name", artistSearch).list();
			
			if (artistList.size() == 0) {
				throw new Exception("Nothing found!");
			}
			transAct.commit();
			session.close();
			
			return artistList;
		}catch(Exception e){
			System.out.println("exception why??");
			if(transAct != null)
				transAct.rollback();
			throw e;
		}
		
	}
	
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
	
//	public Artist searchArtist(String artistSearch) {
//		Transaction transAct = null;
//		try(Session session = sessionFactory.openSession()){
//			transAct = session.beginTransaction();	
//			Query query = session.createQuery("From Artisti where artistiNimi like:name");
//			List<Artist> artistList = query.setParameter("name", artistSearch).list();
//
//			transAct.commit();
//			session.close();
//			return artistList.get(0);			
//		}
//	}	
	
	//To be tested!
	public boolean editArtist(LocalArtist artistEdit, int id) {
		Transaction transAct = null;		
		try(Session session = sessionFactory.openSession()){
		transAct = session.beginTransaction();		
		Artist editArtist = (Artist)session.load(Artist.class, id);		
		session.saveOrUpdate(editArtist);
		transAct.commit();
		return true;
		
		}catch(Exception e) {
			if(transAct != null)
				transAct.rollback();
			throw e;
			}
		}		
	
	//To be tested!
	public boolean removeArtist(int id) {
		Transaction transAct = null;		
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
	
	
	//Still not sure how to handle the song list here :/
	public boolean createAlbum(LocalAlbum localAlbum, LocalSong[] songs, LocalArtist[] artists, LocalGenre[] genres) throws Exception {
		LocalAlbum[] albumSearch = readAlbums();		
		//First loop to check whether a given genre is already found within the database
		for(int i = 0; i < albumSearch.length; i++) {			
			if(albumSearch[i].getAlbumName().equals(localAlbum.getAlbumName())) {
				throw new Exception("This Album already exists!");
			}
		}

		Transaction transAct = null;	
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();
			
			for(LocalSong localSong : songs) {
				localSong.addAlbum(localAlbum);
				
				session.saveOrUpdate(localSong);
			}
			for(LocalArtist localArtist : artists) {
				// try catch
				try {
					LocalArtist localArtis = (LocalArtist)session.load(LocalArtist.class, localArtist.getArtistID());
					localArtis.addAlbum(localAlbum);
					session.update(localArtis);
					
				} catch (Exception e){
					System.out.println(e.getMessage());
					localArtist.addAlbum(localAlbum);
					session.save(localArtist);					
				}
				
			}
			for(LocalGenre localGenre : genres) {
				try {
					LocalGenre localGenr = (LocalGenre)session.load(LocalGenre.class, localGenre.getGenreID());
					localGenr.addAlbum(localAlbum);
					session.update(localGenr);					
				} catch (Exception e) {
					System.out.println(e.getMessage());
					localGenre.addAlbum(localAlbum);
					session.save(localGenre);					
				}
				
			}
			
//			session.saveOrUpdate(localAlbum);			
			session.save(localAlbum);
			transAct.commit();
			return true;
		}catch(Exception e) {
			if(transAct != null) 
				transAct.rollback();
			throw e;			
		}
	}
	
	public LocalAlbum readAlbum(int id) throws Exception {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		LocalAlbum album = (LocalAlbum)session.get(LocalAlbum.class, id);
		System.out.println("Found this thing -> "+album.getAlbumName());
		session.getTransaction().commit();
		session.close();
		return album;	
	}
	
	public LocalAlbum[] readAlbums() {
		Transaction transAct = null;
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();
			System.out.println("album1");
			@SuppressWarnings("unchecked")
			List<LocalAlbum> result = (List<LocalAlbum>) session.createQuery("from LocalAlbum order by AlbumName").list();
			System.out.println("album2");
			LocalAlbum[] array = new LocalAlbum[result.size()];
			transAct.commit();
			return (LocalAlbum[]) result.toArray(array);
		}catch (Exception e) {
			if (transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	
	//To be tested! Still not sure how to handle the song list here :/
	public boolean editAlbum(LocalAlbum albumEdit, LocalSong[] songEdit, int id) {
		Transaction transAct = null;		
		try(Session session = sessionFactory.openSession()){
		transAct = session.beginTransaction();		
		LocalAlbum editAlbum = (LocalAlbum)session.load(LocalAlbum.class, id);		
		session.saveOrUpdate(editAlbum);
		transAct.commit();
		return true;
		
		}catch(Exception e) {
			if(transAct != null)
				transAct.rollback();
			throw e;
		}
	}
		
	//To be tested!
	public boolean removeAlbum(int id) {
		Transaction transAct = null;		
		try(Session session = sessionFactory.openSession()){
		transAct = session.beginTransaction();		
		LocalAlbum removeAlbum = (LocalAlbum)session.load(LocalAlbum.class, id);		
		session.delete(removeAlbum);
		transAct.commit();
		return true;
		
		}catch(Exception e) {
			if(transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	
	public boolean editSong(int id) {
		Transaction transAct = null;		
		try(Session session = sessionFactory.openSession()){
		transAct = session.beginTransaction();		
		LocalSong editSong = (LocalSong)session.load(LocalSong.class, id);		
		session.saveOrUpdate(editSong);
		transAct.commit();
		return true;
		
		}catch(Exception e) {
			if(transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	
	public List<String> getSearchable() {
		Transaction transaction = null;
		try(Session session = sessionFactory.openSession()) {
			transaction = session.beginTransaction();
			String sql = "select artistinimi from artisti union select albuminimi from albumi union select genrenimi from genre union select kappalenimi from kappale";
			SQLQuery query = session.createSQLQuery(sql);
			List<String> results = query.list();
			transaction.commit();
			return results;
		} catch(Exception e) {
			if(transaction != null)
				transaction.rollback();
			throw e;
		}
		
	}
	/*
	 * Method takes in an album's ID and then opens a session with it. During the session, a song-list can be created based on the instance
	 * After loading the list, the session is closed and the method returns a list of Songs based on the album.
	 */
	public List<LocalSong> localAlbumSongs(int albumID){
		Transaction transAct = null;
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();
			LocalAlbum localAlbum = (LocalAlbum) session.load(LocalAlbum.class, albumID);
			List<LocalSong> array = localAlbum.getAlbumSongs();
			System.out.println(array);
			transAct.commit();
			session.close();
			return array;
		}catch(Exception e) {
			if(transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	
	public List<LocalGenre> getLocalAlbumGenres(int albumID) {
		Transaction transAct = null;
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();
			System.out.println("albumgenre1");
			LocalAlbum localAlbum = (LocalAlbum) session.load(LocalAlbum.class, albumID);
			System.out.println("albumgenre2");
			List<LocalGenre> array = localAlbum.getAlbumGenres();
			System.out.println("albumgenre3 " + array);
			transAct.commit();
			System.out.println("albumgenre4");
			session.close();
			System.out.println("albumgenre close");
			return array;
		}catch(Exception e) {
			System.out.println("albumgenre5");
			if(transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	
	public List<LocalArtist> getLocalAlbumArtists(int albumID) {
		Transaction transAct = null;
		try(Session session = sessionFactory.openSession()){
			transAct = session.beginTransaction();
			System.out.println("albumgenre1");
			LocalAlbum localAlbum = (LocalAlbum) session.load(LocalAlbum.class, albumID);
			System.out.println("albumgenre2");
			List<LocalArtist> array = localAlbum.getAlbumArtists();
			System.out.println("albumgenre3 " + array);
			transAct.commit();
			System.out.println("albumgenre4");
			session.close();
			System.out.println("albumgenre close");
			return array;
		}catch(Exception e) {
			System.out.println("albumgenre5");
			if(transAct != null)
				transAct.rollback();
			throw e;
		}
	}
	
	@SuppressWarnings("deprecation")
	public boolean searchWishlist(int albumID) {

		
		
		Transaction transAct = null;
		
		try(Session session = sessionFactory.openSession()) {
			transAct = session.beginTransaction();
//			WishList wishList = (WishList) session.createQuery("FROM WishList where id = :albumID");
			Query query = session.createQuery("FROM WishList WHERE albumID = :albumID");
			query.setParameter("albumID", albumID);
			List results = query.list();
			if(results.size() == 0) {
				System.out.println("Could not find item");
				return false;
			}
			System.out.println("Found item: " + results);
			transAct.commit();
			session.close();
			return true;
		} catch (Exception e) {
			if(transAct != null) {
				System.out.println("searchWishlist exception");
				transAct.rollback();
				return false;
			}
		}
		return false;
	}
	
	public boolean addToWishlist(int albumID) {
		if(searchWishlist(albumID) == true) {
			return false;
		}
		Transaction transAct = null;
		try(Session session = sessionFactory.openSession()) {
			transAct = session.beginTransaction();
			WishList wishList = new WishList();
			wishList.setAlbumID(albumID);
			session.save(wishList);
			transAct.commit();
			session.close();
			return true;
		} catch (Exception e) {
			if(transAct != null) {
				transAct.rollback();
			}
			throw e;
		}
	}
	
	public void finalize() {
		try {
			if(sessionFactory != null)
				sessionFactory.close();
		}catch(Exception e) {
			System.err.println("Session factory couldn't be closed: "+e.getMessage());
		}
	}

}
