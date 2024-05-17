import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;

/**
    The TileMapRenderer class draws a TileMap on the screen.
    It draws all tiles, sprites, and an optional background image
    centered around the position of the player.

    <p>If the width of background image is smaller the width of
    the tile map, the background image will appear to move
    slowly, creating a parallax background effect.

    <p>Also, three static methods are provided to convert pixels
    to tile positions, and vice-versa.

    <p>This TileMapRender uses a tile size of 64.
*/
public class TileMapRenderer 
{

	public boolean drawPlayer = false;
	
    private static final int TILE_SIZE = 64;
    // the size in bits of the tile
    // Math.pow(2, TILE_SIZE_BITS) == TILE_SIZE
    private static final int TILE_SIZE_BITS = 6;

    private BufferedImage background;

    /**
        Converts a pixel position to a tile position.
    */
    public static int PixelsToTiles(float pixels) { return PixelsToTiles(Math.round(pixels)); }

    /**
        Converts a pixel position to a tile position.
    */
    public static int PixelsToTiles(int pixels) 
    {
        // use shifting to get correct values for negative pixels
        return pixels >> TILE_SIZE_BITS;

        // or, for tile sizes that aren't a power of two,
        // use the floor function:
        //return (int)Math.floor((float)pixels / TILE_SIZE);
    }


    /**
        Converts a tile position to a pixel position.
    */
    public static int TilesToPixels(int numTiles) 
    {
        // no real reason to use shifting here.
        // it's slighty faster, but doesn't add up to much
        // on modern processors.
        return numTiles << TILE_SIZE_BITS;

        // use this if the tile size isn't a power of 2:
        // return numTiles * TILE_SIZE;
    }


    /**
        Sets the background to draw.
    */
    public void SetBackground(BufferedImage background) 
    {
    	float ratio = (Game2D.SCREEN_HEIGHT / background.getHeight() );
    	int   bgh = background.getHeight();
    	int   bgw = background.getWidth();
    	
    	int newW = (int) (bgw * ratio * 2);
    	int newH = (int) (bgh * ratio * 2);
    	
        Image tmp = background.getScaledInstance( newW , newH, Image.SCALE_FAST);
        BufferedImage newBg =  new BufferedImage( newW , newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = newBg.createGraphics();
        
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        this.background = newBg;
    }


    /**
        Draws the specified TileMap.
    */
    public void Draw(Graphics2D pen, TileMap map, int screenWidth, int screenHeight)
    {
        // Get player and map dimensions
        Player player = map.GetPlayer();
        int mapWidth = TilesToPixels(map.GetWidth());

        // Calculate map scrolling offset based on player position
        int offsetX = CalculateOffsetX(player.GetX(), screenWidth, mapWidth);

        // Draw background image with parallax effect
        //DrawParallaxBackground(pen, screenWidth, screenHeight, offsetX);

        // Draw visible tiles
        DrawVisibleTiles(pen, map, offsetX, screenWidth, screenHeight);

        if (drawPlayer)
        {
        	// Draw player
        	DrawPlayer(player, pen, offsetX);
        }
        // Draw other creatures
        DrawOtherCreatures(pen, map.GetEntities(), offsetX);
    }

    // Calculate horizontal offset for map scrolling
    private int CalculateOffsetX(float playerX, int screenWidth, int mapWidth) {
        int offsetX = screenWidth / 2 - Math.round(playerX) - TILE_SIZE;
        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX, screenWidth - mapWidth);
        return offsetX;
    }

    /*
    // Draw parallax background image
    private void DrawParallaxBackground(Graphics2D pen, int screenWidth, int screenHeight, int offsetX) 
    {
        if (background != null) {
            int x = offsetX * (screenWidth - background.getWidth(null)) / (screenWidth - TilesToPixels(map.GetWidth()));
            int y = screenHeight - background.getHeight(null);
            pen.drawImage(background, x, y, null);
        }
    }
    */

    // Draw visible tiles
    private void DrawVisibleTiles(Graphics2D pen, TileMap map, int offsetX, int screenWidth, int screenHeight) {
        int firstTileX = PixelsToTiles(-offsetX);
        int lastTileX = firstTileX + PixelsToTiles(screenWidth) + 1;

        for (int y = 0; y < map.GetHeight(); y++) {
            for (int x = 0; x <= map.GetWidth(); x++) {
                Rect rect = map.GetTile(x, y);
                if (rect != null) {
                    rect.Draw(pen);
                    if (rect.anim.GetImage() != null) {
                        rect.DrawImage(pen);
                    }
                }
            }
        }
    }

    // Draw player
    private void DrawPlayer(Player player, Graphics2D pen, int offsetX) 
    {
        int playerSpriteHeight = player.GetImage().getHeight();
        player.DrawSprite(pen, player.GetX() - (playerSpriteHeight / 2) + (player.width / 2) , player.GetY() - (playerSpriteHeight / 2) + (player.height / 2));
        player.Draw(pen);
    }

    // Draw other creatures
    private void DrawOtherCreatures(Graphics2D pen, Iterator entities, int offsetX) {
        while (entities.hasNext()) {
            Rect sprite = (Rect) entities.next();
            sprite.Draw(pen, sprite.GetX(), sprite.GetY());
            if (sprite instanceof Creature) {
                Creature creature = (Creature) sprite;
                int creatureX = Math.round(creature.GetX() + (creature.GetW() / 2) - creature.GetImage().getWidth(null) / 2) + offsetX;
                int creatureY = Math.round(creature.GetY() + creature.GetH() - creature.GetImage().getHeight(null));
                pen.drawImage(creature.GetImage(), creatureX, creatureY, null);
            }
        }
    }
}
