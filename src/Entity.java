import javax.media.opengl.GL;

public abstract class Entity {
	public Vector position;
	public abstract void render(GL glo);
	public abstract void frame(double dt);
	
	public Entity() {
		position = new Vector();
	}
}
