package tileMap;

import java.awt.image.BufferedImage;

//Controls tile information
public class Tile {
	public static final int NORMAL = 0;	// Normal type of tile
	public static final int BLOCKED = 1;// Block type of tile
	private BufferedImage image;		// Tile image
	private int type;					// Type of block

	// Tile constructor
	public Tile(BufferedImage image, int type) {
		this.image = image;
		this.type = type;
	}
	
	// Tile image
	public BufferedImage getImage() { 
		return image; 
	}
	
	// Retrieve the type of the tile
	public int getType() { 
		return type; 
	}
}