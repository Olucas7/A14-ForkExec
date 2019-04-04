package com.forkexec.hub.domain.exceptions;

public class InvalidTextException extends Exception {
	private final String MESSAGE = "Invalid test";

	@Override
	public String getMessage() {
		return MESSAGE;
	}
}