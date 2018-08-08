package functions;
 
import java.util.ArrayList;


/**
 * Repesents a set of points, a polygon
 * 
 * @author Nick Dressler
 *
 */
public class Polygon {
	
	private int pointCount;
	
	private int storedPointCount;
	
	private Point[] points;

	private boolean sorted;

	
	public Polygon(int count, double epsilon) {
		this.points = new Point[count];
		this.pointCount = count;
		this.storedPointCount = 0;
	}


	public double getArcLength() {
		if(!sorted) sortByX();
		double length = 0;
		for (int i = 0; i < this.pointCount - 1; i++) {
			length = length + this.points[i].getDistance(this.points[i + 1]);
		}
		return length;
	}

	public void addPoint(Point point) {
		if(storedPointCount < pointCount) {
			this.points[this.storedPointCount] = point;
			this.storedPointCount++;
		}
		sorted=false;
	}
	

	public void sortByX() {
		Point c = null;
		for (int i = 1; i < points.length; i++) {
			for (int j = 0; j < points.length - i; j++) {
				if (points[j].getX() > points[j+1].getX()) {
					c = points[j];
					points[j] = points[j+1];
					points[j+1] = c;
				}
			}
		}
		sorted=true;
	
	}

}
