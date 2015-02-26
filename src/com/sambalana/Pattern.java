package com.sambalana;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pattern 
{
	public static List<Character> types = new ArrayList<Character>();
	public static Pattern[][] map = new Pattern['Z'+1][10];
	
	public Boolean[][] data = new Boolean[4][4];
	public int maxRotations = 0;
	public char type;
	public int rotation;
	
	Pattern(Boolean[][] data, char type, int rotation)
	{
		this.data = data;
		this.type = type;
		this.rotation = rotation;
	}
	
	public static List<Character> getTypes()
	{
		return types;
	}
	
	public static Pattern[][] getMap()
	{
		return map;
	}
	
	public static void importPatterns(String path, char blockCharacter)
	{
		System.out.println("Importing piece files...");
		File[] fileList = new File(path).listFiles();
		Arrays.sort(fileList);
		
		for( File f : fileList )
		{
			// Get filename info
			Boolean[][] pattern = new Boolean[4][4];
			char type = f.getName().charAt(0);
			int rotation = (int) (f.getName().charAt(2) - 48);
			System.out.println( " > " + type + "-" + rotation );
			
			// Get pattern data
			try( BufferedReader br = new BufferedReader(new FileReader(f)) )
			{
				String currentLine;
				for( int i=0; (currentLine = br.readLine()) != null; i++ ) // It's official, I'm a genius
				{
					for( int j=0; j < currentLine.length(); j++)
					{
						// Process data
						if( currentLine.charAt(j) == blockCharacter )
							pattern[i][j] = true;
						else
							pattern[i][j] = false;
					}
				}
			} catch( IOException e ) {
				e.printStackTrace();
			}
			
			// Add to pattern type list and map
			if( !types.contains(type) )
				types.add(type);
			map[type][rotation] = new Pattern(pattern, type, rotation);
			map[type][1].maxRotations++;
		}
		System.out.println("Finished importing piece files!\n");
	}
}
