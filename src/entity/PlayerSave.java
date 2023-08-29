package entity;

// Controls the basic of the players progress
public class PlayerSave {
	private static int lives = 3;	// Number of retries the player has
	private static int health = 5;	// Number of hits the player can take
	private static long time = 0;	// The amount of time that has passed since starting the level
	
	//Initialize
	public static void init() {
		lives = 3;
		health = 5;
		time = 0;
	}
	
	// Retrieve the number of lives the player has
	public static int getLives() { 
		return lives; 
	}
	
	// Set the number of lives the player has
	public static void setLives(int i) {
		lives = i; 
	}
	
	// Retrieve the amount of health the player has
	public static int getHealth() { 
		return health; 
	}
	
	// Set the amount of heatlh the player has
	public static void setHealth(int i) { 
		health = i; 
	}
	
	// Retrieve the elapsed time
	public static long getTime() { 
		return time; 
	}
	
	// Set the time that has elapsed
	public static void setTime(long t) { 
		time = t; 
	}
}