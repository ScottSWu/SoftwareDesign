import java.awt.Dimension;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.glu.GLU;


public class DodgeCanvas extends GLCanvas implements GLEventListener {
	private static final long serialVersionUID = 1L;
	private GLU gluo;
	private Dimension screen;
	private Game game;
	
	public DodgeCanvas(Dimension dim) {
		this.addGLEventListener(this);
		this.setSize(dim);
		screen = dim;
		game = new Game(dim);
		this.addKeyListener(game);
		this.addMouseListener(game);
		this.addMouseMotionListener(game);
		this.addMouseWheelListener(game);
	}
	
	private long lastTime;
	public void display(GLAutoDrawable arg0) {
		GL glo = arg0.getGL();
		glo.glClear(GL.GL_COLOR_BUFFER_BIT);
		glo.glLoadIdentity();
		
		game.render(glo);
		long currentTime = System.nanoTime();
		game.frame(currentTime-lastTime);
		game.time += currentTime-lastTime;
		lastTime = currentTime;
	}
	
	public void displayChanged(GLAutoDrawable arg0,boolean arg1,boolean arg2) {
		//GL glo = arg0.getGL();
	}
	
	public void init(GLAutoDrawable arg0) {
		GL glo = arg0.getGL();
		gluo = new GLU();
		
		glo.glClearColor(0f,0f,0f,1f);
		glo.glShadeModel(GL.GL_SMOOTH);
		glo.glEnable(GL.GL_BLEND);
		//glo.glEnable(GL.GL_TEXTURE_2D);
		glo.glBlendFunc(GL.GL_SRC_ALPHA,GL.GL_ONE_MINUS_SRC_ALPHA);
		glo.glHint(GL.GL_POLYGON_SMOOTH_HINT,GL.GL_FASTEST);
		glo.glHint(GL.GL_LINE_SMOOTH_HINT,GL.GL_FASTEST);
		
		lastTime = System.nanoTime();
		game.start();
	}
	
	public void reshape(GLAutoDrawable arg0,int arg1,int arg2,int arg3,int arg4) {
		GL glo = arg0.getGL();
		glo.glMatrixMode(GL.GL_PROJECTION);
		glo.glLoadIdentity();
		gluo.gluOrtho2D(-arg3/2,arg3/2,-arg4/2,arg4/2);
		
		glo.glMatrixMode(GL.GL_MODELVIEW);
		glo.glLoadIdentity();
		glo.glViewport(0,0,arg3,arg4);
		
		screen.setSize(arg3,arg4);
	}
}
