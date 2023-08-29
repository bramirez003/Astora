package gameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import audio.JukeBox;
import handlers.Keys;
import main.GamePanel;

//Handles the control menu
public class ControlState extends GameState {
	private Color titleColor;								// Color for title
	private Font titleFont;									// Font for menu title
	private Font font;										// Font for menu options
	
	// Constructor
	public ControlState(GameStateManager manager) {
		super(manager);
		init();	
	}
	
	// Initialize
	public void init() {		
		try {	
		// titles and fonts
		titleColor = Color.WHITE;
		titleFont = new Font("Times New Roman", Font.ITALIC, 20);
		font = new Font("Arial", Font.ITALIC, 14);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Update
	public void update() {
		// check keys
		input();
	}
	
	// Draw controls
	public void draw(Graphics2D graphics) {
		// draw bg
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		// draw title
		graphics.setColor(titleColor);
		graphics.setFont(titleFont);
		graphics.drawString("Controls", 120, 20);
		// draw menu options
		graphics.setFont(font);
		graphics.setColor(Color.WHITE);
		graphics.drawString("W = UP or JUMP", 110, 60);
		graphics.drawString("A = LEFT", 110, 80);
		graphics.drawString("S = DOWN", 110, 100);
		graphics.drawString("D = RIGHT", 110, 120);
		graphics.drawString("SPACE = JUMP", 110, 140);
		graphics.drawString("J = ATTACK", 110, 160);
		graphics.drawString("K = CHARGE", 110, 180);
		graphics.drawString("Press ESCAPE to return to MENU", 55, 230);
	}

	// Handles user input
	public void input() {
		if(Keys.isPressed(Keys.ESCAPE)) {
			JukeBox.play("menuselect");
			manager.setState(GameStateManager.MENUSTATE);
		}
	}
}