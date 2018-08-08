package functions;
 
/**
 * FunctionPart containing multiple other functionParts. Abstract parent class of {@link FactorNexus}, {@link Chaining} and
 * {@link AdditionNexus}
 * <br>This {@link FunctionPart} returns null in {@link #getAntiderivative()}.
 * @author Jonathan Brose, Nick Dressler
 * 
 */

public abstract class Nexus extends FunctionPart{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9018293457025021132L;

	@Override
	public abstract FunctionPart getDerivative();

	@Override
	public FunctionPart getAntiderivative() {
		return null;
	}

	@Override
	public abstract double getFunctionValue(double x0);
	@Override
	public abstract double getA();
	@Override
	public abstract void setA(double a);

	@Override
	public abstract String getString(int i, String variablenName);

}
