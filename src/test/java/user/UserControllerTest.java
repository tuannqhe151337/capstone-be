package user;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.capstone_project.config.JacksonConfig;
import com.example.capstone_project.controller.UserController;
import com.example.capstone_project.controller.body.user.create.CreateUserBody;

import com.example.capstone_project.controller.responses.user.detail.UserDetailResponse;
import com.example.capstone_project.controller.responses.user.list.UserResponse;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.service.impl.UserServiceImpl;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;

import com.example.capstone_project.utils.mapper.user.create.CreateUserBodyMapperImpl;
import com.example.capstone_project.utils.mapper.user.detail.DetailUserResponseMapperImpl;

import com.example.capstone_project.utils.mapper.user.list.ListUserResponseMapperImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
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


    private final ObjectMapper objectMapper = new JacksonConfig().objectMapper();


    private Long userId;
    private Long actorId;

    private User user;
    private User user2;

    private Position position;
    private Department department;
    private Role role;
    @MockBean
    private CreateUserBodyMapperImpl createUserBodyMapper = new CreateUserBodyMapperImpl();

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

        user2 = User.builder()
                .id(2L)
                .username("username2")
                .fullName("NutNUnu")
                .password("password")
                .role(role)
                .department(department)
                .position(position)
                .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0, 0))
                .email("mailho0021@gmail.com")
                .address("Ha Noi")
                .isDelete(false)
                .phoneNumber("0999965877")
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
    @Test
    public void testCreateUser_Success() throws Exception {

        // Mock user input
        CreateUserBody userBody = new CreateUserBody();
        userBody.setFullName("Nutalomlok Nunu");
        userBody.setPhoneNumber("0990900099");
        userBody.setEmail("giangdvhe16317888@fpt.edu.vn");
        userBody.setDepartmentId(1L);
        userBody.setPositionId(1L);
        userBody.setRoleId(1L);
        userBody.setDob(LocalDateTime.of(2002, 11, 11, 0, 0));
        userBody.setAddress("BAC GIANG");


        String content = objectMapper.writeValueAsString(userBody); //object to json


        // Mock UserService behavior
        doNothing().when(userServiceImpl).createUser(any(User.class));

        // Perform POST request
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))  //Json
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    @Test
    public void testCreateUser_Unauthorized() throws Exception {
        // Mock user input
        CreateUserBody userBody = new CreateUserBody();
        // Mock UserService behavior
        doNothing().when(userServiceImpl).createUser(any(User.class));
        // Mocking the service method to throw exception
        doThrow(new UnauthorizedException("Unauthorized")).when(userServiceImpl).createUser(any(User.class));


        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userBody)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testCreateUser_EmailAlreadyExists() throws Exception {
        // Mock user input
        CreateUserBody userBody = new CreateUserBody();

        // Mock UserService behavior
        doNothing().when(userServiceImpl).createUser(any(User.class));

        // Mocking the service method to throw exception
        doThrow(new DataIntegrityViolationException("Email already exists")).when(userServiceImpl).createUser(any(User.class));

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.field").value("email"))
                .andExpect(jsonPath("$.message").value("emails already exists"));
    }





}