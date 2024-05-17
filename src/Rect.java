import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Rect 
{
	Float  pos = new Float(),
		  oPos = new Float(),
		   vel = new Float();
	
	int width, height;
	float oldX, oldY;
	
	private LinkedList<Rect> children = new LinkedList<Rect>();
	
	boolean playerOverlap;
	protected Animation anim;
	
	public Rect(float x, float y, int width, int height) 
	{
		
		pos.x = (int) x;
		pos.y = (int) y;
		
		this.width  = width;
		this.height = height;
		
		oldX =  x;
		oldY =  y;
	}
	
	public void AssignAnimation(Animation anim) {this.anim = anim;}
	
	public void ResizeBy(int dw, int dh) 
	{
		width += dw;
		height+= dh;
	}
	
	public void Draw(Graphics pen) 
	{
		pen.drawRect( (int) GetX(), (int) GetY(), width, height);
	}
	
	// Draw the rectangle with an offset
	public void Draw(Graphics pen, float dx, float dy) 
	{
		pen.drawRect( (int) (GetX() + dx), (int) (GetY() + dy), GetW(), GetH());
	}
	
	public boolean Overlaps(Rect r) 
	{
		// Get top left and bottom right of both rectangles.
		float cornerX = GetX() + GetW();
		float cornerY = GetY() + GetH();
		
		float rCornerX = r.GetX() + r.GetW();
		float rCornerY = r.GetY() + r.GetH();
		
		return (	(cornerX  >= r.GetX()) 
				&& 	(cornerY  >= r.GetY()) 
				&& 	(rCornerX >= GetX())	
				&& 	(rCornerY >= GetY())  );
		
	}
	

	// Pushback function for Collisions with impermeable objects
	public void PushbackFrmLT(Rect r) {SetX(r.GetX() -   GetW() - 1);}
	public void PushbackFrmRT(Rect r) {SetX(r.GetX() + r.GetW() + 1);}
	public void PushbackFrmUP(Rect r) {SetY(r.GetY() -   GetH() - 1);}
	public void PushbackFrmDN(Rect r) {SetY(r.GetY() + r.GetH() + 1);}

	// Movement Functions
	public void MoveUP(float dy) 
	{
		oPos.y = (int) GetY();
		pos.y -= dy;
	}
	public void MoveDN(float dy) 
	{
		oPos.y = (int) GetY();
		pos.y += dy;
	}
	public void MoveRT(float dx) 
	{
		oPos.x = (int) GetX();
		pos.x += dx;
	}
	public void MoveLT(float dx) 
	{
		oPos.x = (int) GetX();
		pos.x -= dx;
	}

	// Updates this entity's Animation and it's position based on velocity
	public void Update(float deltaTime) 
	{
		if (anim != null) {anim.Update(deltaTime);}
	}
	
	// Detecting where a collision happened
	public boolean CollidesLT(Rect r) {return oPos.x + GetW()  	< r.GetX() + 1;}
	public boolean CollidesRT(Rect r) {return oPos.x + 1 		> r.GetX() + r.GetW() - 1;}
	public boolean CollidesDN(Rect r) {return oPos.y + GetH()	< r.GetY();}
	public boolean CollidesUP(Rect r) {return oPos.y - 1 		> r.GetY() + r.height;}
	
	// Do something when a Collision Happens
	public boolean CheckCollision(Rect neighbor) 
	{
		playerOverlap = false;
		
		if (neighbor.CollidesLT(this)) {OnCollisionLT(neighbor);}
		if (neighbor.CollidesRT(this)) {OnCollisionRT(neighbor);}
		if (neighbor.CollidesDN(this)) {OnCollisionUP(neighbor);}
		if (neighbor.CollidesUP(this)) {OnCollisionDN(neighbor);}
				
		if (neighbor instanceof Player && Overlaps(neighbor)) {playerOverlap = true;}
		return Overlaps(neighbor);
	}
	
	// Check if the rectangle is not on the screen
	protected boolean IsOffscreen() 
	{ 
		// Allow for spawning some enemies off screen, adjust if needed
		int padding = 30;
		
		return (pos.getX() > Game2D.SCREEN_WIDTH  + padding ||
				pos.getY() > Game2D.SCREEN_HEIGHT + padding ||
				pos.getX() < 0 - padding 					||
				pos.getY() < 0 - padding					  ); 
	}
	
	// Override-able for Collision Handling
	public void OnCollisionLT(Rect neighbor) {}
	public void OnCollisionRT(Rect neighbor) {}
	public void OnCollisionDN(Rect neighbor) {} 
	public void OnCollisionUP(Rect neighbor) {}
	
	// Getting Values of a Rectangle
	public float GetX() {return pos.x;}
	public float GetY() {return pos.y;}
	public int 	 GetW() {return width;}
	public int 	 GetH() {return height;}
	public Point2D.Float GetPos() { return pos; }

	// Setting Positions on the Screen
	public void SetX(float x) {pos.x = x;}
	public void SetY(float y) {pos.y = y;}
	public void SetW(int w)   {this.width  = w;}
	public void SetH(int h)   {this.height = h;}
	public void SetPos(Point2D.Float pos) { this.pos = pos; }
	
	// Getting and Setting Velocity
	public float GetVelX() {return vel.x;}
	public float GetVelY() {return vel.y;}
	public void  SetVelX( float dx ) { vel.x = dx; }
	public void  SetVelY( float dy ) { vel.y = dy; }
	
	// Finding a point relative to the Rect
	public boolean RT_OF(float destination) { return destination < GetX(); }
	public boolean LT_OF(float destination) { return destination > GetX(); }
	public boolean UP_OF(float destination) { return destination > GetY(); }
	public boolean DN_OF(float destination) { return destination < GetY(); }
		
	// TODO: FIX THIS
	// Finding a Rect relative to the Rect
	public boolean RT_OF(Rect neighbor) { return neighbor.GetX() + neighbor.GetW()/2 < GetX() + GetW()/2; }
	public boolean LT_OF(Rect neighbor) { return neighbor.GetX() + neighbor.GetW()/2 > GetX() + GetW()/2; }
	public boolean UP_OF(Rect neighbor) { return neighbor.GetY() + neighbor.GetH()/2 > GetX() + GetH()/2; }
	public boolean DN_OF(Rect neighbor) { return neighbor.GetY() + neighbor.GetH()/2 < GetY() + GetH()/2; }
	
	// Checking if the position is at the specified point
	protected boolean AtPoint ( Float float1 )
	{
		return (pos.x == float1.x && pos.y == float1.y);
	}
	
	// Checking if the position is around the specified point
	protected boolean AroundPoint (Float destination, float padding)
	{
		return  (
					pos.x < destination.x + padding &&
					pos.x > destination.x - padding &&
					pos.y < destination.y + padding &&
					pos.y > destination.y - padding
				);
	}
	
	// Get Animation Image
	public BufferedImage GetImage() { return anim.GetImage(); }
	
	public void SetImage(String filepath) 
	{
		Animation newanim = new Animation();
		newanim.AddFrame(Animation.ReadImage(filepath), 100);
		anim = newanim;
	}
	
	public void DrawImage(Graphics pen) 
	{
		if (anim != null && anim.GetImage() != null)
			pen.drawImage(anim.GetImage(), (int) GetX(), (int) GetY(), null);
	}
	
	// Add children to rectangle if object owns another rectangle
	protected void AddChild(Rect child) 
	{
		children.add(child);
	}
	
	// Iterate through all children
	public Rect ReadChild(int child)
	{
		return (Rect) children.get(child);
	}
	
	// Get amount of children
	public Iterator  GetChildren()
	{
		return children.iterator();
	}
	
	// Return if the rectangle owns children
	public boolean HasChildren() {return children.size() != 0;}
	
	// Cloning this Object
	public Object Clone(int x, int y) 
	{ 
		Rect cloneRect = new Rect( x, y, width, height);
		
		if (this.anim != null) 
		{
			cloneRect.anim = new Animation();
			cloneRect.AssignAnimation(anim);
		}
		
		return cloneRect; 
	}
}
