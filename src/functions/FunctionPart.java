package functions;
 
import java.io.Serializable;

/**
 * This class is the superclass of all functionParts, which can be stored in
 * {@link Function}.
 * @author Jonathan Brose, Nick Dressler
 */

public abstract class FunctionPart implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8104874808387497036L;
	/**
	 * Returns the function term as String.
	 * calls {@link #getString(int, String)} with i=0 and VariableName "x".
	 * @return function term as String
	 * @see #getString(int, String)
	 */
	@Override
	public String toString() {
		return getString(0, "x");
	}

	/**
	 * Checks whether the to Objects are equal, Meaning the class and attributes are equal.
	 * @param functionPart to check equality
	 * @return true if equal otherwise true
	 */
	public abstract boolean isEqual(FunctionPart functionPart);

	/**
	 * Multiplies the function with the parameter a
	 * 
	 * @param a
	 *            double a
	 */
	public abstract void multiply(double a);

	/**
	 * Calculates the derivative of this functionpart and returns it.
	 * @return derivative as {@link FunctionPart}.
	 */
	public abstract FunctionPart getDerivative();

	/**
	 *Calculates the antiderivative of this functionpart and returns it.
	 * If the functionPart is to complex, returns null.
	 * @return antiderivative as {@link FunctionPart}.
	 */
	public abstract FunctionPart getAntiderivative();

	/**
	 * 
	 * Calculates the function value with x0 as input.
	 * <br><code>return = f(x0)</code>
	 * @return function value with x0 as input.
	 * 
	 * @param x0
	 *            input value of the functionPart.
	 */
	public abstract double getFunctionValue(double x0);

	/**
	 * Returns the function term as String.
	 * @param i
	 *            :<br>
	 *           <ul><li> if <code>i &gt; 0</code>
	 *            the String starts with  "+" or "-" depending on factor a.<br>
	 *            if <code>i=0</code> only "-" will be added (if a is negative)
	 *            so if the a is positive nothing will be added in front of the value.<br>
	 *            <b>For example:</b><ul><li> <code> -4x^2 </code> <br>
	 *            if <code>i  &lt; 0</code> the String is like if <code>i &gt; 0</code>,
	 *           but with a space between.</li></ul>
	 *            <b>For example:</b><ul><li><code>  - 4x^2</code> oder <code> + 4x^2 </code>
	 * 			</li></ul></li></ul>
	 * @param variableName
	 *            <br>
	 *          <ul> <li> Name of the variable used to calculate the function <br>
	 *            <b>For example:</b> <ul><li> <code>f(vName) = vName^2</code></li></ul>
	 *</li></ul>
	 * @return the function term as String.
	 */
	public abstract String getString(int i, String variableName);

	/**
	 * 
	 * @return factor a.
	 */
	public abstract double getA();

	/**
	 * Setter for factor a
	 * @param a
	 *            new value for a
	 */
	public abstract void setA(double a);

	/**
	 * Copies the Object with all of its attributes.
	 * <br>
	 *         <b>Info:</b>
	 *         <ul>
	 *       <li>  All attributes stored in the Object will get also get copied.</li></ul>
	 * 
	 * @return a copy of the {@link FunctionPart}
	 *         
	 * 
	 */
	public abstract FunctionPart clone();

	/**
	 * This method is essential for  {@link Chaining#getString(int, String)}.
	 * Works almost like {@link #getString(int, String)}, but it´s split at the variableName
	 * and both sides get stored in the array. 
	 * 
	 * @param i
	 *            :<br>
	 *           <ul><li> if <code>i &gt; 0</code>
	 *            the String starts with  "+" or "-" depending on factor a.<br>
	 *            if <code>i=0</code> only "-" will be added (if a is negative)
	 *            so if the a is positive nothing will be added in front of the value.<br>
	 *            <b>For example:</b> <ul><li> <code> -4x^2 </code> <br>
	 *            if <code>i  &lt; 0</code> the String is like if <code>i &gt; 0</code>,
	 *           but with a space between.</li></ul>
	 *            <b>For example:</b><ul><li><code>  - 4x^2</code> oder <code> + 4x^2 </code>
	 * </li></ul></li></ul>
	 *  @return an array with the String left and right from the variable.<br>
	 *         <b>For example:</b><ul><li> for <code> -2x^3 </code> the method returns 
	 *         <code> ["-2x("; ")^3"]</code> </li></ul>
	 *         Now the inner function String can be inserted between this parts.
	 *         <br>
	 *         <b>For example:</b> <ul><li><code> -2x(sin(4))^3</code> </li></ul>
	 */
	public abstract String[] getChainingString(int i);
	
	/**
	 * Checks whether the double d is a product of an integer and <b> PI</b> or <b>e</b><br>
	 * In reality this method just calls the real method in {@link Utilities}.
	 * This reduces the imports in the inherits of {@link FunctionPart}.
	 * @param d double to be checked
	 * @return double <b>d</b> the checked value as String.
	 * @see functions.Utilities#checkDouble(double)
	 */
	String checkDouble(double d) {
		return Utilities.checkDouble(d);
	}

}
