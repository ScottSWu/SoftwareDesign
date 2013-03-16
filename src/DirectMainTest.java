import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import com.jogamp.opengl.util.FPSAnimator;


public class DirectMainTest {
	// Options
	private static boolean FULLSCREEN = false;
	private static int WIDTH = 800;
	private static int HEIGHT = 600;
	
	// Variables
	private static JFrame window;
	private static RenderCanvas canvas;
	private static Dimension screenSize;
	private static FPSAnimator animator;
	
	public static void main(String args[]) {
		window = new JFrame("DirectMainTest");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if (FULLSCREEN) {
			screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			window.setResizable(false);
			window.setUndecorated(true);
			window.setSize(screenSize);
		}
		else {
			screenSize = new Dimension(WIDTH,HEIGHT);
			window.setSize(screenSize);
			window.setVisible(true);
			Dimension actualSize = window.getContentPane().getSize();
			Dimension adjustedSize = new Dimension(2*screenSize.width - actualSize.width,2*screenSize.height - actualSize.height);
			window.setSize(adjustedSize);
		}
		canvas = new RenderCanvas(screenSize);
		window.add(canvas);
		
		animator = new FPSAnimator(canvas,120);
		animator.start();
	}
}
