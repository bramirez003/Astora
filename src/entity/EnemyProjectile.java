package entity;

import java.awt.Graphics2D;
import tileMap.TileMap;

//Controls enemy projectiles
public abstract class EnemyProjectile extends MapObject {
	protected boolean hit;		// Projectile makes contact
	protected boolean remove;	// Remove projectile
	protected int damage;		// Projectile damage
	
	// Constructor
	public EnemyProjectile(TileMap tm) {
		super(tm);
	}
	
	// Projectile damage
	public int getDamage() { 
		return damage; 
	}
	
	// Remove projectile
	public boolean shouldRemove() { 
		return remove; 
	}
	
	// Set projectile contact
	public abstract void setHit();
	
	// Update
	public abstract void update();
	
	// Display graphics
	public void draw(Graphics2D graphics) {
		super.draw(graphics);
	}
}