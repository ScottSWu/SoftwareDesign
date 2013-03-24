/**
 * Configuration
 * 
 * Defines and contains global static variables.
 */

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.media.opengl.GLProfile;
import com.jogamp.opengl.util.awt.TextRenderer;

public class Configuration {
	
	// Graphical
	public static Dimension screen =  new Dimension();
	public static Dimension bounds =  new Dimension();
	public static Dimension screenOffset =  new Dimension();
	public static Point mousePosition =  new Point();
	public static Cursor hiddenCursor;
	public static final GLProfile GLP = GLProfile.getDefault();
	public static TextRenderer gltr = new TextRenderer(new Font("Arial",Font.BOLD,24));
	
	// Audio
	public static float audioVolume = 0f;
	
	// Initialization
	static {
		hiddenCursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1,1,BufferedImage.TYPE_4BYTE_ABGR),new Point(0,0),"hidden");
	}
}
