package info.kuechler.bmf.taxcalculator.rw;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.kuechler.bmf.taxcalculator.Accessor;

/**
 * Simple test for {@link TaxCalculatorFactory}.
 */
public class FactoryTest {

	@SuppressWarnings("unused")
	private final static Logger LOG = LoggerFactory.getLogger(FactoryTest.class);

	/**
	 * Simple Test for using a {@link Writer} from
	 * {@link TaxCalculatorFactory#createWithWriter(int, int)}.
	 * 
	 * @throws ReadWriteException
	 *             error
	 */
	@Test
	public final void testWriter() throws ReadWriteException {
		final Writer input = TaxCalculatorFactory.createWithWriter(0, 2015);

		final Map<String, Number> values = new HashMap<>();
		values.put("STKL", 1);
		values.put("LZZ", 1);
		values.put("RE4", new BigDecimal("2500000"));

		input.setAll(values).set("KVZ", new BigDecimal("0.90"));
		final Reader output = input.calculate();

		System.out.println("LSTLZZ " + output.get("LSTLZZ"));
		System.out.println("SOLZLZZ " + output.get("SOlzLZZ"));
		Assertions.assertEquals(new BigDecimal("269000"), output.get("LSTLZZ"));
		Assertions.assertEquals(new BigDecimal("14795"), output.get("SOlzLZZ"));
	}

	/**
	 * Simple Test for using a {@link Accessor} from
	 * {@link TaxCalculatorFactory#createWithAccessor(int, int)}.
	 * 
	 * @throws ReadWriteException
	 *             error
	 */
	@Test
	public final void testAccessor() throws ReadWriteException {
		final Accessor<String, ?> accessor = TaxCalculatorFactory.createWithAccessor(0, 2015);

		accessor.set("STKL", 1);
		accessor.set("LZZ", 1);
		accessor.setBigDecimal("RE4", new BigDecimal("2500000"));
		accessor.set("KVZ", new BigDecimal("0.90"));

		accessor.getCalculator().calculate();

		System.out.println("LSTLZZ " + accessor.get("LSTLZZ"));
		System.out.println("SOLZLZZ " + accessor.get("SOlzLZZ"));
		Assertions.assertEquals(new BigDecimal("269000"), accessor.get("LSTLZZ"));
		Assertions.assertEquals(new BigDecimal("14795"), accessor.get("SOlzLZZ"));
	}

	/**
	 * Test all values of {@link TaxCalculatorFactory#getYearKey(int, int)}.
	 */
	@Test
	public final void testGetYearKey() {
		final TaxCalculatorFactory factory = new TaxCalculatorFactory();
		Assertions.assertEquals("2006", factory.getYearKey(0, 2006));
		Assertions.assertEquals("2006", factory.getYearKey(5, 2006));
		Assertions.assertEquals("2006", factory.getYearKey(12, 2006));

		Assertions.assertEquals("2007", factory.getYearKey(0, 2007));
		Assertions.assertEquals("2007", factory.getYearKey(5, 2007));
		Assertions.assertEquals("2007", factory.getYearKey(12, 2007));

		Assertions.assertEquals("2008", factory.getYearKey(0, 2008));
		Assertions.assertEquals("2008", factory.getYearKey(5, 2008));
		Assertions.assertEquals("2008", factory.getYearKey(12, 2008));

		Assertions.assertEquals("2009", factory.getYearKey(0, 2009));
		Assertions.assertEquals("2009", factory.getYearKey(5, 2009));
		Assertions.assertEquals("2009", factory.getYearKey(12, 2009));

		Assertions.assertEquals("2010", factory.getYearKey(0, 2010));
		Assertions.assertEquals("2010", factory.getYearKey(5, 2010));
		Assertions.assertEquals("2010", factory.getYearKey(12, 2010));

		Assertions.assertEquals("2011December", factory.getYearKey(0, 2011));
		Assertions.assertEquals("2011November", factory.getYearKey(5, 2011));
		Assertions.assertEquals("2011December", factory.getYearKey(12, 2011));

		Assertions.assertEquals("2012", factory.getYearKey(0, 2012));
		Assertions.assertEquals("2012", factory.getYearKey(5, 2012));
		Assertions.assertEquals("2012", factory.getYearKey(12, 2012));

		Assertions.assertEquals("2013", factory.getYearKey(0, 2013));
		Assertions.assertEquals("2013", factory.getYearKey(5, 2013));
		Assertions.assertEquals("2013", factory.getYearKey(12, 2013));

		Assertions.assertEquals("2014", factory.getYearKey(0, 2014));
		Assertions.assertEquals("2014", factory.getYearKey(5, 2014));
		Assertions.assertEquals("2014", factory.getYearKey(12, 2014));

		Assertions.assertEquals("2015Dezember", factory.getYearKey(0, 2015));
		Assertions.assertEquals("2015", factory.getYearKey(5, 2015));
		Assertions.assertEquals("2015Dezember", factory.getYearKey(12, 2015));

		Assertions.assertEquals("2016", factory.getYearKey(0, 2016));
		Assertions.assertEquals("2016", factory.getYearKey(12, 2016));

		Assertions.assertEquals("2017", factory.getYearKey(0, 2017));
		Assertions.assertEquals("2017", factory.getYearKey(12, 2017));
		
		Assertions.assertEquals("2018", factory.getYearKey(0, 2018));
		Assertions.assertEquals("2018", factory.getYearKey(12, 2018));
		
		Assertions.assertEquals("2019", factory.getYearKey(0, 2019));
        Assertions.assertEquals("2019", factory.getYearKey(12, 2019));
        
        Assertions.assertEquals("2020", factory.getYearKey(0, 2020));
        Assertions.assertEquals("2020", factory.getYearKey(12, 2020));
	}

	/**
	 * Test {@link TaxCalculatorFactory#getInputs(int, int)}.
	 * 
	 * @throws ReadWriteException
	 *             error
	 */
	@Test
	public final void testGetInputsWithType() throws ReadWriteException {
		final Map<String, Class<?>> inputs = TaxCalculatorFactory.getInputs(1, 2016);
		for (final Map.Entry<String, Class<?>> entry : inputs.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue().getName());
			Assertions.assertTrue(entry.getValue() == BigDecimal.class || entry.getValue() == int.class
					|| entry.getValue() == double.class);
		}
	}

	/**
	 * Test {@link TaxCalculatorFactory#getOutputs(int, int)}.
	 * 
	 * @throws ReadWriteException
	 *             error
	 */
	@Test
	public final void testGetOutputsWithType() throws ReadWriteException {
		final Map<String, Class<?>> outputs = TaxCalculatorFactory.getOutputs(1, 2016);
		for (final Map.Entry<String, Class<?>> entry : outputs.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue().getName());
			Assertions.assertTrue(entry.getValue() == BigDecimal.class || entry.getValue() == int.class
					|| entry.getValue() == double.class);
		}
	}
}
