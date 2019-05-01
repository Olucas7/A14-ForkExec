package com.forkexec.pts.ws;

import java.util.concurrent.atomic.AtomicInteger;

import javax.jws.WebService;
import javax.xml.ws.Holder;

import com.forkexec.pts.domain.Points;
import com.forkexec.pts.domain.Value;
import com.forkexec.pts.domain.exception.InvalidEmailFaultException;
import com.forkexec.pts.domain.exception.InvalidPointsFaultException;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.forkexec.pts.ws.PointsPortType", wsdlLocation = "PointsService.wsdl", name = "PointsWebService", portName = "PointsPort", targetNamespace = "http://ws.pts.forkexec.com/", serviceName = "PointsService")
public class PointsPortImpl implements PointsPortType {

	/**
	 * The Endpoint manager controls the Web Service instance during its whole
	 * lifecycle.
	 */
	private final PointsEndpointManager endpointManager;

	/** Constructor receives a reference to the endpoint manager. */
	public PointsPortImpl(final PointsEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}

	// Main operations -------------------------------------------------------

	@Override
	public void readPoints(final String userEmail, Holder<Integer> pointsHolder, Holder<Integer> tagHolder)
			throws InvalidEmailFault_Exception {
		try {
			final Points points = Points.getInstance();
			final Value info = points.getAccountPoints(userEmail);
			pointsHolder.value = info.getPoints().get();
			tagHolder.value = info.getTag();
		} catch (InvalidEmailFaultException e) {
			throwInvalidEmailFault(e.getMessage());
		}
	}

	@Override
	public int writePoints(final String userEmail, final int newpoints, final int tag)
			throws InvalidEmailFault_Exception, InvalidPointsFault_Exception {
		try {
			final Value info = new Value(new AtomicInteger(newpoints), tag);
			final Points points = Points.getInstance();
			points.setPoints(userEmail, info);
		} catch (InvalidEmailFaultException e) {
			throwInvalidEmailFault(e.getMessage());
		} catch (InvalidPointsFaultException e) {
			throwInvalidPointsFault(e.getMessage());
		}
		return newpoints;
	}

	@Override
	public int getMaxTag() {
		Points points = Points.getInstance();
		return points.getMaxTag();
	}

	// Control operations ----------------------------------------------------
	/** Diagnostic operation to check if service is running. */
	@Override
	public String ctrlPing(String inputMessage) {
		// If no input is received, return a default name.
		if (inputMessage == null || inputMessage.trim().length() == 0)
			inputMessage = "friend";

		// If the service does not have a name, return a default.
		String wsName = endpointManager.getWsName();
		if (wsName == null || wsName.trim().length() == 0)
			wsName = PointsApp.class.getSimpleName();

		// Build a string with a message to return.
		final StringBuilder builder = new StringBuilder();
		builder.append("Hello ").append(inputMessage);
		builder.append(" from ").append(wsName);
		return builder.toString();
	}

	/** Return all variables to default values. */
	@Override
	public void ctrlClear() {
		Points.getInstance().reset();
	}

	/** Set variables with specific values. */
	@Override
	public void ctrlInit(final int startPoints) throws BadInitFault_Exception {
		Points.getInstance().setInitialBalance(startPoints);
	}

	// Exception helpers -----------------------------------------------------

	/** Helper to throw a new BadInit exception. */
	private void throwBadInit(final String message) throws BadInitFault_Exception {
		final BadInitFault faultInfo = new BadInitFault();
		faultInfo.message = message;
		throw new BadInitFault_Exception(message, faultInfo);
	}

	/** Helper to throw a new InvalidEmailFault exception. */
	private void throwInvalidEmailFault(final String message) throws InvalidEmailFault_Exception {
		final InvalidEmailFault faultInfo = new InvalidEmailFault();
		faultInfo.message = message;
		throw new InvalidEmailFault_Exception(message, faultInfo);
	}

	/** Helper to throw a new InvalidPointsFault exception. */
	private void throwInvalidPointsFault(final String message) throws InvalidPointsFault_Exception {
		final InvalidPointsFault faultInfo = new InvalidPointsFault();
		faultInfo.message = message;
		throw new InvalidPointsFault_Exception(message, faultInfo);
	}

	
}
