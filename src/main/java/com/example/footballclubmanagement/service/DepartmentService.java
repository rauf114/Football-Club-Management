package com.example.footballclubmanagement.service;

import com.example.footballclubmanagement.dto.request.DepartmentRequestDto;
import com.example.footballclubmanagement.dto.response.DepartmentResponseDto;
import com.example.footballclubmanagement.entity.Department;
import com.example.footballclubmanagement.exception.ResourceNotFoundException;
import com.example.footballclubmanagement.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentResponseDto createDepartment(DepartmentRequestDto dto) {
        Department department = Department.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        Department saved = departmentRepository.save(department);
        return mapToResponseDto(saved);
    }

    public List<DepartmentResponseDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public DepartmentResponseDto getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return mapToResponseDto(department);
    }

    private DepartmentResponseDto mapToResponseDto(Department department) {
        DepartmentResponseDto dto = new DepartmentResponseDto();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setDescription(department.getDescription());
        return dto;
    }


    public DepartmentResponseDto updateDepartment(Long id, DepartmentRequestDto dto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));

        department.setName(dto.getName());
        department.setDescription(dto.getDescription());

        Department updated = departmentRepository.save(department);
        return mapToResponseDto(updated);
    }

    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }
}