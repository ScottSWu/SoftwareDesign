import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.media.opengl.GL2;
import javax.media.opengl.GLException;
import funnytrees.JOsu.OsuData.*;
import funnytrees.JAudioPlayer.*;

public class Game implements KeyListener,MouseListener,MouseMotionListener,MouseWheelListener {
	private Dimension movementField,appearField;
	
	// Game variables
	public long time;
	public int[] mouse = new int[5];
	public boolean[] keys = new boolean[256];
	private String songFolder,songMap;
	private OsuBeatmap map;
	private PlayerEntity player;
	private ArrayList<EnemyEntity> enemies;
	private ArrayList<BulletEntity> bullets;
	
	// Graphics
	private ImageTexture background;
	private ImageTexture inputDisplay;
	
	// Prototype test
	private boolean started;
	private JAudioPlayer audioPlayer;
	private boolean paused;
	private int hitoffset;
	private int timingoffset;
	private long approachSpeed = 800;
	private int hitSize = 30;
	private long score = 0;
	
	// Display animations
	private double healthAnimation;
	
	public Game() {
		appearField = new Dimension(256,192);
		movementField = new Dimension(320,240);
	}
	
	public void setMap(String sf,String sm) {
		songFolder = sf;
		songMap = sm;
	}
	
	public void start() {
		player = new PlayerEntity();
		enemies = new ArrayList<EnemyEntity>();
		bullets = new ArrayList<BulletEntity>();
		
		map = new OsuBeatmap();
		map.read(songFolder,songMap);
		audioPlayer = JAudioPlayer.getPlayer("Songs/" + songFolder + "/" + map.AudioFilename);
		inputDisplay = new ImageTexture("images/keyInput.png");
		
		score = 0;
		
		// Content textures
		try {
			String bgFile = "";
			background = null;
			for (int i=0; i<map.BackgroundEvents.length; i++) {
				if (map.BackgroundEvents[i].type==0) {
					bgFile = map.BackgroundEvents[i].media;
						background = new ImageTexture("Songs/" + songFolder + "/" + bgFile);
						background.loadTexture();
					break;
				}
			}
			
			inputDisplay.loadTexture();
		}
		catch (GLException e) {
			e.printStackTrace();
		}
		
		// Timing
		time = -map.AudioLeadIn;
		hitoffset = 0;
		timingoffset = 0;
		audioPlayer.start();
		audioPlayer.pause();
		audioPlayer.setVolumeGain(-15f);
		paused = false;
		started = true;
		
		// Animations
		healthAnimation = 0;
	}
	
	public void stop() {
		started = false;
		audioPlayer.stop();
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public void render(GL2 glo) {
		//long currentTime = time/1000000;
		long currentTime = time;
		EnemyEntity etemp = new EnemyEntity();
		
		if (background!=null) {
			glo.glEnable(GL2.GL_TEXTURE_2D);
			background.texture.enable(glo);
			background.texture.bind(glo);
			glo.glColor3f(0.5f,0.5f,0.5f);
			drawBox(glo,-Configuration.bounds.width,Configuration.bounds.height,Configuration.bounds.width,-Configuration.bounds.height);
			background.texture.disable(glo);
			glo.glDisable(GL2.GL_TEXTURE_2D);
		}
		
		glo.glBegin(GL2.GL_QUADS);
		float vs = 0.25f,ve = 1f;
		glo.glColor4f(0f,0f,0f,vs); glo.glVertex2i(movementField.width,-movementField.height);
		glo.glColor4f(0f,0f,0f,ve); glo.glVertex2i(Configuration.bounds.width,-Configuration.bounds.height);
		glo.glColor4f(0f,0f,0f,ve); glo.glVertex2i(Configuration.bounds.width,Configuration.bounds.height);
		glo.glColor4f(0f,0f,0f,vs); glo.glVertex2i(movementField.width,movementField.height);
		
		glo.glColor4f(0f,0f,0f,vs); glo.glVertex2i(movementField.width,movementField.height);
		glo.glColor4f(0f,0f,0f,ve); glo.glVertex2i(Configuration.bounds.width,Configuration.bounds.height);
		glo.glColor4f(0f,0f,0f,ve); glo.glVertex2i(-Configuration.bounds.width,Configuration.bounds.height);
		glo.glColor4f(0f,0f,0f,vs); glo.glVertex2i(-movementField.width,movementField.height);
		
		glo.glColor4f(0f,0f,0f,vs); glo.glVertex2i(-movementField.width,movementField.height);
		glo.glColor4f(0f,0f,0f,ve); glo.glVertex2i(-Configuration.bounds.width,Configuration.bounds.height);
		glo.glColor4f(0f,0f,0f,ve); glo.glVertex2i(-Configuration.bounds.width,-Configuration.bounds.height);
		glo.glColor4f(0f,0f,0f,vs); glo.glVertex2i(-movementField.width,-movementField.height);
		
		glo.glColor4f(0f,0f,0f,vs); glo.glVertex2i(-movementField.width,-movementField.height);
		glo.glColor4f(0f,0f,0f,ve); glo.glVertex2i(-Configuration.bounds.width,-Configuration.bounds.height);
		glo.glColor4f(0f,0f,0f,ve); glo.glVertex2i(Configuration.bounds.width,-Configuration.bounds.height);
		glo.glColor4f(0f,0f,0f,vs); glo.glVertex2i(movementField.width,-movementField.height);
		glo.glEnd();
		
		/*
		glo.glBegin(GL2.GL_LINE_LOOP);
			glo.glColor3f(1f,0f,0f);
			glo.glVertex2i(-movementField.width,movementField.height);
			glo.glVertex2i(movementField.width,movementField.height);
			glo.glVertex2i(movementField.width,-movementField.height);
			glo.glVertex2i(-movementField.width,-movementField.height);
		glo.glEnd();
		*/
		
		//if (glo!=null) return;
		// Approaching enemies
		// TODO Need to invert, add to enemies list and let hitoffset be the end of the scan.
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
		// Player
		player.render(glo);
		// Bullets
		for (Entity e : bullets) {
			e.render(glo);
		}
		
		// Render Information
		float hc = (float) (Math.cos(healthAnimation*60/Math.PI)+1)/2;
		glo.glColor3f(hc,1f,hc);
		drawBox(glo,-Configuration.bounds.width*player.health,Configuration.bounds.height-32,Configuration.bounds.width*player.health,Configuration.bounds.height-8);
		
		// Inputs
		glo.glEnable(GL2.GL_TEXTURE_2D);
		inputDisplay.texture.enable(glo);
		inputDisplay.texture.bind(glo);
		
		glo.glColor3f(1f,1f,1f); if (keys[KeyEvent.VK_SHIFT]) glo.glColor3f(1f,0f,0f);
		drawBoxPortion(glo,-movementField.width,	-Configuration.bounds.height,	-movementField.width+60,	-Configuration.bounds.height+30,0,80,80,40,640,80);
		glo.glColor3f(1f,1f,1f); if (keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP]) glo.glColor3f(1f,0f,0f);
		drawBoxPortion(glo,-movementField.width+90,	-Configuration.bounds.height+30,-movementField.width+120,	-Configuration.bounds.height+60,120,40,160,0,640,80);
		glo.glColor3f(1f,1f,1f); if (keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT]) glo.glColor3f(1f,0f,0f);
		drawBoxPortion(glo,-movementField.width+60,	-Configuration.bounds.height,	-movementField.width+90,	-Configuration.bounds.height+30,80,80,120,40,640,80);
		glo.glColor3f(1f,1f,1f); if (keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN]) glo.glColor3f(1f,0f,0f);
		drawBoxPortion(glo,-movementField.width+90,	-Configuration.bounds.height,	-movementField.width+120,	-Configuration.bounds.height+30,120,80,160,40,640,80);
		glo.glColor3f(1f,1f,1f); if (keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT]) glo.glColor3f(1f,0f,0f);
		drawBoxPortion(glo,-movementField.width+120,-Configuration.bounds.height,	-movementField.width+150,	-Configuration.bounds.height+30,160,80,200,40,640,80);
		glo.glColor3f(1f,1f,1f); if (keys[KeyEvent.VK_SPACE]) glo.glColor3f(1f,0f,0f);
		drawBoxPortion(glo,-movementField.width+150,-Configuration.bounds.height,	-movementField.width+240,	-Configuration.bounds.height+30,200,80,320,40,640,80);
		glo.glColor3f(1f,1f,1f); if ((mouse[0]&1)==1 || (mouse[0]&2)==2) glo.glColor3f(1f,0f,0f);
		drawBoxPortion(glo,-movementField.width+240,-Configuration.bounds.height,	-movementField.width+270,	-Configuration.bounds.height+30,320,80,360,40,640,80);
		glo.glColor3f(1f,1f,1f); if ((mouse[0]&4)==4) glo.glColor3f(1f,0f,0f);
		drawBoxPortion(glo,-movementField.width+270,-Configuration.bounds.height,	-movementField.width+300,	-Configuration.bounds.height+30,360,80,400,40,640,80);
			
		inputDisplay.texture.disable(glo);
		glo.glDisable(GL2.GL_TEXTURE_2D);
		
		Configuration.gltr.beginRendering(Configuration.screen.width,Configuration.screen.height);
		Configuration.gltr.setColor(1f,1f,1f,1f);
		Configuration.gltr.draw(String.valueOf(score),Configuration.screen.width-160,0);
		Configuration.gltr.endRendering();
	}
	
	public void frame(long elapsed) {
		long currentTime = time;
		if (audioPlayer==null) return;
		if (!paused) {
			if (!audioPlayer.isPaused()) {
				currentTime = audioPlayer.getTime();
				time = currentTime;
			}
			else if (time<0 && !paused) {
				time += elapsed/1000000;
				if (time>=0) {
					audioPlayer.resume();
				}
			}
			else if (time>=0 && !paused && audioPlayer.isPaused()) {
				audioPlayer.resume();
			}
		}
		double dt = elapsed/1000000000.0;
		System.out.println(currentTime + "\t" + dt + "\t" + paused + "\t" + audioPlayer.isPaused());
		if (paused) return;
		
		// Keypresses
		double moveScale = 100;
		if (keys[KeyEvent.VK_SHIFT]) { moveScale = 250; }
		if (keys[KeyEvent.VK_UP]    || keys[KeyEvent.VK_W]) { player.moveUp(moveScale*dt); }
		if (keys[KeyEvent.VK_LEFT]  || keys[KeyEvent.VK_A]) { player.moveLeft(moveScale*dt); }
		if (keys[KeyEvent.VK_DOWN]  || keys[KeyEvent.VK_S]) { player.moveDown(moveScale*dt); }
		if (keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D]) { player.moveRight(moveScale*dt); }
		
		if (player.position.x<-movementField.width)  player.position.x = -movementField.width;
		if (player.position.x> movementField.width)  player.position.x = movementField.width;
		if (player.position.y<-movementField.height) player.position.y = -movementField.height;
		if (player.position.y> movementField.height) player.position.y = movementField.height;
		
		// Update
		// Player
		if (mouse[0]==0) player.shooting = false;
		else player.shooting = true;
		player.frame(dt);
		
		// Map variables
		EnemyEntity etemp;
		if (hitoffset<map.HitObjects.length && currentTime>map.HitObjects[hitoffset].time) {
			etemp = new EnemyEntity();
			etemp.position.set(map.HitObjects[hitoffset].x - appearField.width,appearField.height - map.HitObjects[hitoffset].y,0);
			etemp.size = hitSize;
			int color = map.Combos[map.HitObjects[hitoffset].combo];
			etemp.color[0] = ((color>>16) & 0xFF)/256f;
			etemp.color[1] = ((color>> 8) & 0xFF)/256f;
			etemp.color[2] = ((color>> 0) & 0xFF)/256f;
			enemies.add(etemp);
			etemp.explode(bullets,map.TimingPoints[timingoffset].bpm);
			hitoffset++;
		}
		if (currentTime>map.TimingPoints[timingoffset].time && timingoffset<map.TimingPoints.length-1) {
			timingoffset++;
		}
		
		double mspb = 60.0/map.TimingPoints[timingoffset].bpm*map.TimingPoints[timingoffset].meter;
		
		// Render entities
		int addscore;
		for (int i=0; i<enemies.size(); i++) {
			enemies.get(i).frame(dt);
			if (enemies.get(i).isPulse(mspb)) {
				enemies.get(i).explode(bullets,map.TimingPoints[timingoffset].bpm);
				//enemies.remove(i--);
			}
			if (mouse[0]>0 && enemies.get(i).isWithin(Configuration.mousePosition.x,Configuration.mousePosition.y)) {
				addscore = (int) (100 - player.shoot*50);
				if (addscore<0) addscore = 0;
				score += addscore;
				enemies.remove(i--);
			}
		}
		for (int i=0; i<bullets.size(); i++) {
			bullets.get(i).frame(dt);
			if (bullets.get(i).isOutside(Configuration.bounds)) {
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
	
	// Drawing
	protected void drawBox(GL2 glo,double sx,double sy,double ex,double ey) {
		glo.glBegin(GL2.GL_QUADS);
		glo.glTexCoord2d(0,0); glo.glVertex2d(sx,sy);
		glo.glTexCoord2d(0,1); glo.glVertex2d(sx,ey);
		glo.glTexCoord2d(1,1); glo.glVertex2d(ex,ey);
		glo.glTexCoord2d(1,0); glo.glVertex2d(ex,sy);
		glo.glEnd();
	}
	
	protected void drawBoxPortion(GL2 glo,double sx,double sy,double ex,double ey,double su,double sv,double eu,double ev) {
		glo.glBegin(GL2.GL_QUADS);
		glo.glTexCoord2d(su,sv); glo.glVertex2d(sx,sy);
		glo.glTexCoord2d(su,ev); glo.glVertex2d(sx,ey);
		glo.glTexCoord2d(eu,ev); glo.glVertex2d(ex,ey);
		glo.glTexCoord2d(eu,sv); glo.glVertex2d(ex,sy);
		glo.glEnd();
	}
	
	protected void drawBoxPortion(GL2 glo,double sx,double sy,double ex,double ey,int su,int sv,int eu,int ev,int width,int height) {
		drawBoxPortion(glo,sx,sy,ex,ey,(double) su/width,(double) sv/height,(double) eu/width,(double) ev/height);
	}
	
	public void createEnemy(double x,double y) {
		EnemyEntity newE = new EnemyEntity();
		newE.position.set(x,y);
		enemies.add(newE);
	}

	// Listeners
	public void handleKeyEvent(KeyEvent arg0) {
		if (arg0.getID()==KeyEvent.KEY_PRESSED && arg0.getKeyCode()==KeyEvent.VK_P) {
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
	
	public void handleMouseWheelEvent(MouseWheelEvent arg0) {
		
	}
	
	public void handleMouseEvent(MouseEvent arg0) {
		int cmouse = mouse[0];
		if (arg0.getID()==MouseEvent.MOUSE_PRESSED) {
			if (arg0.getButton()==MouseEvent.BUTTON1) mouse[0] |= 1;
			if (arg0.getButton()==MouseEvent.BUTTON2) mouse[0] |= 2;
			if (arg0.getButton()==MouseEvent.BUTTON3) mouse[0] |= 4;
			if (cmouse!=mouse[0]) { // Mouse state changed
				player.shoot = 0;
			}
		}
		else if (arg0.getID()==MouseEvent.MOUSE_RELEASED) {
			if (arg0.getButton()==MouseEvent.BUTTON1) mouse[0] &= ~1;
			if (arg0.getButton()==MouseEvent.BUTTON2) mouse[0] &= ~2;
			if (arg0.getButton()==MouseEvent.BUTTON3) mouse[0] &= ~4;
		}
	}
	
	// Listener interface methods
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		handleMouseWheelEvent(arg0);
	}
	
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void mousePressed(MouseEvent arg0) {
		handleMouseEvent(arg0);
	}
	
	public void mouseReleased(MouseEvent arg0) {
		handleMouseEvent(arg0);
	}
	
	public void keyPressed(KeyEvent arg0) {
		keys[arg0.getKeyCode()] = true;
		handleKeyEvent(arg0);
	}
	
	public void keyReleased(KeyEvent arg0) {
		keys[arg0.getKeyCode()] = false;
		handleKeyEvent(arg0);
	}
	
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
