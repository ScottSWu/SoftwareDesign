import java.awt.Cursor;
import java.awt.Dimension;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;

public class RenderCanvas extends GLCanvas implements GLEventListener {
	private GLU gluo;
	public Game game;
	
	public RenderCanvas(Dimension dim) {
		this.addGLEventListener(this);
		this.setSize(dim);
		Configuration.setConfiguration(dim);
		
		String songFolder,songMap;
		//songFolder = "50669 fripSide - only my railgun (TV Size)"; songMap = "fripSide - only my railgun (TV Size) (Kite) [Easy].osu";
		//songFolder = "66221 Suzuki Konomi - DAYS of DASH"; songMap = "Suzuki Konomi - DAYS of DASH (Rotte) [A32's Hard].osu";
		//songFolder = "43003 yanaginagi - Killer Song (Short Ver)"; songMap = "yanaginagi - Killer Song (Short Ver.) (terametis) [Insane].osu";
		//songFolder = "41823 The Quick Brown Fox - The Big Black"; songMap = "The Quick Brown Fox - The Big Black (Blue Dragon) [WHO'S AFRAID OF THE BIG BLACK].osu";
		//songFolder = "41686 Lily - Scarlet Rose"; songMap = "Lily - Scarlet Rose (val0108) [0108 style].osu";
		//songFolder = "13223 Demetori - Emotional Skyscraper ~ World's End"; songMap = "Demetori - Emotional Skyscraper ~ World's End (happy30) [Extra Stage].osu";
		//songFolder = "43003 yanaginagi - Killer Song (Short Ver)"; songMap = "yanaginagi - Killer Song (Short Ver.) (terametis) [Nloldmal].osu";
		songFolder = "37980 An - TearVid"; songMap = "An - TearVid (Shiirn) [Another].osu";
		//songFolder = "31373 SHIKI - BABYLON"; songMap = "SHIKI - BABYLON (miccoliasms) [Another].osu";
		//songFolder = "45074 Black Hole - Pluto"; songMap = "Black Hole - Pluto (7odoa) [Expert].osu";
		
		game = new Game();
		game.setMap(songFolder,songMap);
		
		this.addKeyListener(game);
		this.addMouseListener(game);
		this.addMouseMotionListener(game);
		this.addMouseWheelListener(game);
	}
	
	private long lastTime;
	public void display(GLAutoDrawable arg0) {
		GL2 glo = arg0.getGL().getGL2();
		glo.glClear(GL.GL_COLOR_BUFFER_BIT);
		glo.glLoadIdentity();
		long elapsed = System.nanoTime()-lastTime;
		game.render(glo);
		game.frame(elapsed);
		lastTime += elapsed;
	}
	
	public void displayChanged(GLAutoDrawable arg0,boolean arg1,boolean arg2) {
		GL2 glo = arg0.getGL().getGL2();
	}
	
	public void init(GLAutoDrawable arg0) {
		GL2 glo = arg0.getGL().getGL2();
		gluo = new GLU();
		
		glo.glClearColor(0f,0f,0f,1f);
		glo.glShadeModel(GL2.GL_SMOOTH);
		glo.glEnable(GL.GL_BLEND);
		//glo.glEnable(GL.GL_TEXTURE_2D);
		glo.glBlendFunc(GL2.GL_SRC_ALPHA,GL.GL_ONE_MINUS_SRC_ALPHA);
		glo.glHint(GL2.GL_POLYGON_SMOOTH_HINT,GL.GL_FASTEST);
		glo.glHint(GL2.GL_LINE_SMOOTH_HINT,GL.GL_FASTEST);
		
		lastTime = System.nanoTime();
		game.start();
	}
	
	public void reshape(GLAutoDrawable arg0,int arg1,int arg2,int arg3,int arg4) {
		GL2 glo = arg0.getGL().getGL2();
		glo.glMatrixMode(GL2.GL_PROJECTION);
		glo.glLoadIdentity();
		gluo.gluOrtho2D(-arg3/2,arg3/2,-arg4/2,arg4/2);
		
		glo.glMatrixMode(GL2.GL_MODELVIEW);
		glo.glLoadIdentity();
		glo.glViewport(0,0,arg3,arg4);
		
		Configuration.screen.setSize(arg3,arg4);
	}
	
	public void dispose(GLAutoDrawable arg0) {
		GL2 glo = arg0.getGL().getGL2();
	}
}
