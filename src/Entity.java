/**
 * Entity
 * 
 * Basis class for describing an entity.
 * Includes position, render method and framing method.
 */

import javax.media.opengl.GL2;

public abstract class Entity {
	public Vector position;
	public abstract void render(GL2 glo);
	public abstract void frame(double dt);
	
	public Entity() {
		position = new Vector();
	}
}
