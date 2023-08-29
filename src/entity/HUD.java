package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

//Controls the functions of the Heads Up Display (HUD)
public class HUD {
	private Player player;		// The player entity
	private BufferedImage heart;// THe player heart display
	private BufferedImage life;	// The player life display
	
	// Constructor
	public HUD(Player p) {
		player = p;
		try {
			BufferedImage image = ImageIO.read(
				getClass().getResourceAsStream(
					"/HUD/Hud.gif"
				)
			);
			heart = image.getSubimage(0, 0, 13, 12);
			life = image.getSubimage(0, 12, 12, 11);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Display
	public void draw(Graphics2D graphics) {
		for(int i = 0; i < player.getHealth(); i++) {
			graphics.drawImage(heart, 10 + i * 15, 10, null);
		}
		for(int i = 0; i < player.getLives(); i++) {
			graphics.drawImage(life, 10 + i * 15, 25, null);
		}
		graphics.setColor(java.awt.Color.WHITE);
		graphics.drawString(player.getTimeToString(), 290, 15);
	}
}