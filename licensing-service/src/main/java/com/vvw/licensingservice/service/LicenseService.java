package com.vvw.licensingservice.service;

import com.vvw.licensingservice.dto.LicenseDto;
import com.vvw.licensingservice.dto.OrganizationDto;
import com.vvw.licensingservice.exception.LicenseNotFoundException;
import com.vvw.licensingservice.model.License;
import com.vvw.licensingservice.repository.LicenseRepository;
import com.vvw.licensingservice.repository.OrganizationRedisRepository;
import com.vvw.licensingservice.service.client.OrganizationDiscoveryClient;
import com.vvw.licensingservice.service.client.OrganizationFeignClient;
import com.vvw.licensingservice.service.client.OrganizationRestTemplateClient;
import com.vvw.licensingservice.service.client.OrganizationWebFluxClient;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LicenseService {

	private final Tracer tracer;

	private final LicenseRepository licenseRepository;

	private final OrganizationRedisRepository redisRepository;

	private final OrganizationFeignClient organizationFeignClient;

	private final OrganizationWebFluxClient organizationWebFluxClient;

	private final OrganizationRestTemplateClient organizationRestClient;

	private final OrganizationDiscoveryClient organizationDiscoveryClient;

	@CircuitBreaker(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
	@Bulkhead(name = "bulkheadLicenseService", type = Bulkhead.Type.THREADPOOL, fallbackMethod = "buildFallbackLicenseList")
	@Retry(name = "retryLicenseService", fallbackMethod = "buildFallbackLicenseList")
	@RateLimiter(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
	public List<LicenseDto> getLicenseByOrganization(String organizationId) {
		return licenseRepository.findByOrganizationId(organizationId);
	}

	public void deleteLicense(String id) {
		licenseRepository.deleteById(id);
	}

	public License createLicense(License license) {
		license.setLicenseId(UUID.randomUUID().toString());
		licenseRepository.save(license);

		return license;
	}

	public void update(String id, License license) {
		licenseRepository.findByLicenseId(id)
				.map(entity -> {
					entity.setLicenseType(license.getLicenseType());
					entity.setComment(license.getComment());
					entity.setDescription(license.getDescription());
					entity.setContactPhone(license.getContactPhone());
					entity.setContactName(license.getContactName());
					entity.setContactEmail(license.getContactEmail());
					entity.setProductName(license.getProductName());
					entity.setOrganizationName(license.getOrganizationName());
					return entity;
				})
				.map(licenseRepository::save)
				.orElseThrow(() -> new LicenseNotFoundException(id));
	}

	@CircuitBreaker(name = "organizationService")
	public Optional<OrganizationDto> retrieveOrganization(String organizationId, String clientType) {
		return checkRedisCache(organizationId)
				.map(org -> {
					log.debug("I have successfully retrieved an organization {} from the redis cache: {}", organizationId, org);
					return org;
				})
				.or(() -> {
					log.debug("Unable to locate organization from the redis cache: {}.", organizationId);
					Optional<OrganizationDto> organizationDto = getOrganizationDto(organizationId, clientType);
					cacheOrganizationObject(organizationDto);
					return organizationDto;
				});
	}

	public Optional<License> getLicense(String licenseId, String organizationId, String clientId) {
		Optional<License> optLicense = licenseRepository.findByLicenseIdAndOrganizationId(licenseId, organizationId);
		log.debug("trying to get from repo with licens id {} client id {}", licenseId, clientId);
		optLicense
				.map(license -> {
					retrieveOrganization(organizationId, clientId)
							.ifPresentOrElse(organizationDto -> {
								license.setOrganizationId(organizationDto.id());
								license.setContactName(organizationDto.contactName());
								license.setContactEmail(organizationDto.contactEmail());
								license.setContactPhone(organizationDto.contactPhone());
							}, () -> {
								throw new IllegalArgumentException();
							});
					return license;
				})
				.orElseThrow(() -> new LicenseNotFoundException(licenseId));
		return optLicense;
	}

	private List<License> buildFallbackLicenseList(String organizationId, Throwable t) {
		log.debug(t.getMessage());
		return List.of(License.builder()
				.licenseId("0000000-00-00000")
				.organizationId(organizationId)
				.productName("Sorry no licensing organization available")
				.build());
	}

	private Optional<OrganizationDto> getOrganizationDto(String organizationId, String clientType) {
		return switch (clientType) {
			case "feign" -> organizationFeignClient.getOrganization(organizationId);
			case "flux" -> organizationWebFluxClient.getOrganization(organizationId);
			case "discovery" -> organizationDiscoveryClient.getOrganization(organizationId);
			default -> organizationRestClient.getOrganization(organizationId);
		};
	}

	private Optional<OrganizationDto> checkRedisCache(String organizationId) {
		ScopedSpan newSpan = tracer.startScopedSpan("readLicensingDataFromRedis");
		try {
			return redisRepository.findById(organizationId);
		} catch (Exception ex) {
			log.error("Error encountered while trying to retrieve organization {} check Redis Cache.  Exception {}", organizationId, ex);
			return Optional.empty();
		} finally {
			newSpan.tag("peer.service", "redis");
			newSpan.event("Client received");
			newSpan.end();
		}
	}

	private void cacheOrganizationObject(Optional<OrganizationDto> organization) {
		organization.ifPresent(organizationDto -> {
			try {
				redisRepository.save(organizationDto);
			} catch (Exception ex) {
				log.error("Unable to cache organization {} in Redis. Exception {}", organizationDto.id(), ex);
			}
		});
	}
}
