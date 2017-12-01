package info.kuechler.bmf.taxcalculator.rw;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

import info.kuechler.bmf.taxcalculator.Accessor;
import info.kuechler.bmf.taxcalculator.Calculator;

/**
 * Implementation for a ReadWriteFactory for the BMF tax calculator.
 * <p>
 * Example: <code><br>
 *     final Writer input = TaxCalculatorFactory.create(0, 2018);<br>
 *     <br>
 *     // set values<br>
 *     input.set("KVZ", new BigDecimal("0.90"));<br>
 *     // ...<br>
 *     <br>
 *     // calculate<br>
 *     final Reader output = input.calculate();<br>
 *     <br>
 *     // read<br>
 *     System.out.println("LSTLZZ " + output.get("LSTLZZ"));<br>
 * </code>
 */
@SuppressWarnings("deprecation")
public class TaxCalculatorFactory extends AbstractReadWriteFactory {

	/**
	 * Instance for static methods usage.
	 */
	private static final TaxCalculatorFactory SINGLETON = new TaxCalculatorFactory();

	/**
	 * Creates an {@link Writer}. The {@link Writer} contains a clean
	 * {@link Calculator} instance. This method set all values to her initial
	 * values (0). There is no need to call {@link Writer#setAllToZero()} again.
	 * 
	 * @param month
	 *            1..12 for the month, with 0 you can choose the key from the
	 *            end of year
	 * @param year
	 *            the year, have to be &gt;= 2006
	 * @return {@link Writer}
	 * @throws ReadWriteException
	 *             year or month are out of range or calculate class cannot
	 *             detect
	 * @since 2018.0.0
	 * @see Writer#setAllToZero()
	 */
	public static Writer createWithWriter(final int month, final int year) throws ReadWriteException {
		final TaxCalculatorFactory factory = SINGLETON;
		try {
			return factory.create(factory.getYearKey(month, year)).setAllToZero();
		} catch (IllegalArgumentException e) {
			throw new ReadWriteException("year or month are out of range", e);
		}
	}

	/**
	 * Get an {@link Accessor} by month and year. The {@link Accessor} contains
	 * a clean {@link Calculator} instance. This method set all values to her
	 * initial values (0). There is no need to call
	 * {@link Accessor#setAllToZero()} again.
	 * 
	 * @param month
	 *            1..12 for the month, with 0 you can choose the key from the
	 *            end of year
	 * @param year
	 *            the year, have to be &gt;= 2006
	 * @return the {@link Accessor}
	 * @throws ReadWriteException
	 *             year or month are out of range or calculate class cannot
	 *             detect
	 * 
	 * @since 2018.0.0
	 */
	public static Accessor<String, ?> createWithAccessor(final int month, final int year) throws ReadWriteException {
		final TaxCalculatorFactory factory = SINGLETON;
		try {
			final Accessor<String, ?> accessor = factory.createAccessor(factory.getYearKey(month, year));
			accessor.setAllToZero();
			return accessor;
		} catch (IllegalArgumentException e) {
			throw new ReadWriteException("year or month are out of range", e);
		}
	}

	/**
	 * Create an instance of the calculate class. This method set all values to
	 * her initial values (0). There is no need to call
	 * {@link Writer#setAllToZero()} again.
	 * 
	 * @param month
	 *            1..12 for the month, with 0 you can choose the key from the
	 *            end of year
	 * @param year
	 *            the year, have to be &gt;= 2006
	 * @return an instance of the class
	 * @throws ReadWriteException
	 *             year or month are out of range or calculate class cannot
	 *             detect
	 * @since 2018.0.0
	 */
	public static Calculator<?> createCalculator(final int month, final int year) throws ReadWriteException {
		final TaxCalculatorFactory factory = SINGLETON;
		try {
			final Calculator<?> calculator = factory.createCalculator(factory.getYearKey(month, year));
			calculator.getAccessor().setAllToZero();
			return calculator;
		} catch (IllegalArgumentException e) {
			throw new ReadWriteException("year or month are out of range", e);
		}
	}

	/**
	 * Get all output fields from a {@link Calculator} class. The type can be
	 * {@link BigDecimal}, {@code int.class} or {@code double.class}.
	 * 
	 * @param month
	 *            1..12 for the month, with 0 you can choose the key from the
	 *            end of year
	 * @param year
	 *            the year, have to be &gt;= 2006
	 * @return a {@link Map} of output names and type. Names are case
	 *         insensitive.
	 * @throws ReadWriteException
	 *             year or month are out of range or calculate class cannot
	 *             detect
	 * @since 2018.0.0
	 */
	public static Map<String, Class<?>> getOutputs(final int month, final int year) throws ReadWriteException {
		final TaxCalculatorFactory factory = SINGLETON;
		try {
			return factory.getOutputsWithType(factory.getYearKey(month, year));
		} catch (IllegalArgumentException e) {
			throw new ReadWriteException("year or month are out of range", e);
		}
	}

	/**
	 * Get all input fields from a {@link Calculator} class with the type. The
	 * type can be {@link BigDecimal}, {@code int.class} or
	 * {@code double.class}.
	 *
	 * @param month
	 *            1..12 for the month, with 0 you can choose the key from the
	 *            end of year
	 * @param year
	 *            the year, have to be &gt;= 2006
	 * @return {@link Map} with name and type. Names are case insensitive.
	 * @throws ReadWriteException
	 *             year or month are out of range or calculate class cannot
	 *             detect
	 * @since 2018.0.0
	 */
	public static Map<String, Class<?>> getInputs(final int month, final int year) throws ReadWriteException {
		final TaxCalculatorFactory factory = SINGLETON;
		try {
			return factory.getInputsWithType(factory.getYearKey(month, year));
		} catch (IllegalArgumentException e) {
			throw new ReadWriteException("year or month are out of range", e);
		}
	}

	/**
	 * Returns the yearKey to use with
	 * {@link TaxCalculatorFactory#create(String)} and the other methods with
	 * year key. Please do not fix the key in your application. The key may be
	 * changed in future versions.
	 * 
	 * @param month
	 *            1..12 for the month, with 0 you can choose the key from the
	 *            end of year
	 * @param year
	 *            the year, have to be &gt;= 2006
	 * @return the key
	 * @throws IllegalArgumentException
	 *             if year or month are out of range
	 */
	public String getYearKey(final int month, final int year) {
		if (year < 2006 || month > 12 || month < 0) {
			throw new IllegalArgumentException("Month have to be between 0 and 12, year >= 2006");
		}
		if (year == 2011) {
			if (month == 12 || month == 0) {
				return "2011December";
			}
			return "2011November";
		}
		if (year == 2015 && (month == 12 || month == 0)) {
			return "2015Dezember";
		}
		return Integer.toString(year);
	}

	/**
	 * Create a {@link Writer}. The key is used to create a clean
	 * {@link Calculator} instance.
	 * <p>
	 * Use preferably the method {@link #createWithWriter(int, int)}.
	 * </p>
	 * 
	 * @param yearKey
	 *            the key to reference the class. The method
	 *            {@link #getCalculatorClass(String)} is called with this key.
	 * @return a {@link Writer}
	 * @throws ReadWriteException
	 *             error during create a {@link Writer}
	 * @see Writer#setAllToZero()
	 */
	public Writer create(final String yearKey) throws ReadWriteException {
		return super.create(yearKey);
	}

	/**
	 * Get all input fields from a {@link Calculator} class. Returns a
	 * modifiable copy of the {@link Set}.
	 * <p>
	 * Use preferably the method {@link #getInputs(int, int)}.
	 * </p>
	 * 
	 * @param yearKey
	 *            the key to reference the class. The method
	 *            {@link #getCalculatorClass(String)} is called with this key.
	 * @return a {@link Set} of input names. Names are case insensitive.
	 * @throws ReadWriteException
	 *             class cannot detect
	 */
	public Set<String> getInputs(final String yearKey) throws ReadWriteException {
		return super.getInputs(yearKey);
	}

	/**
	 * Get all input fields from a {@link Calculator} class with the type. The
	 * type can be {@link BigDecimal}, {@code int.class} or
	 * {@code double.class}.
	 * 
	 * <p>
	 * Returns a modifiable copy of the {@link Map}.
	 * </p>
	 * <p>
	 * Use preferably the method {@link #getInputs(int, int)}.
	 * </p>
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
		return super.getInputsWithType(yearKey);
	}

	/**
	 * Get all output fields from a {@link Calculator} class. Returns a
	 * modifiable copy of the {@link Set}.
	 * <p>
	 * Use preferably the method {@link #getOutputs(int, int)}.
	 * </p>
	 * 
	 * @param yearKey
	 *            the key to reference the class. The method
	 *            {@link #getCalculatorClass(String)} is called with this key.
	 * @return a {@link Set} of output names. Names are case insensitive.
	 * @throws ReadWriteException
	 *             class cannot detect
	 */
	public Set<String> getOutputs(final String yearKey) throws ReadWriteException {
		return super.getOutputs(yearKey);
	}

	/**
	 * Get all output fields from a {@link Calculator} class. The type can be
	 * {@link BigDecimal}, {@code int.class} or {@code double.class}.
	 * 
	 * <p>
	 * Returns a modifiable copy of the {@link Map}.
	 * </p>
	 * <p>
	 * Use preferably the method {@link #getOutputs(int, int)}.
	 * </p>
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
		return super.getOutputsWithType(yearKey);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Creates a object from following class:
	 * {@code "info.kuechler.bmf.taxcalculator.Lohnsteuer" + yearKey + "Big"}
	 * </p>
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected <T extends Calculator<T>> Class<T> getCalculatorClass(final String yearKey)
			throws ClassNotFoundException {
		return (Class<T>) Class.forName("info.kuechler.bmf.taxcalculator.Lohnsteuer" + yearKey + "Big");
	}

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
	protected <T extends Calculator<T>> T createCalculator(final String yearKey) throws ReadWriteException {
		try {
			final Class<T> clazz = getCalculatorClass(yearKey);
			return (T) clazz.newInstance();
		} catch (ClassNotFoundException e) {
			throw new ReadWriteException("Class not found for key: " + yearKey, e);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new ReadWriteException("Cannot create instance for class referenced by key: " + yearKey, e);
		}
	}

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
	protected <T extends Calculator<T>> Accessor<String, T> createAccessor(final String yearKey)
			throws ReadWriteException {
		final T calculator = createCalculator(yearKey);
		return calculator.getAccessor();
	}
}
