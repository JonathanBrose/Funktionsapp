package src;

/**
 * Klasse die eine Verkettung zweier Funktionen folgender Form darstellt:<br>
 * <code>f(x) = f(u(x))</code>
 * <br>Dieses {@link Funktionsteil} gibt bei {@link #gibStammfunktion()} null zurück
 * @author Jonathan Brose, Nick Dressler 
 *
 */

public class Verkettung extends Verknuepfung {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5338877732578061538L;
	/**
	 * Das &auml;u&szlig;ere Funktionsteil der Verkettung.
	 */
	private Funktionsteil f;
	/**
	 * Das innere Funktionsteil der Verkettung
	 */
	private Funktionsteil u;

	/**
	 * Klasse die eine Verkettung zweier Funktionen folgender Form
	 * darstellt.<br>
	 * <code>f(x) = f(u(g))</code>
	 * 
	 * @param f
	 *            erste, äussere Funktion
	 * @param u
	 *            zweite, innere Funktion
	 */
	public Verkettung(Funktionsteil f, Funktionsteil u) {
		this.f = f;
		this.u = u;
	}

	@Override
	public Funktionsteil gibAbleitung() {
		return new FaktorVerknuepfung(new Verkettung(f.gibAbleitung(), u), u.gibAbleitung());

	}

	@Override
	public double gibFunktionswert(double x0) {
		return this.f.gibFunktionswert(this.u.gibFunktionswert(x0));
	}

	@Override
	public String[] gibVerkettungString(int i) {
		String[] sA = new String[2];
		sA[0] = f.gibVerkettungString(i)[0] + u.gibVerkettungString(0)[0];
		sA[1] = u.gibVerkettungString(0)[1] + f.gibVerkettungString(0)[1];
		return sA;
	}

	@Override
	public boolean entspricht(Funktionsteil dasFunktionsteil) {
		if (dasFunktionsteil instanceof Verkettung)
			return (f.entspricht(((Verkettung) dasFunktionsteil).getF()) && u.entspricht(((Verkettung) dasFunktionsteil).getU()));
		else
			return false;
	}

	@Override
	public Funktionsteil clone() {
		return new Verkettung(f.clone(), u.clone());
	}

	@Override
	public void multipliziere(double a) {
		f.multipliziere(a);
	}

	@Override
	public String gibString(int i, String variablenName) {
		String s = "";
		for (int c = 0; c < f.gibVerkettungString(0).length; c += 2) {
			s += f.gibVerkettungString(i)[0 + c];
			if (s.endsWith("(") && u instanceof AddVerknuepfung) {
				s = s.substring(0, s.length() - 1);
				s += u.gibString(0, variablenName);
				String s2 = f.gibVerkettungString(i)[1 + c];
				if (s2.length() > 1)
					s += s2.substring(1, s2.length());

			} else if (f instanceof PotenzFunktion && (u instanceof Cos || u instanceof Sin)) {
				s = "";
				Funktionsteil f1 = f.clone();
				f1.multipliziere(Math.pow(u.gibA(),((PotenzFunktion) f).gibP()));
				if (u instanceof Cos) {
					String s1 = f1.gibVerkettungString(i)[0];
					s1 = s1.substring(0, s1.length() - 1);
					String s2 = f1.gibVerkettungString(i)[1];
					s += s1 + "cos(" + variablenName + s2;
				} else if (u instanceof Sin) {
					String s1 = f1.gibVerkettungString(i)[0];
					s1 = s1.substring(0, s1.length() - 1);
					String s2 = f1.gibVerkettungString(i)[1];
					s += s1 + "sin(" + variablenName + s2;
				}

			} else {
				s += u.gibString(0, variablenName);
				s += f.gibVerkettungString(i)[1 + c];
			}
		}
		return s;
	}

	@Override
	public void setzeA(double a) {
		f.setzeA(a);
	}

	@Override
	public double gibA() {
		return f.gibA();
	}

	/**
	 * 
	 * @return gibt die äußere Funktion zurück.
	 */
	public Funktionsteil getF() {
		return f;
	}

	/**
	 * 
	 * @return gibt die innere Funktion zurück.
	 */
	public Funktionsteil getU() {
		return u;
	}
}
