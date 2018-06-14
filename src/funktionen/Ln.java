package funktionen;
 
import static funktionen.Hilfsmethoden.runde;
/**
 * Diese Klasse stellt eine Funktion der Form <code>f(x)= a*ln(x) dar.</code>.
 * <br><code>ln()</code> ist der Logarithmus zur Basis e.
 * <br><code>ln(x)</code> ist nur f&uuml;r x &gt; 0 definiert.
 * @author Jonathan Brose
 */
public class Ln extends Funktionsteil {
	
	private static final long serialVersionUID = -7161290652437039798L;
	/**
	 * Der Vorfaktor a. Das Ergebnis von {@link #gibFunktionswert(double)} wird mit ihm multipliziert.
	 */
	private double a;
	
	public Ln(double a) {
		this.a=a;
	}

	@Override
	public boolean entspricht(Funktionsteil dasFunktionsteil) {
		if(!(dasFunktionsteil instanceof Ln))return false;
		if(this.a==dasFunktionsteil.gibA())return true;
		return false;
	}

	@Override
	public void multipliziere(double a) {
		this.a*=a;

	}

	@Override
	public Funktionsteil gibAbleitung() {
		return new PotenzFunktion(a, -1);
	}

	@Override
	public Funktionsteil gibStammfunktion() {
		
		return new AddVerknuepfung(
				new FaktorVerknuepfung(new Ln(1),new PotenzFunktion(a,1))
				,new PotenzFunktion(-a, 1));
	}

	@Override
	public double gibFunktionswert(double x0) {
		return Math.log(x0)*a;
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
		s += "ln(" + variablenName + ")";
		return s;
	}

	@Override
	public double gibA() {
		return a;
	}

	@Override
	public void setzeA(double a) {
		this.a=a;

	}

	@Override
	public Funktionsteil clone() {
		return new Ln(a);
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
		verkettungString[0] += "ln(";
		verkettungString[1] = ")";
		return verkettungString;
	}

}
