package functions;
 
import static functions.Utilities.round;
/**
 * This class represents a cosine function with the form <code>f(x) = a*cos(x)</code>
 * @author Jonathan Brose, Nick Dressler 
 */
public class Cos extends FunctionPart {

	private static final long serialVersionUID = 6447989144560498856L;
	/**
	 * Factor a, the result of {@link #getFunctionValue(double)} gets multiplied with it.
	 */
	private double a;

	/**
	 * Creates a cosine functionPart.
	 * 
	 * @param a
	 *           Factor a
	 */
	public Cos(double a) {
		this.a = a;
	}
	

	@Override
	public FunctionPart getDerivative() {
		return new Sin(-this.a);
	}

	@Override
	public FunctionPart getAntiderivative() {
		return new Sin(this.a);
	}

	@Override
	public double getFunctionValue(double x0) {
		return this.a * Math.cos(x0);
	}

	@Override
	public String getString(int i, String variablenName) {
		double a = round(this.a, 14);
		String s = "";
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
		s += "cos(" + variablenName + ")";
		return s;
	}

	@Override
	public String[] getChainingString(int i) {
		double a = round(this.a, 14);
		String[] chainingString = new String[2];
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
		}else {
			if (a < 0) {
				a *= -1;
				chainingString[0] += "-";
			} else if (i != 0) {
				chainingString[0] += "+";
			}
			if (i < 0)
				chainingString[0] += " ";
	
			chainingString[0] += (a == (int) (a) ? (int) (a) + "*" : checkDouble(a) + "*");
		}
		chainingString[0] += "cos(";
		chainingString[1] = ")";
		return chainingString;
	
	}

	@Override
	public boolean isEqual(FunctionPart functionPart) {
		if (functionPart instanceof Cos)
			return (this.a == ((Cos) functionPart).getA());
		else
			return false;
	}

	@Override
	public FunctionPart clone() {
		return new Cos(this.a);
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

}
