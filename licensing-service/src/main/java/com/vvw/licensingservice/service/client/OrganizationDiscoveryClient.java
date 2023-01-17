package com.vvw.licensingservice.service.client;

import com.vvw.licensingservice.dto.OrganizationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

//Intentionally doesn't propagate authorization header with bearer token
@Component
@RequiredArgsConstructor
public class OrganizationDiscoveryClient {

    private final DiscoveryClient discoveryClient;

    public Optional<OrganizationDto> getOrganization(String organizationId) {
        var restTemplate = new RestTemplate();
        List<ServiceInstance> instances = discoveryClient.getInstances("organization-service");

        if (instances.size() == 0) return Optional.empty();
        String serviceUri = String.format("%s/api/v1/organization/%s",instances.get(0).getUri().toString(), organizationId);
    
        ResponseEntity<OrganizationDto> restExchange =
                restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        null, OrganizationDto.class, organizationId);

        return Optional.ofNullable(restExchange.getBody());
    }
}
