package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import main.GamePanel;

// Create the title for the game
public class Title {
	public BufferedImage image;	// Title image
	public int count;			// Counter
	private boolean done;		// Is the process done
	private boolean remove;		// Remove title when appropriate
	private double x;			// The x position of the title
	private double y;			// The y position of the title
	private double dx;			// How it should the be spread across the screen
	private int width;			// THe width of the title
	
	// The title string
	public Title(String s) {
		try {
			image = ImageIO.read(getClass().getResourceAsStream(s));
			width = image.getWidth();
			x = -width;
			done = false;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Create the title
	public Title(BufferedImage image) {
		this.image = image;
		width = image.getWidth();
		x = -width;
		done = false;
	}
	
	// The height of the title
	public void sety(double y) { 
		this.y = y; 
	}
	
	// Load the title
	public void begin() {
		dx = 10;
	}
	
	// Check if the title should be removed
	public boolean shouldRemove() { 
		return remove; 
	}
	
	// Update the state of the title
	public void update() {
		if(!done) {
			if(x >= (GamePanel.WIDTH - width) / 2) {
				x = (GamePanel.WIDTH - width) / 2;
				count++;
				if(count >= 120) {
					done = true;
				}
			}
			else {
				x += dx;
			}
		}
		else {
			x += dx;
			if(x > GamePanel.WIDTH) remove = true;
		}
	}
	
	// Draw the image
	public void draw(Graphics2D graphics) {
		graphics.drawImage(image, (int)x, (int)y, null);
	}
}