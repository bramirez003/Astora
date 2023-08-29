package gameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import handlers.Keys;
import main.GamePanel;

//Handles the pausing ability of the game
public class PauseState extends GameState {
	private Font font;// Font of the pause statement
	
	// Pause
	public PauseState(GameStateManager manager) {
		super(manager);
		// fonts
		font = new Font("Century Gothic", Font.PLAIN, 14);
	}
	
	// Initialize
	public void init() {}
	
	// Update
	public void update() {
		input();
	}
	
	// Display pause menu
	public void draw(Graphics2D graphics) {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		graphics.setColor(Color.WHITE);
		graphics.setFont(font);
		graphics.drawString("Game Paused", 115, 90);
	}
	
	// Handle user input
	public void input() {
		if(Keys.isPressed(Keys.ESCAPE)) {
			manager.setPaused(false);
		}
		if(Keys.isPressed(Keys.ENTER)) {
			manager.setPaused(false);
		}
	}
}