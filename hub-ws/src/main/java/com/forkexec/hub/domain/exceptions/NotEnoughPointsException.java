package com.forkexec.hub.domain.exceptions;

public class NotEnoughPointsException extends Exception {
	private final String MESSAGE = "User without balance";

	@Override
	public String getMessage() {
		return MESSAGE;
	}
}