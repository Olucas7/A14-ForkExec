package com.forkexec.hub.domain.exceptions;

public class InvalidCartItemIdException extends Exception {
	private final String MESSAGE = "Invalid Menu Id";

	@Override
	public String getMessage() {
		return MESSAGE;
	}
}