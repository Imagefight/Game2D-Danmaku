import java.awt.geom.Point2D;

public interface ShotStrategy 
{
	public void Fire(BulletSpawner gun);
	
	public class FireA1 implements ShotStrategy
	{
		int angle; 
		float speed; 
		float delay;
				
		public void SetSpeed(float speed) 	{ this.speed = speed;}
		
		public void SetAngle(int angle) 	{ this.angle = angle;}
		public void SetDelay(float delay) 	{ this.delay = delay; }
		
		public FireA1(int angle, float speed, float delay) 
		{
			SetAngle(angle);
			SetSpeed(speed);
			SetDelay(delay);
		}
		
		public void Fire(BulletSpawner gun) 
		{
			gun.FireA1(angle, speed, delay);
		}
	}
	
	public class FireA2 implements ShotStrategy
	{
		int angle; 
		float speed, delay, maxSpeed, acceleration;
		
		public void SetSpeed(float speed) 	{ this.speed = speed;}
		public void SetMaxSpeed(float maxSpeed) {this.maxSpeed = maxSpeed;}
		public void SetAcceleration( float acceleration ) { this.acceleration = acceleration; }
		
		public void SetAngle(int angle) 	{ this.angle = angle;}
		public void SetDelay(float delay) 	{ this.delay = delay; }
		
		public FireA2(int angle, float speed, float acceleration, float maxSpeed, float delay) 
		{

			SetAngle(angle);
			SetSpeed(speed);
			SetAcceleration(acceleration);
			SetMaxSpeed(maxSpeed);
			SetDelay(delay);
		}
		
		public void Fire(BulletSpawner gun) 
		{
			gun.FireA2(angle, speed, acceleration, maxSpeed, delay);
		}
		
	}
	
	public class FireEllipseA1 implements ShotStrategy
	{
		int angle, radius, amt, apexX, apexY; 
		float speed, delay;
				
		public void SetApexX (int apexX) {this.apexX = apexX;} 
		public void SetApexY (int apexY) {this.apexY = apexY;} 
		public void SetRadius(int radius) {this.radius = radius;}
		
		public void SetAmount(int amt) {this.amt = amt;} 
		
		public void SetSpeed(float speed) 	{ this.speed = speed;}
		
		public void SetAngle(int angle) 	{ this.angle = angle;}
		public void SetDelay(float delay) 	{ this.delay = delay; }
		
		public FireEllipseA1( int amt, int angle, int apexX, int apexY, int radius, float speed, float delay) 
		{
			SetAmount(amt);
			SetAngle(angle);
			SetApexX(apexX);
			SetApexY(apexY);
			SetRadius(radius);
			SetSpeed(speed);
			SetDelay(delay);
		}
		
		public void Fire(BulletSpawner gun) 
		{
			gun.FireEllipseA1(amt, angle, apexX, apexY, radius, speed, delay);
		}
	}
	
	public class FireEllipseA2 implements ShotStrategy
	{
		int angle, radius, amt, apexX, apexY; 
		float speed, delay, maxSpeed, acceleration;
				
		public void SetApexX (int apexX) {this.apexX = apexX;} 
		public void SetApexY (int apexY) {this.apexY = apexY;} 
		public void SetRadius(int radius) {this.radius = radius;}
		
		public void SetAmount(int amt) {this.amt = amt;} 
		
		public void SetSpeed(float speed) 	{ this.speed = speed;}
		public void SetMaxSpeed(float maxSpeed) {this.maxSpeed = maxSpeed;}
		public void SetAcceleration( float acceleration ) { this.acceleration = acceleration; }
		
		public void SetAngle(int angle) 	{ this.angle = angle;}
		public void SetDelay(float delay) 	{ this.delay = delay; }
		
		public FireEllipseA2(int amt, int angle, int apexX, int apexY, int radius, float speed, float acceleration, float maxSpeed, float delay) 
		{
			SetAmount(amt);
			SetAngle(angle);
			SetApexX(apexX);
			SetApexY(apexY);
			SetRadius(radius);
			SetSpeed(speed);
			SetAcceleration(acceleration);
			SetMaxSpeed(maxSpeed);
			SetDelay(delay);
		}
		
		public void Fire(BulletSpawner gun) 
		{
			gun.FireEllipseA2(amt, angle, apexX, apexY, radius, speed, acceleration, maxSpeed, delay);
		}
	}
	
	public class FireCircleA1 implements ShotStrategy
	{
		int angle, radius, amt; 
		float speed, delay; 
		
		public void SetRadius(int radius) {this.radius = radius;}
		
		public void SetAmount(int amt) {this.amt = amt;} 
		
		public void SetSpeed(float speed) 	{ this.speed = speed;}
		
		public void SetAngle(int angle) 	{ this.angle = angle;}
		public void SetDelay(float delay) 	{ this.delay = delay; }
		
		public FireCircleA1(int amt, int angle, int radius, float speed, float delay) 
		{
			SetAngle(angle);
			SetAmount(amt);
			SetRadius(radius);
			SetSpeed(speed);
			SetDelay(delay);
		}
		
		public void Fire(BulletSpawner gun) 
		{
			gun.FireCircleA1(amt, angle, radius, speed, delay);
		}
	}
	
	public class FireCircleA2 implements ShotStrategy
	{
		int angle, radius, amt, apexX, apexY; 
		float speed, delay, maxSpeed, acceleration;
				
		public void SetRadius(int radius) {this.radius = radius;}
		
		public void SetAmount(int amt) {this.amt = amt;} 
		
		public void SetSpeed(float speed) 	{ this.speed = speed;}
		public void SetMaxSpeed(float maxSpeed) {this.maxSpeed = maxSpeed;}
		public void SetAcceleration( float acceleration ) { this.acceleration = acceleration; }
		
		public void SetAngle(int angle) 	{ this.angle = angle;}
		public void SetDelay(float delay) 	{ this.delay = delay; }
		
		public FireCircleA2(int amt, int angle, int radius, float speed, float acceleration, float maxSpeed, float delay) 
		{
			SetAmount(amt);
			SetAngle(angle);
			SetRadius(radius);
			SetSpeed(speed);
			SetAcceleration(acceleration);
			SetMaxSpeed(maxSpeed);
			SetDelay(delay);
		}
		
		public void Fire(BulletSpawner gun) 
		{
			gun.FireCircleA2(amt, angle, radius, speed, acceleration, maxSpeed, delay);
		}
	}
	
	public class FireSpreadA1 implements ShotStrategy
	{
		int angle, amt; 
		float speed, spacing, delay;
				
		public void SetSpacing(float spacing) {this.spacing = spacing;}
		public void SetAmount(int amt) 		{this.amt = amt;} 
		
		public void SetSpeed(float speed) 	{ this.speed = speed;}
		
		public void SetAngle(int angle) 	{ this.angle = angle;}
		public void SetDelay(float delay) 	{ this.delay = delay; }
		
		public FireSpreadA1(int amt, int angle, float spacing, float speed, float acceleration, float maxSpeed, float delay) 
		{
			SetAngle(angle);
			SetAmount(amt);
			SetSpacing(spacing);
			SetSpeed(speed);
			SetDelay(delay);
		}
		
		public void Fire(BulletSpawner gun) 
		{
			gun.FireSpreadA1(amt, angle, spacing, speed, delay);
		}
	}
	
	public class FireSpreadA2 implements ShotStrategy
	{
		int angle, amt; 
		float speed, delay, spacing, maxSpeed, acceleration;

		public void SetSpacing(float spacing) {this.spacing = spacing;}
		public void SetAmount(int amt) 		  {this.amt = amt;} 
		
		public void SetSpeed(float speed) 	{ this.speed = speed;}
		public void SetMaxSpeed(float maxSpeed) {this.maxSpeed = maxSpeed;}
		public void SetAcceleration( float acceleration ) { this.acceleration = acceleration; }
		
		public void SetAngle(int angle) 	{ this.angle = angle;}
		public void SetDelay(float delay) 	{ this.delay = delay; }
		
		public FireSpreadA2(int amt, int angle, float spacing, float speed, float acceleration, float maxSpeed, float delay) 
		{
			SetAmount(amt);
			SetAngle(angle);
			SetSpacing(spacing);
			SetSpeed(speed);
			SetAcceleration(acceleration);
			SetMaxSpeed(maxSpeed);
			SetDelay(delay);
		}
		
		public void Fire(BulletSpawner gun) 
		{
			gun.FireSpreadA2(amt, angle, spacing, speed, acceleration, maxSpeed, delay);
		}
	}
	
	public class FireStackA2 implements ShotStrategy
	{
		int angle, amt; 
		float speed, delay, speedOffset, maxSpeed, acceleration;
		
		public void SetSpeedOffset(float speedOffset) {this.speedOffset = speedOffset;}
		public void SetAmount(int amt) 		{this.amt = amt;} 
		
		public void SetSpeed(float speed) 	{ this.speed = speed;}
		public void SetMaxSpeed(float maxSpeed) {this.maxSpeed = maxSpeed;}
		public void SetAcceleration( float acceleration ) { this.acceleration = acceleration; }
		
		public void SetAngle(int angle) 	{ this.angle = angle;}
		public void SetDelay(float delay) 	{ this.delay = delay; }
		
		public FireStackA2(int amt, int angle, float speedOffset, float speed, float acceleration, float maxSpeed, float delay) 
		{
			SetAmount(amt);
			SetAngle(angle);
			SetSpeedOffset(speedOffset);
			SetSpeed(speed);
			SetAcceleration(acceleration);
			SetMaxSpeed(maxSpeed);
			SetDelay(delay);
		}
		
		public void Fire(BulletSpawner gun) 
		{
			gun.FireStackA2(amt, angle, speedOffset, speed, acceleration, maxSpeed, delay);
		}
	}
	
	public class FireNothing implements ShotStrategy
	{
		// Do nothing, obviously.
		public void Fire(BulletSpawner gun) {}
	}
}
