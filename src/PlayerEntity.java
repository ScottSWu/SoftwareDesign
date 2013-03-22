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
		shootDirection = 0,
		shoot = 0,
		flash = 0,
		health = 1;
	public int hits = 0;
	private boolean
		movedLeft = false,
		movedRight = false,
		movedUp = false,
		movedDown = false;
	public boolean shooting = false;
	
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
		glo.glPushMatrix();
		glo.glRotated(direction,0,0,1);
		
		// Player
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
		
		// Hit point
		glo.glColor3f(1f,1f,1f);
		glo.glBegin(GL2.GL_QUADS);
			glo.glVertex2d(-1,-1);
			glo.glVertex2d(-1, 1);
			glo.glVertex2d( 1, 1);
			glo.glVertex2d( 1,-1);
		glo.glEnd();
		glo.glPopMatrix();
		
		glo.glRotated(shootDirection,0,0,1);
		// Laser
		if (shoot>0) {
			double cx = position.x-Configuration.mousePosition.x;
			double cy = position.y-Configuration.mousePosition.y;
			double laserDistance = Math.sqrt(cx*cx + cy*cy) - 8;
			float laserColor = (float) shoot/0.25f;
			if (laserColor>1f) laserColor = 0f; else laserColor = 1f - laserColor;
			glo.glBegin(GL2.GL_QUADS);
				glo.glColor4f(laserColor,laserColor,1f,1f);
				glo.glVertex2d(-1,8);
				glo.glVertex2d( 1,8);
				glo.glColor4f(laserColor,laserColor,1f,0.2f);
				glo.glVertex2d( 1,laserDistance/2);
				glo.glVertex2d(-1,laserDistance/2);
				
				glo.glVertex2d(-1,laserDistance/2);
				glo.glVertex2d( 1,laserDistance/2);
				glo.glColor4f(laserColor,laserColor,1f,1f);
				glo.glVertex2d( 1,laserDistance);
				glo.glVertex2d(-1,laserDistance);
			glo.glEnd();
		}
		
		glo.glPopMatrix();
		glo.glPopMatrix();
	}
	
	public void frame(double elapsed) {
		// Hit flash
		if (flash>0) {
			flash -= elapsed;
		}
		
		// Health
		health += elapsed/25;
		if (health>1) health = 1;
		
		// Shooting
		if (shooting) {
			shoot += elapsed;
		}
		else {
			shoot = 0;
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
		
		double dirDiff = moveDirection-direction;
		if (dirDiff<-180) dirDiff += 360;
		if (dirDiff>180) dirDiff -= 360;
		
		if (dirDiff<-1) {
			direction -= elapsed*900;
			
		}
		else if (dirDiff>1) {
			direction += elapsed*900;
			
		}
		if (direction<-180) direction += 360;
		if (direction>180) direction -= 360;
		
		shootDirection = Math.atan2(Configuration.mousePosition.y - position.y,Configuration.mousePosition.x - position.x)/Math.PI*180 - 90;
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
