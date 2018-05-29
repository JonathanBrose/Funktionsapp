package src;
 
import static src.Hilfsmethoden.ueberpruefeDouble;;
/**
 * 
 * Klasse, die ein Intervall mit den Grenzen Links und Rechts repr&auml;sentiert.
 * 
 * @author Nick Dressler
 *
 */
public class Intervall {
	/**
	 * Stellt die linke Grenze des Intervalls dar. der Wert selbst geh&ouml;rt noch zum Intervall.
	 */
	private double linkerWert;
	/**
	 * Stellt die rechte Grenze des Intervalls dar. der Wert selbst geh&ouml;rt noch zum Intervall.
	 */
	private double rechterWert;

	/**
	 * 
	 * Konstruktor der Klasse Intervall mit zwei double-Parameter, welche die
	 * Grenzen darstellen.
	 * 
	 * @param linkerWert
	 *            Der Anfang, oder auch linke Grenze des Intervalls
	 * @param rechterWert
	 *            Das Ende, oder auch die rechte Grenze des Intervalls
	 */

	public Intervall(double linkerWert, double rechterWert) {
		if (rechterWert < linkerWert) {
			double zwischenSpeicher = linkerWert;
			linkerWert = rechterWert;
			rechterWert = zwischenSpeicher;
		}
		this.linkerWert = linkerWert;
		this.rechterWert = rechterWert;
	}

	/**
	 * Überschriebene toString() Methode, welche den Intervall in folgender Form
	 * als String zurückgibt.<br>
	 * <code>[a ; b]</code>
	 */
	@Override
	public String toString() {
		return "[" + ueberpruefeDouble(linkerWert)+ " ; " + ueberpruefeDouble(rechterWert) + "]";
	}

	/**
	 * Prüft, ob eine Ganzzahl in dem Intervall liegt.
	 * 
	 * @param x
	 *            Ganzzahl
	 * @return Boolean:<br>
	 *         true, wenn x in dem Intervall liegt, <br>
	 *         false wenn nicht.
	 */
	public boolean enthaelt(int x) {
		return x >= linkerWert && x <= rechterWert;
	}

	/**
	 * 
	 * @return linke Grenze des Intervalls als double
	 */
	public double gibLinkenWert() {
		return linkerWert;
	}

	/**
	 * Setter für die linke Grenzes
	 * 
	 * @param linkerWert
	 *            Ganzzahl für die Linke Grenze
	 */
	public void setzeLinkenWert(double linkerWert) {
		this.linkerWert = linkerWert;
	}

	/**
	 * 
	 * @return rechte Grenze des Intervalls als double
	 */
	public double gibRechtenWert() {
		return rechterWert;
	}

	/**
	 * Setter für die rechte Grenzes
	 * 
	 * @param rechterWert
	 *            Ganzzahl für die Rechte Grenze
	 */
	public void setzeRechtenWert(double rechterWert) {
		this.rechterWert = rechterWert;
	}

	/**
	 * Statische Methode, welche ohne Instanziierung eines Objektes aufgerufen
	 * werden kann.
	 * 
	 * @return gibt ein Intervall mit den Grenzen des Datentyps Short, zurück.
	 */
	public static Intervall R() {
		return new Intervall(Short.MIN_VALUE, Short.MAX_VALUE);
	}


}
