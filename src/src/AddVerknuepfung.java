package src;

import java.util.ArrayList;
/**
*
*Speichert mehrere {@link Funktionsteil}e. Diese sind dann mit + bzw - verknuepft.
*<br>zum Beispiel:
*<ul><li>{@link Sin}(1), {@link Cos}(2) und {@link PotenzFunktion}(2,3)</li></ul>
*ergibt: 
*<ul><li>sin(x) + cos(x) + 2x<sup>3</sup></li></ul>
*Diese {@link Verknuepfung} gibt bei {@link #gibStammfunktion()} nicht null zur&uuml;ck, obwohl sie eine {@link Verknuepfung} ist.
*@author Jonathan Brose, Nick Dressler
*/
public class AddVerknuepfung extends Verknuepfung {
	
	private static final long serialVersionUID = 4256856795591018174L;
	/**
	 * Speichert die {@link Funktionsteil}e der AddVerknuepfung.
	 */
	private ArrayList<Funktionsteil> dieFunktionsTeile;

	public AddVerknuepfung(ArrayList<Funktionsteil> dieFunktionsTeile) {
		this.dieFunktionsTeile = dieFunktionsTeile;
		while (enthaeltAddverknuepfung())
			fasseZusammen();
	}

	public AddVerknuepfung(Funktionsteil... dasFunktionsteilArray) {
		this.dieFunktionsTeile = new ArrayList<Funktionsteil>();
		for (int i = 0; i < dasFunktionsteilArray.length; i++) {
			dieFunktionsTeile.add(dasFunktionsteilArray[i]);
		}
		while (enthaeltAddverknuepfung())
			fasseZusammen();

	}

	@Override
	public Funktionsteil gibAbleitung() {
	
		ArrayList<Funktionsteil> dieRueckgabeListe = new ArrayList<Funktionsteil>();
		for (Funktionsteil dasFunktionsteil : dieFunktionsTeile) {
			dieRueckgabeListe.add(dasFunktionsteil.gibAbleitung());
		}
		return new AddVerknuepfung(dieRueckgabeListe);
	
	}

	@Override
	public Funktionsteil gibStammfunktion() {
		ArrayList<Funktionsteil> dieRueckgabeListe = new ArrayList<Funktionsteil>();
		for (Funktionsteil dasFunktionsteil : dieFunktionsTeile) {
			dieRueckgabeListe.add(dasFunktionsteil.gibStammfunktion());
		}
		return new AddVerknuepfung(dieRueckgabeListe);
	}

	@Override
	public void multipliziere(double a) {
		for (Funktionsteil dasFunktionsteil : dieFunktionsTeile) {
			dasFunktionsteil.multipliziere(a);
		}
	
	}

	@Override
	public double gibFunktionswert(double x0) {
		boolean naN=false;
		double funktionswert = 0;
		Double testFunktionswert =0.0;
		for (Funktionsteil dasFunktionsteil : dieFunktionsTeile) {
			testFunktionswert = dasFunktionsteil.gibFunktionswert(x0);
			if(!testFunktionswert.isNaN()) {
				funktionswert+=testFunktionswert;
				naN=false;
			}else
				naN=true;
		}
		if(naN)
			return Double.NaN;
		
		return funktionswert;
	}

	@Override
	public String gibString(int i, String variablenName) {
		String s = "(";
		for (Funktionsteil dasFunktionsteil : dieFunktionsTeile) {
			if (dieFunktionsTeile.indexOf(dasFunktionsteil) != 0)
				s += " ";
	
			i = dieFunktionsTeile.indexOf(dasFunktionsteil) * -1;
			s += "" + dasFunktionsteil.gibString(i, variablenName);
		}
		s += ")";
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
			String innererVerkettungString[] = dasFunktionsteil.gibVerkettungString(1);
	
			for (int c1 = 0; c1 < innererVerkettungString.length; c1 += 2) {
				if (j == 0) {
					verkettungString[0] = "(";
				} else {
					verkettungString[c + c1] = "";
				}
				verkettungString[c + c1] += dasFunktionsteil.gibVerkettungString(i + j)[c1];
				verkettungString[c + c1 + 1] = innererVerkettungString[c1 + 1];
	
				if (c + c1 + 1 == verkettungString.length - 1) {
					verkettungString[c + c1 + 1] += ")";
				} else {
					verkettungString[c + c1 + 1] += " ";
				}
			}
			c += innererVerkettungString.length;
	
		}
	
		return verkettungString;
	
	}

	@Override
	public Funktionsteil clone() {
		ArrayList<Funktionsteil> dieRueckgabeListe = new ArrayList<Funktionsteil>();
		for (Funktionsteil dasFunktionsteil : dieFunktionsTeile) {
			dieRueckgabeListe.add(dasFunktionsteil.clone());
		}
		return new AddVerknuepfung(dieRueckgabeListe);
	}

	@Override
	public boolean entspricht(Funktionsteil dasFunktionsteil1) {
	
		if (dasFunktionsteil1 instanceof AddVerknuepfung) {
			ArrayList<Funktionsteil> dieFunktionsteile = new ArrayList<Funktionsteil>();
			dieFunktionsteile.addAll(dieFunktionsTeile);
			for (Funktionsteil dasFunktionsteil : ((AddVerknuepfung) dasFunktionsteil1).gibFunktionsteile()) {
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
	public double gibA() {
		return 1;
	}

	@Override
	public void setzeA(double a) {
		
	}

	/**
	 * F&uuml;gt den Parameter der {@link AddVerknuepfung} hinzu.
	 * 
	 * @param dasFunktionsteil
	 *            {@link Funktionsteil} das hinzugef&uuml;gt werden soll
	 */
	
	public void fuegeHinzu(Funktionsteil dasFunktionsteil) {
		this.dieFunktionsTeile.add(dasFunktionsteil);
		if (this.enthaeltAddverknuepfung())
			fasseZusammen();
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
		if (this.enthaeltAddverknuepfung())
			fasseZusammen();
	}

	/**
	 * 
	 * @param dieKlasse
	 *            Objekt einer Klasse von der Elemente zur&uuml;ckgegeben werden sollen
	 * 
	 * @return gibt alle Objekte der Klasse der das &uuml;bergebene Objekt angeh&ouml;rt
	 *         zurück
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

	public ArrayList<Funktionsteil> gibFunktionsteile() {
		return dieFunktionsTeile;
	}

	/**
	 * Verschiebt, falls sich {@link AddVerknuepfung}en in der Liste dieser Instanz
	 * befinden, alle Inhalte dieser AddVerknuepfungen in die Hauptliste <br>
	 * <br>
	 * 
	 * <b> Beispiel:</b><br>
	 * <ul><li>AddVerknuepfung.funktionsTeile[{@link AddVerknuepfung}[{@link Sin} ,
	 * {@link PotenzFunktion}], {@link ExponentialFunktion}]</li></ul>
	 * <ul><li>toString: <code>"(Sin + PotenzFunktion) + ExponentialFunktion"</code></li></ul>
	 * <b>ergibt:</b> 
	 * <ul><li>AddVerknuepfung.funktionsTeile[{@link Sin}, {@link PotenzFunktion},
	 * {@link ExponentialFunktion}]</li></ul>
	 * <ul><li>toString:<code> "Sin + PotenzFunktion + ExponentialFunktion"</code></li></ul>
	 */
	private void fasseZusammen() {
		ArrayList<Funktionsteil> dieZuLöschendeListe = new ArrayList<Funktionsteil>();
		ArrayList<Funktionsteil> dieHinzuzufuegendeListe = new ArrayList<Funktionsteil>();
		for (Funktionsteil dasFunktionsteil : dieFunktionsTeile) {
			if (dasFunktionsteil instanceof AddVerknuepfung) {
				for (Funktionsteil dasInnereFunktionsteil : ((AddVerknuepfung) dasFunktionsteil).gibFunktionsteile()) {
					dieHinzuzufuegendeListe.add(dasInnereFunktionsteil);
				}
				dieZuLöschendeListe.add(dasFunktionsteil);
			}
		}
		dieFunktionsTeile.removeAll(dieZuLöschendeListe);
		dieFunktionsTeile.addAll(dieHinzuzufuegendeListe);
	
	}

	/**
	 * 
	 * Überprüft, ob in der sich in der funktionsListe {@link AddVerknuepfung}en
	 * befinden.
	 * 
	 * @return - true oder false
	 */
	private boolean enthaeltAddverknuepfung() {
		for (Funktionsteil dasFunktionsteil : dieFunktionsTeile) {
			if (dasFunktionsteil instanceof AddVerknuepfung) {
				return true;
			}
		}
		return false;
	}

	
}
