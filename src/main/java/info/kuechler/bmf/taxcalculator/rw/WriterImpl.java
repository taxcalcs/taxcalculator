package info.kuechler.bmf.taxcalculator.rw;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import info.kuechler.bmf.taxcalculator.Accessor;
import info.kuechler.bmf.taxcalculator.Calculator;

/**
 * Class to set input values in tax calculator classes.
 * 
 * <p>
 * To create this class use the {@link TaxCalculatorFactory} class.
 * </p>
 * 
 * @see TaxCalculatorFactory#createWithWriter(int, int)
 */
public class WriterImpl<T extends Calculator<T>> implements Writer {

	private final Accessor<String, T> accessor;

	/**
	 * Constructor.
	 * 
	 * @param calculator
	 *            the calculate class.
	 */
	protected WriterImpl(final T calculator) {
	    Objects.requireNonNull(calculator, "Argument must not be zero");
		this.accessor = calculator.getAccessor();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Writer setAllToZero() {
		accessor.setAllToZero();
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <V> Writer set(final String key, final V value) throws ReadWriteException {
	    Objects.requireNonNull(key, "Argument must not be zero");
	    Objects.requireNonNull(value, "Argument must not be zero");
		try {
			accessor.set(key, value);
		} catch (IllegalArgumentException e) {
			throw new ReadWriteException(e.getMessage(), e);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Writer set(final String key, final BigDecimal value) throws ReadWriteException {
	    Objects.requireNonNull(key, "Argument must not be zero");
	    Objects.requireNonNull(value, "Argument must not be zero");
		try {
			accessor.setBigDecimal(key, value);
		} catch (IllegalArgumentException e) {
			throw new ReadWriteException(e.getMessage(), e);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Writer set(final String key, final int value) throws ReadWriteException {
	    Objects.requireNonNull(key, "Argument must not be zero");
		try {
			accessor.setInt(key, value);
		} catch (IllegalArgumentException e) {
			throw new ReadWriteException(e.getMessage(), e);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Writer set(final String key, final double value) throws ReadWriteException {
	    Objects.requireNonNull(key, "Argument must not be zero");
		try {
			accessor.setDouble(key, value);
		} catch (IllegalArgumentException e) {
			throw new ReadWriteException(e.getMessage(), e);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Writer setAll(final Map<String, ?> values) throws ReadWriteException {
	    Objects.requireNonNull(values, "Argument must not be zero");
		try {
		    values.entrySet().forEach(e -> accessor.set(e.getKey(), e.getValue()));
		} catch (IllegalArgumentException e) {
			throw new ReadWriteException(e.getMessage(), e);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Reader calculate() {
		final T calculator = accessor.getCalculator();
		calculator.calculate();
		return new ReaderImpl<T>(calculator);
	}
}
