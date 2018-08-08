package functions;
 
import static functions.Utilities.round;
/**
 * This class is representing a function with the form <code>f(x)= a*ln(x)</code>.
 * <br><code>ln()</code> is basically log to bas e.
 * <br><code>ln(x)</code> is defined for x &gt; 0 
 * @author Jonathan Brose
 */
public class Ln extends FunctionPart {
	
	private static final long serialVersionUID = -7161290652437039798L;
	
	private double a;
	
	public Ln(double a) {
		this.a=a;
	}

	@Override
	public boolean isEqual(FunctionPart functionPart) {
		if(!(functionPart instanceof Ln))return false;
		if(this.a==functionPart.getA())return true;
		return false;
	}

	@Override
	public void multiply(double a) {
		this.a*=a;

	}

	@Override
	public FunctionPart getDerivative() {
		return new PowerFunction(a, -1);
	}

	@Override
	public FunctionPart getAntiderivative() {
		
		return new AdditionNexus(
				new FactorNexus(new Ln(1),new PowerFunction(a,1))
				,new PowerFunction(-a, 1));
	}

	@Override
	public double getFunctionValue(double x0) {
		return Math.log(x0)*a;
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
		s += "ln(" + variableName + ")";
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
		return new Ln(a);
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
		chainingString[0] += "ln(";
		chainingString[1] = ")";
		return chainingString;
	}

}
