// ========================================
// PaginatedResponse.java
// ========================================

package com.techtorque.admin_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Generic paginated response wrapper
 * Used by: GET endpoints with pagination
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginatedResponse<T> {
    private List<T> data;
    private Integer page;
    private Integer limit;
    private Long total;
    private Integer totalPages;
    private Boolean hasNext;
    private Boolean hasPrevious;
}