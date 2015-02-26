package com.sambalana;

import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

public class Color 
{
	public static List<Color> list = new ArrayList<Color>();
	//public static Map<String, Color> map = new HashMap<String, Color>();
	
	public String name;
	public Image image;
	
	Color(String name, Image image)
	{
		list.add(this);
		this.name = name;
		this.image = image;
	}
	
	public static void importColors(String path)
	{
		System.out.println("Importing colors...");
		File[] fileList = new File(path).listFiles();
		
		for( File f : fileList )
		{
			// Get filename info
			String filename = f.getName();
			String name = filename.replaceFirst("[.][^.]+$", "");
			
			// Create Color based on the image and filename
			new Color(name, new ImageIcon(path.concat(filename)).getImage());
			System.out.println(" > " + name);
		}
		System.out.println("Finished importing colors!\n");
	}
}
