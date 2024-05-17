import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioFormat;

public class GameManager extends Game2D
{
	// Managers
	protected InputManager 	  inputManager;
	protected ResourceManager resourceManager;
	protected TileMapRenderer renderer;
	protected Sound sound;
	//protected SoundManager	  soundManager;
	
	// Managing Game State
    private boolean inMenu   = true,
					inBattle = false;
	
    // To pause a song
    public long songTime;
    
    // Sounds and Songs
    // uncompressed, 44100Hz, 16-bit, mono, signed, little-endian
    //private static final AudioFormat PLAYBACK_FORMAT = new AudioFormat(44100, 16, 1, true, false);
    //private SoundManager.Sound hitSound;
    //private SoundManager.Sound Stage1_Song;
    
	// Map
	private TileMap map;
	
	// GameActions and input manager
	private GameAction   CMD_UP, CMD_LT, 
				 		 CMD_RT, CMD_DN,
				 		 CMD_FC, CMD_SH,
				 		 CMD_BM, CMD_PA;
	
	// Player
	public static Player player;
	
	// Enemies loaded to the game
	protected ArrayList<Enemy> enemyQueue =  new ArrayList<Enemy>();
	protected static boolean NoEnemies;
	
	// Orphaned Bullet Spawners
	// Allows for enemies who have already been removed from memory to have their bullets remain on screen
	protected ArrayList<Bullet> orphanedBullets = new ArrayList<Bullet>();
	
	public static void main(String[] args) 
	{
        new GameManager().run();
    }
	
	// Initialization Process
	public void Init() 
	{
		super.Init();
		
		// Start Input Manager
		InitInput();
		System.out.println("Input Manager Loaded");
		
		// Start Resource Manager
		resourceManager = new ResourceManager();
		System.out.println("Resource Manager Loaded");
		
		// Load Resources
		renderer = new TileMapRenderer();
		// renderer.SetBackground(null);
		System.out.println("TileMap Renderer Loaded");
		
		// Load Sounds
		sound = new Sound();
		//LoadSound();
		//System.out.println("Sound Manager Loaded");
		
		
		// Load First Map
		map = resourceManager.LoadNextMap();
		// PlayMusic(1);
		player = map.GetPlayer();
	}
	

	public void Update()
	{
		if (IsInBattle() ) { BattleLoop();  }
		if (IsInMenu()   ) { MenuLoop();	}
		
		ReportFPS();
	}
	
	
	private void BattleLoop() 
	{
		ClampPlayer();
		player.Update(Game2D.Delta());
		UpdateCreature(player, Game2D.Delta());
		
		// Hardcoding for now but refactor when working
		resourceManager.GetLevel().Update();
		
		if (!resourceManager.GetLevel().GetEnemies().isEmpty()) 
		{
			enemyQueue.addAll(resourceManager.GetLevel().GetEnemies());
			resourceManager.GetLevel().ClearQueue();
		}
		
		if ( !enemyQueue.isEmpty() )
		{
			enemyQueue.forEach( enemy -> {
				enemy.Update( Game2D.Delta() );
				UpdateCreature(enemy, Game2D.Delta());
				
				if (enemy.IsState(Creature.STATE_DEAD ) || enemy.IsOffscreen()) 
				{
					orphanedBullets.addAll(enemy.gun.GetBullets());
				}
				
			});
		}
		
		enemyQueue.removeIf( enemy -> enemy.IsOffscreen() || enemy.IsState( Creature.STATE_DEAD ) );
		enemyQueue.trimToSize();
		
		orphanedBullets.forEach( bullet -> {
			bullet.Update(Game2D.Delta());
		});
		
		orphanedBullets.removeIf( bullet -> ( bullet.expired ) );
		
		PlayerBulletUpdate();
		
		// TODO: Do a better way of doing this
		if (enemyQueue.isEmpty()) {NoEnemies = true;}
		else {NoEnemies = false;}
	}
	
	private void MenuLoop() 
	{
		GetMenuInput();
	}
	
	// Creation of Player Input Actions
	private void InitInput() 
	{
		CMD_LT = new GameAction("MoveLeft");
		CMD_RT = new GameAction("MoveRight");
		CMD_UP = new GameAction("MoveUp");
		CMD_DN = new GameAction("MoveDown");
		CMD_SH = new GameAction("Shoot");
		CMD_FC = new GameAction("Focus");
		
		CMD_PA = new GameAction("Pause", GameAction.INIT_PRESS_ONLY);
		CMD_BM = new GameAction("Bomb",  GameAction.INIT_PRESS_ONLY);
		
		inputManager = new InputManager(sm.GetWindow());
		
		inputManager.MapToKey(CMD_PA, KeyEvent.VK_ESCAPE);
		inputManager.MapToKey(CMD_UP, KeyEvent.VK_UP	);
		inputManager.MapToKey(CMD_LT, KeyEvent.VK_LEFT	);
		inputManager.MapToKey(CMD_FC, KeyEvent.VK_SHIFT	);
		inputManager.MapToKey(CMD_RT, KeyEvent.VK_RIGHT	);
		inputManager.MapToKey(CMD_DN, KeyEvent.VK_DOWN	);
		inputManager.MapToKey(CMD_SH, KeyEvent.VK_Z		);
		inputManager.MapToKey(CMD_BM, KeyEvent.VK_X		);
	}
	
	// Check player inputs
	public void GetInput()
	{
		Player player = (Player) map.GetPlayer();
		
		// Combat Commands
		player.CMD_SH = CMD_SH.IsPressed();
		player.CMD_FC = CMD_FC.IsPressed();
		
		// Movement Commands
		player.MOV_RT = CMD_RT.IsPressed();
		player.MOV_LT = CMD_LT.IsPressed();
		player.MOV_UP = CMD_UP.IsPressed();
		player.MOV_DN = CMD_DN.IsPressed();
		
		if (CMD_PA.IsPressed()) { TogglePause(); }
	}
	
	public void GetMenuInput() 
	{
		if (CMD_PA.IsPressed() || CMD_SH.IsPressed())
		{
			TogglePause();
		}
	}
	
	/*
	// For testing enemy Movement, to delete later
	public void DrawLines(Graphics2D pen) 
	{
		float tension = 0.5f;
		int segments = 20;
		
		Line testpoints = new Line( 
		new Point2D.Float[] {
			new Float(600, -30), // Always make the first path the spawnpoint
			new Float(600, Game2D.SCREEN_HEIGHT - 400),
			new Float(500, Game2D.SCREEN_HEIGHT - 300),
			new Float(600, Game2D.SCREEN_HEIGHT - 200),
			new Float(700, Game2D.SCREEN_HEIGHT - 300),
			new Float(600, Game2D.SCREEN_HEIGHT - 400),
			new Float(500, Game2D.SCREEN_HEIGHT - 300),
			new Float(600, Game2D.SCREEN_HEIGHT - 200),
			new Float(700, Game2D.SCREEN_HEIGHT - 300),
			new Float(600, Game2D.SCREEN_HEIGHT - 400),
			new Float(-30, -30)
		}, tension, segments);
		
		Line testpoints2 = new Line( 
		new Point2D.Float[] {
				new Float(200, -30), // Always make the first path the spawnpoint
				new Float(300, 200),
				new Float(200, 400),
				new Float(100, 600),
				new Float(200, 700),
				new Float(300, 800),
				new Float(200, 1000),
				new Float(100, 1200),
				new Float(200, 1400),
				new Float(300, 1600),
				new Float(200, Game2D.SCREEN_HEIGHT+30)
		},  tension, segments);

		//testpoints.Draw(pen);
		testpoints2.Draw(pen);
	}
	*/
	
	public void Draw(Graphics2D pen) 
	{
        if (IsInBattle()) { RenderBattle(pen); }
        else
        if (IsInMenu()) { RenderMenu(pen); }
	}
	
	public void RenderMenu(Graphics2D pen)
	{
		renderer.Draw(pen, map, SCREEN_HEIGHT, SCREEN_HEIGHT);
		pen.setColor(Color.WHITE);
		
		pen.setFont(ResourceManager.NICER_NIGHTIE.deriveFont(Font.PLAIN, 24f));
		pen.drawString("Songs by +TEK", SCREEN_WIDTH / 10, SCREEN_HEIGHT - 50);
		
		pen.setFont(ResourceManager.NICER_NIGHTIE.deriveFont(Font.PLAIN, 72f));
		pen.drawString("Emergency Everyday", SCREEN_WIDTH / 10, SCREEN_HEIGHT - 172);
		
		pen.setFont(ResourceManager.NICER_NIGHTIE.deriveFont(Font.PLAIN, 36f));
		pen.drawString("Press ESC or Z to play.", SCREEN_WIDTH / 10, SCREEN_HEIGHT - 100);
	}
	
	public void RenderBattle(Graphics2D pen) 
	{
		renderer.Draw(pen, map, SCREEN_HEIGHT, SCREEN_HEIGHT);
		
		if (debug)
		{
			DrawGrid(pen);
			
			pen.setColor(Color.GRAY);
			pen.drawString("(" + player.GetX() + ", " + player.GetY() +")" , 200, SCREEN_HEIGHT - 200);
			
			player.gun.DrawDebug(pen);
			player.Draw(pen);
			
			enemyQueue.forEach ( enemy -> { 
				enemy.Draw(pen);
				enemy.gun.DrawDebug(pen);
			});
			
			orphanedBullets.forEach ( bullet -> { bullet.Draw(pen); }  );
			
		}
		
		enemyQueue.forEach ( enemy -> { 
			enemy.DrawSprite(pen, enemy.GetX(), enemy.GetY());
			enemy.gun.Draw(pen);
		});
		
		orphanedBullets.forEach ( bullet -> { 
			bullet.Draw(pen);
		});
	}
	
	public void ClampPlayer() 
	{
		if (player.GetX() + player.GetW() > sm.GetWidth()) 
			player.SetX(sm.GetWidth() - player.GetW());
		
		if (player.GetX() < 0)
			player.SetX(0);
		
		if (player.GetY() + player.GetH() > sm.GetHeight())
			player.SetY(sm.GetHeight() - player.GetH());
		
		if (player.GetY() < 0)
			player.SetY(0);
	}
	
	public void PlayerBulletUpdate() 
	{
		player.GetGun().GetBullets().forEach( (Bullet bullet) -> {
			enemyQueue.forEach( enemy -> {
				if (bullet.Overlaps(enemy) && !(bullet.expiring || bullet.IsExpired())) 
				{
					bullet.expiring = true;
					PlaySFX(2);
					enemy.ReceiveDamage(bullet.damage);
				}
			});
		});
	}
	
	// Checks where a character is going depending on their velocities (dx, dy)
	
	// Handles Collisions and Changes Course of Character
	public void UpdateCreature(Creature creature, float deltaTime) 
	{		
		// Checking Horizontal Collisions
		// Change X Coordinate
		float dx   = creature.GetVelX();
		float oldX = creature.GetX();
		float newX = oldX + dx;
		
		// Set to new Coordinate
		creature.SetX( newX );
		
		// Change Y Coordinate
		float dy   = creature.GetVelY();
		float oldY = creature.GetY();
		float newY = oldY + dy;
				
		// Set to new Coordinate
		creature.SetY( newY ); 
		
		// Apply Friction
		FrictionHandler(creature);
		
		// if (creature instanceof Player) { CheckPlayerCollision((Player) creature, false); }
	}
		
	// Crunch the numbers for friction
	public void FrictionHandler (Creature creature) 
	{
		// Get Deceleration
		float deceleration = creature.deceleration;
		
		if (!(creature.MOV_LT || creature.MOV_RT))
		{
			// Sliding in a SHMUP can be rage inducing, Do not apply air friction to player.
			if (creature instanceof Player) { creature.SetVelX(0); } 
			
			// Stop moving infintesmally small amounts
			if (Math.abs(creature.GetVelX()) < 0.50f) {creature.SetVelX(0);}		
			creature.SetVelX( MathF.Lerp(creature.GetVelX(), 0, deceleration) );
		}
		
		if (!(creature.MOV_UP || creature.MOV_DN) )
		{
			// Sliding in a SHMUP can be rage inducing, Do not apply air friction to player.
			if (creature instanceof Player) { creature.SetVelY(0); } 
			
			// Stop moving infintesmally small amounts
			if (Math.abs(creature.GetVelY()) < 0.50f) {creature.SetVelY(0);}		
			
			creature.SetVelY( MathF.Lerp(creature.GetVelY(), 0, deceleration) );
		}
	}
	
	/* TODO: Player Collisions
	// Check for player collision with other sprites
	public void CheckPlayerCollision(Player player, boolean canKill) 
	{
		if (!player.IsAlive()) {return;}
			
		Rect collisionSprite = GetSpriteCollision(player);
			
		if (collisionSprite instanceof Creature)
		{
			Creature badguy = (Creature) collisionSprite;
			if (canKill) {}
			else { player.health--; }
		}
	}
	
	public boolean IsCollision(Rect s1, Rect s2) 
	{
        // if the Sprites are the same, return false
        if (s1 == s2) { return false; }    
        return s2.CheckCollision(s1);
    }
	
	public Enemy GetEnemyCollision() 
	{
		enemyQueue.forEach(enemy -> {
			if (IsCollision())
		});
	}
	*/
	
	public void PlayMusic(int i) 
	{
		sound.SetFile(i);
		sound.SetVolume(0.07f);
		sound.Play();
		sound.Loop();
	}
	
	public void StopMusic(int i)
	{
		sound.Stop();
	}
	
	
	public void PlaySFX(int i)
	{
		sound.SetFile(i);
		sound.SetVolume(0.07f);
		sound.Play();
	}
	
	// Load in the map
	public TileMap GetMap() { return map; }

	// Announce the Game State
	public boolean IsInMenu() 	 { return inMenu;	}
	public boolean IsInBattle()  { return inBattle;	}
	public void   TogglePause()  { inBattle = !inBattle; 
								   inMenu 	= !inBattle; 
								   renderer.drawPlayer = inBattle;}
	// Announces there are no more enemies on screen.
	public static boolean NoEnemies() { return NoEnemies; }
}
