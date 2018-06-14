package funktionen;
 
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Beinhaltet die {@link #vereinfache(ArrayList)} 
 * methode und von dieser benutzte privaten methoden welche die 
 * {@link Funktion} vereinfacht. 
 * @author Jonathan Brose, Nick Dressler 
 */
public class Vereinfachung {
	public static String NamenAnfang = "(";
	public static String NamenEnde = ")";
	public static String WendepunktName = NamenAnfang + "W" + NamenEnde;
	public static String HochpunktName = NamenAnfang + "H" + NamenEnde;
	public static String TiefpunktName = NamenAnfang + "T" + NamenEnde;
	public static String NullpunktName = NamenAnfang + "N" + NamenEnde;
	public static String SattelpunktName = NamenAnfang + "S" + NamenEnde;

	/**
	 * Vergleicht die &uuml;bergebenen {@link Funktionsteil}e miteinander und vereinfacht soweit wie mˆglich.
	 * @param dieFunktionsteile die {@link ArrayList} der zuVereinfachenden {@link Funktion}.
	 * @return {@link AddVerknuepfung}, beinhaltet die vereinfachte {@link ArrayList} mit {@link Funktionsteil}en.
	 */
	public static Funktionsteil vereinfache(ArrayList<Funktionsteil> dieFunktionsteile) {
		ArrayList<ExponentialFunktion> dieExponentialFunktionen = new ArrayList<ExponentialFunktion>();
		ArrayList<PotenzFunktion> diePotenzFunktionen = new ArrayList<PotenzFunktion>();
		ArrayList<Sin> dieSinFunktionen = new ArrayList<Sin>();
		ArrayList<Cos> dieCosFunktionen = new ArrayList<Cos>();
		ArrayList<NatuerlicheExponentialFunktion> natuerlicheExponentialFunktionen = new ArrayList<NatuerlicheExponentialFunktion>();
		ArrayList<Funktionsteil> dieVerkettungFunktionen = new ArrayList<Funktionsteil>();
		ArrayList<Funktionsteil> dieFaktorVerknuepfungFunktionen = new ArrayList<Funktionsteil>();
		ArrayList<Funktionsteil> dieAddVerknuepfungFunktionen = new ArrayList<Funktionsteil>();
		ArrayList<Funktionsteil> dieLnFunktionen = new ArrayList<Funktionsteil>();
		ArrayList<Funktionsteil> dieXExponentialFunktionen = new ArrayList<Funktionsteil>();

		for (Funktionsteil dasFunktionsteil : dieFunktionsteile) {

			if (dasFunktionsteil instanceof ExponentialFunktion) {
				dieExponentialFunktionen.add((ExponentialFunktion) dasFunktionsteil);
			} else if (dasFunktionsteil instanceof PotenzFunktion) {
				diePotenzFunktionen.add((PotenzFunktion) dasFunktionsteil);
			} else if (dasFunktionsteil instanceof Sin) {
				dieSinFunktionen.add((Sin) dasFunktionsteil);
			} else if (dasFunktionsteil instanceof Cos) {
				dieCosFunktionen.add((Cos) dasFunktionsteil);
			} else if (dasFunktionsteil instanceof NatuerlicheExponentialFunktion) {
				natuerlicheExponentialFunktionen.add((NatuerlicheExponentialFunktion) dasFunktionsteil);
			} else if (dasFunktionsteil instanceof Verkettung) {
				dieVerkettungFunktionen.add((Verkettung) dasFunktionsteil);
			} else if (dasFunktionsteil instanceof FaktorVerknuepfung) {
				dieFaktorVerknuepfungFunktionen.add((FaktorVerknuepfung) dasFunktionsteil);
			} else if (dasFunktionsteil instanceof AddVerknuepfung) {
				dieAddVerknuepfungFunktionen.add((AddVerknuepfung) dasFunktionsteil);
			}else if(dasFunktionsteil instanceof Ln) {
				dieLnFunktionen.add((Ln) dasFunktionsteil);
			}else if(dasFunktionsteil instanceof XExponentialFunktion) {
				dieXExponentialFunktionen.add(dasFunktionsteil);
			}
		}
		dieFunktionsteile.clear();

		vereinfacheCos(dieCosFunktionen);
		vereinfacheFaktor(dieFaktorVerknuepfungFunktionen);
		vereinfacheExponential(dieExponentialFunktionen);
		vereinfacheNExponential(natuerlicheExponentialFunktionen);
		vereinfacheSin(dieSinFunktionen);
		vereinfachePotenz(diePotenzFunktionen);
		vereinfacheVerkettung(dieVerkettungFunktionen);
		vereinfacheAdd(dieAddVerknuepfungFunktionen, dieFunktionsteile);
		vereinfacheLn(dieLnFunktionen);
		vereinfacheXExponential(dieXExponentialFunktionen);

		dieFunktionsteile.addAll(dieVerkettungFunktionen);
		dieFunktionsteile.addAll(dieFaktorVerknuepfungFunktionen);
		dieFunktionsteile.addAll(dieLnFunktionen);
		dieFunktionsteile.addAll(diePotenzFunktionen);
		dieFunktionsteile.addAll(dieSinFunktionen);
		dieFunktionsteile.addAll(dieCosFunktionen);
		dieFunktionsteile.addAll(natuerlicheExponentialFunktionen);
		dieFunktionsteile.addAll(dieExponentialFunktionen);
		dieFunktionsteile.addAll(dieXExponentialFunktionen);
		dieFunktionsteile.addAll(dieAddVerknuepfungFunktionen);
		

		while (enthaeltAddVerknuepfung(dieFunktionsteile)) {
			vereinfache(dieFunktionsteile);

		}
		if (dieFunktionsteile.size() == 1) {
			return dieFunktionsteile.get(0);
		}
		return new AddVerknuepfung(dieFunktionsteile);
	}

	private static boolean enthaeltAddVerknuepfung(ArrayList<Funktionsteil> dieFunktionsteile) {
		for (Funktionsteil dasFunktionsteil : dieFunktionsteile) {
			if (dasFunktionsteil instanceof AddVerknuepfung)
				return true;
		}
		return false;
	}

	private static void vereinfacheAdd(ArrayList<Funktionsteil> dieAddVerknuepfungFunktionen,
			ArrayList<Funktionsteil> funktionsTeile) {
		ArrayList<Funktionsteil> dieZuLoeschendenFunktionsteile = new ArrayList<Funktionsteil>();
		for (Funktionsteil dieAddVerknuepfung : dieAddVerknuepfungFunktionen) {
			Funktionsteil dieVereinfachteAddVerknuepfung = vereinfache(
					((AddVerknuepfung) dieAddVerknuepfung.clone()).gibFunktionsteile());
			if (dieVereinfachteAddVerknuepfung != null) {
				if (dieVereinfachteAddVerknuepfung instanceof AddVerknuepfung) {
					for (Funktionsteil x : ((AddVerknuepfung) dieVereinfachteAddVerknuepfung).gibFunktionsteile()) {
						funktionsTeile.add(x);
					}
					dieZuLoeschendenFunktionsteile.add(dieAddVerknuepfung);

				} else {
					funktionsTeile.add(dieVereinfachteAddVerknuepfung);
					dieZuLoeschendenFunktionsteile.add(dieAddVerknuepfung);
				}
			}

		}
		dieAddVerknuepfungFunktionen.removeAll(dieZuLoeschendenFunktionsteile);

	}

	private static void vereinfacheExponential(ArrayList<ExponentialFunktion> dieExponentialfunktionen) {
		HashMap<Double, Double> dieSortierungsMap = new HashMap<>();

		for (ExponentialFunktion dieExponentialFunktion : dieExponentialfunktionen) {
			if (dieExponentialFunktion.gibA() == 0)
				continue;
			double exponent = dieExponentialFunktion.gibB();
			if (dieSortierungsMap.get(exponent) == null) {
				dieSortierungsMap.put(exponent, 0.0D);
			}
			dieSortierungsMap.put(exponent, dieSortierungsMap.get(exponent) + dieExponentialFunktion.gibA());
		}

		dieExponentialfunktionen.clear();

		for (Double exponent : dieSortierungsMap.keySet()) {
			dieExponentialfunktionen.add(new ExponentialFunktion(dieSortierungsMap.get(exponent), exponent));
		}
		ExponentialFunktion dieExponentialFunktion = null;
		for (int i = 1; i < dieExponentialfunktionen.size(); i++) {
			for (int j = 0; j < dieExponentialfunktionen.size() - i; j++) {
				if (dieExponentialfunktionen.get(j).gibB() < dieExponentialfunktionen.get(j + 1).gibB()) {
					dieExponentialFunktion = dieExponentialfunktionen.get(j);
					dieExponentialfunktionen.set(j, dieExponentialfunktionen.get(j + 1));
					dieExponentialfunktionen.set(j + 1, dieExponentialFunktion);
				}
			}
		}
	}

	private static void vereinfachePotenz(ArrayList<PotenzFunktion> diePotenzFunktionen) {

		HashMap<Double, Double> dieSortierungsMap = new HashMap<>();

		for (PotenzFunktion potenzFunktion : diePotenzFunktionen) {
			if (potenzFunktion.gibA() == 0)
				continue;
			double exponent = potenzFunktion.gibP();
			if (dieSortierungsMap.get(exponent) == null) {
				dieSortierungsMap.put(exponent, 0.0D);
			}
			dieSortierungsMap.put(exponent, dieSortierungsMap.get(exponent) + potenzFunktion.gibA());
		}

		diePotenzFunktionen.clear();

		for (Double exponent : dieSortierungsMap.keySet()) {
			diePotenzFunktionen.add(new PotenzFunktion(dieSortierungsMap.get(exponent), exponent));
		}
		PotenzFunktion diePotenzFunktion = null;
		for (int i = 1; i < diePotenzFunktionen.size(); i++) {
			for (int j = 0; j < diePotenzFunktionen.size() - i; j++) {
				if (diePotenzFunktionen.get(j).gibP() < diePotenzFunktionen.get(j + 1).gibP()) {
					diePotenzFunktion = diePotenzFunktionen.get(j);
					diePotenzFunktionen.set(j, diePotenzFunktionen.get(j + 1));
					diePotenzFunktionen.set(j + 1, diePotenzFunktion);
				}
			}
		}

	}

	private static void vereinfacheSin(ArrayList<Sin> dieSinusFunktionen) {

		double summeDerFaktoren = 0.0D;

		for (Sin sinFunktion : dieSinusFunktionen) {
			if (sinFunktion.gibA() == 0)
				continue;
			summeDerFaktoren += sinFunktion.gibA();
		}

		dieSinusFunktionen.clear();
		if (summeDerFaktoren != 0.0D)
			dieSinusFunktionen.add(new Sin(summeDerFaktoren));
	}
	private static void vereinfacheLn(ArrayList<Funktionsteil> dieLnFunktionen) {

		double summeDerFaktoren = 0.0D;

		for (Funktionsteil lnFunktion : dieLnFunktionen) {
			if (lnFunktion.gibA() == 0)
				continue;
			summeDerFaktoren += lnFunktion.gibA();
		}

		dieLnFunktionen.clear();
		if (summeDerFaktoren != 0.0D)
			dieLnFunktionen.add(new Ln(summeDerFaktoren));
	}
	private static void vereinfacheXExponential(ArrayList<Funktionsteil> dieXExponentialFunktionen) {

		double summeDerFaktoren = 0.0D;

		for (Funktionsteil dieXExponentialFunktion : dieXExponentialFunktionen) {
			if (dieXExponentialFunktion.gibA() == 0)
				continue;
			summeDerFaktoren += dieXExponentialFunktion.gibA();
		}

		dieXExponentialFunktionen.clear();
		if (summeDerFaktoren != 0.0D)
			dieXExponentialFunktionen.add(new XExponentialFunktion(summeDerFaktoren));
	}

	private static void vereinfacheCos(ArrayList<Cos> dieCosinusFunktionen) {
		double summeDerFaktoren = 0.0D;

		for (Cos dieCosinusFunktion : dieCosinusFunktionen) {
			if (dieCosinusFunktion.gibA() == 0)
				continue;
			summeDerFaktoren += dieCosinusFunktion.gibA();
		}

		dieCosinusFunktionen.clear();
		if (summeDerFaktoren != 0.0D)
			dieCosinusFunktionen.add(new Cos(summeDerFaktoren));
	}

	private static void vereinfacheNExponential(ArrayList<NatuerlicheExponentialFunktion> dieNatExponentialFunktionen) {
		double summeDerFaktoren = 0.0D;

		for (NatuerlicheExponentialFunktion dieEFunktion : dieNatExponentialFunktionen) {
			if (dieEFunktion.gibA() == 0)
				continue;
			summeDerFaktoren += dieEFunktion.gibA();
		}

		dieNatExponentialFunktionen.clear();
		if (summeDerFaktoren != 0.0D)
			dieNatExponentialFunktionen.add(new NatuerlicheExponentialFunktion(summeDerFaktoren));
	}

	private static void vereinfacheVerkettung(ArrayList<Funktionsteil> dieVerkettungen) {
		ArrayList<Funktionsteil> dieRueckgabeListe = new ArrayList<Funktionsteil>();
		for (Funktionsteil dieVerkettung : dieVerkettungen) {
			Funktionsteil dasVereinfachteFunktionsteil = vereinfacheVerkettung(((Verkettung) dieVerkettung).getF(),
					((Verkettung) dieVerkettung).getU()).clone();
			if (dasVereinfachteFunktionsteil != null)
				dieRueckgabeListe.add(dasVereinfachteFunktionsteil);
		}
		dieVerkettungen.clear();
		dieVerkettungen.addAll(dieRueckgabeListe);

	}

	private static Funktionsteil vereinfacheVerkettung(Funktionsteil dasAeuﬂereFunktionsteil,
			Funktionsteil dasInnereFunktionsteil) {

		ArrayList<Funktionsteil> dieAddVerknuepfungsTeile = new ArrayList<Funktionsteil>();
		if (dasAeuﬂereFunktionsteil instanceof Verkettung) {

			dasAeuﬂereFunktionsteil = vereinfacheVerkettung(((Verkettung) dasAeuﬂereFunktionsteil).getF(),
					((Verkettung) dasAeuﬂereFunktionsteil).getU());
			if (dasAeuﬂereFunktionsteil == null)
				return null;

		} else if (dasAeuﬂereFunktionsteil instanceof ExponentialFunktion) {

			if (dasInnereFunktionsteil == null)
				return new PotenzFunktion(dasAeuﬂereFunktionsteil.gibA(), 0);
			if (dasInnereFunktionsteil.gibA() == 0 && !(dasInnereFunktionsteil instanceof Verknuepfung))
				return new PotenzFunktion(dasAeuﬂereFunktionsteil.gibA(), 0);

			if (((ExponentialFunktion) dasAeuﬂereFunktionsteil).gibA() == 0)

				return null;
		} else if (dasAeuﬂereFunktionsteil instanceof PotenzFunktion) {

			if (((PotenzFunktion) dasAeuﬂereFunktionsteil).gibP() == 0)
				return dasAeuﬂereFunktionsteil.clone();
			if (((PotenzFunktion) dasAeuﬂereFunktionsteil).gibP() == 1) {
				dasInnereFunktionsteil.multipliziere(dasAeuﬂereFunktionsteil.gibA());
				return dasInnereFunktionsteil.clone();
			}
			if (dasInnereFunktionsteil == null)
				return new PotenzFunktion(dasAeuﬂereFunktionsteil.gibA(), 0);
			if (dasInnereFunktionsteil.gibA() == 0 && !(dasInnereFunktionsteil instanceof Verknuepfung))
				return null;
			if (((PotenzFunktion) dasAeuﬂereFunktionsteil).gibA() == 0)
				return null;
			if (dasInnereFunktionsteil instanceof NatuerlicheExponentialFunktion) {
				NatuerlicheExponentialFunktion dieNExponentialFunktion = (NatuerlicheExponentialFunktion) dasInnereFunktionsteil;
				ArrayList<Funktionsteil> dieFaktorFunktionsteile = new ArrayList<Funktionsteil>();
				for (int i = 0; i < (int) ((PotenzFunktion) dasAeuﬂereFunktionsteil).gibP(); i++) {
					double a = 1;
					if (i == 0)
						a = dasAeuﬂereFunktionsteil.gibA() * Math.pow(dasInnereFunktionsteil.gibA(),
								((PotenzFunktion) dasAeuﬂereFunktionsteil).gibP());

					dieFaktorFunktionsteile.add(new NatuerlicheExponentialFunktion(a));
				}
				return new FaktorVerknuepfung(dieFaktorFunktionsteile);
			}

		} else if (dasAeuﬂereFunktionsteil instanceof Sin) {

			if (dasInnereFunktionsteil == null)
				return null;
			if (dasInnereFunktionsteil.gibA() == 0 && !(dasInnereFunktionsteil instanceof Verknuepfung))
				return null;
			if (((Sin) dasAeuﬂereFunktionsteil).gibA() == 0 && !(dasInnereFunktionsteil instanceof Verknuepfung))

				return null;
		} else if (dasAeuﬂereFunktionsteil instanceof Cos) {

			if (dasInnereFunktionsteil == null)
				return new PotenzFunktion(dasAeuﬂereFunktionsteil.gibA(), 0);
			if (dasInnereFunktionsteil.gibA() == 0)
				return new PotenzFunktion(dasAeuﬂereFunktionsteil.gibA(), 0);

			if (((Cos) dasAeuﬂereFunktionsteil).gibA() == 0 && !(dasInnereFunktionsteil instanceof Verknuepfung))
				return null;

		} else if (dasAeuﬂereFunktionsteil instanceof NatuerlicheExponentialFunktion) {

			if (dasInnereFunktionsteil.gibA() == 0 && !(dasInnereFunktionsteil instanceof Verknuepfung))
				return new PotenzFunktion(dasAeuﬂereFunktionsteil.gibA(), 1);
			if (((NatuerlicheExponentialFunktion) dasAeuﬂereFunktionsteil).gibA() == 0)
				return null;

		} else if (dasAeuﬂereFunktionsteil instanceof FaktorVerknuepfung) {

			dasAeuﬂereFunktionsteil = vereinfacheFaktor(dasAeuﬂereFunktionsteil);

		} else if (dasAeuﬂereFunktionsteil instanceof AddVerknuepfung) {

			dasAeuﬂereFunktionsteil = vereinfache(((AddVerknuepfung) dasAeuﬂereFunktionsteil).gibFunktionsteile());
			dieAddVerknuepfungsTeile.addAll(((AddVerknuepfung) dasAeuﬂereFunktionsteil).gibFunktionsteile());

		}

		if (dasInnereFunktionsteil instanceof Verkettung) {

			dasInnereFunktionsteil = vereinfacheVerkettung(((Verkettung) dasInnereFunktionsteil).getF(),
					((Verkettung) dasInnereFunktionsteil).getU());

		} else if (dasInnereFunktionsteil instanceof PotenzFunktion) {

			PotenzFunktion dieInnerePotenzFunktion = (PotenzFunktion) dasInnereFunktionsteil;

			if (dieInnerePotenzFunktion.gibA() == 1 && dieInnerePotenzFunktion.gibP() == 1)
				return dasAeuﬂereFunktionsteil;

		} else if (dasInnereFunktionsteil instanceof FaktorVerknuepfung) {

			dasInnereFunktionsteil = vereinfacheFaktor(dasInnereFunktionsteil);

		} else if (dasInnereFunktionsteil instanceof AddVerknuepfung) {

			dasInnereFunktionsteil = vereinfache(((AddVerknuepfung) dasInnereFunktionsteil).gibFunktionsteile());
		}

		if (dasAeuﬂereFunktionsteil.getClass() == dasInnereFunktionsteil.getClass()) {

			if (dasAeuﬂereFunktionsteil instanceof PotenzFunktion) {

				PotenzFunktion f1 = (PotenzFunktion) dasAeuﬂereFunktionsteil;
				PotenzFunktion f2 = (PotenzFunktion) dasInnereFunktionsteil;
				return new PotenzFunktion(f1.gibA() * Math.pow(f2.gibA(), f1.gibP()), f1.gibP() * f2.gibP());
			}
		}

		if (dieAddVerknuepfungsTeile.size() > 0) {

			ArrayList<Funktionsteil> dieVerkettungen = new ArrayList<Funktionsteil>();
			for (Funktionsteil dasFunktionsteil : dieAddVerknuepfungsTeile) {
				dieVerkettungen.add(new Verkettung(dasFunktionsteil, dasInnereFunktionsteil));
			}
			return new AddVerknuepfung(dieVerkettungen);
		}

		return new Verkettung(dasAeuﬂereFunktionsteil, dasInnereFunktionsteil);
	}

	private static void vereinfacheFaktor(ArrayList<Funktionsteil> dieFaktorVerknuepfungen) {
		ArrayList<Funktionsteil> dieRueckgabeListe = new ArrayList<Funktionsteil>();
		for (Funktionsteil dasFunktionsteil : dieFaktorVerknuepfungen) {

			Funktionsteil dasVereinfachteFunktionsteil = vereinfacheFaktor(dasFunktionsteil.clone());
			if (dasVereinfachteFunktionsteil != null)
				dieRueckgabeListe.add(dasVereinfachteFunktionsteil);
		}
		dieFaktorVerknuepfungen.clear();
		dieFaktorVerknuepfungen.addAll(dieRueckgabeListe);

	}

	private static Funktionsteil vereinfacheFaktor(Funktionsteil dieFaktorVerknuepfung) {
		ArrayList<Funktionsteil> dieExponentialFunktionen = new ArrayList<Funktionsteil>();
		ArrayList<PotenzFunktion> diePotenzFunktionen = new ArrayList<PotenzFunktion>();
		ArrayList<Funktionsteil> dieSinFunktionen = new ArrayList<Funktionsteil>();
		ArrayList<Funktionsteil> dieCosFunktionen = new ArrayList<Funktionsteil>();
		ArrayList<Funktionsteil> dieNatuerlicheExponentialFunktionen = new ArrayList<Funktionsteil>();
		ArrayList<Funktionsteil> dieVerkettungFunktionen = new ArrayList<Funktionsteil>();
		ArrayList<Funktionsteil> dieFaktorVerknuepfungFunktionen = new ArrayList<Funktionsteil>();
		ArrayList<Funktionsteil> dieAddVerknuepfungFunktionen = new ArrayList<Funktionsteil>();
		ArrayList<Funktionsteil> dieAddVerknuepfungFunktionen2 = new ArrayList<Funktionsteil>();
		ArrayList<Funktionsteil> dieLnFunktionen = new ArrayList<Funktionsteil>();
		ArrayList<Funktionsteil> dieXExponentialFunktionen = new ArrayList<Funktionsteil>();
 
		for (Funktionsteil dasFunktionsteil : ((FaktorVerknuepfung) dieFaktorVerknuepfung).gibFunktionsteile()) {
			if (dasFunktionsteil instanceof ExponentialFunktion) {
				dieExponentialFunktionen.add((ExponentialFunktion) dasFunktionsteil);
			} else if (dasFunktionsteil instanceof PotenzFunktion) {
				diePotenzFunktionen.add((PotenzFunktion) dasFunktionsteil);
			} else if (dasFunktionsteil instanceof Sin) {
				dieSinFunktionen.add((Sin) dasFunktionsteil);
			} else if (dasFunktionsteil instanceof Cos) {
				dieCosFunktionen.add((Cos) dasFunktionsteil);
			} else if (dasFunktionsteil instanceof NatuerlicheExponentialFunktion) {
				dieNatuerlicheExponentialFunktionen.add((NatuerlicheExponentialFunktion) dasFunktionsteil);
			} else if (dasFunktionsteil instanceof Verkettung) {
				dieVerkettungFunktionen.add((Verkettung) dasFunktionsteil);
			} else if (dasFunktionsteil instanceof FaktorVerknuepfung) {
				dieFaktorVerknuepfungFunktionen.add((FaktorVerknuepfung) dasFunktionsteil);
			} else if (dasFunktionsteil instanceof AddVerknuepfung) {
				dieAddVerknuepfungFunktionen.add((AddVerknuepfung) dasFunktionsteil);
			} else if (dasFunktionsteil == null) {
				return null;
			}else if(dasFunktionsteil instanceof Ln) {
				dieLnFunktionen.add((Ln)dasFunktionsteil);
			}else if(dasFunktionsteil instanceof XExponentialFunktion) {
				dieXExponentialFunktionen.add(dasFunktionsteil);
			}

		}
		((FaktorVerknuepfung) dieFaktorVerknuepfung).gibFunktionsteile().clear();
		vereinfacheVerkettung(dieVerkettungFunktionen);
		vereinfacheAdd(dieAddVerknuepfungFunktionen, dieAddVerknuepfungFunktionen2);
		vereinfacheFaktor(dieFaktorVerknuepfungFunktionen);
		fVereinfachePotenz(diePotenzFunktionen);
		fVereinfacheExponential(dieExponentialFunktionen);
		fVereinfacheNexponential(dieNatuerlicheExponentialFunktionen);
		double a = 1;
		ArrayList<Funktionsteil> dieZuLoeschendenFunktionsteile = new ArrayList<Funktionsteil>();
		for (Funktionsteil diePotenzfunktion : diePotenzFunktionen) {
			if (((PotenzFunktion) diePotenzfunktion).gibP() == 0) {
				a *= diePotenzfunktion.gibA();
				dieZuLoeschendenFunktionsteile.add(diePotenzfunktion);
			}
		}
		diePotenzFunktionen.removeAll(dieZuLoeschendenFunktionsteile);
		fVereinfacheCos(dieCosFunktionen);
		fVereinfacheSin(dieSinFunktionen);
		fVereinfacheXexponential(dieXExponentialFunktionen);
		fVereinfacheLn(dieLnFunktionen);
		((FaktorVerknuepfung) dieFaktorVerknuepfung).gibFunktionsteile().addAll(dieVerkettungFunktionen);
		((FaktorVerknuepfung) dieFaktorVerknuepfung).gibFunktionsteile().addAll(dieFaktorVerknuepfungFunktionen);
		((FaktorVerknuepfung) dieFaktorVerknuepfung).gibFunktionsteile().addAll(diePotenzFunktionen);
		((FaktorVerknuepfung) dieFaktorVerknuepfung).gibFunktionsteile().addAll(dieCosFunktionen);
		((FaktorVerknuepfung) dieFaktorVerknuepfung).gibFunktionsteile().addAll(dieSinFunktionen);
		((FaktorVerknuepfung) dieFaktorVerknuepfung).gibFunktionsteile().addAll(dieLnFunktionen);
		((FaktorVerknuepfung) dieFaktorVerknuepfung).gibFunktionsteile().addAll(dieXExponentialFunktionen);
		if(dieAddVerknuepfungFunktionen2.size()>0)
		((FaktorVerknuepfung) dieFaktorVerknuepfung).gibFunktionsteile().add(new AddVerknuepfung(dieAddVerknuepfungFunktionen2));
		((FaktorVerknuepfung) dieFaktorVerknuepfung).gibFunktionsteile().addAll(dieExponentialFunktionen);
		((FaktorVerknuepfung) dieFaktorVerknuepfung).gibFunktionsteile().addAll(dieNatuerlicheExponentialFunktionen);
		

		((FaktorVerknuepfung) dieFaktorVerknuepfung).fasseVorfaktorenZusammen();
		((FaktorVerknuepfung) dieFaktorVerknuepfung).setzeA(dieFaktorVerknuepfung.gibA() * a);
		if (dieFaktorVerknuepfung.gibA() == 0)
			return null;

		if (((FaktorVerknuepfung) dieFaktorVerknuepfung).gibFunktionsteile().size() == 1) {
			return ((FaktorVerknuepfung) dieFaktorVerknuepfung).gibFunktionsteile().get(0);
		}
		return dieFaktorVerknuepfung;
	}

	private static void fVereinfacheNexponential(ArrayList<Funktionsteil> dieExponentialFunktionen) {
		if (dieExponentialFunktionen.size() > 0) {
			double neuesA = 1;
			double anzahlFunktionen = 0;
			for (Funktionsteil dieExponentialFunktion : dieExponentialFunktionen) {
				neuesA *= dieExponentialFunktion.gibA();
				anzahlFunktionen++;
			}
			dieExponentialFunktionen.clear();
			if (anzahlFunktionen > 1) {
				dieExponentialFunktionen.add(new Verkettung(new PotenzFunktion(neuesA, anzahlFunktionen),
						new NatuerlicheExponentialFunktion(1)));
			} else {
				dieExponentialFunktionen.add(new NatuerlicheExponentialFunktion(neuesA));
			}
		}

	}

	private static void fVereinfacheExponential(ArrayList<Funktionsteil> dieExponentialFunktionen) {
		if (dieExponentialFunktionen.size() > 0) {
			double neuesA = 1, neuesB = 1;
			for (Funktionsteil dasFunktionsteil : dieExponentialFunktionen) {
				ExponentialFunktion dieExponentialFunktion = (ExponentialFunktion) dasFunktionsteil;
				neuesA *= dieExponentialFunktion.gibA();
				neuesB *= dieExponentialFunktion.gibB();
			}
			dieExponentialFunktionen.clear();
			dieExponentialFunktionen.add(new ExponentialFunktion(neuesA, neuesB));
		}
	}

	private static void fVereinfachePotenz(ArrayList<PotenzFunktion> diePotenzFunktionen) {
		if (diePotenzFunktionen.size() != 0) {
			double neuesP = 0;
			double neuesA = 1;
			for (PotenzFunktion diePotenzfunktion : diePotenzFunktionen) {
				neuesA *= diePotenzfunktion.gibA();
				neuesP += diePotenzfunktion.gibP();
			}
			diePotenzFunktionen.clear();
			if (!(neuesA == 1 && neuesP == 0)) {
				diePotenzFunktionen.add(new PotenzFunktion(neuesA, neuesP));
			}
		}

	}

	private static void fVereinfacheCos(ArrayList<Funktionsteil> dieCosFunktionen) {
		if (dieCosFunktionen.size() != 0) {
			double p = 0;
			double neuesA = 1;
			for (Funktionsteil dieCosFunktion : dieCosFunktionen) {
				neuesA *= dieCosFunktion.gibA();
				p++;

			}
			dieCosFunktionen.clear();
			if (p > 1) {
				dieCosFunktionen.add(new Verkettung(new PotenzFunktion(neuesA, p), new Cos(1)));
			} else {
				dieCosFunktionen.add(new Cos(neuesA));
			}
		}
	}

	private static void fVereinfacheSin(ArrayList<Funktionsteil> dieSinFunktionen) {
		if (dieSinFunktionen.size() != 0) {
			double p = 0;
			double neuesA = 1;
			for (Funktionsteil dieSinFunktion : dieSinFunktionen) {
				neuesA *= dieSinFunktion.gibA();
				p++;

			}
			dieSinFunktionen.clear();
			if (p > 1) {
				dieSinFunktionen.add(new Verkettung(new PotenzFunktion(neuesA, p), new Sin(1)));

			} else {
				dieSinFunktionen.add(new Sin(neuesA));

			}
		}
	}
	private static void fVereinfacheXexponential(ArrayList<Funktionsteil> dieXExponentialFunktionen) {
		if (dieXExponentialFunktionen.size() != 0) {
			double p = 0;
			double neuesA = 1;
			for (Funktionsteil dieXExponentialFunktion : dieXExponentialFunktionen) {
				neuesA *= dieXExponentialFunktion.gibA();
				p++;

			}
			dieXExponentialFunktionen.clear();
			if (p > 1) {
				dieXExponentialFunktionen.add(new Verkettung(new PotenzFunktion(neuesA, p), new XExponentialFunktion(1)));
				
			} else {
				dieXExponentialFunktionen.add(new XExponentialFunktion(neuesA));

			}
		}
	}
	private static void fVereinfacheLn(ArrayList<Funktionsteil> dieLnFunktionen) {
		if (dieLnFunktionen.size() != 0) {
			double p = 0;
			double neuesA = 1;
			for (Funktionsteil dasLnFunktionsteil : dieLnFunktionen) {
				neuesA *= dasLnFunktionsteil.gibA();
				p++;

			}
			dieLnFunktionen.clear();
			if (p > 1) {
				dieLnFunktionen.add(new Verkettung(new PotenzFunktion(neuesA, p), new Ln(1)));
			} else {
				dieLnFunktionen.add(new Ln(neuesA));

			}
		}
	}

}
