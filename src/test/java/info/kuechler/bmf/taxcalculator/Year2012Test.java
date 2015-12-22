package info.kuechler.bmf.taxcalculator;

import java.net.URI;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test Class 2012.
 */
public class Year2012Test extends AbstractYearTest<Lohnsteuer2012Big> {

    private final static Logger LOG = LoggerFactory.getLogger(Year2012Test.class);

    @Test
    public final void test() throws Exception {
        runFolderTestCases(new URI("http://www.bmf-steuerrechner.de/interface/2012.jsp"), "/info/kuechler/bmf/taxcalculator/2010");
    }

    @Override
    Logger getLogger() {
        return LOG;
    }

    @Override
    protected void calculate(final Lohnsteuer2012Big calc) {
        // calculate
        calc.calculate();
    }

    @Override
    protected Lohnsteuer2012Big createCalculator() {
        return new Lohnsteuer2012Big();
    }
}
