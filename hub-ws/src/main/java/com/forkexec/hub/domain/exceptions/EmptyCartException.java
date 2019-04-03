package com.forkexec.hub.domain.exceptions;

public class EmptyCartException extends Exception {
	private final String MESSAGE = "Cart is empty";

	@Override
	public String getMessage() {
		return MESSAGE;
	}
}