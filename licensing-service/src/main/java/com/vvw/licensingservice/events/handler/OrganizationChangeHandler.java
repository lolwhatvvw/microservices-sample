package com.vvw.licensingservice.events.handler;

import com.vvw.licensingservice.events.model.OrganizationChangeModel;
import com.vvw.licensingservice.repository.LicenseRepository;
import com.vvw.licensingservice.repository.OrganizationRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrganizationChangeHandler {

    private final OrganizationRedisRepository redisRepository;

	private final LicenseRepository licenseRepository;

	@Transactional
	public void loggerSink(OrganizationChangeModel organization) {
        log.debug("{}", organization.toString());
        String action = organization.action();
        String organizationId = organization.organizationId();

        log.debug("Received an {} event from organization id {}",
		        action, organizationId);

        if (action.equals("UPDATED") || action.equals("DELETED")) {
            log.debug("Invalidating redis cache for organization id {}", organizationId);

            redisRepository.deleteById(organizationId);
	        Long numbOfDeletedLicenses = licenseRepository.deleteLicensesByOrganizationId(organizationId);

			log.debug("Deleted {} row(s)", numbOfDeletedLicenses);
        }
    }
}
