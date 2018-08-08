package stringConverter;

import java.util.ArrayList;

public class Stack {
	private ArrayList<Token> tokens;
	
	public Stack() {
		tokens = new ArrayList<Token>();
	}
	
	public void add(Token dasToken) {
		tokens.add(dasToken);
	}
	public Token takeToken() {
		if(isEmpty())return null;
		Token dasToken = tokens.get(tokens.size()-1);
		tokens.remove(tokens.size()-1);
		return dasToken;
	}
	public Token getToken() {
		if(isEmpty())return null;
		return tokens.get(tokens.size()-1);
	}
	public boolean isEmpty() {
		return tokens.size() == 0;
	}
	@Override
	public String toString() {
		return tokens.toString();
	}
	

}
