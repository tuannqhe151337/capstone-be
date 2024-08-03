package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.user.activate.ActivateUserBody;
import com.example.capstone_project.controller.body.user.changePassword.ChangePasswordBody;
import com.example.capstone_project.controller.body.user.create.CreateUserBody;
import com.example.capstone_project.controller.body.user.deactive.DeactiveUserBody;
import com.example.capstone_project.controller.body.user.forgotPassword.ForgetPasswordEmailBody;
import com.example.capstone_project.controller.body.user.otp.OTPBody;
import com.example.capstone_project.controller.body.user.resetPassword.ResetPasswordBody;
import com.example.capstone_project.controller.body.user.update.UpdateUserBody;
import com.example.capstone_project.controller.body.user.updateUserSetting.UpdateUserSettingBody;
import com.example.capstone_project.controller.responses.ExceptionResponse;
import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.token.Token;
import com.example.capstone_project.controller.responses.user.list.UserResponse;
import com.example.capstone_project.controller.responses.user.detail.UserDetailResponse;
import com.example.capstone_project.entity.User;
import com.example.capstone_project.service.UserService;
import com.example.capstone_project.utils.exception.InvalidInputException;
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
import org.springframework.dao.InvalidDataAccessResourceUsageException;
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
            @RequestParam(name = "roleId", required = false) Long roleId,
            @RequestParam(name = "departmentId", required = false) Long departmentId,
            @RequestParam(name = "positionId", required = false) Long positionId,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {
        try {
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
            List<User> users = userService.getAllUsers(roleId, departmentId, positionId, query, pageable);
            long count = this.userService.countDistinct(query, roleId, departmentId, positionId);

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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
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
            ExceptionResponse exObject = ExceptionResponse.builder().field("email").message("Emails already exists").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exObject);
        } catch (InvalidDepartmentIdException e) {
            ExceptionResponse exObject = ExceptionResponse.builder().field("department").message("Department does not exist").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exObject);
        } catch (InvalidPositionIdException e){
            ExceptionResponse exObject = ExceptionResponse.builder().field("position").message("Position does not exist").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exObject);
        } catch(InvalidRoleIdException e) {
            ExceptionResponse exObject = ExceptionResponse.builder().field("role").message("Role does not exist").build();
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
            ExceptionResponse exObject = ExceptionResponse.builder().field("email").message("Email already exists").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exObject);
        } catch (InvalidDepartmentIdException e) {
            ExceptionResponse exObject = ExceptionResponse.builder().field("department").message("Department does not exist").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exObject);
        } catch (InvalidPositionIdException e) {
            ExceptionResponse exObject = ExceptionResponse.builder().field("position").message("Position does not exist").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exObject);
        } catch (InvalidRoleIdException e) {
            ExceptionResponse exObject = ExceptionResponse.builder().field("role").message("Role does not exist").build();
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
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (UnauthorizedException e) {
            throw new UnauthorizedException(e.getMessage());
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    // build delete user REST API
    @PostMapping("/activate")
    public ResponseEntity<Object> activeUser(@Valid @RequestBody ActivateUserBody activateUserBody, BindingResult bindingResult) {
        try {
            userService.activateUser(activateUserBody);
        } catch (UnauthorizedException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } catch (ResourceNotFoundException e){
            ExceptionResponse exceptionResponse =  ExceptionResponse.builder().field("error").message("User not found").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/change-password")

    public ResponseEntity<Object> changePassword(@Valid @RequestBody ChangePasswordBody changePasswordBody, BindingResult bindingResult) {

        try {
            userService.changePassword(changePasswordBody);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (IllegalArgumentException e) {
            ExceptionResponse exceptionResponse =  ExceptionResponse.builder().field("error").message("Old password does not match").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
        }catch(ResourceNotFoundException e){
            ExceptionResponse exceptionResponse =  ExceptionResponse.builder().field("error").message("User not found").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<Object> resetPassword(@Valid @RequestHeader("Authorization") String authHeader, @Valid @RequestBody ResetPasswordBody resetPasswordBody, BindingResult bindingResult) {
        try {
            userService.resetPassword(authHeader, resetPasswordBody);
            return ResponseEntity.status(HttpStatus.OK).body(null);
        } catch (ResourceNotFoundException e) {
            ExceptionResponse exceptionResponse = ExceptionResponse.builder().field("error").message("User not found").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
        } catch (InvalidDataAccessResourceUsageException e){
            ExceptionResponse exceptionResponse = ExceptionResponse.builder().field("error").message("UserID cannot be retrieved from AuthHeader").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/user-setting/update")
    public ResponseEntity<String> updateUserSetting(@Valid @RequestBody UpdateUserSettingBody updateUserSettingBody, BindingResult bindingResult) {
        userService.updateUserSetting(updateUserSettingBody);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping("/auth/forgot-password")
    public ResponseEntity<Object> receiveEmail(@Valid  @RequestBody ForgetPasswordEmailBody forgetPasswordEmailBody, BindingResult bindingResult) {
        //return token   user:otp:absodjfaod, {userId: 1, otp: 374923}.
        String token = null;
        try {
            token = userService.forgetPassword(forgetPasswordEmailBody);
            Token tokenString = Token.builder().token(token).build();
            return ResponseEntity.status(HttpStatus.OK).body(tokenString);
        } catch (ResourceNotFoundException e) {
            ExceptionResponse exceptionResponse =  ExceptionResponse.builder().field("error").message("Email not found").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }
    @PostMapping("/auth/otp")
    public ResponseEntity<Object> OTPValidate(@Valid @RequestHeader("Authorization") String authHeader, @Valid @RequestBody OTPBody otpBody, BindingResult bindingResult) {
        //return  Token  user:dnfpajsdfhp...:id, 6.
        try {
            String token = userService.otpValidate(otpBody, authHeader);
            Token tokenString = Token.builder().token(token).build();
            return ResponseEntity.status(HttpStatus.OK).body(tokenString);
        }catch (DataIntegrityViolationException e){
            ExceptionResponse exceptionResponse =  ExceptionResponse.builder().field("error").message("Bearer token does not exist").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
        }catch (UnauthorizedException e){
            ExceptionResponse exceptionResponse =  ExceptionResponse.builder().field("error").message("Invalid otp").build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exceptionResponse);
        }catch (InvalidDataAccessResourceUsageException e){
            ExceptionResponse exceptionResponse =  ExceptionResponse.builder().field("error").message("Invalid user id").build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }

    }
}

