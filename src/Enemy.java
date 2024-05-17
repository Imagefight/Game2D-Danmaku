import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public abstract class Enemy extends Creature
{
	// Generic Enemy Class for easy enemy implementation
	
	protected Float spawnpoint;
	
	// When the enemy fires
	protected float firerate;
	protected float shotTimer = 0;

	// Movement values
	// To be debated
	private ArrayList<Line> rails = new ArrayList<>();
	private ArrayList<java.lang.Float> deadlines = new ArrayList<>();
	private int railIndex = 0;
    private float moveTimer = 0.0f;
    private Direction dir;
    
	// Shooting Strategies
	private ShotStrategy ShootBehavior;
	
	/*
	private boolean isMoving = false;
	private Point2D.Float targetPosition;
	private int framesToDestination ,
				startAngle, endAngle;
	private float startMagnitude, endMagnitude;
	private float moveTimer = 0.0f;
	*/
	
	// TODO: Look at these later: 
	// https://touhou.fandom.com/wiki/Touhou_Danmakufu:_Simple_Script 
	// https://en.touhouwiki.net/wiki/Touhou_Danmakufu/Functions/Motion

	
	public Enemy(int x, int y, int width, int height, int health) 
	{
		super(x, y, width, height);
		this.health = health;
		this.spawnpoint = new Float (x, y);
	}
	
	public Enemy(Point2D.Float pos, int width, int height, int health) 
	{
		super( (int) pos.x, (int) pos.y, width, height);
		this.health = health;
		this.spawnpoint = pos;	
	}
	
	public enum Direction { UP, DN, LT, RT, NA }
	
	public void Initialize() 
	{
		super.Initialize();
		// defaults
		firerate  = 60;
		// Do nothing by default
		ShootBehavior = new ShotStrategy.FireNothing();
	}
	
	// This is ran 60 times per second
	public void Update(float deltaTime) 
	{
		super.Update(deltaTime);
		// Attacking
		ShootAtRate(firerate);
		/*
		System.out.println
		("MoveTimer: " + moveTimer +
		 ", MoveLines: " + moveLines + 
		 ", Deadlines: " + deadlines );
		 */
	}
	
	protected Float FindPlayer() { return GameManager.player.pos; }
	
	protected void ShootAtRate( float firerate )
	{
		shotTimer += Game2D.Delta();
		if (shotTimer >= firerate)
		{
			ShootBehavior.Fire(gun);
			shotTimer = 0;
		}
	}
	
	public void SetFirerate(float rate) { firerate = rate; }
	
	public Direction GetDirection() { return dir; }

    public void Move() 
    {
        if (!rails.isEmpty()) 
        {
            // Update the move timer
            moveTimer += Game2D.Delta();

            // Get the current rail
            Line currentRail = rails.get(railIndex);
            float deadline = deadlines.get(railIndex);

            // Calculate the progress based on the move timer and the deadline
            float progress = Math.min(moveTimer / deadline, 1.0f);

            // Calculate the eased progress using a chosen easing function
            float easedProgress = MathF.Ease.EaseInOutQuint(progress); // You can change the easing function as needed

            // Get the point on the line based on the eased progress
            Point2D.Float targetPoint = currentRail.CardinalInterpolatePoint(easedProgress);

            // Determine dominant movement direction
            Direction newDirection = SetDirection(new Point2D.Float(pos.x, pos.y), targetPoint);

            // Update current direction
            dir = newDirection;

            // Move the enemy directly to the target point
            SetX(targetPoint.x);
            SetY(targetPoint.y);

            // Check if the enemy has reached the end of the line
            if (progress >= 1.0f) 
            {
                // Move to the next rail if available
                if (railIndex < rails.size() - 1) 
                {
                    railIndex++;
                    moveTimer = 0.0f; // Reset the move timer for the next rail
                } 
                
                else 
                {
                    // Reset to the first rail if there are no more rails
                    railIndex = 0;
                    moveTimer = 0.0f; // Reset the move timer for the next cycle
                }
            }
            
        }
    }


	private Direction SetDirection(Point2D.Float currentPosition, Point2D.Float targetPoint) 
	{
		float deltaX = targetPoint.x - currentPosition.x;
	    float deltaY = targetPoint.y - currentPosition.y;

	    // Calculate the absolute values of deltaX and deltaY
	    float absDeltaX = Math.abs(deltaX);
	    float absDeltaY = Math.abs(deltaY);

	    if (absDeltaX > absDeltaY) 
	    {
	        // If horizontal movement has a bigger magnitude or equal, return left or right
	        return deltaX > 0 ? Direction.RT : deltaX < 0.25 ? Direction.LT : Direction.NA;
	    } 
	    
	    else 
	    {
	        // If vertical movement has a bigger magnitude, return up or down
	        return deltaY > 0 ? Direction.DN : deltaY < 0.25 ? Direction.UP : Direction.NA;
	    }
	    
	}

	// Changing the firing strategy
	public void SetStrategy( ShotStrategy newStrategy ) { ShootBehavior = newStrategy; }
	public ShotStrategy GetStrategy() { return ShootBehavior; }
	
	// Making a line from points
	public void NewPath( float tension, int segments, float deadline, Point2D.Float... points ) 
	{
		if (rails.isEmpty()) 
		{
			Line newLine = new Line();
			
			newLine.SetTension (tension);
			newLine.SetSegments(segments);
			
			newLine.PlotPoint(spawnpoint);
			newLine.PlotPoints(points);
			newLine.CardinalInterpolateLine();
			
			rails.add(newLine);
		}
		
		else
		{
			Line newLine = new Line();
			
			newLine.SetTension (tension);
			newLine.SetSegments(segments);
			Point2D.Float startarea = rails.get( rails.size() - 1 ).GetLastPoint();
			
			newLine.PlotPoint(startarea);
			newLine.PlotPoints(points);
			newLine.CardinalInterpolateLine();
			
			rails.add(newLine);
		}
		
		deadlines.add(deadline);
	}
	
	public void Draw(Graphics pen) 
	{
		//Graphics2D pencil = (Graphics2D) pen;
		super.Draw(pen);
		rails.forEach(line -> line.Draw( (Graphics2D) pen));
		gun.Draw(pen);
	}
	
	@Override
	public abstract Object Clone(int x, int y);
	
}
