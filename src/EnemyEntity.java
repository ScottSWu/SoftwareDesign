import java.util.ArrayList;
import javax.media.opengl.GL2;


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
	
	public void render(GL2 glo) {
		glo.glColor3f(1f,1f,1f);
		glo.glBegin(GL2.GL_TRIANGLE_FAN);
		glo.glVertex2d(size*circleArray[0] + position.x,size*circleArray[1] + position.y);
		for (int i=0; i<circleArray.length; i+=2) {
			glo.glVertex2d(size*circleArray[i] + position.x,size*circleArray[i+1] + position.y);
		}
		glo.glEnd();
		
		glo.glColor3fv(color,0);
		glo.glBegin(GL2.GL_TRIANGLE_FAN);
		glo.glVertex2d(0.9*size*circleArray[0] + position.x,0.9*size*circleArray[1] + position.y);
		for (int i=0; i<circleArray.length; i+=2) {
			glo.glVertex2d(0.9*size*circleArray[i] + position.x,0.9*size*circleArray[i+1] + position.y);
		}
		glo.glEnd();
		
		glo.glColor3f(0f,0f,0f);
		glo.glBegin(GL2.GL_LINE_LOOP);
		for (int i=0; i<circleArray.length; i+=2) {
			glo.glVertex2d(size*circleArray[i] + position.x,size*circleArray[i+1] + position.y);
		}
		glo.glEnd();
	}
	
	public void frame(double dt) {
		position.z += dt;
	}
	
	public boolean isDone() {
		return (position.z>0);
	}
	
	public void explode(ArrayList<BulletEntity> bullets,double bpm) {
		BulletEntity bt;
		double speed = Math.sqrt(bpm*25);
		for (int i=0; i<circleArray.length; i+=4) {
			bt = new BulletEntity();
			bt.position.set(position.x + circleArray[i]*size,position.y + circleArray[i+1]*size);
			bt.direction.set(circleArray[i]*(speed),circleArray[i+1]*(speed));
			bt.color = color;
			bullets.add(bt);
		}
	}
}
