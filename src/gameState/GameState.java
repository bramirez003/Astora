package gameState;

import java.awt.Graphics2D;

//Wrapper for the state of the game
public abstract class GameState {
	protected GameStateManager manager;// Game state controller
	
	// Constructor
	public GameState(GameStateManager manager) {
		this.manager = manager;
	}
	
	// Initialize
	public abstract void init();
	
	// Update current state
	public abstract void update();
	
	// Draw graphics to screen
	public abstract void draw(Graphics2D graphics);
	
	// Interpret user choice
	public abstract void input();
}