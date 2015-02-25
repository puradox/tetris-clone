import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class Sound 
{
	private static Sound instance;
	
	private Sound()
	{
		
	}
	
	public static Sound getInstance()
	{
		if (instance == null)
		{
			instance = new Sound();
		}
		return instance;
	}
	
	public static void playSound(String path)
	{
		try
		{
			AudioInputStream audio = AudioSystem.getAudioInputStream(new File(path));
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
		} 
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}
