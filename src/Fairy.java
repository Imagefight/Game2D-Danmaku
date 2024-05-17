import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Fairy extends Enemy
{
	// Animations
	private Animation idle;
	private Animation left, right, up, down;
	private Animation shoot, shootLT, shootRT;
	
	public Fairy(int x, int y, int width, int height, int health) 
	{
		super(x, y, width, height, health);
		gun = new BulletSpawner(x, y, 16, 16, 90, 2);
		maxSpeed = 13f;
	}
	
	public Fairy(Point2D.Float pos, int width, int height, int health) 
	{
		super( pos, width, height, health);
		gun = new BulletSpawner( (int) pos.x, (int) pos.y, 16, 16, 90, 2);
		maxSpeed = 13f;
	}
	
	public void Initialize() 
	{
		super.Initialize();
		
		// Animate the enemy
		idle  = new Animation();
		shoot = new Animation();
		shootLT = new Animation();
		shootRT = new Animation();
		left  = new Animation();
		right = new Animation();
		up 	  = new Animation();
		down  = new Animation();
		
		// Set Up Animations
		idle.AddFrame(Animation.ReadImage("Fairy/Fairy-ID"), 100);
		
		// Shooting
		shoot.AddFrame(Animation.ReadImage("Fairy/Fairy-DN"), (firerate/3));
		shoot.AddFrame(Animation.ReadImage("Fairy/Fairy-SH"), 100);
		
		shootLT.AddFrame(Animation.ReadImage("Fairy/Fairy-LT-SH1"), (firerate/3));
		shootLT.AddFrame(Animation.ReadImage("Fairy/Fairy-LT-SH2"), 100);
		
		shootRT.AddFrame(Animation.ReadImage("Fairy/Fairy-RT-SH1"), (firerate/3));
		shootRT.AddFrame(Animation.ReadImage("Fairy/Fairy-RT-SH2"), 100);
		
		// Movement Directions
		left. AddFrame(Animation.ReadImage("Fairy/Fairy-LT"), 100);
		right.AddFrame(Animation.ReadImage("Fairy/Fairy-RT"), 100);
		up.   AddFrame(Animation.ReadImage("Fairy/Fairy-DN"), 100);
		down. AddFrame(Animation.ReadImage("Fairy/Fairy-UP"), 100);
		
		float scaleby = 1.5f;
		idle.ScaleAnimation(scaleby);
		shoot.ScaleAnimation(scaleby);
		shootLT.ScaleAnimation(scaleby);
		shootRT.ScaleAnimation(scaleby);
		left.ScaleAnimation(scaleby);
		right.ScaleAnimation(scaleby);
		up.ScaleAnimation(scaleby);
		down.ScaleAnimation(scaleby);
		
		// Make it idle by default
		anim = idle;
	}
	
	public void Update(float deltaTime) 
	{ 
		super.Update(deltaTime);
	}
	
	public void UpdateAnimation() 
	{
		Animation newAnim = anim;
		
		if (GetDirection() == Direction.NA)
			newAnim = idle;
		
		if (GetDirection() == Direction.RT)
			newAnim = right;
		
		if (GetDirection() == Direction.LT)
			newAnim = left;
		
		if (GetDirection() == Direction.DN)
			newAnim = down;
		
		if (GetDirection() == Direction.UP)
			newAnim = right;
			
		if ( shotTimer >= ( firerate - ( firerate * 0.75f ) ) ) 
		{
			if (GetDirection() == Direction.RT)
				newAnim = shootRT;
			else 
			if (GetDirection() == Direction.LT)
				newAnim = shootLT;
			else
			if (GetDirection() != Direction.LT && GetDirection() != Direction.RT)
				newAnim = shoot;
		}
		
		// Update Player Animations
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
	
	public void Draw(Graphics pen) 
	{
		pen.setColor(Color.PINK);
		pen.fillRect((int) GetX(), (int) GetY(), GetW(), GetH());
		gun.Draw(pen);
		super.Draw(pen);
	}
	
	public Object Clone(int x, int y) 
	{
		return new Fairy(x, y, width, height, health);
	}


	@Override
	public void ShootGun() 
	{
		// gun.FireCircleA2( 10, MathF.FindAngle(pos, FindPlayer()), 10,  0, 0.003f, 2, 0);
	}
	
}
