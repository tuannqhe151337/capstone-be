package com.example.capstone_project.securityContextMock;

import com.example.capstone_project.entity.Authority;
import com.example.capstone_project.repository.AuthorityRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserDetails principal = User.builder()
                .username(customUser.username())
                .password("password")
                .authorities(AuthorityUtils.createAuthorityList(customUser.roles()))
                .build();
        Authentication auth = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
