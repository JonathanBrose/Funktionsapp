package funktionen;
 
import static funktionen.Hilfsmethoden.runde;
/**
 * 
 * Klasse, welche eine E-Funktion der Form <code>f(x) = a*e<sup>x</sup></code>,
 * repr&auml;sentiert.
 * 
 * @author Jonathan Brose, Nick Dressler 
 *
 */

public class NatuerlicheExponentialFunktion extends Funktionsteil {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2305964786217743397L;
	/**
	 * Der Vorfaktor a. Das Ergebnis von {@link #gibFunktionswert(double)} wird mit ihm multipliziert.
	 */
	private double a;

	// f(x) = a*e^x
	/**
	 * Konstruktor der Klasse, die eine Exponentialfunktion mit der basis e
	 * repr&auml;sentiert.<br>
	 * <code>f(x) = a*e<sup>x</sup></code>
	 * 
	 * @param a
	 *            Faktor a
	 */
	public NatuerlicheExponentialFunktion(double a) {
		this.a = a;
	}

	@Override
	public Funktionsteil gibAbleitung() {
		return this;
	}

	@Override
	public Funktionsteil gibStammfunktion() {
		return this;
	}

	@Override
	public double gibFunktionswert(double x0) {
		return this.a * Math.pow(Math.E, x0);
	}

	@Override
	public String gibString(int i, String variablenName) {
		String s = "";
		double a = runde(this.a, 14);
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
	
			s += (a == (int) (a) ? (int) (a) + "" : ueberpruefeDouble(a) + "");
		}
		s += "e^" + variablenName + "";
		return s;
	}

	@Override
	public String[] gibVerkettungString(int i) {
		double a = runde(this.a, 14);
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
	
			verkettungString[0] += (a == (int) (a) ? (int) (a) + "" : ueberpruefeDouble(a) + "");
		}
		verkettungString[0] += "e^(";
		verkettungString[1] = ")";
		return verkettungString;
	
	}

	@Override
	public void multipliziere(double a) {
		this.a *= a;
	
	}

	@Override
	public double gibA() {
		return a;
	}

	@Override
	public void setzeA(double a) {
		this.a = a;
	}

	@Override
	public boolean entspricht(Funktionsteil dasFunktionsteil) {
		if (dasFunktionsteil instanceof NatuerlicheExponentialFunktion)
			return (this.a == ((NatuerlicheExponentialFunktion) dasFunktionsteil).gibA());
		else
			return false;
	}

	@Override
	public Funktionsteil clone() {
		return new NatuerlicheExponentialFunktion(a);
	}

}
