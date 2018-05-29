package src;

import java.io.Serializable;

/**
 * Diese Klasse ist die Vaterklasse aller Funktionsteile, <br>
 * die dann in {@link Funktion} eingesetzt werden können.
 * @author Jonathan Brose, Nick Dressler
 */

public abstract class Funktionsteil implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8104874808387497036L;
	/**
	 * Gibt den Funktionsterm des Funktionsteils zur&uuml;ck.
	 * ruft {@link #gibString(int, String)} mit dem i=0 und dem VariablenName "x" auf.
	 * @return der Funktionsterm
	 * @see #gibString(int, String)
	 */
	@Override
	public String toString() {
		return gibString(0, "x");
	}

	/**
	 * Vergleicht dieses mit dem &Uuml;bergebenen Funktionsteil, ob deren Klasse und Attribute identisch sind.
	 * @param dasFunktionsteil das zu vergleichende Funktionsteil
	 * @return gibt true zurück, wenn der Parameter dieFunktion die gleichen Werte hat.
	 */
	public abstract boolean entspricht(Funktionsteil dasFunktionsteil);

	/**
	 * Multipliziert die Funktion mit dem Parameter a.
	 * 
	 * @param a
	 *            double a
	 */
	public abstract void multipliziere(double a);

	/**
	 * Berechnet und gibt die Ableitung der gespeicherten Funktion zur&uuml;ck.
	 * @return gibt die Ableitung der Funktion als {@link Funktionsteil} zurück
	 */
	public abstract Funktionsteil gibAbleitung();

	/**
	 * Berechnet und gibt die Stammfunktion der gespeicherten Funktion zur&uuml;ck.
	 * Die y-Verschiebung C ist immmer 0.
	 * @return gibt eine Stammfunktion als {@link Funktionsteil} zurück.
	 */
	public abstract Funktionsteil gibStammfunktion();

	/**
	 * 
	 * Berechnet den Funktionwert an der Stelle x0.
	 * <br><code>return = f(x0)</code>
	 * @return gibt den Funktionswert an der Stelle x0 als double zurück
	 * 
	 * @param x0
	 *            Die Stelle an der der Funktionswert errechnet werden soll
	 */
	public abstract double gibFunktionswert(double x0);

	/**
	 * @param i
	 *            :<br>
	 *            bei <code>i &gt; 0</code> beginnt der String mit dem Vorzeichen von dem
	 *            Vorfaktor a, also + bzw. - <br>
	 *            bei <code>i=0</code> beginnt der String bei einem positiven Wert
	 *            ohne Vorzeichen, also nur ein - falls negativ <br>
	 *            Beispiel: <code> -4x^2 </code> <br>
	 *            bei <code>i  &lt; 0</code> beginnt der String wie bei <code>i &gt; 0</code>,
	 *            nur mit einem zusätzlichen Leerzeichen.<br>
	 *            z.B.<code>  - 4x^2</code> oder <code> + 4x^2 </code>
	 * 
	 * @param variablenName
	 *            <br>
	 *            Name der Funktionsvariable. <br>
	 *            z.B. <code>f(vName) = vName^2</code>
	 *
	 * @return gibt die Formel des gespeicherten {@link Funktionsteil}s als
	 *         Zeichenkette zurück.
	 */
	public abstract String gibString(int i, String variablenName);

	/**
	 * 
	 * @return gibt den Vorfaktor a zurück
	 */
	public abstract double gibA();

	/**
	 * Setzt einen neuen Wert f&uuml;r den Vorfaktor a.
	 * @param a
	 *            neuer Wert für a
	 */
	public abstract void setzeA(double a);

	/**
	 * Kopiert das Objekt mit seinen Attributen.
	 * <br>
	 *         <b>Hinweis:</b><br>
	 *         Dabei werden alle im Objekt gespeicherten Instanzen von Objekten
	 *         ebenfalls kopiert.
	 * 
	 * @return gibt eine Kopie des Objekts zurück <br>
	 *         
	 * 
	 */
	public abstract Funktionsteil clone();

	/**
	 * Wird benötigt für toString(i,vName) in {@link Verkettung}.
	 * Fast wie {@link #gibString(int, String)}, nur das an der Stelle wo der VariablenName ist gesplittet wird und diese Array mit den H&auml;lften 
	 * wird zur&uuml;ckgegeben.
	 * 
	 * @param i
	 *            <br>
	 *            bei <code>i &gt; 0</code> beginnt der String mit dem Vorzeichen von dem
	 *            Vorfaktor a, also + bzw. - <br>
	 *            bei <code>i=0</code> beginnt der String bei einem positiven Wert
	 *            ohne Vorzeichen, also nur ein - falls negativ <br>
	 *            Beispiel: <code> -4x^2 </code> <br>
	 *            bei <code>i &lt; 0</code> beginnt der String wie bei <code>i &gt; 0</code>,
	 *            nur mit einem zusätzlichen Leerzeichen.<br>
	 *            z.B.<code>  - 4x^2</code> oder <code> + 4x^2 </code>
	 * @return ein Array mit den Teilen links und rechts von der Funktionsvariable
	 *         als String. <br>
	 *         z.B. für <code> -2x^3 </code> gibt die methode
	 *         <code> ["-2x("; ")^3"]</code> zurück. <br>
	 *         zwischen den Klammern kann nun die innere Funktion eingefügt werden.
	 *         <br>
	 *         z.B. <code> -2x(sin(4))^3</code>
	 */
	public abstract String[] gibVerkettungString(int i);
	
	/**
	 * &Uuml;berpr&uuml;ft den double ob er ein Vielfaches von pi oder e ist.<br>
	 * Die eigentliche Methode liegt in {@link Hilfsmethoden}.
	 * Dies spart die static imports in den Unterklassen von Funktionsteil.
	 * @param d double der auf beinhaltung von pi und e &uuml;berpr&uuml;ft werden soll.
	 * @return d als Text.
	 * @see src.Hilfsmethoden#ueberpruefeDouble(double)
	 */
	String ueberpruefeDouble(double d) {
		return Hilfsmethoden.ueberpruefeDouble(d);
	}

}
