package com.example.footballclubmanagement.service;

import com.example.footballclubmanagement.dto.request.TeamMemberCreateDto;
import com.example.footballclubmanagement.dto.response.TeamMemberResponseDto;
import com.example.footballclubmanagement.entity.Department;
import com.example.footballclubmanagement.util.Role;
import com.example.footballclubmanagement.entity.TeamMember;
import com.example.footballclubmanagement.repository.DepartmentRepository;
import com.example.footballclubmanagement.repository.TeamMemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamMemberServiceTest {

    @Mock
    private TeamMemberRepository teamMemberRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private TeamMemberService teamMemberService;

    @Test
    void createMember_Success() {
        Department department = Department.builder().id(1L).name("First Team").build();
        TeamMemberCreateDto dto = new TeamMemberCreateDto();
        dto.setFirstName("Jose");
        dto.setLastName("Mourinho");
        dto.setEmail("jose@club.com");
        dto.setRole(Role.MANAGER);
        dto.setSalary(new BigDecimal("100000"));
        dto.setDepartmentId(1L);

        TeamMember savedMember = TeamMember.builder()
                .id(1L)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .role(dto.getRole())
                .salary(dto.getSalary())
                .department(department)
                .build();

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(department));
        when(teamMemberRepository.save(any(TeamMember.class))).thenReturn(savedMember);

        TeamMemberResponseDto response = teamMemberService.createMember(dto);

        assertNotNull(response);
        assertEquals("Jose", response.getFirstName());
        assertEquals("First Team", response.getDepartmentName());
        verify(teamMemberRepository, times(1)).save(any(TeamMember.class));
    }
}