package functions;
 
import java.util.ArrayList;
/**
*
*Stores multiple {@link FunctionPart}s. They are connected with + or -.
*<br>for example:
*<ul><li>{@link Sin}(1), {@link Cos}(2) und {@link PowerFunction}(2,3)</li></ul>
*this results in: 
*<ul><li>sin(x) + cos(x) + 2x<sup>3</sup></li></ul>
*This {@link Nexus} doesn´t return null in {@link #getAntiderivative()}
* even though its a {@link Nexus}.
*@author Jonathan Brose, Nick Dressler
*/
public class AdditionNexus extends Nexus {
	
	private static final long serialVersionUID = 4256856795591018174L;
	/**
	 * Stores the {@link FunctionPart}s of this AdditionNexus.
	 */
	private ArrayList<FunctionPart> functionParts;
	/**
	 * Creates a {@link AdditionNexus}.
	 * @param functionParts the {@link FunctionPart}s to be added.
	 */
	public AdditionNexus(ArrayList<FunctionPart> functionParts) {
		this.functionParts = functionParts;
		while (containsAdditionNexus())
			sumUp();
	}
	/**
	 * Creates a {@link AdditionNexus}.
	 * @param functionParts the {@link FunctionPart}s to be added.
	 */
	public AdditionNexus(FunctionPart... functionParts) {
		this.functionParts = new ArrayList<FunctionPart>();
		for (int i = 0; i < functionParts.length; i++) {
			this.functionParts.add(functionParts[i]);
		}
		while (containsAdditionNexus())
			sumUp();

	}

	@Override
	public FunctionPart getDerivative() {
	
		ArrayList<FunctionPart> returnList = new ArrayList<FunctionPart>();
		for (FunctionPart dasFunktionsteil : functionParts) {
			returnList.add(dasFunktionsteil.getDerivative());
		}
		return new AdditionNexus(returnList);
	
	}

	@Override
	public FunctionPart getAntiderivative() {
		ArrayList<FunctionPart> returnList = new ArrayList<FunctionPart>();
		for (FunctionPart dasFunktionsteil : functionParts) {
			returnList.add(dasFunktionsteil.getAntiderivative());
		}
		return new AdditionNexus(returnList);
	}

	@Override
	public void multiply(double a) {
		for (FunctionPart functionPart : functionParts) {
			functionPart.multiply(a);
		}
	
	}

	@Override
	public double getFunctionValue(double x0) {
		boolean naN=false;
		double functionValue = 0;
		Double testFunctionValue =0.0;
		for (FunctionPart functionPart : functionParts) {
			testFunctionValue = functionPart.getFunctionValue(x0);
			if(!testFunctionValue.isNaN()) {
				functionValue += testFunctionValue;
				naN=false;
			}else
				naN=true;
		}
		if(naN)
			return Double.NaN;
		
		return functionValue;
	}

	@Override
	public String getString(int i, String variableName) {
		String s ="";
		if(i!=0) s+="+";
		if(i<0) s+=" ";
		s += "(";
		for (FunctionPart functionPart : functionParts) {
			if (functionParts.indexOf(functionPart) != 0)
				s += " ";
	
			i = functionParts.indexOf(functionPart) * -1;
			s += "" + functionPart.getString(i, variableName);
		}
		s += ")";
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
			String innerChainingString[] = functionPart.getChainingString(1);
	
			for (int c1 = 0; c1 < innerChainingString.length; c1 += 2) {
				if (j == 0) {
					chainingString[0] = "(";
				} else {
					chainingString[c + c1] = "";
				}
				chainingString[c + c1] += functionPart.getChainingString(i + j)[c1];
				chainingString[c + c1 + 1] = innerChainingString[c1 + 1];
	
				if (c + c1 + 1 == chainingString.length - 1) {
					chainingString[c + c1 + 1] += ")";
				} else {
					chainingString[c + c1 + 1] += " ";
				}
			}
			c += innerChainingString.length;
	
		}
	
		return chainingString;
	
	}

	@Override
	public FunctionPart clone() {
		ArrayList<FunctionPart> returnList = new ArrayList<FunctionPart>();
		for (FunctionPart functionPart : functionParts) {
			returnList.add(functionPart.clone());
		}
		return new AdditionNexus(returnList);
	}

	@Override
	public boolean isEqual(FunctionPart dasFunktionsteil1) {
	
		if (dasFunktionsteil1 instanceof AdditionNexus) {
			ArrayList<FunctionPart> funcionParts = new ArrayList<FunctionPart>();
			funcionParts.addAll(this.functionParts);
			for (FunctionPart functionPart : ((AdditionNexus) dasFunktionsteil1).gibFunktionsteile()) {
				boolean equals = false;
				for (FunctionPart innerFunctionPart : this.functionParts) {
					if (functionPart.isEqual(innerFunctionPart) && funcionParts.contains(innerFunctionPart)) {
						equals = true;
						funcionParts.remove(innerFunctionPart);
						break;
					}
				}
				if (!equals) {
					return false;
				}
			}
			return true;
	
		} else
			return false;
	}

	@Override
	public double getA() {
		return 1;
	}

	@Override
	public void setA(double a) {
		
	}

	/**
	 * Adds the parameter to this {@link AdditionNexus}.
	 * 
	 * @param functionPart
	 *            {@link FunctionPart} to be added
	 */
	
	public void add(FunctionPart functionPart) {
		this.functionParts.add(functionPart);
		if (this.containsAdditionNexus())
			sumUp();
	}

	/**
	 * Adds all {@link FunctionPart}s to this {@link AdditionNexus}.
	 * 
	 * @param functionParts
	 *            {@link ArrayList} with {@link FunctionPart}s to be added
	 */
	public void addAll(ArrayList<FunctionPart> functionParts) {
		this.functionParts.addAll(functionParts);
		if (this.containsAdditionNexus())
			sumUp();
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

	public ArrayList<FunctionPart> gibFunktionsteile() {
		return functionParts;
	}

	/**
	 * If this {@link AdditionNexus} contains other {@link AdditionNexus}es, this method moves all of their content into
	 * this {@link AdditionNexus} <br>
	 * <br>
	 * <b>For example:</b><br>
	 * <ul><li>AddVerknuepfung.funktionsTeile[{@link AdditionNexus}[{@link Sin} ,
	 * {@link PowerFunction}], {@link ExponentialFunction}]</li></ul>
	 * <ul><li>toString: <code>"(Sin + PowerFunction) + ExponentialFunction"</code></li></ul>
	 * <b>results in:</b> 
	 * <ul><li>AddVerknuepfung.funktionsTeile[{@link Sin}, {@link PowerFunction},
	 * {@link ExponentialFunction}]</li></ul>
	 * <ul><li>toString:<code> "Sin + PowerFunction + ExponentialFunction"</code></li></ul>
	 */
	private void sumUp() {
		ArrayList<FunctionPart> deleteList = new ArrayList<FunctionPart>();
		ArrayList<FunctionPart> addList = new ArrayList<FunctionPart>();
		for (FunctionPart functionPart : functionParts) {
			if (functionPart instanceof AdditionNexus) {
				for (FunctionPart innerFunctionPart : ((AdditionNexus) functionPart).gibFunktionsteile()) {
					addList.add(innerFunctionPart);
				}
				deleteList.add(functionPart);
			}
		}
		functionParts.removeAll(deleteList);
		functionParts.addAll(addList);
	
	}

	/**
	 * 
	 * This method checks wether this {@link AdditionNexus} contains other {@link AdditionNexus}es.
	 * @return - true if other {@link AdditionNexus}es where found, otherwise false.
	 */
	private boolean containsAdditionNexus() {
		for (FunctionPart functionPart : functionParts) {
			if (functionPart instanceof AdditionNexus) {
				return true;
			}
		}
		return false;
	}

}
