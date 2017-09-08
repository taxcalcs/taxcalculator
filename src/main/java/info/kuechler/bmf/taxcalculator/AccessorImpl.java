package info.kuechler.bmf.taxcalculator;

import java.math.BigDecimal;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;

/**
 * Implementation of {@link Accessor}. To handle case insensitive keys use
 * {@link #newMap()} for parameter {@link Map}s for constructor.
 * 
 * @since 2018.0.0
 *
 * @param <T>
 *            the {@link Calculator} implementation
 */
public class AccessorImpl<T extends Calculator> implements Accessor {
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
	 * Use {@link #newMap()} to create input maps.
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
	 * Creates a new {@link Map} for constructor input.
	 * 
	 * @return
	 */
	public static <V> Map<String, V> newMap() {
		return new TreeMap<String, V>(String.CASE_INSENSITIVE_ORDER);
	}

	/**
	 * Copies a {@link Map} into a new {@link Map}.
	 * 
	 * @param map
	 *            the map to copy
	 * @return the new map
	 */
	public static <V> Map<String, V> copyMap(final Map<String, V> map) {
		if (map instanceof SortedMap) {
			// fast: use same Comparator and linear adding time
			return new TreeMap<String, V>((SortedMap<String, V>) map);
		}
		final Map<String, V> newMap = newMap();
		map.putAll(map);
		return newMap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getInt(final String key) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}
		return getterIntMap.get(key).applyAsInt(calculator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getBigDecimal(final String key) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}
		return getterBigDecimalMap.get(key).apply(calculator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDouble(final String key) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}
		return getterDoubleMap.get(key).applyAsDouble(calculator);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setInt(final String key, final int value) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}
		setterIntMap.get(key).accept(calculator, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setBigDecimal(final String key, final BigDecimal value) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}
		setterBigDecimalMap.get(key).accept(calculator, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDouble(final String key, final double value) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}
		setterDoubleMap.get(key).accept(calculator, value);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <V> void set(final String key, final V value) {
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}
		final Class<?> type = inputs.get(key);
		if (type == null) {
			throw new IllegalArgumentException("Key unknown: " + key);
		} else if (type == BigDecimal.class) {
			setBigDecimal(key, (BigDecimal) value);
		} else if (type == int.class) {
			setInt(key, ((Integer) value).intValue());
		} else if (type == double.class) {
			setDouble(key, ((Double) value).doubleValue());
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
		if (key == null) {
			throw new IllegalArgumentException("key is null");
		}
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
		return outputTypes.get(key);
	}
}
