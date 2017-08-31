package info.kuechler.bmf.taxcalculator.rw;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import info.kuechler.bmf.taxcalculator.Accessor;
import info.kuechler.bmf.taxcalculator.Calculator;

/**
 * Abstract factory to creates a {@link Writer} to write values to a call.
 * <p>
 * An example can be found on implementation class {@link TaxCalculatorFactory}.
 * 
 * @deprecated will be removed in 2019 version, all methods are moved into the {@link TaxCalculatorFactory}.
 */
@Deprecated
public abstract class AbstractReadWriteFactory {

	/**
	 * This method have to be return the class object for the class referenced
	 * by your key. You send your (whatever) key and returns a class object.
	 * 
	 * @param yearKey
	 *            the key to reference the class
	 * @return the class object
	 * @throws ClassNotFoundException
	 *             class does not exists
	 */
	protected abstract Class<Calculator> getCalculatorClass(final String yearKey) throws ClassNotFoundException;

	/**
	 * Create an instance of the calculate class.
	 * 
	 * @param yearKey
	 *            the key to reference the class
	 * @return an instance of the class
	 * @throws ReadWriteException
	 *             class does not exists or other issues during creation
	 * @since 2018.0.0
	 */
	public abstract Calculator createCalculator(final String yearKey) throws ReadWriteException;
	
	/**
	 * Get an {@link Accessor} by key.
	 * 
	 * @param yearKey
	 *            the key to reference the class. The method
	 *            {@link #getCalculatorClass(String)} is called with this key.
	 * @return the {@link Accessor}
	 * @throws ReadWriteException
	 *             class cannot detect
	 * 
	 * @since 2018.0.0
	 */
	public abstract Accessor createAccessor(final String yearKey) throws ReadWriteException;
	
	/**
	 * Create a {@link Writer}. The key is used to create a instance and detect
	 * all methods (calculate, getter, setter).
	 * 
	 * @param yearKey
	 *            the key to reference the class. The method
	 *            {@link #getCalculatorClass(String)} is called with this key.
	 * @return a {@link Writer}
	 * @throws ReadWriteException
	 *             error during create a {@link Writer}
	 */
	public Writer create(final String yearKey) throws ReadWriteException {
		return new Writer(createCalculator(yearKey));
	}

	/**
	 * Get all input fields from a class.
	 * 
	 * @param yearKey
	 *            the key to reference the class. The method
	 *            {@link #getCalculatorClass(String)} is called with this key.
	 * @return a {@link Set} of input names. Names are case insensitive.
	 * @throws ReadWriteException
	 *             class cannot detect
	 */
	public Set<String> getInputs(final String yearKey) throws ReadWriteException {
		return getInputsWithType(yearKey).keySet();
	}

	/**
	 * Get all input fields from a class with the type. Should be
	 * {@link BigDecimal}, int or double.
	 * 
	 * @param yearKey
	 *            the key to reference the class. The method
	 *            {@link #getCalculatorClass(String)} is called with this key.
	 * @return {@link Map} with name and type. Names are case insensitive.
	 * @throws ReadWriteException
	 *             class cannot detect
	 * @since 2016.2.0
	 */
	public Map<String, Class<?>> getInputsWithType(final String yearKey) throws ReadWriteException {
		return createAccessor(yearKey).getInputsWithType();
	}

	/**
	 * Get all output fields from a class.
	 * 
	 * @param yearKey
	 *            the key to reference the class. The method
	 *            {@link #getCalculatorClass(String)} is called with this key.
	 * @return a {@link Set} of output names. Names are case insensitive.
	 * @throws ReadWriteException
	 *             class cannot detect
	 */
	public Set<String> getOutputs(final String yearKey) throws ReadWriteException {
		return getOutputsWithType(yearKey).keySet();
	}

	/**
	 * Get all output fields from a class. Should be {@link BigDecimal}, int or
	 * double.
	 * 
	 * @param yearKey
	 *            the key to reference the class. The method
	 *            {@link #getCalculatorClass(String)} is called with this key.
	 * @return a {@link Map} of output names and type. Names are case
	 *         insensitive.
	 * @throws ReadWriteException
	 *             class cannot detect
	 * @since 2016.2.0
	 */
	public Map<String, Class<?>> getOutputsWithType(final String yearKey) throws ReadWriteException {
		return createAccessor(yearKey).getOutputsWithType();
	}
}