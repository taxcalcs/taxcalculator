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

    /**
     * Simple Test for general features.
     * @throws ReadWriteException
     */
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
            Assert.assertNotEquals("'CLASS' getter found.", "CLASS", in);
        }
    }
    
    /**
     * Test {@link TaxCalculatorFactory#getYearKey(int, int)}.
     */
    @Test
    public final void testGetYearKey() {
        final TaxCalculatorFactory factory = new TaxCalculatorFactory();
        Assert.assertEquals("2006", factory.getYearKey(0, 2006));
        Assert.assertEquals("2006", factory.getYearKey(5, 2006));
        Assert.assertEquals("2006", factory.getYearKey(12, 2006));
        
        Assert.assertEquals("2007", factory.getYearKey(0, 2007));
        Assert.assertEquals("2007", factory.getYearKey(5, 2007));
        Assert.assertEquals("2007", factory.getYearKey(12, 2007));
        
        Assert.assertEquals("2008", factory.getYearKey(0, 2008));
        Assert.assertEquals("2008", factory.getYearKey(5, 2008));
        Assert.assertEquals("2008", factory.getYearKey(12, 2008));
        
        Assert.assertEquals("2009", factory.getYearKey(0, 2009));
        Assert.assertEquals("2009", factory.getYearKey(5, 2009));
        Assert.assertEquals("2009", factory.getYearKey(12, 2009));
        
        Assert.assertEquals("2010", factory.getYearKey(0, 2010));
        Assert.assertEquals("2010", factory.getYearKey(5, 2010));
        Assert.assertEquals("2010", factory.getYearKey(12, 2010));
        
        Assert.assertEquals("2011December", factory.getYearKey(0, 2011));
        Assert.assertEquals("2011November", factory.getYearKey(5, 2011));
        Assert.assertEquals("2011December", factory.getYearKey(12, 2011));
        
        Assert.assertEquals("2012", factory.getYearKey(0, 2012));
        Assert.assertEquals("2012", factory.getYearKey(5, 2012));
        Assert.assertEquals("2012", factory.getYearKey(12, 2012));
        
        Assert.assertEquals("2013", factory.getYearKey(0, 2013));
        Assert.assertEquals("2013", factory.getYearKey(5, 2013));
        Assert.assertEquals("2013", factory.getYearKey(12, 2013));
        
        Assert.assertEquals("2014", factory.getYearKey(0, 2014));
        Assert.assertEquals("2014", factory.getYearKey(5, 2014));
        Assert.assertEquals("2014", factory.getYearKey(12, 2014));
        
        Assert.assertEquals("2015Dezember", factory.getYearKey(0, 2015));
        Assert.assertEquals("2015", factory.getYearKey(5, 2015));
        Assert.assertEquals("2015Dezember", factory.getYearKey(12, 2015));
        
        Assert.assertEquals("2016", factory.getYearKey(0, 2016));
        Assert.assertEquals("2016", factory.getYearKey(12, 2016));
    }
}
