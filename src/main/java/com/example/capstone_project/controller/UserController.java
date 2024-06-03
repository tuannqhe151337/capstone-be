package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.user.create.CreateUserBody;
import com.example.capstone_project.controller.body.user.edit.EditUserBody;
import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;

import com.example.capstone_project.controller.responses.user.list.UserResponse;
import com.example.capstone_project.controller.responses.user.create.UserDetailResponse;
import com.example.capstone_project.entity.Department;
import com.example.capstone_project.entity.Position;
import com.example.capstone_project.entity.Role;
import com.example.capstone_project.entity.User;
import com.example.capstone_project.service.UserService;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.user.create.CreateUserBodyMapperImpl;
import com.example.capstone_project.utils.mapper.user.detail.DetailUserResponseMapperImpl;
import com.example.capstone_project.utils.mapper.user.edit.EditUserResponseEntityMapperImpl;
import com.example.capstone_project.utils.mapper.user.list.ListUserResponseMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ListResponse<UserResponse>> getAllUsers(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {
        // Handling page and pageSize
        Integer pageInt = PaginationHelper.convertPageToInteger(page);
        Integer sizeInt = PaginationHelper.convertPageSizeToInteger(size);

        // Handling query
        if (query == null) {
            query = "";
        }

        // Handling pagination
        Pageable pageable = PaginationHelper.handlingPagination(query, pageInt, sizeInt, sortBy, sortType);

        // Get data
        List<User> users = userService.getAllUsers(query, pageable);

        long count = this.userService.countDistinct(query);

        // Response
        ListResponse<UserResponse> response = new ListResponse<>();

        if (users != null && !users.isEmpty()) {
            for (User user : users) {
                String iconCode = "";
                //mapperToUserResponse
                response.getData().add(new ListUserResponseMapperImpl().mapToUserResponse(user));
            }
        }

        long numPages = PaginationHelper.calculateNumPages(count, sizeInt);

        response.setPagination(Pagination.builder()
                .count(count)
                .page(pageInt)
                .displayRecord(sizeInt)
                .numPages(numPages)
                .build());

        return ResponseEntity.ok(response);
    }

    // build create user REST API
    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody CreateUserBody userBody) {
        User user = new User();
        user = new CreateUserBodyMapperImpl().mapBodytoUser(userBody);
        System.out.println(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    // build get user by id REST API
    @GetMapping("{id}")
    public ResponseEntity<UserDetailResponse> getUserById(@PathVariable("id") Long userId) {
//        User user =  userService.getUserById(userId);
//        UserResponse userResponse = new UserMapperImpl().mapToUserResponse(user);
        User user = User.builder()
                .id(1L)
                .username("USERNAME")
                .email("EMAIL")
                .dob(LocalDate.now())
                .note("NOTE")
                .fullName("FULLNAME")
                .phoneNumber("00000000")
                .address("ADDRESS").isDelete(false)
                .position(Position.builder().id(1L).name("POSITION A").build())
                .department(Department.builder().id(2L).name("DEPARTMENT").build())
                .role(Role.builder().id(1L).code("ROLE CODE").name("ROLE NAME").build())
                .build();
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        UserDetailResponse userResponse = new DetailUserResponseMapperImpl().mapToUserDetail(user);

        return ResponseEntity.ok(userResponse);
    }

    // build update user REST API
    @PutMapping()
    public ResponseEntity<User> updateUser( @RequestBody
                                                EditUserBody userEditBody) {
       User user = new EditUserResponseEntityMapperImpl().mapEditResponsetoUser(userEditBody);
        System.out.println(user);
       return ResponseEntity.status(HttpStatus.OK).body(null);

    }

    // build delete user REST API
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId) {
        return null;
    }
}
