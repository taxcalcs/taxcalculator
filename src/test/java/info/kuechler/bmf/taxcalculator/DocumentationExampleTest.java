package info.kuechler.bmf.taxcalculator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import info.kuechler.bmf.taxcalculator.rw.ReadWriteException;
import info.kuechler.bmf.taxcalculator.rw.Reader;
import info.kuechler.bmf.taxcalculator.rw.TaxCalculatorFactory;
import info.kuechler.bmf.taxcalculator.rw.Writer;

public class DocumentationExampleTest {

	/**
	 * Using {@link Writer} and {@link Reader}. You can use a fluent API to set
	 * values. This is the highest abstract variant. {@link Writer} and
	 * {@link Reader} use {@link Accessor} internally.
	 * 
	 * @throws ReadWriteException
	 *             an error
	 */
	@Test
	public final void useWriterAndReader() throws ReadWriteException {
		Writer writer = TaxCalculatorFactory.createWithWriter(0, 2019);
		// 1. monthly payment
		// 2. tax class
		// 3. income in cent
		writer.set("LZZ", 2).set("STKL", 1).set("RE4", new BigDecimal("223456"));
		// 4. a half child :)
		// 5. additional med insurance [percent]
		// 6. pensions fund: east germany
		final Map<String, Object> parameter = new HashMap<>();
		parameter.put("ZKF", 0.5);
		parameter.put("KVZ", 1.10);
		parameter.put("KRV", 1);
		writer.setAll(parameter); // with setAll the correct type is detected

		// calculate result and return a Reader to read values
		Reader reader = writer.calculate();

		BigDecimal lst = reader.get("LSTLZZ");
		BigDecimal soli = reader.get("SOLZLZZ");

		Assertions.assertEquals(23350, lst.longValue());
		Assertions.assertEquals(825, soli.longValue());

		System.out.println("Lohnsteuer: " + lst.divide(new BigDecimal("100")) + " EUR");
		System.out.println("Soli: " + soli.divide(new BigDecimal("100")) + " EUR");
	}

	/**
	 * Using {@link Accessor}. {@link Accessor} is a class to access
	 * {@link Calculator}.
	 * 
	 * @throws ReadWriteException
	 *             an error
	 */
	@Test
	public final void useAccessor() throws ReadWriteException {
		Accessor<String, ?> accessor = TaxCalculatorFactory.createWithAccessor(0, 2019);

		accessor.set("LZZ", 2);
		accessor.set("STKL", 1);
		accessor.set("RE4", 223456);
		accessor.set("ZKF", 0.5);
		accessor.set("KVZ", 1.10);
		accessor.set("KRV", 1);

		accessor.getCalculator().calculate();

		BigDecimal lst = accessor.get("LSTLZZ");
		BigDecimal soli = accessor.get("SOLZLZZ");

		Assertions.assertEquals(23350, lst.longValue());
		Assertions.assertEquals(825, soli.longValue());

		System.out.println("Lohnsteuer: " + lst.divide(new BigDecimal("100")) + " EUR");
		System.out.println("Soli: " + soli.divide(new BigDecimal("100")) + " EUR");
	}

	/**
	 * Using the {@link Calculator} class directly.
	 */
	@Test
	public final void useGeneratedClasses() {
		Lohnsteuer2019Big tax = new Lohnsteuer2019Big();

		tax.setLZZ(2); // monthly payment
		tax.setSTKL(1); // tax class
		tax.setRE4(new BigDecimal("223456")); // income in cent
		tax.setLZZFREIB(BigDecimal.ZERO); // Freibeträge
		tax.setPVS(0); // not in saxony
		tax.setPVZ(0); // Additional care insurance for employee: birth > 1940,
						// older than 23, no kids
		tax.setR(0); // no church
		tax.setZKF(new BigDecimal("0.5")); // a half child :)
		tax.setKVZ(new BigDecimal("1.10")); // additional med insurance
											// [percent]
		tax.setKRV(1); // pensions fund: east germany

		tax.setVBEZ(BigDecimal.ZERO);
		tax.setLZZHINZU(BigDecimal.ZERO);
		tax.setSONSTB(BigDecimal.ZERO);
		tax.setVKAPA(BigDecimal.ZERO);
		tax.setVMT(BigDecimal.ZERO);

		tax.calculate();

		Assertions.assertEquals(23350, tax.getLSTLZZ().longValue());
		Assertions.assertEquals(825, tax.getSOLZLZZ().longValue());

		System.out.println("Lohnsteuer: " + tax.getLSTLZZ().divide(new BigDecimal("100")) + " EUR");
		System.out.println("Soli: " + tax.getSOLZLZZ().divide(new BigDecimal("100")) + " EUR");
	}
}
