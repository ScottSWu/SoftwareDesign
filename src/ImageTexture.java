import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.media.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;


public class ImageTexture {
	public Texture texture;
	public String imageFile;
	public int width,height;
	
	public ImageTexture(String f) {
		imageFile = f;
	}
	
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
