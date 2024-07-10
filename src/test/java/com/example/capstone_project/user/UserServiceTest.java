package com.example.capstone_project.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import com.example.capstone_project.repository.UserRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.service.impl.UserServiceImpl;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.helper.UserHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.capstone_project.entity.User;
import com.example.capstone_project.entity.Position;
import com.example.capstone_project.entity.Department;
import com.example.capstone_project.entity.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserAuthorityRepository userAuthorityRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private Long userId;
    private Long actorId;
    private User user;
    private Position position;
    private Department department;
    private Role role;

    @BeforeEach
    void setUp() {
        userId = 1L;
        actorId = 2L;

        role = Role.builder()
                .id(1L)
                .code("admin")
                .name("Admin")
                .build();

        department = Department.builder()
                .id(1L)
                .name("Accounting department")
                .build();

        position = Position.builder()
                .id(1L)
                .name("Techlead")
                .build();

        user = User.builder()
                .id(userId)
                .username("username1")
                .fullName("Nutalomlok Nunu")
                .password("password")
                .role(role)
                .department(department)
                .position(position)
                .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                .email("mailho21@gmail.com")
                .address("Ha Noi")
                .isDelete(false)
                .phoneNumber("0999988877")
                .build();

        // Mock the SecurityContextHolder to return a valid user ID
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(actorId.toString());

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testGetUserById_Unauthorized() {
        when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
        when(userAuthorityRepository.get(actorId)).thenReturn(Set.of());

        Exception exception = assertThrows(UnauthorizedException.class, () -> {
            userServiceImpl.getUserById(userId);
        });

        assertEquals("Unauthorized to view user details", exception.getMessage());
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
        when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.VIEW_USER_DETAILS.getValue()));
        when(userRepository.findUserDetailedById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userServiceImpl.getUserById(userId);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testGetUserById_Success() throws Exception {
        when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
        when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.VIEW_USER_DETAILS.getValue()));
        when(userRepository.findUserDetailedById(userId)).thenReturn(Optional.of(user));

        User foundUser = userServiceImpl.getUserById(userId);

        assertEquals(userId, foundUser.getId());
        assertEquals("Nutalomlok Nunu", foundUser.getFullName());
        assertEquals("username1", foundUser.getUsername());
        assertEquals("password", foundUser.getPassword());
        assertEquals("mailho21@gmail.com", foundUser.getEmail());
        assertEquals(LocalDateTime.of(2002, 11, 11, 0, 0, 0), foundUser.getDob());
        assertEquals("Ha Noi", foundUser.getAddress());
        assertEquals("0999988877", foundUser.getPhoneNumber());
        assertEquals(position.getId(), foundUser.getPosition().getId());
        assertEquals(position.getName(), foundUser.getPosition().getName());
        assertEquals(department.getId(), foundUser.getDepartment().getId());
        assertEquals(department.getName(), foundUser.getDepartment().getName());
        assertEquals(role.getId(), foundUser.getRole().getId());
        assertEquals(role.getName(), foundUser.getRole().getName());
    }
}
