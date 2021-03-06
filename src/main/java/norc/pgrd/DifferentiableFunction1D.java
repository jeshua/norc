package norc.pgrd;

import java.util.Random;

/**
 * DifferentiableFunction1D represents a scalar-valued function for which we
 * can evaluate the function and gradient w.r.t. its parameters theta.
 *
 * Implementing this interface allows the use of gradient validation. (See ValidateGradient.java)
 * 
 * @author Jeshua Bratman
 */
public interface DifferentiableFunction1D<T> extends ParameterizedFunction{
		
	/**
	 * Evaluate this function's output and the gradient at a given input. 
	 * @param input - arbitrary input object 
	 */
	public OutputAndGradient evaluate(T input);
	public static class OutputAndGradient{
		public double   y;
		public double[] dy;//dy/dtheta or log(dy/dtheta) if gradient is in logspace
		public boolean logspace = false; //is gradient in logspace?
	}
	
	/**
	 * Provide a sample random input object to the function. Required for
	 * gradient checking code.
	 */
	public T generateRandomInput(Random rand);
}
