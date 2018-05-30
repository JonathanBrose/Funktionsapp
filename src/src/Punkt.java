package src;
import static src.Hilfsmethoden.ueberpruefeDouble;
import com.google.common.math.DoubleMath;
 
/**
 * Klasse, die einen 2dimensionalen Punkt repr&auml;sentiert.
 * 
 * @author Nick Dressler
 *
 */
public class Punkt {
	/**
	 * Der Name des Punktes 
	 */
	private String name;
	/**
	 * Die X-Koordinate des Punktes.
	 */
	private double x; 
	/**
	 * Die Y-Koordinate des Punktes.
	 */
	private double y;

	/**
	 * Konstruktor der Klasse, die einen 2dimensionalen Punkt repr&auml;sentiert.
	 * 
	 * @param name
	 *            Name des Punktes
	 * @param x
	 *            x-Koordinate
	 * @param y
	 *            y-Koordinate
	 */
	public Punkt(String name, double x, double y) {
		this.name = name;
		this.x = x;
		this.y = y;
	}

	/**
	 * &Uuml;berladener Konstruktor, welcher den Namen auf P setzt.
	 * 
	 * @param x
	 *            X-Koordinate
	 * @param y
	 *            Y-Koordinate
	 */
	public Punkt(double x, double y) {
		this("P", x, y);
	}

	/**
	 * Gibt den Punkt als {@link String} der Form <code>[P] (x|y)</code>,
	 * zur&uuml;ck.
	 */
	@Override
	public String toString() {
		return "[" + this.name + "] (" + ueberpruefeDouble(x) + "|" + ueberpruefeDouble(y) + ")";
	}

	/**
	 * Vergleicht diesen Punkt mit dem als Parameter angegebenen.
	 * 
	 * @param derPunkt
	 *            zu vergleichender Punkt
	 * @return true, wenn beide Punkte die gleichen x- und y-Werte haben.
	 * 
	 */
	public boolean entspricht(Punkt derPunkt) {
		if (DoubleMath.fuzzyEquals(gibX(), derPunkt.gibX(), 0.00001)) {
			if (DoubleMath.fuzzyEquals(gibY(), derPunkt.gibY(), 0.00001)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Addiert deltaX zu der X-Koordinate.
	 * 
	 * @param deltaX
	 *            als {@link Integer}
	 */
	public void addiereX(double deltaX) {
		this.x += deltaX;
	}

	/**
	 * Subtrahiert deltaX von der X-Koordinate.
	 * 
	 * @param deltaX
	 *            als {@link Integer}
	 */
	public void subtrahiereX(int deltaX) {
		this.x -= deltaX;
	
	}

	/**
	 * Addiert y zu der Y-Koordinate.
	 * 
	 * @param deltaY
	 *            als {@link Integer}
	 */
	public void addiereY(double deltaY) {
		this.y += deltaY;
	}

	/**
	 * Subtrahiert deltaY von der Y-Koordinate.
	 * 
	 * @param deltaY
	 *            als {@link Integer}
	 */
	public void subtrahiereY(int deltaY) {
		this.y -= deltaY;
	}

	/**
	 * Berechnet die Distanz zwischen zwei Punkten.
	 * 
	 * @param derPunkt
	 *            zweiter {@link Punkt}
	 * @return Distanz als {@link Double}
	 */
	public double gibDistanz(Punkt derPunkt) {
		double xAbstand, yAbstand;
	
		if (this.x > derPunkt.gibX()) {
			xAbstand = this.x - derPunkt.gibX();
		} else {
			xAbstand = derPunkt.gibX() - this.x;
		}
	
		if (this.y > derPunkt.gibY()) {
			yAbstand = this.y - derPunkt.gibY();
		} else {
			yAbstand = derPunkt.gibY() - this.y;
		}
	
		return Math.sqrt(Math.pow(xAbstand, 2) + Math.pow(yAbstand, 2));
	}
	/**
	 * Getter f&uuml;r {@link #name}
	 * @return Der name des Punkts als String.
	 */
	public String gibName() {
		return name;
	}
	/**
	 * Setter f&uuml;r den Name des Punkts
	 * @param name neuer Name
	 */
	public void setzeName(String name) {
		this.name = name;
	}

	/**
	 * Getter der X-Koordinate
	 * 
	 * @return {@link #x}
	 */
	public double gibX() {
		return x;
	}

	/**
	 * Setter der X-Koordinate.
	 * 
	 * @param x neue X-Koordinate
	 */
	public void setzeX(double x) {
		this.x = x;
	}

	/**
	 * Getter der Y-Koordinate
	 * 
	 * @return {@link #y}
	 */
	public double gibY() {
		return y;
	}

	/**
	 * Setter der Y-Koordinate.
	 * 
	 * @param y neue Y-Koordinate
	 */
	public void setzeY(double y) {
		this.y = y;
	}

}
