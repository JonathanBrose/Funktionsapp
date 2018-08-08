package functions;
 
import java.util.ArrayList;
import java.util.HashMap;

/**
 *Contains methods the simplify a {@link Function}
 * @author Jonathan Brose, Nick Dressler 
 */
public class Simplification {
	public static String NamenAnfang = "(";
	public static String NamenEnde = ")";
	public static String WendepunktName = NamenAnfang + "Turn" + NamenEnde;
	public static String HochpunktName = NamenAnfang + "High" + NamenEnde;
	public static String TiefpunktName = NamenAnfang + "Low" + NamenEnde;
	public static String NullpunktName = NamenAnfang + "Zero" + NamenEnde;
	public static String SattelpunktName = NamenAnfang + "Saddle" + NamenEnde;

	/**
	 * simplifies the Function, or its functionParts List.
	 * @param functionParts List with the function to be simplified.
	 * @return {@link AdditionNexus}, containing the simplified function.
	 */
	public static FunctionPart simplify(ArrayList<FunctionPart> functionParts) {
		ArrayList<ExponentialFunction> exponentialFunctions = new ArrayList<ExponentialFunction>();
		ArrayList<PowerFunction> powerFunctions = new ArrayList<PowerFunction>();
		ArrayList<Sin> sinFunctions = new ArrayList<Sin>();
		ArrayList<Cos> cosFunctions = new ArrayList<Cos>();
		ArrayList<EulerExponentialFunction> eulerExpFunctions = new ArrayList<EulerExponentialFunction>();
		ArrayList<FunctionPart> chainings = new ArrayList<FunctionPart>();
		ArrayList<FunctionPart> factorNexuses = new ArrayList<FunctionPart>();
		ArrayList<FunctionPart> additionNexuses = new ArrayList<FunctionPart>();
		ArrayList<FunctionPart> lnFunctions = new ArrayList<FunctionPart>();
		ArrayList<FunctionPart> xExponentialFunctions = new ArrayList<FunctionPart>();
		ArrayList<PoweredFunctionPart> poweredFunctionParts = new ArrayList<PoweredFunctionPart>();

		for (FunctionPart functionPart : functionParts) {

			if (functionPart instanceof ExponentialFunction) {
				exponentialFunctions.add((ExponentialFunction) functionPart);
			} else if (functionPart instanceof PowerFunction) {
				powerFunctions.add((PowerFunction) functionPart);
			} else if (functionPart instanceof Sin) {
				sinFunctions.add((Sin) functionPart);
			} else if (functionPart instanceof Cos) {
				cosFunctions.add((Cos) functionPart);
			} else if (functionPart instanceof EulerExponentialFunction) {
				eulerExpFunctions.add((EulerExponentialFunction) functionPart);
			} else if (functionPart instanceof Chaining) {
				chainings.add((Chaining) functionPart);
			} else if (functionPart instanceof FactorNexus) {
				factorNexuses.add((FactorNexus) functionPart);
			} else if (functionPart instanceof AdditionNexus) {
				additionNexuses.add((AdditionNexus) functionPart);
			}else if(functionPart instanceof Ln) {
				lnFunctions.add((Ln) functionPart);
			}else if(functionPart instanceof XExponentialFunction) {
				xExponentialFunctions.add(functionPart);
			}else if(functionPart instanceof PoweredFunctionPart) {
				poweredFunctionParts.add((PoweredFunctionPart) functionPart);
			}
		}
		functionParts.clear();

		simplifyCos(cosFunctions);
		simplifyFactor(factorNexuses);
		simplifyExponential(exponentialFunctions);
		simplifyEuler(eulerExpFunctions);
		vereinfacheSin(sinFunctions);
		simplifyPower(powerFunctions);
		simplifyChaining(chainings);
		simplifiyAdd(additionNexuses, functionParts);
		simplifyLn(lnFunctions);
		simplifyXexponential(xExponentialFunctions);

		functionParts.addAll(chainings);
		functionParts.addAll(factorNexuses);
		functionParts.addAll(lnFunctions);
		functionParts.addAll(powerFunctions);
		functionParts.addAll(sinFunctions);
		functionParts.addAll(cosFunctions);
		functionParts.addAll(eulerExpFunctions);
		functionParts.addAll(exponentialFunctions);
		functionParts.addAll(xExponentialFunctions);
		functionParts.addAll(poweredFunctionParts);
		functionParts.addAll(additionNexuses);
		
		

		while (containsAdditionNexus(functionParts)) {
			simplify(functionParts);

		}
		if (functionParts.size() == 1) {
			return functionParts.get(0);
		}
		return new AdditionNexus(functionParts);
	}

	private static boolean containsAdditionNexus(ArrayList<FunctionPart> functionParts) {
		for (FunctionPart functionPart : functionParts) {
			if (functionPart instanceof AdditionNexus)
				return true;
		}
		return false;
	}

	private static void simplifiyAdd(ArrayList<FunctionPart> additionNexuses,
			ArrayList<FunctionPart> functionParts) {
		ArrayList<FunctionPart> deleteList = new ArrayList<FunctionPart>();
		for (FunctionPart additionNexus : additionNexuses) {
			FunctionPart simplifiedFP = simplify(
					((AdditionNexus) additionNexus.clone()).gibFunktionsteile());
			if (simplifiedFP != null) {
				if (simplifiedFP instanceof AdditionNexus) {
					for (FunctionPart x : ((AdditionNexus) simplifiedFP).gibFunktionsteile()) {
						functionParts.add(x);
					}
						deleteList.add(additionNexus);
					
				} else {
					functionParts.add(simplifiedFP);
					deleteList.add(additionNexus);
				}
			}

		}
		additionNexuses.removeAll(deleteList);

	}
	private static void simplifyAddF(ArrayList<FunctionPart> additionNexuses,
			ArrayList<FunctionPart> functionParts) {
		ArrayList<FunctionPart> deleteList = new ArrayList<FunctionPart>();
		for (FunctionPart additionNexus : additionNexuses) {
			FunctionPart simplifiedFP = simplify(
					((AdditionNexus) additionNexus.clone()).gibFunktionsteile());
			if (simplifiedFP != null) {
					functionParts.add(simplifiedFP);
					deleteList.add(additionNexus);
			
			}

		}
		additionNexuses.removeAll(deleteList);

	}

	private static void simplifyExponential(ArrayList<ExponentialFunction> expFunctions) {
		HashMap<Double, Double> sortMap = new HashMap<>();

		for (ExponentialFunction expFunction : expFunctions) {
			if (expFunction.getA() == 0)
				continue;
			double power = expFunction.getB();
			if (sortMap.get(power) == null) {
				sortMap.put(power, 0.0D);
			}
			sortMap.put(power, sortMap.get(power) + expFunction.getA());
		}

		expFunctions.clear();

		for (Double exponent : sortMap.keySet()) {
			expFunctions.add(new ExponentialFunction(sortMap.get(exponent), exponent));
		}
		ExponentialFunction dieExponentialFunktion = null;
		for (int i = 1; i < expFunctions.size(); i++) {
			for (int j = 0; j < expFunctions.size() - i; j++) {
				if (expFunctions.get(j).getB() < expFunctions.get(j + 1).getB()) {
					dieExponentialFunktion = expFunctions.get(j);
					expFunctions.set(j, expFunctions.get(j + 1));
					expFunctions.set(j + 1, dieExponentialFunktion);
				}
			}
		}
	}

	private static void simplifyPower(ArrayList<PowerFunction> powerFunction) {

		HashMap<Double, Double> sortMap = new HashMap<>();

		for (PowerFunction powerFunction1 : powerFunction) {
			if (powerFunction1.getA() == 0)
				continue;
			double power = powerFunction1.getP();
			if (sortMap.get(power) == null) {
				sortMap.put(power, 0.0D);
			}
			sortMap.put(power, sortMap.get(power) + powerFunction1.getA());
		}

		powerFunction.clear();

		for (Double power : sortMap.keySet()) {
			powerFunction.add(new PowerFunction(sortMap.get(power), power));
		}
		PowerFunction powerFunction2 = null;
		for (int i = 1; i < powerFunction.size(); i++) {
			for (int j = 0; j < powerFunction.size() - i; j++) {
				if (powerFunction.get(j).getP() < powerFunction.get(j + 1).getP()) {
					powerFunction2 = powerFunction.get(j);
					powerFunction.set(j, powerFunction.get(j + 1));
					powerFunction.set(j + 1, powerFunction2);
				}
			}
		}

	}

	private static void vereinfacheSin(ArrayList<Sin> sinFunctions) {

		double factorSum = 0.0D;

		for (Sin sinFunction : sinFunctions) {
			if (sinFunction.getA() == 0)
				continue;
			factorSum += sinFunction.getA();
		}

		sinFunctions.clear();
		if (factorSum != 0.0D)
			sinFunctions.add(new Sin(factorSum));
	}
	private static void simplifyLn(ArrayList<FunctionPart> lnFunctions) {

		double factorSum = 0.0D;

		for (FunctionPart lnFunction : lnFunctions) {
			if (lnFunction.getA() == 0)
				continue;
			factorSum += lnFunction.getA();
		}

		lnFunctions.clear();
		if (factorSum != 0.0D)
			lnFunctions.add(new Ln(factorSum));
	}
	private static void simplifyXexponential(ArrayList<FunctionPart> xExponentialFunctions) {

		double summeDerFaktoren = 0.0D;

		for (FunctionPart dieXExponentialFunktion : xExponentialFunctions) {
			if (dieXExponentialFunktion.getA() == 0)
				continue;
			summeDerFaktoren += dieXExponentialFunktion.getA();
		}

		xExponentialFunctions.clear();
		if (summeDerFaktoren != 0.0D)
			xExponentialFunctions.add(new XExponentialFunction(summeDerFaktoren));
	}

	private static void simplifyCos(ArrayList<Cos> cosFunctions) {
		double factorSum = 0.0D;

		for (Cos cosFunction : cosFunctions) {
			if (cosFunction.getA() == 0)
				continue;
			factorSum += cosFunction.getA();
		}

		cosFunctions.clear();
		if (factorSum != 0.0D)
			cosFunctions.add(new Cos(factorSum));
	}

	private static void simplifyEuler(ArrayList<EulerExponentialFunction> eulerFunctions) {
		double factorSum = 0.0D;

		for (EulerExponentialFunction eFunction : eulerFunctions) {
			if (eFunction.getA() == 0)
				continue;
			factorSum += eFunction.getA();
		}

		eulerFunctions.clear();
		if (factorSum != 0.0D)
			eulerFunctions.add(new EulerExponentialFunction(factorSum));
	}

	private static void simplifyChaining(ArrayList<FunctionPart> chainings) {
		ArrayList<FunctionPart> returnList = new ArrayList<FunctionPart>();
		for (FunctionPart chaining : chainings) {
			FunctionPart simplifiedFP = simplifyChaining(((Chaining) chaining).getF(),
					((Chaining) chaining).getU()).clone();
			if (simplifiedFP != null)
				returnList.add(simplifiedFP);
		}
		chainings.clear();
		chainings.addAll(returnList);

	}

	private static FunctionPart simplifyChaining(FunctionPart outerFunctionPart,
			FunctionPart innerFunctionPart) {

		ArrayList<FunctionPart> additionNexuses = new ArrayList<FunctionPart>();
		if (outerFunctionPart instanceof Chaining) {

			outerFunctionPart = simplifyChaining(((Chaining) outerFunctionPart).getF(),
					((Chaining) outerFunctionPart).getU());
			if (outerFunctionPart == null)
				return null;

		} else if (outerFunctionPart instanceof ExponentialFunction) {

			if (innerFunctionPart == null)
				return new PowerFunction(outerFunctionPart.getA(), 0);
			if (innerFunctionPart.getA() == 0 && !(innerFunctionPart instanceof Nexus))
				return new PowerFunction(outerFunctionPart.getA(), 0);

			if (((ExponentialFunction) outerFunctionPart).getA() == 0)

				return null;
		} else if (outerFunctionPart instanceof PowerFunction) {

			if (((PowerFunction) outerFunctionPart).getP() == 0)
				return outerFunctionPart.clone();
			if (((PowerFunction) outerFunctionPart).getP() == 1) {
				innerFunctionPart.multiply(outerFunctionPart.getA());
				return innerFunctionPart.clone();
			}
			if (innerFunctionPart == null)
				return new PowerFunction(outerFunctionPart.getA(), 0);
			if (innerFunctionPart.getA() == 0 && !(innerFunctionPart instanceof Nexus))
				return null;
			if (((PowerFunction) outerFunctionPart).getA() == 0)
				return null;
			if (innerFunctionPart instanceof EulerExponentialFunction) {
				ArrayList<FunctionPart> factorFunctionParts = new ArrayList<FunctionPart>();
				for (int i = 0; i < (int) ((PowerFunction) outerFunctionPart).getP(); i++) {
					double a = 1;
					if (i == 0)
						a = outerFunctionPart.getA() * Math.pow(innerFunctionPart.getA(),
								((PowerFunction) outerFunctionPart).getP());

					factorFunctionParts.add(new EulerExponentialFunction(a));
				}
				return new FactorNexus(factorFunctionParts);
			}

		} else if (outerFunctionPart instanceof Sin) {

			if (innerFunctionPart == null)
				return null;
			if (innerFunctionPart.getA() == 0 && !(innerFunctionPart instanceof Nexus))
				return null;
			if (((Sin) outerFunctionPart).getA() == 0 && !(innerFunctionPart instanceof Nexus))

				return null;
		} else if (outerFunctionPart instanceof Cos) {

			if (innerFunctionPart == null)
				return new PowerFunction(outerFunctionPart.getA(), 0);
			if (innerFunctionPart.getA() == 0)
				return new PowerFunction(outerFunctionPart.getA(), 0);

			if (((Cos) outerFunctionPart).getA() == 0 && !(innerFunctionPart instanceof Nexus))
				return null;

		} else if (outerFunctionPart instanceof EulerExponentialFunction) {

			if (innerFunctionPart.getA() == 0 && !(innerFunctionPart instanceof Nexus))
				return new PowerFunction(outerFunctionPart.getA(), 1);
			if (((EulerExponentialFunction) outerFunctionPart).getA() == 0)
				return null;

		} else if (outerFunctionPart instanceof FactorNexus) {

			outerFunctionPart = simplifyFacor(outerFunctionPart);

		} else if (outerFunctionPart instanceof AdditionNexus) {

			outerFunctionPart = simplify(((AdditionNexus) outerFunctionPart).gibFunktionsteile());
			additionNexuses.addAll(((AdditionNexus) outerFunctionPart).gibFunktionsteile());

		}

		if (innerFunctionPart instanceof Chaining) {

			innerFunctionPart = simplifyChaining(((Chaining) innerFunctionPart).getF(),
					((Chaining) innerFunctionPart).getU());

		} else if (innerFunctionPart instanceof PowerFunction) {

			PowerFunction innerPower = (PowerFunction) innerFunctionPart;

			if (innerPower.getA() == 1 && innerPower.getP() == 1)
				return outerFunctionPart;

		} else if (innerFunctionPart instanceof FactorNexus) {

			innerFunctionPart = simplifyFacor(innerFunctionPart);

		} else if (innerFunctionPart instanceof AdditionNexus) {

			innerFunctionPart = simplify(((AdditionNexus) innerFunctionPart).gibFunktionsteile());
		}

		if (outerFunctionPart.getClass() == innerFunctionPart.getClass()) {

			if (outerFunctionPart instanceof PowerFunction) {

				PowerFunction f1 = (PowerFunction) outerFunctionPart;
				PowerFunction f2 = (PowerFunction) innerFunctionPart;
				if(((PowerFunction) outerFunctionPart).getP() >= 1)
					return new PowerFunction(f1.getA() * Math.pow(f2.getA(), f1.getP()), f1.getP() * f2.getP());
			}
		}

		if (additionNexuses.size() > 0) {

			ArrayList<FunctionPart> dieVerkettungen = new ArrayList<FunctionPart>();
			for (FunctionPart functionPart : additionNexuses) {
				dieVerkettungen.add(new Chaining(functionPart, innerFunctionPart));
			}
			return new AdditionNexus(dieVerkettungen);
		}

		return new Chaining(outerFunctionPart, innerFunctionPart);
	}

	private static void simplifyFactor(ArrayList<FunctionPart> factorNexuses) {
		ArrayList<FunctionPart> returnList = new ArrayList<FunctionPart>();
		for (FunctionPart factorNexus : factorNexuses) {

			FunctionPart simplifiedFP = simplifyFacor(factorNexus.clone());
			if (simplifiedFP != null)
				returnList.add(simplifiedFP);
		}
		factorNexuses.clear();
		factorNexuses.addAll(returnList);

	}

	private static FunctionPart simplifyFacor(FunctionPart factorNexus) {
		ArrayList<FunctionPart> expFunctions = new ArrayList<FunctionPart>();
		ArrayList<PowerFunction> powerFunctions = new ArrayList<PowerFunction>();
		ArrayList<FunctionPart> sinFunctions = new ArrayList<FunctionPart>();
		ArrayList<FunctionPart> cosFunctions = new ArrayList<FunctionPart>();
		ArrayList<FunctionPart> eulerFunctions = new ArrayList<FunctionPart>();
		ArrayList<FunctionPart> chainingFunctions = new ArrayList<FunctionPart>();
		ArrayList<FunctionPart> factorNexuses = new ArrayList<FunctionPart>();
		ArrayList<FunctionPart> additionNexuses = new ArrayList<FunctionPart>();
		ArrayList<FunctionPart> secondAdditionNexuses = new ArrayList<FunctionPart>();
		ArrayList<FunctionPart> lnFunctions = new ArrayList<FunctionPart>();
		ArrayList<FunctionPart> xExponentialFunctions = new ArrayList<FunctionPart>();
		ArrayList<PoweredFunctionPart> poweredFunctionParts = new ArrayList<PoweredFunctionPart>();
 
		for (FunctionPart functionParts : ((FactorNexus) factorNexus).gibFunktionsteile()) {
			if (functionParts instanceof ExponentialFunction) {
				expFunctions.add((ExponentialFunction) functionParts);
			} else if (functionParts instanceof PowerFunction) {
				powerFunctions.add((PowerFunction) functionParts);
			} else if (functionParts instanceof Sin) {
				sinFunctions.add((Sin) functionParts);
			} else if (functionParts instanceof Cos) {
				cosFunctions.add((Cos) functionParts);
			} else if (functionParts instanceof EulerExponentialFunction) {
				eulerFunctions.add((EulerExponentialFunction) functionParts);
			} else if (functionParts instanceof Chaining) {
				chainingFunctions.add((Chaining) functionParts);
			} else if (functionParts instanceof FactorNexus) {
				factorNexuses.add((FactorNexus) functionParts);
			} else if (functionParts instanceof AdditionNexus) {
				additionNexuses.add((AdditionNexus) functionParts);
			} else if (functionParts == null) {
				return null;
			}else if(functionParts instanceof Ln) {
				lnFunctions.add((Ln)functionParts);
			}else if(functionParts instanceof XExponentialFunction) {
				xExponentialFunctions.add(functionParts);
			}else if(functionParts instanceof PoweredFunctionPart) {
				poweredFunctionParts.add((PoweredFunctionPart) functionParts);
			}

		}
		((FactorNexus) factorNexus).gibFunktionsteile().clear();
		simplifyChaining(chainingFunctions);
		simplifyAddF(additionNexuses, secondAdditionNexuses);
		simplifyFactor(factorNexuses);
		simplifyPowerF(powerFunctions);
		simplifyExpF(expFunctions);
		simplifyEulerF(eulerFunctions);
		double a = 1;
		ArrayList<FunctionPart> deleteList = new ArrayList<FunctionPart>();
		for (FunctionPart powerFunction : powerFunctions) {
			if (((PowerFunction) powerFunction).getP() == 0) {
				a *= powerFunction.getA();
				deleteList.add(powerFunction);
			}
		}
		powerFunctions.removeAll(deleteList);
		simplifyCosF(cosFunctions);
		simplifySinF(sinFunctions);
		simplifyXExponentialF(xExponentialFunctions);
		simplifyLnF(lnFunctions);
		FactorNexus factorNexusF = (FactorNexus) factorNexus;
		ArrayList<FunctionPart> functionParts = factorNexusF.gibFunktionsteile();
		functionParts.addAll(poweredFunctionParts);
		functionParts.addAll(chainingFunctions);
		functionParts.addAll(factorNexuses);
		functionParts.addAll(powerFunctions);
		functionParts.addAll(cosFunctions);
		functionParts.addAll(sinFunctions);
		functionParts.addAll(lnFunctions);
		functionParts.addAll(xExponentialFunctions);
		functionParts.addAll(secondAdditionNexuses);
		functionParts.addAll(expFunctions);
		functionParts.addAll(eulerFunctions);
		
		factorNexusF.sumUpFactors();
		factorNexusF.setA(factorNexus.getA() * a);
		if (factorNexusF.getA() == 0)
			return null;

		if (functionParts.size() == 1) {
			return functionParts.get(0);
		}
		return factorNexus;
	}

	private static void simplifyEulerF(ArrayList<FunctionPart> eulerFunctions) {
		if (eulerFunctions.size() > 0) {
			double newA = 1;
			double functionCount = 0;
			for (FunctionPart eulerFunction : eulerFunctions) {
				newA *= eulerFunction.getA();
				functionCount++;
			}
			eulerFunctions.clear();
			if (functionCount > 1) {
				eulerFunctions.add(new Chaining(new PowerFunction(newA, functionCount),
						new EulerExponentialFunction(1)));
			} else {
				eulerFunctions.add(new EulerExponentialFunction(newA));
			}
		}

	}

	private static void simplifyExpF(ArrayList<FunctionPart> expFunctions) {
		if (expFunctions.size() > 0) {
			double newA = 1, newB = 1;
			for (FunctionPart expFunction : expFunctions) {
				ExponentialFunction exponentialFunction = (ExponentialFunction) expFunction;
				newA *= exponentialFunction.getA();
				newB *= exponentialFunction.getB();
			}
			expFunctions.clear();
			expFunctions.add(new ExponentialFunction(newA, newB));
		}
	}

	private static void simplifyPowerF(ArrayList<PowerFunction> powerFunctions) {
		if (powerFunctions.size() != 0) {
			double newP = 0;
			double newA = 1;
			for (PowerFunction powerFunction : powerFunctions) {
				newA *= powerFunction.getA();
				newP += powerFunction.getP();
			}
			powerFunctions.clear();
			if (!(newA == 1 && newP == 0)) {
				powerFunctions.add(new PowerFunction(newA, newP));
			}
		}

	}

	private static void simplifyCosF(ArrayList<FunctionPart> cosFunctions) {
		if (cosFunctions.size() != 0) {
			double p = 0;
			double newA = 1;
			for (FunctionPart cosFunction : cosFunctions) {
				newA *= cosFunction.getA();
				p++;

			}
			cosFunctions.clear();
			if (p > 1) {
				cosFunctions.add(new Chaining(new PowerFunction(newA, p), new Cos(1)));
			} else {
				cosFunctions.add(new Cos(newA));
			}
		}
	}

	private static void simplifySinF(ArrayList<FunctionPart> sinFunctions) {
		if (sinFunctions.size() != 0) {
			double p = 0;
			double newA = 1;
			for (FunctionPart sinFunction : sinFunctions) {
				newA *= sinFunction.getA();
				p++;

			}
			sinFunctions.clear();
			if (p > 1) {
				sinFunctions.add(new Chaining(new PowerFunction(newA, p), new Sin(1)));

			} else {
				sinFunctions.add(new Sin(newA));

			}
		}
	}
	private static void simplifyXExponentialF(ArrayList<FunctionPart> xExponentialFunctions) {
		if (xExponentialFunctions.size() != 0) {
			double p = 0;
			double newA = 1;
			for (FunctionPart xExponentialFunction : xExponentialFunctions) {
				newA *= xExponentialFunction.getA();
				p++;

			}
			xExponentialFunctions.clear();
			if (p > 1) {
				xExponentialFunctions.add(new Chaining(new PowerFunction(newA, p), new XExponentialFunction(1)));
				
			} else {
				xExponentialFunctions.add(new XExponentialFunction(newA));

			}
		}
	}
	private static void simplifyLnF(ArrayList<FunctionPart> lnFunctions) {
		if (lnFunctions.size() != 0) {
			double p = 0;
			double newA = 1;
			for (FunctionPart lnFunction : lnFunctions) {
				newA *= lnFunction.getA();
				p++;

			}
			lnFunctions.clear();
			if (p > 1) {
				lnFunctions.add(new Chaining(new PowerFunction(newA, p), new Ln(1)));
			} else {
				lnFunctions.add(new Ln(newA));

			}
		}
	}

}
