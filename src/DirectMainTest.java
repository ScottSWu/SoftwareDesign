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
		Dimension fullSize = Toolkit.getDefaultToolkit().getScreenSize();
		if (FULLSCREEN) {
			screenSize = fullSize;
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
			window.setLocation((fullSize.width-adjustedSize.width)/2,(fullSize.height-adjustedSize.height)/2);
		}
		canvas = new RenderCanvas(window,screenSize);
		window.add(canvas);
		
		String songFolder,songMap;
		//songFolder = "50669 fripSide - only my railgun (TV Size)"; songMap = "fripSide - only my railgun (TV Size) (Kite) [Easy].osu";
		//songFolder = "66221 Suzuki Konomi - DAYS of DASH"; songMap = "Suzuki Konomi - DAYS of DASH (Rotte) [A32's Hard].osu";
		//songFolder = "43003 yanaginagi - Killer Song (Short Ver)"; songMap = "yanaginagi - Killer Song (Short Ver.) (terametis) [Insane].osu";
		//songFolder = "41823 The Quick Brown Fox - The Big Black"; songMap = "The Quick Brown Fox - The Big Black (Blue Dragon) [WHO'S AFRAID OF THE BIG BLACK].osu";
		//songFolder = "41686 Lily - Scarlet Rose"; songMap = "Lily - Scarlet Rose (val0108) [0108 style].osu";
		//songFolder = "13223 Demetori - Emotional Skyscraper ~ World's End"; songMap = "Demetori - Emotional Skyscraper ~ World's End (happy30) [Extra Stage].osu";
		//songFolder = "43003 yanaginagi - Killer Song (Short Ver)"; songMap = "yanaginagi - Killer Song (Short Ver.) (terametis) [Nloldmal].osu";
		//songFolder = "37980 An - TearVid"; songMap = "An - TearVid (Shiirn) [Another].osu";
		songFolder = "31373 SHIKI - BABYLON"; songMap = "SHIKI - BABYLON (miccoliasms) [Normal].osu";
		//songFolder = "45074 Black Hole - Pluto"; songMap = "Black Hole - Pluto (7odoa) [Expert].osu";
		canvas.loadGame(songFolder,songMap);
		
		animator = new FPSAnimator(canvas,120);
		animator.start();
		
		canvas.startGame();
	}
}
