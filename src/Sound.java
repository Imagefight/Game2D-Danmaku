import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
public class Sound 
{
	Clip clip;
	ArrayList<File> soundPath =  new ArrayList<>();
	
	public Sound() 
	{
		soundPath.add(new File("Assets/Sounds/Songs/STAGE_1.wav")); // Stage 1's theme
		soundPath.add(new File("Assets/Sounds/Songs/BOSS.wav")); // Boss Theme
		soundPath.add(new File("Assets/Sounds/FX/Hurt.wav")); // Hurt sound
	}
	
	public void SetFile(int i) 
	{
	    try 
	    {
	        File file = soundPath.get(i);
	        
	        if (file == null) 
	        {
	            System.out.println("Resource URL is null for index " + i);
	            return;
	        }
	        
	        System.out.println("Loading sound from: " + file.getPath());
	        AudioInputStream ais = AudioSystem.getAudioInputStream(file);
	        clip = AudioSystem.getClip();
	        clip.open(ais);
	    } 
	    catch (Exception e) 
	    {
	        e.printStackTrace();
	    }
	}

	public void SetVolume(double volume) 
	{
        if (volume < 0.0 || volume > 1.0) {
            throw new IllegalArgumentException("Volume should be between 0.0 and 1.0");
        }
        // Get the Control object from the Clip
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        // Calculate the gain value to set the volume
        float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
        gainControl.setValue(dB);
    }
	
	public void Play() 
	{
		clip.start();
	}
	
	public void Loop()
	{
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void Stop() 
	{
		clip.stop();
	}
}
