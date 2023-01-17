package com.vvw.licensingservice.repository;

import com.vvw.licensingservice.dto.OrganizationDto;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRedisRepository extends ListCrudRepository<OrganizationDto, String> {
}
