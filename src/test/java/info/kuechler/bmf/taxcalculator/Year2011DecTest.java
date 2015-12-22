package info.kuechler.bmf.taxcalculator;

import java.net.URI;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test Class 2011 December.
 */
public class Year2011DecTest extends AbstractYearTest<Lohnsteuer2011DecemberBig> {

    private final static Logger LOG = LoggerFactory.getLogger(Year2011DecTest.class);

    @Test
    public final void test() throws Exception {
        runFolderTestCases(new URI("http://www.bmf-steuerrechner.de/interface/2011Dez.jsp"), "/info/kuechler/bmf/taxcalculator/2010");
    }

    @Override
    Logger getLogger() {
        return LOG;
    }

    @Override
    protected void calculate(final Lohnsteuer2011DecemberBig calc) {
        // calculate
        calc.calculate();
    }

    @Override
    protected Lohnsteuer2011DecemberBig createCalculator() {
        return new Lohnsteuer2011DecemberBig();
    }
}
