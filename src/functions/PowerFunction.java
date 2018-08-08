package functions;
 
import static functions.Utilities.round;

/**
 * 
 * 
 * Klasse, welche eine Potenzfunktion der Form
 * <code>f(x) = a*x<sup>p</sup></code> repr&auml;sentiert.
 *
 * 
 * @author Jonathan Brose, Nick Dressler 
 */

public class PowerFunction extends FunctionPart {

	/**
	 * 
	 */
	private static final long serialVersionUID = -292429041735981834L;
	/**
	 * Factor a. The result of {@link #getFunctionValue(double)} is a product of a and 
	 * base function value.
	 */
	private double a;
	/**
	 * Der Exponent p. Der Funktionswert wird mit p potenziert und mit {@link #a} multipliziert.
	 */
	private double p;

	/**
	 * Konstruktor der Klasse, welche eine Potenzfunktion der Form
	 * <code>f(x) = a*x<sup>p</sup></code> repr&auml;sentiert.
	 * 
	 * @param a
	 *            Faktor a
	 * @param p
	 *            Potenz p
	 */
	public PowerFunction(double a, double p) {
		this.a = a;
		this.p = p;
	}

	@Override
	public FunctionPart getDerivative() {
		return new PowerFunction(this.a * this.p, this.p - 1);
	}

	@Override
	public FunctionPart getAntiderivative() {
		if(p==-1) {
			return new Ln(a);
		}
		return new PowerFunction(this.a / (this.p + 1), this.p + 1);

	}

	@Override
	public double getFunctionValue(double x0) {
		return this.a * Math.pow(x0, this.p);
	}

	@Override
	public String getString(int i, String variablenName) {
		String s = "";
		double a = round(this.a, 14);
		double p = round(this.p, 14);
		if (a == 1 && p > 0) {
			s += "";
			if (i != 0)
				s += "+";
			if (i < 0)
				s += " ";
		} else if (a == -1 && p > 0) {
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

			s += (a == (int) (a) ? (int) (a) + "" : checkDouble(a) + (p > 0 ? "*" : ""));
		}
		if (p == 0) {
			s += "";
		} else if (p == 1) {
			s += "" + variablenName + "";
		} else if (p == 0.5) {
			s += "\u221A(" + variablenName + ")";
		}  else if (p < 0) {
			p *= -1;
			s += "/";
			if (p == 0.5) {
				s += "\u221A(" + variablenName + ")";
			}  else if (p == 1) {
				s += variablenName;
			}else {
				s += variablenName + "^" + (p == (int) (p) ? (int) (p) + "" : checkDouble(p) + "");
			}
		} else {
			s += "" + variablenName + "^" + (p == (int) (p) ? (int) (p) + "" : checkDouble(p) + "");
		}
		return s;
	}

	public String[] getFractionString(int i, String variablenName) {
		if(p>=0)return null;
		double a = round(this.a, 14);
		double p = round(this.p, 14);
		String[] sA = new String[2];
		sA[0] = "";
		if (a == 1) {
			sA[0] += "";
			if (i != 0)
				sA[0] += "+";
			if (i < 0)
				sA[0] += " ";
		} else if (a == -1) {
			sA[0] += "-";
			if (i < 0)
				sA[0] += " ";
		} else {
			if (a < 0) {
				a *= -1;
				sA[0] += "-";
			} else if (i != 0) {
				sA[0] += "+";
			}
			if (i < 0)
				sA[0] += " ";

			sA[0] += (a == (int) (a) ? (int) (a) + "" : checkDouble(a)+"*");
		}
		sA[0] += "(";
		sA[1]=")/";
		p *= -1;
		if (p == 0.5) {
			sA[1]+= "\u221A("+variablenName+")";
		} else if (p == 1) {
			sA[1]+= variablenName+"";
		}else {
			sA[1] +="("+variablenName+ "^" + (p == (int) (p) ? (int) (p) + "" : checkDouble(p) + ")");
		}
		return sA;
	}
	
	@Override
	public String[] getChainingString(int i) {
		double a = round(this.a, 14);
		double p = round(this.p, 14);
		String[] verkettungString = new String[2];
		verkettungString[0] = "";
		if (a == 1 && p > 0) {
			verkettungString[0] += "";
			if (i != 0)
				verkettungString[0] += "+";
			if (i < 0)
				verkettungString[0] += " ";
		} else if (a == -1 && p > 0) {
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

			verkettungString[0] += (a == (int) (a) ? (int) (a) + "" : checkDouble(a) + (p > 0 ? "*" : ""));
		}

		if (p == 0) {
			verkettungString[1] = "";
		} else if (p == 1) {
			verkettungString[0] += "(";
			verkettungString[1] = ")";
		} else if (p < 0) {
			p *= -1;
			verkettungString[0]+="/";
			if (p == 0.5) {
				verkettungString[0]+= "\u221A(";
				verkettungString[1]= ")";
			} else if (p == 1) {
				verkettungString[0]+= "(";
				verkettungString[1]= ")";
			}else {
			verkettungString[0] += "(";
			verkettungString[1] = ")^" + (p == (int) (p) ? (int) (p) + "" : checkDouble(p) + "");
			}
		} else if (p == 0.5) {
			verkettungString[0]+= "\u221A(";
			verkettungString[1]= ")";
		} else {
			verkettungString[0] += "(";
			verkettungString[1] = ")^" + (p == (int) (p) ? (int) (p) + "" : checkDouble(p) + "");
		}

		return verkettungString;

	}

	@Override
	public boolean isEqual(FunctionPart dasFunktionsteil) {
		if (dasFunktionsteil instanceof PowerFunction)
			return (this.a == ((PowerFunction) dasFunktionsteil).getA()
					&& this.p == ((PowerFunction) dasFunktionsteil).getP());
		else
			return false;
	}

	@Override
	public FunctionPart clone() {
		return new PowerFunction(a, p);
	}

	@Override
	public void multiply(double a) {
		this.a *= a;

	}

	@Override
	public double getA() {
		return a;
	}

	public void setA(double a) {
		this.a = a;
	}

	/**
	 * Getter des Exponents p
	 * @return {@link #p}
	 */
	public double getP() {
		return p;
	}

	/**
	 * Setter des Exponents p.
	 * 
	 * @param p neuer Wert f&uuml;r den Exponenten p
	 */
	public void setP(double p) {
		this.p = p;
	}

}
