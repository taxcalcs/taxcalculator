package info.kuechler.bmf.taxcalculator.rw;

import java.math.BigDecimal;
import java.util.Map;

import info.kuechler.bmf.taxcalculator.Calculator;

/**
 * Reader to read output values from a {@link Calculator} class.
 * <p>
 * Fix key type is {@link String}.
 * </p>
 */
public interface Reader {

	/**
	 * Reads a value. Can be {@link Integer}, {@link Double} or
	 * {@link BigDecimal}.
	 * 
	 * <p>implNote: case insensitive key</p>
	 * 
	 * @param key
	 *            the property name
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
	 * <p>implNote: case insensitive key</p>
	 * 
	 * @param key
	 *            the property name
	 * @return the value
	 * @throws ReadWriteException
	 *             Error while read the value.
	 * @since 2018.0.0
	 */
	BigDecimal getBigDecimal(String key) throws ReadWriteException;

	/**
	 * Reads an int.
	 * 
	 * <p>implNote: case insensitive key</p>
	 * 
	 * @param key
	 *            the property name
	 * @return the value
	 * @throws ReadWriteException
	 *             Error while read the value.
	 * @since 2018.0.0
	 */
	int getInt(String key) throws ReadWriteException;

	/**
	 * Reads a double.
	 * 
	 * <p>implNote: case insensitive key</p>
	 * 
	 * @param key
	 *            the property name
	 * @return the value
	 * @throws ReadWriteException
	 *             Error while read the value.
	 * @since 2018.0.0
	 */
	double getDouble(String key) throws ReadWriteException;

	/**
	 * Read all values and returns the results as a {@link Map}. Values can be
	 * {@link Integer}, {@link Double} or {@link BigDecimal}.
	 * 
	 * <p>implNote: case insensitive key</p>
	 * 
	 * @param keys
	 *            the property names
	 * @return a {@link Map} with values.
	 * @throws ReadWriteException
	 *             Error while read the values.
	 * @since 2018.0.0
	 */
	Map<String, Number> getAll(Iterable<String> keys) throws ReadWriteException;
}