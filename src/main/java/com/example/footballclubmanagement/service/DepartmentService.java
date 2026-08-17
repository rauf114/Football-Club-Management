package com.example.footballclubmanagement.service;

import com.example.footballclubmanagement.dto.request.DepartmentRequestDto;
import com.example.footballclubmanagement.dto.response.DepartmentResponseDto;
import com.example.footballclubmanagement.dto.response.TeamMemberResponseDto;
import com.example.footballclubmanagement.entity.Department;
import com.example.footballclubmanagement.entity.TeamMember;
import com.example.footballclubmanagement.exception.ResourceNotFoundException;
import com.example.footballclubmanagement.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    @CacheEvict(value = "departments", allEntries = true)
    public DepartmentResponseDto createDepartment(DepartmentRequestDto dto) {
        Department department = Department.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        Department saved = departmentRepository.save(department);
        return mapToResponseDto(saved);
    }

    @Cacheable(value = "departments")
    public List<DepartmentResponseDto> getAllDepartments() {
        return departmentRepository.findAllWithMembers().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "department", key = "#id")
    public DepartmentResponseDto getDepartmentById(Long id) {
        Department department = departmentRepository.findWithMembersById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + id));
        return mapToResponseDto(department);
    }

    @CacheEvict(value = {"departments", "department"}, key = "#id", allEntries = true)
    public DepartmentResponseDto updateDepartment(Long id, DepartmentRequestDto dto) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + id));

        department.setName(dto.getName());
        department.setDescription(dto.getDescription());

        Department updated = departmentRepository.save(department);
        return mapToResponseDto(updated);
    }

    @CacheEvict(value = {"departments", "department"}, key = "#id", allEntries = true)
    public void deleteDepartment(Long id) {
        if (!departmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Department not found with id: " + id);
        }
        departmentRepository.deleteById(id);
    }

    private DepartmentResponseDto mapToResponseDto(Department department) {
        DepartmentResponseDto dto = new DepartmentResponseDto();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.getDescription();
        dto.setDescription(department.getDescription());

        if (department.getMembers() != null) {
            List<TeamMemberResponseDto> memberDtos = department.getMembers().stream()
                    .map(this::mapMemberToDto)
                    .collect(Collectors.toList());
            dto.setMembers(memberDtos);
        }

        return dto;
    }

    private TeamMemberResponseDto mapMemberToDto(TeamMember member) {
        TeamMemberResponseDto dto = new TeamMemberResponseDto();
        dto.setId(member.getId());
        dto.setFirstName(member.getFirstName());
        dto.setLastName(member.getLastName());
        dto.setEmail(member.getEmail());
        dto.setRole(member.getRole());
        dto.setSalary(member.getSalary());
        if (member.getDepartment() != null) {
            dto.setDepartmentName(member.getDepartment().getName());
        }
        return dto;
    }
}