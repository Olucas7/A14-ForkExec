package com.forkexec.hub.domain.exceptions;

public class InvalidCardNumberException extends Exception {
	private final String MESSAGE = "Invalid card number";

	@Override
	public String getMessage() {
		return MESSAGE;
	}
}