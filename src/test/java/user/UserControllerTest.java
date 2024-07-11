package user;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.capstone_project.config.JacksonConfig;
import com.example.capstone_project.controller.UserController;
import com.example.capstone_project.controller.body.user.create.CreateUserBody;
import com.example.capstone_project.controller.responses.plan.UserResponse;
import com.example.capstone_project.controller.responses.user.detail.UserDetailResponse;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.service.impl.UserServiceImpl;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;

import com.example.capstone_project.utils.mapper.user.create.CreateUserBodyMapperImpl;
import com.example.capstone_project.utils.mapper.user.detail.DetailUserResponseMapperImpl;

import com.example.capstone_project.utils.mapper.user.list.ListUserResponseMapperImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
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

    //USER LIST TEST
    @Test
    public void testGetAllUsers() throws Exception {

        List<User> userList = Arrays.asList(user, user2);

        when(userServiceImpl.getAllUsers(anyString(), any(Pageable.class)))
                .thenReturn(userList);

        when(userServiceImpl.countDistinct(anyString()))
                .thenReturn((long) userList.size());

        // Perform the GET request
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/api/user")
                        .param("query", "")
                        .param("page", "1")
                        .param("size", "1")
                        .param("sortBy", "username")
                        .param("sortType", "asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.length()").value(userList.size()))
                .andExpect(jsonPath("$.pagination.totalRecords").value(userList.size()))
                .andExpect(jsonPath("$.pagination.numPages").value(2))
                .andExpect(jsonPath("$.data[0].userId").value(user.getId()))
                .andExpect(jsonPath("$.data[0].username").value(user.getUsername()))
                .andExpect(jsonPath("$.data[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$.data[0].role.id").value(user.getRole().getId()))
                .andExpect(jsonPath("$.data[0].role.code").value(user.getRole().getCode()))
                .andExpect(jsonPath("$.data[0].role.name").value(user.getRole().getName()))
                .andExpect(jsonPath("$.data[0].department.id").value(user.getDepartment().getId()))
                .andExpect(jsonPath("$.data[0].department.name").value(user.getDepartment().getName()))
                .andExpect(jsonPath("$.data[0].position.id").value(user.getPosition().getId()))
                .andExpect(jsonPath("$.data[0].position.name").value(user.getPosition().getName()))
                .andExpect(jsonPath("$.data[0].deactivate").value(user.getIsDelete()))
                .andExpect(jsonPath("$.data[1].userId").value(user2.getId()))
                .andExpect(jsonPath("$.data[1].username").value(user2.getUsername()))
                .andExpect(jsonPath("$.data[1].email").value(user2.getEmail()))
                .andExpect(jsonPath("$.data[1].role.id").value(user2.getRole().getId()))
                .andExpect(jsonPath("$.data[1].role.code").value(user2.getRole().getCode()))
                .andExpect(jsonPath("$.data[1].role.name").value(user2.getRole().getName()))
                .andExpect(jsonPath("$.data[1].department.id").value(user2.getDepartment().getId()))
                .andExpect(jsonPath("$.data[1].department.name").value(user2.getDepartment().getName()))
                .andExpect(jsonPath("$.data[1].position.id").value(user2.getPosition().getId()))
                .andExpect(jsonPath("$.data[1].position.name").value(user2.getPosition().getName()))
                .andExpect(jsonPath("$.data[1].deactivate").value(user2.getIsDelete()));
    }

}



