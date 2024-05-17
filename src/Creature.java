import java.awt.Graphics2D;
import java.awt.Point;
import java.lang.reflect.Constructor;

/* Abstract class that defines what a creature is,
 * creatures are entities which can move around,
 * and are effected by physics unless otherwise stated
 */
public abstract class Creature extends Rect{
	
	// Physics TODO: Refactor to be less ugly
	
	// Statistics
	// Movement Stats
	public float maxSpeed = 13;
	public final float acceleration = 0.25f;
	public final float deceleration = acceleration/2;
	
	// Combat Stats
	public int health = 3;
	public int damage = 1;
	
	// Generic states in which a creature can remain in
	public static final int STATE_NORMAL = 0;
	public static final int STATE_DYING = 1;
	public static final int STATE_DEAD = 2;

	// Character States
	protected int  state;
	protected long stateTime;
	
	// Bullet Spawner for all creatures
	public BulletSpawner gun;
	
	// Get if creature is attempting to move
	public boolean 	MOV_LT, MOV_RT, 
					MOV_UP, MOV_DN;

	// Death Animations for all creatures to use
	public static Animation[] explosions = InitializeExplosions();
	
	protected Animation death;
	
	// Constructor
	public Creature(int x, int y, int width, int height) 
	{ 
		super(x, y, width, height); 
		gun = new BulletSpawner(x, y, 16, 16, 90, 2);
		Initialize();
	}
	
	public void Initialize() 
	{
		InitializeExplosions();
		death = explosions[MathF.RandomRange(0, 3)];
	}
	
	// Process Damage Taken
	public void ReceiveDamage(int damage) 
	{
		this.health -= damage;
		if (health <= 0) { SetState(STATE_DYING); }
	}
	
	public void StopMoving() {MOV_LT = MOV_RT = MOV_DN = MOV_UP = false;} 
	
	public void Update(float deltaTime) 
	{
		if (IsState(STATE_DYING) || IsState(STATE_DEAD) ) 
		{ 
			anim = death;
			if (anim.Ended())
			{
				SetState(STATE_DEAD);
			}
			else	
			{
				anim.Update(Game2D.Delta());
			}
		}
		
		if (!IsState(STATE_DYING) || IsState(STATE_DEAD) )
		{
			Move();
			UpdateAnimation();
		}
		
		UpdateGun(deltaTime);
	}

	public abstract void UpdateAnimation();
	public abstract void Move();
	
	// Object Cloning
	public abstract Object Clone(int x, int y);

	// Draw the spirte of the character
	public void DrawSprite(Graphics2D pen, float offsetX, float offsetY) 
	{
		pen.drawImage(anim.GetImage(), 
				(int) GetX() - (GetImage().getWidth()  / 2) + (GetW() / 2), 
				(int) GetY() - (GetImage().getHeight() / 2) + (GetH() / 2), 
				null);
	}
	
	// Announce & Set State
	public void SetState(int state) 
	{
		if (this.state != state)
		{
			this.state = state;
			stateTime = 0;
			if (state == STATE_DYING)
			{
				// TODO: Stop the creature from moving
			}
		}
	}
	
	public void UpdateGun( float deltaTime ) 
	{
		gun.SetX( GetX() + (GetH()/2) - (gun.GetCaliber().GetH() / 2) );
		gun.SetY( GetY() + (GetW()/2) - (gun.GetCaliber().GetW() / 2) );
		gun.UpdateBullets(deltaTime);
	}
	
	private static Animation[] InitializeExplosions() 
	{
		Animation[] Explosions = new Animation[3];

		for (int i = 0; i < Explosions.length; i++)
		{
			Explosions[i] = Animation.ReadSheet("SmokeFX/SmokeFX-01", 64, 64, 2, i, 16);
			Explosions[i].ScaleAnimation(3);
		}
		
		return Explosions;
	}
	
	public BulletSpawner GetGun() { return gun; }
	
	// Function to use the bulletspawner 
	// All methods to fire the gun should go here 
	public abstract void ShootGun();
	
	public int 	    GetState() 	  {return state;}
	public boolean  IsState(int otherState) { return GetState() == otherState; }
	public boolean  IsAlive() 	  {return state == STATE_NORMAL;}
	public float    GetMaxSpeed() {return 0;}
}
