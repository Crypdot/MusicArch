package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "WishList")
public class WishList {

	@Id
	@Column(name = "wishListID", updatable = false, nullable = false)
	private int wishListID;
	
	@Column(name = "albumID")
	private int albumID;
	
	public int getWishListID() {
		return this.wishListID;
	}
	
	public void setWishListID(int id) {
		this.wishListID = id;
	}
	
	public int getAlbumID() {
		return this.albumID;
	}
	
	public void setAlbumID(int id) {
		this.albumID = id;
	}
	
}
