package com.example.footballclubmanagement.controller;

import com.example.footballclubmanagement.dto.request.TeamMemberCreateDto;
import com.example.footballclubmanagement.dto.response.ApiResponse;
import com.example.footballclubmanagement.dto.response.TeamMemberResponseDto;
import com.example.footballclubmanagement.service.TeamMemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
@Tag(name = "Team Member Controller", description = "Endpoints for managing club members (players, staff, coaches)")
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    @PostMapping
    @Operation(summary = "Create a new team member", description = "Adds a new team member. Requires ADMIN role.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Team member created successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
    })
    public ResponseEntity<ApiResponse<TeamMemberResponseDto>> createMember(@Valid @RequestBody TeamMemberCreateDto dto) {
        TeamMemberResponseDto createdMember = teamMemberService.createMember(dto);
        return new ResponseEntity<>(
                ApiResponse.success(createdMember, "Team member created successfully", HttpStatus.CREATED),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get team member by ID", description = "Retrieves details of a specific team member by ID.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Team member found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Team member not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<TeamMemberResponseDto>> getMemberById(@PathVariable Long id) {
        TeamMemberResponseDto member = teamMemberService.getMemberById(id);
        return ResponseEntity.ok(ApiResponse.success(member));
    }

    @GetMapping
    @Operation(summary = "Get all team members with pagination", description = "Retrieves paginated and sorted list of team members.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<ApiResponse<Page<TeamMemberResponseDto>>> getAllMembers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<TeamMemberResponseDto> members = teamMemberService.getAllMembers(pageable);
        return ResponseEntity.ok(ApiResponse.success(members));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update team member", description = "Updates an existing team member by ID. Requires ADMIN role.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Team member updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Team member not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
    })
    public ResponseEntity<ApiResponse<TeamMemberResponseDto>> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody TeamMemberCreateDto dto) {
        TeamMemberResponseDto updatedMember = teamMemberService.updateMember(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updatedMember));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete team member", description = "Deletes a team member by ID. Requires ADMIN role.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Team member deleted successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Team member not found"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Forbidden - Requires ADMIN role")
    })
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable Long id) {
        teamMemberService.deleteMember(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Team member deleted successfully")
                .responseStatus(HttpStatus.OK.value())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}