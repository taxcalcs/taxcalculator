package info.kuechler.bmf.taxcalculator;

import java.net.URI;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test Class 2009.
 */
public class Year2009Test extends AbstractYearTest<Lohnsteuer2009Big> {

    private final static Logger LOG = LoggerFactory.getLogger(Year2009Test.class);

    @Test
    public final void test() throws Exception {
        runFolderTestCases(new URI("http://www.bmf-steuerrechner.de/interface/2009.jsp"), "/info/kuechler/bmf/taxcalculator/2008");
    }

    @Override
    Logger getLogger() {
        return LOG;
    }

    @Override
    protected void calculate(final Lohnsteuer2009Big calc) {
        // calculate
        calc.calculate();
    }

    @Override
    protected Lohnsteuer2009Big createCalculator() {
        return new Lohnsteuer2009Big();
    }
}
