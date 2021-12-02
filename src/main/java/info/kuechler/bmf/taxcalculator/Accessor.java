package info.kuechler.bmf.taxcalculator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Accessor interface to simple handle of setter and getter from {@link Calculator} instances.
 * 
 * <ul>
 * <li>There are four methods to set values: {@link #set(Object, Object)},
 * {@link #setInt(Object, int)}, {@link #setDouble(Object, double)} and
 * {@link #setBigDecimal(Object, BigDecimal)}.</li>
 * <li>There are four methods to get values: {@link #get(Object)},
 * {@link #getInt(Object)}, {@link #getDouble(Object)} and
 * {@link #getBigDecimal(Object)}.</li>
 * <li>There are two methods to get the types: {@link #getInputsWithType()} and
 * {@link #getOutputsWithType()}. Calling the methods do not change the
 * instance.</li>
 * <li>There is a method to reset all values: {@link #setAllToZero()}.
 * With this method you can set all internal values in the initial state.</li>
 * <li>There is a method method to get the original {@link Calculator} instance:
 * {@link #getCalculator()}.</li>
 * <li>There is a method to get the special type for output values:
 * {@link #getOutputSpecialType(Object)}.</li>
 * </ul>
 * 
 * @param <K>
 *            the key type
 * @param <T>
 *            the {@link Calculator} type
 * 
 * @since 2018.0.0
 */
public interface Accessor<K, T extends Calculator<T>> {

    /**
     * Creates a new {@link Map} for constructor input. 
     * {@link Map} uses the keys in a case-insensitive manner.
     * Static way to using before create an instance for use as a constructor parameter.
     * 
     * @param <V>
     *            the value type
     * 
     * @return a new created {@link Map}
     */
    public static <V> Map<String, V> newMap() {
        return new TreeMap<String, V>(String.CASE_INSENSITIVE_ORDER);
    }
    
	/**
	 * Creates a new {@link Map} for {@link Accessor} data. I.e. you can use the
	 * same key {@link Comparator} like the original {@link Map}s.
	 * 
	 * @param <V>
	 *            the value type
	 * 
	 * @return a new created {@link Map}
	 * @deprecated use {@link #newMap()}, since 2022.0.0, forRemoval=true
	 */
    @Deprecated
    default <V> Map<String, V> createValueMap() {
        return Accessor.newMap();
    }

	/**
	 * Get an int value.
	 * 
	 * @param key
	 *            the key
	 * @return the value
	 * @throws IllegalArgumentException
	 *             if key is {@code null}, wrong type or unknown key
	 */
	int getInt(final K key);

	/**
	 * Get a {@link BigDecimal} value.
	 * 
	 * @param key
	 *            the key
	 * @return the value
	 * @throws IllegalArgumentException
	 *             if key is {@code null}, wrong type or unknown key
	 */
	BigDecimal getBigDecimal(final K key);

	/**
	 * Get a double value.
	 * 
	 * @param key
	 *            the key
	 * @return the value
	 * @throws IllegalArgumentException
	 *             if key is {@code null}, wrong type or unknown key
	 */
	double getDouble(final K key);

	/**
	 * Get a value.
	 * 
	 * @param <V>
	 *            return object class, can be {@link Integer}, {@link Double} or
	 *            {@link BigDecimal}.
	 * 
	 * @param key
	 *            the key
	 * @return the value, {@link Integer}, {@link Double} or {@link BigDecimal}
	 * @throws IllegalArgumentException
	 *             if key is {@code null} or unknown key
	 */
	<V> V get(final K key);

	/**
	 * Set an {@code int} value.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @throws IllegalArgumentException
	 *             if key is {@code null}, wrong type or unknown key
	 */
	void setInt(final K key, final int value);

	/**
	 * Set a {@link BigDecimal} value.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @throws IllegalArgumentException
	 *             if key is {@code null}, wrong type or unknown key
	 */
	void setBigDecimal(final K key, final BigDecimal value);

	/**
	 * Set a {@code double} value.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @throws IllegalArgumentException
	 *             if key is {@code null}, wrong type or unknown key
	 */
	void setDouble(final K key, final double value);

	/**
	 * Set a value. The value will be converted into {@link BigDecimal},
	 * {@code int} or {@code double}.
	 * 
	 * <p>
	 * <strong> Be sure that you are already using the correct type or choose a
	 * type so that the value can be converted without loss. </strong> For
	 * correct target type see {@link #getInputsWithType()}.
	 * </p>
	 * 
	 * <table border="1">
	 * <caption>Conversion</caption>
	 * <tr>
	 * <td><strong>Target Type</strong></td>
	 * <td><strong>Source Type</strong></td>
	 * <td><strong>Conversion</strong></td>
	 * </tr>
	 * <tr>
	 * <td>int</td>
	 * <td>Number</td>
	 * <td>{@link Number#intValue()}</td>
	 * </tr>
	 * <tr>
	 * <td>double</td>
	 * <td>Number</td>
	 * <td>{@link Number#doubleValue()}</td>
	 * </tr>
	 * <tr>
	 * <td rowspan="6">{@link BigDecimal}</td>
	 * <td>{@link BigDecimal}</td>
	 * <td>-</td>
	 * </tr>
	 * <tr>
	 * <td>{@link BigInteger}</td>
	 * <td>{@code BigInteger bi = (BigInteger) value;}<br>
	 * {@code new BigDecimal(bi)}</td>
	 * </tr>
	 * <tr>
	 * <td>{@link Integer}<br>
	 * {@link Byte}<br>
	 * {@link Short}<br>
	 * {@link AtomicInteger}</td>
	 * <td>{@code int i = ((Number) value).intValue();}<br>
	 * {@code new BigDecimal(i);}</td>
	 * </tr>
	 * <tr>
	 * <td>{@link Long}<br>
	 * {@link AtomicLong}</td>
	 * <td>{@code long l = ((Number) value).longValue();}<br>
	 * {@code new BigDecimal(l);}</td>
	 * </tr>
	 * <tr>
	 * <td>{@link Double}<br>
	 * {@link Float}</td>
	 * <td>{@code double d = ((Number) value).doubleValue();}<br>
	 * {@code new BigDecimal(d);}</td>
	 * </tr>
	 * <tr>
	 * <td>another {@link Number} object</td>
	 * <td>throws {@link IllegalArgumentException}</td>
	 * </tr>
	 * <tr>
	 * <td>...</td>
	 * <td>not a {@link Number}</td>
	 * <td>throws {@link IllegalArgumentException}</td>
	 * </tr>
	 * </table>
	 * 
	 * @param <V>
	 *            value object class
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 * @throws IllegalArgumentException
	 *             if key is {@code null} or key unknown
	 * @throws NullPointerException
	 *             if value is {@code null}
	 */
	<V> void set(final K key, final V value);

	/**
	 * Set all values to 0, 0.0 or {@link BigDecimal#ZERO}.
	 */
	void setAllToZero();

	/**
	 * Get all inputs with type {@link Class}. The type can be
	 * {@link BigDecimal}, {@code int.class} or {@code double.class}.
	 * 
	 * @implNote the map is copied before returning it, {@link Map} uses the keys in a case-insensitive manner
	 * @return {@link Map}
	 */
	Map<K, Class<?>> getInputsWithType();

	/**
	 * Get all outputs with type. The type can be {@link BigDecimal},
	 * {@code int.class} or {@code double.class}.
	 * 
	 * @implNote the map is copied before returning it, {@link Map} uses the keys in a case-insensitive manner
	 * @return {@link Map}
	 */
	Map<K, Class<?>> getOutputsWithType();

	/**
	 * Get the output type, like {@value Calculator#OUTPUT_TYPE_STANDARD} or {@value Calculator#OUTPUT_TYPE_DBA}.
	 * 
	 * @param key
	 *            key
	 * @return the key
	 * @throws IllegalArgumentException
	 *             if key is {@code null} or key unknown
	 */
	String getOutputSpecialType(final K key);

	/**
	 * Get the underlying {@link Calculator} instance.
	 * 
	 * @return the calculator
	 */
	T getCalculator();
}
