package com.vvw.organizationservice.exception;

import java.io.Serial;

public class OrganizationNotFoundException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 5071646428281007896L;

	public OrganizationNotFoundException(String message) {
		super(message);
	}
}