package info.kuechler.bmf.taxcalculator.rw;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import info.kuechler.bmf.taxcalculator.Accessor;
import info.kuechler.bmf.taxcalculator.Calculator;

/**
 * Class to read values from the tax calculator classes.
 * 
 * <p>
 * Creates from the calculate method from {@link Writer}.
 * </p>
 * 
 * @see Writer#calculate()
 */
public class ReaderImpl<T extends Calculator<T>> implements Reader {

	private final Accessor<String, T> accessor;

	/**
	 * Constructor.
	 * 
	 * @param calculator
	 *            the calculate class.
	 */
	protected ReaderImpl(final T calculator) {
	    Objects.requireNonNull(calculator, "Argument must not be zero");
		this.accessor = calculator.getAccessor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <V> V get(final String key) throws ReadWriteException {
	    Objects.requireNonNull(key, "Argument must not be zero");
		try {
			return accessor.get(key);
		} catch (IllegalArgumentException e) {
			throw new ReadWriteException(e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BigDecimal getBigDecimal(final String key) throws ReadWriteException {
	    Objects.requireNonNull(key, "Argument must not be zero");
		try {
			return accessor.getBigDecimal(key);
		} catch (IllegalArgumentException e) {
			throw new ReadWriteException(e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getInt(final String key) throws ReadWriteException {
	    Objects.requireNonNull(key, "Argument must not be zero");
		try {
			return accessor.getInt(key);
		} catch (IllegalArgumentException e) {
			throw new ReadWriteException(e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getDouble(final String key) throws ReadWriteException {
	    Objects.requireNonNull(key, "Argument must not be zero");
		try {
			return accessor.getDouble(key);
		} catch (IllegalArgumentException e) {
			throw new ReadWriteException(e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Number> getAll(final Iterable<String> keys) throws ReadWriteException {
	    Objects.requireNonNull(keys, "Argument must not be zero");
	    try {
			final Map<String, Number> result = Accessor.newMap();
			keys.spliterator().forEachRemaining(key -> result.put(key, accessor.get(key)));
			return result;
		} catch (IllegalArgumentException e) {
			throw new ReadWriteException(e.getMessage(), e);
		}
	}
}
