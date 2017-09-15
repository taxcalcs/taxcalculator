package info.kuechler.bmf.taxcalculator.rw;

import java.math.BigDecimal;
import java.util.Map;

import info.kuechler.bmf.taxcalculator.Calculator;

/**
 * Reader to read values from {@link Calculator} class.
 * <p>
 * Fix key type is {@link String}.
 * </p>
 */
public interface Reader {

	/**
	 * Reads a value. Can be {@link Integer}, {@link Double} or
	 * {@link BigDecimal}.
	 * 
	 * @param key
	 *            the property name, is case insensitive
	 * @param <V>
	 *            Type of the result object
	 * @return the value
	 * @throws ReadWriteException
	 *             Error while read the value.
	 * @see #getAll(Iterable)
	 */
	<V> V get(String key) throws ReadWriteException;

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
	BigDecimal getBigDecimal(String key) throws ReadWriteException;

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
	int getInt(String key) throws ReadWriteException;

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
	double getDouble(String key) throws ReadWriteException;

	/**
	 * Read values and returns the results as a {@link Map}. Values can be
	 * {@link Integer}, {@link Double} or {@link BigDecimal}.
	 * 
	 * @param keys
	 *            the property names, is case insensitive
	 * @return a {@link Map} with values.
	 * @throws ReadWriteException
	 *             Error while read the values.
	 * @since 2018.0.0
	 */
	Map<String, Number> getAll(Iterable<String> keys) throws ReadWriteException;
}