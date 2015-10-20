package info.kuechler.bmf.taxcalculator;

import java.net.URI;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test Class 2015.
 */
public class Year2015DecTest extends AbstractYearTest<Lohnsteuer2015DezemberBig> {

    private final static Logger LOG = LoggerFactory.getLogger(Year2015DecTest.class);

    @Test
    public final void test() throws Exception {
        runFolderTestCases(new URI("http://www.bmf-steuerrechner.de/interface/2015Dez.jsp"), "/info/kuechler/bmf/taxcalculator/2015Dec");
    }

    @Override
    Logger getLogger() {
        return LOG;
    }

    @Override
    protected void calculate(final Lohnsteuer2015DezemberBig calc) {
        // calculate
        calc.calculate();
    }

    @Override
    protected Lohnsteuer2015DezemberBig createCalculator() {
        return new Lohnsteuer2015DezemberBig();
    }
}
