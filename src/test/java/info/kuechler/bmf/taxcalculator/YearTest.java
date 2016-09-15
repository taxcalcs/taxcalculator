package info.kuechler.bmf.taxcalculator;

import static info.kuechler.bmf.taxcalculator.rw.SetterGetterUtil.createGetterName;
import static info.kuechler.bmf.taxcalculator.rw.SetterGetterUtil.createSetterName;
import static info.kuechler.bmf.taxcalculator.rw.SetterGetterUtil.getFirstParameterType;
import static info.kuechler.bmf.taxcalculator.rw.SetterGetterUtil.getParameterCount;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Test class. Iterate through a folder with test cases and run the test with every test case. To compare the result
 * call the BMF web service to get the expected result.
 * </p>
 * 
 * <pYou can set a proxy server with the 'https_proxy' property.
 * </p>
 * 
 */
@RunWith(Parameterized.class)
public class YearTest {

    private final static Logger LOG = LoggerFactory.getLogger(YearTest.class);
    
    private static CloseableHttpClient client;
    private static JAXBContext context;

    @Parameter(value = 0)
    public String url;

    @Parameter(value = 1)
    public String testFolder;

    @Parameter(value = 2)
    public String className;
    
    @Parameters
    public static Collection<String[]> data() {
        return Arrays.asList(new String[][] {
                { "http://www.bmf-steuerrechner.de/interface/2006.jsp", "/info/kuechler/bmf/taxcalculator/2006",
                        "Lohnsteuer2006Big" },
                { "http://www.bmf-steuerrechner.de/interface/2007.jsp", "/info/kuechler/bmf/taxcalculator/2006",
                        "Lohnsteuer2007Big" },
                { "http://www.bmf-steuerrechner.de/interface/2008.jsp", "/info/kuechler/bmf/taxcalculator/2008",
                        "Lohnsteuer2008Big" },
                { "http://www.bmf-steuerrechner.de/interface/2009.jsp", "/info/kuechler/bmf/taxcalculator/2008",
                        "Lohnsteuer2009Big" },
                { "http://www.bmf-steuerrechner.de/interface/2010.jsp", "/info/kuechler/bmf/taxcalculator/2010",
                        "Lohnsteuer2010Big" },

                { "http://www.bmf-steuerrechner.de/interface/2011bisNov.jsp", "/info/kuechler/bmf/taxcalculator/2010",
                        "Lohnsteuer2011NovemberBig" },
                { "http://www.bmf-steuerrechner.de/interface/2011Dez.jsp", "/info/kuechler/bmf/taxcalculator/2010",
                        "Lohnsteuer2011DecemberBig" },

                { "http://www.bmf-steuerrechner.de/interface/2012.jsp", "/info/kuechler/bmf/taxcalculator/2010",
                        "Lohnsteuer2012Big" },
                { "http://www.bmf-steuerrechner.de/interface/2013.jsp", "/info/kuechler/bmf/taxcalculator/2010",
                        "Lohnsteuer2013Big" },
                { "http://www.bmf-steuerrechner.de/interface/2014.jsp", "/info/kuechler/bmf/taxcalculator/2010",
                        "Lohnsteuer2014Big" },

                { "http://www.bmf-steuerrechner.de/interface/2015bisNov.jsp", "/info/kuechler/bmf/taxcalculator/2015",
                        "Lohnsteuer2015Big" },
                { "http://www.bmf-steuerrechner.de/interface/2015Dez.jsp", "/info/kuechler/bmf/taxcalculator/2015",
                        "Lohnsteuer2015DezemberBig" },
                        
                { "https://www.bmf-steuerrechner.de/interface/2016V1.jsp", "/info/kuechler/bmf/taxcalculator/2015",
                        "Lohnsteuer2016Big" }
                //
        });
    }

    /**
     * Initialize.
     * 
     * @throws JAXBException
     *             exception during create JAXB context.
     */
    @BeforeClass
    public static final void init() throws JAXBException {
        final Builder builder = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(60000);
        final String proxy = System.getProperty("https_proxy");
        if (StringUtils.isNotBlank(proxy)) {
            builder.setProxy(HttpHost.create(proxy));
        }
        final RequestConfig config = builder.build();

        client = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        context = JAXBContext.newInstance(Result.class);
    }

    /**
     * Destroy.
     * 
     * @throws IOException
     *             exception during close HTTP client.
     */
    @AfterClass
    public static final void stop() throws IOException {
        client.close();
    }

    @Test
    public final void test() throws Exception {
        runFolderTestCases(new URI(url), testFolder);
    }

    /**
     * Create the calculator w/o initialization. Needs return a (new) uninitialized instance at every call.
     * 
     * @return the calculator.
     */
    private Object createCalculator() throws Exception {
        return Class.forName("info.kuechler.bmf.taxcalculator." + className).newInstance();
    }

    /**
     * Run all tests cases in the folder. Test cases requires the name "test<index>.xml". The index starts with "1" and
     * will be risen to the first non exists number.
     * 
     * @param baseUri
     *            Basic URI (w/o parameter) for request expected values.
     * @param folder
     *            the folder (access as resource folder).
     * @throws Exception
     *             an error, test failed.
     */
    private void runFolderTestCases(final URI baseUri, final String folder) throws Exception {
        int i = 1;
        InputStream in = null;
        while (true) {
            try {
                in = getClass().getResourceAsStream(folder + "/test" + i + ".xml");
                if (in == null) {
                    break; // not the best, but we need breaking the loop and
                           // closing the stream
                }
                LOG.info("run " + "test" + i + ".xml");
                final Properties properties = new Properties();
                properties.loadFromXML(in);
                run(baseUri, properties);
                i++;
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        }
    }

    /**
     * Run a test.
     * 
     * @param baseUri
     *            Basic URI (w/o parameter) for request expected values.
     * @param testCase
     *            the input properties.
     * @throws Exception
     *             an error. Test failed
     */
    private void run(final URI baseUri, final Map<?, ?> testCase) throws Exception {
        final Result result = getExpected(baseUri, testCase);
        final Object calc = createCalculator();

        // set input values
        for (final ResultElement elem : result.getInput()) {
            boolean found = false;
            final String setterName = createSetterName(elem.getName());
            for (final Method method : calc.getClass().getMethods()) {
                if (setterName.equals(method.getName()) && getParameterCount(method) == 1) {
                    final Class<?> parameterClass = getFirstParameterType(method);
                    final Object parameter = convert(parameterClass, elem.getValue());
                    method.invoke(calc, parameter);
                    LOG.debug("Input " + elem.getName() + " = " + parameter);
                    found = true;
                    break;
                }
            }
            Assert.assertTrue("No setter found for " + elem.getName(), found || isTestCaseId(elem));
        }

        final Method method = calc.getClass().getMethod("calculate");
        method.invoke(calc);

        // compare output values
        for (final ResultElement elem : result.getOutput()) {
            final Object r = getValue(calc, elem.getName());
            LOG.debug("Output " + elem.getName() + " = " + elem.getValue() + '/' + r);
            Assert.assertEquals(r, elem.getValue());
        }
    }

    /**
     * Extract a value by name from an object. The access will be try via getter and reflections.
     * 
     * @param calc
     *            object
     * @param elemName
     *            element name to search
     * @return value
     * @throws NoSuchFieldException
     *             Exception during access
     * @throws SecurityException
     *             Exception during access
     * @throws IllegalArgumentException
     *             Exception during access
     * @throws IllegalAccessException
     *             Exception during access
     */
    private Object getValue(final Object calc, final String elemName)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        try {
            final Method getter = calc.getClass().getMethod(createGetterName(elemName));
            return getter.invoke(calc);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            LOG.trace("Extract internal field {} tru reflection.", elemName);
            // API sends internal values
            final Field field = calc.getClass().getDeclaredField(elemName);
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            return field.get(calc);
        }
    }

    /**
     * Creates the URI for calling the web service for the expected result.
     * 
     * @param baseUri
     *            Basic URI (w/o parameter) for request expected values.
     * @param testCase
     *            the input properties for the parameter.
     * @return the URI
     * @throws URISyntaxException
     *             an error.
     */
    private URI createUri(final URI baseUri, final Map<?, ?> testCase) throws URISyntaxException {
        final URIBuilder uriBuilder = new URIBuilder(baseUri);
        for (final Map.Entry<?, ?> entry : testCase.entrySet()) {
            uriBuilder.addParameter(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        return uriBuilder.build();
    }

    /**
     * Call the web service to get the expected result.
     * 
     * @param baseUri
     *            Basic URI (w/o parameter) for request expected values.
     * @param testCase
     *            the input properties.
     * @return the expected result.
     * @throws Exception
     *             an error
     */
    private Result getExpected(final URI baseUri, final Map<?, ?> testCase) throws Exception {
        final URI uri = createUri(baseUri, testCase);
        final HttpGet httpget = new HttpGet(uri);

        final HttpResponse response = client.execute(httpget);
        Assert.assertTrue("Method failed: " + response.getStatusLine(),
                response.getStatusLine().getStatusCode() == HttpStatus.SC_OK);

        // BufferedReader in = new BufferedReader(new
        // InputStreamReader(response.getEntity().getContent()));
        // String l;
        // while ((l = in.readLine()) != null) {
        // System.out.println(l);
        // }
        //
        Result result = null;

        InputStream in = null;
        try {
            final Unmarshaller unmarshaller = context.createUnmarshaller();
            in = response.getEntity().getContent();
            result = (Result) unmarshaller.unmarshal(in);
        } finally {
            if (in != null) {
                in.close();
            }
        }

        // Simple validation
        for (final ResultElement resultElement : result.getInput()) {
            Assert.assertTrue("State not ok " + resultElement,
                    StringUtils.equals("ok", resultElement.getStatus()) || isTestCaseId(resultElement));
        }
        Assert.assertTrue("Need at least five input values.", result.getInput().size() > 5);
        Assert.assertTrue("Need at least five output values.", result.getOutput().size() > 5);

        return result;
    }

    /**
     * Converts a {@link BigDecimal} in the expected class. Supports int, long and BigDecimal.
     * 
     * @param parameter
     *            the expected class.
     * @param value
     *            the input
     * @return the converted value or {@link BigDecimal}
     */
    private Object convert(Class<?> parameter, BigDecimal value) {
        if (parameter == int.class) {
            return value.intValue();
        }
        if (parameter == long.class) {
            return value.longValue();
        }
        return value;
    }

    /**
     * Is the {@link ResultElement} the element of the test case ID?
     * 
     * @param resultElement
     *            element to validate
     * @return <code>true</code> if is the element, otherwise <code>false</code>
     */
    private boolean isTestCaseId(ResultElement resultElement) {
        return "TESTCASEID".equals(resultElement.getName());
    }
}
