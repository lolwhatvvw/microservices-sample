package com.vvw.licensingservice;

import com.vvw.licensingservice.events.handler.OrganizationChangeHandler;
import com.vvw.licensingservice.events.model.OrganizationChangeModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.server.resource.web.reactive.function.client.ServletBearerExchangeFilterFunction;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class LicensingServiceApplication {

	public static void main(String[] args) {
        SpringApplication.run(LicensingServiceApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder().filter(new ServletBearerExchangeFilterFunction());
    }

    @Bean
    @LoadBalanced
    RestTemplate loadBalancedRestTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    Consumer<OrganizationChangeModel> organizationChangeState(OrganizationChangeHandler organizationChangeHandler) {
        return organizationChangeHandler::loggerSink;
    }
}