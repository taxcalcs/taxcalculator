package info.kuechler.bmf.taxcalculator;

import java.net.URI;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test Class 2015.
 */
public class Year2015BisNovTest extends AbstractYearTest<Lohnsteuer2015Big> {

    private final static Logger LOG = LoggerFactory.getLogger(Year2015BisNovTest.class);

    @Test
    public final void test() throws Exception {
        runFolderTestCases(new URI("http://www.bmf-steuerrechner.de/interface/2015bisNov.jsp"), "/info/kuechler/bmf/taxcalculator/2015");
    }

    @Override
    Logger getLogger() {
        return LOG;
    }

    @Override
    protected void calculate(final Lohnsteuer2015Big calc) {
        // calculate
        calc.calculate();
    }

    @Override
    protected Lohnsteuer2015Big createCalculator() {
        return new Lohnsteuer2015Big();
    }
}
