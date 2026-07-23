package com.example.footballclubmanagement.controller;

import com.example.footballclubmanagement.dto.response.ApiResponse;
import com.example.footballclubmanagement.dto.request.DepartmentRequestDto;
import com.example.footballclubmanagement.dto.response.DepartmentResponseDto;
import com.example.footballclubmanagement.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> createDepartment(@Valid @RequestBody DepartmentRequestDto dto) {
        DepartmentResponseDto created = departmentService.createDepartment(dto);
        return new ResponseEntity<>(
                ApiResponse.success(created, "Department created successfully", HttpStatus.CREATED),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponseDto>>> getAllDepartments() {
        List<DepartmentResponseDto> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.success(departments));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponseDto>> getDepartmentById(@PathVariable Long id) {
        DepartmentResponseDto department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(ApiResponse.success(department));
    }
}