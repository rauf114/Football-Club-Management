package com.example.footballclubmanagement.repository;

import com.example.footballclubmanagement.entity.TeamMember;
import com.example.footballclubmanagement.util.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long>, JpaSpecificationExecutor<TeamMember> {

    @Query("SELECT tm FROM TeamMember tm " +
            "JOIN tm.department d " +
            "WHERE d.id = :departmentId " +
            "AND tm.role = :role " +
            "AND tm.salary >= :minSalary " +
            "ORDER BY tm.salary DESC")
    List<TeamMember> findHighEarningMembersByDepartmentAndRole(
            @Param("departmentId") Long departmentId,
            @Param("role") Role role,
            @Param("minSalary") BigDecimal minSalary
    );

    @Query(value = "SELECT d.name AS departmentName, COUNT(m.id) AS totalMembers, AVG(m.salary) AS avgSalary " +
            "FROM team_members m " +
            "INNER JOIN departments d ON m.department_id = d.id " +
            "WHERE m.salary > :salaryThreshold " +
            "GROUP BY d.name " +
            "HAVING COUNT(m.id) >= :minMembersCount",
            nativeQuery = true)
    List<Object[]> getDepartmentSalaryStatsNative(
            @Param("salaryThreshold") BigDecimal salaryThreshold,
            @Param("minMembersCount") int minMembersCount
    );
}