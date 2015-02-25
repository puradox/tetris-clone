import java.util.ArrayList;
import java.util.List;

public class Piece 
{	
	List<Block> blocks = new ArrayList<Block>();
	Pattern pattern;
	Color color;
	
	Piece(Pattern pattern, Color color)
	{
		this.pattern = pattern;
		this.color = color;
		
		for( int y=0; y < 4; y++ )
		{
			for( int x=0; x < 4; x++ )
			{
				if( pattern.data[y][x] == true )
				{
					// Create block
					Block b = new Block(color, this, x, y);
					blocks.add(b);
					// Anchor point?
					if( y==1 && x==2)
						b.anchor = true;
				}
			}
		}
	}
	
	// Preview piece -- doesn't get added to block list
	Piece(Pattern pattern, Color color, boolean preview)
	{
		this.pattern = pattern;
		this.color = color;
		
		for( int y=0; y < 4; y++ )
		{
			for( int x=0; x < 4; x++ )
			{
				if( pattern.data[y][x] == true )
				{
					// Create block
					Block b = new Block(color, this, x, y, preview);
					blocks.add(b);
					// Anchor point?
					if( y==1 && x==2)
						b.anchor = true;
				}
			}
		}
	}
}
