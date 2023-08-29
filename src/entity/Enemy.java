package entity;

import audio.JukeBox;
import tileMap.TileMap;

//Controls the enemies of the game
public class Enemy extends MapObject {
	protected int health;		// Enemy current health
	protected int maxHealth;	// Enemy max health
	protected boolean dead;		// Enemy is dead
	protected int damage;		// Enemy damage
	protected boolean remove;	// Remove enemy
	protected boolean flinching;// Is player flinching
	protected long flinchCount;	// Flinch duration
	
	// Constructor
	public Enemy(TileMap tm) {
		super(tm);
		remove = false;
	}
	
	// Is the enemy dead
	public boolean isDead() { 
		return dead; 
	}
	
	// Remove enemy
	public boolean shouldRemove() { 
		return remove; 
	}
	
	// Player receives damage
	public int getDamage() { 
		return damage; 
	}
	
	// Register enemy collision
	public void hit(int damage) {
		if(dead || flinching) {
			return;
		}
		JukeBox.play("enemyhit");
		health -= damage;
		if(health < 0) {
			health = 0;
		}
		if(health == 0) {
			dead = true;
		}
		if(dead) {
			remove = true;
		}
		flinching = true;
		flinchCount = 0;
	}
	
	// Update
	public void update() {}
}