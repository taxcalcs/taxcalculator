package info.kuechler.bmf.taxcalculator;

/**
 * Interface for calculation classes.
 * 
 * @since 2018.0.0
 */
public interface Calculator {
	/**
	 * Calculate the tax.
	 */
	void calculate();

	/**
	 * Gets an {@link Accessor} for simple use of getter and setter.
	 * 
	 * @return {@link Accessor}
	 */
	Accessor getAccessor();
}
