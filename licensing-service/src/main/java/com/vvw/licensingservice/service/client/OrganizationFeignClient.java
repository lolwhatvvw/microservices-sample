package com.vvw.licensingservice.service.client;


import com.vvw.licensingservice.dto.OrganizationDto;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@FeignClient("organization-service")
public interface OrganizationFeignClient {
    @RequestMapping(
            method= RequestMethod.GET,
            value="api/v1/organization/{organizationId}",
            consumes="application/json")
    Optional<OrganizationDto> getOrganization(@PathVariable("organizationId") String organizationId);
}

@Component
class LanguageRequestInterceptor implements RequestInterceptor {
	private static final String AUTHORIZATION = "Authorization";
	@Override
	public void apply(RequestTemplate requestTemplate) {
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (requestAttributes == null) {
			return;
		}
		HttpServletRequest request = requestAttributes.getRequest();
		String auth = request.getHeader(AUTHORIZATION);
		if (auth == null) {
			return;
		}
		requestTemplate.header(AUTHORIZATION, auth);
	}
}