package info.kuechler.bmf.taxcalculator;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import info.kuechler.bmf.taxcalculator.rw.ReadWriteException;
import info.kuechler.bmf.taxcalculator.rw.Reader;
import info.kuechler.bmf.taxcalculator.rw.TaxCalculatorFactory;
import info.kuechler.bmf.taxcalculator.rw.Writer;

/**
 * Test classes with examples CSV files. Test data from
 * <a href="https://www.bmf-steuerrechner.de/interface/pap.jsp">PAP</a>
 */
@RunWith(Parameterized.class)
public class ExamplesCsvTest {
	private final static Logger LOG = LoggerFactory.getLogger(ExamplesCsvTest.class);

	public static final CSVFormat FORMAT = CSVFormat.DEFAULT;

	private static final WriterTaxClassConfigurer CONF_SPECIAL = (writer, taxClass) -> {
		// Merker für die Vorsorgepauschale 1 = für den Arbeitnehmer wird die
		// gekürzte Vorsorgepauschale angewandt (§ 10c Abs. 3 EStG),
		// soweit nicht Arbeitnehmer der Fallgruppe 2.

		// unknown, not documented for test data, but "1" returns the correct
		// result
		writer.set("KRV", 1);
	};

	private static final WriterTaxClassConfigurer CONF_SPECIAL_PKV = (writer, taxClass) -> {
		// 1 = ausschließlich privat krankenversicherte Arbeitnehmer OHNE
		// Arbeitgeberzuschuss
		writer.set("PKV", 1);
		// Merker für die Vorsorgepauschale 2 = der Arbeitnehmer ist NICHT
		// in der gesetzlichen
		// Rentenversicherung versichert.
		writer.set("KRV", 2);
		// 1, wenn er der Arbeitnehmer den Zuschlag zur sozialen
		// Pflegeversicherung zu zahlen hat, sonst 0.
		writer.set("PKPV", BigDecimal.ZERO);
	};

	private static final WriterTaxClassConfigurer CONF_GENERAL = (writer, taxClass) -> {
		// pensions fund: west germany
		writer.set("KRV", 0);
	};

	private static final WriterTaxClassConfigurer CONF_GENERAL_PKV = (writer, taxClass) -> {
		// Krankenversicherung: 0 = gesetzlich krankenversicherte
		// Arbeitnehmer
		writer.set("PKV", 0);
		// pensions fund: west germany
		writer.set("KRV", 0);
		// 1, wenn er der Arbeitnehmer den Zuschlag zur sozialen
		// Pflegeversicherung zu zahlen hat, sonst 0.
		writer.set("PVZ", taxClass == 2 ? 0 : 1);
	};

	private static final WriterTaxClassConfigurer CONF_GENERAL_PKV_09 = (writer, taxClass) -> {
		// additional med insurance [percent]
		writer.set("KVZ", new BigDecimal("0.90"));

		CONF_GENERAL_PKV.configure(writer, taxClass);
	};

	private static final WriterTaxClassConfigurer CONF_GENERAL_PKV_11 = (writer, taxClass) -> {
		// additional med insurance [percent]
		writer.set("KVZ", new BigDecimal("1.10"));

		CONF_GENERAL_PKV.configure(writer, taxClass);
	};

	@Parameter(value = 0)
	public String testFilePath;

	@Parameter(value = 1)
	public int month;

	@Parameter(value = 2)
	public int year;

	@Parameter(value = 3)
	public WriterTaxClassConfigurer configurer;

	@Parameters(name = "{index}: {1}.{2}")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] { //
				{ "/info/kuechler/bmf/taxcalculator/2006/general.csv", 0, 2006, CONF_GENERAL },
				{ "/info/kuechler/bmf/taxcalculator/2006/special.csv", 0, 2006, CONF_SPECIAL },
				{ "/info/kuechler/bmf/taxcalculator/2007/general.csv", 0, 2007, CONF_GENERAL },
				{ "/info/kuechler/bmf/taxcalculator/2007/special.csv", 0, 2007, CONF_SPECIAL },
				{ "/info/kuechler/bmf/taxcalculator/2008/general.csv", 0, 2008, CONF_GENERAL },
				{ "/info/kuechler/bmf/taxcalculator/2008/special.csv", 0, 2008, CONF_SPECIAL },
				{ "/info/kuechler/bmf/taxcalculator/2009/general.csv", 0, 2009, CONF_GENERAL },
				{ "/info/kuechler/bmf/taxcalculator/2009/special.csv", 0, 2009, CONF_SPECIAL },
				{ "/info/kuechler/bmf/taxcalculator/2010/general.csv", 0, 2010, CONF_GENERAL_PKV },
				{ "/info/kuechler/bmf/taxcalculator/2010/special.csv", 0, 2010, CONF_SPECIAL_PKV },
				{ "/info/kuechler/bmf/taxcalculator/2011/general-nov.csv", 11, 2011, CONF_GENERAL_PKV },
				{ "/info/kuechler/bmf/taxcalculator/2011/special-nov.csv", 11, 2011, CONF_SPECIAL_PKV },
				{ "/info/kuechler/bmf/taxcalculator/2011/general-dec.csv", 0, 2011, CONF_GENERAL_PKV },
				{ "/info/kuechler/bmf/taxcalculator/2011/special-dec.csv", 0, 2011, CONF_SPECIAL_PKV },
				{ "/info/kuechler/bmf/taxcalculator/2012/general.csv", 0, 2012, CONF_GENERAL_PKV },
				{ "/info/kuechler/bmf/taxcalculator/2012/special.csv", 0, 2012, CONF_SPECIAL_PKV },
				{ "/info/kuechler/bmf/taxcalculator/2013/general.csv", 0, 2013, CONF_GENERAL_PKV },
				{ "/info/kuechler/bmf/taxcalculator/2013/special.csv", 0, 2013, CONF_SPECIAL_PKV },
				{ "/info/kuechler/bmf/taxcalculator/2014/general.csv", 0, 2014, CONF_GENERAL_PKV },
				{ "/info/kuechler/bmf/taxcalculator/2014/special.csv", 0, 2014, CONF_SPECIAL_PKV },
				{ "/info/kuechler/bmf/taxcalculator/2015/general-nov.csv", 11, 2015, CONF_GENERAL_PKV_09 },
				{ "/info/kuechler/bmf/taxcalculator/2015/special-nov.csv", 11, 2015, CONF_SPECIAL_PKV },
				{ "/info/kuechler/bmf/taxcalculator/2015/general-dec.csv", 0, 2015, CONF_GENERAL_PKV_09 },
				{ "/info/kuechler/bmf/taxcalculator/2015/special-dec.csv", 0, 2015, CONF_SPECIAL_PKV },
				{ "/info/kuechler/bmf/taxcalculator/2016/general.csv", 0, 2016, CONF_GENERAL_PKV_11 },
				{ "/info/kuechler/bmf/taxcalculator/2016/special.csv", 0, 2016, CONF_SPECIAL_PKV },
				{ "/info/kuechler/bmf/taxcalculator/2017/general.csv", 0, 2017, CONF_GENERAL_PKV_11 },
				{ "/info/kuechler/bmf/taxcalculator/2017/special.csv", 0, 2017, CONF_SPECIAL_PKV }
				//
		});
	}

	@Test
	public final void test() throws ReadWriteException, ParseException, IOException {
		try (final InputStreamReader in = new InputStreamReader(getClass().getResourceAsStream(testFilePath),
				StandardCharsets.UTF_8); final CSVParser parser = new CSVParser(in, FORMAT);) {
			int[] classes = null;
			String name = null;
			int tests = 0;
			for (final CSVRecord record : parser) {
				if (parser.getCurrentLineNumber() == 1L) {
					name = record.get(0);
					classes = readTaxClasses(record);
				} else {
					final BigDecimal income = new BigDecimal(record.get(0));
					for (int i = 1; i < record.size(); i++) {
						final int taxClass = classes[i - 1];
						final BigDecimal tax = new BigDecimal(record.get(i));

						final Writer writer = TaxCalculatorFactory.createWithWriter(month, year);
						// 1. yearly payment
						// 2. tax class
						// 3. income in cent
						writer.set("LZZ", 1).set("STKL", taxClass).set("RE4", income.multiply(new BigDecimal("100")));
						configurer.configure(writer, taxClass);

						final Reader reader = writer.calculate();
						final BigDecimal lst = ((BigDecimal) reader.get("LSTLZZ")).divide(new BigDecimal("100"));
						Assert.assertEquals(name + " " + taxClass + ": " + income + " -> " + tax + " vs. " + lst, tax,
								lst);
						tests++;
					}
				}
			}
			Assert.assertTrue("No Tests", tests > 0);
			LOG.debug("{}: {} tests", name, tests);
		}
	}

	private int[] readTaxClasses(final CSVRecord record) {
		int[] classes;
		classes = new int[record.size() - 1];
		for (int i = 1; i < record.size(); i++) {
			classes[i - 1] = convertTaxClass(record.get(i));
		}
		return classes;
	}

	private int convertTaxClass(String clazz) {
		switch (clazz) {
		case "I":
			return 1;
		case "II":
			return 2;
		case "III":
			return 3;
		case "IV":
			return 4;
		case "V":
			return 5;
		case "VI":
			return 6;
		default:
			throw new IllegalArgumentException(clazz);
		}
	}

	@FunctionalInterface
	interface WriterTaxClassConfigurer {
		void configure(final Writer writer, final int taxClass) throws ReadWriteException;
	}
}
