import java.util.ArrayList;
import javax.media.opengl.GL;


public class EnemyEntity extends Entity {
	private double[] circleArray = Geometry.getCircle(1,32,0);
	public double size;
	public float[] color = {1f,1f,1f};
	public double pulse;
	
	public EnemyEntity() {
		super();
		size = 1;
		pulse = 0;
	}
	
	public void render(GL glo) {
		glo.glColor3f(1f,1f,1f);
		glo.glPushMatrix();
		glo.glTranslated(position.x,position.y,0);
		glo.glBegin(GL.GL_POLYGON);
		for (int i=0; i<circleArray.length; i+=2) {
			glo.glVertex2d(size*circleArray[i],size*circleArray[i+1]);
		}
		glo.glEnd();
		glo.glColor3fv(color,0);
		glo.glScaled(0.9,0.9,0.9);
		glo.glBegin(GL.GL_POLYGON);
		for (int i=0; i<circleArray.length; i+=2) {
			glo.glVertex2dv(circleArray,i);
		}
		glo.glEnd();
		glo.glPopMatrix();
	}
	
	public void frame(double dt) {
		position.z += dt;
	}
	
	public boolean isDone() {
		return (position.z>0);
	}
	
	public void explode(ArrayList<BulletEntity> bullets) {
		BulletEntity bt;
		for (int i=0; i<circleArray.length; i+=2) {
			bt = new BulletEntity();
			bt.position.set(position.x + circleArray[i]*size,position.y + circleArray[i+1]*size);
			bt.direction.set(circleArray[i]*(100),circleArray[i+1]*(100));
			bullets.add(bt);
		}
	}
}
