package src;

import static src.Hilfsmethoden.runde;

/**
 * 
 * Klasse, welche eine Sinusfunktion der Form <code>f(x) = a*sin(x)</code>
 * repr&auml;sentiert.
 * 
 * @author Jonathan Brose, Nick Dressler 
 *
 */
public class Sin extends Funktionsteil {

	/**
	 * 
	 */
	private static final long serialVersionUID = -528912013771015838L;
	/**
	 * Der Vorfaktor a. Das Ergebnis von {@link #gibFunktionswert(double)} wird mit ihm multipliziert.
	 */
	private double a;

	/**
	 * 
	 * Konstruktor der Klasse, welche eine Sinusfunktion der Form
	 * <code>f(x) = a*sin(x)</code> repr&auml;sentiert.
	 * 
	 * @param a
	 *            Faktor a
	 */
	public Sin(double a) {
		this.a = a;
	}

	@Override
	public Funktionsteil gibAbleitung() {
		return new Cos(this.a);
	}

	@Override
	public Funktionsteil gibStammfunktion() {
		return new Cos(-this.a);
	}

	@Override
	public double gibFunktionswert(double x0) {
		return this.a * Math.sin(x0);
	}

	@Override
	public boolean entspricht(Funktionsteil dasFunktionsteil) {
		if (dasFunktionsteil instanceof Sin)
			return (this.a == ((Sin) dasFunktionsteil).gibA());
		else
			return false;
	}

	@Override
	public String[] gibVerkettungString(int i) {
		double a = runde(this.a, 14);
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

			verkettungString[0] += (a == (int) (a) ? (int) (a) + "*" : ueberpruefeDouble(a) + "*");
		}
		verkettungString[0] += "sin(";
		verkettungString[1] = ")";
		return verkettungString;

	}

	@Override
	public String gibString(int i, String variablenName) {
		double a = runde(this.a, 14);
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

			s += (a == (int) (a) ? (int) (a) + "*" : ueberpruefeDouble(a) + "*");
		}
		s += "sin(" + variablenName + ")";
		return s;
	}

	@Override
	public Funktionsteil clone() {
		return new Sin(a);
	}

	@Override
	public void multipliziere(double a) {
		this.a *= a;

	}

	public double gibA() {
		return this.a;
	}

	public void setzeA(double a) {
		this.a = a;
	}

	// @Override
	// public String gibString(int i, String variablenName) {
	// double a = runde(this.a, 14);
	// String s = "";
	// if (a == 1) {
	// s += "";
	// if(i!=0)
	// s += "+";
	// if (i < 0)
	// s += " ";
	// } else if (a == -1) {
	// s += "-";
	// if (i < 0)
	// s += " ";
	// } else {
	// if (a < 0) {
	// a *= -1;
	// s += "-";
	// } else if (i != 0) {
	// s += "+";
	// }
	// if (i < 0)
	// s += " ";
	//
	// s += (a == (int) (a) ? (int) (a) + "*" : ueberpruefeDouble(a) + "*");
	// }
	// s += "sin(" + variablenName + ")";
	// return s;
	// }

}
