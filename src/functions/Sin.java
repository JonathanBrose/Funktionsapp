package functions;
 
import static functions.Utilities.round;

/**
 * 
 * Class representing a sinus function <br>
 * <code>f(x) = a * sin(x)</code>
 * 
 * @author Jonathan Brose, Nick Dressler 
 *
 */
public class Sin extends FunctionPart {

	private static final long serialVersionUID = -528912013771015838L;
	/**
	 * Factor a. The result of {@link #getFunctionValue(double)} is a product of a and 
	 * base function value.
	 */
	private double a;

	/**
	 * 
	 * creates a {@link Sin}us function
	 * 
	 * @param a
	 *            factor a
	 */
	public Sin(double a) {
		this.a = a;
	}

	@Override
	public FunctionPart getDerivative() {
		return new Cos(this.a);
	}

	@Override
	public FunctionPart getAntiderivative() {
		return new Cos(-this.a);
	}

	@Override
	public double getFunctionValue(double x0) {
		return this.a * Math.sin(x0);
	}

	@Override
	public boolean isEqual(FunctionPart dasFunktionsteil) {
		if (dasFunktionsteil instanceof Sin)
			return (this.a == ((Sin) dasFunktionsteil).getA());
		else
			return false;
	}

	@Override
	public String[] getChainingString(int i) {
		double a = round(this.a, 14);
		String[] verkettungString = new String[2];
		verkettungString[0] = "";
		if (a == 1) {
			verkettungString[0] += "";
			if (i != 0)
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
		verkettungString[0] += "sin(";
		verkettungString[1] = ")";
		return verkettungString;

	}

	@Override
	public String getString(int i, String variablenName) {
		double a = round(this.a, 14);
		String s = "";
		if (a == 1) {
			s += "";
			if (i != 0)
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
		s += "sin(" + variablenName + ")";
		return s;
	}

	@Override
	public FunctionPart clone() {
		return new Sin(a);
	}

	@Override
	public void multiply(double a) {
		this.a *= a;

	}

	public double getA() {
		return this.a;
	}

	public void setA(double a) {
		this.a = a;
	}

}
