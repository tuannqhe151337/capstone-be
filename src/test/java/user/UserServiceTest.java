package user;

import com.example.capstone_project.controller.body.user.activate.ActivateUserBody;
import com.example.capstone_project.controller.body.user.deactive.DeactiveUserBody;
import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.impl.MailRepository;
import com.example.capstone_project.repository.redis.UserDetailRepository;
import com.example.capstone_project.repository.result.UpdateUserDataOption;
import com.example.capstone_project.service.impl.UserServiceImpl;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.department.InvalidDepartmentIdException;
import com.example.capstone_project.utils.exception.position.InvalidPositionIdException;
import com.example.capstone_project.utils.exception.role.InvalidRoleIdException;
import com.example.capstone_project.utils.helper.UserHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.passay.PasswordGenerator;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserAuthorityRepository userAuthorityRepository;

    @Mock
    private UserSettingRepository userSettingRepository;

    @Mock
    private MailRepository mailRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AuthorityRepository authorityRepository;

    @Mock
    private UserDetailRepository userDetailRepository;

    @Mock
    private PasswordGenerator mockPasswordGenerator;

    private Long userId;
    private Long actorId;
    private Position position;
    private Department department;
    private Role role;
    private User user;
    private User user2;
    private User oldUser;
    private Pageable pageable;

    @Nested
    class TestsWithCommonSetup {
        @BeforeEach
        void setUp() {
            userId = 1L;
            actorId = 2L;

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
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .email("mailho21@gmail.com")
                    .address("Ha Noi")
                    .isDelete(false)
                    .phoneNumber("0999988877")
                    .build();
            user2 = User.builder()
                    .id(2L)
                    .username("username2")
                    .fullName("Nuto Nu")
                    .password("password")
                    .role(role)
                    .department(department)
                    .position(position)
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .email("mailho21@gmail.com")
                    .address("Ha Noi")
                    .isDelete(false)
                    .phoneNumber("0999988877")
                    .build();
            oldUser = User.builder()
                    .id(userId)
                    .username("username1")
                    .fullName("Nutalomlokkkkk")
                    .password("password")
                    .role(role)
                    .department(department)
                    .position(position)
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .email("mailho2111@gmail.com")
                    .address("Ha Noi")
                    .isDelete(false)
                    .phoneNumber("0999988877")
                    .build();
            pageable = PageRequest.of(0, 10);

            // Mock the SecurityContextHolder to return a valid user ID
            SecurityContext securityContext = mock(SecurityContext.class);
            Authentication authentication = mock(Authentication.class);

            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(actorId.toString());

            SecurityContextHolder.setContext(securityContext);
        }

        //test get all user list
        @Test
        void testGetAllUsers_WithAuthority() {
            // Mock user authority check
            when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
            when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.VIEW_LIST_USERS.getValue()));

            // Mock repository response
            //suppose that any entered inputs will return this list
            List<User> expectedUsers = Arrays.asList(user, user2);
            when(userRepository.getUserWithPagination(1L, 1L, 1L, "username", pageable))
                    .thenReturn(expectedUsers);

            // Test getAllUsers method
            List<User> actualUsers = userServiceImpl.getAllUsers(1L, 1L, 1L, "username", pageable);

            // Verify repository method call
            verify(userRepository).getUserWithPagination(1L, 1L, 1L, "username", pageable);

            // Assert the result
            assertEquals(expectedUsers, actualUsers);
        }

        @Test
        void testGetAllUsers_WithoutAuthority() {
            // Mock user authority check
            when(UserHelper.getUserId()).thenReturn(Math.toIntExact(userId));
            when(userAuthorityRepository.get(userId)).thenReturn((Set.of()));

            UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
                userServiceImpl.getAllUsers(1L, 1L, 1L, "username", pageable);
            });

            assertEquals("Unauthorized to view all users", exception.getMessage());
            verify(userRepository, never()).getUserWithPagination(1L, 1L, 1L, "username", pageable);
        }

        @Test
        void testGetAllUsers_EmptyResult() {
            // Mock user authority check
            when(UserHelper.getUserId()).thenReturn(Math.toIntExact(userId));
            when(userAuthorityRepository.get(userId)).thenReturn(Set.of(AuthorityCode.VIEW_LIST_USERS.getValue()));

//        // Mock repository response to return empty list
//        when(userRepository.getUserWithPagination(anyLong(), anyLong(), anyLong(), anyString(), any(Pageable.class)))
//                .thenReturn(Collections.emptyList());

            // Test getAllUsers method
            List<User> actualUsers = userServiceImpl.getAllUsers(1L, 1L, 1L, "username", pageable);

            // Verify repository method call
            verify(userRepository).getUserWithPagination(1L, 1L, 1L, "username", pageable);

            // Assert the result
            assertTrue(actualUsers.isEmpty());
        }
        @Test
        void testGetUserById_Unauthorized() {
            when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
            when(userAuthorityRepository.get(actorId)).thenReturn(Set.of());

            Exception exception = assertThrows(UnauthorizedException.class, () -> {
                userServiceImpl.getUserById(userId);
            });

            assertEquals("Unauthorized to view user details", exception.getMessage());
        }

        @Test
        void testGetUserById_UserNotFound() {
            when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
            when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.VIEW_USER_DETAILS.getValue()));
            when(userRepository.findUserDetailedById(userId)).thenReturn(Optional.empty());

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
                userServiceImpl.getUserById(userId);
            });

            assertEquals("User not found", exception.getMessage());
        }

        @Test
        void testGetUserById_Success() throws Exception {
            when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
            when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.VIEW_USER_DETAILS.getValue()));
            when(userRepository.findUserDetailedById(userId)).thenReturn(Optional.of(user));

            User foundUser = userServiceImpl.getUserById(userId);

            assertEquals(userId, foundUser.getId());
            assertEquals("Nutalomlok Nunu", foundUser.getFullName());
            assertEquals("username1", foundUser.getUsername());
            assertEquals("password", foundUser.getPassword());
            assertEquals("mailho21@gmail.com", foundUser.getEmail());
            assertEquals(LocalDateTime.of(2002, 11, 11, 0, 0, 0), foundUser.getDob());
            assertEquals("Ha Noi", foundUser.getAddress());
            assertEquals("0999988877", foundUser.getPhoneNumber());
            assertEquals(position.getId(), foundUser.getPosition().getId());
            assertEquals(position.getName(), foundUser.getPosition().getName());
            assertEquals(department.getId(), foundUser.getDepartment().getId());
            assertEquals(department.getName(), foundUser.getDepartment().getName());
            assertEquals(role.getId(), foundUser.getRole().getId());
            assertEquals(role.getName(), foundUser.getRole().getName());
        }

        @Test
        public void testGetUserById_InternalServerError() throws Exception {
            when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
            when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.VIEW_USER_DETAILS.getValue()));
            when(userRepository.findUserDetailedById(userId)).thenThrow(new RuntimeException("Database error"));

            Exception exception = assertThrows(Exception.class, () -> {
                userServiceImpl.getUserById(userId);
            });

            assertEquals("Database error", exception.getMessage());
            verify(userRepository, times(1)).findUserDetailedById(userId);
        }


        @Test
        public void testGetUserById_BoundaryValue() throws Exception {
            when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
            when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.VIEW_USER_DETAILS.getValue()));
            when(userRepository.findUserDetailedById(userId)).thenReturn(Optional.of(user));

            User result = userServiceImpl.getUserById(userId);

            assertNotNull(result);
            assertEquals(userId, result.getId());
            verify(userRepository, times(1)).findUserDetailedById(userId);
        }

        //create user test
        @Test
        public void testCreateUser_Unauthorized() {
            when(UserHelper.getUserId()).thenReturn(1);
            when(userAuthorityRepository.get(1L)).thenReturn(Set.of());

            Exception exception = assertThrows(UnauthorizedException.class, () -> {
                userServiceImpl.createUser(user);
            });

            assertEquals("Unauthorized to create new user", exception.getMessage());
        }

        @Test
        public void testCreateUser_EmailAlreadyExists() {
            when(UserHelper.getUserId()).thenReturn(1);
            when(userAuthorityRepository.get(1L)).thenReturn(Set.of(AuthorityCode.CREATE_NEW_USER.getValue()));
            when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

            Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
                userServiceImpl.createUser(user);
            });

            assertEquals("Email already exists", exception.getMessage());
        }

        @Test
        public void testCreateUser_Success() throws Exception {
            when(UserHelper.getUserId()).thenReturn(1);
            when(userAuthorityRepository.get(1L)).thenReturn((Set.of(AuthorityCode.CREATE_NEW_USER.getValue())));
            when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
            when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

            when(departmentRepository.existsById(anyLong())).thenReturn(true);
            when(positionRepository.existsById(anyLong())).thenReturn(true);
            when(roleRepository.existsById(anyLong())).thenReturn(true);

            userServiceImpl.createUser(user);

            verify(userRepository, times(1)).save(user);
            verify(userSettingRepository, times(1)).save(any(UserSetting.class));
            verify(mailRepository, times(1)).sendEmail(eq("mailho21@gmail.com"), eq(user.getFullName()), eq(user.getUsername()), anyString());
        }

        @Test
        public void testUpdateUser_Success() throws Exception {
            // Mock the necessary methods
            //authority ok
            when(UserHelper.getUserId()).thenReturn(1);
            when(userAuthorityRepository.get(1L)).thenReturn((Set.of(AuthorityCode.EDIT_USER.getValue())));

            //user exists
            when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

            //exist
            when(departmentRepository.existsById(user.getDepartment().getId())).thenReturn(true);
            when(positionRepository.existsById(user.getDepartment().getId())).thenReturn(true);
            when(roleRepository.findById(user.getRole().getId())).thenReturn(Optional.of(role));


            // Initialize authority objects and set their codes
            Authority authority1 = new Authority();
            authority1.setCode(AuthorityCode.EDIT_USER);  // Ensure code is set
            Authority authority2 = new Authority();
            authority2.setCode(AuthorityCode.VIEW_USER_DETAILS);  // Ensure code is set

            when(authorityRepository.findAuthoritiesOfRole(anyLong())).thenReturn(List.of(authority1, authority2));

            // Perform the update
            userServiceImpl.updateUser(user);

            // Verify interactions and assert results
            verify(userRepository, times(1)).findById(user.getId());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(userRepository, times(1)).saveUserData(eq(user), any(UpdateUserDataOption.class));
            verify(userAuthorityRepository, times(1)).save(anyLong(), anyList(), any());
            verify(userDetailRepository, times(1)).save(anyLong(), any(UserDetail.class), any());
        }

        @Test
        public void testCreateUser_DepartmentNotExist() throws Exception {
            when(UserHelper.getUserId()).thenReturn(1);
            when(userAuthorityRepository.get(1L)).thenReturn((Set.of(AuthorityCode.CREATE_NEW_USER.getValue())));

            when(departmentRepository.existsById(anyLong())).thenReturn(false);
            Exception exception = assertThrows(InvalidDepartmentIdException.class, () -> {
                userServiceImpl.createUser(user);
            });

            assertEquals("Department does not exist", exception.getMessage());
        }

        @Test
        public void testCreateUser_PositionNotExist() throws Exception {
            when(UserHelper.getUserId()).thenReturn(1);
            when(userAuthorityRepository.get(1L)).thenReturn((Set.of(AuthorityCode.CREATE_NEW_USER.getValue())));

            when(positionRepository.existsById(anyLong())).thenReturn(false);
            when(departmentRepository.existsById(anyLong())).thenReturn(true);
            Exception exception = assertThrows(InvalidPositionIdException.class, () -> {
                userServiceImpl.createUser(user);
            });

            assertEquals("Position does not exist", exception.getMessage());
        }

        @Test
        public void testCreateUser_RoleNotExist() throws Exception {
            when(UserHelper.getUserId()).thenReturn(1);
            when(userAuthorityRepository.get(1L)).thenReturn((Set.of(AuthorityCode.CREATE_NEW_USER.getValue())));
            when(positionRepository.existsById(anyLong())).thenReturn(true);
            when(departmentRepository.existsById(anyLong())).thenReturn(true);
            when(roleRepository.existsById(anyLong())).thenReturn(false);
            Exception exception = assertThrows(InvalidRoleIdException.class, () -> {
                userServiceImpl.createUser(user);
            });

            assertEquals("Role does not exist", exception.getMessage());
        }
        @Test
        void testActivateUser_Unauthorized() {
            ActivateUserBody activateUserBody = ActivateUserBody.builder().id(1L).build();
            when(UserHelper.getUserId()).thenReturn(1);
            when(userAuthorityRepository.get(1L)).thenReturn((Set.of()));

            assertThrows(UnauthorizedException.class, () -> userServiceImpl.activateUser(activateUserBody));
        }

        @Test
        void testActivateUser_UserNotFound() {
            ActivateUserBody activateUserBody = ActivateUserBody.builder().id(1L).build();
            when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
            when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.ACTIVATE_USER.getValue()));

            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.activateUser(activateUserBody));
        }

        @Test
        void testActivateUser_Success() {
            ActivateUserBody activateUserBody = ActivateUserBody.builder().id(1L).build();
            when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
            when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.ACTIVATE_USER.getValue()));

            when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

            userServiceImpl.activateUser(activateUserBody);

            assertFalse(user.getIsDelete());

            verify(userRepository, times(1)).save(user);
        }
        @Test
        void testDeactivateUser_Unauthorized() {
            DeactiveUserBody deactivateUserBody =   DeactiveUserBody.builder().id(1L).build();
            when(UserHelper.getUserId()).thenReturn(1);
            when(userAuthorityRepository.get(1L)).thenReturn((Set.of()));
            assertThrows(UnauthorizedException.class, () -> userServiceImpl.deactivateUser(deactivateUserBody));
        }

        @Test
        void testDeactivateUser_UserNotFound() {
            DeactiveUserBody deactiveUserBody = DeactiveUserBody.builder().id(1L).build();
            when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
            when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.DEACTIVATE_USER.getValue()));

            when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () -> userServiceImpl.deactivateUser(deactiveUserBody));
        }

        @Test
        void testDeactivateUser_Success() {
            DeactiveUserBody deactivateUserBody = DeactiveUserBody.builder().id(1L).build();
            when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
            when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.DEACTIVATE_USER.getValue()));

            when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

            userServiceImpl.deactivateUser(deactivateUserBody);

            assertTrue(user.getIsDelete());

            verify(userRepository, times(1)).save(user);
        }
        //list



    }

    @Nested
    class TestsWithCustomSetup {
        @BeforeEach
        void setUp() {
            // Custom setup for this test group if needed
        }

        @Test
        void testGenerateUsernameFromFullName_NoExistingUsernames() throws Exception {
            String fullname = "Nguyen Van Anh";
            Method method = UserServiceImpl.class.getDeclaredMethod("generateUsernameFromFullName", String.class);
            method.setAccessible(true);
            String result = (String) method.invoke(userServiceImpl, fullname);
            assertEquals("AnhNV", result);
        }

        @Test
        void testGenerateUsernameFromFullName_ExistingUsernamesWithoutNumbers() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            String fullname = "Nguyen Van Anh";
            Method method = UserServiceImpl.class.getDeclaredMethod("generateUsernameFromFullName", String.class);
            method.setAccessible(true);
            lenient().when(userRepository.getLatestSimilarUsername("AnhNV")).thenReturn("AnhNV");
            String result = (String) method.invoke(userServiceImpl, fullname);
            assertEquals("AnhNV2", result);
        }

        @Test
        void testGenerateUsernameFromFullName_ExistingUsernamesWithNumbers() throws Exception {
            String fullname = "Nguyen Van Anh";
            Method method = UserServiceImpl.class.getDeclaredMethod("generateUsernameFromFullName", String.class);
            method.setAccessible(true);
            lenient().when(userRepository.getLatestSimilarUsername("AnhNV")).thenReturn("AnhNV5");
            String result = (String) method.invoke(userServiceImpl, fullname);
            assertEquals("AnhNV6", result);
        }

        @Test
        public void testGeneratePassayPassword() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
            Method method = UserServiceImpl.class.getDeclaredMethod("generatePassayPassword");
            method.setAccessible(true);
            String password = (String) method.invoke(userServiceImpl);

            assertEquals(10, password.length(), "Password should be 10 characters long");

            // Check for at least 2 lowercase letters
            long lowerCaseCount = password.chars()
                    .filter(Character::isLowerCase)
                    .count();
            assertTrue(lowerCaseCount >= 2, "Password should have at least 2 lowercase letters");

            // Check for at least 2 uppercase letters
            long upperCaseCount = password.chars()
                    .filter(Character::isUpperCase)
                    .count();
            assertTrue(upperCaseCount >= 2, "Password should have at least 2 uppercase letters");

            // Check for at least 2 digits
            long digitCount = password.chars()
                    .filter(Character::isDigit)
                    .count();
            assertTrue(digitCount >= 2, "Password should have at least 2 digits");

            // Check for at least 2 special characters
            Pattern specialCharPattern = Pattern.compile("[!@#$%^&*()_+]");
            long specialCharCount = password.chars()
                    .filter(ch -> specialCharPattern.matcher(Character.toString((char) ch)).matches())
                    .count();
            assertTrue(specialCharCount >= 2, "Password should have at least 2 special characters");
        }


    }
}
