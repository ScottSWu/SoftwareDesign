import java.awt.Dimension;
import javax.media.opengl.GL;


public class BulletEntity extends Entity {
	public static int TYPE_DEFAULT = 0;
	
	public Vector direction;
	private double[] circleArray = Geometry.getCircle(1,16,0);
	public int type = 0;
	public double size = 3;
	
	public BulletEntity() {
		super();
		direction = new Vector();
	}
	
	public void render(GL glo) {
		glo.glColor3f(1f,0f,0f);
		glo.glPushMatrix();
		glo.glTranslated(position.x,position.y,0);
		glo.glBegin(GL.GL_POLYGON);
		for (int i=0; i<circleArray.length; i+=2) {
			glo.glVertex2d(size*circleArray[i],size*circleArray[i+1]);
		}
		glo.glEnd();
		glo.glPopMatrix();
	}
	
	public void frame(double dt) {
		position.step(direction,dt);
	}
	
	public boolean isOutside(Dimension bounds) {
		return (position.x<-bounds.width || position.x>bounds.width || position.y<-bounds.height || position.y>bounds.height);
	}
	
	public boolean isNear(Entity e,double threshold) {
		return (e.position.x>position.x-threshold && e.position.x<position.x+threshold && e.position.y>position.y-threshold && e.position.y<position.y+threshold);
	}
}
