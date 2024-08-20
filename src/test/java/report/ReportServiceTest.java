package report;

import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.ExpenseStatusRepository;
import com.example.capstone_project.repository.FinancialPlanExpenseRepository;
import com.example.capstone_project.repository.FinancialReportRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.redis.UserDetailRepository;
import com.example.capstone_project.service.impl.FinancialReportServiceImpl;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.enums.RoleCode;
import com.example.capstone_project.utils.enums.TermDuration;
import com.example.capstone_project.utils.enums.TermStatusCode;
import com.example.capstone_project.utils.exception.InvalidInputException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {
    @Mock
    private UserAuthorityRepository userAuthorityRepository;
    @Mock
    private UserDetailRepository userDetailRepository;
    @Mock
    private FinancialReportRepository financialReportRepository;
    @Mock
    private SecurityContext securityContext = mock(SecurityContext.class);
    @Mock
    private Authentication authentication = mock(Authentication.class);
    @InjectMocks
    private FinancialReportServiceImpl reportService;
    @Mock
    private FinancialPlanExpenseRepository expenseRepository;
    @Mock
    private ExpenseStatusRepository expenseStatusRepository;


    @Nested
    class approvalExpensesTest {
        private Long adminId;
        private Long accountantId;
        private Long staffId;
        private UserDetail adminDetail;
        private UserDetail accountantDetail;
        private UserDetail staffDetail;
        private Term term;
        private FinancialPlan plan;
        private ReportStatus reportStatus;
        private FinancialReport report;
        private FinancialPlanExpense expense1, expense2, expense3;

        @BeforeEach
        public void setup() {
            adminId = 1L;
            accountantId = 2L;
            staffId = 3L;

            adminDetail = UserDetail.builder()
                    .departmentId(1L)
                    .roleCode(RoleCode.ADMIN.getValue()).build();

            accountantDetail = UserDetail.builder()
                    .departmentId(2L)
                    .roleCode(RoleCode.ACCOUNTANT.getValue()).build();

            staffDetail = UserDetail.builder()
                    .departmentId(1L)
                    .roleCode(RoleCode.FINANCIAL_STAFF.getValue()).build();

            Department department = Department.builder()
                    .id(1L)
                    .name("IT department")
                    .build();

            TermStatus termStatus = TermStatus.
                    builder()
                    .id(1L).
                    name("Not started")
                    .code(TermStatusCode.NEW).build();

            term = Term.builder()
                    .id(8L)
                    .name("August 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 8, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 8, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 8, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 8, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 8, 25, 0, 0, 0)))
                    .status(termStatus)
                    .build();


            plan = FinancialPlan.builder()
                    .name("Financial Plan Mock")
                    .term(term)
                    .department(department)
                    .build();

            reportStatus = ReportStatus.builder().build();

            report = FinancialReport.builder()
                    .name(term.getName() + "_Report")
                    .expectedCost(BigDecimal.valueOf(901660487L))
                    .actualCost(BigDecimal.valueOf(616579382))
                    .month(term.getFinalEndTermDate().toLocalDate())
                    .status(reportStatus)
                    .term(term)
                    .build();

            expense1 = FinancialPlanExpense.builder()
                    .name("Expense A")
                    .unitPrice(BigDecimal.valueOf(10000L))
                    .amount(5)
                    .project(Project.builder().build())
                    .supplier(Supplier.builder().build())
                    .pic(User.builder().build())
                    .status(ExpenseStatus.builder().build())
                    .costType(CostType.builder().build())
                    .currency(Currency.builder().build())
                    .build();
        }

        @Test
        public void approvalExpenses_Unauthorized() throws Exception {
            long planId = 2L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(adminId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(adminId.intValue());
            when(userDetailRepository.get(adminId)).thenReturn(adminDetail);
            when(userAuthorityRepository.get(adminId)).thenReturn(Set.of());

            Exception exception = assertThrows(UnauthorizedException.class, () -> {
                reportService.approvalExpenses(planId, List.of(1L, 2L, 3L, 4L));
            });

            assertEquals("Unauthorized to approval expense", exception.getMessage());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
        }

        @Test
        public void approvalExpenses_InvalidInputException() throws Exception {
            long planId = 2L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userDetailRepository.get(accountantId)).thenReturn(accountantDetail);
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.APPROVE_PLAN.getValue()));

            Exception exception = assertThrows(InvalidInputException.class, () -> {
                reportService.approvalExpenses(planId, List.of(1L, 2L, 3L, 4L));
            });

            assertEquals("List expense Id invalid ", exception.getMessage());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
        }

        @Test
        public void approvalExpenses_Successful() throws Exception {
            long planId = 2L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userDetailRepository.get(accountantId)).thenReturn(accountantDetail);
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.APPROVE_PLAN.getValue()));
            when(expenseRepository.countListExpenseInReport(anyLong(), anyList(), any(), any())).thenReturn(4L);
            when(financialReportRepository.findById(anyLong())).thenReturn(Optional.ofNullable(report));
            when(expenseRepository.getReferenceById(anyLong())).thenReturn(expense1);

            reportService.approvalExpenses(planId, List.of(1L, 2L, 3L, 4L));

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
        }
    }

    @Nested
    class denyExpensesTest {
        private Long adminId;
        private Long accountantId;
        private Long staffId;
        private UserDetail adminDetail;
        private UserDetail accountantDetail;
        private UserDetail staffDetail;
        private Term term;
        private FinancialPlan plan;
        private ReportStatus reportStatus;
        private FinancialReport report;
        private FinancialPlanExpense expense1, expense2, expense3;

        @BeforeEach
        public void setup() {
            adminId = 1L;
            accountantId = 2L;
            staffId = 3L;

            adminDetail = UserDetail.builder()
                    .departmentId(1L)
                    .roleCode(RoleCode.ADMIN.getValue()).build();

            accountantDetail = UserDetail.builder()
                    .departmentId(2L)
                    .roleCode(RoleCode.ACCOUNTANT.getValue()).build();

            staffDetail = UserDetail.builder()
                    .departmentId(1L)
                    .roleCode(RoleCode.FINANCIAL_STAFF.getValue()).build();

            Department department = Department.builder()
                    .id(1L)
                    .name("IT department")
                    .build();

            TermStatus termStatus = TermStatus.
                    builder()
                    .id(1L).
                    name("Not started")
                    .code(TermStatusCode.NEW).build();

            term = Term.builder()
                    .id(8L)
                    .name("August 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 8, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 8, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 8, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 8, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 8, 25, 0, 0, 0)))
                    .status(termStatus)
                    .build();


            plan = FinancialPlan.builder()
                    .name("Financial Plan Mock")
                    .term(term)
                    .department(department)
                    .build();

            reportStatus = ReportStatus.builder().build();

            report = FinancialReport.builder()
                    .name(term.getName() + "_Report")
                    .expectedCost(BigDecimal.valueOf(901660487L))
                    .actualCost(BigDecimal.valueOf(616579382))
                    .month(term.getFinalEndTermDate().toLocalDate())
                    .status(reportStatus)
                    .term(term)
                    .build();

            expense1 = FinancialPlanExpense.builder()
                    .name("Expense A")
                    .unitPrice(BigDecimal.valueOf(10000L))
                    .amount(5)
                    .project(Project.builder().build())
                    .supplier(Supplier.builder().build())
                    .pic(User.builder().build())
                    .status(ExpenseStatus.builder().build())
                    .costType(CostType.builder().build())
                    .currency(Currency.builder().build())
                    .build();
        }

        @Test
        public void denyExpenses_Unauthorized() throws Exception {
            long planId = 2L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(adminId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(adminId.intValue());
            when(userDetailRepository.get(adminId)).thenReturn(adminDetail);
            when(userAuthorityRepository.get(adminId)).thenReturn(Set.of());

            Exception exception = assertThrows(UnauthorizedException.class, () -> {
                reportService.denyExpenses(planId, List.of(1L, 2L, 3L, 4L));
            });

            assertEquals("Unauthorized to approval expense", exception.getMessage());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
        }

        @Test
        public void denyExpenses_InvalidInputException() throws Exception {
            long planId = 2L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userDetailRepository.get(accountantId)).thenReturn(accountantDetail);
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.APPROVE_PLAN.getValue()));

            Exception exception = assertThrows(InvalidInputException.class, () -> {
                reportService.denyExpenses(planId, List.of(1L, 2L, 3L, 4L));
            });

            assertEquals("List expense Id invalid ", exception.getMessage());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
        }

        @Test
        public void denyExpenses_Successful() throws Exception {
            long planId = 2L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userDetailRepository.get(accountantId)).thenReturn(accountantDetail);
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.APPROVE_PLAN.getValue()));
            when(expenseRepository.countListExpenseInReport(anyLong(), anyList(), any(), any())).thenReturn(4L);
            when(expenseRepository.getReferenceById(anyLong())).thenReturn(expense1);

            reportService.denyExpenses(planId, List.of(1L, 2L, 3L, 4L));

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
        }
    }
}
