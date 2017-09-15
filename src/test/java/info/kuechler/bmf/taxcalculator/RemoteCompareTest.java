package info.kuechler.bmf.taxcalculator;

import static info.kuechler.bmf.taxapi.TaxApiFactory.getUrl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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

import info.kuechler.bmf.taxapi.Ausgabe;
import info.kuechler.bmf.taxapi.Eingabe;
import info.kuechler.bmf.taxapi.Lohnsteuer;

/**
 * <p>
 * Test class. Iterate through a folder with test cases and run the test with
 * every test case. To compare the result call the BMF web service to get the
 * expected result.
 * </p>
 * 
 * <p>
 * You can set a proxy server with the 'https_proxy' property.
 * </p>
 * 
 */
@RunWith(Parameterized.class)
public class RemoteCompareTest {

	private final static Logger LOG = LoggerFactory.getLogger(RemoteCompareTest.class);

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
		return Arrays.asList(new String[][] { //
				{ getUrl(0, 2006), "/info/kuechler/bmf/taxcalculator/2006", "Lohnsteuer2006Big" },
				{ getUrl(0, 2007), "/info/kuechler/bmf/taxcalculator/2006", "Lohnsteuer2007Big" },
				{ getUrl(0, 2008), "/info/kuechler/bmf/taxcalculator/2008", "Lohnsteuer2008Big" },
				{ getUrl(0, 2009), "/info/kuechler/bmf/taxcalculator/2008", "Lohnsteuer2009Big" },
				{ getUrl(0, 2010), "/info/kuechler/bmf/taxcalculator/2010", "Lohnsteuer2010Big" },

				{ getUrl(1, 2011), "/info/kuechler/bmf/taxcalculator/2010", "Lohnsteuer2011NovemberBig" },
				{ getUrl(0, 2011), "/info/kuechler/bmf/taxcalculator/2010", "Lohnsteuer2011DecemberBig" },

				{ getUrl(0, 2012), "/info/kuechler/bmf/taxcalculator/2010", "Lohnsteuer2012Big" },
				{ getUrl(0, 2013), "/info/kuechler/bmf/taxcalculator/2010", "Lohnsteuer2013Big" },
				{ getUrl(0, 2014), "/info/kuechler/bmf/taxcalculator/2010", "Lohnsteuer2014Big" },

				{ getUrl(4, 2015), "/info/kuechler/bmf/taxcalculator/2015", "Lohnsteuer2015Big" },
				{ getUrl(0, 2015), "/info/kuechler/bmf/taxcalculator/2015", "Lohnsteuer2015DezemberBig" },

				{ getUrl(0, 2016), "/info/kuechler/bmf/taxcalculator/2015", "Lohnsteuer2016Big" },
				{ getUrl(0, 2017), "/info/kuechler/bmf/taxcalculator/2015", "Lohnsteuer2017Big" }
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
		context = JAXBContext.newInstance(Lohnsteuer.class);
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
	 * Create the calculator w/o initialization. Needs return a (new)
	 * uninitialized instance at every call.
	 * 
	 * @return the calculator.
	 */
	@SuppressWarnings("unchecked")
	private <T extends Calculator<T>> T createCalculator() throws Exception {
		return (T) Class.forName("info.kuechler.bmf.taxcalculator." + className).newInstance();
	}

	/**
	 * Run all tests cases in the folder. Test cases requires the name
	 * "test&lt;index&gt;.xml". The index starts with "1" and will be risen to
	 * the first non exists number.
	 * 
	 * @throws Exception
	 *             an error, test failed.
	 */
	@Test
	public final void runFolderTestCases() throws Exception {
		int i = 1;
		InputStream in = null;
		while (true) {
			try {
				in = getClass().getResourceAsStream(testFolder + "/test" + i + ".xml");
				if (in == null) {
					break; // not the best, but we need breaking the loop and
							// closing the stream
				}
				LOG.info("run " + "test" + i + ".xml");
				final Properties properties = new Properties();
				properties.loadFromXML(in);
				Assert.assertTrue(run(new URI(url), properties));
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
	private boolean run(final URI baseUri, final Map<?, ?> testCase) throws Exception {
		final Lohnsteuer result = getExpected(baseUri, testCase);
		final Calculator<?> calc = createCalculator();
		final Accessor<String, ?> accessor = calc.getAccessor();
		final Map<String, Class<?>> types = accessor.getInputsWithType();

		// set input values
		for (final Eingabe elem : result.getEingaben()) {
			if (!isTestCaseId(elem)) {
				final Class<?> type = types.get(elem.getName());
				accessor.set(elem.getName(), convert(type, elem.getValue()));
				LOG.debug("Input " + elem.getName() + " = " + elem.getValue());
			}
		}

		calc.calculate();

		// compare output values
		final Set<String> outs = accessor.getOutputsWithType().keySet();
		for (final Ausgabe elem : result.getAusgaben()) {
			if (outs.contains(elem.getName())) {
				final Object r = accessor.get(elem.getName());
				LOG.debug("Output " + elem.getName() + " = " + elem.getValue() + '/' + r);
				Assert.assertEquals(r, elem.getValue());
			} else {
				LOG.debug("Unknown result " + elem.getName() + " maybe an internal field");
			}
		}
		return true;
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
	private Lohnsteuer getExpected(final URI baseUri, final Map<?, ?> testCase) throws Exception {
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
		Lohnsteuer result = null;

		InputStream in = null;
		try {
			final Unmarshaller unmarshaller = context.createUnmarshaller();
			in = response.getEntity().getContent();
			result = (Lohnsteuer) unmarshaller.unmarshal(in);
		} finally {
			if (in != null) {
				in.close();
			}
		}

		// Simple validation
		for (final Eingabe resultElement : result.getEingaben()) {
			Assert.assertTrue("State not ok " + resultElement,
					StringUtils.equals("ok", resultElement.getStatus()) || isTestCaseId(resultElement));
		}
		Assert.assertTrue("Need at least five input values.", result.getEingaben().size() > 5);
		Assert.assertTrue("Need at least five output values.", result.getAusgaben().size() > 5);

		return result;
	}

	/**
	 * Converts a {@link BigDecimal} in the expected class. Supports int, long
	 * and BigDecimal.
	 * 
	 * @param parameter
	 *            the expected class.
	 * @param value
	 *            the input
	 * @return the converted value or {@link BigDecimal}
	 */
	private Number convert(Class<?> parameter, BigDecimal value) {
		if (parameter == int.class) {
			return Integer.valueOf(value.intValue());
		}
		if (parameter == long.class) {
			return Double.valueOf(value.longValue());
		}
		return value;
	}

	/**
	 * Is the {@link ResultElement} the element of the test case ID?
	 * 
	 * @param resultElement
	 *            element to validate
	 * @return {@code true} if is the element, otherwise {@code false}
	 */
	private boolean isTestCaseId(Eingabe resultElement) {
		return "TESTCASEID".equals(resultElement.getName());
	}
}
