package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import tileMap.TileMap;

//Controls the teleporter at the end of each level
public class Teleport extends MapObject {
	private BufferedImage[] sprites;// Teleport sprite
	
	// Constructor
	public Teleport(TileMap tm) {
		super(tm);
		facingRight = true;
		width = height = 40;
		cwidth = 20;
		cheight = 40;
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream("/Sprites/Player/Teleport.gif")
			);
			sprites = new BufferedImage[9];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
					i * width, 0, width, height
				);
			}
			animation.setFrames(sprites);
			animation.setDelay(1);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Update
	public void update() {
		animation.update();
	}
	
	// Display
	public void draw(Graphics2D graphics) {
		super.draw(graphics);
	}	
}