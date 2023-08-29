package gameState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import handlers.Keys;
import main.GamePanel;

//Handles adventurer being poisoned
public class PoisonState extends GameState {
	private float hue;
	private Color color;
	private double angle;
	private BufferedImage image;
	
	// Constructor
	public PoisonState(GameStateManager manager) {
		super(manager);
		try {
			image = ImageIO.read(
			getClass().getResourceAsStream(
			"/Sprites/Player/Adventurer.gif"
			)).getSubimage(0, 0, 40, 40);
		}
		catch(Exception e) {}
	}
	
	// Initialize
	public void init() {}
	
	// Update
	public void update() {
		input();
		color = Color.getHSBColor(hue, 1f, 1f);
		hue += 0.01;
		if(hue > 1) {
			hue = 0;
		}
		angle += 0.1;
	}
	
	// Draw poison effect
	public void draw(Graphics2D graphics) {
		graphics.setColor(color);
		graphics.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		AffineTransform at = new AffineTransform();
		at.translate(GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2);
		at.rotate(angle);
		graphics.drawImage(image, at, null);
	}
	
	// Handle user input
	public void input() {
		if(Keys.isPressed(Keys.ESCAPE)) manager.setState(GameStateManager.MENUSTATE);
	}
}