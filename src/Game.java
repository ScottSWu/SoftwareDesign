import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import javax.media.opengl.GL;
import funnytrees.JOsu.OsuData.*;
import funnytrees.JAudioPlayer.*;

public class Game implements KeyListener,MouseListener,MouseMotionListener,MouseWheelListener{
	private Dimension screen;
	private Dimension bounds,movementField,appearField;
	private boolean keys[] = new boolean[256];
	
	public long time;
	private PlayerEntity player;
	private ArrayList<EnemyEntity> enemies;
	private ArrayList<BulletEntity> bullets;
	
	// Prototype test
	private OsuBeatmap map;
	private JAudioPlayer audioPlayer;
	private int hitoffset;
	private long approachSpeed = 800;
	private int hitSize = 30;
	
	// Display animations
	private double healthAnimation;
	
	public Game(Dimension dim) {
		screen = dim;
		bounds = new Dimension(screen.width/2,screen.height/2);
		appearField = new Dimension(256,192);
		movementField = new Dimension(320,240);
		player = new PlayerEntity();
		enemies = new ArrayList<EnemyEntity>();
		bullets = new ArrayList<BulletEntity>();
	}
	
	public void start() {
		// Content
		map = new OsuBeatmap();
		String songFolder,songMap;
		//songFolder = "50669 fripSide - only my railgun (TV Size)"; songMap = "fripSide - only my railgun (TV Size) (Kite) [Easy].osu";
		//songFolder = "66221 Suzuki Konomi - DAYS of DASH"; songMap = "Suzuki Konomi - DAYS of DASH (Rotte) [A32's Hard].osu";
		//songFolder = "43003 yanaginagi - Killer Song (Short Ver)"; songMap = "yanaginagi - Killer Song (Short Ver.) (terametis) [Insane].osu";
		//songFolder = "41823 The Quick Brown Fox - The Big Black"; songMap = "The Quick Brown Fox - The Big Black (Blue Dragon) [WHO'S AFRAID OF THE BIG BLACK].osu";
		//songFolder = "41823 The Quick Brown Fox - The Big Black"; songMap = "The Quick Brown Fox - The Big Black (Blue Dragon) [Ono's Taiko Oni].osu";
		//songFolder = "41686 Lily - Scarlet Rose"; songMap = "Lily - Scarlet Rose (val0108) [0108 style].osu";
		//songFolder = "13223 Demetori - Emotional Skyscraper ~ World's End"; songMap = "Demetori - Emotional Skyscraper ~ World's End (happy30) [Extra Stage].osu";
		//songFolder = "43003 yanaginagi - Killer Song (Short Ver)"; songMap = "yanaginagi - Killer Song (Short Ver.) (terametis) [Noldmal].osu";
		songFolder = "37980 An - TearVid"; songMap = "An - TearVid (Shiirn) [Another].osu";
		map.read(songFolder,songMap);
		audioPlayer = JAudioPlayer.getPlayer("Songs/" + songFolder + "/" + map.AudioFilename);
		
		// Timing
		time = 0;
		hitoffset = 0;
		audioPlayer.start();
		//audioPlayer.pause();
		//paused = true;
		
		// Animations
		healthAnimation = 0;
	}
	
	public void render(GL glo) {
		//long currentTime = time/1000000;
		long currentTime = audioPlayer.getTime();
		EnemyEntity etemp = new EnemyEntity();
		
		glo.glBegin(GL.GL_LINE_LOOP);
			glo.glColor3f(1f,0f,0f);
			glo.glVertex3i(-movementField.width,movementField.height,0);
			glo.glVertex3i(movementField.width,movementField.height,0);
			glo.glVertex3i(movementField.width,-movementField.height,0);
			glo.glVertex3i(-movementField.width,-movementField.height,0);
		glo.glEnd();
		
		//if (glo!=null) return;
		// PlayerEntity
		player.render(glo);
		// Approaching enemies
		int color;
		for (int i=hitoffset; i<map.HitObjects.length && map.HitObjects[i].time<currentTime + approachSpeed; i++) {
			etemp.position.set(map.HitObjects[i].x - appearField.width,appearField.height - map.HitObjects[i].y);
			etemp.size = hitSize * (1.0 - (double) (map.HitObjects[i].time-currentTime)/approachSpeed);
			color = map.Combos[map.HitObjects[i].combo];
			etemp.color[0] = ((color>>16) & 0xFF)/256f;
			etemp.color[1] = ((color>> 8) & 0xFF)/256f;
			etemp.color[2] = ((color>> 0) & 0xFF)/256f;
			etemp.render(glo);
		}
		// Enemies
		for (Entity e : enemies) {
			e.render(glo);
		}
		// Bullets
		for (Entity e : bullets) {
			e.render(glo);
		}
		
		// Render Information
		float hc = (float) (Math.cos(healthAnimation*60/Math.PI)+1)/2;
		glo.glColor3f(hc,1f,hc);
		glo.glBegin(GL.GL_QUADS);
			glo.glVertex2d(-bounds.width*player.health,bounds.height-32);
			glo.glVertex2d(-bounds.width*player.health,bounds.height);
			glo.glVertex2d(bounds.width*player.health,bounds.height);
			glo.glVertex2d(bounds.width*player.health,bounds.height-32);
		glo.glEnd();
	}
	
	public void frame(long elapsed) {
		long currentTime = audioPlayer.getTime();
		double dt = elapsed/1000000000.0;
		System.out.println(currentTime + "\t" + dt + "\t" + enemies.size() + "\t" + bullets.size() + "\t" + player.hits);
		if (paused) return;
		
		// Keypresses
		double moveScale = 250;
		if (keys[KeyEvent.VK_SHIFT]) { moveScale = 100; }
		if (keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A]) { player.position.x -= moveScale*dt; }
		if (keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D]) { player.position.x += moveScale*dt; }
		if (keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S]) { player.position.y -= moveScale*dt; }
		if (keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W]) { player.position.y += moveScale*dt; }
		
		if (player.position.x<-movementField.width) player.position.x = -movementField.width;
		if (player.position.x> movementField.width) player.position.x = movementField.width;
		if (player.position.y<-movementField.height) player.position.y = -movementField.height;
		if (player.position.y> movementField.height) player.position.y = movementField.height;
		
		//if (keys[KeyEvent.VK_M]) { createEnemy(Math.random()*screen.width-screen.width/2,Math.random()*screen.height-screen.height/2); }
		
		// Update
		// Player
		player.frame(dt);
		
		// New Enemies
		EnemyEntity etemp;
		if (hitoffset<map.HitObjects.length && currentTime>map.HitObjects[hitoffset].time) {
			etemp = new EnemyEntity();
			etemp.position.set(map.HitObjects[hitoffset].x - appearField.width,appearField.height - map.HitObjects[hitoffset].y,0);
			etemp.size = hitSize;
			enemies.add(etemp);
			hitoffset++;
		}
		
		// Render entities
		for (int i=0; i<enemies.size(); i++) {
			enemies.get(i).frame(dt);
			if (enemies.get(i).isDone()) {
				enemies.get(i).explode(bullets);
				enemies.remove(i--);
			}
		}
		for (int i=0; i<bullets.size(); i++) {
			bullets.get(i).frame(dt);
			if (bullets.get(i).isOutside(bounds)) {
				bullets.remove(i--);
			}
			else if (bullets.get(i).isNear(player,bullets.get(i).size)) {
				player.hit();
				bullets.remove(i--);
			}
		}
		
		// Progress Animations
		healthAnimation += dt;
		if (healthAnimation>120) healthAnimation = 0;
	}
	
	public void createEnemy(double x,double y) {
		EnemyEntity newE = new EnemyEntity();
		newE.position.set(x,y);
		enemies.add(newE);
	}

	// Listeners
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		keys[arg0.getKeyCode()] = true;
		if (arg0.getKeyCode()==KeyEvent.VK_M) { createEnemy(Math.random()*screen.width-screen.width/2,Math.random()*screen.height-screen.height/2); }
		if (arg0.getKeyCode()==KeyEvent.VK_P) {
			System.out.println("Paused");
			if (!paused) {
				paused = true;
				audioPlayer.pause();
			}
			else {
				paused = false;
				audioPlayer.resume();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		keys[arg0.getKeyCode()] = false;
	}
	
	private boolean paused = false;
	public void keyTyped(KeyEvent arg0) {
		
	}
}
