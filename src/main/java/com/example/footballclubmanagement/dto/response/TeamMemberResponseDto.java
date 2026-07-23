package com.example.footballclubmanagement.dto.response;

import com.example.footballclubmanagement.util.Role;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TeamMemberResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private BigDecimal salary;
    private String departmentName;
}