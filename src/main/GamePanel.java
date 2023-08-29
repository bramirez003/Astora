package main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import gameState.GameStateManager;
import handlers.Keys;

//Controls the interactions with the game window
@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener{
	public static final int WIDTH = 320;	// Game window width
	public static final int HEIGHT = 240;	// Game window height
	public static final int SCALE = 4;		// Game window scale
	private Thread thread;					// The operation thread
	private boolean running;				// Check if the game is running
	private int FPS = 60;					// The frame rate of the game
	private long targetTime = 1000 / FPS;	// Attempt to reach the frame rate
	private BufferedImage image;			// The image for the window
	private Graphics2D graphics;			// The application for the graphics
	private GameStateManager manager;		// The master control of the game state
	private boolean recording = false;		// Is the time being recorded
	private int recordingCount = 0;			// How much time has elapsed since recording
	private boolean screenshot;				// Capture game state
	
	// The interactable game window
	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		setFocusable(true);
		requestFocus();
	}
	
	// Notify if thread is empty
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			addKeyListener(this);
			thread.start();
		}
	}
	
	// Initialize
	private void init() {
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		graphics = (Graphics2D) image.getGraphics();
		running = true;
		manager = new GameStateManager();	
	}
	
	// The game loop
	public void run() {
		init();
		long start;
		long elapsed;
		long wait;
		while(running) {
			start = System.nanoTime();
			update();
			draw();
			drawToScreen();
			elapsed = System.nanoTime() - start;
			wait = targetTime - elapsed / 1000000;
			if(wait < 0) {
				wait = 5;
			}
			try {
				Thread.sleep(wait);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// Update the frame of the screen
	private void update() {
		manager.update();
		Keys.update();
	}
	
	// Draw the content
	private void draw() {
		manager.draw(graphics);
	}
	
	// Draw the content to the screen
	private void drawToScreen() {
		Graphics graphics2 = getGraphics();
		graphics2.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		graphics2.dispose();
		if(screenshot) {
			screenshot = false;
			try {
				java.io.File out = new java.io.File("screenshot " + System.nanoTime() + ".gif");
				javax.imageio.ImageIO.write(image, "gif", out);
			}
			catch(Exception e) {}
		}
		if(!recording) {
			return;
		}
		try {
			java.io.File out = new java.io.File("C:\\out\\frame" + recordingCount + ".gif");
			javax.imageio.ImageIO.write(image, "gif", out);
			recordingCount++;
		}
		catch(Exception e) {}
	}
	
	// See if a key is typed
	public void keyTyped(KeyEvent key) {}
	
	// See if a key is held
	public void keyPressed(KeyEvent key) {
		if(key.isControlDown()) {
			if(key.getKeyCode() == KeyEvent.VK_O) {
				recording = !recording;
				return;
			}
			if(key.getKeyCode() == KeyEvent.VK_P) {
				screenshot = true;
				return;
			}
		}
		Keys.keySet(key.getKeyCode(), true);
	}
	
	// Listen for a key being released
	public void keyReleased(KeyEvent key) {
		Keys.keySet(key.getKeyCode(), false);
	}
}