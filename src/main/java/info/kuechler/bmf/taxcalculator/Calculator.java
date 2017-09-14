package info.kuechler.bmf.taxcalculator;

/**
 * Interface for calculation classes.
 * 
 * @since 2018.0.0
 */
public interface Calculator<T extends Calculator<T>> {
	/**
	 * Calculate the tax.
	 * <p>
	 * set all input values -&gt; {@link #calculate()} the tax -&gt; read the
	 * output values
	 * </p>
	 * 
	 * @since 2018.0.0
	 */
	void calculate();

	/**
	 * Gets an {@link Accessor} instance for simple use of getter and setter.
	 * 
	 * @return {@link Accessor}
	 * 
	 * @since 2018.0.0
	 */
	Accessor<T> getAccessor();
}
