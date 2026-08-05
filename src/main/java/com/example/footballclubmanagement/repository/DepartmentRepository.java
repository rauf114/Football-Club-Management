package com.example.footballclubmanagement.repository;

import com.example.footballclubmanagement.entity.Department;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @EntityGraph(attributePaths = {"members"})
    @Query("SELECT d FROM Department d")
    List<Department> findAllWithMembers();

    @EntityGraph(attributePaths = {"members"})
    Optional<Department> findWithMembersById(Long id);
}