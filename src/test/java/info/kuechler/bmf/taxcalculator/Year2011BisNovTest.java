package info.kuechler.bmf.taxcalculator;

import java.net.URI;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test Class 2011 to November.
 */
public class Year2011BisNovTest extends AbstractYearTest<Lohnsteuer2011NovemberBig> {

    private final static Logger LOG = LoggerFactory.getLogger(Year2011BisNovTest.class);

    @Test
    public final void test() throws Exception {
        runFolderTestCases(new URI("http://www.bmf-steuerrechner.de/interface/2011bisNov.jsp"), "/info/kuechler/bmf/taxcalculator/2010");
    }

    @Override
    Logger getLogger() {
        return LOG;
    }

    @Override
    protected void calculate(final Lohnsteuer2011NovemberBig calc) {
        // calculate
        calc.calculate();
    }

    @Override
    protected Lohnsteuer2011NovemberBig createCalculator() {
        return new Lohnsteuer2011NovemberBig();
    }
}
