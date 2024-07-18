package user;

import com.example.capstone_project.entity.Department;
import com.example.capstone_project.entity.Position;
import com.example.capstone_project.entity.Role;
import com.example.capstone_project.entity.User;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.service.impl.MailRepository;
import com.example.capstone_project.service.impl.UserServiceImpl;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.helper.UserHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserAuthorityRepository userAuthorityRepository;

    @Mock
    private UserSettingRepository userSettingRepository;

    @Mock
    private MailRepository mailRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private RoleRepository roleRepository;

    private Long userId;
    private Long actorId;
    private Position position;
    private Department department;
    private Role role;
    private User user ;
    private User user2;
    private Pageable pageable;

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
        user2 = User.builder()
                .id(userId)
                .username("username2")
                .fullName("Nuto Nu")
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
        pageable = PageRequest.of(0, 10);

        // Mock the SecurityContextHolder to return a valid user ID
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(actorId.toString());

        SecurityContextHolder.setContext(securityContext);
    }

    //test get all user list

    @Test
    void testGetAllUsers_WithAuthority() {
        // Mock user authority check
        when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
        when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.VIEW_LIST_USERS.getValue()));

        // Mock repository response
        List<User> expectedUsers = Arrays.asList(user2);
        when(userRepository.getUserWithPagination(anyLong(), anyLong(), anyLong(), anyString(), any(Pageable.class)))
                .thenReturn(expectedUsers);

        // Test getAllUsers method
        List<User> actualUsers = userServiceImpl.getAllUsers(1L, 1L, 5L, "username2", pageable);

        // Verify repository method call
        verify(userRepository).getUserWithPagination(1L, 1L, 5L, "username2", pageable);

        // Assert the result
        assertEquals(expectedUsers, actualUsers);
    }










}
