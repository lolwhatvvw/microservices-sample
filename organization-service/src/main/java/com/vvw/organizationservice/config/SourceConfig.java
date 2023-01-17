package com.vvw.organizationservice.config;

import com.vvw.organizationservice.events.model.OrganizationChangeModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Supplier;

@Configuration
public class SourceConfig {

	@Bean
	public Sinks.Many<OrganizationChangeModel> organizationSinks(){
		return Sinks.many().multicast().onBackpressureBuffer();
	}

	@Bean
	public Supplier<Flux<OrganizationChangeModel>> organizationSupplier(Sinks.Many<OrganizationChangeModel> sinks){
		return sinks::asFlux;
	}
}
