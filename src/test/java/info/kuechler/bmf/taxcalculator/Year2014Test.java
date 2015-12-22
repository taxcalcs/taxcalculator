package info.kuechler.bmf.taxcalculator;

import java.net.URI;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test Class 2014.
 */
public class Year2014Test extends AbstractYearTest<Lohnsteuer2014Big> {

    private final static Logger LOG = LoggerFactory.getLogger(Year2014Test.class);

    @Test
    public final void test() throws Exception {
        runFolderTestCases(new URI("http://www.bmf-steuerrechner.de/interface/2014.jsp"), "/info/kuechler/bmf/taxcalculator/2010");
    }

    @Override
    Logger getLogger() {
        return LOG;
    }

    @Override
    protected void calculate(final Lohnsteuer2014Big calc) {
        // calculate
        calc.calculate();
    }

    @Override
    protected Lohnsteuer2014Big createCalculator() {
        return new Lohnsteuer2014Big();
    }
}
