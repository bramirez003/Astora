package entityOrb;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import entity.MapObject;
import tileMap.TileMap;

//Controls top left piece of orb
public class TopLeftPiece extends MapObject{
	private BufferedImage[] sprites;// Orb sprite
	
	// Constructor
	public TopLeftPiece(TileMap tm) {
		super(tm);
		try {	
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream("/Sprites/Other/Orb.gif")
			);
			sprites = new BufferedImage[1];
			width = height = 4;
			sprites[0] = spritesheet.getSubimage(0, 0, 10, 10);
			animation.setFrames(sprites);
			animation.setDelay(-1);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Update
	public void update() {
		x += dx;
		y += dy;
		animation.update();
	}
	
	// Display
	public void draw(Graphics2D graphics) {
		super.draw(graphics);
	}
}