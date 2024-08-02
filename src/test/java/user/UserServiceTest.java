package user;

import com.example.capstone_project.controller.body.user.otp.OTPBody;
import com.example.capstone_project.entity.Department;
import com.example.capstone_project.entity.Position;
import com.example.capstone_project.entity.Role;
import com.example.capstone_project.entity.User;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.repository.redis.OTPTokenRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.impl.MailRepository;
import com.example.capstone_project.repository.redis.UserIdTokenRepository;
import com.example.capstone_project.service.impl.UserServiceImpl;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.helper.JwtHelper;
import com.example.capstone_project.utils.helper.UserHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.Duration;
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

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private PositionRepository positionRepository;

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private OTPTokenRepository otpTokenRepository;
    @Mock
    private UserIdTokenRepository userIdTokenRepository;
    @Mock
    private JwtHelper jwtHelper;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private Long userId;
    private Long actorId;
    private Position position;
    private Department department;
    private Role role;
    private User user;
    private User user2;
    private User oldUser;
    private Pageable pageable;
    @Value("${application.security.blank-token-otp.expiration}")
    private String BLANK_TOKEN_OTP_EXPIRATION;

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
        public void testOtpValidate_Success() throws Exception {
            // Arrange
            String authHeaderToken = "validToken";
            OTPBody otp = new OTPBody("123456");
            String userId = "1"; // Ensure this is non-null
            User user = new User();
            user.setIsDelete(false);
            String savedOtp = "123456";
            String newToken = "newToken";

            // Mock behaviors
            when(otpTokenRepository.getUserID(authHeaderToken)).thenReturn(userId);
            when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
            when(otpTokenRepository.getOtpCode(authHeaderToken, anyLong())).thenReturn(savedOtp);
            when(jwtHelper.genBlankTokenOtp()).thenReturn(newToken);

            // Act
            String result = userServiceImpl.otpValidate(otp, authHeaderToken);

            // Assert
            assertEquals(newToken, result);

            // Verify
            verify(userIdTokenRepository).save(eq(newToken), eq(Long.parseLong(userId)), any());
        }


    }

    @Nested
    class TestsWithCustomSetup {
        @BeforeEach
        void setUp() {
            // Custom setup for this test group if needed
        }
        @Test
        void testOtpValidate_InvalidToken() {
            String authHeaderToken = "invalidToken";

            when(otpTokenRepository.getUserID(authHeaderToken)).thenReturn(null);

            assertThrows(InvalidDataAccessResourceUsageException.class, () -> userServiceImpl.otpValidate(new OTPBody(), authHeaderToken));
        }
        @Test
        void testOtpValidate_InvalidOtp() {
            String authHeaderToken = "validToken";
            OTPBody otpBody = new OTPBody();
            otpBody.setOtp("123456");

            when(otpTokenRepository.getUserID(authHeaderToken)).thenReturn("1");
            User user = new User();
            user.setId(1L);
            user.setIsDelete(false);
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(otpTokenRepository.getOtpCode(authHeaderToken, 1L)).thenReturn("654321");

            assertThrows(UnauthorizedException.class, () -> userServiceImpl.otpValidate(otpBody, authHeaderToken));
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
