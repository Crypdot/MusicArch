package com.jcg.hibernate.maven;
import java.io.Serializable;
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

import org.hibernate.mapping.Collection;

@Entity
@Table(name = "Genre")
public class Genre implements Serializable {
	// @MappedBy("Album")
	// @ManyToMany("")
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GenreID", updatable = false, nullable = false)
	private int genreID;
	
	@Column(name = "GenreNimi")
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
	
	@ManyToMany(fetch=FetchType.LAZY,
			cascade={CascadeType.ALL})
	@JoinTable(
			name="koostuu",
			joinColumns={@JoinColumn(name="GenreID")},
			inverseJoinColumns={@JoinColumn(name="AlbumiID")}
			)
	private List<Album> genreAlbums;
	
	public List<Album> getGenreAlbums(){
		return genreAlbums;
	}
	public void setGenreAlbums(List<Album> genreAlbums) {
		this.genreAlbums = genreAlbums;
	}
	public void addAlbum(Album album) {
		if(genreAlbums == null) {
			genreAlbums = new ArrayList<>();
		}
		genreAlbums.add(album);
	}
	
	public Genre() {}
}
