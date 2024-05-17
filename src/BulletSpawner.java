import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;

// All credit goes to the danmakufu community for making such an intuitive guide on Curtain-Firing Game Development.
// https://sparen.github.io/ph3tutorials/docs.html#sub16ShotFunctions
// https://www.bulletforge.org/projects
// https://en.touhouwiki.net/wiki/Touhou_Danmakufu

public class BulletSpawner 
{
	
	// Objective: Make an object that fires bullets from an object pool of bullets
	private ArrayList<Bullet> magazine;
	private Float  	 position;
	private int 	 currentbullet;
	
	// Primary Bullet this spawner can create
	// TODO: Make this changeable
	private Bullet caliber;
	
	// Basic Statistics, enemies' damage do not need to exceed one, but the player does.
	private int		 damage = 1;
	private float 	 speed  = 12;
	
	// All bullets by default will aim at the player, different angle values will shoot at the player at an offset
	// turning this off allows for free aiming of the bullets
	public boolean aimPlayer = true;
	
	public BulletSpawner(int x, int y, int bulletWidth, int bulletHeight, int angle, int shotType) 
	{		
		caliber = new Bullet(x, y, bulletWidth, bulletHeight, damage, angle, speed, shotType);
		
		magazine = new ArrayList<Bullet>();
		position = new Float(x, y);
	}
	
	// TODO: REFACTOR
	
	// Shooting function for shooting a bullet at a constant speed.
	// Delay specifies the amount of time until the bullet is fired.
	public void FireA1(float x, float y, int angle, float speed, float delay) 
	{	
		FireA2 ( x, y, angle, speed, 1, speed, delay );
	}
	
	// Fires a bullet that will move at Angle and Speed Defined,
	// Incrementing by acceleration every frame, capped at maximum speed.
	public void FireA2(float x, float y, int angle, float speed, float acceleration, float maxSpeed, float delay) 
	{
		LoadMagazine();
		IncrementBullet();
		
		// Set the Bullet's Position and angle
		if (aimPlayer) 	magazine.get(currentbullet).SetAngle( angle + FindPlayer() );
		else			magazine.get(currentbullet).SetAngle( angle );
		
		magazine.get(currentbullet).SetInitialSpeed(speed);
		magazine.get(currentbullet).SetAcceleration(acceleration);

		PlaceAtX(x - (caliber.width / 2) );
		PlaceAtY(y - (caliber.height/ 2) );
		UpdateSpawnpoint();
		
		magazine.get(currentbullet).timer = 0f;
				
		// Spawn the bullet after the delay
		magazine.get(currentbullet).QueueActivation(delay);
		
		IncrementBullet();
	}

	public void FireEllipseA1( float x, float y, int amt, int angle, int apexX, int apexY, float radius, float speed, float delay) 
	{
		FireEllipseA2( x, y, amt, apexX, apexY, angle, radius, speed, 1, 1, delay );
	}
	
	public void FireEllipseA2( float x, float y, int amt, int angle, int apexX, int apexY, float radius, float speed, float acceleration, float maxSpeed, float delay ) 
	{
		// Spawn a bullet on the spawner's position
		float spawnX = GetX() - caliber.GetW()/2;
		float spawnY = GetY() - caliber.GetH()/2;

		int   angleT = angle;
				
		for ( int i = 1; i <= amt ; i++ )
		{
			// Spawn the bullet around the spawner
			float circX  = (float) (spawnX + apexX * MathF.GetCos(angleT));
			float circY  = (float) (spawnY + apexY * MathF.GetSin(angleT));

			FireA2(circX, circY, angleT, speed, acceleration, maxSpeed, delay);
					
			angleT += 360/amt;
		}
	}
	
	public void FireCircleA1 ( float x, float y, int amt, int angle, float radius, float speed, float delay )
	{
		FireEllipseA1( x, y, amt, 90, 90, angle, radius, speed, delay );
	}
	
	public void FireCircleA2 ( float x, float y, int amt, int angle, float radius, float speed, float acceleration, float maxSpeed, float delay )
	{
		FireEllipseA2( x, y, amt, angle, 90, 90, radius, speed, acceleration, maxSpeed, delay );
	}

	public void FireSpreadA1 ( float x, float y, int amt, int angle, float spacing, float speed, float delay )
	{		
		for (int i = -( amt-1 )/2 ; i < ( amt-1 )/2 + 1; i++)
		{
			float angleoffset = angle + (spacing * i);
			FireA1( x, y, (int) angleoffset, speed, delay );
		}
	}
	
	public void FireSpreadA2 ( float x, float y, int amt, int angle, float spacing, float speed, float acceleration, float maxSpeed, float delay )
	{		
		for (int i = -( amt-1 )/2 ; i < ( amt-1 )/2 + 1; i++)
		{
			float angleoffset = angle + (spacing * i);
			FireA2( x, y, (int) angleoffset, speed, acceleration, maxSpeed, delay );
		}
	}
		
	public void FireStackA1  ( float x, float y, int amt, int angle, float speedOffset, float speed, float delay ) 
	{
		for ( int i = 0 ; i < amt ; i++ )
		{
			FireA1( x, y, angle, speed + speedOffset*i, delay );
		}
	}
	
	public void FireStackA2  ( float x, float y, int amt, int angle, float speedOffset, float speed, float acceleration, float maxSpeed, float delay ) 
	{
		for ( int i = 0 ; i < amt ; i++ )
		{
			FireA2( x, y, angle, speed + speedOffset*i, acceleration, maxSpeed, delay );
		}
	}
	
	
	// Cool things to make with these:
	// https://wiki.froth.zone/wiki/Lissajous_curve?lang=en
	// https://wiki.froth.zone/wiki/Parametric_equation?lang=en
	// https://sparen.github.io/ph3tutorials/ph3u1l11.html
	
	// if x and y are not provided, just place it at spawner position
	
	public void FireA1		 (int angle, float speed, float delay) 
	{	
		FireA1(GetX(), GetY(), angle, speed, delay);
	}

	public void FireA2		 (int angle, float speed, float acceleration, float maxSpeed, float delay) 
	{	
		FireA2(GetX(), GetY(), angle, speed, acceleration, maxSpeed, delay);
	}
	
	public void FireCircleA1 ( int amt, int angle, float radius, float speed, float delay ) 
	{
		FireCircleA1( GetX(), GetY(), amt, angle, radius, speed, delay  );
	}

	public void FireCircleA2 ( int amt, int angle, float radius, float speed, float acceleration, float maxSpeed, float delay ) 
	{
		FireCircleA2( GetX(), GetY(), amt, angle, radius, speed, acceleration, maxSpeed, delay );
	}
	
	public void FireEllipseA1( int amt, int angle, int apexX, int apexY, float radius, float speed, float delay ) 
	{
		FireEllipseA1(GetX(), GetY(), amt, angle, apexX, apexY, radius, speed, delay);
	}
	
	public void FireEllipseA2( int amt, int angle, int apexX, int apexY, float radius, float speed, float acceleration, float maxSpeed, float delay ) 
	{
		FireEllipseA2( GetX(), GetY(), amt, angle, apexX, apexY, radius, speed, acceleration, maxSpeed, delay );
	}

	public void FireSpreadA1 ( int amt, int angle, float spacing, float speed, float delay )
	{
		FireSpreadA1 ( GetX(), GetY(), amt, angle, spacing, speed, delay);
	}
	
	public void FireSpreadA2 ( int amt, int angle, float spacing, float speed, float acceleration, float maxSpeed, float delay )
	{
		FireSpreadA2 ( GetX(), GetY(), amt, angle, spacing, speed, acceleration, maxSpeed, delay);
	}
	
	public void FireStackA1  ( int amt, int angle, float speedOffset, float speed, float delay ) 
	{
		FireStackA1(GetX(), GetY(), amt, angle, speedOffset, speed, delay);
	}
	
	public void FireStackA2  ( int amt, int angle, float speedOffset, float speed, float acceleration, float maxSpeed, float delay ) 
	{
		FireStackA2( GetX(), GetY(), amt, angle, speedOffset, speed, acceleration, maxSpeed, delay );
	}
	
	
	// Clears the screen of any bullets made by this spawner.
	public void DeleteAll() 
	{
		magazine.removeIf( (Bullet bullet) -> bullet.IsOffscreen() );
		magazine.trimToSize();
		IncrementBullet();
	}
	
	// Goes to the next bullet in the list.
	public void IncrementBullet() 
	{
		currentbullet = magazine.size() - 1;
	}

	public void SetX(float x) { position.x = x; }
	public void SetY(float y) { position.y = y; }
	
	public float GetX()	{ return position.x; }
	public float GetY()	{ return position.y; }
	
	public void PlaceAtX( float x ) 
	{ 
		float spawnX = x + magazine.get(currentbullet).GetW()/2;
		magazine.get(currentbullet).SetX(spawnX); 
	}
	
	public void PlaceAtY( float y ) 
	{ 
		float spawnY = y + magazine.get(currentbullet).GetH()/2;
		magazine.get(currentbullet).SetY(spawnY);		
	}
	
	public void UpdateSpawnpoint() 
	{ 
		Bullet thisBullet 	  = magazine.get(currentbullet);
		thisBullet.spawnpoint = new Float(thisBullet.GetX(), thisBullet.GetY()); 
	}
	
	public void UpdateBullets(float deltaTime) 
	{
		magazine.forEach( (Bullet bullet) -> {
			bullet.Update(deltaTime);
		});
		
		magazine.removeIf( ( Bullet bullet ) -> bullet.IsExpired() );
	}
	
	private int FindPlayer() 
	{
		float playerX = GameManager.player.GetX();
		float playerY = GameManager.player.GetY();
		
		playerX += GameManager.player.GetW() / 2;
		playerY += GameManager.player.GetH() / 2;
		
		Point2D.Float playerPosition = new Point2D.Float (playerX, playerY);
		return MathF.FindAngle(position, playerPosition);
	}
	
	public ArrayList<Bullet> GetBullets() 
	{
		return magazine;
	}
	
	public void LoadMagazine() 
	{
		// Get the same time of caliber to add to the magazine
		magazine.add( (Bullet) caliber.Clone( (int) GetX(), (int) GetY() ));
	}
	
	public boolean IsEmpty() { return magazine.isEmpty(); }
	
	public Bullet GetCaliber() { return caliber; }
	
	public void Draw(Graphics pen) 
	{
		magazine.forEach( (Bullet bullet) -> {
			bullet.Draw(pen);
		});
	}
	
	// Draw the rectangle for debugging
	public void DrawDebug(Graphics pen)
	{
		pen.setColor(Color.RED);
		magazine.forEach( (Bullet bullet) -> {
			bullet.DrawDebug(pen);
		});
	}
}
