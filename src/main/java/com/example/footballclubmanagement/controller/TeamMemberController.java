package com.example.footballclubmanagement.controller;

import com.example.footballclubmanagement.dto.request.TeamMemberCreateDto;
import com.example.footballclubmanagement.dto.response.ApiResponse;
import com.example.footballclubmanagement.dto.response.TeamMemberResponseDto;
import com.example.footballclubmanagement.service.TeamMemberService;
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
public class TeamMemberController {

    private final TeamMemberService teamMemberService;

    @PostMapping
    public ResponseEntity<ApiResponse<TeamMemberResponseDto>> createMember(@Valid @RequestBody TeamMemberCreateDto dto) {
        TeamMemberResponseDto createdMember = teamMemberService.createMember(dto);
        return new ResponseEntity<>(
                ApiResponse.success(createdMember, "Team member created successfully", HttpStatus.CREATED),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeamMemberResponseDto>> getMemberById(@PathVariable Long id) {
        TeamMemberResponseDto member = teamMemberService.getMemberById(id);
        return ResponseEntity.ok(ApiResponse.success(member));
    }

    @GetMapping
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
    public ResponseEntity<ApiResponse<TeamMemberResponseDto>> updateMember(
            @PathVariable Long id,
            @Valid @RequestBody TeamMemberCreateDto dto) {
        TeamMemberResponseDto updatedMember = teamMemberService.updateMember(id, dto);
        return ResponseEntity.ok(ApiResponse.success(updatedMember));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMember(@PathVariable Long id) {
        teamMemberService.deleteMember(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Team member deleted successfully")
                .responseStatus(HttpStatus.NO_CONTENT.value())
                .data(null)
                .build();
        return ResponseEntity.ok(response);
    }
}