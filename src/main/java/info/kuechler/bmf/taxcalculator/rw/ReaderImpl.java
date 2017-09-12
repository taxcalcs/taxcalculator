package info.kuechler.bmf.taxcalculator.rw;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import info.kuechler.bmf.taxcalculator.Accessor;
import info.kuechler.bmf.taxcalculator.Calculator;

/**
 * Class to read values in tax calculator classes.
 * 
 * <p>
 * Creates from the calculate method from {@link Writer}.
 * </p>
 * 
 * @see Writer#calculate()
 */
public class ReaderImpl<T extends Calculator<T>> implements Reader {

	private final Accessor<T> accessor;

	/**
	 * Constructor.
	 * 
	 * @param calculator
	 *            the calculate class.
	 */
	protected ReaderImpl(final T calculator) {
		this.accessor = calculator.getAccessor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <V> V get(final String key) throws ReadWriteException {
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
	public Map<String, ?> getAll(final Iterable<String> keys) throws ReadWriteException {
		try {
			// keys are specified as parameter, no need to use a case
			// insensitive result map
			final Map<String, ?> result = new HashMap<>();
			for (final String key : keys) {
				result.put(key, accessor.get(key));
			}
			return result;
		} catch (IllegalArgumentException e) {
			throw new ReadWriteException(e.getMessage(), e);
		}
	}
}
