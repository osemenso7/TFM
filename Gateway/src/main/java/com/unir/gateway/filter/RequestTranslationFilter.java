package com.unir.gateway.filter;

import com.unir.gateway.decorator.RequestDecoratorFactory;
import com.unir.gateway.model.GatewayRequest;
import com.unir.gateway.utils.RequestBodyExtractor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * This class is a custom filter for the Spring Cloud Gateway. It is responsible for translating incoming requests.
 * For JSON requests it uses the RequestBodyExtractor to extract the body of the request and the RequestDecoratorFactory to create a decorator for the request.
 * The decorator is used to modify the request before it is forwarded to the downstream service.
 * Multipart requests are forwarded without modifying their body.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RequestTranslationFilter implements GlobalFilter {

    private final RequestBodyExtractor requestBodyExtractor;
    private final RequestDecoratorFactory requestDecoratorFactory;

    /**
     * This method is the main filter method for the Spring Cloud Gateway.
     * It checks if the incoming request has a content type and is a POST request.
     * JSON requests are processed using RequestBodyExtractor and RequestDecoratorFactory.
     * Multipart requests are forwarded without modifying their body.
     *
     * @param exchange the current server web exchange
     * @param chain the gateway filter chain
     * @return a Mono<Void> that indicates when request handling is complete
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        MediaType contentType = exchange.getRequest().getHeaders().getContentType();

        // Simple check to see if the request has a content type and is a POST request
        if ((contentType == null) || !exchange.getRequest().getMethod().equals(HttpMethod.POST)) {
            log.info("Request does not have a content type or is not a POST request");
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

            return exchange.getResponse().setComplete();
        }

        if (MediaType.MULTIPART_FORM_DATA.isCompatibleWith(contentType)) {
            return handleMultipart(exchange, chain);
        }

        return DataBufferUtils.join(exchange.getRequest().getBody())
                .flatMap(dataBuffer -> {
                    GatewayRequest request = requestBodyExtractor.getRequest(exchange, dataBuffer);
                    ServerHttpRequest mutatedRequest = requestDecoratorFactory.getDecorator(request);
                    //RouteToRequestUrlFilter writes the URI to the exchange attributes *before* any global filters run.
                    exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, mutatedRequest.getURI());

                    if(request.getQueryParams() != null) {
                        request.getQueryParams().clear();
                    }

                    log.info("Proxying request: {} {}", mutatedRequest.getMethod(), mutatedRequest.getURI());

                    return chain.filter(exchange.mutate().request(mutatedRequest).build());
                });
    }

    private Mono<Void> handleMultipart(ServerWebExchange exchange, GatewayFilterChain chain) {
        String targetMethod = exchange.getRequest()
                .getHeaders()
                .getFirst("Target-Method");

        if (targetMethod == null || !targetMethod.equalsIgnoreCase("POST")) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);

            return exchange.getResponse().setComplete();
        }

        ServerHttpRequest request = exchange.getRequest()
                .mutate()
                .headers(headers -> headers.remove("Target-Method"))
                .build();

        return chain.filter(exchange.mutate().request(request).build());
    }
}
