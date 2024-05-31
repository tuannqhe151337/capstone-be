package com.example.capstone_project.controller;

import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.user.list.UserResponse;
import com.example.capstone_project.entity.User;
import com.example.capstone_project.service.UserService;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.userManagement.UserMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<ListResponse<UserResponse>> getAllUsers(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType) {

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

        if (users == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        // Count distinct
        long count = this.userService.countDistinct(query);

        // Response
        ListResponse<UserResponse> response = new ListResponse<>();

        if (!users.isEmpty()) {
            for (User user : users) {
                response.getData().add(new UserMapperImpl().mapToUserResponse(user));
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
    public ResponseEntity<UserResponse> createUser(@RequestBody UserResponse user) {
        return null;
    }

    // build get user by id REST API
    @GetMapping("{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("id") Long userId){
        return null;
    }

    // build update user REST API
    @PutMapping("{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("id") Long userId,
                                                @RequestBody UserResponse userDetails) {
        return null;
    }

    // build delete user REST API
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long userId){
        return null;
    }
}
