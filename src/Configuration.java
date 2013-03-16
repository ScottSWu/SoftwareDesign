import java.awt.Dimension;


public class Configuration {
	public static final int PLACE_MENU = 1;
	public static final int PLACE_OPTIONS = 11;
	public static final int PLACE_SELECT = 21;
	public static final int PLACE_INSTRUCTION = 31;
	public static final int PLACE_GAME = 101;
	public static final int PLACE_EXIT = -1;
	
	public static Dimension screen;
	public static Dimension bounds;
	public static void setConfiguration(Dimension dim) {
		screen = new Dimension(dim.width,dim.height);
		bounds = new Dimension(dim.width/2,dim.height/2);
	}
}
