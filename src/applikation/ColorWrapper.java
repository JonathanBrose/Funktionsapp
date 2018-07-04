package applikation;

import javafx.scene.paint.Color;
	/**
	 * Speichert eine Farbe ({@link Color}) und ermöglicht die Veränderung der Farbe über mehrere Klassen hinweg.
	 * Dies ist nötig, da Instanzen der Klasse {@link Color} final  und somit unveränderbar sind. Also muss das ganze Objekt ersetzt werden.
	 * @author Jonathan Brose
	 *
	 */
public class ColorWrapper {
	/**
	 * Die gespeicherte Farbe 
	 */
	Color dieFarbe;
	
	/**
	 * Konstruktor. 
	 * Erstellt den Wrapper mit der Farbe die Parameter ist.
	 * @param dieFarbe die zu beinhaltende Farbe
	 */
	public ColorWrapper(Color dieFarbe) {
		this.dieFarbe = dieFarbe;
	}
	
	/**
	 * Setzt die Farbe.
	 * @param dieFarbe {@link #dieFarbe}
	 */
	public void setzeFarbe(Color dieFarbe) {
		this.dieFarbe = dieFarbe;
	}
	/**
	 * Gibt die gespeicherte Farbe zur&uuml;ck.
	 * @return {@link #dieFarbe} 
	 */
	public Color gibFarbe() {
		return dieFarbe;
	}
	@Override
	public String toString() {
		return dieFarbe.toString();
	}
}
