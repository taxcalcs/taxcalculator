package info.kuechler.bmf.taxcalculator;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
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
import org.slf4j.Logger;

/**
 * <p>
 * Abstract test class. Iterate through a folder with test cases and run the test with every test case. To compare the
 * result call the BMF web service to get the expected result.
 * </p>
 * 
 * <pYou can set a proxy server with the 'https_proxy' property.</p>
 * 
 * @param <T>
 *            class of the calculator
 */
public abstract class AbstractYearTest<T> {

    private static CloseableHttpClient client;
    private static JAXBContext context;

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

    /**
     * Calculate the result.
     * 
     * @param calc
     *            the initialized calculate object
     */
    abstract void calculate(final T calc);

    /**
     * Create the calculator w/o initialization. Needs return a (new) uninitialized instance at every call.
     * 
     * @return the calculator.
     */
    abstract T createCalculator();

    /**
     * Get the logger.
     * 
     * @return the logger.
     */
    abstract Logger getLogger();

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
    protected void runFolderTestCases(final URI baseUri, final String folder) throws Exception {
        int i = 1;
        InputStream in = null;
        while (true) {
            try {
                in = getClass().getResourceAsStream(folder + "/test" + i + ".xml");
                if (in == null) {
                    break; // not the best, but we need breaking the loop and closing the stream
                }
                getLogger().info("run " + "test" + i + ".xml");
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
        final T calc = createCalculator();

        // set input values
        for (final ResultElement elem : result.getInput()) {
            boolean found = false;
            final String setterName = getSetterName(elem.getName());
            for (final Method method : calc.getClass().getMethods()) {
                if (setterName.equals(method.getName()) && method.getParameterTypes().length == 1) {
                    final Class<?> parameterClass = method.getParameterTypes()[0];
                    final Object parameter = convert(parameterClass, elem.getValue());
                    method.invoke(calc, parameter);
                    getLogger().debug("Input " + elem.getName() + " = " + parameter);
                    found = true;
                    break;
                }
            }
            Assert.assertTrue("No setter found for " + elem.getName(), found || isTestCaseId(elem));
        }

        calculate(calc);

        // compare output values
        for (final ResultElement elem : result.getOutput()) {
            final Object r = getValue(calc, elem.getName());
            getLogger().debug("Output " + elem.getName() + " = " + elem.getValue() + '/' + r);
            Assert.assertEquals(r, elem.getValue());
        }
    }

    /**
     * Extract a value by name from an object. The access will be try via getter
     * and reflections.
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
    private Object getValue(final T calc, final String elemName)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        try {
            final Method getter = calc.getClass().getMethod(getGetterName(elemName));
            return getter.invoke(calc);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            getLogger().trace("Extract internal field {} tru reflection.", elemName);
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

        // BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
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
            Assert.assertTrue("State not ok " + resultElement, StringUtils.equals("ok", resultElement.getStatus())
                            || isTestCaseId(resultElement));
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
     * Creates the setter method name for an property.
     * 
     * @param name
     *            the property name.
     * @return the setter name
     */
    private String getSetterName(String name) {
        return "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    /**
     * Creates the getter method name for an property.
     * 
     * @param name
     *            the property name.
     * @return the getter name
     */
    private String getGetterName(String name) {
        return "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
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
