package com.example.footballclubmanagement.service; // paketi layihənə uyğun tənzimlə

//import com.example.footballclubmanagement.dto.*;
//import com.example.dto.response.TeamMemberResponseDto;
//import com.example.entity.Department;
//import com.example.entity.TeamMember;
//import com.example.exception.ResourceNotFoundException;
//import com.example.repository.DepartmentRepository;
//import com.example.repository.TeamMemberRepository;
import com.example.footballclubmanagement.dto.request.TeamMemberCreateDto;
import com.example.footballclubmanagement.dto.response.TeamMemberResponseDto;
import com.example.footballclubmanagement.entity.Department;
import com.example.footballclubmanagement.entity.TeamMember;
import com.example.footballclubmanagement.exception.ResourceNotFoundException;
import com.example.footballclubmanagement.repository.DepartmentRepository;
import com.example.footballclubmanagement.repository.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    private TeamMemberResponseDto mapToResponseDto(TeamMember member) {
        TeamMemberResponseDto dto = new TeamMemberResponseDto();
        dto.setId(member.getId());
        dto.setFirstName(member.getFirstName());
        dto.setLastName(member.getLastName());
        dto.setEmail(member.getEmail());
        dto.setRole(member.getRole());
        dto.setSalary(member.getSalary());
        dto.setDepartmentName(member.getDepartment().getName());
        return dto;
    }
}