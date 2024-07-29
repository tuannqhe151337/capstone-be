package term;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;

import com.example.capstone_project.repository.impl.MailRepository;
import com.example.capstone_project.service.impl.TermServiceImpl;
import com.example.capstone_project.service.impl.UserServiceImpl;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.enums.TermCode;
import com.example.capstone_project.utils.enums.TermDuration;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.term.InvalidDateException;
import com.example.capstone_project.utils.helper.UserHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
       term = Term.builder()
                .id(4L)
                .name("Winter 2024")
                .duration(TermDuration.HALF_YEARLY)
                .startDate(LocalDateTime.of(2025, 12, 1, 0, 0))
                .endDate(LocalDateTime.of(2025, 12, 31, 23, 59))
                .user(user)
                .status(termStatus)
                .build();

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

    @Test
    public void testCreateTerm_InvalidPlanDueDate() {
        // Mock the necessary methods
        when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
        when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.CREATE_TERM.getValue()));

        term.setEndDate(term.getStartDate().plusDays(1)); // Ensure endDate is set to a valid value
        term.setPlanDueDate(term.getStartDate().plusDays(10)); // Set an invalid plan due date

        // Perform the create term action and expect an InvalidDateException
        InvalidDateException exception = assertThrows(InvalidDateException.class, () -> {
            termServiceImpl.createTerm(term);
        });

        assertEquals("Plan due date must be within 5 days after end date.", exception.getMessage());
        verify(termRepository, never()).save(any(Term.class));
    }

    @Test
    public void testCreateTerm_Success() throws Exception {
        // Mock the necessary methods
        when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
        when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.CREATE_TERM.getValue()));
        when(termStatusRepository.getReferenceById(termStatus.getId())).thenReturn(termStatus);
        when(userRepository.getReferenceById(user.getId())).thenReturn(user);

        // Perform the create term action
        termServiceImpl.createTerm(term);

        // Verify interactions and assert results
        verify(userAuthorityRepository, times(1)).get(actorId);
        verify(termStatusRepository, times(1)).getReferenceById(termStatus.getId());
        verify(userRepository, times(1)).getReferenceById(user.getId()); // Use user.getId() instead of userId
        verify(termRepository, times(1)).save(term);
    }
    //list term management




}
