package entityEnemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import entity.Enemy;
import entity.Player;
import handlers.Content;
import main.GamePanel;
import tileMap.TileMap;

//Contains all the information for the slime entity
public class Slime extends Enemy {
	private BufferedImage[] sprites;// Contains sprite information
	private Player player;			// Relation to the the player entity
	private boolean active;			// The activity of a slime
	
	// Slime Constructor
	public Slime(TileMap tm, Player p) {
		super(tm);
		player = p;
		health = maxHealth = 1;
		width = 25;
		height = 25;
		cwidth = 20;
		cheight = 18;
		damage = 1;
		moveSpeed = 0.8;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -5;
		sprites = Content.Slime[0];
		animation.setFrames(sprites);
		animation.setDelay(4);
		left = true;
		facingRight = false;
	}
	
	// Retrieve the next position of the slime 
	private void getNextPosition() {
		if(left) dx = -moveSpeed;
		else if(right) {
			dx = moveSpeed;
		}
		else {
			dx = 0;
		}
		if(falling) {
			dy += fallSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
		if(jumping && !falling) {
			dy = jumpStart;
		}
	}
	
	// Update the current state of the slime
	public void update() {
		if(!active) {
			if(Math.abs(player.getx() - x) < GamePanel.WIDTH) active = true;
			return;
		}
		// check if done flinching
		if(flinching) {
			flinchCount++;
			if(flinchCount == 6) {
				flinching = false;
			}
		}
		getNextPosition();
		checkTileMapCollision();
		calculateCorners(x, ydest + 1);
		if(!bottomLeft) {
			left = false;
			right = facingRight = true;
		}
		if(!bottomRight) {
			left = true;
			right = facingRight = false;
		}
		setPosition(xtemp, ytemp);
		if(dx == 0) {
			left = !left;
			right = !right;
			facingRight = !facingRight;
		}
		// update animation
		animation.update();
	}
	
	// Draw the slime the screen
	public void draw(Graphics2D graphics) {
		if(flinching) {
			if(flinchCount == 0 || flinchCount == 2) {
				return;
			}
		}
		super.draw(graphics);
	}
}