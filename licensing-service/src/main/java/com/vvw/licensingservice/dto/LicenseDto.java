package com.vvw.licensingservice.dto;

public record LicenseDto (
		String licenseId,
		String description,
		String organizationId,
		String productName,
		String licenseType,
		String comment) {
}