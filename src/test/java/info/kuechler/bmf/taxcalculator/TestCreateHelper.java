package info.kuechler.bmf.taxcalculator;

import static info.kuechler.bmf.taxcalculator.ExamplesCsvTest.FORMAT;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * 
 * Helper to create test CSV. For manual usage. All methods annotated with
 * {@link Ignore}.
 */
public class TestCreateHelper {
	private static final NumberFormat NUM_FORMAT = DecimalFormat.getInstance(Locale.GERMANY);

	@Disabled("for conversion only")
	@Test
	public final void toTable() throws IOException, ParseException {
		final int size = 7;
		int no = 0;
		try (final InputStreamReader in = new InputStreamReader(
				getClass().getResourceAsStream("/info/kuechler/bmf/taxcalculator/toconvert.csv"),
				StandardCharsets.UTF_8);
				final CSVParser parser = new CSVParser(in, FORMAT);
				final CSVPrinter print = new CSVPrinter(System.out, FORMAT);) {
			for (final CSVRecord record : parser) {
				for (final String value : record) {
					print.print(convert(value));
					if (++no % size == 0) {
						print.println();
					}
				}
			}
		}
		Assertions.assertTrue(no > 0);
	}

	private Object convert(String value) throws ParseException {
		final String newValue = value.trim();
		if (isGermanInt(newValue)) {
			return new BigDecimal(NUM_FORMAT.parse(newValue).intValue());
		}
		return newValue;
	}

	private boolean isGermanInt(String value) {
		// simple test
		return Pattern.matches("[\\d.]+", value);
	}

	@Disabled("for conversion only")
	@Test
	public final void transpose() throws IOException {
		final List<String[]> data = new ArrayList<>();
		int size = Integer.MAX_VALUE;

		try (final InputStreamReader in = new InputStreamReader(
				getClass().getResourceAsStream("/info/kuechler/bmf/taxcalculator/2016/general.csv"),
				StandardCharsets.UTF_8); final CSVParser parser = new CSVParser(in, FORMAT);) {
			for (final CSVRecord record : parser) {
				size = Math.min(size, record.size());
				final String[] line = new String[record.size()];
				for (int i = 0; i < record.size(); i++) {
					line[i] = record.get(i);
				}
				data.add(line);
			}
		}

		try (final CSVPrinter print = new CSVPrinter(System.out, FORMAT);) {
			for (int i = 0; i < size; i++) {
				for (final String[] d : data) {
					print.print(d[i]);
				}
				print.println();
			}
		}
		Assertions.assertTrue(size > 0);
	}
}
