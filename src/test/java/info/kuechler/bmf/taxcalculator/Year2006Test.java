package info.kuechler.bmf.taxcalculator;

import java.net.URI;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test Class 2006.
 */
public class Year2006Test extends AbstractYearTest<Lohnsteuer2006Big> {

    private final static Logger LOG = LoggerFactory.getLogger(Year2006Test.class);

    @Test
    public final void test() throws Exception {
        runFolderTestCases(new URI("http://www.bmf-steuerrechner.de/interface/2006.jsp"), "/info/kuechler/bmf/taxcalculator/2006");
    }

    @Override
    Logger getLogger() {
        return LOG;
    }

    @Override
    protected void calculate(final Lohnsteuer2006Big calc) {
        // calculate
        calc.calculate();
    }

    @Override
    protected Lohnsteuer2006Big createCalculator() {
        return new Lohnsteuer2006Big();
    }
}
