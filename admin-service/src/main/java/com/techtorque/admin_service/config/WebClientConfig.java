package com.techtorque.admin_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

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

    @Bean(name = "authServiceWebClient")
    public WebClient authServiceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(authServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean(name = "paymentServiceWebClient")
    public WebClient paymentServiceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(paymentServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean(name = "appointmentServiceWebClient")
    public WebClient appointmentServiceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(appointmentServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean(name = "projectServiceWebClient")
    public WebClient projectServiceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(projectServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean(name = "timeLoggingServiceWebClient")
    public WebClient timeLoggingServiceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(timeLoggingServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Bean(name = "vehicleServiceWebClient")
    public WebClient vehicleServiceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(vehicleServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
