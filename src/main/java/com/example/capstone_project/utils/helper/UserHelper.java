package com.example.capstone_project.utils.helper;

import com.example.capstone_project.utils.exception.UnauthorizedException;
import org.reactivestreams.Publisher;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserHelper {
    public static int getUserId() {
        if (SecurityContextHolder.getContext() != null
                && SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null
                && !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().isBlank()
        ) {
            return Integer.parseInt(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        } else {
            throw new UnauthorizedException("User unauthorized!");
        }
    }
}
