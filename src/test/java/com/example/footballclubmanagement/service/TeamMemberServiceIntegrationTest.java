package com.example.footballclubmanagement.service;

import com.example.footballclubmanagement.dto.request.TeamMemberCreateDto;
import com.example.footballclubmanagement.entity.Department;
import com.example.footballclubmanagement.repository.DepartmentRepository;
import com.example.footballclubmanagement.repository.TeamMemberRepository;
import com.example.footballclubmanagement.util.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeamMemberServiceIntegrationTest {

    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private TeamMemberRepository teamMemberRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Long departmentId;

    @BeforeEach
    void setUp() {
        teamMemberRepository.deleteAll();
        departmentRepository.deleteAll();

        Department department = Department.builder()
                .name("First Team")
                .description("Main Football Team")
                .build();
        Department savedDept = departmentRepository.save(department);
        departmentId = savedDept.getId();
    }

    @Test
    @DisplayName("Rollback Test: No data should be written to the DB at the time of Exception")    void createMembersInBatch_ShouldRollbackOnException() {
        TeamMemberCreateDto validDto = new TeamMemberCreateDto();
        validDto.setFirstName("Jose");
        validDto.setLastName("Mourinho");
        validDto.setEmail("jose@club.com");
        validDto.setRole(Role.MANAGER);
        validDto.setSalary(new BigDecimal("100000"));
        validDto.setDepartmentId(departmentId);

        TeamMemberCreateDto invalidDto = new TeamMemberCreateDto();
        invalidDto.setFirstName("Fail");
        invalidDto.setLastName("User");
        invalidDto.setEmail("fail.error@club.com");
        invalidDto.setRole(Role.MANAGER);
        invalidDto.setSalary(new BigDecimal("50000"));
        invalidDto.setDepartmentId(departmentId);

        List<TeamMemberCreateDto> batch = List.of(validDto, invalidDto);

        assertThrows(RuntimeException.class, () -> teamMemberService.createMembersInBatch(batch));

        assertEquals(0, teamMemberRepository.count(), "There should be no members left in the database because the rollback occurred!");}}