package term;


import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.TermRepository;
import com.example.capstone_project.service.TermService;
import com.example.capstone_project.service.impl.TermServiceImpl;
import com.example.capstone_project.service.scheduler.TermSchedulerService;
import com.example.capstone_project.utils.enums.TermCode;
import com.example.capstone_project.utils.enums.TermDuration;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

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
                .code(TermCode.NEW).build();
        termStatus2 = TermStatus.builder()
                .id(2L)
                .name("In progress")
                .code(TermCode.NEW)
                .build();

        term1 = Term.builder()
                .id(4L)
                .name("Winterrrr 2024")
                .duration(TermDuration.HALF_YEARLY)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1))
                .user(user)
                .status(termStatus)
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
    }

    //test end, test start
    @Test
    public void testStartTerm() throws Exception {
        List<Term> terms = Arrays.asList(term1, term2);

        when(termRepository.findAll()).thenReturn(terms);

        termSchedulerService.startTerm();

        verify(termServiceImpl, times(1)).updateTermStatus(term1, 2L);
        verify(termServiceImpl, times(0)).updateTermStatus(term2, 2L);
    }

    //when there are no terms
    @Test
    public void testStartTermWithNoTerms() throws Exception {
        // Mock empty list
        when(termRepository.findAll()).thenReturn(Arrays.asList());

        // Execute the scheduled method directly
        termSchedulerService.startTerm();

        // Verify no interactions with termService
        verify(termServiceImpl, times(0)).updateTermStatus(any(Term.class), eq(2L));
    }


}
