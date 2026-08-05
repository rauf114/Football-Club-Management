package com.example.footballclubmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String opponentName;

    @Column(nullable = false)
    private LocalDateTime matchDate;

    private String stadium;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "match_team_members",
            joinColumns = @JoinColumn(name = "match_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    @ToString.Exclude
    private Set<TeamMember> members = new HashSet<>();
}