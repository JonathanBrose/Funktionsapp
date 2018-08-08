package functions;

import java.util.ArrayList;

/**
 * Class representing a multiplication of multiple {@link FunctionPart}s <br>
 * This function is constructed like following: <code>f(x) = u(x) * v(x) * ... </code> <br>
 * Returns always null for {@link #getAntiderivative()}.
 * 
 * @author Jonathan Brose, Nick Dressler
 */

public class FactorNexus extends Nexus {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3100011062478552744L;
	/**
	 * Stores the {@link FunctionPart}s getting multiplied.
	 */
	private ArrayList<FunctionPart> functionParts;

	/**
	 * 
	 * Creates a {@link FactorNexus} with the form of:<br>
	 * <code>f(x) = u(x) * v(x) * ... </code> <br>
	 * 
	 * @param functionParts
	 *            {@link ArrayList} containing {@link FunctionPart}s to be multiplied.
	 */
	public FactorNexus(ArrayList<FunctionPart> functionParts) {
		this.functionParts = functionParts;
		while (containsFactorNexus())
			sumUp();
	}

	/**
	 * 
	 * Creates a {@link FactorNexus} with the form of:<br>
	 * <code>f(x) = u(x) * v(x) * ... </code> <br>
	 * 
	 * @param functionParts
	 *            Array containing {@link FunctionPart}s to be multiplied.
	 */
	public FactorNexus(FunctionPart... functionParts) {
		this.functionParts = new ArrayList<FunctionPart>();
		for (int i = 0; i < functionParts.length; i++) {
			this.functionParts.add(functionParts[i]);
		}
		while (containsFactorNexus())
			sumUp();
	}

	@Override
	public FunctionPart getDerivative() {
		FunctionPart derivative = null;
		if (functionParts.size() > 2) {
			ArrayList<FunctionPart> leftFunctionParts = new ArrayList<FunctionPart>(),
					rightFunctionParts = new ArrayList<FunctionPart>();

			for (int i = 0; i < functionParts.size() / 2; i++) {
				leftFunctionParts.add(functionParts.get(i));
			}
			for (int i = (functionParts.size() / 2); i < functionParts.size(); i++) {
				rightFunctionParts.add(functionParts.get(i));
			}
			FunctionPart rightFunctionPart = new FactorNexus(rightFunctionParts);
			FunctionPart leftFunctionPart = (leftFunctionParts.size() == 1 ? leftFunctionParts.get(0)
					: new FactorNexus(leftFunctionParts));
			derivative = new AdditionNexus(
					new FactorNexus(leftFunctionPart, rightFunctionPart.getDerivative()),
					new FactorNexus(leftFunctionPart.getDerivative(), rightFunctionPart));
		} else if (functionParts.size() == 1) {
			derivative = functionParts.get(0).getDerivative();
		} else {
			FunctionPart u = functionParts.get(0), f = functionParts.get(1);
			derivative = new AdditionNexus(new FactorNexus(f, u.getDerivative()),
					new FactorNexus(f.getDerivative(), u));
		}

		return derivative;
	}

	@Override
	public double getFunctionValue(double x0) {
		double functionValue = 1;
		for (FunctionPart functionPart : functionParts) {

			functionValue *= functionPart.getFunctionValue(x0);
		}
		return functionValue;
	}

	@Override
	public String getString(int i, String variableName) {
		String s = "";
		ArrayList<FunctionPart> fractionFunctionParts = new ArrayList<FunctionPart>();
		ArrayList<FunctionPart> fractionCombinationFunctionParts = new ArrayList<FunctionPart>();
		for (FunctionPart functionParts : functionParts) {
			if (functionParts instanceof PowerFunction) {
				if (((PowerFunction) functionParts).getP() < 0)
					fractionFunctionParts.add(functionParts);
			} else {
				fractionCombinationFunctionParts.add(functionParts);
			}
		}
		functionParts.removeAll(fractionFunctionParts);
		for (FunctionPart fractionFunctionPart : fractionFunctionParts) {
			boolean f = false;
			for (FunctionPart functionPart : fractionCombinationFunctionParts) {
				functionParts.remove(functionPart);
				f=true;
				String sA[] = ((PowerFunction) fractionFunctionPart).getFractionString(i, variableName);
				s+=sA[0]+functionPart.getString(i, variableName)+sA[1]+" * ";
				break;
			}
			if(!f) {
				functionParts.add(fractionFunctionPart);
			}
		}

		for (FunctionPart functionPart : functionParts) {
			s += functionPart.getString(i, variableName) + " * ";
			i = 0;
		}
		// removing the last " * "
		s = s.substring(0, s.length() - 3);

		return s;
	}

	@Override
	public String[] getChainingString(int i) {
		if (functionParts.size() == 1) {
			return functionParts.get(0).getChainingString(i);
		} else if (functionParts.size() == 0)
			return new String[2];

		int count = 0;
		for (FunctionPart functionPart : functionParts) {
			count += functionPart.getChainingString(1).length;
		}
		FunctionPart functionPart;
		String[] chainingString = new String[count];
		int c = 0;
		for (int j = 0; j < functionParts.size(); j++) {
			functionPart = functionParts.get(j);
			String innerChainingString[] = functionPart.getChainingString(0);

			for (int c1 = 0; c1 < innerChainingString.length; c1 += 2) {

				chainingString[c + c1] = innerChainingString[c1];
				chainingString[c + c1 + 1] = innerChainingString[c1 + 1];
				if (c + c1 + 1 < chainingString.length - 1) {
					chainingString[c + c1 + 1] += " * ";
				}
			}
			c += innerChainingString.length;

		}

		return chainingString;

	}

	@Override
	public boolean isEqual(FunctionPart functionPart) {

		if (functionPart instanceof FactorNexus) {
			ArrayList<FunctionPart> functionParts = new ArrayList<FunctionPart>();
			functionParts.addAll(this.functionParts);
			for (FunctionPart otherFunctionPart : ((FactorNexus) functionPart).gibFunktionsteile()) {
				boolean equal = false;
				for (FunctionPart innerFunctionPart : this.functionParts) {
					if (otherFunctionPart.isEqual(innerFunctionPart)
							&& functionParts.contains(innerFunctionPart)) {
						equal = true;
						functionParts.remove(innerFunctionPart);
						break;
					}
				}
				if (!equal) {
					return false;
				}
			}
			return true;

		} else
			return false;
	}

	@Override
	public FunctionPart clone() {
		ArrayList<FunctionPart> newFunctionParts = new ArrayList<FunctionPart>();
		for (FunctionPart functionPart : this.functionParts) {
			newFunctionParts.add(functionPart.clone());
		}
		return new FactorNexus(newFunctionParts);

	}

	@Override
	public void multiply(double a) {
		this.functionParts.get(0).multiply(a);
	}

	@Override
	public double getA() {
		double a = 1;
		for (FunctionPart functionPart : this.functionParts) {
			a *= functionPart.getA();
		}
		return a;
	}

	@Override
	public void setA(double a) {
		this.functionParts.get(0).setA(a);
	}

	/**
	 * Multiplies the factors of all {@link FunctionPart}s in {@link #functionParts} to one product.
	 * Then sets the product as factor of the first {@link FunctionPart}. <br>
	 * 
	 * 
	 * <b>For Example:</b><br>
	 * <ul>
	 * <li>functionParts:
	 * <code> 3*sin(x) *4x<sup>2</sup> *1,5e<sup>x</sup></code><br>
	 * </ul>
	 * <b>Results in:</b><br>
	 * <ul>
	 * <li>functionParts: <code> 18*sin(x) *x<sup>2</sup> *e<sup>x</sup></code><br>
	 * </ul>
	 * 
	 */
	public void sumUpFactors() {
		if (functionParts.size() == 0)
			return;
		double newA = 1;
		for (FunctionPart functionPart : this.functionParts) {
			newA *= functionPart.getA();
			functionPart.setA(1);
		}

		this.functionParts.get(0).multiply(newA);
	}

	/**
	 * Adds the parameter to the {@link FunctionPart}s of this {@link FactorNexus} 
	 * 
	 * @param functionPart
	 *            {@link FunctionPart} to be added.
	 */

	public void add(FunctionPart functionPart) {
		this.functionParts.add(functionPart);
		if (this.containsFactorNexus())
			sumUp();
		sumUpFactors();
	}

	/**
	 * Adds all {@link FunctionPart}s of the parameter List to the {@link FunctionPart}s of this {@link FactorNexus} 
	 * 
	 * @param functionParts
	 *            {@link ArrayList} with {@link FunctionPart}s to be added.
	 */
	public void addAll(ArrayList<FunctionPart> functionParts) {
		this.functionParts.addAll(functionParts);
		if (this.containsFactorNexus())
			sumUp();
		sumUpFactors();
	}

	/**
	 * Getter for the {@link FunctionPart}s of this {@link FactorNexus}
	 * @return {@link ArrayList} {@link #functionParts}
	 */

	public ArrayList<FunctionPart> gibFunktionsteile() {
		return functionParts;
	}

	/**
	 * Returns all FunctionParts that have the same class as the parameter
	 * @param functionClass
	 *            Object that represents the class to be chosen.
	 * 
	 * @return all Objects with the same class like the parameter
	 */
	public ArrayList<FunctionPart> getAllOfClass(FunctionPart functionClass) {
		ArrayList<FunctionPart> returnList = new ArrayList<FunctionPart>();
		for (FunctionPart functionPart : functionParts) {
			if (functionPart.getClass() == functionClass.getClass()) {
				returnList.add(functionPart);
			}
		}
		return returnList;
	}

	/**
	 * If this {@link FactorNexus} contains other {@link FactorNexus}es, this method takes all {@link FunctionPart}s contained by 
	 * the other {@link FactorNexus}es and moves them into this {@link FactorNexus}
	 * <br>
	 * <br>
	 * 
	 * <b> For Example:</b>
	 * <ul>
	 * <li>FaktorVerknuepfung.fucntionParts[{@link FactorNexus}[{@link Sin}
	 * , {@link PowerFunction}], {@link ExponentialFunction}]</li>
	 * </ul>
	 * <ul>
	 * <li>
	 * <code>"(Sin * PowerFunction) * ExponentialFunction"</code></li>
	 * </ul>
	 * <b>results in:</b>
	 * <ul>
	 * <li>FaktorVerknuepfung.functionParts[{@link Sin}, {@link PowerFunction},
	 * {@link ExponentialFunction}]</li>
	 * </ul>
	 * <ul>
	 * <li> <code> "Sin * PowerFunction * ExponentialFunction"</code></li>
	 * </ul>
	 */
	private void sumUp() {
		ArrayList<FunctionPart> deleteList = new ArrayList<FunctionPart>();
		ArrayList<FunctionPart> addList = new ArrayList<FunctionPart>();
		for (FunctionPart functionPart : functionParts) {
			if (functionPart instanceof FactorNexus) {
				for (FunctionPart innerFunctionPart : ((FactorNexus) functionPart)
						.gibFunktionsteile()) {
					addList.add(innerFunctionPart);
				}
				deleteList.add(functionPart);
			}
		}
		functionParts.removeAll(deleteList);
		functionParts.addAll(addList);
	}

	/**
	 * checks whether this {@link FactorNexus} contains other {@link FactorNexus}es or not.
	 * 
	 * @return - true if contains other FactorNexuses, otherwise false
	 */
	private boolean containsFactorNexus() {
		for (FunctionPart functionPart : functionParts) {
			if (functionPart instanceof FactorNexus) {
				return true;
			}
		}
		return false;
	}
}
