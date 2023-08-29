package handlers;

import java.awt.event.KeyEvent;

//Keeps track of what keys are pressed
public class Keys {
	public static final int NUM_KEYS = 16;
	public static boolean keyState[] = new boolean[NUM_KEYS];
	public static boolean prevKeyState[] = new boolean[NUM_KEYS];
	public static int UP = 0;
	public static int LEFT = 1;
	public static int DOWN = 2;
	public static int RIGHT = 3;
	public static int JUMP = 4;
	public static int ATTACK = 5;
	public static int CHARGE = 6;
	public static int ENTER = 7;
	public static int ESCAPE = 8;
	
	// Check if the pressed key has a match
	public static void keySet(int i, boolean b) {
		if(i == KeyEvent.VK_W) {
			keyState[UP] = b;
		}
		else if(i == KeyEvent.VK_A) {
			keyState[LEFT] = b;
		}
		else if(i == KeyEvent.VK_S) {
			keyState[DOWN] = b;
		}
		else if(i == KeyEvent.VK_D) {
			keyState[RIGHT] = b;
		}
		else if(i == KeyEvent.VK_SPACE) {
			keyState[JUMP] = b;
		}
		else if(i == KeyEvent.VK_J) {
			keyState[ATTACK] = b;
		}
		else if(i == KeyEvent.VK_K) {
			keyState[CHARGE] = b;
		}
		else if(i == KeyEvent.VK_ENTER) {
			keyState[ENTER] = b;
		}
		else if(i == KeyEvent.VK_ESCAPE) {
			keyState[ESCAPE] = b;
		}
	}
	
	// Update the state of the keys
	public static void update() {
		for(int i = 0; i < NUM_KEYS; i++) {
			prevKeyState[i] = keyState[i];
		}
	}
	
	// Check if a key pressed
	public static boolean isPressed(int i) {
		return keyState[i] && !prevKeyState[i];
	}
	
	// Confirm a key is pressed
	public static boolean anyKeyPress() {
		for(int i = 0; i < NUM_KEYS; i++) {
			if(keyState[i]) {
				return true;
			}
		}
		return false;
	}
}