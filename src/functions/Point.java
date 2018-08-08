package functions;
import static functions.Utilities.checkDouble;
import com.google.common.math.DoubleMath;
 
/**
 * Class representing a point with two dimensions
 * 
 * @author Nick Dressler, Jonathan Brose 
 *
 */
public class Point {
	/**
	 * Name of the Point
	 */
	private String name;
	/**
	 * X coordinate
	 */
	private double x; 
	/**
	 * Y coordinate
	 */
	private double y;

	/**
	 * Creates a Point
	 * 
	 * @param name
	 *           Name of the point
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 */
	public Point(String name, double x, double y) {
		this.name = name;
		this.x = x;
		this.y = y;
	}

	/**
	 * Creates a Point with the name "P"
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 */
	public Point(double x, double y) {
		this("P", x, y);
	}

	/**
	 * Prints the point with the form <code>[P] (x|y)</code>
	 */
	@Override
	public String toString() {
		return "[" + this.name + "] (" + checkDouble(x) + "|" + checkDouble(y) + ")";
	}

	/**
	 * Checks if the Point is equal with the parameter point
	 * 
	 * @param point
	 *            point to be compared
	 * @return true, if both points have equal  x- and y-values
	 * 
	 */
	public boolean isEqual(Point point) {
		if (DoubleMath.fuzzyEquals(getX(), point.getX(), 0.00001)) {
			if (DoubleMath.fuzzyEquals(getY(), point.getY(), 0.00001)) {
				return true;
			}
		}
		return false;
	}

	
	public void addX(double deltaX) {
		this.x += deltaX;
	}

	
	public void subtractX(int deltaX) {
		this.x -= deltaX;
	
	}

	public void addY(double deltaY) {
		this.y += deltaY;
	}

	public void subtractY(int deltaY) {
		this.y -= deltaY;
	}

	/**
	 * Calculates the distance betwwen this and the point parameter 
	 * 	 
	 * @param point
	 *            the second point
	 * @return distance
	 */
	public double getDistance(Point point) {
		double xDif, yDif;
	
		if (this.x > point.getX()) {
			xDif = this.x - point.getX();
		} else {
			xDif = point.getX() - this.x;
		}
	
		if (this.y > point.getY()) {
			yDif = this.y - point.getY();
		} else {
			yDif = point.getY() - this.y;
		}
	
		return Math.sqrt(Math.pow(xDif, 2) + Math.pow(yDif, 2));
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
