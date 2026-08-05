package com.example.footballclubmanagement.controller;

import com.example.footballclubmanagement.dto.request.DepartmentRequestDto;
import com.example.footballclubmanagement.dto.response.ApiResponse;
import com.example.footballclubmanagement.dto.response.DepartmentResponseDto;
import com.example.footballclubmanagement.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Tag(name = "Department Controller", description = "Endpoints for managing departments within the football club")
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    @Operation(summary = "Create a new department", description = "Creates a department with the provided details. Requires ADMIN role.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Department created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
    })
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> createDepartment(@Valid @RequestBody DepartmentRequestDto dto) {
        DepartmentResponseDto created = departmentService.createDepartment(dto);
        return new ResponseEntity<>(
                ApiResponse.success(created, "Department created successfully", HttpStatus.CREATED),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @Operation(summary = "Get all departments", description = "Retrieves a list of all existing departments. Accessible by USER and ADMIN.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> getAllDepartments() {
        List<DepartmentResponseDto> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success(departments));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get department by ID", description = "Retrieves details of a specific department by its ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Department found successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Department not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required")
    })
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> getDepartmentById(@PathVariable Long id) {
        DepartmentResponseDto department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(ApiResponse.success(department));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update department", description = "Updates an existing department's details by ID. Requires ADMIN role.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Department updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Department not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
    })
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequestDto dto) {
        DepartmentResponseDto updated = departmentService.updateDepartment(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updated, "Department updated successfully", HttpStatus.OK));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete department", description = "Deletes a department by ID. Requires ADMIN role.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Department deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Department not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
    })
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Department deleted successfully")
                .responseStatus(HttpStatus.OK.value())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}