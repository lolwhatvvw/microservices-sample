package com.vvw.licensingservice.controller;

import com.vvw.licensingservice.dto.LicenseDto;
import com.vvw.licensingservice.model.License;
import com.vvw.licensingservice.service.LicenseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/api/v1/organization/{organizationId}/license")
public class LicenseController {

    private final LicenseService licenseService;

    @Autowired
    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

	@PostMapping
	public ResponseEntity<License> createLicense(@RequestBody License license) {
		return ResponseEntity.ok(licenseService.createLicense(license));
	}

    @GetMapping
    public ResponseEntity<List<LicenseDto>> getLicenses(@PathVariable("organizationId") String organizationId) {
        return new ResponseEntity<>(licenseService.getLicenseByOrganization(organizationId), HttpStatus.OK);
    }

	@GetMapping("/{licenseId}")
    ResponseEntity<License> getLicense(@PathVariable("licenseId") String licenseId, @PathVariable("organizationId") String organizationId) {
		log.debug("Entering the license-controller  ");
		Optional<License> optLicense = licenseService.getLicense(licenseId, organizationId, "");
		optLicense.map(license -> license.add(
				linkTo(methodOn(LicenseController.class).getLicense(organizationId, license.getLicenseId())).withSelfRel(),
				linkTo(methodOn(LicenseController.class).createLicense(license)).withRel("createLicense"),
				linkTo(methodOn(LicenseController.class).updateLicense(license.getLicenseId(), license)).withRel("updateLicense"),
				linkTo(methodOn(LicenseController.class).deleteLicense(license.getLicenseId())).withRel("deleteLicense")
		));
		return ResponseEntity.of(optLicense);
    }

    @GetMapping("/{licenseId}/{clientId}")
    ResponseEntity<License> getLicenseByClient(
            @PathVariable("organizationId") String organizationId,
            @PathVariable("licenseId") String licenseId,
            @PathVariable("clientId") String clientId) {
        return ResponseEntity.of(licenseService.getLicense(licenseId, organizationId, clientId));
    }

	@PutMapping(value = "/{licenseId}")
	public ResponseEntity<?> updateLicense(@PathVariable("licenseId") String id, @RequestBody License license) {
		licenseService.update(id, license);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{licenseId}")
	public ResponseEntity<?> deleteLicense(@PathVariable("licenseId") String licenseId) {
		licenseService.deleteLicense(licenseId);
		return ResponseEntity.noContent().build();
	}
}
