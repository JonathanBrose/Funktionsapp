package src;

import static src.Hilfsmethoden.runde;
/**
 * Diese Klasse stellt eine Exponentialfunktion der Form <code>f(x)= a* x<sup>x</sup></code> dar.
 * <br>Dieses {@link Funktionsteil} gibt bei {@link #gibStammfunktion()} null zurück, wie auch
 * {@link Verknuepfung} mit den Kinderklassen {@link Verkettung} und {@link FaktorVerknuepfung}.
 *@author Jonathan Brose
 */
public class XExponentialFunktion extends Funktionsteil {
	
	private static final long serialVersionUID = -6316961979583605494L;
	/**
	 * Der Vorfaktor a. Das Ergebnis von {@link #gibFunktionswert(double)} wird mit ihm multipliziert.
	 */
	private double a;
	
	public XExponentialFunktion(double a) {
		this.a=a;
	}

	@Override
	public boolean entspricht(Funktionsteil dasFunktionsteil) {
		if(!(dasFunktionsteil instanceof XExponentialFunktion))return false;
		if(this.a==dasFunktionsteil.gibA())return true;
		return false;
	}

	@Override
	public void multipliziere(double a) {
		this.a*=a;

	}

	@Override
	public Funktionsteil gibAbleitung() {
		return new FaktorVerknuepfung(new XExponentialFunktion(a),
				new AddVerknuepfung(new Ln(a),new PotenzFunktion(a, 0)));
	}

	@Override
	public Funktionsteil gibStammfunktion() {
		
		return null;
	}

	@Override
	public double gibFunktionswert(double x0) {
		return a*Math.pow(x0, x0);
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
		s += variablenName+"^" + variablenName ;
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
		return new XExponentialFunktion(a);
	}

	@Override
	public String[] gibVerkettungString(int i) {
		double a = runde(this.a, 14);
		String[] verkettungString = new String[4];
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
		verkettungString[0] += "*(";
		verkettungString[1] = ")";
		verkettungString[2] += "^(";
		verkettungString[3] = ")";
		return verkettungString;
	}

}
