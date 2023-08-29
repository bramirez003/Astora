package entityEnemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import entity.Enemy;
import handlers.Content;
import tileMap.TileMap;

//Contains all information for the Bat entity
public class Bat extends Enemy {
	private BufferedImage[] idleSprites;// Contains sprite information
	private int tick;					// Update tracker
	private double a;					// Random integer
	private double b;					// Random integer
	
	// Bat constructor
	public Bat(TileMap tm) {
		super(tm);
		health = maxHealth = 2;
		width = 39;
		height = 20;
		cwidth = 25;
		cheight = 15;
		damage = 1;
		moveSpeed = 5;
		idleSprites = Content.Bat[0];
		animation.setFrames(idleSprites);
		animation.setDelay(4);
		tick = 0;
		a = Math.random() * 0.06 + 0.07;
		b = Math.random() * 0.06 + 0.07;
	}
	
	// Update the state of the entity
	public void update() {
		// check if done flinching
		if(flinching) {
			flinchCount++;
			if(flinchCount == 6) {
				flinching = false;
			}
		}
		tick++;
		x = Math.sin(a * tick) + x;
		y = Math.sin(b * tick) + y;
		// update animation
		animation.update();
	}
	
	// Draw the enemy to the screen
	public void draw(Graphics2D graphics) {
		if(flinching) {
			if(flinchCount == 0 || flinchCount == 2) {
				return;
			}
		}
		super.draw(graphics);
	}
}