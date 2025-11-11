package com.techtorque.admin_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import reactor.core.publisher.Mono;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Configuration for WebClient to communicate with other microservices
 */
@Configuration
public class WebClientConfig {

    @Value("${services.auth.url:http://localhost:8081}")
    private String authServiceUrl;

    @Value("${services.payment.url:http://localhost:8086}")
    private String paymentServiceUrl;

    @Value("${services.appointment.url:http://localhost:8083}")
    private String appointmentServiceUrl;

    @Value("${services.project.url:http://localhost:8084}")
    private String projectServiceUrl;

    @Value("${services.time-logging.url:http://localhost:8085}")
    private String timeLoggingServiceUrl;

    @Value("${services.vehicle.url:http://localhost:8082}")
    private String vehicleServiceUrl;

    /**
     * Exchange filter function to propagate JWT token from incoming request to outgoing WebClient calls
     */
    private ExchangeFilterFunction jwtTokenPropagationFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    return Mono.just(
                        org.springframework.web.reactive.function.client.ClientRequest
                            .from(clientRequest)
                            .header(HttpHeaders.AUTHORIZATION, authHeader)
                            .build()
                    );
                }
            }
            return Mono.just(clientRequest);
        });
    }

    @Bean(name = "authServiceWebClient")
    public WebClient authServiceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(authServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .filter(jwtTokenPropagationFilter())
                .build();
    }

    @Bean(name = "paymentServiceWebClient")
    public WebClient paymentServiceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(paymentServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .filter(jwtTokenPropagationFilter())
                .build();
    }

    @Bean(name = "appointmentServiceWebClient")
    public WebClient appointmentServiceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(appointmentServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .filter(jwtTokenPropagationFilter())
                .build();
    }

    @Bean(name = "projectServiceWebClient")
    public WebClient projectServiceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(projectServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .filter(jwtTokenPropagationFilter())
                .build();
    }

    @Bean(name = "timeLoggingServiceWebClient")
    public WebClient timeLoggingServiceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(timeLoggingServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .filter(jwtTokenPropagationFilter())
                .build();
    }

    @Bean(name = "vehicleServiceWebClient")
    public WebClient vehicleServiceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(vehicleServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .filter(jwtTokenPropagationFilter())
                .build();
    }
}
