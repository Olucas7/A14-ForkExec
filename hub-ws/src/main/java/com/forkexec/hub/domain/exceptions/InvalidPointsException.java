package com.forkexec.hub.domain.exceptions;

public class InvalidPointsException extends Exception {
	private final String MESSAGE = "Invalid points";

	@Override
	public String getMessage() {
		return MESSAGE;
	}
}