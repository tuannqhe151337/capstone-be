package term;


import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.TermRepository;
import com.example.capstone_project.service.impl.TermServiceImpl;
import com.example.capstone_project.service.scheduler.TermSchedulerService;
import com.example.capstone_project.utils.enums.TermStatusCode;
import com.example.capstone_project.utils.enums.TermDuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TermScheduleTest {
    @Mock
    private TermServiceImpl termServiceImpl;

    @Mock
    private TermRepository termRepository;

    @InjectMocks
    private TermSchedulerService termSchedulerService;

    private Term term1;
    private Term term2;
    private Term term3;

    private TermStatus termStatus;
    private TermStatus termStatus2;

    @BeforeEach
    void setUp() {

        User user = User.builder()
                .id(1L)
                .username("username1")
                .fullName("Nutalomlok Nunu")
                .password("password")
                .role(new Role())
                .department(new Department())
                .position(new Position())
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
        termStatus2 = TermStatus.builder()
                .id(2L)
                .name("In progress")
                .code(TermStatusCode.NEW)
                .build();


        term1 = Term.builder()
                .id(4L)
                .name("Winterrrr 2024")
                .duration(TermDuration.HALF_YEARLY)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .user(user)
                .status(termStatus2)
                .build();

        term2 = Term.builder()
                .id(5L)
                .name("Winnnnter 2024")
                .duration(TermDuration.HALF_YEARLY)
                .startDate(LocalDateTime.now().minusDays(2))
                .endDate(LocalDateTime.now())
                .user(user)
                .status(termStatus2)
                .build();
        term3 = Term.builder()
                .id(5L)
                .name("Winnn 2024")
                .duration(TermDuration.HALF_YEARLY)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .user(user)
                .status(termStatus)
                .build();
    }


    //when there are no terms

    @Test
    void testGetListTermNeedToStart_ReturnsEmptyList() {

        List<Term> terms = termRepository.getListTermNeedToStart(TermStatusCode.NEW, LocalDateTime.now());

        assertTrue(terms.isEmpty());
        verify(termRepository).getListTermNeedToStart(eq(TermStatusCode.NEW), any(LocalDateTime.class));
    }

    @Test
    void testGetListTermNeedToStart_ReturnsNonEmptyList() {
        // Given
        LocalDateTime mockTime = LocalDateTime.of(2024, 8, 2, 10, 44, 5, 923785900);
        when(termRepository.getListTermNeedToStart(eq(TermStatusCode.NEW), eq(mockTime)))
                .thenReturn(Arrays.asList(term1, term2));

        // When
        List<Term> terms = termRepository.getListTermNeedToStart(TermStatusCode.NEW, mockTime);

        // Then
        assertFalse(terms.isEmpty());
        assertEquals(2, terms.size());
        verify(termRepository).getListTermNeedToStart(eq(TermStatusCode.NEW), eq(mockTime));
    }


}