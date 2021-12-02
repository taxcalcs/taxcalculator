package info.kuechler.bmf.taxcalculator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;

/**
 * Implementation of {@link Accessor}. To use case insensitive keys use
 * {@link Accessor#newMap()} for parameter {@link Map}s for constructor.
 * 
 * @since 2018.0.0
 *
 * @param <T>
 *            the {@link Calculator} implementation
 */
public class AccessorImpl<T extends Calculator<T>> implements Accessor<String, T> {

	private final Map<String, ToIntFunction<T>> getterIntMap;
	private final Map<String, Function<T, BigDecimal>> getterBigDecimalMap;
	private final Map<String, ToDoubleFunction<T>> getterDoubleMap;

	private final Map<String, ObjIntConsumer<T>> setterIntMap;
	private final Map<String, BiConsumer<T, BigDecimal>> setterBigDecimalMap;
	private final Map<String, ObjDoubleConsumer<T>> setterDoubleMap;

	private final T calculator;
	private final Map<String, Class<?>> outputs;
	private final Map<String, Class<?>> inputs;
	private final Map<String, String> outputTypes;

	/**
	 * Constructor. Use {@link Accessor#newMap()} to create input maps.
	 * 
	 * @param getterIntMap
	 *            the map with getter functions to get {@code int} values
	 * @param getterBigDecimalMap
	 *            the map with getter functions to get {@link BigDecimal} values
	 * @param getterDoubleMap
	 *            the map with getter functions to get {@code double} values
	 * @param setterIntMap
	 *            the map with setter functions to set {@code int} values
	 * @param setterBigDecimalMap
	 *            the map with setter functions to set {@link BigDecimal} values
	 * @param setterDoubleMap
	 *            the map with setter functions to set {@code double} values
	 * @param calculator
	 *            the {@link Calculator} instance for reading and writing values
	 * @param inputs
	 *            the input keys with types
	 * @param outputs
	 *            the output keys with types
	 * @param outputTypes
	 *            the special types for output keys
	 */
	public AccessorImpl(
			//
			final Map<String, ToIntFunction<T>> getterIntMap,
			final Map<String, Function<T, BigDecimal>> getterBigDecimalMap,
			final Map<String, ToDoubleFunction<T>> getterDoubleMap,
			//
			final Map<String, ObjIntConsumer<T>> setterIntMap,
			final Map<String, BiConsumer<T, BigDecimal>> setterBigDecimalMap,
			final Map<String, ObjDoubleConsumer<T>> setterDoubleMap,
			//
			final T calculator,
			//
			final Map<String, Class<?>> inputs, final Map<String, Class<?>> outputs,
			//
			final Map<String, String> outputTypes) {

		this.getterIntMap = getterIntMap;
		this.getterBigDecimalMap = getterBigDecimalMap;
		this.getterDoubleMap = getterDoubleMap;

		this.setterIntMap = setterIntMap;
		this.setterBigDecimalMap = setterBigDecimalMap;
		this.setterDoubleMap = setterDoubleMap;

		this.calculator = calculator;
		this.inputs = inputs;
		this.outputs = outputs;

		this.outputTypes = outputTypes;
	}

	/**
	 * Copies a {@link Map} into a new {@link Map}.
	 * 
	 * @param <V>
	 *            the value type
	 * 
	 * @param map
	 *            the map to copy
	 * @return the new map
	 */
	public <V> Map<String, V> copyMap(final Map<String, V> map) {
	    Objects.requireNonNull(map, "Argument must not be zero");
	    if (map instanceof SortedMap) {
			// fast: use same Comparator and linear adding time
			return new TreeMap<String, V>((SortedMap<String, V>) map);
		}
		final Map<String, V> newMap = Accessor.newMap();
		map.putAll(map);
		return newMap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getInt(final String key) {
	    checkKey(key);
		return checkMapResult(getterIntMap.get(key)).applyAsInt(calculator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getBigDecimal(final String key) {
		checkKey(key);
		return checkMapResult(getterBigDecimalMap.get(key)).apply(calculator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDouble(final String key) {
		checkKey(key);
		return checkMapResult(getterDoubleMap.get(key)).applyAsDouble(calculator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInt(final String key, final int value) {
		checkKey(key);
		checkMapResult(setterIntMap.get(key)).accept(calculator, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBigDecimal(final String key, final BigDecimal value) {
		checkKey(key);
		checkMapResult(setterBigDecimalMap.get(key)).accept(calculator, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDouble(final String key, final double value) {
		checkKey(key);
		checkMapResult(setterDoubleMap.get(key)).accept(calculator, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <V> void set(final String key, final V value) {
		checkKey(key);
		final Class<?> type = inputs.get(key);
		if (type == null) {
			throw new IllegalArgumentException("Key unknown: " + key);
		} else if (!(value instanceof Number)) {
			throw new IllegalArgumentException("Value have to be a Number: " + value);
		} else if (type == BigDecimal.class) {
			setBigDecimal(key, toBigDecimal((Number) value));
		} else if (type == int.class) {
			setInt(key, ((Number) value).intValue());
		} else if (type == double.class) {
			setDouble(key, ((Number) value).doubleValue());
		} else {
			throw new IllegalArgumentException("Key type unknown: " + key);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <V> V get(final String key) {
		checkKey(key);
		final Class<?> type = outputs.get(key);
		if (type == null) {
			throw new IllegalArgumentException("Key unknown: " + key);
		}
		if (type == BigDecimal.class) {
			return (V) getBigDecimal(key);
		}
		if (type == int.class) {
			return (V) Integer.valueOf(getInt(key));
		}
		if (type == double.class) {
			return (V) Double.valueOf(getDouble(key));
		}
		throw new IllegalArgumentException("Key type unknown: " + key);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setAllToZero() {
		setterIntMap.values().forEach(setter -> setter.accept(calculator, 0));
		setterDoubleMap.values().forEach(setter -> setter.accept(calculator, .0));
		setterBigDecimalMap.values().forEach(setter -> setter.accept(calculator, BigDecimal.ZERO));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Class<?>> getInputsWithType() {
		return copyMap(inputs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Class<?>> getOutputsWithType() {
		return copyMap(outputs);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getOutputSpecialType(final String key) {
		return checkMapResult(outputTypes.get(key));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getCalculator() {
		return calculator;
	}

	/**
	 * Checks the result of calling {@link Map#get(Object)} of non {@code null}.
	 *
	 * @param <V>
	 *            type of the value
	 * @param parameter
	 *            the values
	 * @return the original value
	 * @throws IllegalArgumentException
	 *             if parameter is {@code null}
	 */
	protected <V> V checkMapResult(final V parameter) {
		if (parameter == null) {
			throw new IllegalArgumentException("Key unknown");
		}
		return parameter;
	}

	/**
	 * Checks the key value is not {@code null}.
	 * 
	 * @param key
	 *            the key value
	 * @throws IllegalArgumentException
	 *             if key is {@code null}.
	 */
	protected void checkKey(final String key) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}
	}

	/**
	 * Converts a {@link Number} into a {@link BigDecimal}.
	 * 
	 * @param value
	 *            the source value
	 * @return the result {@link BigDecimal}
	 */
	protected BigDecimal toBigDecimal(final Number value) {
		if (value instanceof BigDecimal) {
			return (BigDecimal) value;
		}
		if (value instanceof BigInteger) {
			return new BigDecimal((BigInteger) value);
		}
		if (value instanceof Integer || value instanceof Byte || value instanceof Short
				|| value instanceof AtomicInteger) {
			return new BigDecimal(((Number) value).intValue());
		}
		if (value instanceof Long || value instanceof AtomicLong) {
			return new BigDecimal(((Number) value).longValue());
		}
		if (value instanceof Double || value instanceof Float) {
			return new BigDecimal(((Number) value).doubleValue());
		}
		throw new IllegalArgumentException("Type unknown: " + value.getClass());
	}
}
