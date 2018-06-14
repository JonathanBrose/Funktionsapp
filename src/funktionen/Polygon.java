package funktionen;
 
import java.util.ArrayList;

import funktionen.Punkt;

/**
 * Klasse, die ein Polygon repr&auml;sentiert, welches aus {@link Punkt}en besteht.
 * 
 * @author Nick Dressler
 *
 */
public class Polygon {
	/**
	 * Die anzahl der Punkte die gespeichert werden sollen und k&ouml;nnen.
	 * Bestimmt die Gr&ouml;&szlig;e von {@link #diePunkte}
	 */
	private int anzahlPunkte;
	/**
	 * Gibt an wie viele {@link Punkt}e gespeichert wurden, bzw. wie viele Slots von {@link #diePunkte} voll sind.
	 */
	private int anzahlGespeichertePunkte;
	/**
	 * Hier werden die Punkte gespeichert.
	 */
	private Punkt[] diePunkte;
	/**
	 * Gibt an ob das Array {@link #diePunkte} nach dem X-Wert der {@link Punkt}e sortiert ist, oder ob dies noch getan werden muss.
	 */
	private boolean sortiert;

	/**
	 * Konstruktor
	 * 
	 * @param anzahl
	 *            Anzahl der Punkte des Polygons.
	 * @param epsilon die Genaugikeit. Je kleiner die Zahle desto genauer.
	 */
	public Polygon(int anzahl, double epsilon) {
		this.diePunkte = new Punkt[anzahl];
		this.anzahlPunkte = anzahl;
		this.anzahlGespeichertePunkte = 0;
	}

	/**
	 * Berechnet die Bogenl&auml;nge des Polygons
	 * 
	 * @return L&auml;nge als {@link Double}
	 */
	public double getLaenge() {
		if(!sortiert) sortiereNachXWert();
		double laenge = 0;
		for (int i = 0; i < this.anzahlPunkte - 1; i++) {
			laenge = laenge + this.diePunkte[i].gibDistanz(this.diePunkte[i + 1]);
		}
		return laenge;
	}

	/**
	 * Fügt dem Polygon ein {@link Punkt} hinzu.
	 * 
	 * @param derPunkt der neue Punkt
	 */

	public void setPunkt(Punkt derPunkt) {
		if(anzahlGespeichertePunkte < anzahlPunkte) {
			this.diePunkte[this.anzahlGespeichertePunkte] = derPunkt;
			this.anzahlGespeichertePunkte++;
		}
		sortiert=false;
	}
	
	/**
	 * 
	 * 
	 * sortiert die {@link ArrayList} diePunkte der Größe der X-Werte nach sortiert
	 *          zurück.
	 */
	public void sortiereNachXWert() {
		Punkt c = null;
		for (int i = 1; i < diePunkte.length; i++) {
			for (int j = 0; j < diePunkte.length - i; j++) {
				if (diePunkte[j].gibX() > diePunkte[j+1].gibX()) {
					c = diePunkte[j];
					diePunkte[j] = diePunkte[j+1];
					diePunkte[j+1] = c;
				}
			}
		}
		sortiert=true;
	
	}

}
