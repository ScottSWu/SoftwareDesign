
public class Geometry {
	public static double[] circle;
	public static double[] square;
	
	static {
		circle = getCircle(1,32,0);
		square = getCircle(1,4,Math.PI/4);
	}
	
	public static double[] getCircle(double radius,int splits,double offset) {
		double[] coords = new double[splits*2+2];
		int ind = 0;
		
		for (int i=0; i<splits+1; i++) {
			coords[ind++] = radius*Math.cos(2.0*Math.PI*i/splits+offset);
			coords[ind++] = radius*Math.sin(2.0*Math.PI*i/splits+offset);
		}
		return coords;
	}
	
	
}
