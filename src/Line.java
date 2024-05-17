import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;

// Class for Making and Modifying Lines.
// To be used as a pathfinding feature for creatures, or more specifically, enemies.
public class Line 
{
    private float tension = 0;
    private int  segments = 1;
    
    private ArrayList<Point2D.Float> points;
    private Point2D.Float[] interpolatedPoints;
	
    public Line() 
    {
        points = new ArrayList<>();
    }

    public Line(Point2D.Float[] points, float tension, int segments) 
    {
        this.points = new ArrayList<>();
        this.tension = tension;
        this.segments = segments;
        
        PlotPoints(points);
        CardinalInterpolateLine(); // Interpolate the line
    }

    public Line(ArrayList<Point2D.Float> points, float tension, int segments) {
        this.points = new ArrayList<>();
        this.tension = tension;
        this.segments = segments;
        
        PlotPoints(points);
        CardinalInterpolateLine(); // Interpolate the line
    }
	
	// Adding Points to a line
	// These are accessable by the user
	public void PlotPoint (Point2D.Float point) 
	{ 
		points.add(point); 
		
		if (points.size() >= 2)
		{
			CardinalInterpolateLine();
		}
	}
	
	public void PlotPoints(ArrayList<Point2D.Float> newPoints) 
	{ 
		points.addAll(newPoints); 
		if (points.size() >= 2)
		{
			CardinalInterpolateLine();
		}
	}
	
	public void PlotPoints(Line newLine) 
	{ 
		points.addAll(newLine.GetPoints());
		if (points.size() >= 2)
		{
			CardinalInterpolateLine();
		}
	}

	public void PlotPoints(Point2D.Float[] newPoints) 
	{
		for (Point2D.Float point : newPoints)
		{
			PlotPoint(point);
		}
	}
	
	// Get Points in the Line
	public ArrayList<Point2D.Float>   GetPoints() 	 { return points; }
	
	// Clear All Points in a Line
	public void ClearPoints() 
	{ 
		points.clear(); 
	}
	
	// Replace all Points in a line
	public void ReplaceAll ( ArrayList<Point2D.Float> newPoints ) 
	{
		ClearPoints();
		PlotPoints(newPoints);
	}
	
	public void ReplaceAll ( Line newLine ) 
	{
		ClearPoints();
		PlotPoints ( newLine.GetPoints() );
		SetTension ( newLine.GetTension() );
		SetSegments( newLine.GetSegments() );
	}
	
	public void ReplaceAll ( Point2D.Float[] newPoints ) 
	{
		ClearPoints();
		PlotPoints(newPoints);
	}
	
	public float GetTension() { return tension; }
	public int  GetSegments() { return segments; }
	
	public void SetTension  ( float tension ) { this.tension = tension; }
	public void SetSegments ( int  segments ) { this.segments = segments; }
	
	// Line Functions
	public static Float CubicBezier(Float from, Float control1, Float control2, Float to, float magnitude) 
	{
		Float a = MathF.Lerp(from		, control1, magnitude);
		Float b = MathF.Lerp(control1	, control2, magnitude);
		Float c = MathF.Lerp(control2	, to	  , magnitude);
		Float d = MathF.Lerp(a, b, magnitude);
		Float e = MathF.Lerp(b, c, magnitude);
		
		return MathF.Lerp(d, e, magnitude);
	}
	
	// FIND A WAY TO DO THIS NOT 60 FPS, THIS IS EXPENSIVE!
	public void CardinalInterpolateLine() 
	{
	    if (points.size() < 2) 
	    {
	        throw new IllegalArgumentException("At least two points are required for interpolation.");
	    }

	    int numInterpolatedPoints = (points.size() - 1) * segments + 1;
	    interpolatedPoints = new Point2D.Float[numInterpolatedPoints];

	    for (int i = 0; i < points.size() - 1; i++) {
	        Point2D.Float p0 = i == 0 ? points.get(0) : points.get(i - 1);
	        Point2D.Float p1 = points.get(i);
	        Point2D.Float p2 = points.get(i + 1);
	        Point2D.Float p3 = (i == points.size() - 2) ? points.get(i + 1) : points.get(i + 2);

	        for (int j = 0; j < segments; j++) {
	            float t = (float) j / segments;
	            float t2 = t * t;
	            float t3 = t2 * t;

	            // Catmull-Rom spline formula
	            float tx = 0.5f * ((2 * p1.x) + (-p0.x + p2.x) * t + (2 * p0.x - 5 * p1.x + 4 * p2.x - p3.x) * t2 + (-p0.x + 3 * p1.x - 3 * p2.x + p3.x) * t3);
	            float ty = 0.5f * ((2 * p1.y) + (-p0.y + p2.y) * t + (2 * p0.y - 5 * p1.y + 4 * p2.y - p3.y) * t2 + (-p0.y + 3 * p1.y - 3 * p2.y + p3.y) * t3);

	            interpolatedPoints[i * segments + j] = new Point2D.Float(tx, ty);
	        }
	    }
	    
	    interpolatedPoints[numInterpolatedPoints - 1] = points.get(points.size() - 1); 
	}

	// percentage = 0-1f where along the line you want to be
	public Point2D.Float CardinalInterpolatePoint(float percentage) 
	{
	    if (interpolatedPoints == null || interpolatedPoints.length == 0) 
	    {
	        throw new IllegalStateException("Interpolated points not calculated. Call CardinalInterpolateLine first.");
	    }

	    float totalLength = CalculateTotalLength(interpolatedPoints);
	    float targetLength = totalLength * percentage;

	    float accumulatedLength = 0;
	    int segmentIndex = 0;

	    for (int i = 0; i < interpolatedPoints.length - 1; i++) 
	    {
	        float segmentLength = CalculateSegmentLength(interpolatedPoints[i], interpolatedPoints[i + 1]);
	        accumulatedLength += segmentLength;
	        
	        if (accumulatedLength >= targetLength) {
	            segmentIndex = i;
	            break;
	        }
	    }

	    // Calculate the ratio of the target length within the segment
	    float ratio = (targetLength - (accumulatedLength - CalculateSegmentLength(interpolatedPoints[segmentIndex], interpolatedPoints[segmentIndex + 1]))) / CalculateSegmentLength(interpolatedPoints[segmentIndex], interpolatedPoints[segmentIndex + 1]);

	    // Interpolate the point within the segment based on the ratio
	    float interpolatedX = interpolatedPoints[segmentIndex].x + ratio * (interpolatedPoints[segmentIndex + 1].x - interpolatedPoints[segmentIndex].x);
	    float interpolatedY = interpolatedPoints[segmentIndex].y + ratio * (interpolatedPoints[segmentIndex + 1].y - interpolatedPoints[segmentIndex].y);

	    return new Point2D.Float(interpolatedX, interpolatedY);
	}

    // Calculate total length of the interpolated line
	private float CalculateTotalLength(Point2D.Float[] interpolatedPoints) 
	{
	    float totalLength = 0;
	    for (int i = 0; i < interpolatedPoints.length - 1; i++) {
	        totalLength += CalculateSegmentLength(interpolatedPoints[i], interpolatedPoints[i + 1]);
	    }
	    return totalLength;
	}
  
    private float CalculateTotalLength(ArrayList<Point2D.Float> interpolatedPoints) 
    {
        return CalculateTotalLength(interpolatedPoints.toArray(new Point2D.Float[interpolatedPoints.size()]));
    }

    // Calculate length of a segment between two points
    private float CalculateSegmentLength(Point2D.Float point1, Point2D.Float point2) 
    {
        double dx = point2.x - point1.x;
        double dy = point2.y - point1.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public Line CutLine(float cutPercentage) {
        if (interpolatedPoints == null || interpolatedPoints.length == 0) {
            throw new IllegalStateException("Interpolated points not calculated. Call CardinalInterpolateLine first.");
        }

        // Check if cutPercentage is within valid range
        if (cutPercentage < 0 || cutPercentage > 1) {
            throw new IllegalArgumentException("Cut percentage must be between 0 and 1 (inclusive).");
        }

        // Calculate the index of the segment where the cut will occur
        int numSegments = CountSegments();
        if (numSegments == 0) {
            throw new IllegalStateException("Line has no segments to cut.");
        }

        // Calculate the length of the line
        float totalLength = CalculateTotalLength(interpolatedPoints);

        // Calculate the target length where the cut will occur
        float targetLength = totalLength * cutPercentage;

        // Iterate through segments to find the segment where the cut will occur
        int cutSegmentIndex = 0;
        float accumulatedLength = 0;
        for (int i = 0; i < interpolatedPoints.length - 1; i++) {
            float segmentLength = CalculateSegmentLength(interpolatedPoints[i], interpolatedPoints[i + 1]);
            accumulatedLength += segmentLength;
            if (accumulatedLength >= targetLength) {
                cutSegmentIndex = i;
                break;
            }
        }

        // Calculate the ratio of the target length within the segment
        float ratio = (accumulatedLength - targetLength) / CalculateSegmentLength(interpolatedPoints[cutSegmentIndex], interpolatedPoints[cutSegmentIndex + 1]);

        // Interpolate the cut point within the segment
        Point2D.Float cutPoint = new Point2D.Float(
                interpolatedPoints[cutSegmentIndex].x + ratio * (interpolatedPoints[cutSegmentIndex + 1].x - interpolatedPoints[cutSegmentIndex].x),
                interpolatedPoints[cutSegmentIndex].y + ratio * (interpolatedPoints[cutSegmentIndex + 1].y - interpolatedPoints[cutSegmentIndex].y));

        // Create two new lines by splitting the original line at the cut point
        Line line1 = new Line(new ArrayList<>(points.subList(0, cutSegmentIndex + 1)), tension, segments);
        Line line2 = new Line(new ArrayList<>(points.subList(cutSegmentIndex + 1, points.size())), tension, segments);

        // Plot the cut point on both lines
        line1.PlotPoint(cutPoint);
        line2.PlotPoint(cutPoint);

        // Overwrite the current line with line1
        this.ReplaceAll(line1);

        // Return line2
        return line2;
    }



    // Option 2: Find the index of the line segment where the cut will occur
    private int findCutSegmentIndex(float cutPercentage) 
    {
        float accumulatedLength = 0;
        float totalLength = CalculateTotalLength(interpolatedPoints);
        float targetLength = totalLength * cutPercentage;

        for (int i = 0; i < interpolatedPoints.length - 1; i++) {
            float segmentLength = CalculateSegmentLength(interpolatedPoints[i], interpolatedPoints[i + 1]);
            accumulatedLength += segmentLength;

            if (accumulatedLength >= targetLength) {
                return i;
            }
        }

        // If for some reason the cut segment index is not found, return a default value
        return interpolatedPoints.length - 2;
    }

    
    public int CountSegments() 
    {
        // If there are no points or only one point, there are no segments
        if (points.size() < 2) {
            return 0;
        }

        // The number of segments is equal to the number of points minus 1
        return points.size() - 1;
    }

    public Point2D.Float GetLastPoint() { return points.get( points.size() - 1 ); }

    public float GetLength() 
    {
        float length = 0;
        for (int i = 0; i < points.size() - 1; i++) 
        {
            Point2D.Float p1 = points.get(i);
            Point2D.Float p2 = points.get(i + 1);
            
            length += (float) p1.distance(p2);
        }
        return length;
    }
    
    public void Draw(Graphics2D pen) 
    {
        if (interpolatedPoints == null || interpolatedPoints.length < 2) {
            return; // No line to draw
        }

        pen.setColor(Color.RED);

        for (int i = 0; i < interpolatedPoints.length - 1; i++) {
            Point2D.Float p1 = interpolatedPoints[i];
            Point2D.Float p2 = interpolatedPoints[i + 1];
            pen.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
        }

        // Draw points
        pen.setColor(Color.BLUE);
        for (Point2D.Float point : points) 
        {
            pen.fillOval((int) point.x, (int) point.y, 5, 5);
            pen.drawString("(" + point.x + ", " + point.y + ")", (int) point.x, (int) point.y + 25);
        }

    }
}
