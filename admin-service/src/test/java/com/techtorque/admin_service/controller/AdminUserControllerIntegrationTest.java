package com.techtorque.admin_service.controller;

import com.techtorque.admin_service.dto.response.UserResponse;
import com.techtorque.admin_service.service.AdminUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminUserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminUserService adminUserService;

    private UserResponse testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserResponse();
        testUser.setUserId("1");
        testUser.setUsername("john@test.com");
        testUser.setFullName("John Doe");
        testUser.setRole("CUSTOMER");
        testUser.setActive(true);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testListUsers_Success() throws Exception {
        when(adminUserService.getAllUsers(anyString(), any(), anyInt(), anyInt()))
                .thenReturn(Arrays.asList(testUser));

        mockMvc.perform(get("/admin/users")
                        .param("page", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());

        verify(adminUserService, atLeastOnce()).getAllUsers(any(), any(), anyInt(), anyInt());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetUserById_Success() throws Exception {
        when(adminUserService.getUserById(anyString())).thenReturn(testUser);

        mockMvc.perform(get("/admin/users/{userId}", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(adminUserService, times(1)).getUserById("1");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testListUsers_WithFilters() throws Exception {
        when(adminUserService.getAllUsers(anyString(), any(), anyInt(), anyInt()))
                .thenReturn(Arrays.asList(testUser));

        mockMvc.perform(get("/admin/users")
                        .param("role", "CUSTOMER")
                        .param("active", "true")
                        .param("page", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(adminUserService, atLeastOnce()).getAllUsers(any(), any(), anyInt(), anyInt());
    }
}
