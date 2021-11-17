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
@Table(name = "Song")
public class LocalSong {

	@Id
	@Column(name = "SongID", updatable = false, nullable = false)
	private int songID;
	
	@Column(name = "SongName")
	private String songName;
	
	@ManyToMany(fetch=FetchType.EAGER,
			cascade={CascadeType.ALL})
	@JoinTable(
			name="AlbumSongs",
			joinColumns={@JoinColumn(name="SongID")},
			inverseJoinColumns={@JoinColumn(name="AlbumID")}
			)
	private List<LocalAlbum> songAlbums;
	
	public int getSongID() {
		return songID;
	}
	public void setSongID(int songID) {
		this.songID = songID;
	}
	public String getSongName() {
		return songName;
	}
	public void setSongName(String songName) {
		this.songName = songName;
	}
	public void setSongAlbums(List<LocalAlbum> songAlbums) {
		this.songAlbums = songAlbums;
	}
	public List<LocalAlbum> getSongAlbums() {
		return songAlbums;
	}
	public void addAlbum(LocalAlbum localAlbum) {
		if(songAlbums == null) {
			songAlbums = new ArrayList<>();
		}
		songAlbums.add(localAlbum);
	}
}
