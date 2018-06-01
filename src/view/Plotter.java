package view;

import java.util.ArrayList;
import static src.Hilfsmethoden.runde;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import src.Funktion;
import src.Punkt;
/**
 * Verwaltet {@link #dasCanvas} und zeichnet mithilfe von {@link #derGrafikKontext} darauf.
 * @author Jonathan
 *
 */
public class Plotter {
	/**
	 * Das Canvas auf dem gezeichnet wird.
	 */
	private Canvas dasCanvas;
	/**
	 * GrafikKontext von {@link #dasCanvas}. Wird zum zeichnen benutzt.
	 */
	private GraphicsContext derGrafikKontext;
	/**
	 * Skalierung der Zeichungen, oder auch zoom.
	 */
	private double skalierung = 0.7;
	/**
	 * Verschiebung der Zeichnung in x bzw. y-Richtung.
	 */
	private double dX = 0, dY = 0;
	/**
	 * In wie viele Einheiten die X-Achse pro Seite bei 1.0 {@link #skalierung} unterteilt ist.
	 */
	private static final double EINHEITEN_PRO_SEITE_X = 10;
	/**
	 * Der maximale zoom, bzw. der kleinste Wert den {@link #skalierung} annehmen darf.
	 */
	private static final double MINIMALE_SKALIERUNG = 0.05;
	
	/**
	 * Konstruktor
	 * @param dasCanvas das zu verwaltende {@link Canvas}.
	 */
	public Plotter(Canvas dasCanvas) {
		this.dasCanvas = dasCanvas;
		derGrafikKontext = dasCanvas.getGraphicsContext2D();
	}
	/**
	 * Zeichnet die &uuml;bergebene {@link Funktion} in der &uuml;bergebenen Farbe.
	 * @param dieFunktion die Funktion die gezeichnet werden soll.
	 * @param dieFarbe Farbe in der die Funktion gezeichnet werden soll.
	 */
	public void plotteFunktion(Funktion dieFunktion, Color dieFarbe) {
		if (dieFunktion == null)
			return;
		
		if (dieFunktion.entsprichtXAchse())
			return;

		double abstandFaktor = skalierung * EINHEITEN_PRO_SEITE_X / (dasCanvas.getWidth() / 2);
		double abstand = (dasCanvas.getWidth() / 2) / (EINHEITEN_PRO_SEITE_X * skalierung);
		double yVerschiebung = dasCanvas.getHeight() / 2;
		boolean warNaN=false;
		derGrafikKontext.setStroke(dieFarbe);
		derGrafikKontext.setLineWidth(2);
		
		Double derFunktionswert =0.0;
		derGrafikKontext.beginPath();
		for (int i = 0; i <= dasCanvas.getWidth(); i++) {
			double x = i - (dasCanvas.getWidth() / 2);
			x=runde(x * abstandFaktor - dX,6);
			double x2 =runde((i+1 - (dasCanvas.getWidth() / 2)) * abstandFaktor - dX,6);
			derFunktionswert =dieFunktion.berechneFunktionswert(x);
			if (i == 0 || warNaN) {
				warNaN = false;
				if(sollUebersprungenWerden(derFunktionswert, x, x2,dieFunktion)) {
					warNaN = true;
					continue;
				}
				derGrafikKontext.moveTo(i,
						(-derFunktionswert - dY) * abstand+ yVerschiebung);
			}else if(sollUebersprungenWerden(derFunktionswert, x, x2,dieFunktion)) {
				warNaN = true;
				derGrafikKontext.lineTo(i,
						((-derFunktionswert - dY) * abstand + yVerschiebung)*100000000);
				continue;
			}else
				derGrafikKontext.lineTo(i,
					(-derFunktionswert - dY) * abstand + yVerschiebung);
		}
		derGrafikKontext.moveTo(dasCanvas.getWidth() + 1,
				(-1 * dieFunktion.berechneFunktionswert((dasCanvas.getWidth() / 2) * abstandFaktor - dX) - dY) * abstand
						+ yVerschiebung);
		derGrafikKontext.closePath();
		derGrafikKontext.stroke();
		
		
	}
	/**
	 * &Uuml;berpr&uuml;ft ob die n&auml;chste Linie gezeichnet, oder &uuml;bersprungen werden soll.
	 * @param funktionsWert aktueller Funktionswert.
	 * @param x x-Koordinate im Koordinatensystem.
	 * @param dieFunktion die {@link Funktion}, die gezeichnet werden soll.
	 * @param x2 x-Koordinate, die beim n&auml;chsten Schleifendurchlauf kommen wird.
	 * @return wenn &Uuml;bersprungen werden soll true, sonst false.
	 */
	private boolean sollUebersprungenWerden(Double funktionsWert, double x, double x2, Funktion dieFunktion) {
		if(!UeberpruefeDoubleGueltig(funktionsWert))return true;
		if(dieFunktion.berechneFunktionswert(x) > 0 && dieFunktion.berechneFunktionswert(x2) < 0 ) {
			if(!UeberpruefeDoubleGueltig(dieFunktion.berechneFunktionswert(runde(x,1) ))) return true;
			if(!UeberpruefeDoubleGueltig(dieFunktion.berechneFunktionswert(runde(x+runde(x-x2,1),1) ))) return true;
			if(!UeberpruefeDoubleGueltig(dieFunktion.berechneFunktionswert(runde(x2,1) ))) return true;
		}
		if(dieFunktion.berechneFunktionswert(x) < 0 && dieFunktion.berechneFunktionswert(x2) > 0 ) {
			if(!UeberpruefeDoubleGueltig(dieFunktion.berechneFunktionswert(runde(x,1) ))) return true;
			if(!UeberpruefeDoubleGueltig(dieFunktion.berechneFunktionswert(runde(x+runde(x-x2,1),1) ))) return true;
			if(!UeberpruefeDoubleGueltig(dieFunktion.berechneFunktionswert(runde(x2,1) ))) return true;
		}
		return false;
	}
	/**
	 * &Uuml;berpr&uuml;ft ob der Parameter d unendlich oder keine Zahl ist.
	 * @param d der zu pr&uuml;fende Double
	 * @return falls die Zahl nicht unendlich und eine normale Zahle ist, true
	 */
	public boolean UeberpruefeDoubleGueltig(Double d) {
		return !(d.isNaN() || d.isInfinite());
	}
	/**
	 * Zeichnet die von dem {@link KurvendisskusionController} errechneten Punkte in den Graphen ein.
	 * @param derKurvendisskusionController der {@link KurvendisskusionController} der die zu zeichnenden Punkte hat.
	 * @param dieFarbe Farbe in der die {@link Punkt}e gezeichnet werden sollen.
	 */
	public void plotteBesonderePunkte(KurvendisskusionController derKurvendisskusionController, Color dieFarbe) {
		if (derKurvendisskusionController == null)
			return;
		if (!derKurvendisskusionController.sollenPunkteAngezeigtWerden())
			return;
		ArrayList<Punkt> dieBenutztenPunkte = new ArrayList<Punkt>();
		plottePunktListe(derKurvendisskusionController.gibNullstellen(), dieBenutztenPunkte, dieFarbe);
		plottePunktListe(derKurvendisskusionController.gibExtrempunkte(), dieBenutztenPunkte, dieFarbe);
		plottePunktListe(derKurvendisskusionController.gibSattelpunkte(), dieBenutztenPunkte, dieFarbe);
		plottePunktListe(derKurvendisskusionController.gibWendepunkte(), dieBenutztenPunkte, dieFarbe);

	}
	/**
	 * Zeichnet die X und Y-Achse mit Beschriftung.
	 * @see Plotter#zeichneAchsen(double, Color, double)
	 * @see #zeichneBeschriftung(double, Color, double, double, double, double)
	 */
	public void plotteAchsen() {
		
		double abstand = (dasCanvas.getWidth() / 2) / (EINHEITEN_PRO_SEITE_X * skalierung);
		double maxZahlenBreite = 25;
		double kommaUnterteilungen = 1;
		double schriftGroeﬂe = berechneSchriftGroeﬂe();
		double linienDicke =2;
		Color dieFarbe = Color.BLACK;
		if (this.skalierung < 0.25) {
			kommaUnterteilungen = 5;
			if (this.skalierung < 0.08)
				kommaUnterteilungen = 15;
		}
		zeichneAchsen(linienDicke, dieFarbe, abstand);
		zeichneBeschriftung(linienDicke, dieFarbe, abstand, kommaUnterteilungen, schriftGroeﬂe, maxZahlenBreite);
	}
	/**
	 * Zeichnet die X- und die Y-Achse.
	 * @param linienDicke Dicke der Linien in pixeln.
	 * @param dieFarbe {@link Color}, Farbe der Achsen.
	 * @param abstand Abstand zwischen einer Koordinatensystem Einheit in pixeln. 
	 */
	private void zeichneAchsen( double linienDicke, Color dieFarbe, double abstand) {
		derGrafikKontext.setLineWidth(linienDicke);
		derGrafikKontext.setStroke(dieFarbe);
	
			derGrafikKontext.strokeLine(0, this.dasCanvas.getHeight() / 2 - dY * abstand, this.dasCanvas.getWidth(),
					this.dasCanvas.getHeight() / 2 - dY * abstand);
		
			derGrafikKontext.strokeLine(this.dasCanvas.getWidth() / 2 + dX * abstand, 0,
					this.dasCanvas.getWidth() / 2 + dX * abstand, this.dasCanvas.getHeight());
	}
	/**
	 * Zeichnet die Beschriftung f&uuml;r die Achsen.
	 * @param linienDicke die Dicke der Linien des Textes.
	 * @param dieFarbe die Farbe in der gezeichnet wird.
	 * @param abstand ein Koordinateneinheit in pixeln.
	 * @param kommaUnterteilungen wie oft die Beschriftung in Kommazahlen aufgeteilt werden soll.
	 * @param schriftGroeﬂe die Schriftgr&ouml;&szlig;e
	 * @param maxZahlenBreite die maximale Breite die ein einzelner String haben darf.
	 */
	private void zeichneBeschriftung(double linienDicke,Color dieFarbe, double abstand,double kommaUnterteilungen,
			double schriftGroeﬂe, double maxZahlenBreite) {
		
		double c = 0;
		double breite = dasCanvas.getWidth();
		double halbeBreite = breite/ 2;
		double hoehe = dasCanvas.getHeight();
		double halbeHoehe = hoehe/ 2;
		double schrittWeite = abstand / kommaUnterteilungen;
		double schriftPos = 0;
		double ursprung = 0;

		derGrafikKontext.setFill(dieFarbe);
		derGrafikKontext.setFont(new Font(schriftGroeﬂe));
		derGrafikKontext.setLineWidth(linienDicke);
		
		
		// X-Achse -----------------------------------------------------------------
			ursprung =halbeBreite + dX*abstand;
			schriftPos = halbeHoehe- verschiebungY(dY*abstand -schriftGroeﬂe);
			c = 0;
			for (double x = ursprung ; x <= breite; x += schrittWeite) {
				derGrafikKontext.fillText(berechneC(c), x, schriftPos, maxZahlenBreite);
				c += 1 / kommaUnterteilungen;
			}
			c= -1/kommaUnterteilungen;
			for (double z = ursprung-schrittWeite ; z >= 0; z -= schrittWeite) {
				derGrafikKontext.fillText(berechneC(c), z, schriftPos, maxZahlenBreite);
				c -= 1 / kommaUnterteilungen;

			}
			
		// Y-Achse	-----------------------------------------------------------------
			ursprung = halbeHoehe - dY * abstand;
			schriftPos = halbeBreite+ verschiebungX(dX*abstand +2);
			c = 0;
			for (double y = ursprung; y >= 0; y -= schrittWeite) {
				derGrafikKontext.fillText(berechneC(c), schriftPos, y , maxZahlenBreite);
				c += 1 / kommaUnterteilungen;

			}
			c = 1/kommaUnterteilungen;
			for (double y = ursprung + schrittWeite; y <= hoehe; y += schrittWeite) {
				derGrafikKontext.fillText(berechneC(c), schriftPos, y, maxZahlenBreite);
				c -= 1 / kommaUnterteilungen;

			}

	}
	/**
	 * Zeichnet das Gitter f&uuml;r das Koordinatensystem.
	 * @see #plotteGitter(double, double, Color)
	 */
	public void plotteGitter() {
		if (skalierung < 0.25) {
			plotteGitter(15, 0.4, Color.LIGHTGRAY);
			plotteGitter(5, 0.5, Color.web("#969696"));
		} else {
			plotteGitter(5, 0.5, Color.LIGHTGRAY);
		}
		plotteGitter(1, 1, Color.web("#969696"));
	}
	/**
	 * Zeichnet eine Stufe des Gitters.
	 * @param abstandTeilung wie oft das Gitter geteilt werden soll pro Einheit.
	 * @param linienBreite Breite der Linien.
	 * @param dieFarbe Farbe der Linien.
	 */
	private void plotteGitter(double abstandTeilung, double linienBreite, Color dieFarbe) {
		derGrafikKontext.setStroke(dieFarbe);
		derGrafikKontext.setLineWidth(linienBreite * berechneLinienBreiteMultiplikator());
		double halbBreite = dasCanvas.getWidth() / 2;
		double abstand = halbBreite / (EINHEITEN_PRO_SEITE_X * skalierung);
		double xVerschiebung, yVerschiebung;
	
		xVerschiebung = dX - (int) (dX);
		xVerschiebung *= abstand;
		yVerschiebung = dY - (int) (dY);
		yVerschiebung *= -abstand;
		abstand /= abstandTeilung;
		for (double x = halbBreite + xVerschiebung; x <= dasCanvas.getWidth(); x += abstand) {
			derGrafikKontext.strokeLine(x, 0, x, dasCanvas.getHeight());
		}
		for (double x = halbBreite - abstand + xVerschiebung; x >= 0; x -= abstand) {
			derGrafikKontext.strokeLine(x, 0, x, dasCanvas.getHeight());
		}
		double halbHoehe = dasCanvas.getHeight() / 2;
		for (double y = halbHoehe + yVerschiebung; y <= dasCanvas.getHeight(); y += abstand) {
			derGrafikKontext.strokeLine(0, y, dasCanvas.getWidth(), y);
		}
		for (double y = halbHoehe - abstand + yVerschiebung; y >= 0; y -= abstand) {
			derGrafikKontext.strokeLine(0, y, dasCanvas.getWidth(), y);
		}
	}
	/**
	 * Zeichnet eine {@link ArrayList} mit {@link Punkt}
	 * @param diePunktListe die Liste mit den Punkten, die gezeichnet werden sollen.
	 * @param dieBenutztenPunkte Liste mit den Punkten, die schon gezeichnet wurden.
	 * @param dieFarbe Farbe in der die Punkte gezeichnet werden sollen.
	 */
	private void plottePunktListe(ArrayList<Punkt> diePunktListe, ArrayList<Punkt> dieBenutztenPunkte, Color dieFarbe) {
		double punktRadius = 3;
		
		

		if (diePunktListe == null || diePunktListe.size() == 0)
			return;

		double abstand = (dasCanvas.getWidth() / 2) / (EINHEITEN_PRO_SEITE_X * skalierung);
		double yVerschiebung = dasCanvas.getHeight() / 2;
		double xVerschiebung = dasCanvas.getWidth() / 2;
		double schriftGroeﬂe = berechneSchriftGroeﬂe();
		punktRadius *= berechneSchriftGroeﬂe() / 14;

		derGrafikKontext.setFont(new Font(schriftGroeﬂe));
		
		for (Punkt derPunkt : diePunktListe) {
			double yPos = yVerschiebung - derPunkt.gibY() * abstand - dY * abstand - punktRadius * 2;
			if(uberpruefePunktBenutzt(dieBenutztenPunkte, derPunkt)) {
				yPos -= schriftGroeﬂe; 
			}else {
				dieBenutztenPunkte.add(derPunkt);
				//-------Punkt-------
				derGrafikKontext.setFill(Color.BLACK);
				derGrafikKontext.fillOval(xVerschiebung + derPunkt.gibX() * abstand + dX * abstand - punktRadius,
						yVerschiebung - derPunkt.gibY() * abstand - dY * abstand - punktRadius, punktRadius * 2,
						punktRadius * 2);
			}
			//---------Beschriftung----
			derGrafikKontext.setFill(dieFarbe);
			
			if (derPunkt.gibName().equals("(T)"))
				yPos += 3 * punktRadius + schriftGroeﬂe;
			derGrafikKontext.fillText(derPunkt.gibName(),
					xVerschiebung + derPunkt.gibX() * abstand + dX * abstand + punktRadius,
					yPos);
			
		
		}
	}
	/**
	 * &Uuml;berpr&uuml;ft ob in der ArrayList ein Punkt mit den gleichen Werten, wie derPunkt ist.
	 * @param dieBenutztenPunkte Liste mit den bereits geplotteten Punkten.
	 * @param derPunkt {@link Punkt} der auf Gleichheit &uuml;berpr&uuml;ft werden soll.
	 * @return true wenn ein gleicher Punkt enthalten ist, ansonsten false;
	 */
	private boolean uberpruefePunktBenutzt(ArrayList<Punkt> dieBenutztenPunkte, Punkt derPunkt) {
		for(Punkt derBenutztePunkt : dieBenutztenPunkte) {
			if(derBenutztePunkt.entspricht(derPunkt))return true;
		}
		return false;
	}
	/**
	 * Hilfsmethode die XVerschiebung f&uuml;r die Beschriftung der X-Achse Begrenzt.
	 * @param verschiebung die eigentliche Verschiebung.
	 * @return die &uuml;berpr&uuml;fte Verschiebung.
	 */ 
	private double verschiebungX(double verschiebung) {
		if (verschiebung <= -dasCanvas.getWidth() / 2)
			return -dasCanvas.getWidth() / 2;
		if (verschiebung > dasCanvas.getWidth() / 2 - berechneSchriftGroeﬂe() * 2)
			return dasCanvas.getWidth() / 2 - berechneSchriftGroeﬂe() * 2;

		return verschiebung;
	}
	/**
	 * Hilfsmethode die XVerschiebung f&uuml;r die Beschriftung der Y-Achse Begrenzt.
	 * @param verschiebung die eigentliche Verschiebung.
	 * @return die &uuml;berpr&uuml;fte Verschiebung.
	 */ 
	private double verschiebungY(double verschiebung) {
		double d = -dasCanvas.getHeight() / 2;
		if (verschiebung < d)
			return d;
		d = dasCanvas.getHeight() / 2 - berechneSchriftGroeﬂe();
		if (verschiebung > d)
			return d;

		return verschiebung;
	}
	/**
	 * Berechnet aus einem double den passenden String f&uuml;r die Achsenbeschriftung.
	 * @param c der double, der umgewandelt werden soll.
	 * @return der double als String mit Anpassung.
	 */
	private String berechneC(double c) {
		c = runde(c, 2);
		if (skalierung > 1.7)
			return "" + (int) c;
		return "" + c;
	}
	/**
	 * Berechnet die Schriftgr&ouml;&szlig;e f&uuml;r die Beschriftung der Achsen.  
	 * @return Schriftgr&ouml;&szlig;e
	 */
	private double berechneSchriftGroeﬂe() {

		if (skalierung < 0.5)
			return 17;
		if (skalierung > 1.2) {
			double s = 14 - 2 * skalierung;
			if (s >= 4)
				return s;
			return 4;
		}
		return 14;
	}
	/**
	 * Berechnet einen Multiplikator, durch den die Linien abh&auml;ngig von der {@link #skalierung} dicker, bzw. d&uuml;nner werden.
	 * @return der Multiplikator.
	 */
	private double berechneLinienBreiteMultiplikator() {
		return (1 / (skalierung * 3));
	}
	/**
	 * L&ouml;scht die Zeichnung.
	 */
	public void loescheZeichnung() {
		derGrafikKontext.clearRect(0, 0, dasCanvas.getWidth(), dasCanvas.getHeight());
	}
	/**
	 * Setter f&uuml;r {@link #dX}
	 * @param dX neuer Wert f&uuml;r {@link #dX}
	 */
	public void setzeDeltaX(double dX) {
		this.dX = dX;
	}
	/**
	 * Setter f&uuml;r {@link #dY}
	 * @param dY neuer Wert f&uuml;r {@link #dY}
	 */
	public void setzeDeltaY(double dY) {
		this.dY = dY;
	}
	/**
	 * Setter f&uuml;r {@link #skalierung}. Ist begrenzt durch {@link #MINIMALE_SKALIERUNG}.
	 * @param skalierung neuer Wert f&uuml;r {@link #skalierung}, kann nicht kleiner werden als {@value #MINIMALE_SKALIERUNG}.
	 */
	public void setzeSkalierung(double skalierung) {
		if (skalierung >= MINIMALE_SKALIERUNG) {
			this.skalierung = skalierung;
		} else
			setzeSkalierung(MINIMALE_SKALIERUNG);
	}
	/**
	 * Addiert den parameter zu {@link #dX}.
	 * @param addition der Wert der auf {@link #dX} aufaddiert wird.
	 */
	public void addiereZuDeltaX(double addition) {
		this.dX += addition;
	}
	/**
	 * Addiert den parameter zu {@link #dY}.
	 * @param addition der Wert der auf {@link #dY} aufaddiert wird.
	 */
	public void addiereZuDeltaY(double addition) {
		this.dY += addition;
	}
	/**
	 * Addiert den parameter zu {@link #skalierung}.
	 * Der Wert wird allerdings in Abh&auml;ngigkeit von {@link #skalierung} herunterskaliert,
	 *  um die Ver&auml;nderung nicht zu gro&szlig; zu machen.
	 * @param addition der Wert der auf {@link #skalierung} aufaddiert wird.
	 */
	public void addiereZuSkalierung(double addition) {
		addition /= 1000 / (this.skalierung);
		setzeSkalierung(this.skalierung + addition);
	}
	/**
	 * Getter f&uuml;r {@link #dX}
	 * @return {@link #dX}
	 */
	public double gibDeltaX() {
		return dX;
	}
	/**
	 * Getter f&uuml;r {@link #dY}
	 * @return {@link #dY}
	 */
	public double gibDeltaY() {
		return dY;
	}
	/**
	 * Getter f&uuml;r {@link #skalierung}
	 * @return {@link #skalierung}
	 */
	public double gibSkalierung() {
		return skalierung;
	}
}
