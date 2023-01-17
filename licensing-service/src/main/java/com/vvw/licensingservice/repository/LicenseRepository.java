package com.vvw.licensingservice.repository;

import com.vvw.licensingservice.dto.LicenseDto;
import com.vvw.licensingservice.model.License;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public interface LicenseRepository extends ListCrudRepository<License, String> {

    List<LicenseDto> findByOrganizationId(String organizationId);

	Optional<License> findByLicenseId(String id);

	Optional<License> findByLicenseIdAndOrganizationId(String licenseId, String organizationId);

	@Transactional
	Long deleteLicensesByOrganizationId(String organizationId);
}
