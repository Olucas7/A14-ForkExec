package com.forkexec.pts.domain.Exceptions;
public class InvalidEmailException extends Exception {
	private final String MESSAGE = "Invalid email";

	@Override
	public String getMessage() {
		return MESSAGE;
	}
}