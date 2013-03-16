import java.io.File;
import java.io.IOException;
import javax.media.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;


public class ImageTexture {
	public Texture texture;
	public String imageFile;
	
	public ImageTexture(String f) {
		imageFile = f;
	}
	
	public void loadTexture() {
		try {
			texture = TextureIO.newTexture(new File(imageFile),false);
		}
		catch (GLException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
