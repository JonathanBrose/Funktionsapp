package stringConverter;

public enum Operator {
	
	MULTIPLICATION ("*"),
	ADDITION("+"),
	DIVISION("/"),
	SUBTRACTION("-"),
	POWER("^"),
	SIN("sin"),
	COS("cos"),
	LN("ln"),
	SQUARE_ROOT("sqrt");
	
	private Operator(String s) {
		this.characters = s;
	}
	public String getCharacters() {
		return characters;
	}
	private String characters;
}
