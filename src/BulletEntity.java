/**
 * BulletEntity extends Entity
 * 
 * Defines a bullet entity.
 * Each bullet has a given direction and color, specified when created by the EnemyEntity.
 */

import java.awt.Dimension;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;

public class BulletEntity extends Entity {
	
	// Direction and magnitude
	public Vector direction = new Vector();
	// Bullet size
	public double size = 5;
	// Appearance
	private double[] circleArray = Geometry.getCircle(1,16,0);
	public float[] color = {1f,0f,0f};
	
	/**
	 * Override Entity.render
	 */
	public void render(GL2 glo) {
		// Colored circle
		glo.glBegin(GL.GL_TRIANGLE_FAN);
		glo.glColor4f(color[0],color[1],color[2],1f);
		glo.glVertex2d(size*circleArray[0] + position.x,size*circleArray[1] + position.y);
		glo.glColor4f(color[0],color[1],color[2],0.5f);
		for (int i=0; i<circleArray.length; i+=2) {
			glo.glVertex2d(size*circleArray[i] + position.x,size*circleArray[i+1] + position.y);
		}
		glo.glEnd();
		
		// Black outline
		glo.glColor4f(0f,0f,0f,1f);
		glo.glBegin(GL.GL_LINE_LOOP);
		for (int i=0; i<circleArray.length; i+=2) {
			glo.glVertex2d(size*circleArray[i] + position.x,size*circleArray[i+1] + position.y);
		}
		glo.glEnd();
	}
	
	/**
	 * Override Entity.frame
	 */
	public void frame(double dt) {
		position.step(direction,dt);
	}
	
	/**
	 * Checks if the bullet is outside a bounds.
	 * 
	 * @param bounds	Dimension: The bounds to check
	 * @return	boolean: If bullet is outside the bounds
	 */
	public boolean isOutside(Dimension bounds) {
		return (position.x<-bounds.width || position.x>bounds.width || position.y<-bounds.height || position.y>bounds.height);
	}
	
	/**
	 * Checks if the bullet is near another entity.
	 * 
	 * @param e	Entity: The entity to check.
	 * @param threshold	double: Threshold for proximity
	 * @return	boolean: If bullet is near the entity
	 */
	public boolean isNear(Entity e,double threshold) {
		return (e.position.x>position.x-threshold && e.position.x<position.x+threshold && e.position.y>position.y-threshold && e.position.y<position.y+threshold);
	}
}
