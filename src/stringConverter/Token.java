package stringConverter;

import functions.FunctionPart;
/**
 * A Token can represent a function, an operator, a value or a variable.
 * @author Jonathan
 *
 */
public class Token {
	private Operator operator;
	private boolean variable;
	private String characters;
	private double value;
	private boolean bracket;
	private boolean openingBracket;
	private FunctionPart functionPart;

	
	public Token(String characters) {
		this.characters = characters;
		this.value = Double.parseDouble(characters);
	}
	public Token(FunctionPart functionPart) {
		this.characters = functionPart.toString();
		this.functionPart = functionPart;
		this.value = Double.NaN;
	}
	public Token(String characters, boolean variable) {
		this.characters = characters;
		this.variable = variable;
		if(!variable)
			this.value = Double.parseDouble(characters);
		else 
			this.value = Double.NaN;
	}
	public Token(String characters, boolean bracket,boolean openingBracket) {
		this.characters = characters;
		this.bracket = bracket;
		this.openingBracket = openingBracket;
		this.value = Double.NaN;
	}
	public Token(Operator operator) {
		this.operator = operator;
		this.characters = this.operator.getCharacters();
	}
	
	public Operator getOperator() {
		return operator;
	}
	public String getCharacters() {
		return characters;
	}
	public double getValue() {
		return value;
	}

	public boolean isVariable() {
		return variable;
	}
	
	public boolean isBracket() {
		return bracket;
	}

	public boolean isOpeningBracket() {
		if(!bracket)
			return false;
		return openingBracket;
	}
	public boolean isValue() {
		return !bracket && !variable && operator == null && functionPart == null;
	}

	@Override
	public String toString() {
		String s= "";
		
		if(variable) {
			s+="variable: "+characters;
		}else if(functionPart!=null){
			s+="Ft: "+functionPart.toString();
		}else if(operator != null) {
			s+="operator: "+characters;
		}else if(isFunction()){
			s+="function: "+characters;
		}else if(bracket) {
			s+="bracket: "+characters;
		}else {
			s+="value: "+value;
		}
		return s;
	}

	public boolean isFunction() {
		return this.operator == Operator.SIN || this.operator == Operator.COS
				||this.operator == Operator.LN || this.operator == Operator.SQUARE_ROOT;
	}

	public boolean istOperator() {
		return this.operator == Operator.ADDITION || this.operator == Operator.MULTIPLICATION 
				|| this.operator == Operator.SUBTRACTION || this.operator == Operator.DIVISION
				||this.operator == Operator.POWER ;
	}
	public boolean isLeftAssociative() {
		return this.operator == Operator.ADDITION || this.operator == Operator.MULTIPLICATION 
				|| this.operator == Operator.SUBTRACTION || this.operator == Operator.DIVISION;
	}
	public int getPrecedence() {
		if(this.operator == Operator.ADDITION ||this.operator == Operator.SUBTRACTION)
			return 1;
		if(this.operator == Operator.MULTIPLICATION || this.operator == Operator.DIVISION)
			return 2;
		if(this.operator == Operator.POWER)
			return 3;
		if(bracket)
			return 4;
		if(isFunction())
			return 100;
		return 0;
	}
	public FunctionPart gibFunktionsteil() {
		return functionPart;
	}
	public boolean istFunktionsteil() {
		return functionPart != null;
	}
	
}
