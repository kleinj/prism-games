//==============================================================================
//	
//	Copyright (c) 2002-
//	Authors:
//	* Dave Parker <david.parker@comlab.ox.ac.uk> (University of Oxford)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of PRISM.
//	
//	PRISM is free software; you can redistribute it and/or modify
//	it under the terms of the GNU General Public License as published by
//	the Free Software Foundation; either version 2 of the License, or
//	(at your option) any later version.
//	
//	PRISM is distributed in the hope that it will be useful,
//	but WITHOUT ANY WARRANTY; without even the implied warranty of
//	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//	GNU General Public License for more details.
//	
//	You should have received a copy of the GNU General Public License
//	along with PRISM; if not, write to the Free Software Foundation,
//	Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//	
//==============================================================================

package explicit;

import java.util.BitSet;
import java.util.List;

import parser.ast.Expression;
import parser.ast.ExpressionFunc;
import parser.ast.ExpressionProb;
import parser.ast.ExpressionReward;
import parser.ast.ExpressionSS;
import parser.ast.ExpressionStrategy;
import parser.ast.ExpressionTemporal;
import parser.ast.ExpressionUnaryOp;
import parser.ast.RelOp;
import parser.ast.RewardStruct;
import prism.ModelType;
import prism.PrismComponent;
import prism.PrismException;
import prism.PrismSettings;
import explicit.rewards.ConstructRewards;
import explicit.rewards.MCRewards;
import explicit.rewards.MDPRewards;
import explicit.rewards.SMGRewards;
import explicit.rewards.STPGRewards;

/**
 * Super class for explicit-state probabilistic model checkers.
 */
public class ProbModelChecker extends NonProbModelChecker
{
	// Flags/settings
	// (NB: defaults do not necessarily coincide with PRISM)

	// Method used to solve linear equation systems
	protected LinEqMethod linEqMethod = LinEqMethod.GAUSS_SEIDEL;
	// Method used to solve MDPs
	protected MDPSolnMethod mdpSolnMethod = MDPSolnMethod.GAUSS_SEIDEL;
	// Iterative numerical method termination criteria
	protected TermCrit termCrit = TermCrit.RELATIVE;
	// Parameter for iterative numerical method termination criteria
	protected double termCritParam = 1e-8;
	// Max iterations for numerical solution
	protected int maxIters = 100000;
	// Use precomputation algorithms in model checking?
	protected boolean precomp = true;
	protected boolean prob0 = true;
	protected boolean prob1 = true;
	// Direction of convergence for value iteration (lfp/gfp)
	protected ValIterDir valIterDir = ValIterDir.BELOW;
	// Method used for numerical solution
	protected SolnMethod solnMethod = SolnMethod.VALUE_ITERATION;
	// Is non-convergence of an iterative method an error?
	protected boolean errorOnNonConverge = true;
	// Adversary export
	protected boolean exportAdv = false;
	protected String exportAdvFilename;

	protected boolean useDiscounting = false;
	protected double discountFactor = 1.0;

	// Enums for flags/settings

	// Method used for numerical solution
	public enum LinEqMethod {
		POWER, JACOBI, GAUSS_SEIDEL, BACKWARDS_GAUSS_SEIDEL, JOR, SOR, BACKWARDS_SOR;
		public String fullName()
		{
			switch (this) {
			case POWER:
				return "Power method";
			case JACOBI:
				return "Jacobi";
			case GAUSS_SEIDEL:
				return "Gauss-Seidel";
			case BACKWARDS_GAUSS_SEIDEL:
				return "Backwards Gauss-Seidel";
			case JOR:
				return "JOR";
			case SOR:
				return "SOR";
			case BACKWARDS_SOR:
				return "Backwards SOR";
			default:
				return this.toString();
			}
		}
	};

	// Method used for solving MDPs
	public enum MDPSolnMethod {
		VALUE_ITERATION, GAUSS_SEIDEL, POLICY_ITERATION, MODIFIED_POLICY_ITERATION, LINEAR_PROGRAMMING;
		public String fullName()
		{
			switch (this) {
			case VALUE_ITERATION:
				return "Value iteration";
			case GAUSS_SEIDEL:
				return "Gauss-Seidel";
			case POLICY_ITERATION:
				return "Policy iteration";
			case MODIFIED_POLICY_ITERATION:
				return "Modified policy iteration";
			case LINEAR_PROGRAMMING:
				return "Linear programming";
			default:
				return this.toString();
			}
		}
	};

	// Iterative numerical method termination criteria
	public enum TermCrit {
		ABSOLUTE, RELATIVE
	};

	// Direction of convergence for value iteration (lfp/gfp)
	public enum ValIterDir {
		BELOW, ABOVE
	};

	// Method used for numerical solution
	public enum SolnMethod {
		VALUE_ITERATION, GAUSS_SEIDEL, POLICY_ITERATION, MODIFIED_POLICY_ITERATION, LINEAR_PROGRAMMING
	};

	/**
	 * Create a new ProbModelChecker, inherit basic state from parent (unless null).
	 */
	public ProbModelChecker(PrismComponent parent) throws PrismException
	{
		super(parent);

		// If present, initialise settings from PrismSettings
		if (settings != null) {
			String s;
			// PRISM_LIN_EQ_METHOD
			s = settings.getString(PrismSettings.PRISM_LIN_EQ_METHOD);
			if (s.equals("Power")) {
				setLinEqMethod(LinEqMethod.POWER);
			} else if (s.equals("Jacobi")) {
				setLinEqMethod(LinEqMethod.JACOBI);
			} else if (s.equals("Gauss-Seidel")) {
				setLinEqMethod(LinEqMethod.GAUSS_SEIDEL);
			} else if (s.equals("Backwards Gauss-Seidel")) {
				setLinEqMethod(LinEqMethod.BACKWARDS_GAUSS_SEIDEL);
			} else if (s.equals("JOR")) {
				setLinEqMethod(LinEqMethod.JOR);
			} else if (s.equals("SOR")) {
				setLinEqMethod(LinEqMethod.SOR);
			} else if (s.equals("Backwards SOR")) {
				setLinEqMethod(LinEqMethod.BACKWARDS_SOR);
			} else {
				throw new PrismException("Explicit engine does not support linear equation solution method \"" + s + "\"");
			}
			// PRISM_MDP_SOLN_METHOD
			s = settings.getString(PrismSettings.PRISM_MDP_SOLN_METHOD);
			if (s.equals("Value iteration")) {
				setMDPSolnMethod(MDPSolnMethod.VALUE_ITERATION);
			} else if (s.equals("Gauss-Seidel")) {
				setMDPSolnMethod(MDPSolnMethod.GAUSS_SEIDEL);
			} else if (s.equals("Policy iteration")) {
				setMDPSolnMethod(MDPSolnMethod.POLICY_ITERATION);
			} else if (s.equals("Modified policy iteration")) {
				setMDPSolnMethod(MDPSolnMethod.MODIFIED_POLICY_ITERATION);
			} else if (s.equals("Linear programming")) {
				setMDPSolnMethod(MDPSolnMethod.LINEAR_PROGRAMMING);
			} else {
				throw new PrismException("Explicit engine does not support MDP solution method \"" + s + "\"");
			}
			// PRISM_TERM_CRIT
			s = settings.getString(PrismSettings.PRISM_TERM_CRIT);
			if (s.equals("Absolute")) {
				setTermCrit(TermCrit.ABSOLUTE);
			} else if (s.equals("Relative")) {
				setTermCrit(TermCrit.RELATIVE);
			} else {
				throw new PrismException("Unknown termination criterion \"" + s + "\"");
			}
			// PRISM_TERM_CRIT_PARAM
			setTermCritParam(settings.getDouble(PrismSettings.PRISM_TERM_CRIT_PARAM));
			// PRISM_MAX_ITERS
			setMaxIters(settings.getInteger(PrismSettings.PRISM_MAX_ITERS));
			// PRISM_PRECOMPUTATION
			setPrecomp(settings.getBoolean(PrismSettings.PRISM_PRECOMPUTATION));
			// PRISM_PROB0
			setProb0(settings.getBoolean(PrismSettings.PRISM_PROB0));
			// PRISM_PROB1
			setProb1(settings.getBoolean(PrismSettings.PRISM_PROB1));
			// PRISM_FAIRNESS
			if (settings.getBoolean(PrismSettings.PRISM_FAIRNESS)) {
				throw new PrismException("The explicit engine does not support model checking MDPs under fairness");
			}

			// PRISM_EXPORT_ADV
			s = settings.getString(PrismSettings.PRISM_EXPORT_ADV);
			if (!(s.equals("None")))
				setExportAdv(true);
			// PRISM_EXPORT_ADV_FILENAME
			setExportAdvFilename(settings.getString(PrismSettings.PRISM_EXPORT_ADV_FILENAME));

			// Strategy stuff
			setGenerateStrategy(settings.getBoolean(PrismSettings.PRISM_GENERATE_STRATEGY));
			setImplementStrategy(settings.getBoolean(PrismSettings.PRISM_IMPLEMENT_STRATEGY));
		}
	}

	// Settings methods

	/**
	 * Inherit settings (and the log) from another ProbModelChecker object.
	 * For model checker objects that inherit a PrismSettings object, this is superfluous
	 * since this has been done already.
	 */
	public void inheritSettings(ProbModelChecker other)
	{
		super.inheritSettings(other);
		setLinEqMethod(other.getLinEqMethod());
		setMDPSolnMethod(other.getMDPSolnMethod());
		setTermCrit(other.getTermCrit());
		setTermCritParam(other.getTermCritParam());
		setMaxIters(other.getMaxIters());
		setPrecomp(other.getPrecomp());
		setProb0(other.getProb0());
		setProb1(other.getProb1());
		setValIterDir(other.getValIterDir());
		setSolnMethod(other.getSolnMethod());
		setErrorOnNonConverge(other.geterrorOnNonConverge());
	}

	/**
	 * Print summary of current settings.
	 */
	public void printSettings()
	{
		super.printSettings();
		mainLog.print("linEqMethod = " + linEqMethod + " ");
		mainLog.print("mdpSolnMethod = " + mdpSolnMethod + " ");
		mainLog.print("termCrit = " + termCrit + " ");
		mainLog.print("termCritParam = " + termCritParam + " ");
		mainLog.print("maxIters = " + maxIters + " ");
		mainLog.print("precomp = " + precomp + " ");
		mainLog.print("prob0 = " + prob0 + " ");
		mainLog.print("prob1 = " + prob1 + " ");
		mainLog.print("valIterDir = " + valIterDir + " ");
		mainLog.print("solnMethod = " + solnMethod + " ");
		mainLog.print("errorOnNonConverge = " + errorOnNonConverge + " ");
	}

	// Set methods for flags/settings

	/**
	 * Set verbosity level, i.e. amount of output produced.
	 */
	public void setVerbosity(int verbosity)
	{
		this.verbosity = verbosity;
	}

	/**
	 * Set method used to solve linear equation systems.
	 */
	public void setLinEqMethod(LinEqMethod linEqMethod)
	{
		this.linEqMethod = linEqMethod;
	}

	/**
	 * Set method used to solve MDPs.
	 */
	public void setMDPSolnMethod(MDPSolnMethod mdpSolnMethod)
	{
		this.mdpSolnMethod = mdpSolnMethod;
	}

	/**
	 * Set termination criteria type for numerical iterative methods.
	 */
	public void setTermCrit(TermCrit termCrit)
	{
		this.termCrit = termCrit;
	}

	/**
	 * Set termination criteria parameter (epsilon) for numerical iterative
	 * methods.
	 */
	public void setTermCritParam(double termCritParam)
	{
		this.termCritParam = termCritParam;
	}

	/**
	 * Set maximum number of iterations for numerical iterative methods.
	 */
	public void setMaxIters(int maxIters)
	{
		this.maxIters = maxIters;
	}

	/**
	 * Set whether or not to use precomputation (Prob0, Prob1, etc.).
	 */
	public void setPrecomp(boolean precomp)
	{
		this.precomp = precomp;
	}

	/**
	 * Set whether or not to use Prob0 precomputation
	 */
	public void setProb0(boolean prob0)
	{
		this.prob0 = prob0;
	}

	/**
	 * Set whether or not to use Prob1 precomputation
	 */
	public void setProb1(boolean prob1)
	{
		this.prob1 = prob1;
	}

	/**
	 * Set direction of convergence for value iteration (lfp/gfp).
	 */
	public void setValIterDir(ValIterDir valIterDir)
	{
		this.valIterDir = valIterDir;
	}

	/**
	 * Set method used for numerical solution.
	 */
	public void setSolnMethod(SolnMethod solnMethod)
	{
		this.solnMethod = solnMethod;
	}

	/**
	 * Set whether non-convergence of an iterative method an error
	 */
	public void setErrorOnNonConverge(boolean errorOnNonConverge)
	{
		this.errorOnNonConverge = errorOnNonConverge;
	}

	public void setExportAdv(boolean exportAdv)
	{
		this.exportAdv = exportAdv;
	}

	public void setExportAdvFilename(String exportAdvFilename)
	{
		this.exportAdvFilename = exportAdvFilename;
	}

	// Get methods for flags/settings

	public int getVerbosity()
	{
		return verbosity;
	}

	public LinEqMethod getLinEqMethod()
	{
		return linEqMethod;
	}

	public MDPSolnMethod getMDPSolnMethod()
	{
		return mdpSolnMethod;
	}

	public TermCrit getTermCrit()
	{
		return termCrit;
	}

	public double getTermCritParam()
	{
		return termCritParam;
	}

	public int getMaxIters()
	{
		return maxIters;
	}

	public boolean getPrecomp()
	{
		return precomp;
	}

	public boolean getProb0()
	{
		return prob0;
	}

	public boolean getProb1()
	{
		return prob1;
	}

	public ValIterDir getValIterDir()
	{
		return valIterDir;
	}

	public SolnMethod getSolnMethod()
	{
		return solnMethod;
	}

	/**
	 * Is non-convergence of an iterative method an error?
	 */
	public boolean geterrorOnNonConverge()
	{
		return errorOnNonConverge;
	}

	// Model checking functions

	@Override
	public StateValues checkExpression(Model model, Expression expr) throws PrismException
	{
		StateValues res;

		// P operator
		if (expr instanceof ExpressionProb) {
			res = checkExpressionProb(model, (ExpressionProb) expr);
		}
		// R operator
		else if (expr instanceof ExpressionReward) {
			res = checkExpressionReward(model, (ExpressionReward) expr);
		}
		// S operator
		else if (expr instanceof ExpressionSS) {
			res = checkExpressionSteadyState(model, (ExpressionSS) expr);
		}
		// <<>> operator
		else if (expr instanceof ExpressionStrategy) {
			res = checkExpressionStrategy(model, (ExpressionStrategy) expr);
		}
		// Functions (for multi-objective)
		else if (expr instanceof ExpressionFunc) {
			res = checkExpressionFunc(model, (ExpressionFunc) expr);
		}
		// Otherwise, use the superclass
		else {
			res = super.checkExpression(model, expr);
		}

		return res;
	}

	private StateValues checkExpressionStrategy(Model model, ExpressionStrategy expr) throws PrismException
	{
		Expression pb; // Probability bound (expression)
		double p = 0; // Probability bound (actual value)
		RelOp relOp;
		boolean min, exact;
		StateValues probs = null;

		// Only support <<>> right now, not [[]]
		if (!expr.isThereExists())
			throw new PrismException("The " + expr.getOperatorString() + " operator is not yet supported");

		// Only support <<>> for SMGs right now
		if (!(this instanceof SMGModelChecker))
			throw new PrismException("The " + expr.getOperatorString() + " operator is only supported for SMGs currently");

		boolean prob; // P or R?
		ExpressionProb exprProb = null;
		ExpressionReward exprRew = null;
		if (expr.getExpression() instanceof ExpressionProb) {
			prob = true;
			exprProb = ((ExpressionProb) expr.getExpression());
		} else if (expr.getExpression() instanceof ExpressionReward) {
			prob = false;
			exprRew = ((ExpressionReward) expr.getExpression());
		} else {
			throw new PrismException("Currently, only P and R operators can appear within " + expr.getOperatorString());
		}

		// Get info from operator
		List<String> coalition = expr.getCoalition();
		relOp = prob ? exprProb.getRelOp() : exprRew.getRelOp();
		min = relOp.isUpperBound() || relOp.isMin();
		exact = relOp == RelOp.EQ;

		if (prob) {
			// Get info from prob operator
			pb = exprProb.getProb();
			if (pb != null) {
				p = pb.evaluateDouble(constantValues);
				if (p < 0 || p > 1)
					throw new PrismException("Invalid probability bound " + p + " in P operator");
			}

			if (!exact) {
				probs = ((SMGModelChecker) this).checkProbPathFormula(model, exprProb, coalition, min);

				// Print out probabilities
				if (getVerbosity() > 5) {
					mainLog.print("\nProbabilities (non-zero only) for all states:\n");
					probs.print(mainLog);
				}

				// For =? properties, just return values
				if (pb == null) {
					return probs;
				}
				// Otherwise, compare against bound to get set of satisfying
				// states
				else {
					BitSet sol = probs.getBitSetFromInterval(relOp, p);
					probs.clear();
					return StateValues.createFromBitSet(sol, model);
				}
			} else {

				if (pb != null) {
					p = pb.evaluateDouble(constantValues);
					if (p < 0 || p > 1)
						throw new PrismException("Invalid probability bound " + p + " in P operator");
				} else
					throw new PrismException("=? queries for exact probabilities are not supported, please provide a value");
				return ((SMGModelChecker) this).checkExactProbabilityFormula((SMG) model, exprProb, coalition, p);
			}
		} else {
			Object rs; // Reward struct index
			RewardStruct rewStruct = null; // Reward struct object
			Expression rb; // Reward bound (expression)
			double r = 0; // Reward bound (actual value)

			StateValues rews = null;
			SMGRewards smgRewards = null;

			int i;

			// Get info from reward operator
			rs = exprRew.getRewardStructIndex();
			rb = exprRew.getReward();
			if (exprRew.getDiscount() != null) {
				useDiscounting = true;
				discountFactor = ((Expression) exprRew.getDiscount()).evaluateDouble();
			}
			if (rb != null) {
				r = rb.evaluateDouble(constantValues);
				if (r < 0)
					throw new PrismException("Invalid reward bound " + r + " in R[] formula");
			}

			// Get reward info
			if (modulesFile == null)
				throw new PrismException("No model file to obtain reward structures");
			if (modulesFile.getNumRewardStructs() == 0)
				throw new PrismException("Model has no rewards specified");
			if (rs == null) {
				rewStruct = modulesFile.getRewardStruct(0);
			} else if (rs instanceof Expression) {
				i = ((Expression) rs).evaluateInt(constantValues);
				rs = new Integer(i); // for better error reporting below
				rewStruct = modulesFile.getRewardStruct(i - 1);
			} else if (rs instanceof String) {
				rewStruct = modulesFile.getRewardStructByName((String) rs);
			}
			if (rewStruct == null)
				throw new PrismException("Invalid reward structure index \"" + rs + "\"");

			// Build rewards
			mainLog.println("Building reward structure...");
			ConstructRewards constructRewards = new ConstructRewards(mainLog);
			smgRewards = constructRewards.buildSMGRewardStructure((SMG) model, rewStruct, constantValues);
			// Compute rewards
			mainLog.println("Computing rewards...");

			if (!exact) {
				rews = ((SMGModelChecker) this).checkRewardFormula(model, smgRewards, exprRew, coalition, min);

				// Print out probabilities
				if (getVerbosity() > 5) {
					mainLog.print("\nRewards (non-zero only) for all states:\n");
					rews.print(mainLog);
				}

				// For =? properties, just return values
				if (rb == null) {
					return rews;
				}
				// Otherwise, compare against bound to get set of satisfying
				// states
				else {
					BitSet sol = rews.getBitSetFromInterval(relOp, r);
					rews.clear();
					return StateValues.createFromBitSet(sol, model);
				}
			} else {

				if (rb != null) {
					p = rb.evaluateDouble(constantValues);
					if (p < 0)
						throw new PrismException("Invalid probability bound " + p + " in R operator");
				} else
					throw new PrismException("=? queries for exact rewards are not supported, please provide a value");
				return ((SMGModelChecker) this).checkExactRewardFormula((SMG) model, smgRewards, exprRew, coalition, p);
			}
		}
	}

	/**
	 * Model check a P operator expression and return the values for all states.
	 */
	protected StateValues checkExpressionProb(Model model, ExpressionProb expr) throws PrismException
	{
		Expression pb; // Probability bound (expression)
		double p = -1; // Probability bound (actual value, -1 means undefined)
		RelOp relOp; // Relational operator
		// For nondeterministic models, are we finding min (true) or max (false) probs
		MinMax minMax = MinMax.blank();
		ModelType modelType = model.getModelType();

		StateValues probs = null;

		// Get info from prob operator
		relOp = expr.getRelOp();
		pb = expr.getProb();
		if (pb != null) {
			p = pb.evaluateDouble(constantValues);
			if (p < 0 || p > 1)
				throw new PrismException("Invalid probability bound " + p + " in P operator");
		}

		// For nondeterministic models, determine whether min or max probabilities needed
		if (modelType.nondeterministic()) {
			if (relOp == RelOp.EQ && pb == null) {
				throw new PrismException("Can't use \"P=?\" for nondeterministic models; use \"Pmin=?\" or \"Pmax=?\"");
			}
			if (modelType == ModelType.MDP || modelType == ModelType.CTMDP) {
				minMax = (relOp.isLowerBound() || relOp.isMin()) ? MinMax.min() : MinMax.max();
			} else if (modelType == ModelType.SMG) {
				minMax = (relOp.isLowerBound() || relOp.isMin()) ? MinMax.minMin(true, false) : MinMax.minMin(false, true);
			}
			else if (modelType == ModelType.STPG) {
				if (relOp == RelOp.MINMIN) {
					minMax = MinMax.minMin(true, true);
				} else if (relOp == RelOp.MINMAX) {
					minMax = MinMax.minMin(true, false);
				} else if (relOp == RelOp.MAXMIN) {
					minMax = MinMax.minMin(false, true);
				} else if (relOp == RelOp.MAXMAX) {
					minMax = MinMax.minMin(false, false);
				} else {
					throw new PrismException("Use e.g. \"Pminmax=?\" for stochastic games");
				}
				// TODO: p is not always initialised?
				minMax.setBound(p);
			} else {
				throw new PrismException("Don't know how to model check " + expr.getTypeOfPOperator() + " properties for " + modelType + "s");
			}
		}

		// Compute probabilities
		probs = checkProbPathFormula(model, expr.getExpression(), minMax);
		
		// Print out probabilities
		if (getVerbosity() > 5) {
			mainLog.print("\nProbabilities (non-zero only) for all states:\n");
			probs.print(mainLog);
		}

		// For =? properties, just return values
		if (pb == null) {
			return probs;
		}
		// Otherwise, compare against bound to get set of satisfying states
		else {
			BitSet sol = probs.getBitSetFromInterval(relOp, p);
			probs.clear();
			return StateValues.createFromBitSet(sol, model);
		}
	}

	/**
	 * Compute probabilities for the contents of a P operator.
	 */
	protected StateValues checkProbPathFormula(Model model, Expression expr, MinMax minMax) throws PrismException
	{
		// Test whether this is a simple path formula (i.e. PCTL)
		// and then pass control to appropriate method. 
		if (expr.isSimplePathFormula()) {
			return checkProbPathFormulaSimple(model, expr, minMax);
		} else {
			return checkProbPathFormulaLTL(model, expr, false, minMax);
		}
	}

	/**
	 * Compute probabilities for a simple, non-LTL path operator.
	 */
	protected StateValues checkProbPathFormulaSimple(Model model, Expression expr, MinMax minMax) throws PrismException
	{
		StateValues probs = null;

		// Negation/parentheses
		if (expr instanceof ExpressionUnaryOp) {
			ExpressionUnaryOp exprUnary = (ExpressionUnaryOp) expr;
			// Parentheses
			if (exprUnary.getOperator() == ExpressionUnaryOp.PARENTH) {
				// Recurse
				probs = checkProbPathFormulaSimple(model, exprUnary.getOperand(), minMax);
			}
			// Negation
			else if (exprUnary.getOperator() == ExpressionUnaryOp.NOT) {
				// Compute, then subtract from 1 
				probs = checkProbPathFormulaSimple(model, exprUnary.getOperand(), minMax.negate());
				probs.timesConstant(-1.0);
				probs.plusConstant(1.0);
			}
		}
		// Temporal operators
		else if (expr instanceof ExpressionTemporal) {
			ExpressionTemporal exprTemp = (ExpressionTemporal) expr;
			// Next
			if (exprTemp.getOperator() == ExpressionTemporal.P_X) {
				probs = checkProbNext(model, exprTemp, minMax);
			}
			// Until
			else if (exprTemp.getOperator() == ExpressionTemporal.P_U) {
				if (exprTemp.hasBounds()) {
					probs = checkProbBoundedUntil(model, exprTemp, minMax);
				} else {
					probs = checkProbUntil(model, exprTemp, minMax);
				}
			}
			// Anything else - convert to until and recurse
			else {
				probs = checkProbPathFormulaSimple(model, exprTemp.convertToUntilForm(), minMax);
			}
		}

		if (probs == null)
			throw new PrismException("Unrecognised path operator in P operator");

		return probs;
	}

	/**
	 * Compute probabilities for a next operator.
	 */
	protected StateValues checkProbNext(Model model, ExpressionTemporal expr, MinMax minMax) throws PrismException
	{
		// Model check the operand
		BitSet target = checkExpression(model, expr.getOperand2()).getBitSet();

		// Compute/return the probabilities
		ModelCheckerResult res = null;
		switch (model.getModelType()) {
		case CTMC:
			res = ((CTMCModelChecker) this).computeNextProbs((CTMC) model, target);
			break;
		case DTMC:
			res = ((DTMCModelChecker) this).computeNextProbs((DTMC) model, target);
			break;
		case MDP:
			res = ((MDPModelChecker) this).computeNextProbs((MDP) model, target, minMax.isMin());
			break;
		case STPG:
		case SMG:
			res = ((STPGModelChecker) this).computeNextProbs((STPG) model, target, minMax.isMin1(), minMax.isMin2());
			break;
		default:
			throw new PrismException("Cannot model check " + expr + " for " + model.getModelType() + "s");
		}
		return StateValues.createFromDoubleArray(res.soln, model);
	}

	/**
	 * Compute probabilities for a bounded until operator.
	 */
	protected StateValues checkProbBoundedUntil(Model model, ExpressionTemporal expr, MinMax minMax) throws PrismException
	{
		// This method just handles discrete time
		// Continuous-time model checkers will override this method

		// Get info from bounded until
		int time = expr.getUpperBound().evaluateInt(constantValues);
		if (expr.upperBoundIsStrict())
			time--;
		if (time < 0) {
			String bound = expr.upperBoundIsStrict() ? "<" + (time + 1) : "<=" + time;
			throw new PrismException("Invalid bound " + bound + " in bounded until formula");
		}

		// Model check operands first
		BitSet remain = checkExpression(model, expr.getOperand1()).getBitSet();
		BitSet target = checkExpression(model, expr.getOperand2()).getBitSet();

		// Compute/return the probabilities
		// A trivial case: "U<=0" (prob is 1 in target states, 0 otherwise)
		if (time == 0) {
			return StateValues.createFromBitSetAsDoubles(target, model);
		}
		// Otherwise: numerical solution
		ModelCheckerResult res = null;
		switch (model.getModelType()) {
		case DTMC:
			res = ((DTMCModelChecker) this).computeBoundedUntilProbs((DTMC) model, remain, target, time);
			break;
		case MDP:
			res = ((MDPModelChecker) this).computeBoundedUntilProbs((MDP) model, remain, target, time, minMax.isMin());
			break;
		case STPG:
		case SMG:
			res = ((STPGModelChecker) this).computeBoundedUntilProbs((STPG) model, remain, target, time, minMax.isMin1(), minMax.isMin2());
			break;
		default:
			throw new PrismException("Cannot model check " + expr + " for " + model.getModelType() + "s");
		}
		return StateValues.createFromDoubleArray(res.soln, model);
	}

	/**
	 * Compute probabilities for an (unbounded) until operator.
	 */
	protected StateValues checkProbUntil(Model model, ExpressionTemporal expr, MinMax minMax) throws PrismException
	{
		// Model check operands first
		BitSet remain = checkExpression(model, expr.getOperand1()).getBitSet();
		BitSet target = checkExpression(model, expr.getOperand2()).getBitSet();

		// Compute/return the probabilities
		ModelCheckerResult res = null;
		switch (model.getModelType()) {
		case CTMC:
			res = ((CTMCModelChecker) this).computeUntilProbs((CTMC) model, remain, target);
			break;
		case DTMC:
			res = ((DTMCModelChecker) this).computeUntilProbs((DTMC) model, remain, target);
			break;
		case MDP:
			res = ((MDPModelChecker) this).computeUntilProbs((MDP) model, remain, target, minMax.isMin());
			result.setStrategy(res.strat);
			break;
		case STPG:
		case SMG:
			res = ((STPGModelChecker) this).computeUntilProbs((STPG) model, remain, target, minMax.isMin1(), minMax.isMin2(), minMax.getBound());
			break;
		default:
			throw new PrismException("Cannot model check " + expr + " for " + model.getModelType() + "s");
		}
		return StateValues.createFromDoubleArray(res.soln, model);
	}

	/**
	 * Compute probabilities for an LTL path formula
	 */
	protected StateValues checkProbPathFormulaLTL(Model model, Expression expr, boolean qual, MinMax minMax) throws PrismException
	{
		// To be overridden by subclasses
		throw new PrismException("Computation not implemented yet");
	}

	/**
	 * Model check an R operator expression and return the values for all states.
	 */
	protected StateValues checkExpressionReward(Model model, ExpressionReward expr) throws PrismException
	{
		Object rs; // Reward struct index
		RewardStruct rewStruct = null; // Reward struct object
		Expression rb; // Reward bound (expression)
		double r = 0; // Reward bound (actual value)
		RelOp relOp; // Relational operator
		boolean min1 = false;
		boolean min2 = false;
		ModelType modelType = model.getModelType();
		StateValues rews = null;
		MCRewards mcRewards = null;
		MDPRewards mdpRewards = null;
		STPGRewards stpgRewards = null;
		int i;

		// Get info from reward operator
		rs = expr.getRewardStructIndex();
		relOp = expr.getRelOp();
		rb = expr.getReward();
		if (rb != null) {
			r = rb.evaluateDouble(constantValues);
			if (r < 0)
				throw new PrismException("Invalid reward bound " + r + " in R[] formula");
		}

		// For nondeterministic models, determine whether min or max
		// probabilities needed
		if (modelType.nondeterministic()) {
			if (modelType == ModelType.MDP || modelType == ModelType.CTMDP) {
				min1 = relOp.isLowerBound() || relOp.isMin();
				if (relOp == RelOp.EQ && rb == null) {
					throw new PrismException("Can't use \"R=?\" for nondeterministic models; use \"Rmin=?\" or \"Rmax=?\"");
				}
			} else if (modelType == ModelType.STPG) {
				if (relOp == RelOp.MINMIN) {
					min1 = true;
					min2 = true;
				} else if (relOp == RelOp.MINMAX) {
					min1 = true;
					min2 = false;
				} else if (relOp == RelOp.MAXMIN) {
					min1 = false;
					min2 = true;
				} else if (relOp == RelOp.MAXMAX) {
					min1 = false;
					min2 = false;
				} else {
					throw new PrismException("Use e.g. \"Rminmax=?\" for stochastic games");
				}
			} else {
				throw new PrismException("Don't know how to model check " + expr.getTypeOfROperator() + " properties for " + modelType + "s");
			}
		}

		// Get reward info
		if (modulesFile == null)
			throw new PrismException("No model file to obtain reward structures");
		if (modulesFile.getNumRewardStructs() == 0)
			throw new PrismException("Model has no rewards specified");
		if (rs == null) {
			rewStruct = modulesFile.getRewardStruct(0);
		} else if (rs instanceof Expression) {
			i = ((Expression) rs).evaluateInt(constantValues);
			rs = new Integer(i); // for better error reporting below
			rewStruct = modulesFile.getRewardStruct(i - 1);
		} else if (rs instanceof String) {
			rewStruct = modulesFile.getRewardStructByName((String) rs);
		}
		if (rewStruct == null)
			throw new PrismException("Invalid reward structure index \"" + rs + "\"");

		// Build rewards
		ConstructRewards constructRewards = new ConstructRewards(mainLog);
		switch (modelType) {
		case CTMC:
		case DTMC:
			mcRewards = constructRewards.buildMCRewardStructure((DTMC) model, rewStruct, constantValues);
			break;
		case MDP:
			mdpRewards = constructRewards.buildMDPRewardStructure((MDP) model, rewStruct, constantValues);
			break;
		case STPG:
			stpgRewards = constructRewards.buildSTPGRewardStructure((STPG) model, rewStruct, constantValues);
			break;
		default:
			throw new PrismException("Cannot build rewards for " + modelType + "s");
		}

		// Compute rewards
		mainLog.println("Building reward structure...");
		switch (modelType) {
		case CTMC:
			rews = ((CTMCModelChecker) this).checkRewardFormula(model, mcRewards, expr.getExpression());
			break;
		case DTMC:
			rews = ((DTMCModelChecker) this).checkRewardFormula(model, mcRewards, expr.getExpression());
			break;
		case MDP:
			rews = ((MDPModelChecker) this).checkRewardFormula((NondetModel) model, mdpRewards, expr.getExpression(), min1);
			break;
		case STPG:
			rews = ((STPGModelChecker) this).checkRewardFormula(model, stpgRewards, expr.getExpression(), min1, min2);
			break;
		default:
			throw new PrismException("Cannot model check " + expr + " for " + modelType + "s");
		}

		// Print out probabilities
		if (getVerbosity() > 5) {
			mainLog.print("\nRewards (non-zero only) for all states:\n");
			rews.print(mainLog);
		}

		// For =? properties, just return values
		if (rb == null) {
			return rews;
		}
		// Otherwise, compare against bound to get set of satisfying states
		else {
			BitSet sol = rews.getBitSetFromInterval(relOp, r);
			rews.clear();
			return StateValues.createFromBitSet(sol, model);
		}
	}

	/**
	 * Model check an S operator expression and return the values for all states.
	 */
	protected StateValues checkExpressionSteadyState(Model model, ExpressionSS expr) throws PrismException
	{
		Expression pb; // Probability bound (expression)
		double p = 0; // Probability bound (actual value)
		RelOp relOp; // Relational operator
		ModelType modelType = model.getModelType();

		StateValues probs = null;

		// Get info from prob operator
		relOp = expr.getRelOp();
		pb = expr.getProb();
		if (pb != null) {
			p = pb.evaluateDouble(constantValues);
			if (p < 0 || p > 1)
				throw new PrismException("Invalid probability bound " + p + " in P operator");
		}

		// Compute probabilities
		switch (modelType) {
		case CTMC:
			//probs = ((CTMCModelChecker) this).checkSteadyStateFormula(model, expr.getExpression());
			//break;
			throw new PrismException("Explicit engine does not yet support the S operator for CTMCs");
		case DTMC:
			probs = ((DTMCModelChecker) this).checkSteadyStateFormula(model, expr.getExpression());
			break;
		default:
			throw new PrismException("Cannot model check " + expr + " for a " + modelType);
		}

		// Print out probabilities
		if (getVerbosity() > 5) {
			mainLog.print("\nProbabilities (non-zero only) for all states:\n");
			probs.print(mainLog);
		}

		// For =? properties, just return values
		if (pb == null) {
			return probs;
		}
		// Otherwise, compare against bound to get set of satisfying states
		else {
			BitSet sol = probs.getBitSetFromInterval(relOp, p);
			probs.clear();
			return StateValues.createFromBitSet(sol, model);
		}
	}

	/**
	 * Model check a function.
	 */
	protected StateValues checkExpressionFunc(Model model, ExpressionFunc expr) throws PrismException
	{
		if (expr.getNameCode() == ExpressionFunc.MULTI && model.getModelType() == ModelType.SMG) {
			throw new PrismException("Multi-objective model checking is not yet supported for SMGs");
		} else {
			return super.checkExpressionFunc(model, expr);
		}
	}

	/**
	 * Finds states of the model which only have self-loops
	 * 
	 * @param model
	 *            model
	 * @return the bitset indicating which states are terminal
	 */
	protected BitSet findTerminalStates(Model model)
	{
		int n = model.getStatesList().size();
		BitSet ret = new BitSet(n), bs = new BitSet(n);
		for (int i = 0; i < n; i++) {
			bs.clear();
			bs.set(i);
			if (model.allSuccessorsInSet(i, bs))
				ret.set(i);
		}
		return ret;
	}
}
