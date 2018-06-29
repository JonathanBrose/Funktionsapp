package stringEingabe;

import java.util.ArrayList;

import com.google.common.math.DoubleMath;

import funktionen.AddVerknuepfung;
import funktionen.Cos;
import funktionen.ExponentialFunktion;
import funktionen.FaktorVerknuepfung;
import funktionen.Funktion;
import funktionen.Funktionsteil;
import funktionen.Hilfsmethoden;
import funktionen.Ln;
import funktionen.NatuerlicheExponentialFunktion;
import funktionen.PotenzFunktion;
import funktionen.Sin;
import funktionen.Verkettung;
import funktionen.XExponentialFunktion;

/**
 * Generiert aus einem String eine {@link Funktion}
 * 
 * @author Jonathan
 *
 */
public class FunktionBuilder {

	/**
	 * Wandelt den Funktionsterm mit
	 * {@link #stringZuFunktion(String, String, String)} in eine Funktion um.
	 *
	 * Versucht dabei den Name und die Variable der Funktion herauszufinden,
	 * ansonsten wird von folgendem ausgegangen: <br>
	 * <ul>
	 * <li>funktionsName = "f"</li>
	 * <li>variablenName = "x"</li>
	 * </ul>
	 * Diese werden wenn möglich nach der Form
	 * <code>"funktionsName(variablenName) = ...</code> bestimmt.<Br>
	 * z.B.: <code> "f(x) = ..." </code>
	 * 
	 * @param funktionsterm
	 *            der Funktionsterm
	 * @return Funktion erstellt nach dem Funktionsterm
	 * @see #stringZuFunktion(String, String, String)
	 */
	public static Funktion stringZuFunktion(String funktionsterm) {
		String variablenName = "x";
		String funktionsName = "f";
		if (funktionsterm.contains("=")) {
			try {
				String[] sA = funktionsterm.split("=");
				funktionsterm = sA[1];
				String[] sA2 = sA[0].split("\\(");
				funktionsName = sA2[0];
				String[] sA3 = sA2[1].split("\\)");
				variablenName = sA3[0];
			} catch (Exception e) {
				e.printStackTrace();
				variablenName = "x";
				funktionsName = "f";
			}
		}
		return stringZuFunktion(funktionsterm, variablenName, funktionsName);
	}

	/**
	 * Wandelt den Funktionsterm in eine Funktion um. Dafür wird der Funktionsterm
	 * in {@link Token} umgwandelt und dann in die umgekehrte polnische Notation
	 * umgewandelt.
	 * 
	 * @param funktionsterm
	 *            Funktionsterm der umgewandelt werden soll.
	 * @param variablenName
	 *            variable nach der der Funktionsterm aufgelöst wird
	 * @param funktionsName
	 *            Name der Funktion.
	 * @return Funktion erstellt nach dem Funktionsterm.
	 * @see #stringZuUpn(String, String)
	 */
	public static Funktion stringZuFunktion(String funktionsterm, String variablenName, String funktionsName) {

		ArrayList<Funktionsteil> dieFunktionsteile = new ArrayList<Funktionsteil>();
		ArrayList<Token> umgekehrteForm = stringZuUpn(funktionsterm, variablenName);
		Stack stack = new Stack();
		for (Token t : umgekehrteForm) {
			if (t.istZahl() || t.istVariable()) {
				stack.fuegeHinzu(t);
			} else if (t.istOperator()) {
				Token token2 = stack.nehmeToken();
				Token token1 = stack.nehmeToken();
				switch (t.gibZeichen()) {
				case "*":
					if (token1.istZahl()) {
						if (token2.istZahl()) {
							stack.fuegeHinzu(new Token("" + token1.gibWert() * token2.gibWert()));
						} else if (token2.istVariable()) {
							stack.fuegeHinzu(new Token(new PotenzFunktion(token1.gibWert(), 1)));
						} else if (token2.istFunktionsteil()) {
							token2.gibFunktionsteil().multipliziere(token1.gibWert());
							stack.fuegeHinzu(token2);
						}
					} else if (token1.istVariable()) {
						if (token2.istZahl()) {
							stack.fuegeHinzu(new Token(new PotenzFunktion(token2.gibWert(), 1)));
						} else if (token2.istVariable()) {
							stack.fuegeHinzu(new Token(new PotenzFunktion(1, 2)));
						} else if (token2.istFunktionsteil()) {
							Funktionsteil f = new FaktorVerknuepfung(new PotenzFunktion(1, 1),
									token2.gibFunktionsteil());
							stack.fuegeHinzu(new Token(f));
						}
					} else if (token1.istFunktionsteil()) {
						if (token2.istZahl()) {
							token1.gibFunktionsteil().multipliziere(token2.gibWert());
							stack.fuegeHinzu(token1);
						} else if (token2.istVariable()) {
							Funktionsteil f = new FaktorVerknuepfung(new PotenzFunktion(1, 1),
									token1.gibFunktionsteil());
							stack.fuegeHinzu(new Token(f));
						} else if (token2.istFunktionsteil()) {
							Funktionsteil f = new FaktorVerknuepfung(token1.gibFunktionsteil(),
									token2.gibFunktionsteil());
							stack.fuegeHinzu(new Token(f));
						}
					}
					break;
				case "/":
					if (token1.istZahl()) {
						if (token2.istZahl()) {
							stack.fuegeHinzu(new Token("" + token1.gibWert() / token2.gibWert()));
						} else if (token2.istVariable()) {
							stack.fuegeHinzu(new Token(new PotenzFunktion(token1.gibWert(), -1)));
						} else if (token2.istFunktionsteil()) {
							Funktionsteil f = new Verkettung(
									new PotenzFunktion(token1.gibWert(), -1), token2.gibFunktionsteil());
							stack.fuegeHinzu(new Token(f));
						}
					} else if (token1.istVariable()) {
						if (token2.istZahl()) {
							stack.fuegeHinzu(new Token(new PotenzFunktion(1 / token2.gibWert(), 1)));
						} else if (token2.istVariable()) {
							stack.fuegeHinzu(new Token(new PotenzFunktion(1, 0)));
						} else if (token2.istFunktionsteil()) {
							Funktionsteil f = new FaktorVerknuepfung(new PotenzFunktion(1, 1),
									new Verkettung(new PotenzFunktion(1, -1), token2.gibFunktionsteil()));
							stack.fuegeHinzu(new Token(f));
						}
					} else if (token1.istFunktionsteil()) {
						if (token2.istZahl()) {
							token1.gibFunktionsteil().multipliziere(1 / token2.gibWert());
							stack.fuegeHinzu(token1);
						} else if (token2.istVariable()) {
							Funktionsteil f = new FaktorVerknuepfung(new PotenzFunktion(1, -1),
									token1.gibFunktionsteil());
							stack.fuegeHinzu(new Token(f));
						} else if (token2.istFunktionsteil()) {
							Funktionsteil f = new FaktorVerknuepfung(token1.gibFunktionsteil(),
									new Verkettung(new PotenzFunktion(1, -1), token2.gibFunktionsteil()));
							stack.fuegeHinzu(new Token(f));
						}
					}
					break;
				case "+":
					if (token1.istZahl()) {
						if (token2.istZahl()) {
							stack.fuegeHinzu(new Token("" + (token1.gibWert() + token2.gibWert())));
						} else if (token2.istVariable()) {
							Funktionsteil f = new AddVerknuepfung(new PotenzFunktion(token1.gibWert(), 0),
									new PotenzFunktion(1, 1));
							stack.fuegeHinzu(new Token(f));
						} else if (token2.istFunktionsteil()) {
							Funktionsteil f = new AddVerknuepfung(new PotenzFunktion(token1.gibWert(), 0),
									token2.gibFunktionsteil());
							stack.fuegeHinzu(new Token(f));
						}
					} else if (token1.istVariable()) {
						if (token2.istZahl()) {
							Funktionsteil f = new AddVerknuepfung(new PotenzFunktion(token2.gibWert(), 0),
									new PotenzFunktion(1, 1));
							stack.fuegeHinzu(new Token(f));
						} else if (token2.istVariable()) {
							stack.fuegeHinzu(new Token(new PotenzFunktion(2, 1)));
						} else if (token2.istFunktionsteil()) {
							Funktionsteil f = new AddVerknuepfung(new PotenzFunktion(1, 1), token2.gibFunktionsteil());
							stack.fuegeHinzu(new Token(f));
						}
					} else if (token1.istFunktionsteil()) {
						if (token2.istZahl()) {
							Funktionsteil f = new AddVerknuepfung(new PotenzFunktion(token2.gibWert(), 0),
									token1.gibFunktionsteil());
							stack.fuegeHinzu(new Token(f));
						} else if (token2.istVariable()) {
							Funktionsteil f = new AddVerknuepfung(new PotenzFunktion(1, 1), token1.gibFunktionsteil());
							stack.fuegeHinzu(new Token(f));
						} else if (token2.istFunktionsteil()) {
							Funktionsteil f = new AddVerknuepfung(token1.gibFunktionsteil(), token2.gibFunktionsteil());
							stack.fuegeHinzu(new Token(f));
						}
					}
					break;
				case "-":
					if (token1.istZahl()) {
						if (token2.istZahl()) {
							stack.fuegeHinzu(new Token("" + (token1.gibWert() - token2.gibWert())));
						} else if (token2.istVariable()) {
							Funktionsteil f = new AddVerknuepfung(new PotenzFunktion(token1.gibWert(), 0),
									new PotenzFunktion(-1, 1));
							stack.fuegeHinzu(new Token(f));
						} else if (token2.istFunktionsteil()) {
							token2.gibFunktionsteil().multipliziere(-1);
							Funktionsteil f = new AddVerknuepfung(new PotenzFunktion(token1.gibWert(), 0),
									token2.gibFunktionsteil());
							stack.fuegeHinzu(new Token(f));
						}
					} else if (token1.istVariable()) {
						if (token2.istZahl()) {
							Funktionsteil f = new AddVerknuepfung(new PotenzFunktion(1, 1),
									new PotenzFunktion(-token2.gibWert(), 0));
							stack.fuegeHinzu(new Token(f));
						} else if (token2.istVariable()) {

						} else if (token2.istFunktionsteil()) {
							token2.gibFunktionsteil().multipliziere(-1);
							Funktionsteil f = new AddVerknuepfung(new PotenzFunktion(1, 1), token2.gibFunktionsteil());
							stack.fuegeHinzu(new Token(f));
						}
					} else if (token1.istFunktionsteil()) {
						if (token2.istZahl()) {
							Funktionsteil f = new AddVerknuepfung(token1.gibFunktionsteil(),
									new PotenzFunktion(-token2.gibWert(), 0));
							stack.fuegeHinzu(new Token(f));
						} else if (token2.istVariable()) {
							Funktionsteil f = new AddVerknuepfung(token1.gibFunktionsteil(), new PotenzFunktion(-1, 1));
							stack.fuegeHinzu(new Token(f));
						} else if (token2.istFunktionsteil()) {
							token2.gibFunktionsteil().multipliziere(-1);
							Funktionsteil f = new AddVerknuepfung(token1.gibFunktionsteil(), token2.gibFunktionsteil());
							stack.fuegeHinzu(new Token(f));
						}
					}
					break;
				case "^":
					if (token1.istZahl()) {
						if (token2.istZahl()) {
							stack.fuegeHinzu(new Token("" + token1.gibWert() * token2.gibWert()));
						} else if (token2.istVariable()) {
							double d = token1.gibWert() / Math.E;
							if (DoubleMath.fuzzyEquals(d, Hilfsmethoden.runde(d, 1), 0.0001)) {
								stack.fuegeHinzu(new Token(new NatuerlicheExponentialFunktion(d)));
							} else {
								stack.fuegeHinzu(new Token("" + Math.pow(token1.gibWert(), token2.gibWert())));
							}
						} else if (token2.istFunktionsteil()) {
							double d = token1.gibWert() / Math.E;
							Funktionsteil f;
							if (DoubleMath.fuzzyEquals(d, Hilfsmethoden.runde(d, 1), 0.0001)) {
								f = new Verkettung(new NatuerlicheExponentialFunktion(d), token2.gibFunktionsteil());
								stack.fuegeHinzu(new Token(f));
							} else {
								f = new Verkettung(new ExponentialFunktion(1, token1.gibWert()),
										token2.gibFunktionsteil());
								stack.fuegeHinzu(new Token(f));
							}

						}
					} else if (token1.istVariable()) {
						if (token2.istZahl()) {
							stack.fuegeHinzu(new Token(new PotenzFunktion(1, token2.gibWert())));
						} else if (token2.istVariable()) {
							stack.fuegeHinzu(new Token(new XExponentialFunktion(1)));
						} else if (token2.istFunktionsteil()) {

						}
					} else if (token1.istFunktionsteil()) {
						if (token2.istZahl()) {
							Funktionsteil f = new Verkettung(new PotenzFunktion(1, token2.gibWert()),
									token1.gibFunktionsteil());
							stack.fuegeHinzu(new Token(f));
						} else if (token2.istVariable()) {

						} else if (token2.istFunktionsteil()) {

						}
					}
					break;
				}

			} else if (t.istFunktion()) {
				Token token1 = stack.nehmeToken();
				switch (t.gibOperator()) {
				case Sin:
					if (token1.istZahl()) {
						stack.fuegeHinzu(new Token("" + Math.sin(token1.gibWert())));
					} else if (token1.istVariable()) {
						stack.fuegeHinzu(new Token(new Sin(1)));
					} else if (token1.istFunktionsteil()) {
						stack.fuegeHinzu(new Token(new Verkettung(new Sin(1), token1.gibFunktionsteil())));
					}
					break;
				case Cos:
					if (token1.istZahl()) {
						stack.fuegeHinzu(new Token("" + Math.cos(token1.gibWert())));
					} else if (token1.istVariable()) {
						stack.fuegeHinzu(new Token(new Cos(1)));
					} else if (token1.istFunktionsteil()) {
						stack.fuegeHinzu(new Token(new Verkettung(new Cos(1), token1.gibFunktionsteil())));
					}
					break;
				case Ln:
					if (token1.istZahl()) {
						stack.fuegeHinzu(new Token("" + Math.log(token1.gibWert())));
					} else if (token1.istVariable()) {
						stack.fuegeHinzu(new Token(new Ln(1)));
					} else if (token1.istFunktionsteil()) {
						stack.fuegeHinzu(new Token(new Verkettung(new Ln(1), token1.gibFunktionsteil())));
					}
					break;
				case Wurzel:
					if (token1.istZahl()) {
						stack.fuegeHinzu(new Token("" + Math.sqrt(token1.gibWert())));
					} else if (token1.istVariable()) {
						stack.fuegeHinzu(new Token(new PotenzFunktion(1, 0.5)));
					} else if (token1.istFunktionsteil()) {
						stack.fuegeHinzu(new Token(new Verkettung(new PotenzFunktion(1, 0.5), token1.gibFunktionsteil())));
					}
					break;
				
				}
			}

		}
		while (!stack.istLeer()) {
			if(stack.gibToken().istFunktionsteil())
				dieFunktionsteile.add(stack.nehmeToken().gibFunktionsteil());
			else if(stack.gibToken().istZahl())
				dieFunktionsteile.add(new PotenzFunktion(stack.nehmeToken().gibWert(), 0));
		}
		if (dieFunktionsteile.size() > 1) {
			ArrayList<Funktionsteil> teile = new ArrayList<Funktionsteil>();
			teile.addAll(dieFunktionsteile);
			dieFunktionsteile.clear();
			dieFunktionsteile.add(new FaktorVerknuepfung(teile));
		}
		Funktion f = new Funktion(dieFunktionsteile);
		f.setzeVariablenName(variablenName);
		f.setzeFunktionsName(funktionsName);
		return f;
	}

	/**
	 * 
	 * @param funktionsterm
	 * @param variablenName
	 * @return
	 */
	private static ArrayList<Token> stringZuUpn(String funktionsterm, String variablenName) {
		ArrayList<Token> ausgabe = new ArrayList<Token>();
		Stack stack = new Stack();
		for (Token t : wandleFunktionsTermZuToken(funktionsterm, variablenName)) {
			if (t.istZahl() || t.istVariable()) {
				ausgabe.add(t);
			} else if (t.istFunktion()) {
				stack.fuegeHinzu(t);
			} else if (t.istOperator()) {
				while (!stack.istLeer() && stack.gibToken().istOperator()
						&& (t.istLinksAssoziativ() && t.gibPräzendenz() <= stack.gibToken().gibPräzendenz()
								|| t.gibPräzendenz() < stack.gibToken().gibPräzendenz())) {
					ausgabe.add(stack.nehmeToken());
				}
				stack.fuegeHinzu(t);
			} else if (t.istKlammer()) {
				if (t.istKlammerAuf()) {
					stack.fuegeHinzu(t);
				} else {
					while (!(stack.gibToken().istKlammerAuf() && stack.gibToken().istKlammer())) {
						if (stack.istLeer())
							System.err.println("Fehler: der öffnenden Klammer geht keine schließende voraus");
						ausgabe.add(stack.nehmeToken());

					}
					stack.nehmeToken();
					if (!stack.istLeer() && stack.gibToken().istFunktion())
						ausgabe.add(stack.nehmeToken());
				}
			}

		}
		while (!stack.istLeer()) {
			ausgabe.add(stack.nehmeToken());
		}
		return ausgabe;
	}

	private static ArrayList<Token> wandleFunktionsTermZuToken(String funktionsterm, String variablenName) {
		ArrayList<Token> dieToken = new ArrayList<Token>();
		funktionsterm = funktionsterm.toLowerCase();
		boolean negativeZahl = false;
		while (funktionsterm.length() > 0) {
			if (negativeZahl) {
				funktionsterm = zahlZuToken(funktionsterm, dieToken, negativeZahl);
				negativeZahl = false;
				continue;
			}
			if (funktionsterm.startsWith(" ")) {
				funktionsterm = schneideAnfangWeg(funktionsterm, 1);
				continue;
			}
			switch (funktionsterm.toCharArray()[0]) {
			case ' ':
				funktionsterm = schneideAnfangWeg(funktionsterm, 1);
				continue;
			case '*':
			case '\u2219':
				dieToken.add(new Token(Operator.Multiplikation));
				funktionsterm = schneideAnfangWeg(funktionsterm, 1);
				continue;
				
			case '/':
				dieToken.add(new Token(Operator.Division));
				funktionsterm = schneideAnfangWeg(funktionsterm, 1);
				continue;
			case '-':

				Token letztesToken = null;
				if (dieToken.size() > 0)
					letztesToken = dieToken.get(dieToken.size() - 1);

				if (letztesToken == null || (letztesToken.istKlammer() && letztesToken.istKlammerAuf())) {
					negativeZahl = true;
				} else if (letztesToken.istOperator()) {
					if(letztesToken.gibPräzendenz() > new Token(Operator.Subtraktion).gibPräzendenz())
						negativeZahl = true;	
				} else
					dieToken.add(new Token(Operator.Subtraktion));

				funktionsterm = schneideAnfangWeg(funktionsterm, 1);
				continue;
			case '+':
				dieToken.add(new Token(Operator.Addition));
				funktionsterm = schneideAnfangWeg(funktionsterm, 1);
				continue;
			case '^':
				dieToken.add(new Token(Operator.Potenz));
				funktionsterm = schneideAnfangWeg(funktionsterm, 1);
				continue;
			case '(':
				ueberpruefeVersteckteMultiplikation(dieToken);
				dieToken.add(new Token(funktionsterm.substring(0, 1), true, true));
				funktionsterm = schneideAnfangWeg(funktionsterm, 1);
				continue;
			case ')':
				dieToken.add(new Token(funktionsterm.substring(0, 1), true, false));
				funktionsterm = schneideAnfangWeg(funktionsterm, 1);
				continue;
			}
			if (funktionsterm.startsWith(variablenName)) {
				ueberpruefeVersteckteMultiplikation(dieToken);
				dieToken.add(new Token(variablenName, true));
				funktionsterm = schneideAnfangWeg(funktionsterm, variablenName.length());
			} else if (funktionsterm.startsWith("sin")) {
				ueberpruefeVersteckteMultiplikation(dieToken);
				dieToken.add(new Token(Operator.Sin));
				funktionsterm = schneideAnfangWeg(funktionsterm, Operator.Sin.gibZeichen().length());
			} else if (funktionsterm.startsWith("pi")) {
				ueberpruefeVersteckteMultiplikation(dieToken);
				dieToken.add(new Token("" + Math.PI));
				funktionsterm = schneideAnfangWeg(funktionsterm, 2);
			}else if (funktionsterm.startsWith("\u03c0")) {
				ueberpruefeVersteckteMultiplikation(dieToken);
				dieToken.add(new Token("" + Math.PI));
				funktionsterm = schneideAnfangWeg(funktionsterm, 1);
			} else if (funktionsterm.startsWith("e")) {
				ueberpruefeVersteckteMultiplikation(dieToken);
				dieToken.add(new Token("" + Math.E));
				funktionsterm = schneideAnfangWeg(funktionsterm, 1);
			} else if (funktionsterm.startsWith("cos")) {
				ueberpruefeVersteckteMultiplikation(dieToken);
				dieToken.add(new Token(Operator.Cos));
				funktionsterm = schneideAnfangWeg(funktionsterm, Operator.Cos.gibZeichen().length());
			} else if (funktionsterm.startsWith("ln")) {
				ueberpruefeVersteckteMultiplikation(dieToken);
				dieToken.add(new Token(Operator.Ln));
				funktionsterm = schneideAnfangWeg(funktionsterm, Operator.Ln.gibZeichen().length());
			}else if (funktionsterm.startsWith("sqrt")) {
				ueberpruefeVersteckteMultiplikation(dieToken);
				dieToken.add(new Token(Operator.Wurzel));
				funktionsterm = schneideAnfangWeg(funktionsterm, Operator.Wurzel.gibZeichen().length());
			}else if (funktionsterm.startsWith("\u221a")) {
				ueberpruefeVersteckteMultiplikation(dieToken);
				dieToken.add(new Token(Operator.Wurzel));
				funktionsterm = schneideAnfangWeg(funktionsterm, 1);
			} else {
				funktionsterm = zahlZuToken(funktionsterm, dieToken, negativeZahl);
			}

		}
		return dieToken;
	}

	private static String zahlZuToken(String funktionsterm, ArrayList<Token> dieToken, boolean negativeZahl) {
		ueberpruefeVersteckteMultiplikation(dieToken);
		String zahl = "";
		char c;
		if (funktionsterm.length() == 0)
			c = ' ';
		else
			c = funktionsterm.toCharArray()[0];

		while (istEineZahl(c) || c == ',' || c == '.') {
			zahl += c;
			funktionsterm = schneideAnfangWeg(funktionsterm, 1);
			if (funktionsterm.length() == 0)
				c = ' ';
			else
				c = funktionsterm.toCharArray()[0];
		}
		if (negativeZahl) {
			if (zahl.equals(""))
				zahl = "-1" + zahl;
			else
				zahl = "-" + zahl;

			negativeZahl = false;
		}
		dieToken.add(new Token(zahl.replaceAll("\\,", "\\.")));
		return funktionsterm;
	}

	private static void ueberpruefeVersteckteMultiplikation(ArrayList<Token> dieToken) {
		Token letztesToken = null;
		if (dieToken.size() > 0) {
			letztesToken = dieToken.get(dieToken.size() - 1);
			if (letztesToken.istVariable() || letztesToken.istZahl()) {
				dieToken.add(new Token(Operator.Multiplikation));
			}
		}
	}

	private static String schneideAnfangWeg(String s, int anzahlStellen) {
		s = s.substring(anzahlStellen);
		return s;
	}

	private static boolean istEineZahl(char c) {
		if (c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9'
				|| c == '0')
			return true;
		return false;
	}

	public static void main(String args[]) {
		String funktionsterm1 = "g(x) = sqrt(sin(x))"; 
		Funktion f = stringZuFunktion(funktionsterm1);
		System.out.println(f);
		System.out.println(f.gibAbleitung());
		System.out.println(f.gibAbleitung(2));
	}

}
