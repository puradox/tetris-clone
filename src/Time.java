

public class Time 
{
	private static Time instance;
	public static long start, previous, current;
	public static float delta;
	
	private Time() 
	{
		start = System.nanoTime();
		previous = start;
		current = 0;
		delta = 0;
	}
	
	public static Time getInstance()
	{
		if (instance == null)
			instance = new Time();
		return instance;
	}
	
	public static void update()
	{
		previous = current;
		current = System.nanoTime() - start;
		delta = (current - previous) * 0.0000000001f;
	}
	
}
