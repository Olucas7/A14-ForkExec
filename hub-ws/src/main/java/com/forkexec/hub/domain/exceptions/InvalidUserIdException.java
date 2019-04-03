package com.forkexec.hub.domain.exceptions;

public class InvalidUserIdException extends Exception {
	private final String MESSAGE = "Invalid User ID";

	@Override
	public String getMessage() {
		return MESSAGE;
	}
}