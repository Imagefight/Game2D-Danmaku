import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public class Player extends Creature 
{
	
	// Combat Commands
	public boolean CMD_SH, CMD_FC;
	public int damage 	 = 3;
	public int bulletAmt = 200;
	
	public final float focusSpeed = maxSpeed / 2;
	
	private Animation idle, left, right;
	
	public Player(int x, int y, int width, int height) 
	{
		super(x, y, width, height);		
		gun = new BulletSpawner(x, y, 32, 32, -90, 1);
		gun.aimPlayer = false;
		maxSpeed = 12;
	}
	
	// Define Animations
	public void Initialize() 
	{
		idle  = new Animation();
		left  = new Animation();
		right = new Animation();
		
		idle.AddFrame (Animation.ReadImage("Player/Player_ID") , 100);
		left.AddFrame (Animation.ReadImage("Player/Player_LT") , 100);
		right.AddFrame(Animation.ReadImage("Player/Player_RT") , 100);
		
		float scaleby = 1.5f;
		idle.ScaleAnimation(scaleby);
		left.ScaleAnimation(scaleby);
		right.ScaleAnimation(scaleby);
		
		anim = idle;
	}
	
	// Animation & State Updating
	public void Update(float deltaTime) 
	{
		Move();
		super.Update(deltaTime);
		UpdateAnimation();
		ShootGun();
	}
	
	public void UpdateAnimation() 
	{
		Animation newAnim = anim;
		
		if (MOV_LT && !MOV_RT)
			newAnim = left;
		else
		if (MOV_RT && !MOV_LT)
			newAnim = right;
		else
		if ( (MOV_LT && MOV_RT) || !(MOV_LT || MOV_RT) )
			newAnim = idle;
		
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
	
	public void Move() 
	{
		if (MOV_RT) SetVelX(  MathF.Lerp(GetVelX(),  (CMD_FC ? focusSpeed : maxSpeed), acceleration ) );
		if (MOV_LT) SetVelX(  MathF.Lerp(GetVelX(), -(CMD_FC ? focusSpeed : maxSpeed), acceleration ) );
		
		if (MOV_DN) SetVelY(  MathF.Lerp(GetVelY(),  (CMD_FC ? focusSpeed : maxSpeed), acceleration ) );
		if (MOV_UP) SetVelY(  MathF.Lerp(GetVelY(), -(CMD_FC ? focusSpeed : maxSpeed), acceleration ) );
	}
	
	public void ShootGun()
	{
		// if (CMD_SH) gun.FireA1(-90, 12, 0);
		
		if (CMD_SH) gun.FireA2(-90, 0, 0.15f, 12, 0);
		
		// if (CMD_SH) gun.FireSpreadA1(5, -90, 20, 12, 0);
		
		// if (CMD_SH) gun.FireSpreadA2(5, -90, 20, 0, 0.25f, 12, 0);
		
		// if (CMD_SH) gun.FireCircleA1( 3, 20, 12, 0);
				
		// if (CMD_SH) gun.FireEllipseA2( 30, 12, 330, 180, 20, 0, 0.25f, 12, 0);
		
		// if (CMD_SH) gun.FireCircleA2( 20, 45, 10, 0, 0.25f, 12, 0);
		
		// if (CMD_SH) gun.FireStackA2( 20, -90, 10, 0, 0.25f, 12, 0);
		
	}
		
	public void Draw(Graphics pen) 
	{
		if (CMD_FC)
		{
			pen.setColor(Color.RED);
			pen.fillRect((int) GetX(), (int) GetY(), GetW(), GetH());
			super.Draw(pen);
		}
		
		gun.Draw(pen);
	}

	@Override
	public Object Clone(int x, int y) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}
