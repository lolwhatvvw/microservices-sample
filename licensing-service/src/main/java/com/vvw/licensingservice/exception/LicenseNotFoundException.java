package com.vvw.licensingservice.exception;

import java.io.Serial;

public class LicenseNotFoundException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 3192595075395842L;

	public LicenseNotFoundException(String licenseId) {
		super("License with id %s could not be found".formatted(licenseId));
	}
}