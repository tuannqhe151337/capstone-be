package term;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;

import com.example.capstone_project.service.impl.TermServiceImpl;
import com.example.capstone_project.service.impl.UserServiceImpl;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.enums.TermCode;
import com.example.capstone_project.utils.enums.TermDuration;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.term.InvalidDateException;
import com.example.capstone_project.utils.exception.term.InvalidEndDateException;
import com.example.capstone_project.utils.exception.term.InvalidEndReupDateException;
import com.example.capstone_project.utils.exception.term.InvalidStartReupDateException;
import com.example.capstone_project.utils.helper.UserHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class TermServiceTest {
    @Mock
    private UserAuthorityRepository userAuthorityRepository;

    @Mock
    private TermStatusRepository termStatusRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TermRepository termRepository;

    @InjectMocks
    private TermServiceImpl termServiceImpl;

    private Position position;
    private Department department;
    private Role role;

    private Term term;
    private long userId = 1L;
    private User user;
    private TermStatus termStatus;
    private Long actorId;

    @BeforeEach
    void setUp() {
        userId = 1L;
        actorId = 1L;

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
        //Term Status - fixed code
        termStatus = TermStatus.
                builder()
                .id(1L).
                name("Not started")
                .code(TermCode.NEW).build();
        Term term = new Term();
        term.setStartDate(LocalDateTime.now().minusDays(10)); // Start date 10 days ago
        term.setEndDate(LocalDateTime.now().plusDays(5)); // End date in 5 days
        term.setReuploadStartDate(LocalDateTime.now().plusDays(6)); // Re-upload start date in 6 days
        term.setReuploadEndDate(LocalDateTime.now().plusDays(7)); // Re-upload end date in 7 days
        term.setDuration(TermDuration.MONTHLY);
        term.setFinalEndTermDate(LocalDateTime.now().minusDays(1)); // F

        // Mock the SecurityContextHolder to return a valid user ID
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(actorId.toString());

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testCreateTerm_Unauthorized() {
        when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
        when(userAuthorityRepository.get(actorId)).thenReturn(Set.of());

        Exception exception = assertThrows(UnauthorizedException.class, () -> {
          termServiceImpl.createTerm(term);
        });

        assertEquals("Unauthorized to create term", exception.getMessage());
    }

  //create term

//Final end date is in the past
    @Test
    void testFinalEndDateInThePast() {
        // Mock user authority check
        when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
        when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.CREATE_TERM.getValue()));


        Term term1 = Term.builder()
                .id(1L)
                .name("Spring 2024")
                .duration(TermDuration.MONTHLY)
                .startDate(LocalDateTime.now().minusDays(35))   //start date too long time ago , final date is in the past
                .endDate(LocalDateTime.now().plusDays(5))
                .reuploadStartDate(LocalDateTime.now().plusDays(20))
                .reuploadEndDate(LocalDateTime.now().plusDays(21))
                .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.now()))
                .user(user)
                .status(termStatus)
                .build();// Properly set the mocked duration

        assertThrows(InvalidEndDateException.class, () -> {
            termServiceImpl.createTerm(term1);
        });

    }

    // Test case for end date before start date
    @Test
    void testEndDateBeforeStartDate() {
        // Mock user authority check
        when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
        when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.CREATE_TERM.getValue()));

        Term mockTerm = Term.builder()
                .id(1L)
                .name("Spring 2024")
                .duration(TermDuration.MONTHLY)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().minusDays(10)) // // Test case for end date before start date
                .reuploadStartDate(LocalDateTime.now().plusDays(20))
                .reuploadEndDate(LocalDateTime.now().plusDays(21))
                .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.now()))
                .user(user)
                .status(termStatus)
                .build();// Properly set the mocked duration


        // Expect InvalidEndDateException
        assertThrows(InvalidEndDateException.class, () -> {
            termServiceImpl.createTerm(mockTerm);
        });
    }
    // Test case for reupload start date before end date
    @Test
    void testReuploadStartDateBeforeEndDate() {
        // Mock user authority check
        when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
        when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.CREATE_TERM.getValue()));

        Term mockTerm = Term.builder()
                .id(1L)
                .name("Spring 2024")
                .duration(TermDuration.MONTHLY)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .reuploadStartDate(LocalDateTime.now().plusDays(3))// Reupload start date before end date
                .reuploadEndDate(LocalDateTime.now().plusDays(21))
                .user(user)
                .status(termStatus)
                .build();// Properly set the mocked duration


        assertThrows(InvalidStartReupDateException.class, () -> {
            termServiceImpl.createTerm(mockTerm);
        });
    }

    // Test case for reupload end date before reupload start date
    @Test
    void testReuploadEndDateBeforeReuploadStartDate() {
        // Mock user authority check
        when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
        when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.CREATE_TERM.getValue()));

        Term mockTerm = Term.builder()
                .id(1L)
                .name("Spring 2024")
                .duration(TermDuration.MONTHLY)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2))
                .reuploadStartDate(LocalDateTime.now().plusDays(3))
                .reuploadEndDate(LocalDateTime.now().plusDays(2))
                .user(user)
                .status(termStatus)
                .build();// Properly set the mocked duration


        assertThrows(InvalidEndReupDateException.class, () -> {
            termServiceImpl.createTerm(mockTerm);
        });

    }
//end date after final end term date
@Test
void  testEndDateGreaterThanFinalEndTermDate() {
    // Mock user authority check
    when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
    when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.CREATE_TERM.getValue()));

    Term mockTerm = Term.builder()
            .id(1L)
            .name("Spring 2024")
            .duration(TermDuration.MONTHLY)
            .startDate(LocalDateTime.now())
            .endDate(LocalDateTime.now().plusDays(35))  ////end date after final end term date
            .reuploadStartDate(LocalDateTime.now().plusDays(3))
            .reuploadEndDate(LocalDateTime.now().plusDays(2))
            .user(user)
            .status(termStatus)
            .build();// Properly set the mocked duration


    InvalidEndDateException thrown =  assertThrows(InvalidEndDateException.class, () -> {
        termServiceImpl.createTerm(mockTerm);
    });
    assertEquals("End date must be in the future and after start date", thrown.getMessage());
}
    @Test
    void testReuploadStartDateGreaterThanFinalEndTermDate() {
        // Mock user authority check
        when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
        when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.CREATE_TERM.getValue()));

        Term mockTerm = Term.builder()
                .id(1L)
                .name("Spring 2024")
                .duration(TermDuration.MONTHLY)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))  ////end date after final end term date
                .reuploadStartDate(LocalDateTime.now().plusDays(35))
                .reuploadEndDate(LocalDateTime.now().plusDays(2))
                .user(user)
                .status(termStatus)
                .build();// Properly set the mocked duration

        // Mong đợi InvalidStartReupDateException
        InvalidStartReupDateException thrown = assertThrows(InvalidStartReupDateException.class, () -> {
            termServiceImpl.createTerm(mockTerm);
        });
        assertEquals("Re-upload start date must be in the future and after end date", thrown.getMessage());
    }

    @Test
    void testReuploadEndDateGreaterThanFinalEndTermDate() {
        // Mock user authority check
        when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
        when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.CREATE_TERM.getValue()));

        Term mockTerm = Term.builder()
                .id(1L)
                .name("Spring 2024")
                .duration(TermDuration.MONTHLY)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))  ////end date after final end term date
                .reuploadStartDate(LocalDateTime.now().plusDays(6))
                .reuploadEndDate(LocalDateTime.now().plusDays(100))
                .user(user)
                .status(termStatus)
                .build();// Properly set the mocked dura


        // Mong đợi InvalidEndReupDateException
        InvalidEndReupDateException thrown = assertThrows(InvalidEndReupDateException.class, () -> {
            termServiceImpl.createTerm(mockTerm);
        });
        assertEquals("Re-upload end date must be in the future and after re-up start date", thrown.getMessage());
    }


    // Test case for successful term creation
    @Test
    void testCreateTermSuccess() throws Exception {
        // Mock user authority check
        when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
        when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.CREATE_TERM.getValue()));

        Term mockTerm = Term.builder()
                .id(1L)
                .name("Spring 2024")
                .duration(TermDuration.MONTHLY)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .reuploadStartDate(LocalDateTime.now().plusDays(10))
                .reuploadEndDate(LocalDateTime.now().plusDays(15))
                .user(user)
                .status(termStatus)
                .build();// Properly set the mocked duration

        termServiceImpl.createTerm(mockTerm);

        // Verify that the term is saved
        Mockito.verify(termRepository).save(mockTerm);
    }




}
