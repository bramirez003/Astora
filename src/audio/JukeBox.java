package audio;

import java.util.HashMap;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

//The audio player
public class JukeBox {
	private static HashMap<String, Clip> clips;	// The background music (bgm)
	private static int gap;						// Gap between loops
	private static boolean mute = false;		// Is the bgm muted
	
	// Initialize the audio player
	public static void init() {
		clips = new HashMap<String, Clip>();
		gap = 0;
	}
	
	// Load the bgm
	public static void load(String s, String n) {
		if(clips.get(n) != null) return;
		Clip clip;
		try {			
			AudioInputStream stream = AudioSystem.getAudioInputStream(JukeBox.class.getResourceAsStream(s));
			AudioFormat baseFormat = stream.getFormat();
			AudioFormat decodeFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,baseFormat.getSampleRate(),
					16,baseFormat.getChannels(),baseFormat.getChannels() * 2,baseFormat.getSampleRate(),false);
			AudioInputStream dstream = AudioSystem.getAudioInputStream(decodeFormat, stream);
			clip = AudioSystem.getClip();
			clip.open(dstream);
			clips.put(n, clip);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Play constructor
	public static void play(String s) {
		play(s, gap);
	}
	
	// Play the bgm
	public static void play(String s, int i) {
		if(mute) return;
		Clip c = clips.get(s);
		if(c == null) return;
		if(c.isRunning()) c.stop();
		c.setFramePosition(i);
		while(!c.isRunning()) c.start();
	}
	
	// Stop the bgm
	public static void stop(String s) {
		if(clips.get(s) == null) return;
		if(clips.get(s).isRunning()) clips.get(s).stop();
	}
	
	// Resume the bgm
	public static void resume(String s) {
		if(mute) return;
		if(clips.get(s).isRunning()) return;
		clips.get(s).start();
	}
	
	// Loop constructor
	public static void loop(String s) {
		loop(s, gap, gap, clips.get(s).getFrameLength() - 1);
	}
	
	// Loop constructor
	public static void loop(String s, int frame) {
		loop(s, frame, gap, clips.get(s).getFrameLength() - 1);
	}
	
	// Loop constructor
	public static void loop(String s, int start, int end) {
		loop(s, gap, start, end);
	}
	
	// Repeat song bgm when needed
	public static void loop(String s, int frame, int start, int end) {
		stop(s);
		if(mute) return;
		clips.get(s).setLoopPoints(start, end);
		clips.get(s).setFramePosition(frame);
		clips.get(s).loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	// Set the frame when the audio player will play
	public static void setPosition(String s, int frame) {
		clips.get(s).setFramePosition(frame);
	}
	
	// Get the frame
	public static int getFrames(String s) { 
		return clips.get(s).getFrameLength(); 
	}
	
	// Get when the frame the audio player should play on
	public static int getPosition(String s) { 
		return clips.get(s).getFramePosition(); 
	}
	
	// Close the audio player
	public static void close(String s) {
		stop(s);
		clips.get(s).close();
	}
}