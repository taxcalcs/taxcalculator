package info.kuechler.bmf.taxcalculator.rw;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FactoryTest {
    private final static Logger LOG = LoggerFactory.getLogger(FactoryTest.class);

    @Test
    public final void test() throws ReadWriteException {
        final TaxCalculatorFactory factory = new TaxCalculatorFactory();
        final Writer input = factory.create("2015Dezember").setAllToZero();

        final Map<String, Object> values = new HashMap<>();
        values.put("STKL", 1);
        values.put("LZZ", 1);
        values.put("RE4", new BigDecimal("2500000"));

        input.setAll(values).set("KVZ", new BigDecimal("0.90"));
        final Reader output = input.calculate();

        System.out.println("LSTLZZ " + output.get("LSTLZZ"));
        System.out.println("SOLZLZZ " + output.get("SOlzLZZ"));
        Assert.assertEquals(new BigDecimal("269000"), output.get("LSTLZZ"));
        Assert.assertEquals(new BigDecimal("14795"), output.get("SOlzLZZ"));

        LOG.debug("\nInputs");
        for (final String in : factory.getInputs("2015Dezember")) {
            LOG.debug(in);
        }

        LOG.debug("\nOutputs");
        for (final String in : factory.getOutputs("2015Dezember")) {
            LOG.debug(in);
        }
    }
}
