/**
 * Vector
 * 
 * Provides structure for a vector.
 */

public class Vector {
	public double x,y,z;
	
	public Vector() {
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Vector(double arg0,double arg1) {
		x = arg0;
		y = arg1;
		z = 0;
	}
	
	public Vector(double arg0,double arg1,double arg2) {
		x = arg0;
		y = arg1;
		z = arg2;
	}
	
	public void set(double arg0,double arg1) {
		x = arg0;
		y = arg1;
	}
	
	public void set(double arg0,double arg1,double arg2) {
		x = arg0;
		y = arg1;
		z = arg2;
	}
	
	public void step(Vector dir,double dt) {
		x += dir.x*dt;
		y += dir.y*dt;
		z += dir.z*dt;
	}
}
