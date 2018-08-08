package functions;
 
import static functions.Utilities.round;
/**
 * 
 * Class representing an exponential function with the <b>e</b> as base<br>
 * <code> f(x) = a* e^x </code>
 * 
 * @author Jonathan Brose, Nick Dressler 
 *
 */

public class EulerExponentialFunction extends FunctionPart {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2305964786217743397L;
	/**
	 * Factor a. The result of {@link #getFunctionValue(double)} is a product of a and 
	 * base function value.
	 */
	private double a;

	
	/**
	 * Creates an {@link EulerExponentialFunction}<br>
	 * <code>f(x) = a*e<sup>x</sup></code>
	 * 
	 * @param a
	 *            Factor a
	 */
	public EulerExponentialFunction(double a) {
		this.a = a;
	}

	@Override
	public FunctionPart getDerivative() {
		return this;
	}

	@Override
	public FunctionPart getAntiderivative() {
		return this;
	}

	@Override
	public double getFunctionValue(double x0) {
		return this.a * Math.pow(Math.E, x0);
	}

	@Override
	public String getString(int i, String variablenName) {
		String s = "";
		double a = round(this.a, 14);
		if (a == 1) {
			s += "";
			if(i!=0)
				s += "+";
			if (i < 0)
				s += " ";
		} else if (a == -1) {
			s += "-";
			if (i < 0)
				s += " ";
		}else {
			if (a < 0) {
				a *= -1;
				s += "-";
			} else if (i != 0) {
				s += "+";
			}
			if (i < 0)
				s += " ";
	
			s += (a == (int) (a) ? (int) (a) + "" : checkDouble(a) + "");
		}
		s += "e^" + variablenName + "";
		return s;
	}

	@Override
	public String[] getChainingString(int i) {
		double a = round(this.a, 14);
		String[] verkettungString = new String[2];
		verkettungString[0] = "";
		if (a == 1 ) {
			verkettungString[0] += "";
			if(i!=0)
				verkettungString[0] += "+";
			if (i < 0)
				verkettungString[0] += " ";
		} else if (a == -1) {
			verkettungString[0] += "-";
			if (i < 0)
				verkettungString[0] += " ";
		} else {
			if (a < 0) {
				a *= -1;
				verkettungString[0] += "-";
			} else if (i != 0) {
				verkettungString[0] += "+";
			}
			if (i < 0)
				verkettungString[0] += " ";
	
			verkettungString[0] += (a == (int) (a) ? (int) (a) + "" : checkDouble(a) + "");
		}
		verkettungString[0] += "e^(";
		verkettungString[1] = ")";
		return verkettungString;
	
	}

	@Override
	public void multiply(double a) {
		this.a *= a;
	
	}

	@Override
	public double getA() {
		return a;
	}

	@Override
	public void setA(double a) {
		this.a = a;
	}

	@Override
	public boolean isEqual(FunctionPart dasFunktionsteil) {
		if (dasFunktionsteil instanceof EulerExponentialFunction)
			return (this.a == ((EulerExponentialFunction) dasFunktionsteil).getA());
		else
			return false;
	}

	@Override
	public FunctionPart clone() {
		return new EulerExponentialFunction(a);
	}

}
