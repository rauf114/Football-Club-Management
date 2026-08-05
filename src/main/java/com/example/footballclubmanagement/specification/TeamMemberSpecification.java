package com.example.footballclubmanagement.specification;

import com.example.footballclubmanagement.entity.TeamMember;
import com.example.footballclubmanagement.util.Role;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class TeamMemberSpecification {

    public static Specification<TeamMember> hasFirstName(String firstName) {
        return (root, query, cb) -> firstName == null ? null :
                cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<TeamMember> hasLastName(String lastName) {
        return (root, query, cb) -> lastName == null ? null :
                cb.like(cb.lower(root.get("lastName")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<TeamMember> hasRole(Role role) {
        return (root, query, cb) -> role == null ? null : cb.equal(root.get("role"), role);
    }

    public static Specification<TeamMember> hasDepartmentId(Long departmentId) {
        return (root, query, cb) -> departmentId == null ? null :
                cb.equal(root.get("department").get("id"), departmentId);
    }

    public static Specification<TeamMember> salaryBetween(BigDecimal minSalary, BigDecimal maxSalary) {
        return (root, query, cb) -> {
            if (minSalary == null && maxSalary == null) return null;
            if (minSalary != null && maxSalary != null) return cb.between(root.get("salary"), minSalary, maxSalary);
            if (minSalary != null) return cb.greaterThanOrEqualTo(root.get("salary"), minSalary);
            return cb.lessThanOrEqualTo(root.get("salary"), maxSalary);
        };
    }
}