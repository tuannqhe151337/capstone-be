package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.user.activate.ActivateUserBody;
import com.example.capstone_project.controller.body.user.create.CreateUserBody;
import com.example.capstone_project.controller.body.user.delete.DeleteUserBody;
import com.example.capstone_project.controller.body.user.getDetail.GetUserBody;
import com.example.capstone_project.controller.body.user.update.UpdateUserBody;
import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.Pagination;

import com.example.capstone_project.controller.responses.user.list.UserResponse;
import com.example.capstone_project.controller.responses.user.detail.UserDetailResponse;
import com.example.capstone_project.entity.Department;
import com.example.capstone_project.entity.Position;
import com.example.capstone_project.entity.Role;
import com.example.capstone_project.entity.User;
import com.example.capstone_project.service.UserService;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.user.create.CreateUserBodyMapperImpl;
import com.example.capstone_project.utils.mapper.user.detail.DetailUserResponseMapperImpl;
import com.example.capstone_project.utils.mapper.user.edit.UpdateUserToUserDetailResponseMapperImpl;
import com.example.capstone_project.utils.mapper.user.list.ListUserResponseMapperImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<ListPaginationResponse<UserResponse>> getAllUsers(
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
        Pageable pageable = PaginationHelper.handlingPagination(pageInt, sizeInt, sortBy, sortType);

        // Get data
        List<User> users = userService.getAllUsers(query, pageable);

        long count = this.userService.countDistinct(query);

        // Response
        ListPaginationResponse<UserResponse> response = new ListPaginationResponse<>();

        if (users != null && !users.isEmpty()) {
            for (User user : users) {
                //mapperToUserResponse
                response.getData().add(new ListUserResponseMapperImpl().mapToUserResponse(user));
            }
        }

        long numPages = PaginationHelper.calculateNumPages(count, sizeInt);

        response.setPagination(Pagination.builder()
                .totalRecords(100)
                .page(pageInt)
                .limitRecordsPerPage(4)
                .numPages(numPages)
                .build());

        return ResponseEntity.ok(response);
    }

    // build create user REST API
    @PostMapping()
    public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserBody userBody, BindingResult result) {

        User user = new User();
        user = new CreateUserBodyMapperImpl().mapBodytoUser(userBody);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created success");
    }

    // build get user by id REST API
    @GetMapping("/detail")
    public ResponseEntity<UserDetailResponse> getUserById(@Valid @RequestBody GetUserBody getUserBody) {
        try {
            User user = userService.getUserById(getUserBody.getId());
            UserDetailResponse userDetailResponse = new DetailUserResponseMapperImpl().mapToUserDetail(user);
            return ResponseEntity.status(HttpStatus.OK).body(userDetailResponse);
        }catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }catch (Exception e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // build update user REST API
    @PutMapping()
    public ResponseEntity<UserDetailResponse> updateUser(@Valid @RequestBody UpdateUserBody updateUserBody, BindingResult bindingResult) {
        User user = new User();
        UserDetailResponse userDetailResponse = new UpdateUserToUserDetailResponseMapperImpl().mapUpdateUserToUserDetailResponse(updateUserBody);
        userDetailResponse.setCreatedAt(LocalDateTime.now());
        userDetailResponse.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.ok(null);
    }

    // build delete user REST API
    @DeleteMapping()
    public ResponseEntity<String> deleteUser(@Valid @RequestBody DeleteUserBody deleteUserBody, BindingResult bindingResult) {
        return ResponseEntity.status(HttpStatus.OK).body("Delete user success");
    }
    // build delete user REST API
    @PostMapping("/activate")
    public ResponseEntity<String> activeUser(@Valid @RequestBody ActivateUserBody activateUserBody, BindingResult bindingResult) {
        return ResponseEntity.status(HttpStatus.OK).body("Activate user " + activateUserBody.getId()+ " success");
    }


}
