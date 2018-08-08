package stringConverter;

import java.util.ArrayList;

import com.google.common.math.DoubleMath;

import functions.AdditionNexus;
import functions.Chaining;
import functions.Cos;
import functions.EulerExponentialFunction;
import functions.ExponentialFunction;
import functions.FactorNexus;
import functions.Function;
import functions.FunctionPart;
import functions.Ln;
import functions.PowerFunction;
import functions.PoweredFunctionPart;
import functions.Sin;
import functions.Utilities;
import functions.XExponentialFunction;

/**
 * This class contains methods to convert a String into a matching {@link Function} object by using the shunting yard algorithm.
 * 
 * @author Jonathan
 *
 */
public class FunctionBuilder {

	/**
	 * Uses
	 * {@link #stringToFunction(String, String, String)} to convert the input to a {@link Function}
	 *<br>
	 * It trys to find out the variable, and function name from the String,
	 * otherwise it will just use the following defaults: <br>
	 * <ul>
	 * <li>functionName = "f"</li>
	 * <li>variableName = "x"</li>
	 * </ul>
	 * These will be resolved from the following form:<br>
	 * <code>"functionName(variableName) = ..."</code><Br>
	 * for example:<br> <code> "f(x) = ..." </code>
	 * 
	 * @param functionTerm
	 *            The functionTerm
	 * @return Function converted from the functionTerm. if failed, returns null.
	 * @see #stringToFunction(String, String, String)
	 */
	public static Function stringToFunction(String functionTerm) {
		String variableName = "x";
		String functionName = "f";
		if (functionTerm.contains("=")) {
			try {
				String[] sA = functionTerm.split("=");
				functionTerm = sA[1];
				String[] sA2 = sA[0].split("\\(");
				functionName = sA2[0];
				String[] sA3 = sA2[1].split("\\)");
				variableName = sA3[0];
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return stringToFunction(functionTerm, variableName, functionName);
	}

	/**
	 * Converts a String to a {@link Function}. For that it converts the String to a set of {@link Token}s.
	 * After that the tokens will be converted to the reverse polish notation, which allows to then generate the Function in the right order.
	 * 
	 * @param functionTerm
	 *            the function term
	 * @param variableName
	 *            variable after which the functionTerm gets resolved.
	 * @param functionName
	 *            name of the function
	 * @return converted {@link Function}
	 * @see #stringToReversePolishNotation(String, String)
	 */
	public static Function stringToFunction(String functionTerm, String variableName, String functionName) {

		ArrayList<FunctionPart> functionParts = new ArrayList<FunctionPart>();
		ArrayList<Token> reverseNotation = stringToReversePolishNotation(functionTerm, variableName);
		Stack stack = new Stack();
		for (Token t : reverseNotation) {
			if (t.isValue() || t.isVariable()) {
				stack.add(t);
			} else if (t.istOperator()) {
				Token token2 = stack.takeToken();
				Token token1 = stack.takeToken();
				switch (t.getCharacters()) {
				case "*":
					if (token1.isValue()) {
						if (token2.isValue()) {
							stack.add(new Token("" + token1.getValue() * token2.getValue()));
						} else if (token2.isVariable()) {
							stack.add(new Token(new PowerFunction(token1.getValue(), 1)));
						} else if (token2.istFunktionsteil()) {
							token2.gibFunktionsteil().multiply(token1.getValue());
							stack.add(token2);
						}
					} else if (token1.isVariable()) {
						if (token2.isValue()) {
							stack.add(new Token(new PowerFunction(token2.getValue(), 1)));
						} else if (token2.isVariable()) {
							stack.add(new Token(new PowerFunction(1, 2)));
						} else if (token2.istFunktionsteil()) {
							FunctionPart f = new FactorNexus(new PowerFunction(1, 1),
									token2.gibFunktionsteil());
							stack.add(new Token(f));
						}
					} else if (token1.istFunktionsteil()) {
						if (token2.isValue()) {
							token1.gibFunktionsteil().multiply(token2.getValue());
							stack.add(token1);
						} else if (token2.isVariable()) {
							FunctionPart f = new FactorNexus(new PowerFunction(1, 1),
									token1.gibFunktionsteil());
							stack.add(new Token(f));
						} else if (token2.istFunktionsteil()) {
							FunctionPart f = new FactorNexus(token1.gibFunktionsteil(),
									token2.gibFunktionsteil());
							stack.add(new Token(f));
						}
					}
					break;
				case "/":
					if (token1.isValue()) {
						if (token2.isValue()) {
							stack.add(new Token("" + token1.getValue() / token2.getValue()));
						} else if (token2.isVariable()) {
							stack.add(new Token(new PowerFunction(token1.getValue(), -1)));
						} else if (token2.istFunktionsteil()) {
							FunctionPart f = new Chaining(
									new PowerFunction(token1.getValue(), -1), token2.gibFunktionsteil());
							stack.add(new Token(f));
						}
					} else if (token1.isVariable()) {
						if (token2.isValue()) {
							stack.add(new Token(new PowerFunction(1 / token2.getValue(), 1)));
						} else if (token2.isVariable()) {
							stack.add(new Token(new PowerFunction(1, 0)));
						} else if (token2.istFunktionsteil()) {
							FunctionPart f = new FactorNexus(new PowerFunction(1, 1),
									new Chaining(new PowerFunction(1, -1), token2.gibFunktionsteil()));
							stack.add(new Token(f));
						}
					} else if (token1.istFunktionsteil()) {
						if (token2.isValue()) {
							token1.gibFunktionsteil().multiply(1 / token2.getValue());
							stack.add(token1);
						} else if (token2.isVariable()) {
							FunctionPart f = new FactorNexus(new PowerFunction(1, -1),
									token1.gibFunktionsteil());
							stack.add(new Token(f));
						} else if (token2.istFunktionsteil()) {
							FunctionPart f = new FactorNexus(token1.gibFunktionsteil(),
									new Chaining(new PowerFunction(1, -1), token2.gibFunktionsteil()));
							stack.add(new Token(f));
						}
					}
					break;
				case "+":
					if (token1.isValue()) {
						if (token2.isValue()) {
							stack.add(new Token("" + (token1.getValue() + token2.getValue())));
						} else if (token2.isVariable()) {
							FunctionPart f = new AdditionNexus(new PowerFunction(token1.getValue(), 0),
									new PowerFunction(1, 1));
							stack.add(new Token(f));
						} else if (token2.istFunktionsteil()) {
							FunctionPart f = new AdditionNexus(new PowerFunction(token1.getValue(), 0),
									token2.gibFunktionsteil());
							stack.add(new Token(f));
						}
					} else if (token1.isVariable()) {
						if (token2.isValue()) {
							FunctionPart f = new AdditionNexus(new PowerFunction(token2.getValue(), 0),
									new PowerFunction(1, 1));
							stack.add(new Token(f));
						} else if (token2.isVariable()) {
							stack.add(new Token(new PowerFunction(2, 1)));
						} else if (token2.istFunktionsteil()) {
							FunctionPart f = new AdditionNexus(new PowerFunction(1, 1), token2.gibFunktionsteil());
							stack.add(new Token(f));
						}
					} else if (token1.istFunktionsteil()) {
						if (token2.isValue()) {
							FunctionPart f = new AdditionNexus(new PowerFunction(token2.getValue(), 0),
									token1.gibFunktionsteil());
							stack.add(new Token(f));
						} else if (token2.isVariable()) {
							FunctionPart f = new AdditionNexus(new PowerFunction(1, 1), token1.gibFunktionsteil());
							stack.add(new Token(f));
						} else if (token2.istFunktionsteil()) {
							FunctionPart f = new AdditionNexus(token1.gibFunktionsteil(), token2.gibFunktionsteil());
							stack.add(new Token(f));
						}
					}
					break;
				case "-":
					if (token1.isValue()) {
						if (token2.isValue()) {
							stack.add(new Token("" + (token1.getValue() - token2.getValue())));
						} else if (token2.isVariable()) {
							FunctionPart f = new AdditionNexus(new PowerFunction(token1.getValue(), 0),
									new PowerFunction(-1, 1));
							stack.add(new Token(f));
						} else if (token2.istFunktionsteil()) {
							token2.gibFunktionsteil().multiply(-1);
							FunctionPart f = new AdditionNexus(new PowerFunction(token1.getValue(), 0),
									token2.gibFunktionsteil());
							stack.add(new Token(f));
						}
					} else if (token1.isVariable()) {
						if (token2.isValue()) {
							FunctionPart f = new AdditionNexus(new PowerFunction(1, 1),
									new PowerFunction(-token2.getValue(), 0));
							stack.add(new Token(f));
						} else if (token2.isVariable()) {

						} else if (token2.istFunktionsteil()) {
							token2.gibFunktionsteil().multiply(-1);
							FunctionPart f = new AdditionNexus(new PowerFunction(1, 1), token2.gibFunktionsteil());
							stack.add(new Token(f));
						}
					} else if (token1.istFunktionsteil()) {
						if (token2.isValue()) {
							FunctionPart f = new AdditionNexus(token1.gibFunktionsteil(),
									new PowerFunction(-token2.getValue(), 0));
							stack.add(new Token(f));
						} else if (token2.isVariable()) {
							FunctionPart f = new AdditionNexus(token1.gibFunktionsteil(), new PowerFunction(-1, 1));
							stack.add(new Token(f));
						} else if (token2.istFunktionsteil()) {
							token2.gibFunktionsteil().multiply(-1);
							FunctionPart f = new AdditionNexus(token1.gibFunktionsteil(), token2.gibFunktionsteil());
							stack.add(new Token(f));
						}
					}
					break;
				case "^":
					if (token1.isValue()) {
						if (token2.isValue()) {
							stack.add(new Token("" + token1.getValue() * token2.getValue()));
						} else if (token2.isVariable()) {
							double d = token1.getValue() / Math.E;
							if (DoubleMath.fuzzyEquals(d, Utilities.round(d, 1), 0.0001)) {
								stack.add(new Token(new EulerExponentialFunction(d)));
							} else {
								stack.add(new Token("" + Math.pow(token1.getValue(), token2.getValue())));
							}
						} else if (token2.istFunktionsteil()) {
							double d = token1.getValue() / Math.E;
							FunctionPart f;
							if (DoubleMath.fuzzyEquals(d, Utilities.round(d, 1), 0.0001)) {
								f = new Chaining(new EulerExponentialFunction(d), token2.gibFunktionsteil());
								stack.add(new Token(f));
							} else {
								f = new Chaining(new ExponentialFunction(1, token1.getValue()),
										token2.gibFunktionsteil());
								stack.add(new Token(f));
							}

						}
					} else if (token1.isVariable()) {
						if (token2.isValue()) {
							stack.add(new Token(new PowerFunction(1, token2.getValue())));
						} else if (token2.isVariable()) {
							stack.add(new Token(new XExponentialFunction(1)));
						} else if (token2.istFunktionsteil()) {
							stack.add(new Token(new PoweredFunctionPart(1,
									new PowerFunction(1, 1), token2.gibFunktionsteil())));
						}
					} else if (token1.istFunktionsteil()) {
						if (token2.isValue()) {
							FunctionPart f = new Chaining(new PowerFunction(1, token2.getValue()),
									token1.gibFunktionsteil());
							stack.add(new Token(f));
						} else if (token2.isVariable()) {
							stack.add(new Token(new PoweredFunctionPart(1,
									token1.gibFunktionsteil(), new PowerFunction(1, 1) )));
						} else if (token2.istFunktionsteil()) {
							stack.add(new Token(new PoweredFunctionPart(1,
									token1.gibFunktionsteil(), token2.gibFunktionsteil())));
						}
					}
					break;
				}

			} else if (t.isFunction()) {
				Token token1 = stack.takeToken();
				switch (t.getOperator()) {
				case SIN:
					if (token1.isValue()) {
						stack.add(new Token("" + Math.sin(token1.getValue())));
					} else if (token1.isVariable()) {
						stack.add(new Token(new Sin(1)));
					} else if (token1.istFunktionsteil()) {
						stack.add(new Token(new Chaining(new Sin(1), token1.gibFunktionsteil())));
					}
					break;
				case COS:
					if (token1.isValue()) {
						stack.add(new Token("" + Math.cos(token1.getValue())));
					} else if (token1.isVariable()) {
						stack.add(new Token(new Cos(1)));
					} else if (token1.istFunktionsteil()) {
						stack.add(new Token(new Chaining(new Cos(1), token1.gibFunktionsteil())));
					}
					break;
				case LN:
					if (token1.isValue()) {
						stack.add(new Token("" + Math.log(token1.getValue())));
					} else if (token1.isVariable()) {
						stack.add(new Token(new Ln(1)));
					} else if (token1.istFunktionsteil()) {
						stack.add(new Token(new Chaining(new Ln(1), token1.gibFunktionsteil())));
					}
					break;
				case SQUARE_ROOT:
					if (token1.isValue()) {
						stack.add(new Token("" + Math.sqrt(token1.getValue())));
					} else if (token1.isVariable()) {
						stack.add(new Token(new PowerFunction(1, 0.5)));
					} else if (token1.istFunktionsteil()) {
						stack.add(new Token(new Chaining(new PowerFunction(1, 0.5), token1.gibFunktionsteil())));
					}
					break;
				
				}
			}

		}
		while (!stack.isEmpty()) {
			if(stack.getToken().istFunktionsteil())
				functionParts.add(stack.takeToken().gibFunktionsteil());
			else if(stack.getToken().isValue())
				functionParts.add(new PowerFunction(stack.takeToken().getValue(), 0));
		}
		if (functionParts.size() > 1) {
			ArrayList<FunctionPart> parts = new ArrayList<FunctionPart>();
			parts.addAll(functionParts);
			functionParts.clear();
			functionParts.add(new FactorNexus(parts));
		}
		
		Function function;
		try {
			function = new Function(functionParts);
			function.setVariableName(variableName);
			function.setFunctionName(functionName);
		}catch(Exception e) {
			function = null;
		}
		return function;
	}

	/**
	 * Converts the functionTerm to {@link Token}s and sorts them into the reverse polish notation.
	 * @param functionTerm the function term
	 * @param variableName variable after which the functionTerm gets resolved.
	 * @return the function term in the reverse polish notation as a List of {@link Token}s
	 * @see #convertStringToTokens(String, String)
	 */
	private static ArrayList<Token> stringToReversePolishNotation(String functionTerm, String variableName) {
		ArrayList<Token> output = new ArrayList<Token>();
		Stack stack = new Stack();
		for (Token t : convertStringToTokens(functionTerm, variableName)) {
			if (t.isValue() || t.isVariable()) {
				output.add(t);
			} else if (t.isFunction()) {
				stack.add(t);
			} else if (t.istOperator()) {
				while (!stack.isEmpty() && stack.getToken().istOperator()
						&& (t.isLeftAssociative() && t.getPrecedence() <= stack.getToken().getPrecedence()
								|| t.getPrecedence() < stack.getToken().getPrecedence())) {
					output.add(stack.takeToken());
				}
				stack.add(t);
			} else if (t.isBracket()) {
				if (t.isOpeningBracket()) {
					stack.add(t);
				} else {
					while (!(stack.getToken().isOpeningBracket() && stack.getToken().isBracket())) {
						if (stack.isEmpty())
							System.err.println("Error: There is no opening bracket before the closing one!");
						output.add(stack.takeToken());

					}
					stack.takeToken();
					if (!stack.isEmpty() && stack.getToken().isFunction())
						output.add(stack.takeToken());
				}
			}

		}
		while (!stack.isEmpty()) {
			output.add(stack.takeToken());
		}
		return output;
	}
	/**
	 * Converts the String to a List of {@link Token}s
	 * @param functionTerm the String to be converted
	 * @param variableName declares which string should be a variable.
	 * @return The function term as {@link Token}s
	 */
	private static ArrayList<Token> convertStringToTokens(String functionTerm, String variableName) {
		ArrayList<Token> tokens = new ArrayList<Token>();
		functionTerm = functionTerm.toLowerCase();
		boolean negativeNumber = false;
		while (functionTerm.length() > 0) {
			if (negativeNumber) {
				functionTerm = valueToToken(functionTerm, tokens, negativeNumber);
				negativeNumber = false;
				continue;
			}
			if (functionTerm.startsWith(" ")) {
				functionTerm = cutStartOff(functionTerm, 1);
				continue;
			}
			switch (functionTerm.toCharArray()[0]) {
			case ' ':
				functionTerm = cutStartOff(functionTerm, 1);
				continue;
			case '*':
			case '\u2219':
				tokens.add(new Token(Operator.MULTIPLICATION));
				functionTerm = cutStartOff(functionTerm, 1);
				continue;
				
			case '/':
				tokens.add(new Token(Operator.DIVISION));
				functionTerm = cutStartOff(functionTerm, 1);
				continue;
			case '-':

				Token lastToken = null;
				if (tokens.size() > 0)
					lastToken = tokens.get(tokens.size() - 1);

				if (lastToken == null || (lastToken.isBracket() && lastToken.isOpeningBracket())) {
					negativeNumber = true;
				} else if (lastToken.istOperator()) {
					if(lastToken.getPrecedence() > new Token(Operator.SUBTRACTION).getPrecedence())
						negativeNumber = true;	
				} else
					tokens.add(new Token(Operator.SUBTRACTION));

				functionTerm = cutStartOff(functionTerm, 1);
				continue;
			case '+':
				tokens.add(new Token(Operator.ADDITION));
				functionTerm = cutStartOff(functionTerm, 1);
				continue;
			case '^':
				tokens.add(new Token(Operator.POWER));
				functionTerm = cutStartOff(functionTerm, 1);
				continue;
			case '(':
				checkHiddenMultiplikation(tokens);
				tokens.add(new Token(functionTerm.substring(0, 1), true, true));
				functionTerm = cutStartOff(functionTerm, 1);
				continue;
			case ')':
				tokens.add(new Token(functionTerm.substring(0, 1), true, false));
				functionTerm = cutStartOff(functionTerm, 1);
				continue;
			}
			if (functionTerm.startsWith(variableName)) {
				checkHiddenMultiplikation(tokens);
				tokens.add(new Token(variableName, true));
				functionTerm = cutStartOff(functionTerm, variableName.length());
			} else if (functionTerm.startsWith("sin")) {
				checkHiddenMultiplikation(tokens);
				tokens.add(new Token(Operator.SIN));
				functionTerm = cutStartOff(functionTerm, Operator.SIN.getCharacters().length());
			} else if (functionTerm.startsWith("pi")) {
				checkHiddenMultiplikation(tokens);
				tokens.add(new Token("" + Math.PI));
				functionTerm = cutStartOff(functionTerm, 2);
			}else if (functionTerm.startsWith("\u03c0")) {
				checkHiddenMultiplikation(tokens);
				tokens.add(new Token("" + Math.PI));
				functionTerm = cutStartOff(functionTerm, 1);
			} else if (functionTerm.startsWith("e")) {
				checkHiddenMultiplikation(tokens);
				tokens.add(new Token("" + Math.E));
				functionTerm = cutStartOff(functionTerm, 1);
			} else if (functionTerm.startsWith("cos")) {
				checkHiddenMultiplikation(tokens);
				tokens.add(new Token(Operator.COS));
				functionTerm = cutStartOff(functionTerm, Operator.COS.getCharacters().length());
			} else if (functionTerm.startsWith("ln")) {
				checkHiddenMultiplikation(tokens);
				tokens.add(new Token(Operator.LN));
				functionTerm = cutStartOff(functionTerm, Operator.LN.getCharacters().length());
			}else if (functionTerm.startsWith("sqrt")) {
				checkHiddenMultiplikation(tokens);
				tokens.add(new Token(Operator.SQUARE_ROOT));
				functionTerm = cutStartOff(functionTerm, Operator.SQUARE_ROOT.getCharacters().length());
			}else if (functionTerm.startsWith("\u221a")) {
				checkHiddenMultiplikation(tokens);
				tokens.add(new Token(Operator.SQUARE_ROOT));
				functionTerm = cutStartOff(functionTerm, 1);
			} else {
				functionTerm = valueToToken(functionTerm, tokens, negativeNumber);
			}

		}
		return tokens;
	}
	
	private static String valueToToken(String functionTerm, ArrayList<Token> tokens, boolean negativeValue) {
		checkHiddenMultiplikation(tokens);
		String value = "";
		char c;
		if (functionTerm.length() == 0)
			c = ' ';
		else
			c = functionTerm.toCharArray()[0];

		while (isNumber(c) || c == ',' || c == '.') {
			value += c;
			functionTerm = cutStartOff(functionTerm, 1);
			if (functionTerm.length() == 0)
				c = ' ';
			else
				c = functionTerm.toCharArray()[0];
		}
		if (negativeValue) {
			if (value.equals(""))
				value = "-1" + value;
			else
				value = "-" + value;

			negativeValue = false;
		}
		tokens.add(new Token(value.replaceAll("\\,", "\\.")));
		return functionTerm;
	}
	/**
	 * Checks if a multiply operator should be added.
	 * <br>example: 3x -&gt; 3*x
	 * @param tokens the tokens to be checked.
	 */
	private static void checkHiddenMultiplikation(ArrayList<Token> tokens) {
		Token lastToken = null;
		if (tokens.size() > 0) {
			lastToken = tokens.get(tokens.size() - 1);
			if (lastToken.isVariable() || lastToken.isValue()) {
				tokens.add(new Token(Operator.MULTIPLICATION));
			}
		}
	}
	/**
	 * Cuts the given count of chars off the String.
	 * @param s the String
	 * @param count how many places should be cut off
	 * @return the cut String
	 */
	private static String cutStartOff(String s, int count) {
		s = s.substring(count);
		return s;
	}
	/**
	 * Checks whether the given char is a number or not.
	 * @param c char to be checked
	 * @return true if c is a number, otherwise false
	 */
	private static boolean isNumber(char c) {
		if (c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9'
				|| c == '0')
			return true;
		return false;
	}

}
