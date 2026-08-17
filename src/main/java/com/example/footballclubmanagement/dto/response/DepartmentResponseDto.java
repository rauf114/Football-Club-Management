package com.example.footballclubmanagement.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class DepartmentResponseDto {
    private Long id;
    private String name;
    private String description;
    private List<TeamMemberResponseDto> members;
}