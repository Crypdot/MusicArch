package com.jcg.hibernate.maven;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;


@Entity
@Table(name = "Album")
public class Album {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "AlbumID", updatable = false, nullable = false)
	private int albumID;
	
	@Column(name = "AlbumName")
	private String albumName;
	
	@Column(name = "AlbumYear")
	private int albumYear;

	@ManyToMany(fetch=FetchType.LAZY,
			cascade={CascadeType.ALL})
	@JoinTable(
			name="AlbumGenres",
			joinColumns={@JoinColumn(name="AlbumID")},
			inverseJoinColumns={@JoinColumn(name="GenreID")}
			)
	private Set<Genre> albumGenres = new HashSet<Genre>();
	
	@ManyToMany(fetch=FetchType.LAZY,
			cascade={CascadeType.ALL})
	@JoinTable(
			name="AlbumArtists",
			joinColumns={@JoinColumn(name="AlbumID")},
			inverseJoinColumns={@JoinColumn(name="ArtistID")})
	private Set<Artist> albumArtists = new HashSet<Artist>();
	
	@ManyToMany(fetch=FetchType.LAZY,
			cascade= {CascadeType.ALL})
	@JoinTable(
			name="AlbumSongs",
			joinColumns= {@JoinColumn(name="AlbumID")},
			inverseJoinColumns= {@JoinColumn(name="SongID")})
	private Set<Song> albumSongs = new HashSet<Song>();
	
	public Set<Artist> getAlbumArtists(){
		// Do not remove the print below or you will get:
		//"failed to lazily initialize a collection of role: com.jcg.hibernate.maven.Album.albumArtists, could not initialize proxy - no Session" in the AlbumPage
		System.out.println("album artists " + this.albumArtists);
		return albumArtists;
	}
	public void setAlbumArtists(Set<Artist> albumArtists) {
		this.albumArtists = albumArtists;
	}
	public void addArtist(Artist artist) {
		if(albumArtists == null) {
			albumArtists = new HashSet<Artist>();
		}
		albumArtists.add(artist);
	}	
	public Set<Genre> getAlbumGenres(){
		// Do not remove the print below or you will get:
		//"failed to lazily initialize a collection of role: com.jcg.hibernate.maven.Album.albumGenres, could not initialize proxy - no Session" in the AlbumPage
		System.out.println("album genres " + this.albumGenres);
		return albumGenres;
	}
	public void setAlbumGenres(Set<Genre> albumGenres) {
		this.albumGenres = albumGenres;
	}
	public void addGenre(Genre genre) {
		System.out.println("genre = " + genre.getGenreName());
		if(albumGenres == null) {
			albumGenres = new HashSet<Genre>();
		}
		albumGenres.add(genre);
	}
	public void setAlbumSongs(Set<Song> albumSongs) {
		this.albumSongs = albumSongs;
	}
	public void addSong(Song song) {
		if(albumSongs == null) {
			albumSongs = new HashSet<>();
		}
		albumSongs.add(song);
	}
	public Set<Song> getAlbumSongs() {
		System.out.println("album songs " + this.albumSongs);
		return albumSongs;
	}
	public Album() {}
	
	public int getAlbumID() {
		return albumID;
	}
	public void setAlbumID(int albumID) {
		this.albumID = albumID;
	}
	public String getAlbumName() {
		return albumName;
	}
	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}
	public int getAlbumYear() {
		return albumYear;
	}
	public void setAlbumYear(int albumYear) {
		this.albumYear = albumYear;
	}
	
	
}
