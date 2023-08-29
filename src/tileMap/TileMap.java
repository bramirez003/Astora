package tileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import main.GamePanel;

//Controls all of the tile map information
public class TileMap {
	private double x;				// Player x coordinate
	private double y;				// Player y coordinate
	private int xmin;				// The left bound of the screen
	private int xmax;				// The right bound of the screen
	private int ymin;				// The bottom bound of the screen
	private int ymax;				// The top bound of the screen
	private double camera;			// Player camera tracker
	private int[][] map;			// The tile map information
	private int tileSize;			// The size of the tiles
	private int numRows;			// The number of tile rows
	private int numCols;			// The number of tile columns
	private int width;				// The width of the tile
	private int height;				// The height of the tile
	private BufferedImage tileset;	// The tile image
	private int numTilesAcross;		// How many tiles are there in the width
	private Tile[][] tiles;			// The tile information for the map
	private int rowOffset;			// Only load what can be seen vertically
	private int colOffset;			// Only load what can be seen horizontally
	private int numRowsToDraw;		// How many rows to display at any time
	private int numColsToDraw;		// How many columns to display at any time
	private boolean shaking;		// Cause the screen to shake
	private int intensity;			// How violently will the screen shake
	
	// Tile map constructor
	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		camera = 0.07;
	}
	
	// Load tiles to the image
	public void loadTiles(String s) {
		try {
			tileset = ImageIO.read(getClass().getResourceAsStream(s));
			numTilesAcross = tileset.getWidth() / tileSize;
			tiles = new Tile[2][numTilesAcross];
			BufferedImage subimage;
			for(int col = 0; col < numTilesAcross; col++) {
				subimage = tileset.getSubimage(col * tileSize, 0, tileSize, tileSize);
				tiles[0][col] = new Tile(subimage, Tile.NORMAL);
				subimage = tileset.getSubimage(col * tileSize, tileSize, tileSize, tileSize);
				tiles[1][col] = new Tile(subimage, Tile.BLOCKED);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}	
	}
	
	// Load tile map information
	public void loadMap(String s) {
		try {
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;
			xmin = GamePanel.WIDTH - width;
			xmax = 0;
			ymin = GamePanel.HEIGHT - height;
			ymax = 0;
			String delims = "\\s+";
			for(int row = 0; row < numRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for(int col = 0; col < numCols; col++) {
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Return the size of the tile
	public int getTileSize() { 
		return tileSize; 
	}
	
	// Return the x position
	public double getx() { 
		return x; 
	}
	
	// Return the y position
	public double gety() {
		return y; 
	}
	
	// Get the width of the screen
	public int getWidth() { 
		return width; 
	}
	
	// Get the height of the screen
	public int getHeight() { 
		return height; 
	}
	
	// The number of rows on the screen
	public int getNumRows() { 
		return numRows;
	}
	
	// The number of columns on the screen
	public int getNumCols() { 
		return numCols; 
	}
	
	// Get the type of tile
	public int getType(int row, int col) {
		int rowCol = map[row][col];
		int r = rowCol / numTilesAcross;
		int c = rowCol % numTilesAcross;
		return tiles[r][c].getType();
	}
	
	// Check if the screen should be shaking
	public boolean isShaking() { 
		return shaking;
	}
	
	// Set the camera
	public void setCamera(double d) { 
		camera = d; 
	}
	
	// Shake the screen
	public void setShaking(boolean b, int i) { 
		shaking = b; 
		intensity = i; 
	}
	
	// Set the bounds of the tile
	public void setBounds(int left, int bottom, int right, int top) {
		xmin = GamePanel.WIDTH - left;
		ymin = GamePanel.WIDTH - bottom;
		xmax = right;
		ymax = top;
	}
	
	// Set position of the tile
	public void setPosition(double x, double y) {
		this.x += (x - this.x) * camera;
		this.y += (y - this.y) * camera;
		fixBounds();
		colOffset = (int)-this.x / tileSize;
		rowOffset = (int)-this.y / tileSize;
	}
	
	// Set the bounds of the tiles
	public void fixBounds() {
		if(x < xmin) {
			x = xmin;
		}
		if(y < ymin) {
			y = ymin;
		}
		if(x > xmax) {
			x = xmax;
		}
		if(y > ymax) {
			y = ymax;
		}
	}
	
	// Update tile image
	public void update() {
		if(shaking) {
			this.x += Math.random() * intensity - intensity / 2;
			this.y += Math.random() * intensity - intensity / 2;
		}
	}
	
	// Draw the tiles
	public void draw(Graphics2D graphics) {
		for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {
			if(row >= numRows) {
				break;
			}
			for(int col = colOffset; col < colOffset + numColsToDraw; col++) {
				if(col >= numCols) {
					break;
				}
				if(map[row][col] == 0) {
					continue;
				}
				int rowCol = map[row][col];
				int r = rowCol / numTilesAcross;
				int c = rowCol % numTilesAcross;
				graphics.drawImage(tiles[r][c].getImage(), (int)x + col * tileSize, (int)y + row * tileSize, null);		
			}	
		}	
	}	
}