package com.vvw.licensingservice.service.client;

import com.vvw.licensingservice.dto.OrganizationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class OrganizationRestTemplateClient {

    private final RestTemplate restTemplate;

    public Optional<OrganizationDto> getOrganization(String organizationId){
	    restTemplate.getInterceptors().add((request, body, execution) -> {
		    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    if (authentication == null) {
			    return execution.execute(request, body);
		    }

		    if (!(authentication.getCredentials() instanceof AbstractOAuth2Token token)) {
			    return execution.execute(request, body);
		    }
		    request.getHeaders().setBearerAuth(token.getTokenValue());
		    return execution.execute(request, body);
	    });

        ResponseEntity<OrganizationDto> restExchange =
                restTemplate.exchange(
                        "http://organization-service/api/v1/organization/{organizationId}",
                        HttpMethod.GET,
                        null, OrganizationDto.class, organizationId);
        return Optional.ofNullable(restExchange.getBody());
    }
}
