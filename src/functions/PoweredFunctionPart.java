package functions;
 
import static functions.Utilities.round;
/**
 * 
 * This function represents a {@link FunctionPart} powered with a second one.
 * The form is: <code>f(x) = a * f1(x)^f2(x) </code> 
 * 
 * @author Jonathan Brose
 *
 */
public class PoweredFunctionPart extends FunctionPart {
	/**
	 * Factor a. The result of {@link #getFunctionValue(double)} is a product of a and 
	 * base function value.
	 */
	private double a;
	private FunctionPart g;
	private FunctionPart u;
	
	public PoweredFunctionPart(double a,FunctionPart u, FunctionPart g) {
		this.a = a;
		this.u = u;
		this.g = g;
	}

	@Override
	public boolean isEqual(FunctionPart functionPart) {
		if(functionPart instanceof PoweredFunctionPart)
			if(functionPart.getA() == a
			&& g.isEqual(((PoweredFunctionPart) functionPart).gibG())
			&& u.isEqual(((PoweredFunctionPart)functionPart).gibU()))
				return true;
		return false;
	}

	@Override
	public void multiply(double a) {
		this.a *= a;
	}

	@Override
	public FunctionPart getDerivative() {
		FunctionPart f = new FactorNexus(
				new Chaining(new PowerFunction(1, -1),u.clone()), g.clone());
		FunctionPart v = new FactorNexus(new Chaining(new Ln(1),u.clone()), g.getDerivative());
		return new FactorNexus(this.clone(), new AdditionNexus(f,v));
	}

	@Override
	public FunctionPart getAntiderivative() {
		return null;
	}

	@Override
	public double getFunctionValue(double x0) {
		return a*Math.pow(u.getFunctionValue(x0), g.getFunctionValue(x0));
	}

	@Override
	public String getString(int i, String variablenName) {
		String s = "";
		double a = round(this.a, 14);
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

			s += (a == (int) (a) ? (int) (a) + "" : checkDouble(a) + "*");
		}
		s+="("+u.getString(0, variablenName)+")"+"^";
		s+="("+g.getString(0, variablenName)+")";
		return s;
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
	public FunctionPart clone() {
		return new PoweredFunctionPart(a,u.clone(), g.clone());
	}

	@Override
	public String[] getChainingString(int i) {
		String[] sA = new String[u.getChainingString(0).length+g.getChainingString(0).length];
		String s = "";
		double a = round(this.a, 14);
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

			s += (a == (int) (a) ? (int) (a) + "" : checkDouble(a) + "*");
		}
		String usA[] = u.getChainingString(0);
		for (int c = 0; c< sA.length; c++) {
			sA[c] = "";
		}
		int c1 =0;
		sA[0] =s+"(";
		for(c1=0; c1 < usA.length; c1++) {
			sA[c1] += usA[c1];
		}
		sA[c1] +=")^";
		String gsA[] = g.getChainingString(0);
		sA[c1+1]= "("+gsA[0];
		for(int c2 =1; c2<gsA.length; c2++) {
			sA[c1+c2+1] += gsA[c2];
		}
		sA[sA.length-1]+=")";
		return sA;
	}
	public FunctionPart gibG() {
		return this.g;
	}
	public FunctionPart gibU() {
		return u;
		
	}
}
