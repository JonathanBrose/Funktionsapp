package functions;
 
/**
 * This class represents a chaining of two functions, one being the inner function, the second, the outer function.<br>
 * This means the result of the inner function will be used as input for the outer function.<br>
 * <code>f(x) = f(u(x))</code>
 * <br><b>Note:</b> This {@link FunctionPart} returns null in {@link #getAntiderivative()}, because this is extremely complex.
 * @author Jonathan Brose, Nick Dressler 
 *
 */

public class Chaining extends Nexus {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5338877732578061538L;
	/**
	 * f represents the outer functionpart which uses the inner one as input.
	 */
	private FunctionPart f;
	/**
	 * u represents the inner functionpart which is used as input for the outer one.
	 */
	private FunctionPart u;

	/**
	 * Creates a {@link Chaining}.
	 * @param f the outer functionpart, which uses the second one as input.
	 * @param u the inner functionpart, which is used as input for the first one.
	 */
	public Chaining(FunctionPart f, FunctionPart u) {
		this.f = f;
		this.u = u;
	}

	@Override
	public FunctionPart getDerivative() {
		return new FactorNexus(new Chaining(f.getDerivative(), u), u.getDerivative());

	}

	@Override
	public double getFunctionValue(double x0) {
		return this.f.getFunctionValue(this.u.getFunctionValue(x0));
	}

	@Override
	public String[] getChainingString(int i) {
		String[] chainingString = new String[2];
		chainingString[0] = f.getChainingString(i)[0] + u.getChainingString(0)[0];
		chainingString[1] = u.getChainingString(0)[1] + f.getChainingString(0)[1];
		return chainingString;
	}

	@Override
	public boolean isEqual(FunctionPart functionPart) {
		if (functionPart instanceof Chaining)
			return (f.isEqual(((Chaining) functionPart).getF()) && u.isEqual(((Chaining) functionPart).getU()));
		else
			return false;
	}

	@Override
	public FunctionPart clone() {
		return new Chaining(f.clone(), u.clone());
	}

	@Override
	public void multiply(double a) {
		f.multiply(a);
	}

	@Override
	public String getString(int i, String variableName) {
		String s = "";
		for (int c = 0; c < f.getChainingString(0).length; c += 2) {
			s += f.getChainingString(i)[0 + c];
			if (s.endsWith("(") && u instanceof AdditionNexus) {
				s = s.substring(0, s.length() - 1);
				s += u.getString(0, variableName);
				String s2 = f.getChainingString(i)[1 + c];
				if (s2.length() > 1)
					s += s2.substring(1, s2.length());

			} else if (f instanceof PowerFunction && (u instanceof Cos || u instanceof Sin)) {
				s = "";
				FunctionPart f1 = f.clone();
				f1.multiply(Math.pow(u.getA(),((PowerFunction) f).getP()));
				if (u instanceof Cos) {
					String s1 = f1.getChainingString(i)[0];
					s1 = s1.substring(0, s1.length() - 1);
					String s2 = f1.getChainingString(i)[1];
					s += s1 + "cos(" + variableName + s2;
				} else if (u instanceof Sin) {
					String s1 = f1.getChainingString(i)[0];
					s1 = s1.substring(0, s1.length() - 1);
					String s2 = f1.getChainingString(i)[1];
					s += s1 + "sin(" + variableName + s2;
				}

			} else {
				s += u.getString(0, variableName);
				s += f.getChainingString(i)[1 + c];
			}
		}
		return s;
	}

	@Override
	public void setA(double a) {
		f.setA(a);
	}

	@Override
	public double getA() {
		return f.getA();
	}

	/**
	 * 
	 * @return the outer functionPart.
	 */
	public FunctionPart getF() {
		return f;
	}

	/**
	 * 
	 * @return the inner functionPart.
	 */
	public FunctionPart getU() {
		return u;
	}
}
