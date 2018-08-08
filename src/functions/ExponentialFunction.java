package functions;
 
import static functions.Utilities.round;
/**
 * Class representing a exponential function with the following form: <br>
 * <code>f(x) = a*b<sup>x</sup></code>
 * 
 * @author Jonathan Brose, Nick Dressler 
 *
 */

public class ExponentialFunction extends FunctionPart {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1141361740623807483L;
	/**
	 * Factor a. The result of {@link #getFunctionValue(double)} is a product of a and 
	 * base function value.
	 */
	private double a;
	/**
	 * Base b. this value will get powered with the input of the function.
	 */
	private double b;

	/**
	 * Creates an {@link ExponentialFunction}: <br> <code>f(x) = a*b<sup>x</sup></code> 
	 * 
	 * @param a
	 *            Factor a
	 * @param b
	 *            Base b
	 */
	public ExponentialFunction(double a, double b) {
		this.a = a;
		this.b = b;
	}
	@Override
	public FunctionPart getDerivative() {
	
		return new FactorNexus(new Chaining(new Ln(1),new PowerFunction(b, 0)),
				new ExponentialFunction(a, b));
	
	}
	@Override
	public FunctionPart getAntiderivative() {
	
		return new FactorNexus(new Chaining(new PowerFunction(a, -1),
				new Chaining(new Ln(1),new PowerFunction(b, 0))),
				new ExponentialFunction(1, b));
	}
	@Override
	public double getFunctionValue(double x0) {
		return this.a * Math.pow(this.b, x0);
	}
	@Override
	public String getString(int i, String variablenName) {
		String s = "";
		double a = round(this.a, 14);
		double b = round(this.b, 14);
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
		} else {
			if (a < 0) {
				a *= -1;
				s += "-";
			} else if (i != 0) {
				s += "+";
			}
			if (i < 0)
				s += " ";
	
			s += (a == (int) (a) ? (int) (a) + "*" : checkDouble(a) + "*");
		}
		s += (b == (int) (b) ? (int) (b) + "" : checkDouble(b) + "") + "^" + variablenName + "";
	
		return s;
	}
	@Override
	public String[] getChainingString(int i) {
		double a = round(this.a, 14);
		double b = round(this.b, 14);
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
	
			verkettungString[0] += (a == (int) (a) ? (int) (a) + "*" : checkDouble(a) + "*");
		}
		verkettungString[0] += (b == (int) (b) ? (int) (b) + "" : checkDouble(b) + "") + "^(";
		verkettungString[1] = ")";
		return verkettungString;
	
	}
	@Override
	public boolean isEqual(FunctionPart dasFunktionsteil) {
		if (dasFunktionsteil instanceof ExponentialFunction)
			return (this.a == ((ExponentialFunction) dasFunktionsteil).getA() && this.b == ((ExponentialFunction) dasFunktionsteil).getB());
		
		return false;
	}
	@Override
	public FunctionPart clone() {
		return new ExponentialFunction(a, b);
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

	public double getB() {
		return b;
	}

	public void setB(double b) {
		this.b = b;
	}

}
