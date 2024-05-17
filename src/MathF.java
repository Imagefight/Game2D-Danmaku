// Assorted things i need the game to do without knowing where to put them
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;

public class MathF 
{
	public static double[] cos = GenerateCos();
	public static double[] sin = GenerateSin();
	
	// Taken from the unity engine https://discussions.unity.com/t/any-one-know-maths-behind-this-movetowards-function/65501/4
	public static float MoveTowards(float current, float target, float maxDelta) 
	{
		if (Math.abs(target - current) <= maxDelta) {return target;}
		return current + Math.signum(target-current) * maxDelta;
	}
	
	// Linear Interprolation
	public static float Lerp(float from, float to, float magnitude) 
	{
		return ((from * (1.0f - magnitude)) + (to * magnitude));
	}
	
	// Linear Interprolation along two Floating Points
	public static Float Lerp(Float from, Float to, float magnitude) 
	{	
		float x = Lerp(from.x, to.x, magnitude);
		float y = Lerp(from.y, to.y, magnitude);
		Float newPoint = new Float(x, y);
		
		return newPoint;
	}
	
	// Pre-computing cos and sin values to reduce load on computer
	private static double[] GenerateCos() 
	{
		double[] cos = new double[360];
		
		for (int A = 0; A < 360; A++)
		{
			cos[A] = Math.cos(A * Math.PI / 180);
		}
		
		return cos;
	}
	
	private static double[] GenerateSin() 
	{
		double[] sin = new double[360];
		
		for (int A = 0; A < 360; A++)
		{
			sin[A] = Math.sin(A * Math.PI / 180);
		}
		
		return sin;
	}
	
	public static double GetCos(int angle) 
	{	
		if (angle < 0) 	return cos[ValidAngle(angle)];
		else 			return cos[ValidAngle(angle)];
	}
	
	public static double GetSin(int angle) 
	{
		if (angle < 0) 	return sin[ValidAngle(angle)];
		else 			return sin[ValidAngle(angle)];
	}
	
	public static int ValidAngle(int angle) 
	{
		if (angle < 0) 	return (angle % 360) + 360;
		else 			return  angle % 360;
	}
	
	public static int RandomRange(int min, int max)
	{
		return ( (int) (Math.random() * (max - min)) + min );
	}

	public static int FindAngle(float x1, float y1, float x2, float y2)
	{
		return (int) Math.toDegrees(Math.atan2( y2 - y1, x2 - x1));
	}
	
	public static int FindAngle (Float pos1, Float pos2)
	{
		return FindAngle(pos1.x, pos1.y, pos2.x, pos2.y);
	}

	
	public static Point2D.Float CubicBezierLerp(Point2D.Float start, Point2D.Float end, float t, float startMagnitude, int startAngle, float endMagnitude, int endAngle) 
	{
	    // Interpolate position along the cubic Bezier curve
	    float x = CubicBezier(start.x, end.x, t, (float) (startMagnitude * MathF.GetCos(startAngle)), (float) (endMagnitude * MathF.GetCos(endAngle)));
	    float y = CubicBezier(start.y, end.y, t, (float) (startMagnitude * MathF.GetSin(startAngle)), (float) (endMagnitude * MathF.GetSin(endAngle)));
	    return new Point2D.Float(x, y);
	}

	public static float CubicBezier(float p0, float p3, float t, float m0, float m3) {
	    // Cubic Bezier interpolation
	    float t2 = t * t;
	    float t3 = t2 * t;
	    float mt = 1 - t;
	    float mt2 = mt * mt;
	    float mt3 = mt2 * mt;
	    return mt3 * p0 + 3 * mt2 * t * (p0 + m0) + 3 * mt * t2 * (p3 - m3) + t3 * p3;
	}
	
    // TODO: Read Up
    // https://easings.net/ <- What these graphs look like
    // https://www.febucci.com/2018/08/easing-functions/
    // Easing functions specify the rate of change of a parameter over time, allows for the more organic movement of anything
    public static class Ease 
    {
    	// Allows for Ease-In's to become Ease-Out's
    	public static float Flip(float x)
    	{
    		return 1 - x;
    	}
    	
    	// Progress is from 0 - 1 (Percentage)
    	public static float EaseInSine ( float progress ) 
    	{
    		return (float) (1 - GetCos( (int) ( (progress * Math.PI) / 2 ) ));
    	}
    	
    	public static float EaseCubic ( float progress )
    	{
    		return progress * progress * progress;
    	}
    	
    	public static float EaseQuint ( float progress )
    	{
    		return progress * progress * progress * progress;
    	}
    	
    	public static float EaseInOutQuint( float progress ) 
    	{
    		return (float) (progress < 0.5 ? 16 * progress * progress * progress * progress * progress : 1 - Math.pow(-2 * progress + 2, 5) / 2);
    	}
    	
    	public static float EaseCirc ( float progress )
    	{
    		return (float) ( 1 - Math.sqrt( 1 - (progress * progress) ) );
    	}
    	
    	// Might crash the program
    	public static float EaseElastic ( float progress )
    	{
    		float c4 = (float) (2 * Math.PI)/3;
    		
    		return  (float) 
    				( progress == 0 ? 0
    				:progress == 1 ? 1
    				: -(Math.pow( 2, 10 * progress -10) * GetSin( (int) ((progress * 10 - 10.75) * c4) ))
    				);
    	}
    }

    
}