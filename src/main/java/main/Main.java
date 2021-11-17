package main;





import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.jcg.hibernate.maven.RemoteDAO;
import com.jcg.hibernate.maven.Song;
import com.jcg.hibernate.maven.Album;
import com.jcg.hibernate.maven.Artist;
import com.jcg.hibernate.maven.Genre;
import com.jcg.hibernate.maven.LocalDAO;

import controller.Controller;
import controller.GUIController;
import model.LocalAlbum;
import model.LocalGenre;
import model.LocalSong;

import com.jcg.hibernate.maven.Album;
import com.jcg.hibernate.maven.Artist;
import com.jcg.hibernate.maven.RemoteDAO;


import view.*;


public class Main {
	public static void main(String args[]) {
		View.main(args); 
	}
}
