import java.awt.Dimension;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;


public class BulletEntity extends Entity {
	public static int TYPE_DEFAULT = 0;
	
	public Vector direction;
	private double[] circleArray = Geometry.getCircle(1,16,0);
	public int type = 0;
	public double size = 5;
	public float[] color = {1f,0f,0f};
	
	public BulletEntity() {
		super();
		direction = new Vector();
	}
	
	public void render(GL2 glo) {
		glo.glBegin(GL.GL_TRIANGLE_FAN);
		glo.glColor4f(color[0],color[1],color[2],1f);
		glo.glVertex2d(size*circleArray[0] + position.x,size*circleArray[1] + position.y);
		glo.glColor4f(color[0],color[1],color[2],0.5f);
		for (int i=0; i<circleArray.length; i+=2) {
			glo.glVertex2d(size*circleArray[i] + position.x,size*circleArray[i+1] + position.y);
		}
		glo.glEnd();
		
		glo.glColor4f(0f,0f,0f,1f);
		glo.glBegin(GL.GL_LINE_LOOP);
		for (int i=0; i<circleArray.length; i+=2) {
			glo.glVertex2d(size*circleArray[i] + position.x,size*circleArray[i+1] + position.y);
		}
		glo.glEnd();
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
