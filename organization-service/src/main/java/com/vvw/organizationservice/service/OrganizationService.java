package com.vvw.organizationservice.service;

import com.vvw.organizationservice.events.source.SimpleSourceBean;
import com.vvw.organizationservice.exception.OrganizationNotFoundException;
import com.vvw.organizationservice.model.Organization;
import com.vvw.organizationservice.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {

	private final SimpleSourceBean simpleSourceBean;
	private final OrganizationRepository organizationRepository;

	public Organization create(Organization organization) {
		organization.setId(UUID.randomUUID().toString());
		simpleSourceBean.publishOrganizationChange(SimpleSourceBean.Action.CREATED, organization.getId());
		organization = organizationRepository.save(organization);
		return organization;
	}

	public List<Organization> retrieveAllOrganizations() {
		simpleSourceBean.publishOrganizationChange(SimpleSourceBean.Action.GET, "all");
		return organizationRepository.findAll();
	}

	public Optional<Organization> findById(String organizationId) {
		simpleSourceBean.publishOrganizationChange(SimpleSourceBean.Action.GET, organizationId);
		return organizationRepository.findById(organizationId);
	}

	public void update(String organizationId, Organization organization) {
		Organization org = organizationRepository.findById(organizationId)
				.orElseThrow(() -> new OrganizationNotFoundException("No organization with id %s is available".formatted(organizationId)));
		org.setName(organization.getName());
		org.setContactName(organization.getContactName());
		org.setContactPhone(organization.getContactPhone());
		org.setContactEmail(organization.getContactEmail());
		simpleSourceBean.publishOrganizationChange(SimpleSourceBean.Action.UPDATED, org.getId());
		organizationRepository.save(org);
	}

	public void deleteOrganizationById(String organizationId) {
		simpleSourceBean.publishOrganizationChange(SimpleSourceBean.Action.DELETED, organizationId);
		organizationRepository.deleteById(organizationId);
	}
}