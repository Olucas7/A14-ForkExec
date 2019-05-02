package com.forkexec.hub.ws.cli;

/** 
 * Client application. 
 * 
 * Looks for Hub using UDDI and arguments provided
 */
public class HubClientApp {

	public static void main(String[] args) throws Exception {
		// Check arguments.
		if (args.length == 0) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + HubClientApp.class.getName() + " wsURL OR uddiURL wsName");
			return;
		}
		String uddiURL = null;
		String wsName = null;
		String wsURL = null;
		if (args.length == 1) {
			wsURL = args[0];
		} else if (args.length >= 2) {
			uddiURL = args[0];
			wsName = args[1];
		}

		// Create client.
		HubClient client = null;

		if (wsURL != null) {
			System.out.printf("Creating client for server at %s%n", wsURL);
			client = new HubClient(wsURL);
		} else if (uddiURL != null) {
			System.out.printf("Creating client using UDDI at %s for server with name %s%n", uddiURL, wsName);
			client = new HubClient(uddiURL, wsName);
		}

		// The following remote invocation is just a basic example.
		// The actual tests are made using JUnit.

		System.out.println("Invoke ping()...");
		String result = client.ctrlPing("client");
		System.out.print("Result: ");
		System.out.println(result);

		System.in.read();
		
		String user = "jbf@gmail.com";
		System.out.println("Activating user %s" + user);
		client.activateAccount(user);
		System.out.println("Activated");
		System.in.read();

		System.out.println("Geting balance");
		int points = client.accountBalance(user);
		System.out.println("User " + user + " has " + points + " points");
		System.in.read();

		System.out.println("Adding balance 10");
		client.loadAccount(user, 10, "4024007102923926");
		System.out.println("Added");
		System.in.read();

		System.out.println("Geting balance");
		points = client.accountBalance(user);
		System.out.println("User " + user + " has " + points + " points");
	}

}
