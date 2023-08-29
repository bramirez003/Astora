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


public class Level1BState extends GameState {
	private Background temple;				// Inner temple background
	private Player player;					// Player entity
	private TileMap tileMap;				// Tilemap information
	private ArrayList<Enemy> enemies;		// List of enemies
	private ArrayList<Explosion> explosions;// Enemy explosion effects
	private HUD hud;						// Heads up display on top left of screen
	private Teleport teleport;				// Teleport station at end of level
	private boolean blockInput = false;		// Block user input
	private int eventCount = 0;				// Number of events that have transpired
	private boolean eventStart;				// Start event
	private ArrayList<Rectangle> tb;		// Event handler
	private boolean eventFinish;			// Finish event
	private boolean eventDead;				// Player has died
	private boolean eventQuake;				// Earthquake event
	
	// Constructor
	public Level1BState(GameStateManager gsm) {
		super(gsm);
		init();
	}
	
	// Initialize
	public void init() {
		// backgrounds
		temple = new Background("/Backgrounds/temple.gif", 0.5, 0);
		// tilemap
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/ruinstileset.gif");
		tileMap.loadMap("/Maps/level1b.map");
		tileMap.setPosition(140, 0);
		tileMap.setCamera(1);
		// player
		player = new Player(tileMap);
		player.setPosition(300, 131);
		player.setHealth(PlayerSave.getHealth());
		player.setLives(PlayerSave.getLives());
		player.setTime(PlayerSave.getTime());
		// enemies
		enemies = new ArrayList<Enemy>();
		populateEnemies();
		player.init(enemies);
		// explosions
		explosions = new ArrayList<Explosion>();
		// hud
		hud = new HUD(player);
		// teleport
		teleport = new Teleport(tileMap);
		teleport.setPosition(2850, 371);
		// start event
		eventStart = true;
		tb = new ArrayList<Rectangle>();
		eventStart();
		// sfx
		JukeBox.load("/SFX/teleport.mp3", "teleport");
		JukeBox.load("/SFX/explode.mp3", "explode");
		JukeBox.load("/SFX/enemyhit.mp3", "enemyhit");
	}
	
	private void populateEnemies() {
		enemies.clear();
		Slime slime;
		Bat bat;
		//slimes
		slime = new Slime(tileMap, player);
		slime.setPosition(750, 100);
		enemies.add(slime);
		slime = new Slime(tileMap, player);
		slime.setPosition(900, 150);
		enemies.add(slime);
		slime = new Slime(tileMap, player);
		slime.setPosition(1320, 250);
		enemies.add(slime);
		slime = new Slime(tileMap, player);
		slime.setPosition(1570, 160);
		enemies.add(slime);
		slime = new Slime(tileMap, player);
		slime.setPosition(1590, 160);
		enemies.add(slime);
		slime = new Slime(tileMap, player);
		slime.setPosition(2600, 370);
		enemies.add(slime);
		slime = new Slime(tileMap, player);
		slime.setPosition(2620, 370);
		enemies.add(slime);
		slime = new Slime(tileMap, player);
		slime.setPosition(2640, 370);
		enemies.add(slime);
		//bats
		bat = new Bat(tileMap);
		bat.setPosition(904, 130);
		enemies.add(bat);
		bat = new Bat(tileMap);
		bat.setPosition(1080, 270);
		enemies.add(bat);
		bat = new Bat(tileMap);
		bat.setPosition(1200, 270);
		enemies.add(bat);
		bat = new Bat(tileMap);
		bat.setPosition(1704, 300);
		enemies.add(bat);
	}
	
	// Update
	public void update() {
		// check keys
		input();
		// check if quake event should start
		if(player.getx() > 2175 && !tileMap.isShaking()) {
			eventQuake = blockInput = true;
		}
		// check if end of level event should start
		if(teleport.contains(player)) {
			eventFinish = blockInput = true;
		}
		// play events
		if(eventStart) {
			eventStart();
		}
		if(eventDead) {
			eventDead();
		}
		if(eventQuake) {
			eventQuake();
		}
		if(eventFinish) {
			eventFinish();
		}
		// move backgrounds
		temple.setPosition(tileMap.getx(), tileMap.gety());
		// update player
		player.update();
		if(player.getHealth() == 0 || player.gety() > tileMap.getHeight()) {
			eventDead = blockInput = true;
		}
		// update tilemap
		tileMap.setPosition(
			GamePanel.WIDTH / 2 - player.getx(),
			GamePanel.HEIGHT / 2 - player.gety()
		);
		tileMap.update();
		tileMap.fixBounds();
		// update enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()) {
				enemies.remove(i);
				i--;
				explosions.add(
					new Explosion(tileMap, e.getx(), e.gety()));
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
	
	// Display graphics
	public void draw(Graphics2D graphics) {
		// draw background
		temple.draw(graphics);
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
		if(Keys.isPressed(Keys.ESCAPE)) manager.setPaused(true);
		if(blockInput || player.getHealth() == 0) {
			return;
		}
		player.setLeft(Keys.keyState[Keys.LEFT]);
		player.setDown(Keys.keyState[Keys.DOWN]);
		player.setRight(Keys.keyState[Keys.RIGHT]);
		player.setJumping(Keys.keyState[Keys.UP] || Keys.keyState[Keys.JUMP]);
		if(Keys.isPressed(Keys.ATTACK)) {
			player.setAttacking();
		}
		if(Keys.isPressed(Keys.CHARGE)) {
			player.setCharging();
		}
	}

	// Reset level
	private void reset() {
		player.loseLife();
		player.reset();
		player.setPosition(300, 131);
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
	
	// Level started
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
		if(eventCount == 1) player.setDead();
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
				reset();
			}
		}
	}
	
	// Earthquake event
	private void eventQuake() {
		eventCount++;
		if(eventCount == 1) {
			player.stop();
			player.setPosition(2175, player.gety());
		}
		if(eventCount == 60) {
			player.setEmote(Player.CONFUSED);
		}
		if(eventCount == 120) {
			player.setEmote(Player.NONE);
		}
		if(eventCount == 150) {
			tileMap.setShaking(true, 10);
		}
		if(eventCount == 180) {
			player.setEmote(Player.SURPRISED);
		}
		if(eventCount == 300) {
			player.setEmote(Player.NONE);
			eventQuake = blockInput = false;
			eventCount = 0;
		}
	}
	
	// Finished level
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
			manager.setState(GameStateManager.LEVEL1CSTATE);
		}	
	}
}