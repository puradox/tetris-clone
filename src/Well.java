import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Well 
{
	private static Well instance;
	public static Piece currentPiece;
	public static Piece previewPiece;
	private long lastCheck = System.currentTimeMillis();
	public int spawnSize = 4;
	
	//////////////////////////////////////////////////////////////////
	// -Editable variables-
	// TRY EDITING THESE VALUE: ITS FUN!
	public int speed = 500; // down speed in milliseconds
	public int decreasingSpeed = 50; // decreases speed by x milliconds every level
	
	public int columns = 10;
	public int rows = 20;
	
	public int downSpeed = 3; // x times faster
	//
	//////////////////////////////////////////////////////////////////
	
	public static Well getInstance()
	{
		if (instance == null)
		{
			instance = new Well();
		}
		return instance;
	}
	
	public void update()
	{	
		boolean moveDown = true,
				moveLeft = false,
				moveRight = false,
				moveFaster = false;
		float updateTime = speed - (decreasingSpeed * (Game.linesCompleted / 10));
		
		// Controls
		if(Input.getKeyDown(KeyEvent.VK_DOWN))
		{
			moveFaster = true;
			updateTime = speed * ( 1.0f / (float)downSpeed );
		}
		if(Input.getKeyPress(KeyEvent.VK_LEFT))
			moveLeft = true;
		if(Input.getKeyPress(KeyEvent.VK_RIGHT))
			moveRight = true;
		if(Input.getKeyPress(KeyEvent.VK_UP))
			tryRotate();
		
		// Movement checking
		for( Block pb : currentPiece.blocks )
		{
			for( Block b : Block.getList() )
			{	
				// Any other block surrounding?
				if( b.parent != currentPiece && pb.y == b.y - 1 && pb.x == b.x )
					moveDown = false;
				if( b.parent != currentPiece && pb.x == b.x + 1 && pb.y == b.y )
					moveLeft = false;
				if( b.parent != currentPiece && pb.x == b.x - 1 && pb.y == b.y )
					moveRight = false;
				
				// Out of bounds?
				if ( pb.y >= rows + spawnSize )
					moveDown = false;
				if ( pb.x <= 0 )
					moveLeft = false;
				if ( pb.x >= columns - 1 )
					moveRight = false;
				
				// Debug mode
				if( Input.getKeyDown(KeyEvent.VK_SPACE) )
				{
					System.out.println("DEBUG MODE BIOTCH!");
				}
			}
		}
		
		// Movement Down
		if( System.currentTimeMillis() - lastCheck >= updateTime )
		{
			for( Block pb : currentPiece.blocks )
			{
				if( moveDown )
					pb.y++;
			}
			lastCheck = System.currentTimeMillis();
		}
		
		// Movement Left/Right
		for( Block pb : currentPiece.blocks )
		{
			// Left/Right
			if( moveLeft )
				pb.x--;
			if( moveRight )
				pb.x++;
		}
		
		// Collided
		if( !moveDown )
		{
			// Count rows
			int rowCounts[] = new int[rows + spawnSize + 1];
			for( Block b : Block.getList() )
			{
				rowCounts[b.y]++;
			}
			
			// Create queue for full rows
			List<Integer> fullRows = new ArrayList<Integer>();
			for( int y=spawnSize; y <= rows+spawnSize; y++)
			{
				if( rowCounts[y] == columns )
				{
					fullRows.add(y);
					Game.linesCompleted++;
				}
			}
			
			// Remove full rows
			if( fullRows.size() > 0 )
			{
				// Remove full rows
				for( int row : fullRows ) 
				{
					for( int i = Block.getList().size()-1; i >= 0; i-- )
					{
						if( Block.getList().get(i).y == row )
							Block.getList().get(i).remove();
					}
				}
				
				// Go back and reposition other blocks
				for( int rowIndex = fullRows.size()-1; rowIndex >= 0; rowIndex--)
				{
					for( Block b : Block.getList() )
					{
						if( b.y < fullRows.get(rowIndex) )
							b.y++;
					}
					// Remove the row
					fullRows.remove(rowIndex);
				}
				Sound.playSound("./sound/fullrow.wav");
				Game.score += ((Game.linesCompleted/10) * 25) + 100;
				
			} else {
				Sound.playSound("./sound/setdown.wav");
				
				if(moveFaster)
					Game.score += ((Game.linesCompleted/10) * 5) + 10; 	
				else
					Game.score += ((Game.linesCompleted/10) * 3) + 6;
			}
			
			// Check if spawn row is full
			int spawnRow = 0;
			for( Block b : Block.getList() )
			{
				if( b.y <= spawnSize )
					spawnRow++;
			}
			// If not, spawn another piece
			if( spawnRow == 0 )
			{
				nextPiece();
			}
			else 
			{
				Sound.playSound("./sound/gameover.wav");
				Game.gameover = true; 
			}
		}
	}
	
	public void nextPiece()
	{
		Random r = new Random();
		Pattern pattern;
		Color color;
		
		// Generate random numbers based on size of lists
		int r1 = r.nextInt(Pattern.types.size());
		int r2 = r.nextInt(Color.list.size());
		
		// Get random pattern/color from their lists
		pattern = Pattern.map[Pattern.types.get(r1)][1];
		color = Color.list.get(r2);
		
		// Set to previewPiece first
		if( previewPiece == null )
		{
			previewPiece = new Piece(pattern, color, true);
			nextPiece(); // Do this again
		}
		else
		{
			currentPiece = new Piece(previewPiece.pattern, previewPiece.color);
			previewPiece = new Piece(pattern, color, true);
		}
	}
	
	private void tryRotate()
	{
		Pattern next = null;
		char type = currentPiece.pattern.type;
		int rotation = currentPiece.pattern.rotation;
		int maxRotations = Pattern.map[type][1].maxRotations;
		
		// Only one rotation?
		if( maxRotations == 1 )
			return;
		
		// Fetch the next rotation pattern
		if( rotation < maxRotations )
			next = Pattern.map[type][rotation+1];
		else if( rotation == maxRotations )
			next = Pattern.map[type][1];
		
		// Get anchor point of currentPiece
		int anchorX = 0;
		int anchorY = 0;
		for( Block b : currentPiece.blocks )
		{
			if( b.anchor == true )
			{
				anchorX = b.x - 2;
				anchorY = b.y - 1;
				break;
			}
		}
		
		// Check for availability
		for( int y=0; y < 4; y++)
		{
			for( int x=0; x < 4; x++)
			{
				if( next.data[y][x] == true )
				{
					// Other block in place?
					for( Block b : Block.getList() )
					{	
						if( b.parent != currentPiece && 
							b.x - anchorX == x && 
							b.y - anchorY == y )
							return;
					}
					
					// Out of bounds?
					if( x + anchorX > columns ||
						x + anchorX < 0 ||
						y + anchorY > rows + spawnSize ||
						y + anchorY < 0 )
						return;
				}
			}
		}
		
		// Re-arrange the pieces
		int count = 0;
		for( int y=0; y < 4; y++)
		{
			for( int x=0; x < 4; x++)
			{
				if( next.data[y][x] == true )
				{
					currentPiece.blocks.get(count).x = x + anchorX;
					currentPiece.blocks.get(count).y = y + anchorY;
					if( y==1 && x==2)
						currentPiece.blocks.get(count).anchor = true;
					else
						currentPiece.blocks.get(count).anchor = false;
					count++;
				}
			}
		}
		currentPiece.pattern = next;
	}
	
	public void restart()
	{
		// Set pieces to null
		currentPiece = null;
		previewPiece = null;
		
		// Clear Block list
		Block.clearList();
		
		// Generate a new piece
		nextPiece();
	}
}
