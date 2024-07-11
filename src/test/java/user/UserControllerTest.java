package user;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.capstone_project.controller.UserController;
import com.example.capstone_project.controller.responses.plan.UserResponse;
import com.example.capstone_project.controller.responses.user.detail.UserDetailResponse;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.service.impl.UserServiceImpl;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.mapper.user.detail.DetailUserResponseMapperImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {


    private MockMvc mockMvc;

    @Mock
    private UserServiceImpl userServiceImpl;

    @InjectMocks
    private UserController userController;


    private ObjectMapper objectMapper = new ObjectMapper();
    private ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();


    private Long userId;
    private Long actorId;

    private User user;

    private Position position;
    private Department department;
    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        userId = 1L;
        actorId = 2L;

        // Mock the SecurityContextHolder to return a valid user ID
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(actorId.toString());

        SecurityContextHolder.setContext(securityContext);

        // Initialize the User object
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
                .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0, 0))
                .email("mailho21@gmail.com")
                .address("Ha Noi")
                .isDelete(false)
                .phoneNumber("0999988877")
                .build();
    }

    @Test
    void testGetUserById_Unauthorized() throws Exception {
        when(userServiceImpl.getUserById(userId)).thenThrow(new UnauthorizedException("Unauthorized to view user details"));

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/user/detail/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void testGetUserById_UserNotFound() throws Exception {
        when(userServiceImpl.getUserById(userId)).thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/user/detail/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetUserById_Success() throws Exception {
        UserDetailResponse userResponse = new DetailUserResponseMapperImpl().mapToUserDetail(user);
        when(userServiceImpl.getUserById(userId)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/user/detail/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(userId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(userResponse.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.fullName").value(userResponse.getFullName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(userResponse.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.dob").value(userResponse.getDob().toString() + ":00.000"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.note").value(userResponse.getNote()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value(userResponse.getPhoneNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.address").value(userResponse.getAddress()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.department.name").value(userResponse.getDepartment().getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.position.name").value(userResponse.getPosition().getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role.name").value(userResponse.getRole().getName()));
    }

    //create user test

}
