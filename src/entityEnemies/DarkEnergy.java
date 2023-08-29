package entityEnemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import entity.Enemy;
import handlers.Content;
import tileMap.TileMap;

//The boss adds for the final stage of level 1
public class DarkEnergy extends Enemy {
	private BufferedImage[] startSprites;	// Starting sprites
	private BufferedImage[] sprites;		// The rest of the sprites
	private boolean start;					// Begin battle
	private boolean permanent;				// Can they be destroyed
	private int type = 0;					// Which type of energy
	public static int VECTOR = 0;			// Sideways trajectory
	public static int GRAVITY = 1;			// Downward trajectory
	public static int BOUNCE = 2;			// Bounce around enclosure
	private int bounceCount = 0;			// Number of bounces
	
	// Constructor
	public DarkEnergy(TileMap tm) {
		super(tm);
		health = maxHealth = 1;
		width = 20;
		height = 20;
		cwidth = 12;
		cheight = 12;
		damage = 1;
		moveSpeed = 5;
		startSprites = Content.DarkEnergy[0];
		sprites = Content.DarkEnergy[1];
		animation.setFrames(startSprites);
		animation.setDelay(2);
		start = true;
		flinching = true;
		permanent = false;	
	}
	
	// Set the type
	public void setType(int i) { 
		type = i; 
	}
	
	// Determine if permanent
	public void setPermanent(boolean b) { 
		permanent = b; 
	}
	
	// Update
	public void update() {
		if(start) {
			if(animation.hasPlayedOnce()) {
				animation.setFrames(sprites);
				animation.setNumFrames(3);
				animation.setDelay(2);
				start = false;
			}
		}
		if(type == VECTOR) {
			x += dx;
			y += dy;
		}
		else if(type == GRAVITY) {
			dy += 0.2;
			x += dx;
			y += dy;
		}
		else if(type == BOUNCE) {
			double dx2 = dx;
			double dy2 = dy;
			checkTileMapCollision();
			if(dx == 0) {
				dx = -dx2;
				bounceCount++;
			}
			if(dy == 0) {
				dy = -dy2;
				bounceCount++;
			}
			x += dx;
			y += dy;
		}
		// update animation
		animation.update();
		if(!permanent) {
			if(x < 0 || x > tileMap.getWidth() || y < 0 || y > tileMap.getHeight()) {
				remove = true;
			}
			if(bounceCount == 3) {
				remove = true;
			}
		}	
	}
	
	//Display enemy
	public void draw(Graphics2D graphics) {
		super.draw(graphics);
	}	
}