package info.kuechler.bmf.taxcalculator;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Accessor interface to simple handle of setter and getter.
 * 
 * @since 2018.0.0
 */
public interface Accessor {

	/**
	 * Get an int value.
	 * 
	 * @param key
	 *            the key, case insensitive
	 * @return the value
	 * @throws NullPointerException
	 *             if key is null or key unknown
	 */
	int getInt(final String key);

	/**
	 * Get an {@link BigDecimal} value.
	 * 
	 * @param key
	 *            the key, case insensitive
	 * @return the value
	 * @throws NullPointerException
	 *             if key is null or key unknown
	 */
	BigDecimal getBigDecimal(final String key);

	/**
	 * Get an double value.
	 * 
	 * @param key
	 *            the key, case insensitive
	 * @return the value
	 * @throws NullPointerException
	 *             if key is null or key unknown
	 */
	double getDouble(final String key);

	/**
	 * Get a value.
	 * 
	 * @param <V>
	 *            return object class, can be {@link Integer}, {@link Double} or
	 *            {@link BigDecimal}.
	 * 
	 * @param key
	 *            the key, case insensitive
	 * @return the value, {@link Integer}, {@link Double} or {@link BigDecimal}
	 * @throws NullPointerException
	 *             if key is null or key unknown
	 */
	<V> V get(final String key);

	/**
	 * Set an int value.
	 * 
	 * @param key
	 *            the key, case insensitive
	 * @param value
	 *            the value
	 * @throws NullPointerException
	 *             if key is null or key unknown
	 */
	void setInt(final String key, final int value);

	/**
	 * Set an {@link BigDecimal} value.
	 * 
	 * @param key
	 *            the key, case insensitive
	 * @param value
	 *            the value
	 * @throws NullPointerException
	 *             if key is null or key unknown
	 */
	void setBigDecimal(final String key, final BigDecimal value);

	/**
	 * Set an double value.
	 * 
	 * @param key
	 *            the key, case insensitive
	 * @param value
	 *            the value
	 * @throws NullPointerException
	 *             if key is null or key unknown
	 */
	void setDouble(final String key, final double value);

	/**
	 * Set a value with unknown type.
	 * 
	 * @param <V>
	 *            value object class, can be {@link Integer}, {@link Double} or
	 *            {@link BigDecimal}.
	 * 
	 * @param key
	 *            the key, case insensitive
	 * @param value
	 *            the value, {@link Integer}, {@link Double} or
	 *            {@link BigDecimal}
	 * @throws NullPointerException
	 *             if key is null or key unknown
	 */
	<V> void set(final String key, final V value);

	/**
	 * Set all values to 0, 0.0 or {@link BigDecimal#ZERO}.
	 */
	void setAllToZero();

	/**
	 * Get all inputs with type {@link Class}. Returns a copy of the original
	 * {@link Map}.
	 * 
	 * @return {@link Map}
	 */
	Map<String, Class<?>> getInputsWithType();

	/**
	 * Get all outputs with type {@link Class}. Returns a copy of the original
	 * {@link Map}.
	 * 
	 * @return {@link Map}
	 */
	Map<String, Class<?>> getOutputsWithType();

	/**
	 * Get the special type, like "STANDARD" or "DBA"
	 * 
	 * @param key
	 *            key, case insensitive
	 * @return the key
	 */
	String getOutputSpecialType(final String key);
}
