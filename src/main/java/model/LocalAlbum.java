package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = ("Album"))
public class LocalAlbum {

	@Id
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
	private List<LocalGenre> albumGenres;
	
	@ManyToMany(fetch=FetchType.LAZY,
			cascade={CascadeType.ALL})
	@JoinTable(
			name="AlbumArtists",
			joinColumns={@JoinColumn(name="AlbumID")},
			inverseJoinColumns={@JoinColumn(name="ArtistID")})
	private List<LocalArtist> albumArtists;
	
	@ManyToMany(fetch=FetchType.LAZY,
			cascade= {CascadeType.ALL})
	@JoinTable(
			name="AlbumSongs",
			joinColumns= {@JoinColumn(name="AlbumID")},
			inverseJoinColumns= {@JoinColumn(name="SongID")})
	private List<LocalSong> albumSongs;
	
	public List<LocalArtist> getAlbumArtists(){
		return albumArtists;
	}
	public void setAlbumArtists(List<LocalArtist> albumArtists) {
		this.albumArtists = albumArtists;
	}
	public void addArtist(LocalArtist localArtist) {
		if(albumArtists == null) {
			albumArtists = new ArrayList<>();
		}
		albumArtists.add(localArtist);
	}	

	
	public List<LocalGenre> getAlbumGenres(){
		return albumGenres;
	}
	
	public void setAlbumGenres(List<LocalGenre> albumGenres) {
		this.albumGenres = albumGenres;
	}
	public void addGenre(LocalGenre localGenre) {
		if(albumGenres == null) {
			albumGenres = new ArrayList<>();
		}
		albumGenres.add(localGenre);
	}
	
	public void setAlbumSongs(List<LocalSong> albumSongs) {
		this.albumSongs = albumSongs;
	}
	public void addSong(LocalSong localSong) {
		if(albumSongs == null) {
			albumSongs = new ArrayList<>();
		}
		albumSongs.add(localSong);
	}
	public List<LocalSong> getAlbumSongs() {
		return albumSongs;
	}
	
	public LocalAlbum() {}
	
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
