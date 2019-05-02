package com.forkexec.pts.ws.cli;

/**
 * Client application.
 * 
 * Looks for Points using UDDI and arguments provided
 */
public class PointsClientApp {

	public static void main(String[] args) throws Exception {
		// Check arguments.
		if (args.length == 0) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + PointsClientApp.class.getName() + "uddiURL wsName");
			return;
		}
		String uddiURL = null;
		String wsName = null;
		if (args.length >= 2) {
			uddiURL = args[0];
			wsName = args[1];
		}

		// Create client.
		PointsClient client = null;

		if (uddiURL != null) {
			System.out.printf("Creating client using UDDI at %s for server with name %s%n", uddiURL, wsName);
			client = new PointsClient(uddiURL, wsName);
		}

		// The following remote invocation is just a basic example.
		// The actual tests are made using JUnit.

		System.out.println("Invoke ping()...");
		String result = client.ctrlPing("client");
		System.out.print("Result: ");
		System.out.println(result);
	}

}
