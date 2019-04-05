package com.forkexec.hub.ws.it;

import java.io.IOException;
import java.util.Properties;

import com.forkexec.hub.ws.cli.HubClient;

import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Base class for testing a Park Load properties from test.properties
 */
public class BaseIT {

	private static final String TEST_PROP_FILE = "/test.properties";
	protected static Properties testProps;

	protected static HubClient client;

	protected final String VALID_EMAIL = "joao.barata@tecnico.pt";
	protected final String NULL_EMAIL = null;
	protected final String NO_AT_EMAIL = "joao.baratatecnico.pt";
	protected final String EMPTY_EMAIL = "";
	protected final String NO_USER_EMAIL = "@tecnico.pt";
	protected final String NO_DOMAIN_EMAIL = "velhinho@";
	protected final String NO_USER_DOMAIN_EMAIL = "@";
	protected final int INITIAL_BALANCE = 100;
	protected final String RESTAURANT1 = "A14_Restaurant1";
	protected final String RESTAURANT2 = "A14_Restaurant2";

	@BeforeClass
	public static void oneTimeSetup() throws Exception {
		testProps = new Properties();
		try {
			testProps.load(BaseIT.class.getResourceAsStream(TEST_PROP_FILE));
			System.out.println("Loaded test properties:");
			System.out.println(testProps);
		} catch (IOException e) {
			final String msg = String.format("Could not load properties file {}", TEST_PROP_FILE);
			System.out.println(msg);
			throw e;
		}

		final String uddiEnabled = testProps.getProperty("uddi.enabled");
		final String verboseEnabled = testProps.getProperty("verbose.enabled");

		final String uddiURL = testProps.getProperty("uddi.url");
		final String wsName = testProps.getProperty("ws.name");
		final String wsURL = testProps.getProperty("ws.url");

		if ("true".equalsIgnoreCase(uddiEnabled)) {
			client = new HubClient(uddiURL, wsName);
		} else {
			client = new HubClient(wsURL);
		}
		client.setVerbose("true".equalsIgnoreCase(verboseEnabled));
	}

	@AfterClass
	public static void cleanup() {
	}

}
