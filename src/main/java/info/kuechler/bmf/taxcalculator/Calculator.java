package info.kuechler.bmf.taxcalculator;

/**
 * Interface for calculation classes.
 * 
 * @since 2018.0.0
 */
public interface Calculator<T extends Calculator<T>> {
    
    /**
     * Output type {@value #OUTPUT_TYPE_STANDARD}
     * 
     * @since 2022.0.0
     */
    String OUTPUT_TYPE_STANDARD = "STANDARD";
    
    /**
     * Output type {@value #OUTPUT_TYPE_DBA}
     * 
     * @since 2022.0.0
     */
    String OUTPUT_TYPE_DBA = "DBA";
    
	/**
	 * Calculate the tax.
	 * <ol>
	 * <li>Set all input values</li>
	 * <li>{@link #calculate()} the tax</li>
	 * <li>read the output values</li>
	 * </ol>
	 * 
	 * @since 2018.0.0
	 */
	void calculate();

	/**
	 * Gets an {@link Accessor} instance for simple use of getter and setter methods.
	 * 
	 * @return {@link Accessor}
	 * 
	 * @since 2018.0.0
	 */
	Accessor<String, T> getAccessor();
}
