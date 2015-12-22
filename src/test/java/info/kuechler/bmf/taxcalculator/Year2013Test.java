package info.kuechler.bmf.taxcalculator;

import java.net.URI;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test Class 2013.
 */
public class Year2013Test extends AbstractYearTest<Lohnsteuer2013Big> {

    private final static Logger LOG = LoggerFactory.getLogger(Year2013Test.class);

    @Test
    public final void test() throws Exception {
        runFolderTestCases(new URI("http://www.bmf-steuerrechner.de/interface/2013.jsp"), "/info/kuechler/bmf/taxcalculator/2010");
    }

    @Override
    Logger getLogger() {
        return LOG;
    }

    @Override
    protected void calculate(final Lohnsteuer2013Big calc) {
        // calculate
        calc.calculate();
    }

    @Override
    protected Lohnsteuer2013Big createCalculator() {
        return new Lohnsteuer2013Big();
    }
}
