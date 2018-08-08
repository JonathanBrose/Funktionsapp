package functions;
 
import static functions.Utilities.round;
/**
 * Class representing a exponential function with base and exponent x <br> <code>f(x)= a* x<sup>x</sup></code> dar.
 * <br>returns null for {@link #getAntiderivative()}
 *@author Jonathan Brose
 */
public class XExponentialFunction extends FunctionPart {
	
	private static final long serialVersionUID = -6316961979583605494L;
	/**
	 * Factor a. The result of {@link #getFunctionValue(double)} is a product of a and 
	 * base function value.
	 */
	private double a;
	
	public XExponentialFunction(double a) {
		this.a=a;
	}

	@Override
	public boolean isEqual(FunctionPart functionPart) {
		if(!(functionPart instanceof XExponentialFunction))return false;
		if(this.a==functionPart.getA())return true;
		return false;
	}

	@Override
	public void multiply(double a) {
		this.a*=a;

	}

	@Override
	public FunctionPart getDerivative() {
		return new FactorNexus(new XExponentialFunction(a),
				new AdditionNexus(new Ln(a),new PowerFunction(a, 0)));
	}

	@Override
	public FunctionPart getAntiderivative() {
		
		return null;
	}

	@Override
	public double getFunctionValue(double x0) {
		return a*Math.pow(x0, x0);
	}

	@Override
	public String getString(int i, String variableName) {
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
		s += variableName+"^" + variableName ;
		return s;
	}

	@Override
	public double getA() {
		return a;
	}

	@Override
	public void setA(double a) {
		this.a=a;

	}

	@Override
	public FunctionPart clone() {
		return new XExponentialFunction(a);
	}

	@Override
	public String[] getChainingString(int i) {
		double a = round(this.a, 14);
		String[] chainingString = new String[4];
		chainingString[0] = "";
		if (a == 1 ) {
			chainingString[0] += "";
			if(i!=0)
				chainingString[0] += "+";
			if (i < 0)
				chainingString[0] += " ";
		} else if (a == -1) {
			chainingString[0] += "-";
			if (i < 0)
				chainingString[0] += " ";
		} else {
			if (a < 0) {
				a *= -1;
				chainingString[0] += "-";
			} else if (i != 0) {
				chainingString[0] += "+";
			}
			if (i < 0)
				chainingString[0] += " ";
	
			chainingString[0] += (a == (int) (a) ? (int) (a) + "" : checkDouble(a) + "");
		}
		chainingString[0] += "*(";
		chainingString[1] = ")";
		chainingString[2] += "^(";
		chainingString[3] = ")";
		return chainingString;
	}

}
