import java.awt.geom.Point2D.Float;

public class BossFairy extends Boss
{
	Animation AN_MOV_LT	, AN_MOV_RT;
	Animation AN_ID		, AN_CMD_CH, AN_CMD_SH;
	
	int phases;
	
	// TODO: MAKE NONSPELLS, SPELL CARDS
	// TODO: MAKE LOOPING MOVEMENT PATTERNS
	// TODO: MAKE FAMILIARS
	
	public BossFairy(Float pos, int width, int height, int health, int phases) 
	{
		super(pos, width, height, health);	
		this.phases = phases;
	}
	
	public void Initialize()
	{
		
		// Initialize Animations
		AN_ID = new Animation();
		AN_ID.AddFrame(Animation.ReadImage("Assets/Boss/Boss-ID"), 100);
		
		AN_MOV_LT = new Animation();
		AN_MOV_LT.AddFrame(Animation.ReadImage("Assets/Boss/Boss-LT"), 100);
		
		AN_MOV_RT = new Animation();
		AN_MOV_RT.AddFrame(Animation.ReadImage("Assets/Boss/Boss-RT"), 100);
		
		AN_CMD_CH = new Animation();
		AN_CMD_CH.AddFrame(Animation.ReadImage("Assets/Boss/Boss-SH1"), 100);
		
		AN_CMD_SH = new Animation();
		AN_CMD_SH.AddFrame(Animation.ReadImage("Assets/Boss/Boss-SH2"), 100);
		
	}
	
	public void Update() 
	{
		
	}
	
	public void AnimationUpdate() 
	{
		
	}

}
