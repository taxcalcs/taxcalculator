package info.kuechler.bmf.taxcalculator.rw;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import info.kuechler.bmf.taxcalculator.Accessor;
import info.kuechler.bmf.taxcalculator.Calculator;

/**
 * Implementation for a read-write-factory for the BMF tax calculator.
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
public class TaxCalculatorFactory {

	/**
	 * Instance for static methods usage.
	 */
	private static final TaxCalculatorFactory SINGLETON = new TaxCalculatorFactory();

	/**
	 * Creates a {@link Writer}. The {@link Writer} contains a clean
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
	 * <p>implNote: case insensitive keys in the returned {@link Map}</p>
	 * 
	 * @param month
	 *            1..12 for the month, with 0 you can choose the key from the
	 *            end of year
	 * @param year
	 *            the year, have to be &gt;= 2006
	 * @return a {@link Map} of output names and type
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
	 * <p>implNote: case insensitive keys in returned {@link Map}</p>
	 *
	 * @param month
	 *            1..12 for the month, with 0 you can choose the key from the
	 *            end of year
	 * @param year
	 *            the year, have to be &gt;= 2006
	 * @return {@link Map} with name and type.
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
		if (year == 2022 && (month == 0 || month >= 5)) {
		    return "2022Mai";
		}
		return Integer.toString(year);
	}

	/**
     * Create a {@link Writer}. The key is used to create a clean {@link Calculator} instance.
     * <p>
     * Use preferably the method {@link #createWithWriter(int, int)}.
     * </p>
     * 
     * @param <T>
     *            the Calculator type
     * @param yearKey
     *            the key to reference the class. The method {@link #getCalculatorClass(String)} is called with this
     *            key.
     * @return a {@link Writer}
     * @throws ReadWriteException
     *             error during create a {@link Writer}
     * @see Writer#setAllToZero()
     */
	public <T extends Calculator<T>> Writer create(final String yearKey) throws ReadWriteException {
	    Objects.requireNonNull(yearKey, "Argument must not be zero");
	    final T calculator = createCalculator(yearKey);
        return new WriterImpl<>(calculator);
    }

	/**
	 * Get all input fields from a {@link Calculator} class. Returns a
	 * modifiable copy of the {@link Set}.
	 * <p>
	 * Use preferably the method {@link #getInputs(int, int)}.
	 * </p>
	 * 
	 * <p>implNote case insensitive keys in returned {@link Set}</p>
	 * 
	 * @param yearKey
	 *            the key to reference the class. The method
	 *            {@link #getCalculatorClass(String)} is called with this key.
	 * @return a {@link Set} of input names
	 * @throws ReadWriteException
	 *             class cannot detect
	 */
	public Set<String> getInputs(final String yearKey) throws ReadWriteException {
	    Objects.requireNonNull(yearKey, "Argument must not be zero");
	    return getInputsWithType(yearKey).keySet();
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
	 * <p>implNote: the map is copied before returning it, {@link Map} uses the keys in a case-insensitive manner</p>
	 * 
	 * @param yearKey
	 *            the key to reference the class. The method
	 *            {@link #getCalculatorClass(String)} is called with this key.
	 * @return {@link Map} with name and type
	 * @throws ReadWriteException
	 *             class cannot detect
	 * @since 2016.2.0
	 */
	public Map<String, Class<?>> getInputsWithType(final String yearKey) throws ReadWriteException {
	    Objects.requireNonNull(yearKey, "Argument must not be zero");
	    return createAccessor(yearKey).getInputsWithType();
	}

	/**
	 * Get all output fields from a {@link Calculator} class. Returns a
	 * modifiable copy of the {@link Set}.
	 * <p>
	 * Use preferably the method {@link #getOutputs(int, int)}.
	 * </p>
	 * 
	 * <p>implNote: case insensitive keys in returned {@link Set}</p>
	 * 
	 * @param yearKey
	 *            the key to reference the class. The method
	 *            {@link #getCalculatorClass(String)} is called with this key.
	 * @return a {@link Set} of output names
	 * @throws ReadWriteException
	 *             class cannot detect
	 */
    public Set<String> getOutputs(final String yearKey) throws ReadWriteException {
        Objects.requireNonNull(yearKey, "Argument must not be zero");
        return getOutputsWithType(yearKey).keySet();
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
	 * <p>implNote the map is copied before returning it, {@link Map} uses the keys in a case-insensitive manner</p>
	 * 
	 * @param yearKey
	 *            the key to reference the class. The method
	 *            {@link #getCalculatorClass(String)} is called with this key.
	 * @return a {@link Map} of output names and type
	 * @throws ReadWriteException
	 *             class cannot detect
	 * @since 2016.2.0
	 */
    public Map<String, Class<?>> getOutputsWithType(final String yearKey) throws ReadWriteException {
        Objects.requireNonNull(yearKey, "Argument must not be zero");
        return createAccessor(yearKey).getOutputsWithType();
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
	protected <T extends Calculator<T>> Class<T> getCalculatorClass(final String yearKey)
			throws ClassNotFoundException {
	    Objects.requireNonNull(yearKey, "Argument must not be zero");
		return (Class<T>) Class.forName("info.kuechler.bmf.taxcalculator.Lohnsteuer" + yearKey + "Big");
	}

	/**
	 * Create an instance of the calculate class.
	 * 
	 * @param <T>
     *            the Calculator type
	 * @param yearKey
	 *            the key to reference the class
	 * @return an instance of the class
	 * @throws ReadWriteException
	 *             class does not exists or other issues during creation
	 * @since 2018.0.0
	 */
	protected <T extends Calculator<T>> T createCalculator(final String yearKey) throws ReadWriteException {
	    Objects.requireNonNull(yearKey, "Argument must not be zero");
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
	 * @param <T>
     *            the Calculator type
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
	    Objects.requireNonNull(yearKey, "Argument must not be zero");
	    final T calculator = createCalculator(yearKey);
		return calculator.getAccessor();
	}
}
