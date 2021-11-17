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
@Table(name = "Genre")
public class LocalGenre {

	@Id
    @Column(name = "GenreID", updatable = false, nullable = false)
	private int genreID;
	
	@Column(name = "GenreName")
	private String genreName;
	
	public int getGenreID() {
		return genreID;
	}
	public void setGenreID(int genreID) {
		this.genreID = genreID;
	}
	public String getGenreName() {
		return genreName;
	}
	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}
	
	
	//Everything from down here is highly experimental
	@ManyToMany(fetch=FetchType.EAGER,
			cascade = {CascadeType.ALL})
	@JoinTable(
			name="AlbumGenres",
			joinColumns={@JoinColumn(name="GenreID")},
			inverseJoinColumns={@JoinColumn(name="AlbumID")}
			)
	private List<LocalAlbum> genreAlbums;
	
	public List<LocalAlbum> getGenreAlbums(){
		return genreAlbums;
	}
	
	public void setGenreAlbums(List<LocalAlbum> genreAlbums) {
		this.genreAlbums = genreAlbums;
	}
	public void addAlbum(LocalAlbum album) {
		if(genreAlbums == null) {
			genreAlbums = new ArrayList<>();
		}
		genreAlbums.add(album);
	}
	
	public LocalGenre() {}
}
