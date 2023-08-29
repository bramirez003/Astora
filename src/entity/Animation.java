package entity;

import java.awt.image.BufferedImage;

//Handles the animation in the game
public class Animation {
	private BufferedImage[] frames;	// The animation frames
	private int currentFrame;		// The current frame in the animation
	private int numFrames;			// The number of frames in the animation
	private int count;				// The current frame in the animation
	private int delay;				// The number of frames before animation plays
	private int timesPlayed;		// The number of times the animation has played
	
	// Constructor
	public Animation() {
		timesPlayed = 0;
	}
	
	// Set the number of frames an animation takes
	public void setFrames(BufferedImage[] frames) {
		this.frames = frames;
		currentFrame = 0;
		count = 0;
		timesPlayed = 0;
		delay = 2;
		numFrames = frames.length;
	}
	
	// Set how many frames an animation will be delayed by
	public void setDelay(int i) { 
		delay = i; 
	}
	
	// Set the current frame
	public void setFrame(int i) { 
		currentFrame = i; 
	}
	
	// Set the number of frames
	public void setNumFrames(int i) { 
		numFrames = i; 
	}
	
	// Update the frame
	public void update() {
		if(delay == -1) {
			return;
		}
		count++;
		if(count == delay) {
			currentFrame++;
			count = 0;
		}
		if(currentFrame == numFrames) {
			currentFrame = 0;
			timesPlayed++;
		}	
	}
	
	// Retrieve the current frame
	public int getFrame() { 
		return currentFrame; 
	}
	
	// Get the frame number
	public int getCount() { 
		return count; 
	}
	
	// Retrieve frame animation
	public BufferedImage getImage() { 
		return frames[currentFrame]; 
	}
	
	// Play animation only once
	public boolean hasPlayedOnce() { 
		return timesPlayed > 0; 
	}
	
	// Check if animation has already played
	public boolean hasPlayed(int i) { 
		return timesPlayed == i; 
	}
}