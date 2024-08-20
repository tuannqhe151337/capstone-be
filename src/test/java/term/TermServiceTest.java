package term;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Set;

import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;

import com.example.capstone_project.service.impl.TermServiceImpl;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.enums.TermStatusCode;
import com.example.capstone_project.utils.enums.TermDuration;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.term.*;
import com.example.capstone_project.utils.helper.UserHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Mock
    private TermIntervalRepository termIntervalRepository;

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
    private TermInterval termInterval;

    @Nested
    class TestsWithCommonSetup {
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
                .code(TermStatusCode.NEW).build();

        // Mock the SecurityContextHolder to return a valid user ID
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(actorId.toString());

        SecurityContextHolder.setContext(securityContext);
       termInterval = TermInterval
                .builder().startTermDate(25)
                .endTermInterval(5)
                .startReuploadInterval(20)
                .endReuploadInterval(3)
                .build();
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
        when(termIntervalRepository.getReferenceById(1)).thenReturn(termInterval);

        Term term1 = Term.builder()
                .id(1L)
                .name("Spring 2024")
                .duration(TermDuration.MONTHLY)
                .startDate(LocalDateTime.of(2020, 7, 25, 0,0,0))   //start date too long time ago , final date is in the past
                .endDate(LocalDateTime.now().plusDays(5))
                .reuploadStartDate(LocalDateTime.now().plusDays(20))
                .reuploadEndDate(LocalDateTime.now().plusDays(21))
                .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.now()))
                .allowReupload(false)
                .user(user)
                .status(termStatus)
                .build();// Properly set the mocked duration

        InvalidEndDateException e =  assertThrows(InvalidEndDateException.class, () -> {
            termServiceImpl.createTerm(term1);
        });
        assertEquals("Final end date must be in the future", e.getMessage());

    }
    //Wrong start term date
    //Final end date is in the past
    @Test
    void testWrongStartTermDate() {
        // Mock user authority check
        when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
        when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.CREATE_TERM.getValue()));
        when(termIntervalRepository.getReferenceById(1)).thenReturn(termInterval);

        Term term1 = Term.builder()
                .id(1L)
                .name("Spring 2024")
                .duration(TermDuration.MONTHLY)
                .startDate(LocalDateTime.of(2020, 7, 23, 0,0,0))   //start date too long time ago , final date is in the past
                .endDate(LocalDateTime.now().plusDays(5))
                .reuploadStartDate(LocalDateTime.now().plusDays(20))
                .reuploadEndDate(LocalDateTime.now().plusDays(21))
                .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.now()))
                .allowReupload(false)
                .user(user)
                .status(termStatus)
                .build();// Properly set the mocked duration

       InvalidStartTermDateException e =  assertThrows(InvalidStartTermDateException.class, () -> {
            termServiceImpl.createTerm(term1);
        });
        assertEquals("Start date must be in day 25", e.getMessage());
    }
    //CREATE TERM

        //authorized
        @Test
        void createTerm_Success() throws Exception {
            // Giả lập người dùng có quyền tạo Term
            when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
            when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.CREATE_TERM.getValue()));
            when(termIntervalRepository.getReferenceById(1)).thenReturn(termInterval);

            // Giả lập Term
            Term term = Term.builder()
                    .startDate(LocalDateTime.of(2024, 7, 25, 0, 0))  // Đúng ngày bắt đầu
                    .endDate(LocalDateTime.of(2024, 7, 30, 0, 0))    // Trong khoảng thời gian kết thúc cho phép
                    .reuploadStartDate(LocalDateTime.of(2024, 8, 15, 0, 0))  // Trong khoảng thời gian bắt đầu re-upload cho phép
                    .reuploadEndDate(LocalDateTime.of(2024, 8, 18, 0, 0))    // Trong khoảng thời gian kết thúc re-upload cho phép
                    .duration(TermDuration.MONTHLY)
                    .allowReupload(true)
                    .build();

            // Giả lập status và user
            when(termStatusRepository.getReferenceById(1L)).thenReturn(termStatus);
            when(userRepository.getReferenceById(userId)).thenReturn(user);

            // Thực hiện tạo Term
            termServiceImpl.createTerm(term);

            // Kiểm tra giá trị finalEndTermDate
            assertEquals(TermDuration.MONTHLY.calculateEndDate(term.getStartDate()), term.getFinalEndTermDate());

            // Kiểm tra status và user được set đúng
            assertEquals(termStatus, term.getStatus());
            assertEquals(user, term.getUser());

            // Kiểm tra xem Term đã được lưu chưa
            verify(termRepository).save(term);
        }
        //allow reupload false
        @Test
        void createTerm_DisallowReupload() throws Exception {
            // Giả lập người dùng có quyền tạo Term
            long userId = 1L;
            when(UserHelper.getUserId()).thenReturn(Math.toIntExact(actorId));
            when(userAuthorityRepository.get(actorId)).thenReturn(Set.of(AuthorityCode.CREATE_TERM.getValue()));
            when(termIntervalRepository.getReferenceById(1)).thenReturn(termInterval);
            // Giả lập Term
            Term term = Term.builder()
                    .startDate(LocalDateTime.of(2024, 7, 25, 0, 0))
                    .endDate(LocalDateTime.of(2024, 7, 30, 0, 0))
                    .id(1L)
                    .name("Spring 2024")
                    .duration(TermDuration.MONTHLY)
                    .allowReupload(false)  // Không cho phép re-upload
                    .build();

            // Giả lập status và user
            TermStatus status = new TermStatus();
            User user = new User();
            when(termStatusRepository.getReferenceById(1L)).thenReturn(status);
            when(userRepository.getReferenceById(userId)).thenReturn(user);

            // Thực hiện tạo Term
            termServiceImpl.createTerm(term);

            // Kiểm tra reuploadStartDate và reuploadEndDate bị set về null
            assertNull(term.getReuploadStartDate());
            assertNull(term.getReuploadEndDate());

            // Kiểm tra xem Term đã được lưu chưa
            verify(termRepository).save(term);
        }




    }
    @Nested
    class TestsCustomSetup {

        @BeforeEach
        void setUp() {
            termInterval = TermInterval
                    .builder().startTermDate(25)
                    .endTermInterval(5)
                    .startReuploadInterval(20)
                    .endReuploadInterval(3)
                    .build();
        }

        //test
        @Test
        void checkValidDateOfTerm_InvalidEndDate_EndDateOutOfRange_TooLate() {
            when(termIntervalRepository.getReferenceById(1)).thenReturn(termInterval);

            Term term = Term.builder()
                    .startDate(LocalDateTime.of(2026, 7, 25, 0, 0))
                    .endDate(LocalDateTime.of(2026, 7, 31, 0, 0)) // End date out of allowed range
                    .duration(TermDuration.MONTHLY)
                    .build();

            InvalidEndDateException e = assertThrows(InvalidEndDateException.class, () -> {
                termServiceImpl.checkValidDateOfTerm(term);
            });
            assertEquals("End date must be within 2026-07-26 and 2026-07-30", e.getMessage());
        }
        //test
        @Test
        void checkValidDateOfTerm_InvalidEndDate_EndDateOutOfRange_TooEarly() {
            when(termIntervalRepository.getReferenceById(1)).thenReturn(termInterval);

            Term term = Term.builder()
                    .startDate(LocalDateTime.of(2026, 7, 25, 0, 0))
                    .endDate(LocalDateTime.of(2026, 7, 24, 0, 0)) // End date out of allowed range
                    .duration(TermDuration.MONTHLY)
                    .build();

           InvalidEndDateException e =  assertThrows(InvalidEndDateException.class, () -> {
                termServiceImpl.checkValidDateOfTerm(term);
            });
            assertEquals("End date must be within 2026-07-26 and 2026-07-30", e.getMessage());
        }

        @Test
        void checkValidDateOfTerm_InvalidStartReupDate_TooEarly() {
            when(termIntervalRepository.getReferenceById(1)).thenReturn(termInterval);

            Term term = Term.builder()
                    .startDate(LocalDateTime.of(2026, 7, 25, 0, 0))
                    .endDate(LocalDateTime.of(2026, 7, 30, 0, 0))
                    .reuploadStartDate(LocalDateTime.of(2026, 7, 26, 0, 0)) // Re-upload start date too early
                    .duration(TermDuration.MONTHLY)
                    .allowReupload(true)
                    .build();

            InvalidStartReupDateException e = assertThrows(InvalidStartReupDateException.class, () -> {
                termServiceImpl.checkValidDateOfTerm(term);
            });
            assertEquals( "Re-upload start date must be within 2026-08-14 and 2026-08-17", e.getMessage());

        }
        @Test
        void checkValidDateOfTerm_InvalidStartReupDate_TooLate() {
            when(termIntervalRepository.getReferenceById(1)).thenReturn(termInterval);

            Term term = Term.builder()
                    .startDate(LocalDateTime.of(2026, 7, 25, 0, 0))
                    .endDate(LocalDateTime.of(2026, 7, 30, 0, 0))
                    .reuploadStartDate(LocalDateTime.of(2020, 8, 20, 0, 0)) // Re-upload start date too late
                    .duration(TermDuration.MONTHLY)
                    .allowReupload(true)
                    .build();

           InvalidStartReupDateException e =  assertThrows(InvalidStartReupDateException.class, () -> {
                termServiceImpl.checkValidDateOfTerm(term);
            });
            assertEquals( "Re-upload start date must be within 2026-08-14 and 2026-08-17", e.getMessage());
        }




        @Test
        void checkValidDateOfTerm_InvalidEndReupDate_TooLate() {
            when(termIntervalRepository.getReferenceById(1)).thenReturn(termInterval);

            Term term = Term.builder()
                    .startDate(LocalDateTime.of(2026, 7, 25, 0, 0))
                    .endDate(LocalDateTime.of(2026, 7, 30, 0, 0))
                    .reuploadStartDate(LocalDateTime.of(2026, 8, 15, 0, 0))
                    .reuploadEndDate(LocalDateTime.of(2026, 8, 30, 0, 0)) // Invalid reupload end date
                    .duration(TermDuration.MONTHLY)
                    .allowReupload(true)
                    .build();

            InvalidEndReupDateException e = assertThrows(InvalidEndReupDateException.class, () -> {
                termServiceImpl.checkValidDateOfTerm(term);
            });
            assertEquals("Re-upload end date must be within 2026-08-15 and 2026-08-18", e.getMessage());
        }

        @Test
        void checkValidDateOfTerm_InvalidEndReupDate_TooEarly() {
            when(termIntervalRepository.getReferenceById(1)).thenReturn(termInterval);

            Term term = Term.builder()
                    .startDate(LocalDateTime.of(2026, 7, 25, 0, 0))
                    .endDate(LocalDateTime.of(2026, 7, 30, 0, 0))
                    .reuploadStartDate(LocalDateTime.of(2026, 8, 15, 0, 0))
                    .reuploadEndDate(LocalDateTime.of(2026, 8, 5, 0, 0)) // Invalid reupload end date
                    .duration(TermDuration.MONTHLY)
                    .allowReupload(true)
                    .build();

            InvalidEndReupDateException e = assertThrows(InvalidEndReupDateException.class, () -> {
                termServiceImpl.checkValidDateOfTerm(term);
            });
            assertEquals("Re-upload end date must be within 2026-08-15 and 2026-08-18", e.getMessage());

        }
        @Test
        void checkValidDateOfTerm_Success() {
            // Mock TermInterval với các giá trị mặc định
            when(termIntervalRepository.getReferenceById(1)).thenReturn(termInterval);

            Term term = Term.builder()
                    .startDate(LocalDateTime.of(2024, 7, 25, 0, 0))  // Đúng ngày bắt đầu
                    .endDate(LocalDateTime.of(2024, 7, 30, 0, 0))    // Trong khoảng thời gian kết thúc cho phép
                    .reuploadStartDate(LocalDateTime.of(2024, 8, 15, 0, 0))  // Trong khoảng thời gian bắt đầu re-upload cho phép
                    .reuploadEndDate(LocalDateTime.of(2024, 8, 18, 0, 0))    // Trong khoảng thời gian kết thúc re-upload cho phép
                    .duration(TermDuration.MONTHLY)
                    .allowReupload(true)
                    .build();

            // Gọi phương thức để kiểm tra
            assertDoesNotThrow(() -> termServiceImpl.checkValidDateOfTerm(term));
        }



    }









}
