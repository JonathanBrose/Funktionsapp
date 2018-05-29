package src;
 
/**
 * Klasse die mehrere {@link Funktionsteil}e beinhaltet. Abstrakte Klasse, Vaterklasse von {@link FaktorVerknuepfung}, {@link Verkettung} und 
 * {@link AddVerknuepfung}
 * <br>Dieses {@link Funktionsteil} gibt bei {@link #gibStammfunktion()} null zurück.
 * @author Jonathan Brose, Nick Dressler
 * 
 */

public abstract class Verknuepfung extends Funktionsteil implements Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9018293457025021132L;

	@Override
	public abstract Funktionsteil gibAbleitung();

	@Override
	public Funktionsteil gibStammfunktion() {
		return null;
	}

	@Override
	public abstract double gibFunktionswert(double x0);
	@Override
	public abstract double gibA();
	@Override
	public abstract void setzeA(double a);

	@Override
	public abstract String gibString(int i, String variablenName);

}
