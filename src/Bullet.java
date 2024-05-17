import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D.Float;

public class Bullet extends Rect
{

	public float lifetime;
	public int 	 damage;
	
	public float rotation = 0f;
	public float timer    = 0f;
	public float maxSpeed;
	
	public float timetoactivate = 0f;
	public float acceleration   = 1f;
	
	public boolean expired  = false;
	public boolean expiring = false;
	public boolean queued   = false;
	
	public int shotType;
	
	public Float spawnpoint;
	
	// Calculate Angle of Bullets
	double[] cos = MathF.cos;
	double[] sin = MathF.sin;
	private double cosA;
	private double sinA;
	
	// Angle in Degrees
	public int angle;
	
	private Animation moving, hit;
	
	// TODO: Fix bullet rendering
	public Bullet(float x, float y, int width, int height, int damage, int angle, float maxSpeed, int shotType) 
	{
		super(x, y, width, height);
		spawnpoint = new Float(x, y);
		
		this.maxSpeed = maxSpeed;
		this.damage   = damage;
		this.shotType = shotType;
		
		this.SetVelX(1);
		this.SetVelY(1);
		
		AssignAnimation();
		
		anim = moving;
		
		this.angle = MathF.ValidAngle(angle);
		this.cosA  = MathF.GetCos(angle);
		this.sinA  = MathF.GetSin(angle);
	}
	
	public void Update(float deltaTime) 
	{
		super.Update(deltaTime);
		
		if (! (expired || expiring ) )
		{
			if ( IsOffscreen() ) 
			{
				timer = 0; 
				expired = true;
				SetVelX(1);
			}
		
			// in case time needs to be used later
			timer += deltaTime;
			// System.out.println("timer: " + timer);
			pos = Movement();
		}
		
		if (queued) 
		{
			timetoactivate -= deltaTime;
			if (timetoactivate <= 0) 
			{
				queued = false;
				Activate();
			}
		}
		
		if 	( ( expiring && anim == hit && anim.Ended() ) || IsOffscreen()) 
			{ expired = true; }
		
		if ( moving != null )
		{
			UpdateAnimation();
		}
	}
	
	public void AssignAnimation() 
	{
		moving = new Animation();
		hit = new Animation();
		
		if (shotType == 1)
		{
			moving.AddFrame(Animation.ReadImage("1BitSpacePack/BulletYellow/BulletYellow-ID"), 100);
			hit.AddFrame(Animation.ReadImage("1BitSpacePack/BulletYellow/BulletYellow-HT_1"), 5);
			hit.AddFrame(Animation.ReadImage("1BitSpacePack/BulletYellow/BulletYellow-HT_2"), 5);
			hit.AddFrame(Animation.ReadImage("1BitSpacePack/BulletYellow/BulletYellow-HT_3"), 5);
		}
		else if (shotType == 2)
		{
			moving.AddFrame(Animation.ReadImage("1BitSpacePack/BulletRed/BulletRed-ID"), 100);
			hit.AddFrame(Animation.ReadImage("1BitSpacePack/BulletRed/BulletRed-HT_1"), 5);
			hit.AddFrame(Animation.ReadImage("1BitSpacePack/BulletRed/BulletRed-HT_2"), 5);
			hit.AddFrame(Animation.ReadImage("1BitSpacePack/BulletRed/BulletRed-HT_3"), 5);
		}
		
		// All Bullets are 16x16. If this changes, please update accordingly
		moving.ScaleAnimation( 6 );
		hit.ScaleAnimation( 6 );
	}
	
	public void UpdateAnimation() 
	{
		Animation newAnim = anim;
		
		if (!expiring)
			newAnim = moving;
		else
		if (hit != null && expiring)
		{
			newAnim = hit;
		}
		
		
		// Update Animations
		if (anim != newAnim) 
		{
			anim = newAnim;
			anim.Start();
		}
		else
		{
			anim.Update(Game2D.Delta());
		}
		
	}

	public Float Movement() 
	{
		SetVelX(MathF.MoveTowards(GetVelX(), maxSpeed, acceleration));
		
		float x = (float) (GetVelX() * maxSpeed * cosA);
		float y = (float) (GetVelX() * maxSpeed * sinA);
		
		return new Float(x + GetX(), y + GetY());
	}
	
	public boolean IsExpired() { return  expired;  }
	public void 	Activate() { expired = false;  }
	public void	  Deactivate() { expired =  true;  }
	
	public void QueueActivation (float deadline) 
	{
		queued = true;
		timetoactivate = deadline;
	}
	
	public void SetAngle (int angle) 
	{ 
		this.angle = MathF.ValidAngle(angle);
		this.cosA  = MathF.GetCos(angle);
		this.sinA  = MathF.GetSin(angle);
	}
	
	public void MoveAngle(int da)	 
	{ 
		this.angle += MathF.ValidAngle(da);
		this.cosA  += MathF.GetCos(da);
		this.sinA  += MathF.GetSin(da);
	}
	
	public void SetInitialSpeed (float dx)	  { SetVelX(dx); }
	public void SetMaxSpeed 	(float ds) 	  { maxSpeed  = ds; }
	public void SetDamage		(int dd) 	  { damage    = dd; }
	public void SetAcceleration (float accel) { acceleration = accel; }
	
	public void DrawDebug(Graphics pen) 
	{
		if (!expired) 
		{ 
			super.Draw(pen); 
			pen.fillRect((int) GetX(), (int) GetY(), GetW(), GetH());
		}
		
	}
	
	public void Draw(Graphics pen)
	{
		pen.drawImage(
				GetImage(), 
				(int) GetX() - (GetImage().getWidth()  / 2) + (GetW() / 2), 
				(int) GetY() - (GetImage().getHeight() / 2) + (GetH() / 2), 
				null
		);
	}
	
	// Cloning this Object
	public Object Clone(int x, int y) 
	{ 
		Bullet newBullet = new Bullet( x, y, width, height, damage, angle, maxSpeed, shotType);
		return newBullet; 
	}
}
