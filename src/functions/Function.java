package functions;
 
import static functions.Utilities.round;
import static functions.Simplification.HochpunktName;
import static functions.Simplification.NullpunktName;
import static functions.Simplification.SattelpunktName;
import static functions.Simplification.TiefpunktName;
import static functions.Simplification.WendepunktName;
import static functions.Simplification.simplify;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.RoundingMode;
import java.util.ArrayList;

import com.google.common.math.DoubleMath;

/**
 * 
 * Class representing a function <code>f(x)</code> with 
 * {@link FunctionPart}s<br>
 * <br>
 * 
 * <b>For Example:</b><br>
 * <ul>
 * <li>Funktion.functionParts[{@link Sin}(4), {@link PowerFunction}(3,4),
 * {@link ExponentialFunction}(2,5)]
 * </ul><br>
 * <b>toString:</b><br>
 * <ul>
 * <li><code>"4sin(x)+ 3x<sup>4</sup>+ 2*5<sup>x</sup></code><br>
 * </ul>
 * <br>
 * 
 * Features:
 * <ul>
 * <li>build derivative</li>
 * <li>build antiderivative</li>
 * <li>calculate extreme points, turning points &amp; more</li>
 * <li>calculate function value</li>
 * <li>calculate zeros (see {@link #newtonMethod(Interval, double, Function)})</li>
 * <li>calculate rotation volume</li>
 * <li>calculate surface under the curve</li>
 * <li>calculate mean</li>
 * <li>calculate arc length</li>
 * </ul>
 * <br>
 * All costy methods do have an Thread.interrupt() check implemented<br>
 * This check stops the operation and sets the interrupt flag again.
 * 
 * 
 * @version 2.0 {@docRoot}
 * 
 * @author Jonathan Brose, Nick Dressler 
 *
 */

public class Function implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7642955298549937330L;
	/**
	 * variable Name used to calculate the function
	 * Z.B.: x -&gt; <code>f(x)= 2*x -1</code> 
	 */
	private String variableName = "x";
	/**
	 * Name of the function <code>funktionsName(x) = ... </code> <br>
	 * For example: funktionsName = "f" -&gt; <code> f(x)= ... </code>
	 */
	private String functionName = "f";
	/**
	 * Stores the {@link FunctionPart}s of the function.
	 */
	private ArrayList<FunctionPart> functionParts;
	/**
	 * Step in which  {@link #calculateExtremePoints(Interval)}, 
	 * {@link #calculateMaximumPoints(Interval)}, {@link #calculateSaddlePoints(Interval)}, {@link #calculateMinimumPoints(Interval)}
	 * and {@link #newtonMethod(Interval, double, Function)} iterate trough the function values.
	 * If step is to great it can happen that a extreme point or a zero is not recognized<br>
	 * Normally the predefined value works fine.
	 */
	private double step = 0.01;
	/**
	 * The tolerance of all calculations. <br>
	 * A smaller Tolerance is better, but it takes a lot longer to calculate.
	 *
	 *@see #newtonMethod(Interval, double, Function)
	 *@see #calculateArcLength(Interval)
	 *@see #calculateDistance(double, double)
	 *@see #calculateUppersum(Interval)
	 */
	private double epsilon = 0.000001;

	/**
	 * Creates a {@link Function} <code>f(x) = 0</code>
	 */
	public Function() {
		this("f", "x", new PowerFunction(0, 0));

	}

	/**
	 * Creates a {@link Function}
	 * 
	 * @param functionName
	 *            name of the function for example "f"
	 * @param variableName
	 *            name of the variable for example "x"
	 * @param functionParts
	 *            {@link FunctionPart}s as ArrayList
	 */
	public Function(String functionName, String variableName, ArrayList<FunctionPart> functionParts) {
		this.variableName = variableName;
		this.functionName = functionName;
		this.functionParts = functionParts;
		simplify(this.functionParts);
		simplify(this.functionParts);
	}

	/**
	 * Creates a {@link Function}
	 * 
	 * @param functionName
	 *            name of the function for example "f"
	 * @param variableName
	 *            name of the variable for example "x"
	 * @param functionParts
	 *            {@link FunctionPart}s as Array
	 */
	public Function(String functionName, String variableName, FunctionPart... functionParts) {
		this.variableName = variableName;
		this.functionName = functionName;
		this.functionParts = new ArrayList<FunctionPart>();

		for (int i = 0; i < functionParts.length; i++) {
			this.functionParts.add(functionParts[i]);
		}
		simplify(this.functionParts);
		simplify(this.functionParts);
	}

	/**
	 * Creates a {@link Function} <code>f(x)</code>
	 * @param functionParts
	 *            {@link FunctionPart}s as ArrayList
	 */
	public Function(ArrayList<FunctionPart> functionParts) {
		this("f","x",functionParts);
	}

	/**
	 * Creates a {@link Function} <code>f(x)</code>
	 * @param functionParts
	 *            {@link FunctionPart}s as Array
	 */
	public Function(FunctionPart... functionParts) {
		this("f","x",functionParts);
	}

	/**
	 *  Returns the function term as String
	 * @return
	 *  	The function term as String
	 */
	@Override
	public String toString() {
		String s = "";
		s += functionName + "(" + variableName + ") =";
		for (FunctionPart functionPart : functionParts) {
			s += " " + functionPart.getString(functionParts.indexOf(functionPart) * -1, variableName);
		}
		return s.replaceAll("\\*", "\u2219");
	}

	/**
	 * Calculates the first derivative.
	 * 
	 * @return Calculates the first derivative.
	 */
	public Function getDerivative() {
		ArrayList<FunctionPart> dieAbleitungsListe = new ArrayList<FunctionPart>();
		try {
			for (FunctionPart dasAbleitungsFunktionsteil : functionParts) {

				dieAbleitungsListe.add(dasAbleitungsFunktionsteil.getDerivative());
			}
		} catch (Exception e) {
			return null;
		}
		return new Function(functionName + "\'", variableName, dieAbleitungsListe);

	}

	/**
	 * calculates the n-th derivative.
	 * 
	 * @param n count of the derivative
	 * @return the n-th derivative.
	 */
	public Function getDerivative(int n) {

		Function derivative = this;
		for (int i = 0; i < n; i++) {
			if (derivative == null)
				return null;
			derivative = derivative.getDerivative();
		}
		return derivative;

	}

	/**
	 * Calculates the antiderivative, if the function is not to complex.
	 * 
	 * @return antiderivative<br>
	 *         the Y-offset C always is 0!
	 *         <br>if the function is to complex returns null.
	 * 
	 */
	public Function getAntiderivative() {
		ArrayList<FunctionPart> antiDerivativeParts = new ArrayList<FunctionPart>();
		for (FunctionPart functionPart : functionParts) {
			if (functionPart == null)
				return null;
			else if (functionPart.getAntiderivative() == null)
				return null;
			antiDerivativeParts.add(functionPart.getAntiderivative());
		}
		String newFunctionName = this.functionName;
		if (newFunctionName.endsWith("\'")) {
			char[] c = newFunctionName.toCharArray();
			char[] c2 = new char[newFunctionName.length() - 1];
			for (int i = 0; i < c2.length; i++) {
				c2[i] = c[i];
			}
			newFunctionName = new String(c2);
	
		} else {
			newFunctionName = newFunctionName.toUpperCase();
	
		}
		return new Function(newFunctionName, variableName, antiDerivativeParts);
	
	}

	/**
	 * 
	 * Calculates the arc length in the given {@link Interval}
	 * <br><br>can be stopped trough Thread.interrupt()
	 * @param interval
	 *            {@link Interval} in wich the arc length gets calculated
	 * @return arc length in interval 
	 */

	public double calculateArcLength(Interval interval) {
		Polygon polygon;
		double epsilon = this.epsilon*100;
		double start, end, length, oldLength, step;
		int count = 10;

		start = interval.getLeft();
		end = interval.getRight();
		length = 0;
		oldLength = -1;
		do {
			step = start;
			polygon = new Polygon(count, epsilon);
			double difference = (double) Math.abs(end - start) / count;

			for (int i = 0; i < count; i++) {
				if(Thread.interrupted()) {
					Thread.currentThread().interrupt();
					return 0;
				}
				polygon.addPoint(new Point(step, this.calculateFunctionValue(step)));
				step += difference;
			}
			oldLength = length;
			length = polygon.getArcLength();
			count++;
		} while (Math.abs(oldLength - length) > epsilon);
		return length;
	}

	/**
	 * 
	 * Calculates the integral in the given interval
	 * <br><br>can be stopped trough Thread.interrupt()
	 * @param interval
	 *            interval
	 * @return Integral in interval
	 */
	public double calculateIntegral(Interval interval) {

		Function antiderivative = this.getAntiderivative();
		if (antiderivative == null)
			return calculateUppersum(interval);

		return calculateIntegral(antiderivative, interval);
	}

	/**
	 * Calculates the mean in the given interval
	 * <br><br>can be stopped trough Thread.interrupt()
	 * @param derIntervall
	 *            Interval
	 * @return mean in interval (integral mean)
	 */
	public double calculateIntegralMean(Interval derIntervall) {

		return (1 / (derIntervall.getRight() - derIntervall.getLeft())) * calculateIntegral(derIntervall);
	}

	/**
	 * 
	 * Calculates the surface between the curve and the x- axis   
	 *<br><br>can be stopped trough Thread.interrupt()
	 * @param interval
	 *          Interval
	 * 
	 * @return <p>surface (always &gt; 0) because absolute.</p>
	 * 
	 */
	public double calculateSurface(Interval interval) {
		return calculateSurface(interval, this);
	}

	/**
	 * 
	 * Calculates the surface between the curve and the x- axis   
	 *<br><br>can be stopped trough Thread.interrupt()
	 * @param interval
	 *          Interval
	 * @param function
	 * 		function for which the surface should be calculated
	 * @return <p>surface (always &gt; 0) because absolute.</p>
	 * 
	 */
	public double calculateSurface(Interval interval, Function function) {

		double surface = 0.0D;
		ArrayList<Double> zeros = this.calculateZerosDouble(interval);
		Function antiderivative = function.getAntiderivative();

		if (antiderivative == null) {

			if (zeros.size() == 0) {
				surface += Math.abs(calculateUppersum(interval));

			} else if (zeros.size() > 0) {

				Interval[] intervals = new Interval[zeros.size() + 1];

				for (int j = 0; j < intervals.length; j++) {
					if(Thread.interrupted()) {
						Thread.currentThread().interrupt();
						return 0;
					}
					if (j == 0)
						intervals[j] = new Interval(interval.getLeft(), zeros.get(j));

					else if (j == intervals.length - 1)
						intervals[j] = new Interval(zeros.get(j - 1), interval.getRight());

					else
						intervals[j] = new Interval(zeros.get(j - 1), zeros.get(j));
				}

				for (Interval aInterval : intervals) {
					if(Thread.interrupted()) {
						Thread.currentThread().interrupt();
						return 0;
					}
					surface += Math.abs(calculateUppersum(aInterval));

				}
			}
		} else {

			if (zeros.size() == 0) {
				surface += Math.abs(calculateIntegral(antiderivative, interval));

			} else if (zeros.size() > 0) {

				Interval[] intervals = new Interval[zeros.size() + 1];

				for (int j = 0; j < intervals.length; j++) {
					if(Thread.interrupted()) {
						Thread.currentThread().interrupt();
						return 0;
					}
					if (j == 0)
						intervals[j] = new Interval(interval.getLeft(), zeros.get(j));

					else if (j == intervals.length - 1)
						intervals[j] = new Interval(zeros.get(j - 1), interval.getRight());

					else
						intervals[j] = new Interval(zeros.get(j - 1), zeros.get(j));
				}

				for (Interval aInterval : intervals) {
					if(Thread.interrupted()) {
						Thread.currentThread().interrupt();
						return 0;
					}
					surface += Math.abs(calculateIntegral(antiderivative, aInterval));

				}
			}
		}
		return surface;
	}

	/**
	 * 
	 * Calculates the rotation volume of the function in the given interval.
	 * 
	 * *<br><br>can be stopped trough Thread.interrupt()
	 * 
	 * @param interval interval defining the borders of the volume
	 * @return rotation volume
	 * 
	 * 
	 */

	public double calculateRotationVolume(Interval interval) {
		FunctionPart[] functionParts = new FunctionPart[this.functionParts.size()];
		for (int i = 0; i < this.functionParts.size(); i++) {
			if(Thread.interrupted()) {
				Thread.currentThread().interrupt();
				return 0;
			}
			functionParts[i] = this.functionParts.get(i).clone();
		}
		AdditionNexus additionNexus = new AdditionNexus(functionParts);
		return Math.PI * calculateSurface(interval, new Function(new Chaining(new PowerFunction(1, 2), additionNexus)));

	}

	/**
	 * 
	 * Calculates the maximum and minimum points
	 *
	 *<br><br>can be stopped trough Thread.interrupt()
	 * @param interval
	 *            interval to be searched in.
	 * @return maximum and minimum points.
	 * @see Point
	 * @see ArrayList
	 *      
	 *
	 **/
	public ArrayList<Point> calculateExtremePoints(Interval interval) {
		Function derivative = getDerivative();
		ArrayList<Double> potentialXValues = newtonMethod(interval, epsilon, derivative);
		ArrayList<Point> extremPoints = new ArrayList<Point>();
		for (Double x : potentialXValues) {
			if(Thread.interrupted()) {
				Thread.currentThread().interrupt();
				return new ArrayList<Point>();
			}
			
			if (derivative.calculateFunctionValue(x - step) >= 0
					&& derivative.calculateFunctionValue(x + step) < 0) {
	
				extremPoints.add(new Point(HochpunktName, x, calculateFunctionValue(x)));
	
			} else if (derivative.calculateFunctionValue(x - step) < 0
					&& derivative.calculateFunctionValue(x + step) >= 0) {
	
				extremPoints.add(new Point(TiefpunktName, x, calculateFunctionValue(x)));
	
			}
		}
	
		return checkPeriod(extremPoints);
	
	}

	/**
	 * 
	 * calculates the saddle points of the function
	 * <br><br>can be stopped trough Thread.interrupt()
	 * 
	 * @param interval,
	 *          interval in which the method checks for saddle points.
	 * @return saddle points
	 * @see Point
	 * @see ArrayList
	 *      
	 *
	 **/
	public ArrayList<Point> calculateSaddlePoints(Interval interval) {
		Function derivative = getDerivative();
		Function derivative2 = derivative.getDerivative();
		Function derivative3 = derivative2.getDerivative();
	
		ArrayList<Double> potentialXValues = newtonMethod(interval, epsilon, derivative2);
		ArrayList<Point> saddlePoints = new ArrayList<Point>();
	
		for (Double x : potentialXValues) {
			if(Thread.interrupted()) {
				Thread.currentThread().interrupt();
				return new ArrayList<Point>();
			}
			// f''(x) = 0
			if ((derivative2.calculateFunctionValue(x - step) >= 0
					&& derivative2.calculateFunctionValue(x + step) < 0)
					|| (derivative2.calculateFunctionValue(x - step) < 0
							&& derivative2.calculateFunctionValue(x + step) >= 0)) {
	
				// f'''(x) != 0
				if (derivative3.calculateFunctionValue(x) != 0.0D) {
					if (derivative.calculateFunctionValue(x) == 0) {
						saddlePoints.add(new Point(SattelpunktName, x, calculateFunctionValue(x)));
	
					}
				}
	
			}
		}
	
		return checkPeriod(saddlePoints);
	
	}

	/**
	 * Calculates the minimum points of the function
	 *<br><br>can be stopped trough Thread.interrupt()
	 * 
	 * @param interval
	 *           interval in which the method checks for minimum points
	 * @return minimum points
	 * @see Point
	 * @see ArrayList
	 *      
	 *
	 **/
	public ArrayList<Point> calculateMinimumPoints(Interval interval) {
		Function derivative = getDerivative();
		ArrayList<Double> potentialXValues = newtonMethod(interval, epsilon, derivative);
		ArrayList<Point> minimumPoints = new ArrayList<Point>();
		for (Double x : potentialXValues) {
			if(Thread.interrupted()) {
				Thread.currentThread().interrupt();
				return new ArrayList<Point>();
			}
			if (derivative.calculateFunctionValue(x - step) < 0
					&& derivative.calculateFunctionValue(x + step) >= 0) {
	
				minimumPoints.add(new Point(TiefpunktName, x, calculateFunctionValue(x)));
	
			}
		}
		return checkPeriod(minimumPoints);
	
	}

	/**
	 * Calculates the maximum points of the function
	 *<br><br>can be stopped trough Thread.interrupt()
	 * 
	 * @param interval
	 *           interval in which the method checks for minimum points
	 * @return maximum points
	 * @see Point
	 * @see ArrayList
	 *      
	 *
	 **/
	public ArrayList<Point> calculateMaximumPoints(Interval interval) {
		Function derivative = getDerivative();
		ArrayList<Double> potentialXValues = newtonMethod(interval, epsilon, derivative);
		ArrayList<Point> extremPoints = new ArrayList<Point>();
		for (Double x : potentialXValues) {
			if(Thread.interrupted()) {
				Thread.currentThread().interrupt();
				return new ArrayList<Point>();
			}
			if (derivative.calculateFunctionValue(x - step) >= 0
					&& derivative.calculateFunctionValue(x + step) < 0) {
	
				extremPoints.add(new Point(HochpunktName, x, calculateFunctionValue(x)));
			}
		}
		return checkPeriod(extremPoints);
	
	}

	/**
	 * Calculates the turning points of the function
	 *<br><br>can be stopped trough Thread.interrupt()
	 * 
	 * @param interval
	 *           interval in which the method checks for minimum points
	 * @return turning points
	 * @see Point
	 * @see ArrayList
	 *      
	 *
	 **/
	public ArrayList<Point> calculateTurningPoints(Interval interval) {
		Function derivative = getDerivative().getDerivative();
		ArrayList<Double> potentialXValues = newtonMethod(interval, epsilon, derivative);
		ArrayList<Point> extremePoints = new ArrayList<Point>();
		for (Double x : potentialXValues) {
			if(Thread.interrupted()) {
				Thread.currentThread().interrupt();
				return new ArrayList<Point>();
			}
			if (derivative.calculateFunctionValue(x - step) >= 0
					&& derivative.calculateFunctionValue(x + step) < 0) {
	
				extremePoints.add(new Point(WendepunktName + ": L->R", x, calculateFunctionValue(x)));
			} else if (derivative.calculateFunctionValue(x - step) < 0
					&& derivative.calculateFunctionValue(x + step) >= 0) {
				extremePoints.add(new Point(WendepunktName + ": R->L", x, calculateFunctionValue(x)));
			}
		}
		return checkPeriod(extremePoints);
	}

	/**
	 * Calculates the zeros of the function using the newton method.
	 *<br><br>can be stopped trough Thread.interrupt()
	 * 
	 * @param interval
	 *            interval
	 * @return Zeros of the function
	 * @see #newtonMethod(Interval, double, Function)
	 */
	public ArrayList<Point> calculateZeros(Interval interval) {
		ArrayList<Point> zeros = new ArrayList<Point>();
		for (Double x : newtonMethod(interval, epsilon, this)) {
			if(Thread.interrupted()) {
				Thread.currentThread().interrupt();
				return new ArrayList<Point>();
			}
			zeros.add(new Point(NullpunktName, x, 0));
		}
		return checkPeriod(zeros);
	
	}

	/**
	 * Calculates the zeros of the function using the newton method.
	 *<br><br>can be stopped trough Thread.interrupt()
	 * 
	 * @param interval
	 *            interval
	 * @return Zeros of the function
	 * @see #newtonMethod(Interval, double, Function)
	 */
	public ArrayList<Double> calculateZerosDouble(Interval interval) {
		return newtonMethod(interval, epsilon, this);
	
	}

	/**
	 * Calculates the function value for the input x0 <br>
	 * meaning: f(x0)= return
	  *<br><br>can be stopped trough Thread.interrupt()
	 * 
	 * @param x0
	 *            x coordinate or input
	 * @return function value at x0
	 */
	public double calculateFunctionValue(double x0) {
		boolean naN=false;
		double funktionswert = 0;
		Double testFunktionswert =0.0;
		for (FunctionPart dasFunktionsteil : functionParts) {
			if(Thread.interrupted()) {
				Thread.currentThread().interrupt();
				return 0;
			}
			testFunktionswert = dasFunktionsteil.getFunctionValue(x0);
			if(!testFunktionswert.isNaN()) {
				funktionswert+=testFunktionswert;
				naN=false;
			}else
				naN=true;
		}
		if(naN)
			return Double.NaN;
		
		return funktionswert;
		
	}

	/**
	 * 
	 * Calculates the uppersum in the given interval
	 * <br><br>can be stopped trough Thread.interrupt()
	 * 
	 * @param interval
	 *           interval
	 * @return Uppersum
	 * 
	 */
	
	private double calculateUppersum(Interval interval) {
	
		double sum = 0;
		double lastSum = 0;
		int sliceCount = 50;
		int c = 0;
		do {
			double dX = interval.getRight() - interval.getLeft();
			dX /= sliceCount;
			lastSum = sum;
			sum = 0;
			for (double i = interval.getLeft(); i < interval.getRight(); i += dX) {
				if(Thread.interrupted()) {
					Thread.currentThread().interrupt();
					return 0;
				}
				sum += dX * calculateFunctionValue(i);
			}
			sliceCount *= 2;
			if (c > 0)
				sliceCount *= calculateSliceMultiplier(calculateDistance(sum, lastSum));
			c++;
		} while (calculateDistance(sum, lastSum) > epsilon);
		return sum;
	}

	/**
	 * Utility function calculating surface parts.
	 * 
	 * @param antiderivative
	 *            antiderivative
	 * @param interval
	 *           interval
	 * @return Absolute integral = surface
	 */
	private double calculateIntegral(Function antiderivative, Interval interval) {
		return antiderivative.calculateFunctionValue(interval.getRight()) - (antiderivative.calculateFunctionValue(interval.getLeft()));
	}
	/**
	 * Utility function calculating how fast the sliceCount in {@link #calculateUppersum(Interval)} should increase <br>
	 *  This increases speed.
	 * @param difference difference between the last and newest result.
	 * @return 
	 * 		multiplier for increasing the sliceCount
	 */
	private int calculateSliceMultiplier(double difference) {
		int a = (int) calculateDistance(difference, epsilon);
		return a == 0 ? 1 : a;
	}

	/**
	 * 
	 * Calculates the zeros with the newton Method
	 * <br><br>can be stopped trough Thread.interrupt()
	 * 
	 * @param interval
	 *            Ist der Intervall, in dem nach Nullstellen gesucht wird.
	 * @param epsilon
	 *            Epsilon-Wert, bestimmt Genauigkeit.
	 * @param function
	 *            Die zu untersuchende {@link Function}.
	 * @return Gibt {@link ArrayList} mit Double-Werten (Stellen) zur&uuml;ck.
	 */
	private ArrayList<Double> newtonMethod(Interval interval, double epsilon, Function function) {
		ArrayList<Double> zeros = new ArrayList<Double>();
		ArrayList<Double> startValues = new ArrayList<Double>();
		Function derivative = function.getDerivative();
	
		for (double x = interval.getLeft(); x <= interval.getRight(); x += step) {
			if(Thread.interrupted()) {
				Thread.currentThread().interrupt();
				return new ArrayList<Double>();
			}
			if (function.calculateFunctionValue(x) == 0)
				startValues.add(x);
			else if (function.calculateFunctionValue(x) < 0 && function.calculateFunctionValue(x + step) > 0)
				startValues.add(x + step / 2);
			else if (function.calculateFunctionValue(x) > 0 && function.calculateFunctionValue(x + step) < 0)
				startValues.add(x + step / 2);
	
		}
	
		for (Double x0 : startValues) {
			double lastX0 = 0.0;
			while (calculateDistance(lastX0, x0) > epsilon) {
				if(Thread.interrupted()) {
					Thread.currentThread().interrupt();
					return new ArrayList<Double>();
				}
				lastX0 = x0.doubleValue();
				x0 = x0 - (function.calculateFunctionValue(x0) / derivative.calculateFunctionValue(x0));
			}
			if (!x0.isNaN()) {
	
				zeros.add(x0);
			}
		}
		return zeros;
	}

	/**
	 * Calculates the difference between the two values
	 * 
	 * @param left
	 *            first value
	 * @param right
	 *            second value
	 * @return difference which is always positive
	 */
	private double calculateDistance(double left, double right) {
		double difference = left - right;
		if (difference < 0)
			difference *= -1;
		return difference;
	
	}

	/**
	 * Sorts the list of Points by X values <br>
	 * This uses the bubble sort algorithm
	 * 
	 * @param points
	 *            points
	 * @return points sorted by x values ascending
	 */
	private ArrayList<Point> sortByX(ArrayList<Point> points) {
		Point c = null;
		for (int i = 1; i < points.size(); i++) {
			for (int j = 0; j < points.size() - i; j++) {
				if (points.get(j).getX() > points.get(j + 1).getX()) {
					c = points.get(j);
					points.set(j, points.get(j + 1));
					points.set(j + 1, c);
				}
			}
		}
		return points;
	
	}

	/**
	 * 
	 * Checks the points whether the occur periodically and then simplifies them.<br>
	 * Sorts this list at the end.
	 * 
	 * @param extremePoints
	 *            extremePoints
	 * @return simplified list of points
	 * @see #sortByX(ArrayList)
	 */
	private ArrayList<Point> checkPeriod(ArrayList<Point> extremePoints) {
		ArrayList<Point> maximumPoints = new ArrayList<Point>();
		ArrayList<Point> minimumPoints = new ArrayList<Point>();
		ArrayList<Point> turningPointsLR = new ArrayList<Point>();
		ArrayList<Point> turningPointsRL = new ArrayList<Point>();
		ArrayList<Point> saddlePoints = new ArrayList<Point>();
		ArrayList<Point> zeros = new ArrayList<Point>();
		for (Point point : extremePoints) {
			if (point.getName().equals(HochpunktName)) {
				maximumPoints.add(point);
			} else if (point.getName().equals(TiefpunktName)) {
				minimumPoints.add(point);
			} else if (point.getName().equals(WendepunktName + ": L->R")) {
				turningPointsLR.add(point);
			} else if (point.getName().equals(WendepunktName + ": R->L")) {
				turningPointsRL.add(point);
			} else if (point.getName().equals(SattelpunktName)) {
				saddlePoints.add(point);
			} else if (point.getName().equals(NullpunktName)) {
				zeros.add(point);
			}
		}
		extremePoints.clear();
		checkPeriod(turningPointsRL, extremePoints);
		checkPeriod(turningPointsLR, extremePoints);
		checkPeriod(maximumPoints, extremePoints);
		checkPeriod(minimumPoints, extremePoints);
		checkPeriod(saddlePoints, extremePoints);
		checkPeriod(zeros, extremePoints);
	
		return sortByX(extremePoints);
	
	}

	/**
	 * Checks the points whether the occur periodically and then simplifies them.<br>
	 * 
	 * @param points
	 *            points to be checked
	 * @param destinationList
	 *            List which contains the checked points at the end
	 */
	private void checkPeriod(ArrayList<Point> points, ArrayList<Point> destinationList) {
		double epsilon = 0.0001, y = 0,  lastY = 0;
		double difference = 0, lastDifference = 0;
		ArrayList<Point> deleteList = new ArrayList<Point>();
		try {
			y = points.get(0).getY();
		} catch (Exception e) {
		}
		lastY = y;
		for (int i = 1; i < points.size(); i++) {
			y = points.get(i).getY();
			if (!DoubleMath.fuzzyEquals(y, lastY, epsilon)) {
				destinationList.add(points.get(i));
				deleteList.add(points.get(i));
			}
			lastY = y;
		}
		for (Point p : deleteList) {
			points.remove(p);
		}
		deleteList.clear();
	
		try {
			difference = calculateDistance(points.get(0).getX(), points.get(1).getX());
			lastDifference = difference;
			difference = calculateDistance(points.get(1).getX(), points.get(2).getX());
			if (DoubleMath.fuzzyEquals(difference, lastDifference, epsilon)) {
				String period = "";
				double pi_Factor = difference / Math.PI;
				double pi_Factor_rounded = round(pi_Factor, DoubleMath.roundToInt(Math.log10(1 / epsilon), RoundingMode.HALF_UP));
	
				if (DoubleMath.fuzzyEquals(pi_Factor, pi_Factor_rounded, epsilon)) {
					period = (pi_Factor_rounded == 1 ? "" : "" + pi_Factor_rounded) + "PI";
	
				} else {
					period = pi_Factor + "PI";
				}
				points.get(0).setName(points.get(0).getName() + " {+- k* " + period + "}");
				deleteList.add(points.get(1));
			}
		} catch (Exception e) {
		}
		for (int i = 2; i < points.size(); i++) {
			try {
				difference = calculateDistance(points.get(i).getX(), points.get(i + 1).getX());
			} catch (Exception e) {
	
			}
			if (DoubleMath.fuzzyEquals(difference, lastDifference, epsilon)) {
				deleteList.add(points.get(i));
			}
			lastDifference = difference;
		}
		for (Point p : deleteList) {
			points.remove(p);
		}
		destinationList.addAll(points);
	}

	

	/**
	 * Stores the function in a .func file
	 * 
	 * @param filepath
	 *            path of the file
	 * 
	 */
	public void save(String filepath) {
		try {
			FileOutputStream derFileOutputStream = new FileOutputStream(filepath);
			ObjectOutputStream derObjectOutputStream = new ObjectOutputStream(derFileOutputStream);
			derObjectOutputStream.writeObject(this);
			derObjectOutputStream.close();
			derFileOutputStream.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	/**
	 * Stores the function in a .func file
	 * 
	 * @param filepath
	 *            path of the file
	 * 
	 * @param function
	 *            function to be stored
	 */
	public static void save(String filepath, Function function) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(filepath);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(function);
			objectOutputStream.close();
			fileOutputStream.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	/**
	 * 
	 * Loads a function from a existing .func file
	 * 
	 * @param filepath
	 *            filepath
	 * @return the loaded function
	 */
	public static Function load(String filepath) {
		Function function = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(filepath);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			function = (Function) objectInputStream.readObject();
			objectInputStream.close();
			fileInputStream.close();
		} catch (IOException i) {
			i.printStackTrace();
			return null;
		} catch (ClassNotFoundException c) {
			c.printStackTrace();
			return null;
		}
		return function;

	}

	public String getVariableName() {
		return variableName;
	}
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}

	public String getFunctionName() {
		return functionName;
	}
	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public double getEpsilon() {
		return epsilon;
	}
	public void setEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

	public ArrayList<FunctionPart> getFunctionParts() {
		return functionParts;
	}

	/**
	 * checks if the function is logically <code>f(x)=0 </code> 
	 * 
	 * @return true, if <code>f(x)=0 </code> 
	 */
	public boolean isXAxis() {
		if (this.functionParts.size() == 0)
			return true;
		return false;
	}

}
