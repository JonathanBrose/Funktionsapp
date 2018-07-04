package funktionen;

import static funktionen.Hilfsmethoden.runde;
/**
 * 
 * @author Jonathan Brose
 *
 */
public class FunktionsteilPotenz extends Funktionsteil {
	private double a;
	private Funktionsteil g;
	private Funktionsteil u;
	
	public FunktionsteilPotenz(double a,Funktionsteil u, Funktionsteil g) {
		this.a = a;
		this.u = u;
		this.g = g;
	}

	@Override
	public boolean entspricht(Funktionsteil dasFunktionsteil) {
		if(dasFunktionsteil instanceof FunktionsteilPotenz)
			if(dasFunktionsteil.gibA() == a
			&& g.entspricht(((FunktionsteilPotenz) dasFunktionsteil).gibG())
			&& u.entspricht(((FunktionsteilPotenz)dasFunktionsteil).gibU()))
				return true;
		return false;
	}

	@Override
	public void multipliziere(double a) {
		this.a *= a;
	}

	@Override
	public Funktionsteil gibAbleitung() {
		Funktionsteil f = new FaktorVerknuepfung(
				new Verkettung(new PotenzFunktion(1, -1),u.clone()), g.clone());
		Funktionsteil v = new FaktorVerknuepfung(new Verkettung(new Ln(1),u.clone()), g.gibAbleitung());
		return new FaktorVerknuepfung(this.clone(), new AddVerknuepfung(f,v));
	}

	@Override
	public Funktionsteil gibStammfunktion() {
		return null;
	}

	@Override
	public double gibFunktionswert(double x0) {
		return a*Math.pow(u.gibFunktionswert(x0), g.gibFunktionswert(x0));
	}

	@Override
	public String gibString(int i, String variablenName) {
		String s = "";
		double a = runde(this.a, 14);
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

			s += (a == (int) (a) ? (int) (a) + "" : ueberpruefeDouble(a) + "*");
		}
		s+="("+u.gibString(0, variablenName)+")"+"^";
		s+="("+g.gibString(0, variablenName)+")";
		return s;
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
	public Funktionsteil clone() {
		return new FunktionsteilPotenz(a,u.clone(), g.clone());
	}

	@Override
	public String[] gibVerkettungString(int i) {
		String[] sA = new String[u.gibVerkettungString(0).length+g.gibVerkettungString(0).length];
		String s = "";
		double a = runde(this.a, 14);
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

			s += (a == (int) (a) ? (int) (a) + "" : ueberpruefeDouble(a) + "*");
		}
		String usA[] = u.gibVerkettungString(0);
		for (int c = 0; c< sA.length; c++) {
			sA[c] = "";
		}
		int c1 =0;
		sA[0] =s+"(";
		for(c1=0; c1 < usA.length; c1++) {
			sA[c1] += usA[c1];
		}
		sA[c1] +=")^";
		String gsA[] = g.gibVerkettungString(0);
		sA[c1+1]= "("+gsA[0];
		for(int c2 =1; c2<gsA.length; c2++) {
			sA[c1+c2+1] += gsA[c2];
		}
		sA[sA.length-1]+=")";
		return sA;
	}
	public Funktionsteil gibG() {
		return this.g;
	}
	public Funktionsteil gibU() {
		return u;
		
	}
}
