package com.vvw.organizationservice.repository;

import com.vvw.organizationservice.model.Organization;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Repository
public interface OrganizationRepository extends ListCrudRepository<Organization, String> {

    @NotNull
    @Transactional(readOnly = true)
    Optional<Organization> findById(@NotNull String organizationId);
}