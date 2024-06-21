package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.user.activate.ActivateUserBody;
import com.example.capstone_project.controller.body.user.create.CreateUserBody;
import com.example.capstone_project.controller.body.user.delete.DeleteUserBody;
import com.example.capstone_project.controller.body.user.update.UpdateUserBody;
import com.example.capstone_project.controller.responses.ExceptionResponse;
import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;

import com.example.capstone_project.controller.responses.user.list.UserResponse;
import com.example.capstone_project.controller.responses.user.detail.UserDetailResponse;
import com.example.capstone_project.entity.Department;
import com.example.capstone_project.entity.Position;
import com.example.capstone_project.entity.Role;
import com.example.capstone_project.entity.User;
import com.example.capstone_project.service.UserService;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.department.InvalidDepartmentIdException;
import com.example.capstone_project.utils.exception.postion.InvalidPositiontIdException;
import com.example.capstone_project.utils.exception.role.InvalidRoleIdException;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.user.create.CreateUserBodyMapperImpl;
import com.example.capstone_project.utils.mapper.user.detail.DetailUserResponseMapperImpl;

import com.example.capstone_project.utils.mapper.user.list.ListUserResponseMapperImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
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
        Pageable pageable = PaginationHelper.handlingPagination(pageInt, sizeInt, sortBy, sortType);

        // Get data
        List<User> users = userService.getAllUsers(query, pageable);

        long count = this.userService.countDistinct(query);

        // Response
        ListResponse<UserResponse> response = new ListResponse<>();

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
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UpdateUserBody updateUserBody, BindingResult bindingResult) {
        User user = null;
        try {
            user = userService.updateUser(updateUserBody);
            UserDetailResponse userResponse = new DetailUserResponseMapperImpl().mapToUserDetail(user);
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        } catch (UnauthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (DataIntegrityViolationException e) {
            ExceptionResponse exObject = ExceptionResponse.builder().field("email").message("email already exists").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exObject);
        } catch (InvalidDepartmentIdException e) {
            ExceptionResponse exObject = ExceptionResponse.builder().field("department").message("department does not exist").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exObject);
        } catch (InvalidPositiontIdException e) {
            ExceptionResponse exObject = ExceptionResponse.builder().field("position").message("position does not exist").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exObject);
        } catch (InvalidRoleIdException e) {
            ExceptionResponse exObject = ExceptionResponse.builder().field("role").message("role does not exist").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exObject);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
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
