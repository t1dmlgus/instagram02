package com.s1dmlgus.instagram02.config.auth;

import com.s1dmlgus.instagram02.domain.user.Role;
import com.s1dmlgus.instagram02.domain.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;


public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User testUser = User.builder()
                .id(1L)
                .username("t1dmlgus")
                .email("test@gmail.com")
                .name("이의현")
                .build();

        testUser.setRole(Role.ROLE_USER);

        PrincipalDetails principal = new PrincipalDetails(testUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
        context.setAuthentication(authentication);


        return context;
    }
}
