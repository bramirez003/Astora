package entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import handlers.Content;
import tileMap.TileMap;

//Controls explosion effects
public class Explosion extends MapObject {
	private BufferedImage[] sprites;// Explosion sprites
	private boolean remove;			// Remove explosion
	private Point[] points;			// Points of explosion
	private int speed;				// Speed of explosion 
	private double diagSpeed;		// Trajectory speed of explosion
	
	// Constructor
	public Explosion(TileMap tm, int x, int y) {
		super(tm);
		this.x = x;
		this.y = y;
		width = 30;
		height = 30;
		speed = 2;
		diagSpeed = 1.41;
		sprites = Content.Explosion[0];
		animation.setFrames(sprites);
		animation.setDelay(6);
		points = new Point[8];
		for(int i = 0; i < points.length; i++) {
			points[i] = new Point(x, y);
		}
	}
	
	// Update frame
	public void update() {
		animation.update();
		if(animation.hasPlayedOnce()) {
			remove = true;
		}
		points[0].x += speed;
		points[1].x += diagSpeed;
		points[1].y += diagSpeed;
		points[2].y += speed;
		points[3].x -= diagSpeed;
		points[3].y += diagSpeed;
		points[4].x -= speed;
		points[5].x -= diagSpeed;
		points[5].y -= diagSpeed;
		points[6].y -= speed;
		points[7].x += diagSpeed;
		points[7].y -= diagSpeed;
	}
	
	// Remove explosion
	public boolean shouldRemove() { 
		return remove; 
	}

	// Display explosion
	public void draw(Graphics2D graphics) {
		setMapPosition();
		for(int i = 0; i < points.length; i++) {
			graphics.drawImage(
				animation.getImage(),
				(int) (points[i].x + xmap - width / 2),
				(int) (points[i].y + ymap - height / 2),
				null
			);
		}
	}	
}