package src;
import static src.Hilfsmethoden.runde;
/**
 * Klasse, welche eine Cosinusfunktion der Form <code>f(x) = a*cos(x)</code>
 * repr&auml;sentiert.
 * @author Jonathan Brose, Nick Dressler 
 */
public class Cos extends Funktionsteil {

	private static final long serialVersionUID = 6447989144560498856L;
	/**
	 * Der Vorfaktor a. Das Ergebnis von {@link #gibFunktionswert(double)} wird mit ihm multipliziert.
	 */
	private double a;

	/**
	 * Konstruktor der Klasse, welche eine Cosinusfunktion der Form
	 * <code>f(x) = a*cos(x)</code> repr&auml;sentiert.
	 * 
	 * @param a
	 *            Vorfaktor a
	 */
	public Cos(double a) {
		this.a = a;
	}

	@Override
	public Funktionsteil gibAbleitung() {
		return new Sin(-this.a);
	}

	@Override
	public Funktionsteil gibStammfunktion() {
		return new Sin(this.a);
	}

	@Override
	public double gibFunktionswert(double x0) {
		return this.a * Math.cos(x0);
	}

	@Override
	public String gibString(int i, String variablenName) {
		double a = runde(this.a, 14);
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
	
			s += (a == (int) (a) ? (int) (a) + "*" : ueberpruefeDouble(a) + "*");
		}
		s += "cos(" + variablenName + ")";
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
		}else {
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
		verkettungString[0] += "cos(";
		verkettungString[1] = ")";
		return verkettungString;
	
	}

	@Override
	public boolean entspricht(Funktionsteil dasFunktionsteil) {
		if (dasFunktionsteil instanceof Cos)
			return (this.a == ((Cos) dasFunktionsteil).gibA());
		else
			return false;
	}

	@Override
	public Funktionsteil clone() {
		return new Cos(this.a);
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

}
