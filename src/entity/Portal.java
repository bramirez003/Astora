package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import tileMap.TileMap;

//Controls the portal for the boss
public class Portal extends MapObject {
	private BufferedImage[] closedSprites;	// Closed portal sprites
	private BufferedImage[] openingSprites;	// Opening portal sprites
	private BufferedImage[] openedSprites;	// Opened portal sprites
	private boolean opened;					// Portal is open
	private boolean opening;				// THe opening for the portal
	
	// Constructor
	public Portal(TileMap tm) {
		super(tm);
		width = 81;
		height = 111;
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream("/Sprites/Other/Portal.gif")
			);
			closedSprites = new BufferedImage[1];
			closedSprites[0] = spritesheet.getSubimage(0, 0, width, height);
			openingSprites = new BufferedImage[6];
			for(int i = 0; i < openingSprites.length; i++) {
				openingSprites[i] = spritesheet.getSubimage(
					i * width, height, width, height
				);
			}
			openedSprites = new BufferedImage[3];
			for(int i = 0; i < openedSprites.length; i++) {
				openedSprites[i] = spritesheet.getSubimage(
					i * width, 2 * height, width, height
				);
			}
			animation.setFrames(closedSprites);
			animation.setDelay(-1);	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Close the portal
	public void setClosed() {
		animation.setFrames(closedSprites);
		animation.setDelay(-1);
		opened = false;
	}
	
	// Set the portal to opening
	public void setOpening() {
		opening = true;
		animation.setFrames(openingSprites);
		animation.setDelay(2);
	}
	
	// Set the portal to open
	public void setOpened() {
		if(opened) {
			return;
		}
		animation.setFrames(openedSprites);
		animation.setDelay(2);
		opened = true;
	}
	
	// Is the portal open
	public boolean isOpened() { 
		return opened; 
	}
	
	// Update
	public void update() {
		animation.update();
		if(opening && animation.hasPlayedOnce()) {
			opened = true;
			animation.setFrames(openedSprites);
			animation.setDelay(2);
		}
	}
	
	// Display
	public void draw(Graphics2D graphics) {
		super.draw(graphics);
	}
}