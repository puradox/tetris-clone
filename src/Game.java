import java.awt.event.KeyEvent;

import javax.swing.JFrame;

public class Game extends JFrame
{
	// Generated ID to get rid of that annoying warning
	private static final long serialVersionUID = 844447838275322630L;
	
	private Draw display = new Draw();
	public static boolean gameover = false;
	public static boolean paused = false; 
	public static long score = 0;
	public static int linesCompleted = 0;
	
	public Game()
	{
		JFrame frame = new JFrame();
		frame.addKeyListener(Input.getInstance());
		
		Pattern.importPatterns("./pieces/", 'x');
		Color.importColors("./colors/");
		
		Well.getInstance().nextPiece();
		Sound.playSound("./sound/newgame.wav");
		
		display.setBackground(java.awt.Color.gray);
		frame.add(display);
		frame.setTitle("Tetris");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(display.windowX, display.windowY);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
	}
	
	public void run()
	{
		// Pause button
		if(Input.getKeyPress(KeyEvent.VK_ESCAPE))
			paused = !paused;
		
		if( !gameover && !paused )
			Well.getInstance().update(); // <- Main update loop (where all the magic happens)
		else if( gameover )
		{
			// Selection detection *cause it rhythms*
			if(Input.getKeyPress(KeyEvent.VK_UP))
				display.gameoverSelection = 0;
			if(Input.getKeyPress(KeyEvent.VK_DOWN))
				display.gameoverSelection = 1;
			if(Input.getKeyPress(KeyEvent.VK_ENTER))
			{
				switch( display.gameoverSelection )
				{
				case 0: // Restart
					Well.getInstance().restart();
					gameover = false;
					display.setBackground(java.awt.Color.gray);
					Sound.playSound("./sound/restart.wav");
					break;
				case 1: // Quit
					Sound.playSound("./sound/quit.wav");
					try {
						Thread.sleep(1500);
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.exit(0);
					break;
				default:
					break;
				}
			}
		}
		display.repaint();
	}
}
