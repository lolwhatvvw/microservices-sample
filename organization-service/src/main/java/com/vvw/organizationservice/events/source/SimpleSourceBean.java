package com.vvw.organizationservice.events.source;

import com.vvw.organizationservice.events.model.OrganizationChangeModel;
import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Sinks;

@Slf4j
@Component
@RequiredArgsConstructor
public class SimpleSourceBean {

	private final Tracer tracer;
	private final Sinks.Many<OrganizationChangeModel> sink;

	@Transactional
	public void publishOrganizationChange(Action action, String organizationId) {
	    ScopedSpan span = tracer.startScopedSpan("kafka");
		try {
			TraceContext context = span.context();
			log.debug("Trace id {} and span id {}",context.traceId(), context.spanId());
			var model = new OrganizationChangeModel(
					OrganizationChangeModel.class.getTypeName(),
					action.name(),
					context.traceId(),
					organizationId
			);
			sink.tryEmitNext(model);
		} catch (RuntimeException | Error e) {
			span.error(e);
			throw e;
		} finally {
			span.end();
		}
    }

    public enum Action {
        GET,
        CREATED,
        UPDATED,
        DELETED
    }
}
