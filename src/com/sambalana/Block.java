package com.sambalana;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class Block 
{
	private static List<Block> list;
	
	Color color;
	Image image;
	Piece parent;
	boolean anchor = false;
	int x, y;
	
	Block(Color color, Piece parent, int x, int y)
	{
		Block.getList().add(this);
		
		this.color = color;
		this.image = color.image;
		this.parent = parent;
		this.x = x + (Well.getInstance().columns / 2) - 2;
		this.y = y;
	}
	
	// Preview block -- doesn't get added to list
	Block(Color color, Piece parent, int x, int y, boolean preview)
	{
		if( !preview )
			Block.getList().add(this);
		
		this.color = color;
		this.image = color.image;
		this.parent = parent;
		this.x = x + 3;
		this.y = y;
	}
	
	public static synchronized List<Block> getList()
	{
		if (list == null)
		{
			list = new ArrayList<Block>();
		}
		return list;
	}
	
	public void remove()
	{
		list.remove(this);
	}
	
	public static void clearList()
	{
		for( int i = list.size()-1; i >= 0; i-- )
		{
			list.remove(i);
		}
	}
}
