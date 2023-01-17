package com.vvw.gatewayserver.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.client.oidc.web.server.logout.OidcClientInitiatedServerLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

	private final ReactiveClientRegistrationRepository clientRegistrationRepository;

	@Autowired
	public SecurityConfig(ReactiveClientRegistrationRepository clientRegistrationRepository) {
		this.clientRegistrationRepository = clientRegistrationRepository;
	}

	@Bean
	SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http
				.csrf(ServerHttpSecurity.CsrfSpec::disable)
				.authorizeExchange(authorize -> authorize
						.pathMatchers("/actuator/**").permitAll()
						.anyExchange().authenticated()
				)
				.oauth2Login(withDefaults())
				.logout(logout -> logout
						.logoutSuccessHandler(oidcLogoutSuccessHandler())
				);
		return http.build();
	}

	@Bean
	public ServerLogoutSuccessHandler oidcLogoutSuccessHandler() {
		var successHandler = new OidcClientInitiatedServerLogoutSuccessHandler(clientRegistrationRepository);
		successHandler.setPostLogoutRedirectUri("{baseUrl}/logout.html");
		return successHandler;
	}
}
