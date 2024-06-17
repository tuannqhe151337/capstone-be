package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.user.activate.ActivateUserBody;
import com.example.capstone_project.controller.body.user.create.CreateUserBody;
import com.example.capstone_project.controller.body.user.delete.DeleteUserBody;
import com.example.capstone_project.controller.body.user.update.UpdateUserBody;
import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.Pagination;

import com.example.capstone_project.controller.responses.term.getPlans.TermPlanDetailResponse;
import com.example.capstone_project.controller.responses.user.list.UserResponse;
import com.example.capstone_project.controller.responses.user.detail.UserDetailResponse;
import com.example.capstone_project.entity.Department;
import com.example.capstone_project.entity.Position;
import com.example.capstone_project.entity.Role;
import com.example.capstone_project.entity.User;
import com.example.capstone_project.service.UserService;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.user.create.CreateUserBodyMapperImpl;
import com.example.capstone_project.utils.mapper.user.detail.DetailUserResponseMapperImpl;
import com.example.capstone_project.utils.mapper.user.edit.UpdateUserToUserDetailResponseMapperImpl;
import com.example.capstone_project.utils.mapper.user.list.UserEntityToUserResponseMapperImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<ListPaginationResponse<UserResponse>> getAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {
        try {
            // Get data
            List<User> users = userService.getAllUsers();

        // Sort the list by createdAt of user
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getCreatedAt().compareTo(o2.getCreatedAt());
            }
        });

        //map to List UserResponse
        List<UserResponse> userResponseList = new UserEntityToUserResponseMapperImpl().mapToUserResponseList(users);

        //xu ly phan trang
        PageRequest pageRequest = (PageRequest) PaginationHelper.handlingPagination(page, size, sortBy, sortType);

        //Tao Page tu list
        Page<UserResponse> userResponseListPaging = PaginationHelper.createPage(userResponseList, pageRequest);


        //Build response
        Pagination pagination = Pagination
                .builder()
                .page(pageRequest.getPageNumber())
                .limitRecordsPerPage(pageRequest.getPageSize())
                .totalRecords(userResponseListPaging.getNumberOfElements())
                .numPages(PaginationHelper.
                        calculateNumPages((long) userResponseListPaging.getNumberOfElements(),
                                pageRequest.getPageSize())).build();

        ListPaginationResponse<UserResponse> response = new ListPaginationResponse<>();
        response.setData(userResponseList);
        response.setPagination(pagination);

        return ResponseEntity.ok(response);
        }catch (UnauthorizedException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // build create user REST API
    @PostMapping()
    public ResponseEntity<String> createUser(@Valid @RequestBody CreateUserBody userBody, BindingResult result) {

        User user = new User();
        user = new CreateUserBodyMapperImpl().mapBodytoUser(userBody);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created success");
    }

    // build get user by id REST API
    @GetMapping("{id}")
    public ResponseEntity<UserDetailResponse> getUserById(@Valid @PathVariable("id") Long userId) {
//        User user =  userService.getUserById(userId);
//        UserResponse userResponse = new UserMapperImpl().mapToUserResponse(user);
        User user = User.builder()
                .id(1L)
                .username("USERNAME")
                .email("email@gmail.com")
                .dob(LocalDateTime.now())
                .note("NOTE")
                .fullName("FULLNAME")
                .phoneNumber("0999888777")
                .address("ADDRESS")
                .isDelete(false)
                .position(Position.builder().id(1L).name("POSITION A").build())
                .department(Department.builder().id(2L).name("DEPARTMENT").build())
                .role(Role.builder().id(1L).code("ROLE CODE").name("ROLE NAME").build())
                .build();
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());


        UserDetailResponse userResponse = new DetailUserResponseMapperImpl().mapToUserDetail(user);
        return ResponseEntity.ok(null);

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
        return ResponseEntity.status(HttpStatus.OK).body("Activate user " + activateUserBody.getId() + " success");
    }


}
