package funktionen;
 
import static funktionen.Hilfsmethoden.runde;
/**
 * Klasse, welche eine Exponentialfunktion der Form
 * <code>f(x) = a*b<sup>x</sup></code> repr&auml;sentiert.
 * 
 * @author Jonathan Brose, Nick Dressler 
 *
 */

public class ExponentialFunktion extends Funktionsteil {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1141361740623807483L;
	/**
	 * Der Vorfaktor a. Das Ergebnis von {@link #gibFunktionswert(double)} wird mit ihm multipliziert.
	 */
	private double a;
	/**
	 * Die Basis der Exponentialfunktion.
	 */
	private double b;

	/**
	 * Funktionsteil das <code>f(x) = a*b<sup>x</sup></code> repr&auml;sentiert.
	 * 
	 * @param a
	 *            Faktor a
	 * @param b
	 *            Basis b
	 */
	public ExponentialFunktion(double a, double b) {
		this.a = a;
		this.b = b;
	}
	@Override
	public Funktionsteil gibAbleitung() {
	
		return new FaktorVerknuepfung(new Verkettung(new Ln(1),new PotenzFunktion(b, 0)),
				new ExponentialFunktion(a, b));
	
	}
	@Override
	public Funktionsteil gibStammfunktion() {
	
		return new FaktorVerknuepfung(new Verkettung(new PotenzFunktion(a, -1),
				new Verkettung(new Ln(1),new PotenzFunktion(b, 0))),
				new ExponentialFunktion(1, b));
	}
	@Override
	public double gibFunktionswert(double x0) {
		return this.a * Math.pow(this.b, x0);
	}
	@Override
	public String gibString(int i, String variablenName) {
		String s = "";
		double a = runde(this.a, 14);
		double b = runde(this.b, 14);
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
	
			s += (a == (int) (a) ? (int) (a) + "*" : ueberpruefeDouble(a) + "*");
		}
		s += (b == (int) (b) ? (int) (b) + "" : ueberpruefeDouble(b) + "") + "^" + variablenName + "";
	
		return s;
	}
	@Override
	public String[] gibVerkettungString(int i) {
		double a = runde(this.a, 14);
		double b = runde(this.b, 14);
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
	
			verkettungString[0] += (a == (int) (a) ? (int) (a) + "*" : ueberpruefeDouble(a) + "*");
		}
		verkettungString[0] += (b == (int) (b) ? (int) (b) + "" : ueberpruefeDouble(b) + "") + "^(";
		verkettungString[1] = ")";
		return verkettungString;
	
	}
	@Override
	public boolean entspricht(Funktionsteil dasFunktionsteil) {
		if (dasFunktionsteil instanceof ExponentialFunktion)
			return (this.a == ((ExponentialFunktion) dasFunktionsteil).gibA() && this.b == ((ExponentialFunktion) dasFunktionsteil).gibB());
		
		return false;
	}
	@Override
	public Funktionsteil clone() {
		return new ExponentialFunktion(a, b);
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
	/**
	 * 	
	 * 
	 * @return double B<br>
	 * b repr&auml;sentiert die Basis
	 */
	public double gibB() {
		return b;
	}
	/** @param b
	 *  repr&auml;sentiert die Basis
	 */
	public void setzeB(double b) {
		this.b = b;
	}

}
