package funktionen;
 
import static funktionen.Hilfsmethoden.runde;
import static funktionen.Vereinfachung.HochpunktName;
import static funktionen.Vereinfachung.NullpunktName;
import static funktionen.Vereinfachung.SattelpunktName;
import static funktionen.Vereinfachung.TiefpunktName;
import static funktionen.Vereinfachung.WendepunktName;
import static funktionen.Vereinfachung.vereinfache;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.RoundingMode;
import java.util.ArrayList;

import com.google.common.math.DoubleMath;

/**
 * 
 * Klasse die eine Funktion <code>f(x)</code> mithilfe von
 * {@link Funktionsteil}en darstellt.<br>
 * <br>
 * 
 * <b>Beispiel:</b><br>
 * <ul>
 * <li>Funktion.funktionsTeile[{@link Sin}(4), {@link PotenzFunktion}(3,4),
 * {@link ExponentialFunktion}(2,5)]
 * </ul><br>
 * <b>als String:</b><br>
 * <ul>
 * <li><code>"4sin(x)+ 3x<sup>4</sup>+ 2*5<sup>x</sup></code><br>
 * </ul>
 * <br>
 * 
 * Methoden der Klasse:
 * <ul>
 * <li>Ableitung bilden</li>
 * <li>Stammfunktion bilden</li>
 * <li>Extrempunkte, Wendepunkte &amp; co. bestimmen</li>
 * <li>Funktionswert bestimmen</li>
 * <li>Nullstellen bestimmen (siehe Newton-Verfahren)</li>
 * <li>Rotationsvolumen berechnen</li>
 * <li>Flaeche berechnen</li>
 * <li>Mittelwert berechnen</li>
 * <li>Bogenlaenge berechnen</li>
 * </ul>
 * <br>
 * Alle rechenintensiven Methoden haben interrupt Abfragen implementiert, die bei Thread.interrupt()<br>
 * die Berechnungen stoppen und das interrupt Flag wieder setzen.
 * 
 * 
 * @version 2.0 {@docRoot}
 * 
 * @author Jonathan Brose, Nick Dressler 
 *
 */

public class Funktion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7642955298549937330L;
	/**
	 * Der Name der Variable nach der die Funktion rechnet.
	 * Z.B.: x -&gt; <code>f(x)= 2*x -1</code> 
	 */
	private String variablenName = "x";
	/**
	 * Der Name der Funktion. <code>funktionsName(x) = ... </code> <br>
	 * Beispiel: funktionsName = "f" -&gt; <code> f(x)= ... </code>
	 */
	private String funktionsName = "f";
	/**
	 * Speichert die {@link Funktionsteil}e der Funktion.
	 */
	private ArrayList<Funktionsteil> dieFunktionsTeile;
	/**
	 * Die Gr&ouml;&szlig;e der Schritte in denen {@link #berechneExtrempunkte(Intervall)}, 
	 * {@link #berechneHochpunkte(Intervall)}, {@link #berechneSattelpunkte(Intervall)}, {@link #berechneTiefpunkte(Intervall)}
	 * und {@link #berechneMitNewtonVerfahren(Intervall, double, Funktion)} nach VorzeichenWechseln schauen.
	 * Wenn dieser Wert zu gro&szlig; gew&auml;hlt wir kann es passieren, dass Nullstell usw. nicht regristiert werden. <br>
	 * Der voreingestellte Wert ist im Normalfall v&ouml;llig ausreichend.
	 */
	private double vorzeichenWechselAbstand = 0.01;
	/**
	 * Die allgemeine Genauigkeit f&uuml;r alle Berechnungen die sich dem Ergebnis an&auml;hern und nicht exakt berechnen. <br>
	 * Je kleiner epsilon ist, desto genauer ist das Ergebnis, allerdings dauert die Berechnung auch mit jeder Nachkommastelle bedeutend l&auml;nger.
	 *
	 *@see #berechneMitNewtonVerfahren(Intervall, double, Funktion)
	 *@see #berechneBogenlaenge(Intervall)
	 *@see #berechneAbstand(double, double)
	 *@see #berechneObersumme(Intervall)
	 */
	private double epsilon = 0.000001;

	/**
	 * Standardkonstruktor, erzeugt eine Funktion f(x) = 0
	 */
	public Funktion() {
		this("f", "x", new PotenzFunktion(0, 0));

	}

	/**
	 * &Uuml;berladener Konstruktor
	 * 
	 * @param funktionsName
	 *            Funktionsname z.B. f
	 * @param variablenName
	 *            Funktionsvariable z.B. x
	 * @param FunktionsTeile
	 *            {@link Funktionsteil}e als ArrayList
	 */
	public Funktion(String funktionsName, String variablenName, ArrayList<Funktionsteil> FunktionsTeile) {
		this.variablenName = variablenName;
		this.funktionsName = funktionsName;
		this.dieFunktionsTeile = FunktionsTeile;
		vereinfache(this.dieFunktionsTeile);
		vereinfache(this.dieFunktionsTeile);
	}

	/**
	 *  &Uuml;berladener Konstruktor
	 * 
	 * @param funktionsName
	 *            Funktionsname z.B. f
	 * @param variablenName
	 *            Funktionsvariable z.B. x
	 * @param dieFunktionsteile
	 *            {@link Funktionsteil}e als Array
	 */
	public Funktion(String funktionsName, String variablenName, Funktionsteil... dieFunktionsteile) {
		this.variablenName = variablenName;
		this.funktionsName = funktionsName;
		this.dieFunktionsTeile = new ArrayList<Funktionsteil>();

		for (int i = 0; i < dieFunktionsteile.length; i++) {
			dieFunktionsTeile.add(dieFunktionsteile[i]);
		}
		vereinfache(this.dieFunktionsTeile);
		vereinfache(this.dieFunktionsTeile);
	}

	/**
	 *  &Uuml;berladener Konstruktor
	 * 
	 * @param FunktionsTeile
	 *            als ArrayList.
	 */
	public Funktion(ArrayList<Funktionsteil> FunktionsTeile) {

		this.dieFunktionsTeile = FunktionsTeile;
		vereinfache(this.dieFunktionsTeile);
		vereinfache(this.dieFunktionsTeile);
	}

	/**
	 *  &Uuml;berladener Konstruktor
	 * 
	 * @param dieFunktionsteile
	 *            {@link Funktionsteil}e als Array
	 */
	public Funktion(Funktionsteil... dieFunktionsteile) {

		this.dieFunktionsTeile = new ArrayList<Funktionsteil>();

		for (int i = 0; i < dieFunktionsteile.length; i++) {
			dieFunktionsTeile.add(dieFunktionsteile[i]);
		}

		vereinfache(this.dieFunktionsTeile);
		vereinfache(this.dieFunktionsTeile);

	}

	/**
	 * Gibt den Funktionsterm als {@link String} zurueck.
	 */
	@Override
	public String toString() {
		String s = "";
		s += funktionsName + "(" + variablenName + ") =";
		for (Funktionsteil fT : dieFunktionsTeile) {
			s += " " + fT.gibString(dieFunktionsTeile.indexOf(fT) * -1, variablenName);
		}
		return s;
	}

	/**
	 * Berechnet die Ableitung der Funktion.
	 * 
	 * @return Liefert die erste Ableitung der Funktion als {@link Funktion}.
	 */
	public Funktion gibAbleitung() {
		ArrayList<Funktionsteil> dieAbleitungsListe = new ArrayList<Funktionsteil>();
		try {
			for (Funktionsteil dasAbleitungsFunktionsteil : dieFunktionsTeile) {

				dieAbleitungsListe.add(dasAbleitungsFunktionsteil.gibAbleitung());
			}
		} catch (Exception e) {
			return null;
		}
		return new Funktion(funktionsName + "\'", variablenName, dieAbleitungsListe);

	}

	/**
	 * Berechnet die n-te Ableitung der Funktion.
	 * 
	 * @param n grad der gewollten Ableitung.
	 * @return Liefert die n-te Ableitung der Funktion als {@link Funktion}.
	 */
	public Funktion gibAbleitung(int n) {

		Funktion dieAbleitung = this;
		for (int i = 0; i < n; i++) {
			if (dieAbleitung == null)
				return null;
			dieAbleitung = dieAbleitung.gibAbleitung();
		}
		return dieAbleitung;

	}

	/**
	 * Berechnet, fals m&ouml;glich die Stammfunktion der Funktion.
	 * 
	 * @return Liefert eine Stammfunktion der Funktion.<br>
	 *         dabei ist der Y-Achsenabschnitt C immer =0 !
	 *         <br>gibt null zur&uuml;ck wenn die Stammfunktion nicht berechnet werden kann.
	 * 
	 */
	public Funktion gibStammfunktion() {
		ArrayList<Funktionsteil> dieStammFunktionsteile = new ArrayList<Funktionsteil>();
		for (Funktionsteil dasFunktionsteil : dieFunktionsTeile) {
			if (dasFunktionsteil == null)
				return null;
			else if (dasFunktionsteil.gibStammfunktion() == null)
				return null;
			dieStammFunktionsteile.add(dasFunktionsteil.gibStammfunktion());
		}
	
		String neuerFunktionsName = this.funktionsName;
		if (neuerFunktionsName.endsWith("\'")) {
			//Falls ein oder mehrere Striche (Ableitungen) vorhanden, wird einer geloescht.
			char[] c = neuerFunktionsName.toCharArray();
			char[] c2 = new char[neuerFunktionsName.length() - 1];
			for (int i = 0; i < c2.length; i++) {
				c2[i] = c[i];
			}
			neuerFunktionsName = new String(c2);
	
		} else {
			neuerFunktionsName = neuerFunktionsName.toUpperCase();
	
		}
		return new Funktion(neuerFunktionsName, variablenName, dieStammFunktionsteile);
	
	}

	/**
	 * 
	 * Berechnet die L&auml;nge des Bogens der Funktion
	 * <br><br>kann durch Thread.interrupt() gestopt werden.
	 * @param derIntervall
	 *            {@link Intervall} in dem der Bogen berechnet werden soll.
	 * @return die Laenge des Bogens der Funktion in dem uebergebenen Intervall.
	 * 
	 */

	public double berechneBogenlaenge(Intervall derIntervall) {
		Polygon dasPolygon;
		double epsilon = this.epsilon*100;
		double start, ende, laenge, alteLaenge, schrittweite;
		int anzahl = 10;

		start = derIntervall.gibLinkenWert();
		ende = derIntervall.gibRechtenWert();

		laenge = 0;
		alteLaenge = -1;

		do {
			schrittweite = start;
			dasPolygon = new Polygon(anzahl, epsilon);
			double distanz = (double) Math.abs(ende - start) / anzahl;

			for (int i = 0; i < anzahl; i++) {
				if(Thread.interrupted()) {
					Thread.currentThread().interrupt();
					return 0;
				}
				dasPolygon.setPunkt(new Punkt(schrittweite, this.berechneFunktionswert(schrittweite)));
				schrittweite += distanz;
			}
			alteLaenge = laenge;
			laenge = dasPolygon.getLaenge();
			anzahl++;
		} while (Math.abs(alteLaenge - laenge) > epsilon);
		return laenge;
	}

	/**
	 * 
	 * Berechnet das Integral der Kurve
	 * <br><br>kann durch Thread.interrupt() gestopt werden.
	 * @param derIntervall
	 *            Intervall, in welchem Integral berechnet werden soll.
	 * @return Integralwert als double
	 */
	public double berechneIntegral(Intervall derIntervall) {

		Funktion dieStammfunktion = this.gibStammfunktion();
		if (dieStammfunktion == null)
			return berechneObersumme(derIntervall);

		return berechneIntegral(dieStammfunktion, derIntervall);
	}

	/**
	 * Berechnet den Mittelwert der Kurve
	 * <br><br>kann durch Thread.interrupt() gestopt werden.
	 * @param derIntervall
	 *            Intervall, in welchem Mittelwert berechnet werden soll.
	 * @return Mittelwert siehe Integralmittelwert
	 */
	public double berechneMittelwert(Intervall derIntervall) {

		return (1 / (derIntervall.gibRechtenWert() - derIntervall.gibLinkenWert())) * berechneIntegral(derIntervall);
	}

	/**
	 * 
	 * Berechnet die Fl&auml;che zwischen dem Graph und der X-Achse
	 * <br>kann durch Thread.interrupt() gestopt werden.
	 * @param derIntervall
	 *            Intervall, in welchem Fl&auml;cheninhalt zwischen Kurve und x-Achse
	 *            berechnet werden soll.
	 * 
	 * @return <p>Flaecheninhalt (immer &gt; 0) da Betrag.</p>
	 * 
	 */
	public double berechneFlaeche(Intervall derIntervall) {

		return berechneFlaeche(derIntervall, this);
	}

	/**
	 * 
	 * Berechnet die Fl&auml;che zwischen dem Graph und der X-Achse
	 * <br><br>kann durch Thread.interrupt() gestopt werden.
	 * @param derIntervall
	 *            Intervall, in welchem Fl&auml;cheninhalt zwischen Kurve und x-Achse
	 *            berechnet werden soll.
	 * @param dieFunktion
	 *            Funktion deren Fl&auml;che berechnet werden soll.
	 * 
	 * 
	 * @return <p>Fl&auml;cheninhalt (immer &gt; 0) da Betrag.</p>
	 * 
	 * 
	 */
	public double berechneFlaeche(Intervall derIntervall, Funktion dieFunktion) {

		// Summe
		double flaeche = 0.0D;
		ArrayList<Double> dieNullstellen = this.berechneNullstellenD(derIntervall);
		Funktion dieStammfunktion = dieFunktion.gibStammfunktion();

		if (dieStammfunktion == null) {

			if (dieNullstellen.size() == 0) {
				flaeche += Math.abs(berechneObersumme(derIntervall));

			} else if (dieNullstellen.size() > 0) {

				Intervall[] dieIntervalle = new Intervall[dieNullstellen.size() + 1];

				for (int j = 0; j < dieIntervalle.length; j++) {
					if(Thread.interrupted()) {
						Thread.currentThread().interrupt();
						return 0;
					}
					if (j == 0)
						dieIntervalle[j] = new Intervall(derIntervall.gibLinkenWert(), dieNullstellen.get(j));

					else if (j == dieIntervalle.length - 1)
						dieIntervalle[j] = new Intervall(dieNullstellen.get(j - 1), derIntervall.gibRechtenWert());

					else
						dieIntervalle[j] = new Intervall(dieNullstellen.get(j - 1), dieNullstellen.get(j));
				}

				for (Intervall intervall : dieIntervalle) {
					if(Thread.interrupted()) {
						Thread.currentThread().interrupt();
						return 0;
					}
					flaeche += Math.abs(berechneObersumme(intervall));

				}
			}
		} else {

			if (dieNullstellen.size() == 0) {
				flaeche += Math.abs(berechneIntegral(dieStammfunktion, derIntervall));

			} else if (dieNullstellen.size() > 0) {

				Intervall[] dieIntervalle = new Intervall[dieNullstellen.size() + 1];

				for (int j = 0; j < dieIntervalle.length; j++) {
					if(Thread.interrupted()) {
						Thread.currentThread().interrupt();
						return 0;
					}
					if (j == 0)
						dieIntervalle[j] = new Intervall(derIntervall.gibLinkenWert(), dieNullstellen.get(j));

					else if (j == dieIntervalle.length - 1)
						dieIntervalle[j] = new Intervall(dieNullstellen.get(j - 1), derIntervall.gibRechtenWert());

					else
						dieIntervalle[j] = new Intervall(dieNullstellen.get(j - 1), dieNullstellen.get(j));
				}

				for (Intervall intervall : dieIntervalle) {
					if(Thread.interrupted()) {
						Thread.currentThread().interrupt();
						return 0;
					}
					flaeche += Math.abs(berechneIntegral(dieStammfunktion, intervall));

				}
			}
		}
		return flaeche;
	}

	/**
	 * 
	 * Berechnet das Rotationsvolumen der Funktion
	 * <br>kann durch Thread.interrupt() gestopt werden.
	 * 
	 * @param derIntervall in
	 *            den Berechnet werden soll
	 * @return Rotationsvolumen als double
	 * 
	 * 
	 */

	public double berechneRotationsVolumen(Intervall derIntervall) {
		Funktionsteil[] dieFunktionsteile = new Funktionsteil[this.dieFunktionsTeile.size()];
		for (int i = 0; i < this.dieFunktionsTeile.size(); i++) {
			if(Thread.interrupted()) {
				Thread.currentThread().interrupt();
				return 0;
			}
			dieFunktionsteile[i] = this.dieFunktionsTeile.get(i).clone();
		}
		AddVerknuepfung dieAddVerknuepfung = new AddVerknuepfung(dieFunktionsteile);
		return Math.PI * berechneFlaeche(derIntervall, new Funktion(new Verkettung(new PotenzFunktion(1, 2), dieAddVerknuepfung)));

	}

	/**
	 * 
	 * Berechnet die Hoch- und Tiefpunkte der Funktion.
	 *
	 *   Ein Extrempunkt liegt dann vor, wenn gilt:
	 *     <p> <code>f'(x) == 0 &amp;&amp; VZW bei x.</code></p>
	 *     <br><br>kann durch Thread.interrupt() gestopt werden.  
	 * @param derIntervall
	 *            {@link Intervall} in dem nach Extrempunkten gesucht wird.
	 * @return gibt eine {@link ArrayList} mit den Extrempunkten zurueck.
	 * @see Punkt
	 * @see ArrayList
	 *      
	 *
	 **/
	public ArrayList<Punkt> berechneExtrempunkte(Intervall derIntervall) {
		Funktion dieAbleitung = gibAbleitung();
		ArrayList<Double> diePotenziellenXWerte = berechneMitNewtonVerfahren(derIntervall, epsilon, dieAbleitung);
		ArrayList<Punkt> dieExtremPunkte = new ArrayList<Punkt>();
		for (Double x : diePotenziellenXWerte) {
			if(Thread.interrupted()) {
				Thread.currentThread().interrupt();
				return new ArrayList<Punkt>();
			}
			
			if (dieAbleitung.berechneFunktionswert(x - vorzeichenWechselAbstand) >= 0
					&& dieAbleitung.berechneFunktionswert(x + vorzeichenWechselAbstand) < 0) {
	
				dieExtremPunkte.add(new Punkt(HochpunktName, x, berechneFunktionswert(x)));
	
			} else if (dieAbleitung.berechneFunktionswert(x - vorzeichenWechselAbstand) < 0
					&& dieAbleitung.berechneFunktionswert(x + vorzeichenWechselAbstand) >= 0) {
	
				dieExtremPunkte.add(new Punkt(TiefpunktName, x, berechneFunktionswert(x)));
	
			}
		}
	
		return ueberpruefePeriode(dieExtremPunkte);
	
	}

	/**
	 * 
	 * Berechnet die Sattelpunkte der Funktion.
	 *  Ein Sattelpunkt liegt dann vor, wenn die notw. Kriterien f&uuml;r einen
	 *      Extrempunkt und Wendepunkt an selber Stelle x0 erf&uuml;llt sind.
	 * <br><br>kann durch Thread.interrupt() gestopt werden.
	 * 
	 * @param derIntervall,
	 *            {@link Intervall} in dem nach Sattelpunkten gesucht wird.
	 * @return gibt {@link ArrayList} mit den Sattelpunkten zur&uuml;ck.
	 * @see Punkt
	 * @see ArrayList
	 *      
	 *
	 **/
	public ArrayList<Punkt> berechneSattelpunkte(Intervall derIntervall) {
		Funktion dieAbleitung1 = gibAbleitung();
		Funktion dieAbleitung2 = dieAbleitung1.gibAbleitung();
		Funktion dieAbleitung3 = dieAbleitung2.gibAbleitung();
	
		ArrayList<Double> diePotenziellenXWerte = berechneMitNewtonVerfahren(derIntervall, epsilon, dieAbleitung2);
		ArrayList<Punkt> dieSattelPunkte = new ArrayList<Punkt>();
	
		for (Double x : diePotenziellenXWerte) {
			if(Thread.interrupted()) {
				Thread.currentThread().interrupt();
				return new ArrayList<Punkt>();
			}
			// f''(x) = 0
			if ((dieAbleitung2.berechneFunktionswert(x - vorzeichenWechselAbstand) >= 0
					&& dieAbleitung2.berechneFunktionswert(x + vorzeichenWechselAbstand) < 0)
					|| (dieAbleitung2.berechneFunktionswert(x - vorzeichenWechselAbstand) < 0
							&& dieAbleitung2.berechneFunktionswert(x + vorzeichenWechselAbstand) >= 0)) {
	
				// f'''(x) != 0
				if (dieAbleitung3.berechneFunktionswert(x) != 0.0D) {
					if (dieAbleitung1.berechneFunktionswert(x) == 0) {
						dieSattelPunkte.add(new Punkt(SattelpunktName, x, berechneFunktionswert(x)));
	
					}
				}
	
			}
		}
	
		return ueberpruefePeriode(dieSattelPunkte);
	
	}

	/**
	 * Berechnet die Tiefpunkte der Funktion.<p>
	 *      Ein Tiefpunkt liegt dann vor, wenn gilt:
	 *      <code>f'(x) == 0 &amp;&amp; VZW bei x von - -&gt; +</code>
	 *      </p>
	 * <br><br>kann durch Thread.interrupt() gestopt werden.
	 * 
	 * @param derIntervall
	 *            {@link Intervall} in dem nach Tiefpunkten gesucht wird.
	 * @return gibt {@link ArrayList} mit den Tiefpunkten zur&uuml;ck.
	 * @see Punkt
	 * @see ArrayList
	 *      
	 *
	 **/
	public ArrayList<Punkt> berechneTiefpunkte(Intervall derIntervall) {
		Funktion dieAbleitung = gibAbleitung();
		ArrayList<Double> diePotenziellenXWerte = berechneMitNewtonVerfahren(derIntervall, epsilon, dieAbleitung);
		ArrayList<Punkt> dieExtrempunkte = new ArrayList<Punkt>();
		for (Double x : diePotenziellenXWerte) {
			if(Thread.interrupted()) {
				Thread.currentThread().interrupt();
				return new ArrayList<Punkt>();
			}
			if (dieAbleitung.berechneFunktionswert(x - vorzeichenWechselAbstand) < 0
					&& dieAbleitung.berechneFunktionswert(x + vorzeichenWechselAbstand) >= 0) {
	
				dieExtrempunkte.add(new Punkt("T", x, berechneFunktionswert(x)));
	
			}
		}
		return ueberpruefePeriode(dieExtrempunkte);
	
	}

	/**
	 * 
	 * Berechnet die Hochpunkte der Funktion.<p>
	 *      Ein Hochpunkt liegt dann vor, wenn gilt:
	 *      <code>f'(x) == 0 &amp;&amp; VZW bei x von + -&gt; -</code>
	 *      </p>
	 * <br><br>kann durch Thread.interrupt() gestopt werden.
	 * 
	 * @param derIntervall
	 *            {@link Intervall} in dem nach Hochpunkten gesucht wird.
	 * @return gibt {@link ArrayList} mit den Hochpunkten zur&uuml;ck.
	 * @see Punkt
	 * @see ArrayList
	 *      
	 *
	 **/
	public ArrayList<Punkt> berechneHochpunkte(Intervall derIntervall) {
		Funktion dieAbleitung = gibAbleitung();
		ArrayList<Double> diePotenziellenXWerte = berechneMitNewtonVerfahren(derIntervall, epsilon, dieAbleitung);
		ArrayList<Punkt> dieExtremPunkte = new ArrayList<Punkt>();
		for (Double x : diePotenziellenXWerte) {
			if(Thread.interrupted()) {
				Thread.currentThread().interrupt();
				return new ArrayList<Punkt>();
			}
			if (dieAbleitung.berechneFunktionswert(x - vorzeichenWechselAbstand) >= 0
					&& dieAbleitung.berechneFunktionswert(x + vorzeichenWechselAbstand) < 0) {
	
				dieExtremPunkte.add(new Punkt("H", x, berechneFunktionswert(x)));
			}
		}
		return ueberpruefePeriode(dieExtremPunkte);
	
	}

	/**
	 * 
	 * Berechnet die Wendepunkte der Funktion. <p>
	 *      Ein Wendepunkt liegt dann vor, wenn gilt:
	 *      <code>f''(x) == 0 &amp;&amp; VZW bei x.</code>
	 *      </p>
	 * <br><br>kann durch Thread.interrupt() gestopt werden.
	 * 
	 * @param derIntervall
	 *            {@link Intervall} in dem nach Wendepunkten gesucht wird.
	 * @return gibt {@link ArrayList} mit den Wendepunkten zur&uuml;ck.
	 * @see Punkt
	 * @see ArrayList
	 *     
	 *
	 **/
	public ArrayList<Punkt> berechneWendepunkte(Intervall derIntervall) {
		Funktion dieAbleitung = gibAbleitung().gibAbleitung();
		ArrayList<Double> diePotenziellenXWerte = berechneMitNewtonVerfahren(derIntervall, epsilon, dieAbleitung);
		ArrayList<Punkt> dieExtremPunkte = new ArrayList<Punkt>();
		for (Double x : diePotenziellenXWerte) {
			if(Thread.interrupted()) {
				Thread.currentThread().interrupt();
				return new ArrayList<Punkt>();
			}
			if (dieAbleitung.berechneFunktionswert(x - vorzeichenWechselAbstand) >= 0
					&& dieAbleitung.berechneFunktionswert(x + vorzeichenWechselAbstand) < 0) {
	
				dieExtremPunkte.add(new Punkt(WendepunktName + ": L->R", x, berechneFunktionswert(x)));
			} else if (dieAbleitung.berechneFunktionswert(x - vorzeichenWechselAbstand) < 0
					&& dieAbleitung.berechneFunktionswert(x + vorzeichenWechselAbstand) >= 0) {
				dieExtremPunkte.add(new Punkt(WendepunktName + ": R->L", x, berechneFunktionswert(x)));
			}
		}
		return ueberpruefePeriode(dieExtremPunkte);
	}

	/**
	 * Berechnet die Schnittpunkte von der Funktion mit der X-Achse.
	 * <br><br>kann durch Thread.interrupt() gestopt werden.
	 * 
	 * @param derIntervall
	 *            {@link Intervall} in dem berechnet werden soll.
	 * @return Nullstellen der Funktion als {@link ArrayList} mit {@link Punkt}en.
	 * @see #berechneMitNewtonVerfahren(Intervall, double, Funktion)
	 */
	public ArrayList<Punkt> berechneNullstellen(Intervall derIntervall) {
		ArrayList<Punkt> dieNullstellen = new ArrayList<Punkt>();
		for (Double x : berechneMitNewtonVerfahren(derIntervall, epsilon, this)) {
			if(Thread.interrupted()) {
				Thread.currentThread().interrupt();
				return new ArrayList<Punkt>();
			}
			dieNullstellen.add(new Punkt(NullpunktName, x, 0));
		}
		return ueberpruefePeriode(dieNullstellen);
	
	}
	/**
	 * Berechnet die Nullstellen der Funktion.
	 * <br><br>kann durch Thread.interrupt() gestopt werden.
	 * 
	 * 
	 * @param derIntervall
	 *            {@link Intervall} in dem berechnet werden soll.
	 * @return Nullstellen der Funktion als {@link ArrayList} vom typ double.
	 * @see #berechneMitNewtonVerfahren(Intervall, double, Funktion)
	 */
	public ArrayList<Double> berechneNullstellenD(Intervall derIntervall) {
	
		return berechneMitNewtonVerfahren(derIntervall, epsilon, this);
	
	}

	/**
	 * Berechnet den Funktionswert an der Stelle x0, <br>
	 * also: f(x0)= return
	 * <br><br>kann durch Thread.interrupt() gestopt werden.
	 * 
	 * @param x0
	 *            ist die Stelle (x-Koordinate)
	 * @return Gibt den Funktionswert (y-Koordinate) der Funktion an der Stelle x0
	 *         zurueck.
	 */
	public double berechneFunktionswert(double x0) {
		boolean naN=false;
		double funktionswert = 0;
		Double testFunktionswert =0.0;
		for (Funktionsteil dasFunktionsteil : dieFunktionsTeile) {
			if(Thread.interrupted()) {
				Thread.currentThread().interrupt();
				return 0;
			}
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

	/**
	 * 
	 * Berechnet die Obersumme im gegeben Intervall bis zu der durch {@link epsilon}
	 * bestimmten Genauigkeit
	 * <br><br>kann durch Thread.interrupt() gestopt werden.
	 * 
	 * @param derIntervall
	 *            fuer den Berechnet werden soll
	 * @return Obersumme als double
	 * @see #berechneStreifenMultiplikator(double)
	 */
	
	private double berechneObersumme(Intervall derIntervall) {
	
		double summe = 0;
		double letzteSumme = 0;
		int anzahlStreifen = 50;
		int c = 0;
		do {
			double deltaX = derIntervall.gibRechtenWert() - derIntervall.gibLinkenWert();
			deltaX /= anzahlStreifen;
			letzteSumme = summe;
			summe = 0;
			for (double i = derIntervall.gibLinkenWert(); i < derIntervall.gibRechtenWert(); i += deltaX) {
				if(Thread.interrupted()) {
					Thread.currentThread().interrupt();
					return 0;
				}
				summe += deltaX * berechneFunktionswert(i);
			}
			anzahlStreifen *= 2;
			if (c > 0)
				anzahlStreifen *= berechneStreifenMultiplikator(berechneAbstand(summe, letzteSumme));
			c++;
		} while (berechneAbstand(summe, letzteSumme) > epsilon);
		return summe;
	}

	/**
	 * Hilfsfunktion, welche Fl&auml;chen-Etappen berechnet.
	 * 
	 * @param dieStammfunktion
	 *            {@link Funktionsteil} Stammfunktion
	 * @param derIntervall
	 *            {@link Intervall} der Etappe
	 * @return Gibt den Betrag der Fl&auml;che zur&uuml;ck.
	 */
	private double berechneIntegral(Funktion dieStammfunktion, Intervall derIntervall) {
		return dieStammfunktion.berechneFunktionswert(derIntervall.gibRechtenWert()) - (dieStammfunktion.berechneFunktionswert(derIntervall.gibLinkenWert()));
	}
	/**
	 * Hilfsfunktion, die abh&auml;ngig vom Abstand eine h&ouml;here Zahl ausgibt<br>
	 *  um sich der Genauigkeit epsilon schneller anzunaehern.
	 *  <br><br>kann durch Thread.interrupt() gestopt werden.
	 * @param abstand die Differenz zwischen dem letzten und dem aktuellen Ergebnis.
	 * @return 
	 * 		Der Multiplikator um bei groﬂem Abstand die Anzahl der Streifen schneller zu erh&ouml;hen.
	 */
	private int berechneStreifenMultiplikator(double abstand) {
		int a = (int) berechneAbstand(abstand, epsilon);
		return a == 0 ? 1 : a;
	}

	/**
	 * 
	 * Berechnet die Nullstellen der Funktion bis zu der Genauigkeit epsilon mit dem Newtonverfahren.
	 * <br><br>kann durch Thread.interrupt() gestopt werden.
	 * 
	 * @param derIntervall
	 *            Ist der Intervall, in dem nach Nullstellen gesucht wird.
	 * @param epsilon
	 *            Epsilon-Wert, bestimmt Genauigkeit.
	 * @param dieFunktion
	 *            Die zu untersuchende {@link Funktion}.
	 * @return Gibt {@link ArrayList} mit Double-Werten (Stellen) zur&uuml;ck.
	 */
	private ArrayList<Double> berechneMitNewtonVerfahren(Intervall derIntervall, double epsilon, Funktion dieFunktion) {
		ArrayList<Double> dieNullstellen = new ArrayList<Double>();
		ArrayList<Double> dieStartwerte = new ArrayList<Double>();
		Funktion dieAbleitung = dieFunktion.gibAbleitung();
	
		for (double x = derIntervall.gibLinkenWert(); x <= derIntervall.gibRechtenWert(); x += vorzeichenWechselAbstand) {
			if(Thread.interrupted()) {
				Thread.currentThread().interrupt();
				return new ArrayList<Double>();
			}
			if (dieFunktion.berechneFunktionswert(x) == 0)
				dieStartwerte.add(x);
			else if (dieFunktion.berechneFunktionswert(x) < 0 && dieFunktion.berechneFunktionswert(x + vorzeichenWechselAbstand) > 0)
				dieStartwerte.add(x + vorzeichenWechselAbstand / 2);
			else if (dieFunktion.berechneFunktionswert(x) > 0 && dieFunktion.berechneFunktionswert(x + vorzeichenWechselAbstand) < 0)
				dieStartwerte.add(x + vorzeichenWechselAbstand / 2);
	
		}
	
		for (Double x0 : dieStartwerte) {
			double letzterX0Wert = 0.0;
			while (berechneAbstand(letzterX0Wert, x0) > epsilon) {
				if(Thread.interrupted()) {
					Thread.currentThread().interrupt();
					return new ArrayList<Double>();
				}
				letzterX0Wert = x0.doubleValue();
				x0 = x0 - (dieFunktion.berechneFunktionswert(x0) / dieAbleitung.berechneFunktionswert(x0));
			}
			if (!x0.isNaN()) {
	
				dieNullstellen.add(x0);
			}
		}
		return dieNullstellen;
	}

	/**
	 * Berechnet den Abstand der beiden Werte, wobei das Ergebnis immer positiv ist.
	 * 
	 * @param links
	 *            erster Wert
	 * @param rechts
	 *            zweiter Wert
	 * @return Gibt Abstand zwischen l und r zur&uuml;ck.
	 */
	private double berechneAbstand(double links, double rechts) {
		double abstand = links - rechts;
		if (abstand < 0)
			abstand *= -1;
		return abstand;
	
	}

	/**
	 * Sortiert die uebergebene {@link ArrayList} mit {@link Punkt}en nach deren X-Werte. <br>
	 * Dazu wird der bubble-sort Algorythmus verwendet.
	 * 
	 * @param diePunkte
	 *            Die zu sortierende {@link ArrayList} mit {@link Punkt}en
	 * @return gibt die {@link ArrayList} der Gr&ouml;ﬂe der X-Werte nach sortiert
	 *          zur&uuml;ck.
	 */
	private ArrayList<Punkt> sortiereNachXWert(ArrayList<Punkt> diePunkte) {
		Punkt c = null;
		for (int i = 1; i < diePunkte.size(); i++) {
			for (int j = 0; j < diePunkte.size() - i; j++) {
				if (diePunkte.get(j).gibX() > diePunkte.get(j + 1).gibX()) {
					c = diePunkte.get(j);
					diePunkte.set(j, diePunkte.get(j + 1));
					diePunkte.set(j + 1, c);
				}
			}
		}
		return diePunkte;
	
	}

	/**
	 * 
	 * &Uuml;berprueft die Liste auf ein moegliche Periodizitaet, und fasst dann die
	 * betroffenen Punkte zusammen. Sortiert dann die Liste mit sortiereNachXWert().
	 * 
	 * @param dieExtremPunkte
	 *            Die Liste mit Extrem-, Wende-, Sattelpunkten, oder Nullstellen
	 * @return Die zusammengefasste und sortierte Liste mit Extrem-, Wende-,
	 *         Sattelpunkten, oder Nullstellen
	 * @see #sortiereNachXWert(ArrayList)
	 */
	private ArrayList<Punkt> ueberpruefePeriode(ArrayList<Punkt> dieExtremPunkte) {
		ArrayList<Punkt> dieHochPunkte = new ArrayList<Punkt>();
		ArrayList<Punkt> dieTiefPunkte = new ArrayList<Punkt>();
		ArrayList<Punkt> dieWendePunkteLR = new ArrayList<Punkt>();
		ArrayList<Punkt> dieWendePunkteRL = new ArrayList<Punkt>();
		ArrayList<Punkt> dieSattelPunkte = new ArrayList<Punkt>();
		ArrayList<Punkt> dieNullstellen = new ArrayList<Punkt>();
		for (Punkt derPunkt : dieExtremPunkte) {
			if (derPunkt.gibName().equals(HochpunktName)) {
				dieHochPunkte.add(derPunkt);
			} else if (derPunkt.gibName().equals(TiefpunktName)) {
				dieTiefPunkte.add(derPunkt);
			} else if (derPunkt.gibName().equals(WendepunktName + ": L->R")) {
				dieWendePunkteLR.add(derPunkt);
			} else if (derPunkt.gibName().equals(WendepunktName + ": R->L")) {
				dieWendePunkteRL.add(derPunkt);
			} else if (derPunkt.gibName().equals(SattelpunktName)) {
				dieSattelPunkte.add(derPunkt);
			} else if (derPunkt.gibName().equals(NullpunktName)) {
				dieNullstellen.add(derPunkt);
			}
		}
		dieExtremPunkte.clear();
		ueberpruefePeriode(dieWendePunkteRL, dieExtremPunkte);
		ueberpruefePeriode(dieWendePunkteLR, dieExtremPunkte);
		ueberpruefePeriode(dieHochPunkte, dieExtremPunkte);
		ueberpruefePeriode(dieTiefPunkte, dieExtremPunkte);
		ueberpruefePeriode(dieSattelPunkte, dieExtremPunkte);
		ueberpruefePeriode(dieNullstellen, dieExtremPunkte);
	
		return sortiereNachXWert(dieExtremPunkte);
	
	}

	/**
	 * &Uuml;berprueft die {@link ArrayList} mit {@link Punkt}en auf Periodizitaet und f&uuml;gt diese dann (wenn
	 * moeglich zusammengefasst), in die hauptListe ein.
	 * 
	 * @param diePunkte
	 *            Liste mit den zu &uuml;berpr&uuml;fenden Punkten
	 * @param dieZielListe
	 *            Liste in der das Ergebniss der Zusammenfassung gespeichert wird
	 */
	private void ueberpruefePeriode(ArrayList<Punkt> diePunkte, ArrayList<Punkt> dieZielListe) {
		double epsilon = 0.0001, y = 0,  letztesY = 0;
		double abstand = 0, letzterAbstand = 0;
		ArrayList<Punkt> dieZuLoeschendenPunkte = new ArrayList<Punkt>();
		try {
			y = diePunkte.get(0).gibY();
		} catch (Exception e) {
		}
		letztesY = y;
		for (int i = 1; i < diePunkte.size(); i++) {
			y = diePunkte.get(i).gibY();
			if (!DoubleMath.fuzzyEquals(y, letztesY, epsilon)) {
				dieZielListe.add(diePunkte.get(i));
				dieZuLoeschendenPunkte.add(diePunkte.get(i));
			}
			letztesY = y;
		}
		for (Punkt p : dieZuLoeschendenPunkte) {
			diePunkte.remove(p);
		}
		dieZuLoeschendenPunkte.clear();
	
		try {
			abstand = berechneAbstand(diePunkte.get(0).gibX(), diePunkte.get(1).gibX());
			letzterAbstand = abstand;
			abstand = berechneAbstand(diePunkte.get(1).gibX(), diePunkte.get(2).gibX());
			if (DoubleMath.fuzzyEquals(abstand, letzterAbstand, epsilon)) {
				String periode = "";
				double piFaktor = abstand / Math.PI;
				double piFaktorGerundet = runde(piFaktor, DoubleMath.roundToInt(Math.log10(1 / epsilon), RoundingMode.HALF_UP));
	
				if (DoubleMath.fuzzyEquals(piFaktor, piFaktorGerundet, epsilon)) {
					periode = (piFaktorGerundet == 1 ? "" : "" + piFaktorGerundet) + "PI";
	
				} else {
					periode = piFaktor + "PI";
				}
				diePunkte.get(0).setzeName(diePunkte.get(0).gibName() + " {+- k* " + periode + "}");
				dieZuLoeschendenPunkte.add(diePunkte.get(1));
			}
		} catch (Exception e) {
		}
		for (int i = 2; i < diePunkte.size(); i++) {
			try {
				abstand = berechneAbstand(diePunkte.get(i).gibX(), diePunkte.get(i + 1).gibX());
			} catch (Exception e) {
	
			}
			if (DoubleMath.fuzzyEquals(abstand, letzterAbstand, epsilon)) {
				dieZuLoeschendenPunkte.add(diePunkte.get(i));
			}
			letzterAbstand = abstand;
		}
		for (Punkt p : dieZuLoeschendenPunkte) {
			diePunkte.remove(p);
		}
		dieZielListe.addAll(diePunkte);
	}

	

	/**
	 * Speichert die Funktion in einer Datei unter dem gegebenen Dateipfad ab
	 * 
	 * @param derDateipfad
	 *            String mit dem Pfad der zu schreibenden Datei ohne Dateiendung.
	 * 
	 */
	public void speichere(String derDateipfad) {
		try {
			FileOutputStream derFileOutputStream = new FileOutputStream(derDateipfad);
			ObjectOutputStream derObjectOutputStream = new ObjectOutputStream(derFileOutputStream);
			derObjectOutputStream.writeObject(this);
			derObjectOutputStream.close();
			derFileOutputStream.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	/**
	 * 
	 * Speichert die gegebene Funktion in einer Datei unter dem gegebenen Dateipfad
	 * ab
	 * 
	 * @param derDateipfad
	 *            String mit dem Pfad der zu schreibenden Datei ohne Dateiendung.
	 * @param dieFunktion
	 *            Funktion die gespeichert werden soll.
	 */
	public static void speichere(String derDateipfad, Funktion dieFunktion) {
		try {
			FileOutputStream derFileOutputStream = new FileOutputStream(derDateipfad);
			ObjectOutputStream derObjectOutputStream = new ObjectOutputStream(derFileOutputStream);
			derObjectOutputStream.writeObject(dieFunktion);
			derObjectOutputStream.close();
			derFileOutputStream.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	/**
	 * 
	 * L&auml;dt aus einer durch den Dateipfad gegebenen Datei eine Instanz von Funktion
	 * 
	 * @param derDateipfad
	 *            String mit dem Dateipfad der Datei die geladen werden soll.
	 * @return {@link Funktion} aus der gespeicherten datei.
	 */
	public static Funktion lade(String derDateipfad) {
		Funktion dieFunktion = null;
		try {
			FileInputStream derFileInputStream = new FileInputStream(derDateipfad);
			ObjectInputStream derObjectInputStream = new ObjectInputStream(derFileInputStream);
			dieFunktion = (Funktion) derObjectInputStream.readObject();
			derObjectInputStream.close();
			derFileInputStream.close();
		} catch (IOException i) {
			i.printStackTrace();
			return null;
		} catch (ClassNotFoundException c) {
			System.out.println("Funktion class not found");
			c.printStackTrace();
			return null;
		}
		return dieFunktion;

	}

	/**
	 * Getter
	 * @return {@link #variablenName}
	 */
	public String gibVariablenName() {
		return variablenName;
	}

	/**
	 * Setter 
	 * @param variablenName {@link #variablenName}
	 */
	public void setzeVariablenName(String variablenName) {
		this.variablenName = variablenName;
	}

	/**
	 * Getter
	 * @return {@link #funktionsName}
	 */
	public String gibFunktionsName() {
		return funktionsName;
	}

	/**
	 * Setter
	 * @param funktionsName {@link #funktionsName}
	 * 
	 */

	public void setzeFunktionsName(String funktionsName) {
		this.funktionsName = funktionsName;
	}

	/**
	 * Getter
	 * @return {@link #vorzeichenWechselAbstand}
	 */
	public double gibVorzeichenWechselAbstand() {
		return vorzeichenWechselAbstand;
	}

	/**
	 * Setter
	 * @param vorzeichenWechselAbstand {@link #vorzeichenWechselAbstand}
	 */
	public void setzeVorzeichenWechselAbstand(double vorzeichenWechselAbstand) {
		this.vorzeichenWechselAbstand = vorzeichenWechselAbstand;
	}

	/**
	 * Getter
	 * @return {@link #epsilon}
	 */
	public double gibEpsilon() {
		return epsilon;

	}

	/**
	 * Setter
	 * @param epsilon {@link #epsilon}
	 */
	public void setzeEpsilon(double epsilon) {
		this.epsilon = epsilon;
	}

	/**
	 * Getter
	 * @return {@link ArrayList} mit den {@link Funktionsteil}en der
	 *         {@link Funktion}
	 */
	public ArrayList<Funktionsteil> gibFunktionsTeile() {
		return dieFunktionsTeile;
	}

	/**
	 * &Uuml;berprueft ob die aktuelle Funktion <code>f(x)=0 </code> ist.
	 * 
	 * @return true, falls Bedingung oben erf&uuml;llt, sonst false.
	 */
	public boolean entsprichtXAchse() {
		if (this.dieFunktionsTeile.size() == 0)
			return true;
		return false;
	}

}
