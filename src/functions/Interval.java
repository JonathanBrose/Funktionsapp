package functions;
 
import static functions.Utilities.checkDouble;
/**
 * 
 * Class representing an interval
 * 
 * @author Nick Dressler, Jonathan Brose
 *
 */
public class Interval {
	
	private double left;
	private double right;

	/**
	 * 
	 * Creates an interval
	 * 
	 * @param left
	 *            left border
	 * @param right
	 *            right border
	 */

	public Interval(double left, double right) {
		if (right < left) {
			double c = left;
			left = right;
			right = c;
		}
		this.left = left;
		this.right = right;
	}

	/**
	 * Prints the interval in the following form:<br>
	 * <code>[left ; right]</code>
	 */
	@Override
	public String toString() {
		return "[" + checkDouble(left)+ " ; " + checkDouble(right) + "]";
	}

	/**
	 * Checks if the value is in the interval
	 * 
	 * @param x
	 *           value 
	 * @return true if the interval contains the value, otherwise false
	 */
	public boolean contains(double x) {
		return x >= left && x <= right;
	}

	
	public double getLeft() {
		return left;
	}

	public void setLeft(double left) {
		this.left = left;
	}

	public double getRight() {
		return right;
	}

	public void setRight(double right) {
		this.right = right;
	}

	
	


}
