package entity;

import java.awt.Rectangle;
import main.GamePanel;
import tileMap.Tile;
import tileMap.TileMap;

//Controls the map
public abstract class MapObject {
	// tile stuff
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	// position and vector
	protected double x;
	protected double y;
	protected double dx;
	protected double dy;
	// dimensions
	protected int width;
	protected int height;
	// collision box
	protected int cwidth;
	protected int cheight;
	// collision
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	// animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;
	// movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	// movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;
	
	// constructor
	public MapObject(TileMap tm) {
		tileMap = tm;
		tileSize = tm.getTileSize();
		animation = new Animation();
		facingRight = true;
	}
	
	// Points of intersection for the map
	public boolean intersects(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	
	// Points of intersection for the camera
	public boolean intersects(Rectangle r) {
		return getRectangle().intersects(r);
	}
	
	// Container for the map
	public boolean contains(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.contains(r2);
	}
	
	// Container for the camera
	public boolean contains(Rectangle r) {
		return getRectangle().contains(r);
	}
	
	// Camera rectangle
	public Rectangle getRectangle() {
		return new Rectangle(
				(int)x - cwidth / 2,
				(int)y - cheight / 2,
				cwidth,
				cheight
		);
	}
	
	// Determine the corners of the screen
	public void calculateCorners(double x, double y) {
		int leftTile = (int)(x - cwidth / 2) / tileSize;
		int rightTile = (int)(x + cwidth / 2 - 1) / tileSize;
		int topTile = (int)(y - cheight / 2) / tileSize;
		int bottomTile = (int)(y + cheight / 2 - 1) / tileSize;
		if(topTile < 0 || bottomTile >= tileMap.getNumRows() ||
			leftTile < 0 || rightTile >= tileMap.getNumCols()) {
			topLeft = topRight = bottomLeft = bottomRight = false;
			return;
		}
		int tl = tileMap.getType(topTile, leftTile);
		int tr = tileMap.getType(topTile, rightTile);
		int bl = tileMap.getType(bottomTile, leftTile);
		int br = tileMap.getType(bottomTile, rightTile);
		topLeft = tl == Tile.BLOCKED;
		topRight = tr == Tile.BLOCKED;
		bottomLeft = bl == Tile.BLOCKED;
		bottomRight = br == Tile.BLOCKED;
	}
	
	// Compare player position to map
	public void checkTileMapCollision() {
		currCol = (int)x / tileSize;
		currRow = (int)y / tileSize;
		xdest = x + dx;
		ydest = y + dy;
		xtemp = x;
		ytemp = y;
		calculateCorners(x, ydest);
		if(dy < 0) {
			if(topLeft || topRight) {
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		calculateCorners(xdest, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		if(!falling) {
			calculateCorners(x, ydest + 1);
			if(!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
	}
	
	// Retrieve horizontal position
	public int getx() { 
		return (int)x; 
	}
	
	// Retrieve vertical position
	public int gety() { 
		return (int)y; 
	}
	
	// Get width of map
	public int getWidth() {
		return width;
	}
	
	// Get height of map
	public int getHeight() {
		return height;
	}
	
	// Get width of camera
	public int getCWidth() { 
		return cwidth;
	}
	
	// Get height of camera
	public int getCHeight() { 
		return cheight;
	}
	
	// Is the player facing right
	public boolean isFacingRight() { 
		return facingRight; 
	}
	
	// Set the map position
	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	// Set the map speed
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	// Set the position of the map
	public void setMapPosition() {
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}
	
	// Set the left side
	public void setLeft(boolean b) {
		left = b; 
	}
	
	// Set the right side
	public void setRight(boolean b) { 
		right = b; 
	}
	
	// Set the top 
	public void setUp(boolean b) {
		up = b; 
	}
	
	// Set the bottom
	public void setDown(boolean b) { 
		down = b; 
	}
	
	// Set jump
	public void setJumping(boolean b) { 
		jumping = b; 
	}
	
	// Determine if object is offscreen
	public boolean notOnScreen() {
		return x + xmap + width < 0 ||
			x + xmap - width > GamePanel.WIDTH ||
			y + ymap + height < 0 ||
			y + ymap - height > GamePanel.HEIGHT;
	}
	
	// Display
	public void draw(java.awt.Graphics2D graphics) {
		setMapPosition();
		if(facingRight) {
			graphics.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2),
				(int)(y + ymap - height / 2),
				null
			);
		}
		else {
			graphics.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2 + width),
				(int)(y + ymap - height / 2),
				-width,
				height,
				null
			);
		}
	}
}