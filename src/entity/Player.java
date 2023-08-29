package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import audio.JukeBox;
import tileMap.TileMap;

//Controls the player information
public class Player extends MapObject {
	// references
	private ArrayList<Enemy> enemies;
	// player stuff
	private int lives;
	private int health;
	private int maxHealth;
	private int damage;
	private int chargeDamage;
	private boolean knockback;
	private boolean flinching;
	private long flinchCount;
	private int score;
	private boolean doubleJump;
	private boolean alreadyDoubleJump;
	private double doubleJumpStart;
	private long time;
	// actions
	private boolean attacking;
	private boolean charging;
	private int chargingTick;
	private boolean teleporting;
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] NUMFRAMES = {
		4, 6, 7, 4, 2, 6, 3, 3, 3
	};
	private final int[] FRAMEWIDTHS = {
		40, 40, 40, 40, 40, 80, 40, 40, 40
	};
	private final int[] FRAMEHEIGHTS = {
		40, 40, 40, 40, 40, 40, 40, 40, 40
	};
	private final int[] SPRITEDELAYS = {
		-1, 3, 2, 6, 5, 2, 1, -1, 1
	};
	private Rectangle ar;
	private Rectangle aur;
	private Rectangle cr;
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int ATTACKING = 2;
	private static final int JUMPING = 3;
	private static final int FALLING = 4;
	private static final int CHARGING = 5;
	private static final int KNOCKBACK = 6;
	private static final int DEAD = 7;
	private static final int TELEPORTING = 8;
	// emotes
	private BufferedImage confused;
	private BufferedImage surprised;
	public static final int NONE = 0;
	public static final int CONFUSED = 1;
	public static final int SURPRISED = 2;
	private int emote = NONE;
	
	// Constructor
	public Player(TileMap tm) {
		super(tm);
		ar = new Rectangle(0, 0, 0, 0);
		ar.width = 30;
		ar.height = 20;
		aur = new Rectangle((int)x - 15, (int)y - 45, 30, 30);
		cr = new Rectangle(0, 0, 0, 0);
		cr.width = 50;
		cr.height = 40;
		width = 30;
		height = 30;
		cwidth = 15;
		cheight = 38;
		moveSpeed = 1.0;
		maxSpeed = 2.2;
		stopSpeed = 1.6;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -4.6;
		stopJumpSpeed = 0.3;
		doubleJumpStart = -4.6;
		damage = 4;
		chargeDamage = 8;
		facingRight = true;
		lives = 3;
		health = maxHealth = 5;
		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/Player.gif"
				)
			);
			int count = 0;
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < NUMFRAMES.length; i++) {
				BufferedImage[] bi = new BufferedImage[NUMFRAMES[i]];
				for(int j = 0; j < NUMFRAMES[i]; j++) {
					bi[j] = spritesheet.getSubimage(
						j * FRAMEWIDTHS[i],
						count,
						FRAMEWIDTHS[i],
						FRAMEHEIGHTS[i]
					);
				}
				sprites.add(bi);
				count += FRAMEHEIGHTS[i];
			}
			// emotes
			spritesheet = ImageIO.read(getClass().getResourceAsStream(
				"/HUD/Emotes.gif"
			));
			confused = spritesheet.getSubimage(
				0, 0, 14, 17
			);
			surprised = spritesheet.getSubimage(
				14, 0, 14, 17
			);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		setAnimation(IDLE);
		JukeBox.load("/SFX/playerjump.mp3", "playerjump");
		JukeBox.load("/SFX/playerlands.mp3", "playerlands");
		JukeBox.load("/SFX/playerattack.mp3", "playerattack");
		JukeBox.load("/SFX/playerhit.mp3", "playerhit");
		JukeBox.load("/SFX/playercharge.mp3", "playercharge");
	}
	
	// List of enemies
	public void init(ArrayList<Enemy> enemies) {
		this.enemies = enemies;
	}
	
	// Retrieve player current health
	public int getHealth() {
		return health; 
	}
	
	// Retrieve player max health
	public int getMaxHealth() { 
		return maxHealth; 
	}
	
	// Player is emoting
	public void setEmote(int i) {
		emote = i;
	}
	
	// Player is teleporting
	public void setTeleporting(boolean b) { teleporting = b; }
	
	// Player is jumping
	public void setJumping(boolean b) {
		if(knockback) {
			return;
		}
		if(b && !jumping && falling && !alreadyDoubleJump) {
			doubleJump = true;
		}
		jumping = b;
	}
	
	// Player is attacking
	public void setAttacking() {
		if(knockback) {
			return;
		}
		if(charging) {
			return;
		}
		else {
			attacking = true;
		}
	}
	
	// Player is charging
	public void setCharging() {
		if(knockback) {
			return;
		}
		if(!attacking && !charging) {
			charging = true;
			JukeBox.play("playercharge");
			chargingTick = 0;
		}
	}

	// Player is dead
	public void setDead() {
		health = 0;
		stop();
	}
	
	// Get time string
	public String getTimeToString() {
		int minutes = (int) (time / 3600);
		int seconds = (int) ((time % 3600) / 60);
		return seconds < 10 ? minutes + ":0" + seconds : minutes + ":" + seconds;
	}
	
	// Return time
	public long getTime() { 
		return time; 
	}
	
	// Set elapsed time
	public void setTime(long t) { 
		time = t; 
	}
	
	// Set current health
	public void setHealth(int i) { 
		health = i; 
	}
	
	// Set current lives
	public void setLives(int i) { 
		lives = i; 
	}
	
	// Restock lives
	public void gainLife() { 
		lives++; 
	}
	
	// Remove a life
	public void loseLife() { 
		lives--; 
	}
	
	// Retrieve number of lives
	public int getLives() { 
		return lives; 
	}
	
	// Increase player score
	public void increaseScore(int score) {
		this.score += score; 
	}
	
	// Get player score
	public int getScore() { 
		return score; 
	}
	
	// Hit register for boss
	public void hit(int damage) {
		if(flinching) {
			return;
		}
		JukeBox.play("playerhit");
		stop();
		health -= damage;
		if(health < 0) {
			health = 0;
		}
		flinching = true;
		flinchCount = 0;
		if(facingRight) {
			dx = -1;
		}
		else {
			dx = 1;
		}
		dy = -3;
		knockback = true;
		falling = true;
		jumping = false;
	}
	
	// Reset boss
	public void reset() {
		health = maxHealth;
		facingRight = true;
		currentAction = -1;
		stop();
	}
	
	// Stop movement
	public void stop() {
		left = right = up = down = flinching = jumping = attacking = charging = false;
	}
	
	// Get next position
	private void getNextPosition() {
		if(knockback) {
			dy += fallSpeed * 2;
			if(!falling) {
				knockback = false;
			}
			return;
		}
		double maxSpeed = this.maxSpeed;
		// movement
		if(left) {
			dx -= moveSpeed;
			if(dx < -maxSpeed) {
				dx = -maxSpeed;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > maxSpeed) {
				dx = maxSpeed;
			}
		}
		else {
			if(dx > 0) {
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) {
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		// cannot move while attacking, except in air
		if((attacking || charging) && !(jumping || falling)) {
			dx = 0;
		}
		// charging
		if(charging) {
			chargingTick++;
			if(facingRight) {
				dx = moveSpeed * (4 - chargingTick * 0.007);
			}
			else {
				dx = -moveSpeed * (4 - chargingTick * 0.07);
			}
		}
		// jumping
		if(jumping && !falling) {
			//sfx.get("jump").play();
			dy = jumpStart;
			falling = true;
			JukeBox.play("playerjump");
		}
		if(doubleJump) {
			dy = doubleJumpStart;
			alreadyDoubleJump = true;
			doubleJump = false;
			JukeBox.play("playerjump");
		}
		if(!falling) {
			alreadyDoubleJump = false;
		}
		// falling
		if(falling) {
			dy += fallSpeed;
			if(dy < 0 && !jumping) {
				dy += stopJumpSpeed;
			}
			if(dy > maxFallSpeed) {
				dy = maxFallSpeed;
			}
		}
	}
	
	// Set boss animations
	private void setAnimation(int i) {
		currentAction = i;
		animation.setFrames(sprites.get(currentAction));
		animation.setDelay(SPRITEDELAYS[currentAction]);
		width = FRAMEWIDTHS[currentAction];
		height = FRAMEHEIGHTS[currentAction];
	}
	
	// Update
	public void update() {
		time++;
		// update position
		boolean isFalling = falling;
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		if(isFalling && !falling) {
			JukeBox.play("playerlands");
		}
		if(dx == 0) {
			x = (int)x;
		}
		// check done flinching
		if(flinching) {
			flinchCount++;
			if(flinchCount > 120) {
				flinching = false;
			}
		}
		
		// check attack finished
		if(currentAction == ATTACKING) {
			if(animation.hasPlayedOnce()) {
				attacking = false;
			}
		}
		if(currentAction == CHARGING) {
			if(animation.hasPlayedOnce()) {
				charging = false;
			}
			cr.y = (int)y - 20;
			if(facingRight) {
				cr.x = (int)x - 15;
			}
			else {
				cr.x = (int)x - 35;
			}
		}
		// check enemy interaction
		for(int i = 0; i < enemies.size(); i++) {
			
			Enemy e = enemies.get(i);
			// check attack
			if(currentAction == ATTACKING &&
					animation.getFrame() == 2 && animation.getCount() == 0) {
				if(e.intersects(ar)) {
					e.hit(damage);
				}
			}
			// check charging attack
			if(currentAction == CHARGING) {
				if(animation.getCount() == 0) {
					if(e.intersects(cr)) {
						e.hit(chargeDamage);
					}
				}
			}
			// collision with enemy
			if(!e.isDead() && intersects(e) && !charging) {
				hit(e.getDamage());
			}
			if(e.isDead()) {
				JukeBox.play("explode", 2000);
			}
		}
		// set animation, ordered by priority
		if(teleporting) {
			if(currentAction != TELEPORTING) {
				setAnimation(TELEPORTING);
			}
		}
		else if(knockback) {
			if(currentAction != KNOCKBACK) {
				setAnimation(KNOCKBACK);
			}
		}
		else if(health == 0) {
			if(currentAction != DEAD) {
				setAnimation(DEAD);
			}
		}
		else if(attacking) {
			if(currentAction != ATTACKING) {
				JukeBox.play("playerattack");
				setAnimation(ATTACKING);
				ar.y = (int)y - 6;
				if(facingRight) ar.x = (int)x + 10;
				else ar.x = (int)x - 40;
			}
		}
		else if(charging) {
			if(currentAction != CHARGING) {
				setAnimation(CHARGING);
			}
		}
		else if(dy < 0) {
			if(currentAction != JUMPING) {
				setAnimation(JUMPING);
			}
		}
		else if(dy > 0) {
			if(currentAction != FALLING) {
				setAnimation(FALLING);
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				setAnimation(WALKING);
			}
		}
		else if(currentAction != IDLE) {
			setAnimation(IDLE);
		}
		animation.update();
		// set direction
		if(!attacking && !charging && !knockback) {
			if(right) {
				facingRight = true;
			}
			if(left) {
				facingRight = false;
			}
		}
	}
	
	// Display graphics
	public void draw(Graphics2D graphics) {
		// draw emote
		if(emote == CONFUSED) {
			graphics.drawImage(confused, (int)(x + xmap - cwidth / 2), (int)(y + ymap - 40), null);
		}
		else if(emote == SURPRISED) {
			graphics.drawImage(surprised, (int)(x + xmap - cwidth / 2), (int)(y + ymap - 40), null);
		}
		// flinch
		if(flinching && !knockback) {
			if(flinchCount % 10 < 5) {
				return;
			}
		}
		super.draw(graphics);
	}
}