package com.vvw.licensingservice.service.client;

import com.vvw.licensingservice.dto.OrganizationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrganizationWebFluxClient {

    private final WebClient.Builder webClientBuilder;

    public Optional<OrganizationDto> getOrganization(String organizationId) {
        OrganizationDto organizationDtoMono = webClientBuilder.build()
                .get()
                .uri("http://organization-service/api/v1/organization/{organizationId}", organizationId)
                .retrieve()
                .bodyToMono(OrganizationDto.class)
                .block();
        return Optional.ofNullable(organizationDtoMono);
    }
}
