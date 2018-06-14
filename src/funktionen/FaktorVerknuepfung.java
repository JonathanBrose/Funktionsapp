package funktionen;
 
import java.util.ArrayList;

/**
 * Klasse die eine Multiplikation mehrerer Funktionen darstellt. <br>
 * Die Form sieht wiefolgt aus: <code>f(x) = u(x) * v(x) * ... </code>
 * <br>Dieses {@link Funktionsteil} gibt bei {@link #gibStammfunktion()} null zur&uuml;ck.
 * @author Jonathan Brose, Nick Dressler 
 */

public class FaktorVerknuepfung extends Verknuepfung {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3100011062478552744L;
	/**
	 * Speichert die {@link Funktionsteil}e die miteinander multipliziert werden.
	 */
	private ArrayList<Funktionsteil> dieFunktionsTeile;
	/**
	 * 
	 * Klasse die eine Multiplikation zweier Funktionen darstellt. <br>
	 * Die Form sieht wiefolgt aus: <code>f(x) = u(x) * v(x)</code>
	 * 
	 * @param dieFunktionsTeile {@link ArrayList} mit den Funtionsteilen die alle miteinander multipliziert werden
	 */
	public FaktorVerknuepfung(ArrayList<Funktionsteil> dieFunktionsTeile) {
		this.dieFunktionsTeile = dieFunktionsTeile;
		while (enthaeltFaktorVerknuepfung())
			fasseZusammen();
	}
	/**
	 * 
	 * Klasse die eine Multiplikation mehrerer Funktionen darstellt. <br>
	 * Die Form sieht wiefolgt aus: <code>f(x) = u(x) * v(x)</code>
	 * 
	 * @param dasFunktionsteilArray Array mit den Funtionsteilen die alle miteinander multipliziert werden
	 */
	public FaktorVerknuepfung(Funktionsteil... dasFunktionsteilArray) {
		this.dieFunktionsTeile = new ArrayList<Funktionsteil>();
		for (int i = 0; i < dasFunktionsteilArray.length; i++) {
			dieFunktionsTeile.add(dasFunktionsteilArray[i]);
		}
		while (enthaeltFaktorVerknuepfung())
			fasseZusammen();
	}

	@Override
	public Funktionsteil gibAbleitung() {
		Funktionsteil dieAbleitung=null;
		if(dieFunktionsTeile.size()>2) {
			ArrayList<Funktionsteil> dieLinkenFunktionsteile=new ArrayList<Funktionsteil>(), dieRechtenFunktionsteile=new ArrayList<Funktionsteil>();
			
			for(int i=0;i<dieFunktionsTeile.size()/2;i++) {
				dieLinkenFunktionsteile.add(dieFunktionsTeile.get(i));
			}
			for(int i=(dieFunktionsTeile.size()/2);i<dieFunktionsTeile.size();i++) {
				dieRechtenFunktionsteile.add(dieFunktionsTeile.get(i));
			}
			Funktionsteil dasRechteFunktionsteil=new FaktorVerknuepfung(dieRechtenFunktionsteile);
			Funktionsteil dasLinkeFunktionsteil=(dieLinkenFunktionsteile.size() ==1? dieLinkenFunktionsteile.get(0): new FaktorVerknuepfung(dieLinkenFunktionsteile));
			dieAbleitung = new AddVerknuepfung(new FaktorVerknuepfung(dasLinkeFunktionsteil,dasRechteFunktionsteil.gibAbleitung()),new FaktorVerknuepfung(dasLinkeFunktionsteil.gibAbleitung(),dasRechteFunktionsteil));
		}else {
			Funktionsteil u=dieFunktionsTeile.get(0), f=dieFunktionsTeile.get(1);
			dieAbleitung = new AddVerknuepfung(new FaktorVerknuepfung(f,u.gibAbleitung()),new FaktorVerknuepfung(f.gibAbleitung(),u));
		}
		
		return dieAbleitung;
	}
	
	@Override
	public double gibFunktionswert(double x0) {
		double funktionswert = 1;
		for (Funktionsteil dasFunktionsteil : dieFunktionsTeile) {
	
			funktionswert *= dasFunktionsteil.gibFunktionswert(x0);
		}
		return funktionswert;
	}
	@Override
	public String gibString(int i, String variablenName) {
		String s = "";
	
	
		for (Funktionsteil dasFunktionsteil : dieFunktionsTeile) {
			
			s += dasFunktionsteil.gibString(i, variablenName) + " * ";
			i = 0;
		}
		//entfernen des letzten " * "
		s = s.substring(0, s.length() - 3);
	
		return s;
	}
	@Override
	public String[] gibVerkettungString(int i) {
		if (dieFunktionsTeile.size() == 1) {
			return dieFunktionsTeile.get(0).gibVerkettungString(i);
		} else if (dieFunktionsTeile.size() == 0)
			return new String[2];
	
		int anzahl = 0;
		for (Funktionsteil dasFunktionsteil : dieFunktionsTeile) {
			anzahl += dasFunktionsteil.gibVerkettungString(1).length;
		}
		Funktionsteil dasFunktionsteil;
		String[] verkettungString = new String[anzahl];
		int c = 0;
		for (int j = 0; j < dieFunktionsTeile.size(); j++) {
			dasFunktionsteil = dieFunktionsTeile.get(j);
			String innereVerkettungString[] = dasFunktionsteil.gibVerkettungString(0);
	
			for (int c1 = 0; c1 < innereVerkettungString.length; c1 += 2) {
	
				verkettungString[c + c1] = innereVerkettungString[c1];
				verkettungString[c + c1 + 1] = innereVerkettungString[c1 + 1];
				if (c + c1 + 1 < verkettungString.length - 1) {
					verkettungString[c + c1 + 1] += " * ";
				}
			}
			c += innereVerkettungString.length;
	
		}
	
		return verkettungString;
	
	}
	@Override
	public boolean entspricht(Funktionsteil dasFunktionsteil1) {
	
		if (dasFunktionsteil1 instanceof FaktorVerknuepfung) {
			ArrayList<Funktionsteil> dieFunktionsteile = new ArrayList<Funktionsteil>();
			dieFunktionsteile.addAll(dieFunktionsTeile);
			for (Funktionsteil dasFunktionsteil : ((FaktorVerknuepfung) dasFunktionsteil1).gibFunktionsteile()) {
				boolean entspricht = false;
				for (Funktionsteil dasInnereFunktionsteil : dieFunktionsTeile) {
					if (dasFunktionsteil.entspricht(dasInnereFunktionsteil) && dieFunktionsteile.contains(dasInnereFunktionsteil)) {
						entspricht = true;
						dieFunktionsteile.remove(dasInnereFunktionsteil);
						break;
					}
				}
				if (!entspricht) {
					return false;
				}
			}
			return true;
	
		} else
			return false;
	}
	@Override
	public Funktionsteil clone() {
		ArrayList<Funktionsteil> dieNeuenFunktionsteile = new ArrayList<Funktionsteil>();
		for (Funktionsteil dasFunktionsteil : dieFunktionsTeile) {
			dieNeuenFunktionsteile.add(dasFunktionsteil.clone());
		}
		return new FaktorVerknuepfung(dieNeuenFunktionsteile);
	
	}
	@Override
	public void multipliziere(double a) {
		dieFunktionsTeile.get(0).multipliziere(a);
	}


	@Override
	public double gibA() {
		double a = 1;
		for (Funktionsteil dasFunktionsteil : dieFunktionsTeile) {
			a *= dasFunktionsteil.gibA();
		}
		return a;
	}
	@Override
	public void setzeA(double a) {
		this.dieFunktionsTeile.get(0).setzeA(a);
	}
	/** 
	 * Multipliziert alle Vorfaktoren a miteinander und setzt diese Summe dann als Vorfaktor des ersten {@link Funktionsteil}s<br>
	 * 
	 * 
	 * <b>Beispiel:</b><br>
	 * 	<ul><li> funktionsteile: <code> 3*sin(x) *4x<sup>2</sup> *1,5e<sup>x</sup></code><br>
	 * </ul>
	 * <b>Ergibt:</b><br>
	 * <ul>
	 * <li> funktionsteile: <code> 18*sin(x) *x<sup>2</sup> *e<sup>x</sup></code><br>
	 * </ul>
	 * 
	 */
	public void fasseVorfaktorenZusammen() {
		double neuesA = 1;
		for (Funktionsteil dasFunktionsteil : dieFunktionsTeile) {
			neuesA *= dasFunktionsteil.gibA();
			dasFunktionsteil.setzeA(1);
		}
		dieFunktionsTeile.get(0).multipliziere(neuesA);
	}
	/**
	 * F&uuml;gt den Parameter der {@link AddVerknuepfung} hinzu.
	 * 
	 * @param dasFunktionsteil
	 *            {@link Funktionsteil} das hinzugef&uuml;gt werden soll
	 */


	public void fuegeHinzu(Funktionsteil dasFunktionsteil) {
		this.dieFunktionsTeile.add(dasFunktionsteil);
		if(this.enthaeltFaktorVerknuepfung())fasseZusammen();
	}

	/**
	 * F&uuml;gt alle {@link Funktionsteil}e dieser {@link AddVerknuepfung} hinzu.
	 * 
	 * @param dieFunktionsteile
	 *            {@link ArrayList} mit Funktionsteilen die hinzugef&uuml;gt werden
	 *            sollen
	 */
	public void fuegeAlleHinzu(ArrayList<Funktionsteil> dieFunktionsteile) {
		this.dieFunktionsTeile.addAll(dieFunktionsteile);
		if(this.enthaeltFaktorVerknuepfung())fasseZusammen();
	}
	/**
	 * 
	 * @return {@link ArrayList} funktionsTeile
	 */

	public ArrayList<Funktionsteil> gibFunktionsteile() {
		return dieFunktionsTeile;
	}

	/**
	 * 
	 * @param dieKlasse
	 *            Objekt einer Klasse von der Elemente zur&uuml;ckgegeben werden sollen
	 * 
	 * @return gibt alle Objekte der Klasse der das &uuml;bergebene Objekt angehört
	 *         zur&uuml;ck
	 * 
	 * 
	 */

	public ArrayList<Funktionsteil> gibAlleDieserKlasse(Funktionsteil dieKlasse) {
		ArrayList<Funktionsteil> dieRueckgabeListe = new ArrayList<Funktionsteil>();
		for (Funktionsteil dasFunktionsteil : dieFunktionsTeile) {
			if (dasFunktionsteil.getClass() == dieKlasse.getClass()) {
				dieRueckgabeListe.add(dasFunktionsteil);
			}
		}
		return dieRueckgabeListe;
	}


	/**
	 * Verschiebt, falls sich {@link FaktorVerknuepfung}en in der Liste dieser Instanz befinden,
	 *  alle Inhalte dieser FaktorVerknuepfung in die Hauptliste <br> <br>
	 *  
	 * <b> Beispiel:</b>
	 *  <ul><li>	FaktorVerknuepfung.funktionsTeile[{@link FaktorVerknuepfung}[{@link Sin} , {@link PotenzFunktion}], {@link ExponentialFunktion}]</li></ul>
	 *  <ul><li>	toString: <code>"(Sin * PotenzFunktion) * ExponentialFunktion"</code></li></ul>
	 *  <b>ergibt:</b>
	 *  <ul><li> 	FaktorVerknuepfung.funktionsTeile[{@link Sin}, {@link PotenzFunktion}, {@link ExponentialFunktion}]</li></ul>
	 *  <ul><li>	toString:<code> "Sin * PotenzFunktion * ExponentialFunktion"</code></li></ul>
	 */
	private void fasseZusammen() {
		ArrayList<Funktionsteil> zuLoeschendeFunktionsteile = new ArrayList<Funktionsteil>();
		ArrayList<Funktionsteil> hinzuzufuegendeFunktionsteile = new ArrayList<Funktionsteil>();
		for (Funktionsteil dasFunktionsteil : dieFunktionsTeile) {
			if (dasFunktionsteil instanceof FaktorVerknuepfung) {
				for (Funktionsteil dasInnereFunktionsteil : ((FaktorVerknuepfung) dasFunktionsteil).gibFunktionsteile()) {
					hinzuzufuegendeFunktionsteile.add(dasInnereFunktionsteil);
				}
				zuLoeschendeFunktionsteile.add(dasFunktionsteil);
			}
		}
		dieFunktionsTeile.removeAll(zuLoeschendeFunktionsteile);
		dieFunktionsTeile.addAll(hinzuzufuegendeFunktionsteile);
	}
	/**
	 * 
	 * &uuml;berpr&uuml;ft, ob in der sich in der funktionsListe
	 * {@link FaktorVerknuepfung}en befinden.
	 * 
	 * @return - true oder false
	 */
	private boolean enthaeltFaktorVerknuepfung() {
		for (Funktionsteil dasFunktionsteil : dieFunktionsTeile) {
			if (dasFunktionsteil instanceof FaktorVerknuepfung) {
				return true;
			}
		}
		return false;
	}
}
