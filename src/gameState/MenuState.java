package gameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import audio.JukeBox;
import entity.PlayerSave;
import handlers.Keys;
import main.GamePanel;

//Handles the game menu
public class MenuState extends GameState {
	private BufferedImage head;								// HUD
	private int currentChoice = 0;							// User choice
	private String[] options = {"Start","Controls", "Quit"};// Menu Options
	private Color titleColor;								// Color for title
	private Font titleFont;									// Font for menu title
	private Font font;										// Font for menu options
	private Font font2;										// Font for signature
	
	// Constructor
	public MenuState(GameStateManager manager) {
		super(manager);
		try {	
			// load floating head
			head = ImageIO.read(getClass().getResourceAsStream("/HUD/Hud.gif")).getSubimage(0, 12, 12, 11);	
			// titles and fonts
			titleColor = Color.WHITE;
			titleFont = new Font("Times New Roman", Font.ITALIC, 28);
			font = new Font("Arial", Font.ITALIC, 14);
			font2 = new Font("Arial", Font.PLAIN, 10);
			// load sfx
			JukeBox.load("/SFX/menuoption.mp3", "menuoption");
			JukeBox.load("/SFX/menuselect.mp3", "menuselect");
		}
		catch(Exception e) {
			e.printStackTrace();
		}	
	}
	
	// Initialize
	public void init() {}
	
	// Update
	public void update() {
		// check keys
		input();
	}
	
	public void draw(Graphics2D graphics) {
		// draw bg
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		// draw title
		graphics.setColor(titleColor);
		graphics.setFont(titleFont);
		graphics.drawString("A S T O R A", 90, 90);
		// draw menu options
		graphics.setFont(font);
		graphics.setColor(Color.WHITE);
		graphics.drawString("Start", 135, 165);
		graphics.drawString("Controls", 135, 185);
		graphics.drawString("Quit", 135, 205);
		// draw floating head
		if(currentChoice == 0) {
			graphics.drawImage(head, 115, 154, null);
		}
		if(currentChoice == 1) {
			graphics.drawImage(head, 115, 174, null);
		}
		if(currentChoice == 2) {
			graphics.drawImage(head, 115, 194, null);
		}
		// other
		graphics.setFont(font2);
		graphics.drawString("2021 Brandon R.", 10, 232);
	}
	
	// User choice
	private void select() {
		if(currentChoice == 0) {
			JukeBox.play("menuselect");
			PlayerSave.init();
			manager.setState(GameStateManager.LEVEL1ASTATE);
		}
		else if(currentChoice == 1) {
			JukeBox.play("menuselect");
			manager.setState(GameStateManager.CONTROLSTATE);
		}
		else if(currentChoice == 2) {
			System.exit(0);
		}
	}
	
	// Interpret user inputs
	public void input() {
		if(Keys.isPressed(Keys.ENTER)) {
			select();
		}
		if(Keys.isPressed(Keys.UP)) {
			if(currentChoice > 0) {
				JukeBox.play("menuoption", 0);
				currentChoice--;
			}
		}
		if(Keys.isPressed(Keys.DOWN)) {
			if(currentChoice < options.length - 1) {
				JukeBox.play("menuoption", 0);
				currentChoice++;
			}
		}
	}
}