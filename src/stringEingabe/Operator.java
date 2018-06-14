package stringEingabe;

public enum Operator {
	
	Multiplikation ("*"),
	Addition("+"),
	Division("/"),
	Subtraktion("-"),
	Potenz("^"),
	Sin("sin"),
	Cos("cos"),
	Ln("ln");
	
	private Operator(String s) {
		this.dasZeichen = s;
	}
	public String gibZeichen() {
		return dasZeichen;
	}
	private String dasZeichen;
}
