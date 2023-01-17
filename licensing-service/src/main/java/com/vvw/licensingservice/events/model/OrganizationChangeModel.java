package com.vvw.licensingservice.events.model;

public record OrganizationChangeModel(
        String type,
        String action,
        String correlationId,
        String organizationId) {
}
