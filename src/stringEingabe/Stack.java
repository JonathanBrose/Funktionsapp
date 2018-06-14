package stringEingabe;

import java.util.ArrayList;

public class Stack {
	private ArrayList<Token> dieToken;
	
	public Stack() {
		dieToken = new ArrayList<Token>();
	}
	
	public void fuegeHinzu(Token dasToken) {
		dieToken.add(dasToken);
	}
	public Token nehmeToken() {
		if(istLeer())return null;
		Token dasToken = dieToken.get(dieToken.size()-1);
		dieToken.remove(dieToken.size()-1);
		return dasToken;
	}
	public Token gibToken() {
		if(istLeer())return null;
		return dieToken.get(dieToken.size()-1);
	}
	public boolean istLeer() {
		return dieToken.size() == 0;
	}
	@Override
	public String toString() {
		return dieToken.toString();
	}
	

}
