import java.awt.*;

public abstract class Game2D implements Runnable
{
	// Intended Aspect Ratio is 4:3
	// Intended Resolution is 960 x 672
	// TODO: Add ability to resize game and keep aspect ratio
	
	protected ScreenManager sm;
	
	private   Long initialTime = System.nanoTime();
	protected Long timer 	   = System.currentTimeMillis();
	
	private final int UPS = 60,
					  FPS = 60;

	private final  Float TIME_UPDATE = 1000000000f / UPS,
						 TIME_FRAMES = 1000000000f / FPS;
	
	private static Float deltaUpdate = 0f,
						 deltaFrames = 0f;
	
	private int frames = 0, 
				ticks  = 0;
	
	protected boolean debug = true;
	
	public static int SCREEN_HEIGHT,
					  SCREEN_WIDTH;
	
	boolean running = true;

	// temporary
	float totalDelta = 0f;
	
	// What starts the thread
	public void Init() 
	{
		// Window
		sm = new ScreenManager();
		
		SCREEN_HEIGHT = sm.GetHeight();
		SCREEN_WIDTH  = sm.GetWidth();
		
		// Thread t = new Thread(this);
		// t.start();
	}
		
	// What runs inside the thread
	public void run() 
	{
		System.out.println(TileMapRenderer.TilesToPixels(3));
		try 
		{
			Init();
			GameLoop();
		}
		finally {}
	}
	
	// Defined Functions
	
	public void GameLoop() 
	{
		// Game Loop
		while(running) 
		{		
			Long currentTime = System.nanoTime();
			Long updateTime  = (currentTime - initialTime);
			
			deltaUpdate += updateTime / TIME_UPDATE.floatValue();
			deltaFrames += updateTime / TIME_FRAMES.floatValue();
			
			initialTime = currentTime;
			
			// System.out.println( deltaUpdate );

			while (deltaUpdate >= 1) 
			{
				GetInput();
				Update();
				
				ticks++;
				deltaUpdate--;
			}
			
			while (deltaFrames >= 1) 
			{
				frames++;
				deltaFrames--;
				Render();
			}
		}
	}

	// Update Screen
	private void Render() 
	{
		// Drawing to the Screen
		Graphics2D pen = sm.GetGraphics();
		pen.clearRect(0, 0, sm.GetWidth(), sm.GetHeight());
		Draw(pen);
		pen.dispose();
		sm.Update();		
	}
	
	public static Float Delta() {return deltaUpdate;}
	
	// Updates state of game/animation based on amt of elapsed time that has passed
	public void Update() {}
	
	// Draws to the screen. Subclasses must Override this method
	public abstract void Draw (Graphics2D pen);
	
	// draw a grid every 100 pixels to show coordinates
	public void DrawGrid(Graphics2D pen) 
	{
		pen.setColor(Color.WHITE);
		// Horizontal Lines
		for (int i = 0 ; i < sm.GetHeight() ; i += 100)
		{
			pen.drawLine(0, i, sm.GetWidth(), i);
		}
		
		// Vertical Lines
		for (int i = 0; i < sm.GetWidth() ; i += 100)
		{
			pen.drawLine(i, 0, i, sm.GetHeight());
		}
	}
	
	protected void ReportFPS() 
	{
		if (System.currentTimeMillis() - timer > 1000) 
		{
			System.out.println(String.format("UPS: %s, FPS: %s, Delta: %s, totalDelta: %s", ticks, frames, Delta(), totalDelta));
			
			frames = 0;
			ticks  = 0;
			timer += 1000;
			totalDelta = 0;
		}
		else
		{
			totalDelta += Delta();
		}
	}
	
	// Gets Input from the Player
	public abstract void GetInput();
}
