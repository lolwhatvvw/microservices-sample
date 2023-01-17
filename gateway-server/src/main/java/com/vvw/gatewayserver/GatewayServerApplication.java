package com.vvw.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.security.Principal;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
public class GatewayServerApplication {

	@Bean
	@LoadBalanced
	public WebClient.Builder loadBalancedWebClientBuilder() {
		return WebClient.builder();
	}

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
    }

	@Bean
	RouterFunction<ServerResponse> router(RequestHandler requestHandler) {
		return route()
				.GET("/organization-fallback", requestHandler::organizationFallbackHandler)
				.GET("/licensing-fallback", requestHandler::licenseFallbackHandler)
				.GET("/user", requestHandler::userHandler)
				.onError(Exception.class,
						(e, request) -> ServerResponse.badRequest().build())
				.build();
	}

	@Component
	static class RequestHandler {

		private static final String SERVICE_UNAVAILABLE_REPLY = """
		Either because we're updating the site or because someone spilled coffee
		on it again. We'll be back just as soon as we finish update or clean up
		the coffee.
		""";

		public Mono<ServerResponse> organizationFallbackHandler(ServerRequest serverRequest) {
			return ServerResponse.ok()
					.body(Mono.just("Sorry, organization service is not available" + System.lineSeparator() + SERVICE_UNAVAILABLE_REPLY), String.class);
		}

		public Mono<ServerResponse> licenseFallbackHandler(ServerRequest serverRequest) {
			return ServerResponse.ok()
					.body(Mono.just("Sorry, licensing service is not available" + System.lineSeparator() + SERVICE_UNAVAILABLE_REPLY), String.class);
		}

		public Mono<ServerResponse> userHandler(ServerRequest serverRequest) {
			return ServerResponse.ok()
					.body(serverRequest.principal(), Principal.class);
		}
	}
}

