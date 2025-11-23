package com.techtorque.admin_service.service;

import com.techtorque.admin_service.dto.response.UserResponse;
import com.techtorque.admin_service.service.impl.AdminUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {

    @Mock
    private WebClient authServiceWebClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private AdminUserServiceImpl adminUserService;

    private UserResponse testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserResponse();
        testUser.setId(1L);
        testUser.setUserId("1");
        testUser.setUsername("john@test.com");
        testUser.setFullName("John Doe");
        testUser.setRole("CUSTOMER");
        testUser.setActive(true);
    }

    @Test
    void testGetAllUsers_Success() {
        when(authServiceWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(UserResponse.class)).thenReturn(Flux.just(testUser));

        List<UserResponse> users = adminUserService.getAllUsers(null, null, 0, 10);

        assertThat(users).isNotEmpty();
        assertThat(users.get(0).getUsername()).isEqualTo("john@test.com");
    }

    @Test
    void testGetAllUsers_WithFilters() {
        when(authServiceWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(UserResponse.class)).thenReturn(Flux.just(testUser));

        List<UserResponse> users = adminUserService.getAllUsers("CUSTOMER", true, 0, 10);

        assertThat(users).isNotEmpty();
        verify(authServiceWebClient, times(1)).get();
    }

    @Test
    void testGetAllUsers_Error() {
        when(authServiceWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenThrow(new RuntimeException("Connection failed"));

        assertThatThrownBy(() -> adminUserService.getAllUsers(null, null, 0, 10))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to fetch users");
    }

    @Test
    void testGetUserById_Success() {
        when(authServiceWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(UserResponse.class)).thenReturn(Flux.just(testUser));

        UserResponse user = adminUserService.getUserById("1");

        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isEqualTo("1");
    }

    @Test
    void testGetUserById_NotFound() {
        when(authServiceWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(anyString(), anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(UserResponse.class)).thenReturn(Flux.empty());

        assertThatThrownBy(() -> adminUserService.getUserById("999"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }
}
