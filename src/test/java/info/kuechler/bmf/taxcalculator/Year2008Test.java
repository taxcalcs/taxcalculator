package info.kuechler.bmf.taxcalculator;

import java.net.URI;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test Class 2008.
 */
public class Year2008Test extends AbstractYearTest<Lohnsteuer2008Big> {

    private final static Logger LOG = LoggerFactory.getLogger(Year2008Test.class);

    @Test
    public final void test() throws Exception {
        runFolderTestCases(new URI("http://www.bmf-steuerrechner.de/interface/2008.jsp"), "/info/kuechler/bmf/taxcalculator/2008");
    }

    @Override
    Logger getLogger() {
        return LOG;
    }

    @Override
    protected void calculate(final Lohnsteuer2008Big calc) {
        // calculate
        calc.calculate();
    }

    @Override
    protected Lohnsteuer2008Big createCalculator() {
        return new Lohnsteuer2008Big();
    }
}
