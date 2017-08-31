package info.kuechler.bmf.taxcalculator.rw;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import info.kuechler.bmf.taxcalculator.Accessor;
import info.kuechler.bmf.taxcalculator.Calculator;

/**
 * Class to read values in tax calculator classes.
 * 
 * <p>
 * Creates the calculate method from {@link Writer}.
 * </p>
 * 
 * @see Writer#calculate()
 */
public class Reader {
	private final Accessor accessor;

	/**
	 * Constructor.
	 * 
	 * @param calculator
	 *            the calculate class.
	 */
	protected Reader(final Calculator calculator) {
		this.accessor = calculator.getAccessor();
	}

	/**
	 * Reads a value.
	 * 
	 * @param key
	 *            the property name, is case insensitive
	 * @param <T>
	 *            Type of the result object
	 * @return the value
	 * @throws ReadWriteException
	 *             Error while read the value.
	 */
	public <T> T get(final String key) throws ReadWriteException {
		return accessor.get(key);
	}

	/**
	 * Reads a {@link BigDecimal}.
	 * 
	 * @param key
	 *            the property name, is case insensitive
	 * @return the value
	 * @throws ReadWriteException
	 *             Error while read the value.
	 * @since 2018.0.0
	 */
	public BigDecimal getBigDecimal(final String key) throws ReadWriteException {
		return accessor.getBigDecimal(key);
	}

	/**
	 * Reads a int.
	 * 
	 * @param key
	 *            the property name, is case insensitive
	 * @return the value
	 * @throws ReadWriteException
	 *             Error while read the value.
	 * @since 2018.0.0
	 */
	public int getInt(final String key) throws ReadWriteException {
		return accessor.getInt(key);
	}

	/**
	 * Reads a double.
	 * 
	 * @param key
	 *            the property name, is case insensitive
	 * @return the value
	 * @throws ReadWriteException
	 *             Error while read the value.
	 * @since 2018.0.0
	 */
	public double getDouble(final String key) throws ReadWriteException {
		return accessor.getDouble(key);
	}

	/**
	 * Read values and returns the results as a {@link Map}.
	 * 
	 * @param keys
	 *            the property names, is case insensitive
	 * @return a map with values
	 * @throws ReadWriteException
	 *             Error while read the values.
	 * @since 2018.0.0
	 */
	public Map<String, ?> getAll(final Iterable<String> keys) throws ReadWriteException {
		// keys are specified as parameter, no need to use a case insensitive map
		final Map<String, ?> result = new HashMap<>();
		for (final String key : keys) {
			result.put(key, accessor.get(key));
		}
		return result;
	}
}
