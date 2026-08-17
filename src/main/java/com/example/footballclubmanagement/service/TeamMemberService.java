package com.example.footballclubmanagement.service;

import com.example.footballclubmanagement.dto.request.TeamMemberCreateDto;
import com.example.footballclubmanagement.dto.response.TeamMemberResponseDto;
import com.example.footballclubmanagement.entity.Department;
import com.example.footballclubmanagement.entity.TeamMember;
import com.example.footballclubmanagement.exception.ResourceNotFoundException;
import com.example.footballclubmanagement.repository.DepartmentRepository;
import com.example.footballclubmanagement.repository.TeamMemberRepository;
import com.example.footballclubmanagement.specification.TeamMemberSpecification;
import com.example.footballclubmanagement.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamMemberService {

    private final TeamMemberRepository teamMemberRepository;
    private final DepartmentRepository departmentRepository;

    public TeamMemberResponseDto createMember(TeamMemberCreateDto dto) {
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + dto.getDepartmentId()));

        TeamMember member = TeamMember.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .role(dto.getRole())
                .salary(dto.getSalary())
                .department(department)
                .build();

        TeamMember saved = teamMemberRepository.save(member);
        return mapToResponseDto(saved);
    }

    public TeamMemberResponseDto getMemberById(Long id) {
        TeamMember member = teamMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team member not found with id: " + id));
        return mapToResponseDto(member);
    }

    public Page<TeamMemberResponseDto> getAllMembers(Pageable pageable) {
        return teamMemberRepository.findAll(pageable).map(this::mapToResponseDto);
    }

    public TeamMemberResponseDto updateMember(Long id, TeamMemberCreateDto dto) {
        TeamMember member = teamMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team member not found with id: " + id));

        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + dto.getDepartmentId()));

        member.setFirstName(dto.getFirstName());
        member.setLastName(dto.getLastName());
        member.setEmail(dto.getEmail());
        member.setRole(dto.getRole());
        member.setSalary(dto.getSalary());
        member.setDepartment(department);

        TeamMember updated = teamMemberRepository.save(member);
        return mapToResponseDto(updated);
    }

    public void deleteMember(Long id) {
        TeamMember member = teamMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Team member not found with id: " + id));
        teamMemberRepository.delete(member);
    }

    @Transactional(rollbackFor = Exception.class)
    public List<TeamMemberResponseDto> createMembersInBatch(List<TeamMemberCreateDto> dtos) {
        List<TeamMemberResponseDto> createdMembers = new ArrayList<>();

        for (TeamMemberCreateDto dto : dtos) {
            if (dto.getEmail().contains("error")) {
                throw new RuntimeException("Simulated exception for transactional rollback test!");
            }

            TeamMemberResponseDto created = createMember(dto);
            createdMembers.add(created);
        }

        return createdMembers;
    }

    @Transactional(rollbackFor = Exception.class)
    public TeamMemberResponseDto transferMemberToDepartment(Long memberId, Long newDepartmentId) {
        TeamMember member = teamMemberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Team member not found with id: " + memberId));

        Department newDepartment = departmentRepository.findById(newDepartmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with id: " + newDepartmentId));

        member.setDepartment(newDepartment);
        TeamMember updatedMember = teamMemberRepository.save(member);

        newDepartment.setDescription(newDepartment.getDescription() + " (Last transfer processed for member: " + member.getId() + ")");
        departmentRepository.save(newDepartment);

        return mapToResponseDto(updatedMember);
    }

    public Page<TeamMemberResponseDto> searchMembers(
            String firstName, String lastName, Role role, Long departmentId,
            BigDecimal minSalary, BigDecimal maxSalary, Pageable pageable) {

        if (minSalary != null && maxSalary != null && minSalary.compareTo(maxSalary) > 0) {
            throw new IllegalArgumentException("minSalary cannot be greater than maxSalary");
        }

        Specification<TeamMember> spec = TeamMemberSpecification.filterMembers(
                firstName, lastName, role, departmentId, minSalary, maxSalary);

        return teamMemberRepository.findAll(spec, pageable)
                .map(this::mapToResponseDto);
    }

    private TeamMemberResponseDto mapToResponseDto(TeamMember member) {
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