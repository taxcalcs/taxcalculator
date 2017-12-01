package info.kuechler.bmf.taxcalculator.rw;

import java.math.BigDecimal;
import java.util.Map;

import info.kuechler.bmf.taxcalculator.Accessor;
import info.kuechler.bmf.taxcalculator.Calculator;

/**
 * Writer to set values in a {@link Calculator} class.
 * <p>
 * Fix key type is {@link String}.
 * </p>
 *
 */
public interface Writer {

	/**
	 * Initialize all values with zero.
	 * 
	 * @return the {@link Writer} object itself.
	 * @throws ReadWriteException
	 *             Error while setting the values.
	 */
	Writer setAllToZero() throws ReadWriteException;

	/**
	 * Set a value.
	 * <p>
	 * See {@link Accessor#set(Object, Object)} for value type hints.
	 * </p>
	 * 
	 * @param key
	 *            the property name, is case insensitive
	 * @param value
	 *            the value to set.
	 * @param <V>
	 *            the type to set
	 * @return the {@link Writer} object itself.
	 * @throws ReadWriteException
	 *             Error while set the values.
	 */
	<V> Writer set(String key, V value) throws ReadWriteException;

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
	Writer set(String key, BigDecimal value) throws ReadWriteException;

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
	Writer set(String key, int value) throws ReadWriteException;

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
	Writer set(String key, double value) throws ReadWriteException;

	/**
	 * Set all the values from the Map.
	 * 
	 * <p>
	 * See {@link Accessor#set(Object, Object)} for value type hints.
	 * </p>
	 * 
	 * @param values
	 *            the values to set.
	 * @return the current {@link Writer} object itself
	 * @throws ReadWriteException
	 *             Error while setting the values.
	 * @see #set(String, Object)
	 */
	Writer setAll(Map<String, ?> values) throws ReadWriteException;

	/**
	 * Call the calculation method from the {@link Calculator} class.
	 * 
	 * @return The {@link Reader} object to access the output values.
	 * @throws ReadWriteException
	 *             error during creates Reader
	 */
	Reader calculate() throws ReadWriteException;

}