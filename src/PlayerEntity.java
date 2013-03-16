import javax.media.opengl.GL2;

public class PlayerEntity extends Entity {
	private int[][] directions = {
		{90,0,45,135},
		{0,-90,-45,-135},
		{45,-45,0,0},
		{135,-135,-180,180}
	};
	public double
		direction = 0,
		moveDirection = 0,
		flash = 0,
		health = 1;
	public int hits = 0;
	private boolean
		movedLeft = false,
		movedRight = false,
		movedUp = false,
		movedDown = false;
	
	public PlayerEntity() {
		super();
	}
	
	public void render(GL2 glo) {
		if (flash>0) {
			float ch = (float) ((flash*8.0)%1.0);
			glo.glColor3f(ch,1f-ch,1f-ch);
		}
		else {
			glo.glColor3f(0.4f,0.8f,1f);
		}
		glo.glPushMatrix();
		glo.glTranslated(position.x,position.y,0);
		glo.glRotated(direction,0,0,1);
		glo.glBegin(GL2.GL_TRIANGLE_STRIP);
			glo.glColor3f(0f,0.5f,1f);
			glo.glVertex2d(-8,-6);
			glo.glColor3f(0f,0f,1f);
			glo.glVertex2d(0,10);
			glo.glColor3f(0f,0f,1f);
			glo.glVertex2d(0,-2);
			glo.glColor3f(0f,0.5f,1f);
			glo.glVertex2d(8,-6);
		glo.glEnd();
		glo.glColor3f(1f,1f,1f);
		glo.glBegin(GL2.GL_QUADS);
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
		
		int m1,m2;
		if (movedLeft) m1 = 0; else if (movedRight) m1 = 1; else if (movedUp) m1 = 2; else if (movedDown) m1 = 3; else m1 = -1;
		if (movedDown) m2 = 3; else if (movedUp) m2 = 2; else if (movedRight) m2 = 1; else if (movedLeft) m2 = 0; else m2 = -1;
		if (m1!=-1) {
			moveDirection = directions[m1][m2];
		}
		movedLeft = false;
		movedRight = false;
		movedUp = false;
		movedDown = false;
		
		health += elapsed/25;
		if (health>1) health = 1;
		double dirDiff = moveDirection-direction;
		if (dirDiff<-180) dirDiff += 360;
		if (dirDiff>180) dirDiff -= 360;
		//System.out.println(moveDirection + " " + direction + " " + dirDiff);
		if (dirDiff<-1) {
			direction -= elapsed*900;
			
		}
		else if (dirDiff>1) {
			direction += elapsed*900;
			
		}
		if (direction<-180) direction += 360;
		if (direction>180) direction -= 360;
	}
	
	public void hit() {
		hits++;
		health -= 0.1;
		flash = 1;
		
		if (health<0) health = 0;
	}
	
	public void moveLeft(double m) {
		position.x -= m;
		movedLeft = true;
	}
	
	public void moveRight(double m) {
		position.x += m;
		movedRight = true;
	}
	
	public void moveDown(double m) {
		position.y -= m;
		movedDown = true;
	}
	
	public void moveUp(double m) {
		position.y += m;
		movedUp = true;
	}
}
