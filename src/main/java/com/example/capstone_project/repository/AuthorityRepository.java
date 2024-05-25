package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    @Query("select authority from Authority authority " +
            "join authority.roleAuthorities roleAuthority " +
            "join roleAuthority.role role " +
            "where role.code like %:roleCode%")
    List<Authority> findAuthoritiesOfRole(String roleCode);

    @Query("select authority from Authority authority " +
            "join authority.roleAuthorities roleAuthority " +
            "where roleAuthority.id = :roleId")
    List<Authority> findAuthoritiesOfRole(long roleId);
}
