package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.user.activate.ActivateUserBody;
import com.example.capstone_project.controller.body.user.changePassword.ChangePasswordBody;
import com.example.capstone_project.controller.body.user.create.CreateUserBody;
import com.example.capstone_project.controller.body.user.deactive.DeactiveUserBody;
import com.example.capstone_project.controller.body.user.forgotPassword.ForgetPasswordEmailBody;
import com.example.capstone_project.controller.body.user.update.UpdateUserBody;
import com.example.capstone_project.controller.responses.ExceptionResponse;
import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.user.list.UserResponse;
import com.example.capstone_project.controller.responses.user.detail.UserDetailResponse;
import com.example.capstone_project.entity.User;
import com.example.capstone_project.service.UserService;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.department.InvalidDepartmentIdException;
import com.example.capstone_project.utils.exception.position.InvalidPositionIdException;
import com.example.capstone_project.utils.exception.role.InvalidRoleIdException;
import com.example.capstone_project.utils.helper.JwtHelper;
import com.example.capstone_project.utils.helper.PaginationHelper;
import com.example.capstone_project.utils.mapper.user.create.CreateUserBodyMapperImpl;
import com.example.capstone_project.utils.mapper.user.detail.DetailUserResponseMapperImpl;

import com.example.capstone_project.utils.mapper.user.edit.UpdateUserBodyToUserEntityMapperImpl;
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
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {
    private final UserService userService;
    private final JwtHelper jwtHelper;

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
                .totalRecords(count)
                .page(pageInt)
                .limitRecordsPerPage(sizeInt)
                .numPages(numPages)
                .build());

        return ResponseEntity.ok(response);
    }

    // build create user REST API
    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody CreateUserBody userBody, BindingResult result) {
        try {
//convert from userDTO => user
            User newUser = new CreateUserBodyMapperImpl().mapBodytoUser(userBody);
            userService.createUser(newUser);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (UnauthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (DataIntegrityViolationException e) {
            ExceptionResponse exObject = ExceptionResponse.builder().field("email").message("emails already exists").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exObject);
        } catch (InvalidDepartmentIdException e) {
            ExceptionResponse exObject = ExceptionResponse.builder().field("department").message("department does not exist").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exObject);
        } catch (InvalidPositionIdException e){
            ExceptionResponse exObject = ExceptionResponse.builder().field("position").message("position does not exist").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exObject);
        } catch(InvalidRoleIdException e) {
            ExceptionResponse exObject = ExceptionResponse.builder().field("role").message("role does not exist").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exObject);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }

    }

    // build get user by id REST API
    @GetMapping("/detail/{id}")
    public ResponseEntity<UserDetailResponse> getUserById(@Valid @PathVariable("id") Long userid) {
        try {
            User user = userService.getUserById(userid);
            UserDetailResponse userDetailResponse = new DetailUserResponseMapperImpl().mapToUserDetail(user);
            return ResponseEntity.status(HttpStatus.OK).body(userDetailResponse);
        } catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // build update user REST API
    @PutMapping()
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UpdateUserBody updateUserBody, BindingResult bindingResult) {
        try {
            userService.updateUser(
                new UpdateUserBodyToUserEntityMapperImpl().updateUserFromDto(updateUserBody)
            );
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } catch (UnauthorizedException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (DataIntegrityViolationException e) {
            ExceptionResponse exObject = ExceptionResponse.builder().field("email").message("email already exists").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exObject);
        } catch (InvalidDepartmentIdException e) {
            ExceptionResponse exObject = ExceptionResponse.builder().field("department").message("department does not exist").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exObject);
        } catch (InvalidPositionIdException e) {
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
    public ResponseEntity<String> deactivateUser(@Valid @RequestBody DeactiveUserBody deactiveUserBody, BindingResult bindingResult) {
        try {
            userService.deactivateUser(deactiveUserBody);
            return ResponseEntity.status(HttpStatus.OK).body("Deactive user success");
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(e.getMessage());
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }
    // build delete user REST API
    @PostMapping("/activate")
    public ResponseEntity<String> activeUser(@Valid @RequestBody ActivateUserBody activateUserBody, BindingResult bindingResult) {
        try {
            userService.activateUser(activateUserBody);
        } catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        } catch (ResourceNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Activate user " + activateUserBody.getId()+ " success");
    }
    @PostMapping("/change-password")

    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordBody changePasswordBody, BindingResult bindingResult) {

        return ResponseEntity.status(HttpStatus.OK).body("Change password success");
    }
    @PostMapping("/auth/forgot-password")
    public ResponseEntity<String> receiveEmail(@Valid @RequestBody ForgetPasswordEmailBody forgetPasswordEmailBody, BindingResult bindingResult) {
        //return token   user:otp:absodjfaod, {userId: 1, otp: 374923}.
        //String generateAccessToken(Integer userId)
        String token = jwtHelper.genBlankToken();
        return ResponseEntity.status(HttpStatus.OK).body(token );
    }


}
