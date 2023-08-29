package tileMap;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import main.GamePanel;

//Controls all the information related to the background image of the game
public class Background {
	private BufferedImage image;// The background image
	private double x;			// The current horizontal distance
	private double y;			// The current vertical distance
	private double dx;			// The horizontal scrolling speed
	private double dy;			// The vertical scrolling speed
	private int width;			// The width of the background image
	private int height;			// The height of the background image
	private double xscale;		// The horizontal scale for the background image
	private double yscale;		// The vertical scale for the background image
	
	// Constructor
	public Background(String s) {
		this(s, 0.1);
	}
	
	// Constructor
	public Background(String s, double d) {
		this(s, d, d);
	}
	
	// Constructor
	public Background(String s, double horizontal, double vertical) {
		try {
			image = ImageIO.read(getClass().getResourceAsStream(s));
			width = image.getWidth();
			height = image.getHeight();
			xscale = horizontal;
			yscale = vertical;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Constructor
	public Background(String s, double move, int x, int y, int w, int h) {
		try {
			image = ImageIO.read(getClass().getResourceAsStream(s));
			image = image.getSubimage(x, y, w, h);
			width = image.getWidth();
			height = image.getHeight();
			xscale = move;
			yscale = move;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Current position of the background image
	public void setPosition(double x, double y) {
		this.x = (x * xscale) % width;
		this.y = (y * yscale) % height;
	}
	
	// Set scrolling speeds
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	// Set image scale
	public void setScale(double xscale, double yscale) {
		this.xscale = xscale;
		this.yscale = yscale;
	}
	
	// Set the dimensions of the background image
	public void setDimensions(int w, int h) {
		width = w;
		height = h;
	}
	
	// Retrieve the x-coordinate
	public double getx() { 
		return x; 
	}
	
	// Retrieve the y-coordinate
	public double gety() {
		return y; 
	}
	
	// Update the background to the next frame
	public void update() {
		x += dx;
		while(x <= -width) {
			x += width;
		}
		while(x >= width) {
			x -= width;
		}
		y += dy;
		while(y <= -height) {
			y += height;
		}
		while(y >= height) {
			y -= height;
		}
	}
	
	// Create the current state of the background
	public void draw(Graphics2D graphics) {
		graphics.drawImage(image, (int)x, (int)y, null);
		if(x < 0) {
			graphics.drawImage(image, (int)x + GamePanel.WIDTH, (int)y, null);
		}
		if(x > 0) {
			graphics.drawImage(image, (int)x - GamePanel.WIDTH, (int)y, null);
		}
		if(y < 0) {
			graphics.drawImage(image, (int)x, (int)y + GamePanel.HEIGHT, null);
		}
		if(y > 0) {
			graphics.drawImage(image, (int)x, (int)y - GamePanel.HEIGHT, null);
		}
	}
}