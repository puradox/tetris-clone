import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Input extends KeyAdapter
{
	private static Input instance;
	public static List<Integer> inputBuffer;
	
	private Input()
	{
		inputBuffer = new ArrayList<Integer>();
	}
	
	public static synchronized Input getInstance()
	{
		if (instance == null)
		{
			instance = new Input();
		}
		return instance;
	}
	
	public static synchronized boolean getKeyDown (int asciiCode)
	{
		getInstance();
		
		for (int i = inputBuffer.size() - 1; i >= 0; i--)
		{
			if (i >=inputBuffer.size())
				break;
			if (inputBuffer.get(i) == null)
				return false;
			else if (inputBuffer.get(i) == asciiCode)
				return true;
		}
		return false;
	}
	
	public static synchronized boolean getKeyPress (int asciiCode)
	{
		getInstance();
		
		for (int i = inputBuffer.size() - 1; i >= 0; i--)
		{
			if (i >=inputBuffer.size())
				break;
			if (inputBuffer.get(i) == null)
				return false;
			if (inputBuffer.get(i) == asciiCode)
			{
				inputBuffer.remove(i);
				return true;
			}
		}
		return false;
	}
	
	public void keyPressed( KeyEvent e ) 
	{ 
		for (int i = inputBuffer.size() - 1; i >= 0; i--)
		{
			if (e.getKeyCode() == inputBuffer.get(i))
			{
				return;
			}
		}
		inputBuffer.add(e.getKeyCode());
	}
	 
	public void keyReleased( KeyEvent e ) 
	{ 
		for (int i = inputBuffer.size() - 1; i >= 0; i--)
		{
			if (e.getKeyCode() == inputBuffer.get(i))
			{
				inputBuffer.remove(inputBuffer.get(i));
			}
		}
	}
}
