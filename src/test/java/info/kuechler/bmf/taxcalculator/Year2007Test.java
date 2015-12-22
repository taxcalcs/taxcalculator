package info.kuechler.bmf.taxcalculator;

import java.net.URI;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test Class 2007.
 */
public class Year2007Test extends AbstractYearTest<Lohnsteuer2007Big> {

    private final static Logger LOG = LoggerFactory.getLogger(Year2007Test.class);

    @Test
    public final void test() throws Exception {
        runFolderTestCases(new URI("http://www.bmf-steuerrechner.de/interface/2007.jsp"), "/info/kuechler/bmf/taxcalculator/2006");
    }

    @Override
    Logger getLogger() {
        return LOG;
    }

    @Override
    protected void calculate(final Lohnsteuer2007Big calc) {
        // calculate
        calc.calculate();
    }

    @Override
    protected Lohnsteuer2007Big createCalculator() {
        return new Lohnsteuer2007Big();
    }
}
