import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/* RESOURCEMANAGER.JAVA
 * Loads Images, Creates animations and Sprites, and Loads levels
 */

public class ResourceManager 
{
    private ArrayList tiles;
    private int   currentMap, currentLevel;
    private final Level1 LEVEL_ONE = new Level1();
    private Level level;
    public static Font NICER_NIGHTIE = LoadFont("Fonts/NicerNightie.ttf");
    
    // Host sprites used for cloning
    private Player player = new Player(Game2D.SCREEN_WIDTH/2, Game2D.SCREEN_HEIGHT / 3, 8, 8);
    
    public ResourceManager() 
    {
    	System.out.println("Trying to load tile images.");
    	// LoadTileImages();
    	currentMap = currentLevel = 1;
    	System.out.println("Tile images loaded.");
    }

    public TileMap LoadNextMap() 
    {
        System.out.println("Attempting to Load Next Map");

        TileMap map = null;
        String mapFilename = "Maps/map" + currentMap + ".txt";

        try 
        {
            map = LoadMap(mapFilename);
            level = LoadLevel(currentLevel);
        } 
        
        catch (IOException ex) 
        {
            // Unable to load map, handle the error
            ex.printStackTrace();
        }

        // Move to the next map if successfully loaded
        if (map != null) 
        {
            currentMap++;
            currentLevel++;
        } 
        
        else 
        {
            // No more maps to load, reset map index
            currentMap = 1;
        }

        return map;
    }


    public TileMap ReloadMap() {
        try 
        { 
        	return LoadMap( "Maps/map" + currentMap + ".txt"); 	
        }
        
        catch (IOException ex) 
        {
            ex.printStackTrace();
            return null;
        }
    }

    
    private TileMap LoadMap(String filename) throws IOException 
    {
        ArrayList<String> mapLines = ReadMap(filename);
        
        if (mapLines.isEmpty()) 
        {
            throw new IOException("Empty map file: " + filename);
        }

        int mapWidth = FindMapWidth(mapLines);
        int mapHeight = mapLines.size();

        TileMap newMap = CreateEmptyMap(mapWidth, mapHeight);
        
        PopulateMap(newMap, mapLines);
        
        newMap.SetPlayer(player);
        
        System.out.println(newMap.IsEmpty());
        
        return newMap;
    }

    private ArrayList<String> ReadMap(String filename) throws IOException 
    {
        ArrayList<String> mapLines = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                if (!line.startsWith("\\")) 
                { 
                	// Skip comments
                    mapLines.add(line);
                }
            }
        }
        
        return mapLines;
    }

    private TileMap CreateEmptyMap(int mapWidth, int mapHeight) { return new TileMap(mapWidth, mapHeight); }

    private int FindMapWidth(ArrayList<String> mapLines) 
    {
        int maxWidth = 0;
        
        for (String line : mapLines) 
        {
            maxWidth = Math.max(maxWidth, line.length());
        }
        
        return maxWidth;
    }

    private void PopulateMap(TileMap tileMap, ArrayList<String> mapLines) 
    {
        int y = 0;
        
        for (String line : mapLines) 
        {
            int x = 0;
            
            for (char tileChar : line.toCharArray()) 
            {
            	int newX = TileMapRenderer.TilesToPixels(x);
                int newY = TileMapRenderer.TilesToPixels(y);
            	Rect tile = new Rect (newX, newY, 64, 64);
            	switch (tileChar) 
                {
                    case '#':
                        // Ignore comments
                    	tile.SetImage("Tiles/Black");
                    	tile.anim.ScaleAnimation(4);
                    	tileMap.SetTile(x, y, tile);
                        break;
                    case 'A':
                    	tile.SetImage("Tiles/Dots");
                    	tile.anim.ScaleAnimation(4);
                        tileMap.SetTile(x, y, tile);
                        break;
                    case 'B':
                    	tile.SetImage("Tiles/Grass");
                    	tile.anim.ScaleAnimation(4);
                    	tileMap.SetTile(x, y, tile);
                        break;
                    case 'C':
                    	tile.SetImage("Tiles/Grass2");
                    	tile.anim.ScaleAnimation(4);
                    	tileMap.SetTile(x, y, tile);
                        break;
                    case 'D':
                    	tile.SetImage("Tiles/Stars");
                    	tile.anim.ScaleAnimation(4);
                    	tileMap.SetTile(x, y, tile);
                        break;
                    case 'E':
                    	tile.SetImage("Tiles/Rocks");
                    	tile.anim.ScaleAnimation(4);
                    	tileMap.SetTile(x, y, tile);
                        break;
                    case 'F':
                    	tile.SetImage("Tiles/Flowery");
                    	tile.anim.ScaleAnimation(4);
                    	tileMap.SetTile(x, y, tile);
                        break;
                    case 'G':
                    	tile.SetImage("Tiles/Branchwall-horizontal");
                    	tile.anim.ScaleAnimation(4);
                    	tileMap.SetTile(x, y, tile);
                        break;
                    case 'H':
                    	tile.SetImage("Tiles/Branchwall-vertical");
                    	tile.anim.ScaleAnimation(4);
                    	tileMap.SetTile(x, y, tile);
                        break;
                    case 'I':
                    	tile.SetImage("Tiles/Branchwall-top-left");
                    	tile.anim.ScaleAnimation(4);
                    	tileMap.SetTile(x, y, tile);
                        break;
                    case 'J':
                    	tile.SetImage("Tiles/Branchwall-top-right");
                    	tile.anim.ScaleAnimation(4);
                    	tileMap.SetTile(x, y, tile);
                        break;
                    case 'K':
                    	tile.SetImage("Tiles/Branchwall-bottom-left");
                    	tile.anim.ScaleAnimation(4);
                    	tileMap.SetTile(x, y, tile);
                        break;
                    case 'L':
                    	tile.SetImage("Tiles/Branchwall-bottom-right");
                    	tile.anim.ScaleAnimation(4);
                    	tileMap.SetTile(x, y, tile);
                        break;
                    case 'M':
                    	tile.SetImage("Tiles/Bigbricks");
                    	tile.anim.ScaleAnimation(4);
                    	tileMap.SetTile(x, y, tile);
                        break;
                    default:
                    	tile.SetImage("Tiles/Black");
                    	tile.anim.ScaleAnimation(4);
                    	tileMap.SetTile(x, y, tile);
                    	break;
                }
                x++;
            }
            y++;
        }
    }

    // Managing and Loading levels
    public Level LoadLevel(int level)
    {
    	return LEVEL_ONE;
    }

    // Getting the Current Level
    public Level GetLevel() { return level; }
    
    // -----------------------------------------------------------
    // code for loading sprites and images
    // -----------------------------------------------------------
	private void AddRect(TileMap map, Rect sprite, int tileX, int tileY)
    {
        if (sprite != null) 
        {    
        	// clone the sprite from the "host"
            sprite.SetX( (TileMapRenderer.TilesToPixels(tileX) + (TileMapRenderer.TilesToPixels(1) - sprite.GetW()) / 2));
            sprite.SetY( (TileMapRenderer.TilesToPixels(tileY  + 1) - sprite.GetH()) );
            
            // add it to the map
            map.AddRect(sprite);
        }
    }
	
	private static Font LoadFont(String filepath)
	{
		try 
		{
            // Load font file
			File fontFile = new File("Assets/"+filepath);	
            return Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(Font.PLAIN, 24);
        } 
		catch (IOException | FontFormatException e) 
		{
            e.printStackTrace();
            return null;
        }
	}
}
