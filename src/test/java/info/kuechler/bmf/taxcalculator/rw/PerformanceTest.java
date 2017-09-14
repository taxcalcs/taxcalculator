package info.kuechler.bmf.taxcalculator.rw;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class which contains tests for performance checks.
 */
public class PerformanceTest {

	private static final int LOOP = 10;

	private static final int SUB_LOOP = 100000;

	@Test
	public final void testPerformance() throws ReadWriteException, InterruptedException {
		final long startAll = System.nanoTime();
		for (int i = 0; i < LOOP; i++) {
			final long start = System.nanoTime();
			for (int j = 0; j < SUB_LOOP; j++) {
				final Writer input = TaxCalculatorFactory.createWithWriter(0, 2015);

				final Map<String, Number> values = new HashMap<>();
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
	 *             ExecutorService stop timeout
	 */
	@Test
	public final void testPerformanceParallel() throws ReadWriteException, InterruptedException {
		final ExecutorService executor = Executors.newFixedThreadPool(LOOP);
		final long startAll = System.nanoTime();
		for (int i = 0; i < LOOP; i++) {
			executor.submit(() -> {
				try {
					final long start = System.nanoTime();
					for (int j = 0; j < SUB_LOOP; j++) {
						final Writer input = TaxCalculatorFactory.createWithWriter(0, 2015);

						final Map<String, Number> values = new HashMap<>();
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
					throw new IllegalStateException(e);
				}
			});
		}
		try {
			executor.shutdown();
			executor.awaitTermination(1, TimeUnit.MINUTES);
			final long endAll = System.nanoTime();
			System.out.println("Run all: " + (endAll - startAll) / 1000000 + " ms");
		} catch (InterruptedException e) {
			Assert.fail("Timeout");
		}
	}
}
