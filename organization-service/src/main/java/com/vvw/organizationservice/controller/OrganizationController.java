package com.vvw.organizationservice.controller;

import com.vvw.organizationservice.model.Organization;
import com.vvw.organizationservice.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/organization")
public class OrganizationController {

    private final OrganizationService service;

    @Autowired
    public OrganizationController(OrganizationService service) {
        this.service = service;
    }

    @GetMapping(value = "/{organizationId}")
    public ResponseEntity<Organization> getOrganization(@PathVariable("organizationId") String organizationId) {
        return ResponseEntity.of(service.findById(organizationId));
    }

    @GetMapping
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        return new ResponseEntity<>(service.retrieveAllOrganizations(), HttpStatus.OK);
    }

	@PostMapping
	public ResponseEntity<Organization> saveOrganization(@RequestBody Organization organization) {
		return ResponseEntity.ok(service.create(organization));
	}

    @PutMapping(value = "/{organizationId}")
    public ResponseEntity<?> updateOrganization(@PathVariable("organizationId") String id, @RequestBody Organization organization) {
        service.update(id, organization);
		return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{organizationId}")
    public ResponseEntity<?> deleteOrganization(@PathVariable("organizationId") String organizationId) {
        service.deleteOrganizationById(organizationId);
        return ResponseEntity.noContent().build();
    }
}
