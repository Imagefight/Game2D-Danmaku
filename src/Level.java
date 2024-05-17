import java.awt.geom.Point2D;
import java.util.*;

/* Level:
 * A level has the information to schedule enemy spawns,
 * as well as a boss enemy at the end of a stage.
 * 
 */
public abstract class Level 
{
	protected Queue<Enemy> enemyQueue = new LinkedList<>();
	protected Queue<Wave > waveQueue  = new LinkedList<>();
	protected long levelTime = 0;
	protected float timeBetweenWaves = 200f;
	private long lastWaveSpawned = 0;
	// Pixels required to go off the screen
	int padding = 100;
	
	// Generic Lanes to spawn the enemies
	// https://shmups.wiki/library/Boghog%27s_bullet_hell_shmup_101
	protected static Point2D.Float [] SpawnLaneH = 
	{
			new Point2D.Float(100, -30),
			new Point2D.Float(200, -30),
			new Point2D.Float(300, -30),
			new Point2D.Float(400, -30),
			new Point2D.Float(500, -30),
			new Point2D.Float(600, -30),
			new Point2D.Float(700, -30),
			new Point2D.Float(800, -30),
			
	};
	
	protected static Point2D.Float [] SpawnLaneL = 
	{
			new Point2D.Float(-30, 000),
			new Point2D.Float(-30, 100),
			new Point2D.Float(-30, 200),
			new Point2D.Float(-30, 300),
			new Point2D.Float(-30, 400),
			new Point2D.Float(-30, 500),
			
	};
	
	protected static Point2D.Float [] SpawnLaneR = 
	{
		new Point2D.Float(Game2D.SCREEN_WIDTH + 30, 000),
		new Point2D.Float(Game2D.SCREEN_WIDTH + 30, 100),
		new Point2D.Float(Game2D.SCREEN_WIDTH + 30, 200),
		new Point2D.Float(Game2D.SCREEN_WIDTH + 30, 300),
		new Point2D.Float(Game2D.SCREEN_WIDTH + 30, 400),
		new Point2D.Float(Game2D.SCREEN_WIDTH + 30, 500),
	};				

	
	public Level() 
	{
		EnemySchedule();
		System.out.println("Level loaded");
	}
	
	// Run this 60 times per second
	public void Update()
	{
		levelTime += Game2D.Delta();
		SpawnEnemies();
	}
	
	// Schedule for Enemies to show up
	protected abstract void EnemySchedule();
	
	// Add enemies to the enemy Queue if their time is ready
	protected void QueueEnemy (Enemy enemy)
	{
		enemyQueue.add(enemy);
	}
	
	protected void SpawnEnemies() 
	{
		if (!waveQueue.isEmpty())
		{
			Wave currentWave = waveQueue.peek();
			long waveSpawnDelay = (long) (lastWaveSpawned + currentWave.initialSpawnTimer);
			
			System.out.println( (levelTime - lastWaveSpawned) + " :: " + timeBetweenWaves);
			
			if (levelTime >= waveSpawnDelay) 
			{
				if (currentWave.IsCompleted() && ( GameManager.NoEnemies() || levelTime - lastWaveSpawned >= timeBetweenWaves ))
				{
					waveQueue.poll(); // remove completed wave
					lastWaveSpawned = levelTime;
				}
				else
				{
					currentWave.SpawnEnemies(levelTime);
				}
			}
			
		}
	}
	
	
	public void ClearQueue() { enemyQueue.clear(); }
	
	protected void AddWave(Wave wave) 
	{
		waveQueue.offer(wave);
	}
	
	public ArrayList<Enemy> GetEnemies() 
	{ 
		return new ArrayList<> (enemyQueue); 
	}
	
	// Get values to go offscreen
	public int OffscreenLT() { return -padding; }
	public int OffscreenRT() { return Game2D.SCREEN_WIDTH  + padding; }
	public int OffscreenDN() { return Game2D.SCREEN_HEIGHT + padding; }
	public int OffscreenUP() { return OffscreenLT(); }
		
	
	// Enemy Waves
	public class Wave 
	{
	    private ArrayList<Enemy> enemies;
	    private int   currentIndex;
	    private float initialSpawnTimer; // Time until the first enemy spawns in
	    private float spawnInterval; 	 // Interval between enemy spawns in seconds
	    private long  lastSpawnTime;	 // Time of the last enemy spawn

	    // Spawn the enemy as fast as you can
	    public Wave( float spawnInterval ) 
	    {
	    	this.spawnInterval = spawnInterval;
	    	enemies = new ArrayList<>();
	    	initialSpawnTimer = 0;
	        currentIndex = 0;
	        lastSpawnTime = 0;
	    }
	    
	    // Delay spawning the first enemy by an amount of time between the last wave and this wave
	    // First spawn includes the spawnInterval
	    public Wave( float spawnInterval, float initialSpawnTimer ) 
	    {
	    	this.spawnInterval 		= spawnInterval;
	    	this.initialSpawnTimer 	= initialSpawnTimer;
	    	enemies = new ArrayList<>();
	        currentIndex = 0;
	        lastSpawnTime = 0;
	    }

	    public boolean IsCompleted() 
	    {
	        // Check if all enemies in the wave are spawned
	        return currentIndex >= enemies.size();
	    }

	    public void SpawnEnemies(long currentTime) 
	    {
	    	// Enough time has passed since the last spawn and there are more enemies to spawn
	    	if (currentTime - lastSpawnTime >= spawnInterval && currentIndex < enemies.size()) 
	        {
	            QueueEnemy(enemies.get(currentIndex));
	            currentIndex++;
	            lastSpawnTime = currentTime;
	        }
	    }
	    
	    public void MergeWave(Wave wave)
	    {
	    	wave.GetEnemies().forEach(enemy -> AddEnemy(enemy));
	    }

	    public void AddEnemy( Enemy enemy ) 
	    {
	    	enemies.add(enemy);
	    }
	    
	    public ArrayList<Enemy> GetEnemies() 
	    {
	        return enemies;
	    }
	}

}
