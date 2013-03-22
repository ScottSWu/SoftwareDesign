import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.glu.GLU;
import com.jogamp.opengl.util.awt.TextRenderer;

public class RenderCanvas extends GLCanvas implements GLEventListener {
	private GLU gluo;
	public Game game;
	private ImageTexture cursor;
	private Container parent;
	private boolean startRequest = false;
	private TextRenderer gltr;
	
	public RenderCanvas(Container p,Dimension dim) {
		this.addGLEventListener(this);
		this.setSize(dim);
		Configuration.screen = new Dimension(dim.width,dim.height);
		Configuration.bounds = new Dimension(dim.width/2,dim.height/2);
		parent = p;
		
		this.addKeyListener(game);
		this.addMouseListener(game);
		this.addMouseMotionListener(game);
		this.addMouseWheelListener(game);
		
		//this.setCursor(Configuration.hiddenCursor);
		cursor = new ImageTexture("images/cursor.png");
	}
	
	public void loadGame(String fname,String mname) {
		game = new Game();
		this.addKeyListener(game);
		this.addMouseListener(game);
		this.addMouseMotionListener(game);
		this.addMouseWheelListener(game);
		game.setMap(fname,mname);
	}
	
	public void startGame() {
		startRequest = true;
	}
	
	public void endGame() {
		game.stop();
	}
	
	private long lastTime;
	public void display(GLAutoDrawable arg0) {
		GL2 glo = arg0.getGL().getGL2();
		glo.glClear(GL.GL_COLOR_BUFFER_BIT);
		glo.glLoadIdentity();
		long elapsed = System.nanoTime()-lastTime;
		
		if (startRequest) {
			game.start();
			startRequest = false;
			return;
		}
		
		// Mouse position
		Point mpos = MouseInfo.getPointerInfo().getLocation();
		Configuration.mousePosition.x = mpos.x - this.getLocationOnScreen().x - Configuration.bounds.width;
		Configuration.mousePosition.y = Configuration.bounds.height - mpos.y + this.getLocationOnScreen().y;
		
		game.frame(elapsed);
		if (game.isStarted()) game.render(glo);
		
		// Cursor
		cursor.texture.bind(glo);
		cursor.texture.enable(glo);
		glo.glColor3f(1f,1f,1f);
		glo.glEnable(GL2.GL_TEXTURE_2D);
		glo.glBegin(GL2.GL_QUADS);
		glo.glTexCoord2d(0,0); glo.glVertex2d(Configuration.mousePosition.x-cursor.width/2,Configuration.mousePosition.y-cursor.height/2);
		glo.glTexCoord2d(0,1); glo.glVertex2d(Configuration.mousePosition.x-cursor.width/2,Configuration.mousePosition.y+cursor.height/2);
		glo.glTexCoord2d(1,1); glo.glVertex2d(Configuration.mousePosition.x+cursor.width/2,Configuration.mousePosition.y+cursor.height/2);
		glo.glTexCoord2d(1,0); glo.glVertex2d(Configuration.mousePosition.x+cursor.width/2,Configuration.mousePosition.y-cursor.height/2);
		glo.glEnd();
		glo.glDisable(GL2.GL_TEXTURE_2D);
		cursor.texture.disable(glo);
		
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
		
		//Configuration.gltr = new TextRenderer(new Font("Arial",Font.BOLD,24));
		
		cursor.loadTexture();
		
		lastTime = System.nanoTime();
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
