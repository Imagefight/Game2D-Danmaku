import java.awt.geom.Point2D.Float;

public abstract class Boss extends Enemy
{

	public Boss(Float pos, int width, int height, int health) 
	{
		super(pos, width, height, health);
	}

	public void Initialize() 
	{
		
	}
	
	public void Update()
	{
		
	}
	
	@Override
	public void UpdateAnimation() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void ShootGun() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public Object Clone(int x, int y) 
	{
		// TODO Auto-generated method stub
		return null;
	}


}
