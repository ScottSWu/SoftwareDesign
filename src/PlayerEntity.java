import javax.media.opengl.GL;


public class PlayerEntity extends Entity {
	private double[] circleArray = Geometry.getCircle(8,16,0);
	public double flash = 0;
	public int hits;
	
	public PlayerEntity() {
		super();
	}
	
	public void render(GL glo) {
		if (flash>0) {
			float ch = (float) ((flash*8.0)%1.0);
			glo.glColor3f(ch,1f-ch,1f-ch);
		}
		else {
			glo.glColor3f(0.4f,0.8f,1f);
		}
		glo.glPushMatrix();
		glo.glTranslated(position.x,position.y,0);
		glo.glBegin(GL.GL_POLYGON);
		for (int i=0; i<circleArray.length; i+=2) {
			glo.glVertex2dv(circleArray,i);
		}
		glo.glEnd();
		glo.glColor3f(1f,1f,1f);
		glo.glBegin(GL.GL_QUADS);
			glo.glVertex2d(-1,-1);
			glo.glVertex2d(-1, 1);
			glo.glVertex2d( 1, 1);
			glo.glVertex2d( 1,-1);
		glo.glEnd();
		glo.glPopMatrix();
	}
	
	public void frame(double elapsed) {
		if (flash>0) {
			flash -= elapsed;
		}
	}
	
	public void hit() {
		hits++;
		flash = 1;
	}
}
