package handlers;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

//Upon booting load resources
public class Content {
	public static final BufferedImage[][] Gel = null;
	public static BufferedImage[][] Explosion = load("/Sprites/Enemies/Explosion.gif", 30, 30);
	public static BufferedImage[][] Bat = load("/Sprites/Enemies/Bat.gif", 39, 20);
	public static BufferedImage[][] Slime = load("/Sprites/Enemies/Slime.gif", 25, 25);
	public static BufferedImage[][] BatDeath = load("/Sprites/Enemies/BatDie.gif", 39, 20);
	public static BufferedImage[][] SlimeDeath = load("/Sprites/Enemies/SlimeDie.gif", 25, 25);
	public static BufferedImage[][] DarkEnergy = load("/Sprites/Enemies/DarkEnergy.gif", 20, 20);
	public static BufferedImage[][] load(String s, int w, int h) {
		BufferedImage[][] ret;
		try {
			BufferedImage spritesheet = ImageIO.read(Content.class.getResourceAsStream(s));
			int width = spritesheet.getWidth() / w;
			int height = spritesheet.getHeight() / h;
			ret = new BufferedImage[height][width];
			for(int i = 0; i < height; i++) {
				for(int j = 0; j < width; j++) {
					ret[i][j] = spritesheet.getSubimage(j * w, i * h, w, h);
				}
			}
			return ret;
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("Error loading graphics.");
			System.exit(0);
		}
		return null;
	}
}