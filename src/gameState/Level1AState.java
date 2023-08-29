package gameState;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import audio.JukeBox;
import entity.Enemy;
import entity.Explosion;
import entity.HUD;
import entity.Player;
import entity.PlayerSave;
import entity.Teleport;
import entityEnemies.Bat;
import entityEnemies.Slime;
import handlers.Keys;
import main.GamePanel;
import tileMap.Background;
import tileMap.TileMap;

//Manages the first stage of level 1
public class Level1AState extends GameState {
	private Background sky;								// The sky for the background
	private Background clouds;							// The smoke for the background
	private Background mountains;						// The mountains for the background
	private Player player;								// The player entity
	private TileMap tileMap;							// The tilemap information
	private ArrayList<Enemy> enemies;					// The list of enemies for the level
	private ArrayList<Explosion> explosions;			// Explosion effects for enemies
	private HUD hud;									// Heads up display on top left of screen
	private Teleport teleport;							// Teleport to send player to next part of the level
	private boolean blockInput = false;					// Stop player input
	private int eventCount = 0;							// The number of events that have occurred
	private boolean eventStart;							// Start an event
	private ArrayList<Rectangle> tb;					// Event handler
	private boolean eventFinish;						// Has the event finished?
	private boolean eventDead;							// Player has died
	
	// Launch level 1
	public Level1AState(GameStateManager manager) {
		super(manager);
		init();
	}
	
	// Initialize
	public void init() {
		// backgrounds
		sky = new Background("/Backgrounds/sky.gif", 0);
		clouds = new Background("/Backgrounds/clouds.gif", 0.1);
		mountains = new Background("/Backgrounds/mountains.gif", 0.2);
		// tilemap
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/ruinstileset.gif");
		tileMap.loadMap("/Maps/level1a.map");
		tileMap.setPosition(140, 0);
		tileMap.setBounds(
			tileMap.getWidth() - 1 * tileMap.getTileSize(),
			tileMap.getHeight() - 2 * tileMap.getTileSize(),
			0, 0
		);
		tileMap.setCamera(1);
		// player
		player = new Player(tileMap);
		player.setPosition(300, 161);
		player.setHealth(PlayerSave.getHealth());
		player.setLives(PlayerSave.getLives());
		player.setTime(PlayerSave.getTime());
		// enemies
		enemies = new ArrayList<Enemy>();
		populateEnemies();
		// init player
		player.init(enemies);
		// explosions
		explosions = new ArrayList<Explosion>();
		// hud
		hud = new HUD(player);
		// teleport
		teleport = new Teleport(tileMap);
		teleport.setPosition(3700, 131);
		// start event
		eventStart = true;
		tb = new ArrayList<Rectangle>();
		eventStart();
		// sfx
		JukeBox.load("/SFX/teleport.mp3", "teleport");
		JukeBox.load("/SFX/explode.mp3", "explode");
		JukeBox.load("/SFX/enemyhit.mp3", "enemyhit");
		// music
		JukeBox.load("/Music/level1.mp3", "level1");
		JukeBox.loop("level1", 600, JukeBox.getFrames("level1") - 2200);
	}
	
	private void populateEnemies() {
		Slime slime;
		Bat bat;
		// Slimes
		slime = new Slime(tileMap, player);
		slime.setPosition(1300, 100);
		enemies.add(slime);
		slime = new Slime(tileMap, player);
		slime.setPosition(1320, 100);
		enemies.add(slime);
		slime = new Slime(tileMap, player);
		slime.setPosition(1340, 100);
		enemies.add(slime);
		slime = new Slime(tileMap, player);
		slime.setPosition(1660, 100);
		enemies.add(slime);
		slime = new Slime(tileMap, player);
		slime.setPosition(1680, 100);
		enemies.add(slime);
		slime = new Slime(tileMap, player);
		slime.setPosition(1700, 100);
		enemies.add(slime);
		slime = new Slime(tileMap, player);
		slime.setPosition(2177, 100);
		enemies.add(slime);
		slime = new Slime(tileMap, player);
		slime.setPosition(2960, 100);
		enemies.add(slime);
		slime = new Slime(tileMap, player);
		slime.setPosition(2980, 100);
		enemies.add(slime);
		slime = new Slime(tileMap, player);
		slime.setPosition(3000, 100);
		enemies.add(slime);
		// Bats
		bat = new Bat(tileMap);
		bat.setPosition(2600, 100);
		enemies.add(bat);
		bat = new Bat(tileMap);
		bat.setPosition(3550, 100);
		enemies.add(bat);
	}
	
	// Update
	public void update() {
		// check keys
		input();
		// check if end of level event should start
		if(teleport.contains(player)) {
			eventFinish = blockInput = true;
		}
		// check if player dead event should start
		if(player.getHealth() == 0 || player.gety() > tileMap.getHeight()) {
			eventDead = blockInput = true;
		}
		// play events
		if(eventStart) {
			eventStart();
		}
		if(eventDead) {
			eventDead();
		}
		if(eventFinish) {
			eventFinish();
		}
		// move backgrounds
		clouds.setPosition(tileMap.getx(), tileMap.gety());
		mountains.setPosition(tileMap.getx(), tileMap.gety());
		
		// update player
		player.update();
		
		// update tilemap
		tileMap.setPosition(
			GamePanel.WIDTH / 2 - player.getx(),
			GamePanel.HEIGHT / 2 - player.gety()
		);
		tileMap.update();
		tileMap.fixBounds();
		
		// Update enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()) {
				enemies.remove(i);
				i--;
				explosions.add(new Explosion(tileMap, e.getx(), e.gety()));
			}
		}
		// update explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()) {
				explosions.remove(i);
				i--;
			}
		}
		// update teleport
		teleport.update();	
	}
	
	// Draw enemies
	public void draw(Graphics2D graphics) {
		// draw background
		sky.draw(graphics);
		clouds.draw(graphics);
		mountains.draw(graphics);
		// draw tilemap
		tileMap.draw(graphics);
		// draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(graphics);
		}	
		// draw explosions
		for(int i = 0; i < explosions.size(); i++) {
			explosions.get(i).draw(graphics);
		}
		// draw player
		player.draw(graphics);
		// draw teleport
		teleport.draw(graphics);
		// draw hud
		hud.draw(graphics);
		// draw transition boxes
		graphics.setColor(java.awt.Color.BLACK);
		for(int i = 0; i < tb.size(); i++) {
			graphics.fill(tb.get(i));
		}	
	}
	
	// Handle user input
	public void input() {
		if(Keys.isPressed(Keys.ESCAPE)) {
			manager.setPaused(true);
		}
		if(blockInput || player.getHealth() == 0) {
			return;
		}
		player.setLeft(Keys.keyState[Keys.LEFT]);
		player.setDown(Keys.keyState[Keys.DOWN]);
		player.setRight(Keys.keyState[Keys.RIGHT]);
		player.setJumping(Keys.keyState[Keys.JUMP] || Keys.keyState[Keys.UP]);
		if(Keys.isPressed(Keys.ATTACK)) {
			player.setAttacking();
		}
		if(Keys.isPressed(Keys.CHARGE)) {
			player.setCharging();
		}
	}
	
	// Level reset
	private void reset() {
		player.reset();
		player.setPosition(300, 161);
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
				enemies.remove(i);
				i--;
		}
		populateEnemies();
		blockInput = true;
		eventCount = 0;
		tileMap.setShaking(false, 0);
		eventStart = true;
		eventStart();
	}
	
	// Begin the level
	private void eventStart() {
		eventCount++;
		if(eventCount == 1) {
			tb.clear();
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(0, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
			tb.add(new Rectangle(0, GamePanel.HEIGHT / 2, GamePanel.WIDTH, GamePanel.HEIGHT / 2));
			tb.add(new Rectangle(GamePanel.WIDTH / 2, 0, GamePanel.WIDTH / 2, GamePanel.HEIGHT));
		}
		if(eventCount > 1 && eventCount < 60) {
			tb.get(0).height -= 4;
			tb.get(1).width -= 6;
			tb.get(2).y += 4;
			tb.get(3).x += 6;
		}
		if(eventCount == 60) {
			eventStart = blockInput = false;
			eventCount = 0;
			tb.clear();
		}
	}
	
	// Player has died
	private void eventDead() {
		eventCount++;
		if(eventCount == 1) {
			player.setDead();
			player.stop();
		}
		if(eventCount == 60) {
			tb.clear();
			tb.add(new Rectangle(
				GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		}
		else if(eventCount > 60) {
			tb.get(0).x -= 6;
			tb.get(0).y -= 4;
			tb.get(0).width += 12;
			tb.get(0).height += 8;
		}
		if(eventCount >= 120) {
			if(player.getLives() == 0) {
				manager.setState(GameStateManager.MENUSTATE);
			}
			else {
				eventDead = blockInput = false;
				eventCount = 0;
				player.loseLife();
				reset();
			}
		}
	}
	
	// Finished the level
	private void eventFinish() {
		eventCount++;
		if(eventCount == 1) {
			JukeBox.play("teleport");
			player.setTeleporting(true);
			player.stop();
		}
		else if(eventCount == 120) {
			tb.clear();
			tb.add(new Rectangle(
				GamePanel.WIDTH / 2, GamePanel.HEIGHT / 2, 0, 0));
		}
		else if(eventCount > 120) {
			tb.get(0).x -= 6;
			tb.get(0).y -= 4;
			tb.get(0).width += 12;
			tb.get(0).height += 8;
			JukeBox.stop("teleport");
		}
		if(eventCount == 180) {
			PlayerSave.setHealth(player.getHealth());
			PlayerSave.setLives(player.getLives());
			PlayerSave.setTime(player.getTime());
			manager.setState(GameStateManager.LEVEL1BSTATE);
		}
	}
}