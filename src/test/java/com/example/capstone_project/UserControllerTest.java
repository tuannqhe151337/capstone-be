package com.example.capstone_project;

import com.example.capstone_project.controller.UserController;


import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.plan.UserResponse;
import com.example.capstone_project.entity.Department;
import com.example.capstone_project.entity.Position;
import com.example.capstone_project.entity.Role;
import com.example.capstone_project.entity.User;
import com.example.capstone_project.repository.UserRepository;
import com.example.capstone_project.service.UserService;


import com.example.capstone_project.service.impl.UserServiceImpl;
import com.example.capstone_project.utils.mapper.user.list.ListUserResponseMapperImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@RunWith(SpringJUnit4ClassRunner.class)

public class UserControllerTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    //json convert to object
    ObjectWriter objectWriter = objectMapper.writer();



    @InjectMocks
    private UserServiceImpl userServiceImpl; // Inject the implementation class

    @Mock
    private UserRepository userRepository;

    private UserService userService;


    @InjectMocks
    private UserController userController;

    User user1 = User.builder()
            .id(1L)
            .username("username1")
            .fullName("Nutalomlok Nunu")
            .password(("password"))
            .role(Role.builder()
                    .code("admin")
                    .name("Admin")
                    .build())
            .department(Department.builder()
                    .name("Accounting department")
                    .build())
            .position(Position.builder()
                    .name("Techlead")
                    .build())
            .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
            .email("mailho21@gmail.com")
            .address("Ha Noi ")
            .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
            .isDelete(false)
            .phoneNumber("0999988877")
            .build();
    User user2 = User.builder()
            .id(2L)
            .username("username2")
            .fullName("Nutalomlok Nunu2")
            .password(("password"))
            .role(Role.builder()
                    .code("admin")
                    .name("Admin")
                    .build())
            .department(Department.builder()
                    .name("Accounting department")
                    .build())
            .position(Position.builder()
                    .name("Techlead")
                    .build())
            .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
            .email("mailho2111@gmail.com")
            .address("Ha Noiiii ")
            .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
            .isDelete(false)
            .phoneNumber("0999988855")
            .build();

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.userService = userServiceImpl;
    // Assign the implementation to the interface type
        this.mockMvc = MockMvcBuilders.standaloneSetup(userServiceImpl).build();
    }


    @Test
    @WithMockUser
    public void getUserById_success() throws Exception {
  Mockito.when(userService.getUserById(user1.getId())).thenReturn(user1);

  mockMvc.perform(MockMvcRequestBuilders.
          get("http://localhost:8080/api/user/detail/" + user1.getId())
          .contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(user1.getId()))
          .andExpect(jsonPath("$.username").value(user1.getUsername()))
          .andExpect(jsonPath("$.fullName").value(user1.getFullName()))
          .andExpect(jsonPath("$.email").value(user1.getEmail()))
          .andExpect(jsonPath("$.department").value(user1.getDepartment()))
          .andExpect(jsonPath("$.position").value(user1.getPosition()))
          .andExpect(jsonPath("$.role").value(user1.getRole()))
          .andExpect(jsonPath("$.dob").value(user1.getDob()))
      ;


    }



}
