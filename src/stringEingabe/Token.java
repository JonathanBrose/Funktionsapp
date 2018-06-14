package stringEingabe;

import funktionen.Funktionsteil;

public class Token {
	private Operator derOperator;
	private boolean istVariable;
	private String dieZeichen;
	private double wert;
	private boolean istKlammer;
	private boolean klammerAuf;
	private Funktionsteil dasFunktionsteil;
	/**
	 * Konstruktor f&uuml;r Zahlen
	 * @param dieZeichen String mit der Zahl.
	 */
	public Token(String dieZeichen) {
		this.dieZeichen = dieZeichen;
		this.wert = Double.parseDouble(dieZeichen);
	}
	public Token(Funktionsteil dasFunktionsteil) {
		this.dieZeichen = dasFunktionsteil.toString();
		this.dasFunktionsteil = dasFunktionsteil;
		this.wert = Double.NaN;
	}
	public Token(String dieZeichen, boolean istVariable) {
		this.dieZeichen = dieZeichen;
		this.istVariable = istVariable;
		if(!istVariable)
			this.wert = Double.parseDouble(dieZeichen);
		else 
			this.wert = Double.NaN;
	}
	public Token(String dieZeichen, boolean istKlammer,boolean klammerAuf) {
		this.dieZeichen = dieZeichen;
		this.istKlammer = istKlammer;
		this.klammerAuf = klammerAuf;
		this.wert = Double.NaN;
	}
	/**
	 * Konstruktor f&uuml;r Operatoren.
	 * @param derOperator der Operator
	 */
	public Token(Operator derOperator) {
		this.derOperator = derOperator;
		this.dieZeichen = this.derOperator.gibZeichen();
	}
	
	public Operator gibOperator() {
		return derOperator;
	}
	public String gibZeichen() {
		return dieZeichen;
	}
	public double gibWert() {
		return wert;
	}

	public boolean istVariable() {
		return istVariable;
	}
	
	public boolean istKlammer() {
		return istKlammer;
	}

	public boolean istKlammerAuf() {
		if(!istKlammer)
			return false;
		return klammerAuf;
	}
	public boolean istZahl() {
		return !istKlammer && !istVariable && derOperator == null && dasFunktionsteil == null;
	}

	@Override
	public String toString() {
		String s= "";
		
		if(istVariable) {
			s+="Variable "+dieZeichen;
		}else if(dasFunktionsteil!=null){
			s+="Ft: "+dasFunktionsteil.toString();
		}else if(derOperator != null) {
			s+="Operator: "+dieZeichen;
		}else if(istKlammer) {
			s+="Klammer "+dieZeichen;
		}else {
			s+="Zahl: "+wert;
		}
		return s;
	}

	public boolean istFunktion() {
		return this.derOperator == Operator.Sin || this.derOperator == Operator.Cos
				||this.derOperator == Operator.Ln ;
	}

	public boolean istOperator() {
		return this.derOperator == Operator.Addition || this.derOperator == Operator.Multiplikation 
				|| this.derOperator == Operator.Subtraktion || this.derOperator == Operator.Division
				||this.derOperator == Operator.Potenz ;
	}
	public boolean istLinksAssoziativ() {
		return this.derOperator == Operator.Addition || this.derOperator == Operator.Multiplikation 
				|| this.derOperator == Operator.Subtraktion || this.derOperator == Operator.Division;
	}
	public int gibPräzendenz() {
		if(this.derOperator == Operator.Addition ||this.derOperator == Operator.Subtraktion)
			return 1;
		if(this.derOperator == Operator.Multiplikation || this.derOperator == Operator.Division)
			return 2;
		if(this.derOperator == Operator.Potenz)
			return 3;
		if(istKlammer)
			return 4;
		if(istFunktion())
			return 100;
		return 0;
	}
	public Funktionsteil gibFunktionsteil() {
		return dasFunktionsteil;
	}
	public boolean istFunktionsteil() {
		return dasFunktionsteil != null;
	}
	
}
