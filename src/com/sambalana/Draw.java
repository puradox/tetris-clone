package com.sambalana;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Draw extends JPanel
{
	// Generated ID to get rid of that annoying warning
	private static final long serialVersionUID = -2253781043090150593L;

	int gameoverSelection = 0;
	
	//////////////////////////////////////////////////////////////////
	// -Size variables-
	// Unless your a master UI designer, you might not want to mess
	// with this stuff.
	int bSize = 25;
	int padding = bSize * 2;
	int spawnSize = Well.getInstance().spawnSize * bSize;
	
	int wellPadding = padding;
	int wellX = (Well.getInstance().columns * bSize) + wellPadding;
	int wellY = (Well.getInstance().rows * bSize);
	
	int windowX = Well.getInstance().columns * bSize + 7 + (4 * bSize) + (wellPadding * 2);
	int windowY = (Well.getInstance().rows + 2) * bSize  + 4;
	//
	/////////////////////////////////////////////////////////////////
	
	public void paint(Graphics g)
	{
		super.paint(g);
		Graphics2D g2D = (Graphics2D) g;
		
		if( !Game.gameover )
		{
			// Grid Lines
			for( int y=0; y <= Well.getInstance().rows; y++)
			{
				for( int x=0; x < Well.getInstance().columns; x++ )
				{
					g2D.drawRect((x * bSize) + wellPadding, 
								 (y * bSize), 
								 bSize, bSize);
				}
			}
			Stroke oldStroke = g2D.getStroke();
			g2D.setStroke(new BasicStroke(2));
			g2D.drawRect(wellPadding, 0, wellX - wellPadding, wellY + 25);
			g2D.setStroke(oldStroke);
			
			// Blocks
			synchronized( Block.getList() )
			{
				for(Block b : Block.getList())
				{
					g2D.drawImage(b.image, 
								  b.x*bSize + wellPadding, 
								  b.y*bSize - spawnSize, 
								  null);
				}
			}
			
			// Preview box
			for( int y=0; y < 4; y++)
			{
				for( int x=0; x < 4; x++ )
				{
					g2D.drawRect((x * bSize) + wellX, 
								 (y * bSize) + padding,
								 bSize, bSize);
				}
			}
			oldStroke = g2D.getStroke();
			g2D.setStroke(new BasicStroke(2));
			g2D.drawRect(wellX, padding, 100, 100);
			g2D.setStroke(oldStroke);
			
			// Preview blocks
			for( Block b : Well.previewPiece.blocks )
			{
				g2D.drawImage(b.image, 
							  b.x*bSize + wellX - 75,
							  b.y*bSize + padding, 
							  null);
			}
			
			try
			{
				Font font = Font.createFont(Font.TRUETYPE_FONT, new File("./fonts/usuziv2.ttf"));
				
				GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				ge.registerFont(font);
				
				g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
			            			RenderingHints.VALUE_ANTIALIAS_ON);
				
				// Score
				g2D.setFont(font.deriveFont(20f));
				g2D.drawString("Score:",
						   	   4*bSize + wellX - 80, 
						       4*bSize + padding*2 - 20);
				g2D.setFont(font.deriveFont(15f));
				g2D.drawString(Long.toString(Game.score),
					   	       4*bSize + wellX - 80, 
					           4*bSize + padding*2 - 5);
				
				// Level
				g2D.setFont(font.deriveFont(20f));
				g2D.drawString("Level: " + Integer.toString(Game.linesCompleted/10 + 1),
						   	   4*bSize + wellX - 80,
						       4*bSize + padding*2 + 25);
				g2D.setFont(font.deriveFont(15f));
				g2D.drawString(Integer.toString(Game.linesCompleted%10) + "0%",
					   	       4*bSize + wellX - 80, 
					           4*bSize + padding*2 + 40);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			if( Game.paused )
			{
				g2D.drawImage(new ImageIcon("./images/pause.png").getImage(), 
						  windowX/2 - 90, 
						  windowY/2 - 58, 
						  null);
			}
		}
		else // Gameover
		{
			setBackground(java.awt.Color.white);
			g2D.drawImage(new ImageIcon("./images/gameover.png").getImage(), 
						  windowX/2 - 135, 
						  windowY/2 - 58, 
						  null);
			g2D.drawImage(new ImageIcon("./images/selections.png").getImage(), 
					  windowX/2 - 49, 
					  windowY/2 - 63 + 135, 
					  null);
			g2D.drawImage(new ImageIcon("./images/selector.png").getImage(), 
					  windowX/2 - 75, 
					  windowY/2 + 77 + (gameoverSelection * 40), 
					  null);
		}
	}
	
}
