package info.kuechler.bmf.taxcalculator.rw;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test class which contains ignored tests for performance checks.
 */
public class PerformanceTestHelper {

	@Test
	@Ignore("for simple performance test only")
	public final void testPerformance() throws ReadWriteException, InterruptedException {
		final int loop1 = 10;
		final int loop2 = 1000000;
		final long startAll = System.nanoTime();
		for (int i = 0, l1 = loop1; i < l1; i++) {
			final long start = System.nanoTime();
			for (int j = 0, l2 = loop2; j < l2; j++) {
				final Writer input = TaxCalculatorFactory.create(0, 2015);

				final Map<String, Object> values = new HashMap<>();
				values.put("STKL", 1);
				values.put("LZZ", 1);
				values.put("RE4", new BigDecimal("2500000"));

				input.setAll(values).set("KVZ", new BigDecimal("0.90"));
				final Reader output = input.calculate();

				Assert.assertEquals(new BigDecimal("269000"), output.get("LSTLZZ"));
				Assert.assertEquals(new BigDecimal("14795"), output.get("SOlzLZZ"));
			}
			final long end = System.nanoTime();
			System.out.println("Run: " + (end - start) / 1000000 + " ms");
		}
		final long endAll = System.nanoTime();
		System.out.println("Run all: " + (endAll - startAll) / 1000000 + " ms");
	}

	/**
	 * Tests the performance (very simple)
	 * 
	 * @throws ReadWriteException
	 *             error
	 * @throws InterruptedException
	 */
	@Test
	@Ignore("for simple performance test only")
	public final void testPerformanceParallel() throws ReadWriteException, InterruptedException {
		final int loop1 = 10;
		final int loop2 = 1000000;
		final ExecutorService executor = Executors.newFixedThreadPool(loop1);
		final long startAll = System.nanoTime();
		for (int i = 0, l1 = loop1; i < l1; i++) {
			executor.submit(() -> {
				try {
					final long start = System.nanoTime();
					for (int j = 0, l2 = loop2; j < l2; j++) {
						final Writer input = TaxCalculatorFactory.create(0, 2015);

						final Map<String, Object> values = new HashMap<>();
						values.put("STKL", 1);
						values.put("LZZ", 1);
						values.put("RE4", new BigDecimal("2500000"));

						input.setAll(values).set("KVZ", new BigDecimal("0.90"));
						final Reader output = input.calculate();

						Assert.assertEquals(new BigDecimal("269000"), output.get("LSTLZZ"));
						Assert.assertEquals(new BigDecimal("14795"), output.get("SOlzLZZ"));
					}
					final long end = System.nanoTime();
					System.out.println("Run: " + (end - start) / 1000000 + " ms");
				} catch (ReadWriteException e) {
					throw new RuntimeException(e);
				}
			});
		}
		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.MINUTES);
		final long endAll = System.nanoTime();
		System.out.println("Run all: " + (endAll - startAll) / 1000000 + " ms");
	}
}
