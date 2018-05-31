package src;
 
import static src.Hilfsmethoden.runde;

/**
 * 
 * 
 * Klasse, welche eine Potenzfunktion der Form
 * <code>f(x) = a*x<sup>p</sup></code> repr&auml;sentiert.
 *
 * 
 * @author Jonathan Brose, Nick Dressler 
 */

public class PotenzFunktion extends Funktionsteil {

	/**
	 * 
	 */
	private static final long serialVersionUID = -292429041735981834L;
	/**
	 * Der Vorfaktor a. Das Ergebnis von {@link #gibFunktionswert(double)} wird mit ihm multipliziert.
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
	public PotenzFunktion(double a, double p) {
		this.a = a;
		this.p = p;
	}

	@Override
	public Funktionsteil gibAbleitung() {
		return new PotenzFunktion(this.a * this.p, this.p - 1);
	}

	@Override
	public Funktionsteil gibStammfunktion() {
		if(p==-1) {
			return new Ln(a);
		}
		return new PotenzFunktion(this.a / (this.p + 1), this.p + 1);

	}

	@Override
	public double gibFunktionswert(double x0) {
		return this.a * Math.pow(x0, this.p);
	}

	@Override
	public String gibString(int i, String variablenName) {
		String s = "";
		double a = runde(this.a, 14);
		double p = runde(this.p, 14);
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

			s += (a == (int) (a) ? (int) (a) + "" : ueberpruefeDouble(a) + (p > 0 ? "*" : ""));
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
				s += variablenName + "^" + (p == (int) (p) ? (int) (p) + "" : ueberpruefeDouble(p) + "");
			}
		} else {
			s += "" + variablenName + "^" + (p == (int) (p) ? (int) (p) + "" : ueberpruefeDouble(p) + "");
		}
		return s;
	}

	@Override
	public String[] gibVerkettungString(int i) {
		double a = runde(this.a, 14);
		double p = runde(this.p, 14);
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

			verkettungString[0] += (a == (int) (a) ? (int) (a) + "" : ueberpruefeDouble(a) + (p > 0 ? "*" : ""));
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
			verkettungString[1] = ")^" + (p == (int) (p) ? (int) (p) + "" : ueberpruefeDouble(p) + "");
			}
		} else if (p == 0.5) {
			verkettungString[0]+= "\u221A(";
			verkettungString[1]= ")";
		} else {
			verkettungString[0] += "(";
			verkettungString[1] = ")^" + (p == (int) (p) ? (int) (p) + "" : ueberpruefeDouble(p) + "");
		}

		return verkettungString;

	}

	@Override
	public boolean entspricht(Funktionsteil dasFunktionsteil) {
		if (dasFunktionsteil instanceof PotenzFunktion)
			return (this.a == ((PotenzFunktion) dasFunktionsteil).gibA()
					&& this.p == ((PotenzFunktion) dasFunktionsteil).gibP());
		else
			return false;
	}

	@Override
	public Funktionsteil clone() {
		return new PotenzFunktion(a, p);
	}

	@Override
	public void multipliziere(double a) {
		this.a *= a;

	}

	@Override
	public double gibA() {
		return a;
	}

	public void setzeA(double a) {
		this.a = a;
	}

	/**
	 * Getter des Exponents p
	 * @return {@link #p}
	 */
	public double gibP() {
		return p;
	}

	/**
	 * Setter des Exponents p.
	 * 
	 * @param p neuer Wert f&uuml;r den Exponenten p
	 */
	public void setzeP(double p) {
		this.p = p;
	}

}
