import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.Arrays;

public class Level1 extends Level
{
	
	protected void EnemySchedule() 
	{
		// Wave one
		
		Wave waveOne = new Wave(200, 0);
		
		waveOne.AddEnemy(new Fairy( SpawnLaneH[0] , 64, 64, 10));
		waveOne.AddEnemy(new Fairy( SpawnLaneH[1] , 64, 64, 10));
		waveOne.AddEnemy(new Fairy( SpawnLaneH[2] , 64, 64, 10));
		waveOne.AddEnemy(new Fairy( SpawnLaneH[3] , 64, 64, 10));
		
		waveOne.GetEnemies().forEach(enemy -> {
			enemy.NewPath( 
					0.5f, 30, 500f, // Tension, Segments, Deadline
					new Float(300, Game2D.SCREEN_HEIGHT - 400)
			);
			
			enemy.NewPath(
					0.5f, 30, 800f, 
					new Float(400, Game2D.SCREEN_HEIGHT - 300),
					new Float(300, Game2D.SCREEN_HEIGHT - 200),
					new Float(200, Game2D.SCREEN_HEIGHT - 300),
					new Float(300, Game2D.SCREEN_HEIGHT - 400),
					new Float(400, Game2D.SCREEN_HEIGHT - 300),
					new Float(300, Game2D.SCREEN_HEIGHT - 200),
					new Float(200, Game2D.SCREEN_HEIGHT - 300),
					new Float(300, Game2D.SCREEN_HEIGHT - 400),
					new Float(OffscreenRT(), OffscreenUP())
			);
			
			enemy.SetStrategy( new ShotStrategy.FireCircleA2(50, 0, 0, 0, 0.0025f, 3, 0));
			// enemy.SetStrategy( new ShotStrategy.FireA2( enemy.GetX(), enemy.GetY(), 0, 0, 0.25f, 3, 0) );
		});
		
		Wave waveOne2 = new Wave(200, 0);
		
		waveOne2.AddEnemy(new Fairy( SpawnLaneH[4] , 64, 64, 10));
		waveOne2.AddEnemy(new Fairy( SpawnLaneH[5] , 64, 64, 10));
		waveOne2.AddEnemy(new Fairy( SpawnLaneH[6] , 64, 64, 10));
		waveOne2.AddEnemy(new Fairy( SpawnLaneH[7] , 64, 64, 10));
		
		waveOne2.GetEnemies().forEach(enemy -> {
			enemy.NewPath( 
					0.5f, 30, 500f, // Tension, Segments, Deadline
					new Float(Game2D.SCREEN_WIDTH - 300, Game2D.SCREEN_HEIGHT - 400)
			);
			
			enemy.NewPath( 
					0.5f, 30, 800f, // Tension, Segments, Deadline
					new Float(Game2D.SCREEN_WIDTH - 400, Game2D.SCREEN_HEIGHT - 300),
					new Float(Game2D.SCREEN_WIDTH - 300, Game2D.SCREEN_HEIGHT - 200),
					new Float(Game2D.SCREEN_WIDTH - 200, Game2D.SCREEN_HEIGHT - 300),
					new Float(Game2D.SCREEN_WIDTH - 300, Game2D.SCREEN_HEIGHT - 400),
					new Float(Game2D.SCREEN_WIDTH - 400, Game2D.SCREEN_HEIGHT - 300),
					new Float(Game2D.SCREEN_WIDTH - 300, Game2D.SCREEN_HEIGHT - 200),
					new Float(Game2D.SCREEN_WIDTH - 200, Game2D.SCREEN_HEIGHT - 300),
					new Float(Game2D.SCREEN_WIDTH - 300, Game2D.SCREEN_HEIGHT - 400),
					new Float(OffscreenLT(), OffscreenUP())
			);
			enemy.SetStrategy( new ShotStrategy.FireSpreadA2(5, 0, 0.05f, 0, 0.0025f, 3, 0));
		});
		
		waveOne.MergeWave(waveOne2);
		AddWave(waveOne);	
		/*
		// Wave 2
		
		Wave waveTwo = new Wave(30, 100);
		
		waveTwo.AddEnemy(new Fairy( SpawnLaneL[1] , 64, 64, 10));
		waveTwo.AddEnemy(new Fairy( SpawnLaneL[2] , 64, 64, 10));
		waveTwo.AddEnemy(new Fairy( SpawnLaneL[3] , 64, 64, 10));
		waveTwo.AddEnemy(new Fairy( SpawnLaneL[4] , 64, 64, 10));
		
		waveTwo.GetEnemies().forEach(enemy -> {
				enemy.NewPath( 
						0.5f, 1, 400f, // Tension, Segments, Deadline
						new Float(Game2D.SCREEN_WIDTH - 200, enemy.GetY())
				);
				enemy.NewPath( 
						0.5f, 1, 400f, // Tension, Segments, Deadline
						new Float(OffscreenRT(), enemy.GetY())
				);
				enemy.SetStrategy( new ShotStrategy.FireSpreadA2(9, 0, 30, 0, 0.0025f, 3, 0));
		});
		
		AddWave(waveTwo);
	
		
		Wave waveThree = new Wave(30, 100);
		
		waveThree.AddEnemy(new Fairy( SpawnLaneR[1] , 64, 64, 10));
		waveThree.AddEnemy(new Fairy( SpawnLaneR[2] , 64, 64, 10));
		waveThree.AddEnemy(new Fairy( SpawnLaneR[3] , 64, 64, 10));
		waveThree.AddEnemy(new Fairy( SpawnLaneR[4] , 64, 64, 10));
		
		waveThree.GetEnemies().forEach(enemy -> {
				enemy.SetStrategy( new ShotStrategy.FireCircleA2(20, 0, 0, 0, 0.0025f, 3, 0));
				enemy.NewPath(
						0.5f, 1, 400f, 
						new Float( 200, enemy.GetY() )
				);
				enemy.NewPath(
						0.5f, 1, 400f, 
						new Float( OffscreenLT(), enemy.GetY() )
				);
		});
		
		AddWave(waveThree);
		
		Wave waveFour = new Wave(30, 100);
		
		waveFour.AddEnemy(new Fairy( SpawnLaneR[4] , 64, 64, 70));
		
		waveFour.GetEnemies().forEach(enemy -> {
				enemy.SetStrategy( new ShotStrategy.FireCircleA2(20, 0, 0, 0, 0.0025f, 3, 0));
				enemy.NewPath(
						0.5f, 30, 500f, 
						new Float( 800, 400 ),
						new Float( 650, 600 ),
						new Float( 500, 400 ),
						new Float( 350, 100 ),
						new Float( 200, 400 ),
						new Float( 350, 600 ),
						new Float( 500, 400 ),
						new Float( 650, 100 )
				);
				enemy.NewPath(
						0.5f, 30, 500f, 
						new Float( 800, 400 ),
						new Float( 650, 600 ),
						new Float( 500, 400 ),
						new Float( 350, 100 ),
						new Float( 200, 400 ),
						new Float( 350, 600 ),
						new Float( 500, 400 ),
						new Float( 650, 100 ),
						new Float( 800, 400 ),
						new Float( 650, 600 ),
						new Float( 500, 400 ),
						new Float( 350, 100 )
						
				);
				enemy.NewPath(
						0.5f, 30, 200f, 
						new Float( 200, 400 ),
						new Float( 350, 600 ),
						new Float( 500, 400 ),
						new Float( 650, 100 ),
						new Float( 800, 400 ),
						new Float( 650, 600 ),
						new Float( 500, 400 ),
						new Float( 350, 100 ),
						new Float( 200, 400 )
				);
				enemy.NewPath(
						0.5f, 1, 500f,
						new Float( OffscreenLT(), 400 )
				);
		});
		
		AddWave(waveFour);
	

		Wave waveFive = new Wave(0, 200);
		
		waveFive.AddEnemy(new Fairy( SpawnLaneH[6] , 64, 64, 10));
		waveFive.AddEnemy(new Fairy( SpawnLaneH[6] , 64, 64, 10));
		waveFive.AddEnemy(new Fairy( SpawnLaneH[6] , 64, 64, 10));
		waveFive.AddEnemy(new Fairy( SpawnLaneH[6] , 64, 64, 10));
		
		waveFive.GetEnemies().forEach(enemy -> {
			enemy.NewPath( 
					0.5f, 30, 200f, // Tension, Segments, Deadline
					new Float(Game2D.SCREEN_WIDTH - 400, 400),
					new Float(Game2D.SCREEN_WIDTH - 800, 500)
			);
			
			enemy.NewPath( 
					0.5f, 30, 200f, // Tension, Segments, Deadline
					new Float(Game2D.SCREEN_WIDTH - 500, 700),
					new Float(Game2D.SCREEN_WIDTH - 200, 800)
			);
			
			enemy.NewPath( 
					0.5f, 30, 200f, // Tension, Segments, Deadline
					new Float(Game2D.SCREEN_WIDTH - 400, 1000),
					new Float(Game2D.SCREEN_WIDTH - 800, 1100)
			);
			
			enemy.NewPath( 
					0.5f, 30, 200f, // Tension, Segments, Deadline
					new Float(Game2D.SCREEN_WIDTH - 700, 1200),
					new Float(OffscreenLT(), OffscreenDN())
			);
			enemy.SetStrategy( new ShotStrategy.FireStackA2(5, 0, 0.05f, 0, 0.0025f, 3, 0));
			// enemy.SetStrategy( new ShotStrategy.FireA2( enemy.GetX(), enemy.GetY(), 0, 0, 0.25f, 3, 0) );
		});
		
		Wave waveFive2 = new Wave(0, 50);
		
		waveFive2.AddEnemy(new Fairy( SpawnLaneH[2] , 64, 64, 10));
		waveFive2.AddEnemy(new Fairy( SpawnLaneH[2] , 64, 64, 10));
		waveFive2.AddEnemy(new Fairy( SpawnLaneH[2] , 64, 64, 10));
		waveFive2.AddEnemy(new Fairy( SpawnLaneH[2] , 64, 64, 10));
		
		waveFive2.GetEnemies().forEach(enemy -> {
			enemy.NewPath( 
					0.5f, 30, 500f, // Tension, Segments, Deadline
					new Float(400, 400),
					new Float(800, 500)
			);
			
			enemy.NewPath( 
					0.5f, 30, 500f, // Tension, Segments, Deadline
					new Float(500, 700),
					new Float(200, 800)
			);
			
			enemy.NewPath( 
					0.5f, 30, 500f, // Tension, Segments, Deadline
					new Float(400, 1000),
					new Float(800, 1100)
			);
			
			enemy.NewPath( 
					0.5f, 30, 500f, // Tension, Segments, Deadline
					new Float(700, 1200),
					new Float(OffscreenRT(), OffscreenDN())
			);
			enemy.SetStrategy( new ShotStrategy.FireStackA2(5, 0, 0.05f, 0, 0.0025f, 3, 0));
		});
		
		waveFive.MergeWave(waveFive2);
		AddWave(waveFive);	
		*/
	}
}
