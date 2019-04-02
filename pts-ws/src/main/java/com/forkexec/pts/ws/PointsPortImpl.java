package com.forkexec.pts.ws;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jws.WebService;

import com.forkexec.pts.domain.Points;

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
	public void activateUser(final String userEmail)throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception{
        checkEmail(userEmail);
        Points psingleton = Points.getInstance();
        psingleton.registerEmail(userEmail);
        
    }

    
    @Override
    public int pointsBalance(final String userEmail) throws InvalidEmailFault_Exception {
        checkEmail(userEmail);
        return Points.getInstance().getBalance(userEmail);
    }

    @Override
    public int addPoints(final String userEmail, final int pointsToAdd)
	    throws InvalidEmailFault_Exception, InvalidPointsFault_Exception {
        //TODO
        return -1;
    }

    @Override
    public int spendPoints(final String userEmail, final int pointsToSpend)
	    throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
        //TODO
        return -1;
    }

    // Control operations ----------------------------------------------------
    // TODO
    /** Diagnostic operation to check if service is running. */
    @Override
    public String ctrlPing(String inputMessage) {
	// If no input is received, return a default name.
	if (inputMessage == null || inputMessage.trim().length() == 0)
	    inputMessage = "friend";

	// If the park does not have a name, return a default.
	String wsName = endpointManager.getWsName();
	if (wsName == null || wsName.trim().length() == 0)
	    wsName = "Park";

	// Build a string with a message to return.
	final StringBuilder builder = new StringBuilder();
	builder.append("Hello ").append(inputMessage);
	builder.append(" from ").append(wsName);
	return builder.toString();
    }

    /** Return all variables to default values. */
    @Override
    public void ctrlClear() {
        //TODO
    }

    /** Set variables with specific values. */
    @Override
    public void ctrlInit(final int startPoints) throws BadInitFault_Exception {
        //TODO
    }

    // Aux functions --------------------------------------------------------

    public void checkEmail(String userEmail) throws InvalidEmailFault_Exception{
        final InvalidEmailFault faultInfo = new InvalidEmailFault();
        String message_null = "null email address";
        String message_invalid = "Not a valid email address";


        if (userEmail == null ) 
            throw new InvalidEmailFault_Exception(message_null, faultInfo);

        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher mat = pattern.matcher(userEmail);
        if(!mat.matches())
            throw new InvalidEmailFault_Exception(message_invalid, faultInfo);
    }


    // Exception helpers -----------------------------------------------------

    /** Helper to throw a new BadInit exception. */
    private void throwBadInit(final String message) throws BadInitFault_Exception {
        final BadInitFault faultInfo = new BadInitFault();
        faultInfo.message = message;
        throw new BadInitFault_Exception(message, faultInfo);
    }
}