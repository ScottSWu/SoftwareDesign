/**
 * ImageTexture
 * 
 * Defines a wrapper for an texture, defined by a file name.
 */

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.media.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class ImageTexture {
	
	// Texture object
	public Texture texture;
	// Image file path
	public String imageFile;
	// Metadata
	public int width,height;
	
	/**
	 * ImageTexture constructor
	 * 
	 * @param f	String: Image file.
	 */
	public ImageTexture(String f) {
		imageFile = f;
	}
	
	/**
	 * Load texture of image file into Texture object.
	 */
	public void loadTexture() {
		try {
			BufferedImage image = ImageIO.read(new File(imageFile));
			width = image.getWidth();
			height = image.getHeight();
			texture = AWTTextureIO.newTexture(Configuration.GLP,image,false);
		}
		catch (GLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
