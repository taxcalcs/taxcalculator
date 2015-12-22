package info.kuechler.bmf.taxcalculator;

import java.net.URI;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test Class 2010.
 */
public class Year2010Test extends AbstractYearTest<Lohnsteuer2010Big> {

    private final static Logger LOG = LoggerFactory.getLogger(Year2010Test.class);

    @Test
    public final void test() throws Exception {
        runFolderTestCases(new URI("http://www.bmf-steuerrechner.de/interface/2010.jsp"), "/info/kuechler/bmf/taxcalculator/2010");
    }

    @Override
    Logger getLogger() {
        return LOG;
    }

    @Override
    protected void calculate(final Lohnsteuer2010Big calc) {
        // calculate
        calc.calculate();
    }

    @Override
    protected Lohnsteuer2010Big createCalculator() {
        return new Lohnsteuer2010Big();
    }
}
