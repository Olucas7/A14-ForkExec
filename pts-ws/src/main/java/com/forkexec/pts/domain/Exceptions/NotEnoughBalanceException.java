package com.forkexec.pts.domain.Exceptions;
public class NotEnoughBalanceException extends Exception {
	private final String MESSAGE = "not enough balance";

	@Override
	public String getMessage() {
		return MESSAGE;
	}
}