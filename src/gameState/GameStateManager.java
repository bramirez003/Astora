package gameState;

import audio.JukeBox;
import main.GamePanel;

//Controls the state of the game
public class GameStateManager {
	private GameState[] gameStates;				// Possible game states
	private int currentState;					// The current state of the game
	private PauseState pauseState;				// Pause the game
	private boolean paused;						// Is the game paused?
	public static final int NUMGAMESTATES = 16;	// Number of possible game states
	public static final int MENUSTATE = 0;		// The menu
	public static final int CONTROLSTATE = 1;	// The controls for the game
	public static final int LEVEL1ASTATE = 2;	// First part of level 1
	public static final int LEVEL1BSTATE = 3;	// Second part of level 1
	public static final int LEVEL1CSTATE = 4;	// Third part of level 1
	public static final int POISONSTATE = 15;	// A type of player state
	
	// Constructor
	public GameStateManager() {
		JukeBox.init();
		gameStates = new GameState[NUMGAMESTATES];
		pauseState = new PauseState(this);
		paused = false;
		currentState = MENUSTATE;
		loadState(currentState);
	}
	
	// Possible game states
	private void loadState(int state) {
		if(state == MENUSTATE)
			gameStates[state] = new MenuState(this);
		else if(state == CONTROLSTATE)
			gameStates[state] = new ControlState(this);
		else if(state == LEVEL1ASTATE)
			gameStates[state] = new Level1AState(this);
		else if(state == LEVEL1BSTATE)
			gameStates[state] = new Level1BState(this);
		else if(state == LEVEL1CSTATE)
			gameStates[state] = new Level1CState(this);
		else if(state == POISONSTATE)
			gameStates[state] = new PoisonState(this);
	}
	
	// Remove state of the game
	private void unloadState(int state) {
		gameStates[state] = null;
	}
	
	// Set the state of the game
	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}
	
	// Pause game
	public void setPaused(boolean b) { 
		paused = b; 
	}
	
	// Update
	public void update() {
		if(paused) {
			pauseState.update();
			return;
		}
		if(gameStates[currentState] != null) {
			gameStates[currentState].update();
		}
	}
	
	// Display game state
	public void draw(java.awt.Graphics2D graphics) {
		if(paused) {
			pauseState.draw(graphics);
			return;
		}
		if(gameStates[currentState] != null) {
			gameStates[currentState].draw(graphics);
		}
		else {
			graphics.setColor(java.awt.Color.BLACK);
			graphics.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		}
	}
}