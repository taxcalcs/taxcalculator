package info.kuechler.bmf.taxcalculator.rw;

import java.math.BigDecimal;
import java.util.Map;

import info.kuechler.bmf.taxcalculator.Accessor;
import info.kuechler.bmf.taxcalculator.Calculator;

/**
 * Class to set input values in tax calculator classes.
 * 
 * <p>
 * To create this class use the {@link TaxCalculatorFactory} class.
 * </p>
 * 
 * @see TaxCalculatorFactory#create(String)
 */
public class Writer {

	private final Calculator calculator;
	private final Accessor accessor;

	/**
	 * Constructor.
	 * 
	 * @param calculator
	 *            the calculate class.
	 */
	protected Writer(final Calculator calculator) {
		this.calculator = calculator;
		this.accessor = calculator.getAccessor();
	}

	/**
	 * Initialize all values with zero.
	 * 
	 * @return the {@link Writer} object itself.
	 * @throws ReadWriteException
	 *             Error while setting the values.
	 */
	public Writer setAllToZero() throws ReadWriteException {
		accessor.setAllToZero();
		return this;
	}

	/**
	 * Set a value. The type of the value have to be correct.
	 * 
	 * @param key
	 *            the property name, is case insensitive
	 * @param value
	 *            the value to set.
	 * @param <T>
	 *            the result type
	 * @return the {@link Writer} object itself.
	 * @throws ReadWriteException
	 *             Error while set the values.
	 */
	public <T> Writer set(final String key, final T value) throws ReadWriteException {
		accessor.set(key, value);
		return this;
	}

	/**
	 * Set a value.
	 * 
	 * @param key
	 *            the property name, is case insensitive
	 * @param value
	 *            the value to set.
	 * @return the {@link Writer} object itself.
	 * @throws ReadWriteException
	 *             Error while set the values.
	 * @since 2018.0.0
	 */
	public Writer set(final String key, final BigDecimal value) throws ReadWriteException {
		accessor.setBigDecimal(key, value);
		return this;
	}

	/**
	 * Set a value.
	 * 
	 * @param key
	 *            the property name, is case insensitive
	 * @param value
	 *            the value to set.
	 * @return the {@link Writer} object itself.
	 * @throws ReadWriteException
	 *             Error while set the values.
	 * @since 2018.0.0
	 */
	public Writer set(final String key, final int value) throws ReadWriteException {
		accessor.setInt(key, value);
		return this;
	}

	/**
	 * Set a value.
	 * 
	 * @param key
	 *            the property name, is case insensitive
	 * @param value
	 *            the value to set.
	 * @return the {@link Writer} object itself.
	 * @throws ReadWriteException
	 *             Error while set the values.
	 * @since 2018.0.0
	 */
	public Writer set(final String key, final double value) throws ReadWriteException {
		accessor.setDouble(key, value);
		return this;
	}

	/**
	 * Set all the values from the Map.
	 * 
	 * @param values
	 *            the values to set.
	 * @return the current {@link Writer} object itself
	 * @throws ReadWriteException
	 *             Error while setting the values.
	 * @see #set(String, Object)
	 */
	public Writer setAll(final Map<String, ?> values) throws ReadWriteException {
		for (final Map.Entry<String, ?> e : values.entrySet()) {
			accessor.set(e.getKey(), e.getValue());
		}
		return this;
	}

	/**
	 * Call the calculation method.
	 * 
	 * @return The {@link Reader} object to access the output values.
	 */
	public Reader calculate() {
		calculator.calculate();
		return new Reader(calculator);
	}
}
