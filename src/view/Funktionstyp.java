package view;

import src.ExponentialFunktion;
import src.FaktorVerknuepfung;
import src.Funktionsteil;
import src.NatuerlicheExponentialFunktion;
import src.PotenzFunktion;
import src.Verkettung;
import src.XExponentialFunktion;
/**
 * Enth&auml;lt f&uuml; die Verschiedenen {@link Funktionsteil}e Beschreibung, parameterAnzahl und Funktionsterm.
 * @author Jonathan
 *
 */
public enum Funktionstyp {
	/**
	 * Enth&auml;lt die f&uuml;r {@link EingabeController} n&ouml;tige Informationen zum Erstellen einer {@link PotenzFunktion} und die Passende Beschreibung.
	 */
	Polynom("f(x) = a*x^n", new String[] { "*x^", "" },
			"Eine Polynomfunktion mit der Form a*x^p kann jeden"
			+ " Grad einer Potenzfunktion darstellen.\n Außerdem dient sie der Darstellung von Wurzelfunktionen, wobei bei a*x^(1/n)"
			+ " \n n der Grad der Wurzel ist. \n\n\n Tipp: sie können auch Brüche eingeben."),
	/**
	 * Enth&auml;lt die f&uuml;r {@link EingabeController} n&ouml;tige Informationen zum Erstellen einer {@link Sin}usFunktion und die Passende Beschreibung.
	 */
	Sin("f(x) = a*sin(x)", new String[] { "*sin(x)" },
			"Eine Sinusfunktion hat einen periodischen Verlauf und hat eine Periode von 2\u03C0.\n"
			+ "Die Amplitude wird durch den eingebenen Wert a bestimmt, da Sin(x) Werte von -1 bis 1 liefert.\n"
			+ "\n\n\n Tipp: Sie können in allen Eingabefeldern auch pi eingeben."), 
	/**
	 * Enth&auml;lt die f&uuml;r {@link EingabeController} n&ouml;tige Informationen zum Erstellen einer {@link Cos}inusFunktion und die Passende Beschreibung.
	 */
	Cos("f(x) = a*cos(x)", new String[] { "*cos(x)" },
			"Eine Cosinusfunktion hat wie Sinus einen periodischen Verlauf und hat eine Periode von 2\u03C0.\n"
			+ "Allerdings ist Cosinus in X-Richtung verschoben"
			+ "Die Amplitude wird durch den eingebenen Wert a bestimmt, da Cos(x) Werte von -1 bis 1 liefert.\n"
			+ "\n\n Tipp: Sie können in allen Eingabefeldern auch pi eingeben."), 
	/**
	 * Enth&auml;lt die f&uuml;r {@link EingabeController} n&ouml;tige Informationen zum Erstellen einer {@link ExponentialFunktion} und die Passende Beschreibung.
	 */
	Exponential("f(x) = a*b^x", new String[] { "*", "^x" },
			"Eine Exponentialfunktion der Form a*b^x zur variablen Basis b."
			+ "\nmit dieser Funktion werden für gewöhnlich Wachstumsverläufe beschrieben."
			+ "\n\n\n\n Tipp: Sie können in allen Eingabefeldern auch e eingeben"),
	/**
	 * Enth&auml;lt die f&uuml;r {@link EingabeController} n&ouml;tige Informationen zum Erstellen einer {@link NatuerlicheExponentialFunktion} und die Passende Beschreibung.
	 */
	NatürlicheExponential("f(x) = a*e^x",new String[] { "*e^x" },
			"Eine Exponentialfunktion der Form a*e^x zur festen Basis e."
			+ "\ne entspricht "+Math.E+"\n"
			+ "mit dieser Funktion werden für gewöhnlich Wachstumsverläufe beschrieben."
			+ "\n\n\n Tipp: Sie können in allen Eingabefeldern auch e eingeben"), 
	/**
	 * Enth&auml;lt die f&uuml;r {@link EingabeController} n&ouml;tige Informationen zum Erstellen einer {@link XExponentialFunktion} und die Passende Beschreibung.
	 */
	XExponential("f(x) = a*x^x",new String[] {"*x^x"},
			"Eine Exponentialfunktion der Form a*x^x zur Basis x."
			+ "\n dies Funktion hat die Form eines Schöpflöffels\n"
			+ "\n\n\n Tipp: Sie können in allen Eingabefeldern auch e eingeben"),
	/**
	 * Enth&auml;lt die f&uuml;r {@link EingabeController} n&ouml;tige Informationen zum Erstellen einer {@link Ln}Funktion und die Passende Beschreibung.
	 */
	Ln("f(x) = a*ln(x)", new String[] {"*ln(x)"},
			"Eine Logarithmusfunktion der Form a*ln(x).\n"
			+ "Ln ist der Logarithmus zur Basis e, also Log_e(x)"
			+ "\ne entspricht "+Math.E
			+ "\n\n\n Tipp: Sie können in allen Eingabefeldern auch e eingeben"), 
	/**
	 * Enth&auml;lt die f&uuml;r {@link EingabeController} n&ouml;tige Informationen zum Erstellen einer {@link Verkettung} und die Passende Beschreibung.
	 */
	Verkettung("f(x) = u(v(x))", new String[] {},
			"Ein Verkettung besteht aus zwei einzelenen Funktionsteilen.\n"
			+ "z.B.: Sin(x) und 2x^3, das ergibt als Verkettung: Sin(2x^3)\n"
			+ "Dabei ist die erste Funktion die äußere und die zweite Funktion die innere."), 
	/**
	 * Enth&auml;lt die f&uuml;r {@link EingabeController} n&ouml;tige Informationen zum Erstellen einer {@link FaktorVerknuepfung} und die Passende Beschreibung.
	 */
	FaktorVerknuepfung("f(x) = u(x) * v(x)",new String[] {},
			"Multipliziert beliebig viele Funktionsteile miteinander \n"
			+ "z.B.: sin(x) * 2x^3 * cos(x) "), 
	/**
	 * Enth&auml;lt die f&uuml;r {@link EingabeController} n&ouml;tige Informationen zum Erstellen einer {@link src.AddVerknuepfung} und die Passende Beschreibung.
	 */
	AddVerknuepfung("f(x) = u(x) + v(x)", new String[] {},
			"Verknüpft beliebig viele Funktionsteile mit + bzw -\n"
			+ "Das ist als Inhalt einer Verkettung oder Faktorverknüpfung gedacht um \nz.B.:"
			+ "Sin(2x^3 + 3e^x) ermöglichen.");
	
	
	/**
	 * Speichert den Funktionsterm.
	 */
	private String derName;
	/**
	 * Enth&auml;t den Funktionsterm ohne die Faktoren usw.
	 * Au&szlig;erdem ist er aufgeteilt, sodass die Teile zwischen die Eingabefelder in dem {@link EingabeController} passen.
	 */
	private String[] dieParameter;
	/**
	 * Beschreibung &Uuml;ber die Funktion an sich. Wird f&uuml;r den nutzer in dem {@link EingabeController} angezeigt.
	 */
	private String dieBeschreibung;
	
	private Funktionstyp(String derName, String[] dieParameter) {
		this.derName = derName;
		this.dieParameter = dieParameter;
		this.dieBeschreibung="";
	}
	private Funktionstyp(String derName, String[] dieParameter,String dieBeschreibung) {
		this.derName = derName;
		this.dieParameter = dieParameter;
		this.dieBeschreibung=dieBeschreibung;
	}
	/**
	 * Getter f&uuml;r {@link #dieBeschreibung}
	 * @return {@link #dieBeschreibung}
	 */
	public String gibBeschreibung() {
		return dieBeschreibung;
	}
	/**
	 * Getter f&uuml;r {@link #derName}
	 * @return {@link #derName}
	 */
	public String gibName() {
		return derName;
	}
	/**
	 * Getter f&uuml;r {@link #dieParameter}
	 * @return {@link #dieParameter}
	 */
	public String[] gibParameter() {
		return dieParameter;
	}

}
