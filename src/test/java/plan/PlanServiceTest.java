package plan;

import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.redis.UserDetailRepository;
import com.example.capstone_project.repository.result.ExpenseResult;
import com.example.capstone_project.repository.result.FileNameResult;
import com.example.capstone_project.repository.result.PlanVersionResult;
import com.example.capstone_project.service.impl.FinancialPlanServiceImpl;
import com.example.capstone_project.utils.enums.*;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.term.InvalidDateException;
import com.example.capstone_project.utils.helper.HandleFileHelper;
import com.example.capstone_project.utils.helper.UserHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlanServiceTest {
    @Mock
    private FinancialPlanFileExpenseRepository financialPlanFileExpenseRepository;
    @Mock
    private HandleFileHelper handleFileHelper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CurrencyRepository currencyRepository;
    @Mock
    private SupplierRepository supplierRepository;
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private CostTypeRepository costTypeRepository;
    @Mock
    private ExpenseStatusRepository expenseStatusRepository;
    @Mock
    private FinancialPlanRepository planRepository;
    @Mock
    private UserAuthorityRepository userAuthorityRepository;
    @Mock
    private UserDetailRepository userDetailRepository;
    @Mock
    private TermRepository termRepository;
    @Mock
    private FinancialPlanFileRepository financialPlanFileRepository;
    @Mock
    private FinancialPlanExpenseRepository expenseRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @InjectMocks
    private FinancialPlanServiceImpl planService;
    @Mock
    private SecurityContext securityContext = mock(SecurityContext.class);
    @Mock
    private Authentication authentication = mock(Authentication.class);

    @Nested
    class countDistinctTest {
        private Long adminId;
        private Long accountantId;
        private Long staffId;
        private UserDetail adminDetail;
        private UserDetail accountantDetail;
        private UserDetail staffDetail;
        private Term term;
        private FinancialPlan plan;

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

            Term term8 = Term.builder()
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

        }

        @Test
        public void countDistinct_Unauthorized() throws Exception {
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(adminId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(adminId.intValue());
            when(userDetailRepository.get(adminId)).thenReturn(adminDetail);
            when(userAuthorityRepository.get(adminId)).thenReturn(Set.of());

            Exception exception = assertThrows(UnauthorizedException.class, () -> {
                planService.countDistinct("query", 1L, 2L);
            });

            assertEquals("Unauthorized to view plan", exception.getMessage());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, never()).countDistinct(any(), any(), any());
        }

        @Test
        public void testCountDistinct_AllParametersPresent() throws Exception {
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userDetailRepository.get(accountantId)).thenReturn(accountantDetail);
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.VIEW_PLAN.getValue()));
            when(planRepository.countDistinct("query", 1L, 2L)).thenReturn(5L);

            long result = planService.countDistinct("query", 1L, 2L);

            assertEquals(5L, result);

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, times(1)).countDistinct("query", 1L, 2L);
        }

        @Test
        public void testCountDistinct_SomeParametersNull() throws Exception {
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userDetailRepository.get(accountantId)).thenReturn(accountantDetail);
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.VIEW_PLAN.getValue()));
            when(planRepository.countDistinct(null, null, 2L)).thenReturn(3L);

            long result = planService.countDistinct(null, null, 2L);

            assertEquals(3L, result);

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, times(1)).countDistinct(null, null, 2L);
        }

        @Test
        public void testCountDistinct_AllParametersNull() throws Exception {
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userDetailRepository.get(accountantId)).thenReturn(accountantDetail);
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.VIEW_PLAN.getValue()));
            when(planRepository.countDistinct(null, null, null)).thenReturn(3L);

            long result = planService.countDistinct(null, null, null);

            assertEquals(3L, result);

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, times(1)).countDistinct(null, null, null);
        }
    }

    @Nested
    class getPlanWithPaginationTest {
        private Long adminId;
        private Long accountantId;
        private Long staffId;
        private UserDetail adminDetail;
        private UserDetail accountantDetail;
        private UserDetail staffDetail;
        private Term term;
        private FinancialPlan plan1, plan2, plan3, plan4;

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

            Term term8 = Term.builder()
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

            plan1 = FinancialPlan.builder()
                    .id(1L)
                    .name("Financial Plan Mock")
                    .term(term)
                    .department(department)
                    .build();

            plan2 = FinancialPlan.builder()
                    .id(2L)
                    .name("Financial Plan Mock")
                    .term(term)
                    .department(department)
                    .build();

            plan3 = FinancialPlan.builder()
                    .id(3L)
                    .name("Financial Plan Mock")
                    .term(term)
                    .department(department)
                    .build();

            plan4 = FinancialPlan.builder()
                    .id(4L)
                    .name("Financial Plan Mock")
                    .term(term)
                    .department(department)
                    .build();

        }

        @Test
        public void getPlanWithPagination_Unauthorized() throws Exception {
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(adminId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(adminId.intValue());
            when(userDetailRepository.get(adminId)).thenReturn(adminDetail);
            when(userAuthorityRepository.get(adminId)).thenReturn(Set.of());

            Exception exception = assertThrows(UnauthorizedException.class, () -> {
                planService.getPlanWithPagination("query", 2L, 2L, 3, 10, "id", "asc");
            });

            assertEquals("Unauthorized to view plan", exception.getMessage());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, never()).getPlanWithPagination(any(), any(), any(), any());
        }

        @Test
        public void getPlanWithPagination_RoleAccountant_AllParametersPresent() throws Exception {
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userDetailRepository.get(accountantId)).thenReturn(accountantDetail);
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.VIEW_PLAN.getValue()));
            when(planRepository.getPlanWithPagination("query", 2L, 2L, PageRequest.of(3, 10, Sort.by("id").descending()))).thenReturn(List.of(plan1, plan2, plan3, plan4));

            List<FinancialPlan> plans = planService.getPlanWithPagination("query", 2L, 2L, 3, 10, "id", "desc");

            assertEquals(4L, plans.size());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, times(1)).getPlanWithPagination(any(), any(), any(), any());
        }

        @Test
        public void getPlanWithPagination_RoleAccountant_SomeParametersNull() throws Exception {
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userDetailRepository.get(accountantId)).thenReturn(accountantDetail);
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.VIEW_PLAN.getValue()));
            when(planRepository.getPlanWithPagination("", 2L, 2L, PageRequest.of(1, 10, Sort.by(Sort.Order.desc("updatedAt"), Sort.Order.desc("id"))))).thenReturn(List.of(plan1, plan2, plan3, plan4));

            List<FinancialPlan> plans = planService.getPlanWithPagination("", 2L, 2L, 1, null, "", "");

            assertEquals(4L, plans.size());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, times(1)).getPlanWithPagination(any(), any(), any(), any());
        }


        @Test
        public void getPlanWithPagination_RoleAccountant_AllParametersNull() throws Exception {
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userDetailRepository.get(accountantId)).thenReturn(accountantDetail);
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.VIEW_PLAN.getValue()));
            when(planRepository.getPlanWithPagination("", null, null, PageRequest.of(1, 10, Sort.by(Sort.Order.desc("updatedAt"), Sort.Order.desc("id"))))).thenReturn(List.of(plan1, plan2, plan3, plan4));

            List<FinancialPlan> plans = planService.getPlanWithPagination("", null, null, 1, 10, "", "");

            assertEquals(4L, plans.size());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, times(1)).getPlanWithPagination(any(), any(), any(), any());
            verify(planRepository, times(1)).getListPlanVersion(any(), any(), any());
        }

        @Test
        public void getPlanWithPagination_RoleStaff_AllParametersPresent() throws Exception {
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(staffId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(staffId.intValue());
            when(userDetailRepository.get(staffId)).thenReturn(staffDetail);
            when(userAuthorityRepository.get(staffId)).thenReturn(Set.of(AuthorityCode.VIEW_PLAN.getValue()));
            when(planRepository.getPlanWithPagination("query", 2L, staffDetail.getDepartmentId(), PageRequest.of(3, 10, Sort.by(Sort.Order.asc("id"))))).thenReturn(List.of(plan1, plan2, plan3, plan4));
            when(planRepository.getListPlanVersion("query", 2L, 1L)).thenReturn(List.of());

            List<FinancialPlan> plans = planService.getPlanWithPagination("query", 2L, 1L, 3, 10, "id", "asc");

            assertEquals(4L, plans.size());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, times(1)).getPlanWithPagination(anyString(), anyLong(), anyLong(), any());
        }

        @Test
        public void getPlanWithPagination_RoleStaff_SomeParametersPresent() throws Exception {
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(staffId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(staffId.intValue());
            when(userDetailRepository.get(staffId)).thenReturn(staffDetail);
            when(userAuthorityRepository.get(staffId)).thenReturn(Set.of(AuthorityCode.VIEW_PLAN.getValue()));
            when(planRepository.getPlanWithPagination(null, 2L, 1L, PageRequest.of(3, 10, Sort.by(Sort.Order.desc("updatedAt"), Sort.Order.desc("id"))))).thenReturn(List.of(plan1, plan2, plan3));

            List<FinancialPlan> plans = planService.getPlanWithPagination(null, 2L, 2L, 3, 10, null, null);

            assertEquals(3L, plans.size());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, times(1)).getPlanWithPagination(any(), any(), any(), any());
        }

        @Test
        public void getPlanWithPagination_RoleStaff_AllParametersNull() throws Exception {
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(staffId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(staffId.intValue());
            when(userDetailRepository.get(staffId)).thenReturn(staffDetail);
            when(userAuthorityRepository.get(staffId)).thenReturn(Set.of(AuthorityCode.VIEW_PLAN.getValue()));
            when(planRepository.getPlanWithPagination("", null, 1L, PageRequest.of(1, 10, Sort.by(Sort.Order.desc("updatedAt"), Sort.Order.desc("id"))))).thenReturn(List.of(plan1, plan2, plan3));

            List<FinancialPlan> plans = planService.getPlanWithPagination("", null, null, null, null, null, null);

            assertEquals(3L, plans.size());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, times(1)).getPlanWithPagination(any(), any(), any(), any());
        }
    }

    @Nested
    class createPlanTest {
        private Long adminId;
        private Long accountantId;
        private Long staffId;
        private UserDetail adminDetail;
        private UserDetail accountantDetail;
        private UserDetail staffDetail;
        private Term term;
        private FinancialPlan plan;
        private List<FinancialPlanExpense> expenses;
        private FinancialPlanExpense expense1, expense2, expense3, expense4;
        private CostType costType;
        private Project project;
        private Supplier supplier;
        private Currency currency;

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
                    .name("Test Term Available To Create New Plan")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0)))
                    .status(termStatus)
                    .build();

            plan = FinancialPlan.builder()
                    .id(1L)
                    .name("Financial Plan Mock")
                    .term(term)
                    .department(department)
                    .build();

            expenses = new ArrayList<>();

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

            expense2 = FinancialPlanExpense.builder()
                    .name("Expense B")
                    .unitPrice(BigDecimal.valueOf(40000L))
                    .amount(5)
                    .project(Project.builder().build())
                    .supplier(Supplier.builder().build())
                    .pic(User.builder().build())
                    .status(ExpenseStatus.builder().build())
                    .costType(CostType.builder().build())
                    .currency(Currency.builder().build())
                    .build();

            expense3 = FinancialPlanExpense.builder()
                    .name("Expense C")
                    .unitPrice(BigDecimal.valueOf(50000L))
                    .amount(5)
                    .project(Project.builder().build())
                    .supplier(Supplier.builder().build())
                    .pic(User.builder().build())
                    .status(ExpenseStatus.builder().build())
                    .costType(CostType.builder().build())
                    .currency(Currency.builder().build())
                    .build();

            expenses.add(expense1);
            expenses.add(expense2);
            expenses.add(expense3);

            costType = CostType.builder().build();
            project = Project.builder().build();
            supplier = Supplier.builder().build();
            currency = Currency.builder().build();
        }

        @Test
        public void createPlan_Unauthorized() {
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(adminId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(adminId.intValue());
            when(userAuthorityRepository.get(adminId)).thenReturn(Set.of());

            Exception exception = assertThrows(UnauthorizedException.class, () -> {
                planService.createPlan(plan, expenses, "file_Name", 2L);
            });

            assertEquals("Unauthorized to create plan", exception.getMessage());

            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(termRepository, never()).existsPlanOfDepartmentInTerm(anyLong(), anyLong());
            verify(planRepository, never()).save(any(FinancialPlan.class));
        }

        @Test
        public void createPlan_Successful() throws Exception {
            ExpenseStatus status = ExpenseStatus.builder().code(ExpenseStatusCode.NEW).build();
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userDetailRepository.get(accountantId)).thenReturn(accountantDetail);
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.IMPORT_PLAN.getValue()));
            when(termRepository.existsPlanOfDepartmentInTerm(accountantDetail.getDepartmentId(), term.getId())).thenReturn(false);
            when(termRepository.getReferenceById(term.getId())).thenReturn(term);
            when(expenseStatusRepository.getReferenceByCode(ExpenseStatusCode.NEW)).thenReturn(status);
            when(costTypeRepository.getReferenceById(any())).thenReturn(costType);
            when(projectRepository.getReferenceById(any())).thenReturn(project);
            when(supplierRepository.getReferenceById(any())).thenReturn(supplier);
            when(currencyRepository.getReferenceById(any())).thenReturn(currency);
            when(userRepository.getReferenceById(any())).thenReturn(User.builder().build());

            planService.createPlan(plan, expenses, "file_Name", 8L);

            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(termRepository, times(1)).existsPlanOfDepartmentInTerm(anyLong(), anyLong());
            verify(planRepository, times(1)).save(any(FinancialPlan.class));
        }
    }

    @Nested
    class deletePlanTest {
        private Long adminId;
        private Long accountantId;
        private Long staffId;
        private UserDetail adminDetail;
        private UserDetail accountantDetail;
        private UserDetail staffDetail;
        private Term term;
        private FinancialPlan plan;

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

        }

        @Test
        public void deletePlan_Unauthorized() {
            long planId = 1L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(adminId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(adminId.intValue());
            when(userAuthorityRepository.get(adminId)).thenReturn(Set.of());

            Exception exception = assertThrows(UnauthorizedException.class, () -> {
                planService.deletePlan(planId);
            });

            assertEquals("Unauthorized to delete plan", exception.getMessage());

            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, never()).findById(anyLong());
            verify(planRepository, never()).save(any(FinancialPlan.class));
        }

        @Test
        public void deletePlan_InvalidDate() {
            long planId = 1L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.DELETE_PLAN.getValue()));
            when(termRepository.getTermByPlanId(any())).thenReturn(term);

            Exception exception = assertThrows(InvalidDateException.class, () -> {
                planService.deletePlan(planId);
            });

            assertEquals("Can not delete plan in this time period", exception.getMessage());

            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, never()).findById(anyLong());
            verify(planRepository, never()).save(any(FinancialPlan.class));
        }

        @Test
        public void deletePlan_Successful() throws Exception {

            term = Term.builder()
                    .id(21L)
                    .name("Test Term Available To Create New Plan")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0)))
                    .build();

            long planId = 1L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.DELETE_PLAN.getValue()));
            when(termRepository.getTermByPlanId(any())).thenReturn(term);
            when(planRepository.findById(planId)).thenReturn(Optional.ofNullable(plan));
            planService.deletePlan(planId);


            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, times(1)).findById(anyLong());
            verify(planRepository, times(1)).save(any(FinancialPlan.class));
        }
    }

    @Nested
    class getPlanDetailByPlanIdTest {
        private Long adminId;
        private Long accountantId;
        private Long staffId;
        private UserDetail adminDetail;
        private UserDetail accountantDetail;
        private UserDetail staffDetail;
        private Term term;
        private FinancialPlan plan;

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

            Term term8 = Term.builder()
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

        }

        @Test
        public void getPlanDetailByPlanId_Unauthorized() {
            long planId = 1L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(adminId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(adminId.intValue());
            when(userAuthorityRepository.get(adminId)).thenReturn(Set.of());

            Exception exception = assertThrows(UnauthorizedException.class, () -> {
                planService.getPlanDetailByPlanId(planId);
            });

            assertEquals("Unauthorized to view plan", exception.getMessage());

            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, never()).getFinancialPlanById(anyLong());
        }

        @Test
        public void getPlanVersionById_Successful() {
            long planId = 1L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(adminId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(adminId.intValue());
            when(userAuthorityRepository.get(adminId)).thenReturn(Set.of(AuthorityCode.VIEW_PLAN.getValue()));
            when(planRepository.existsById(planId)).thenReturn(true);
            when(planRepository.getPlanVersionByPlanId(planId)).thenReturn(3);

            int result = planService.getPlanVersionById(planId);

            assertEquals(3, result);

            verify(planRepository, times(1)).getPlanVersionByPlanId(anyLong());
        }
    }

    @Nested
    class convertListExpenseAndMapToPlanTest {
        private Long adminId;
        private Long accountantId;
        private Long staffId;
        private UserDetail adminDetail;
        private UserDetail accountantDetail;
        private UserDetail staffDetail;
        private Term term;
        private FinancialPlan plan;

        private List<FinancialPlanExpense> expenses;

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

            expenses = new ArrayList<>();

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

            expense2 = FinancialPlanExpense.builder()
                    .name("Expense B")
                    .unitPrice(BigDecimal.valueOf(40000L))
                    .amount(5)
                    .project(Project.builder().build())
                    .supplier(Supplier.builder().build())
                    .pic(User.builder().build())
                    .status(ExpenseStatus.builder().build())
                    .costType(CostType.builder().build())
                    .currency(Currency.builder().build())
                    .build();

            expense3 = FinancialPlanExpense.builder()
                    .name("Expense C")
                    .unitPrice(BigDecimal.valueOf(50000L))
                    .amount(5)
                    .project(Project.builder().build())
                    .supplier(Supplier.builder().build())
                    .pic(User.builder().build())
                    .status(ExpenseStatus.builder().build())
                    .costType(CostType.builder().build())
                    .currency(Currency.builder().build())
                    .build();

            expenses.add(expense1);
            expenses.add(expense2);
            expenses.add(expense3);


        }

        @Test
        public void convertListExpenseAndMapToPlan_Unauthorized() throws Exception {
            long planId = 1L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(adminId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(adminId.intValue());
            when(userAuthorityRepository.get(adminId)).thenReturn(Set.of());

            Exception exception = assertThrows(UnauthorizedException.class, () -> {
                planService.convertListExpenseAndMapToPlan(planId, List.of());
            });

            assertEquals("Unauthorized to re-upload plan", exception.getMessage());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, never()).getDepartmentIdByPlanId(anyLong());
            verify(expenseRepository, never()).getListExpenseByPlanId(anyLong());
            verify(expenseRepository, never()).getLastExpenseCode(anyLong());
            verify(planRepository, never()).getCurrentVersionByPlanId(anyLong());
            verify(expenseRepository, never()).getReferenceById(anyLong());
        }

        @Test
        public void convertListExpenseAndMapToPlan_InvalidDate() throws Exception {
            long planId = 1L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.RE_UPLOAD_PLAN.getValue()));
            when(planRepository.existsById(anyLong())).thenReturn(true);
            when(termRepository.getTermByPlanId(planId)).thenReturn(term);

            Exception exception = assertThrows(InvalidDateException.class, () -> {
                planService.convertListExpenseAndMapToPlan(planId, List.of());
            });

            assertEquals("Can not re-upload plan in this time period", exception.getMessage());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, never()).getDepartmentIdByPlanId(anyLong());
            verify(expenseRepository, never()).getListExpenseByPlanId(anyLong());
            verify(expenseRepository, never()).getLastExpenseCode(anyLong());
            verify(planRepository, never()).getCurrentVersionByPlanId(anyLong());
            verify(expenseRepository, never()).getReferenceById(anyLong());
        }

        @Test
        public void convertListExpenseAndMapToPlan_InvalidDepartment() throws Exception {
            long planId = 1L;

            term = Term.builder()
                    .id(22L)
                    .name("Test Term Available To Re-upload Plan")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0)))
                    .allowReupload(true)
                    .build();

            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userDetailRepository.get(anyLong())).thenReturn(accountantDetail);
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.RE_UPLOAD_PLAN.getValue()));
            when(planRepository.existsById(anyLong())).thenReturn(true);
            when(termRepository.getTermByPlanId(planId)).thenReturn(term);

            Exception exception = assertThrows(UnauthorizedException.class, () -> {
                planService.convertListExpenseAndMapToPlan(planId, List.of());
            });

            assertEquals("User can't upload this plan because departmentId of plan not equal with departmentId of user", exception.getMessage());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, times(1)).getDepartmentIdByPlanId(anyLong());
            verify(expenseRepository, never()).getListExpenseByPlanId(anyLong());
            verify(expenseRepository, never()).getLastExpenseCode(anyLong());
            verify(planRepository, never()).getCurrentVersionByPlanId(anyLong());
            verify(expenseRepository, never()).getReferenceById(anyLong());
        }

        @Test
        public void convertListExpenseAndMapToPlan_Successful() throws Exception {
            long planId = 1L;

            term = Term.builder()
                    .id(22L)
                    .name("Test Term Available To Re-upload Plan")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0)))
                    .allowReupload(true)
                    .build();

            plan = FinancialPlan.builder()
                    .name("Financial Plan Mock")
                    .term(term)
                    .department(Department.builder().id(2L).build())
                    .build();

            ExpenseResult result = new ExpenseResult() {
                @Override
                public String getExpenseCode() {
                    return "Expense Code";
                }

                @Override
                public String getExpenseName() {
                    return "Expense Name";
                }

                @Override
                public LocalDateTime getDate() {
                    return LocalDateTime.now();
                }

                @Override
                public String getTermName() {
                    return "Term Name";
                }

                @Override
                public String getDepartmentName() {
                    return "Department Name";
                }

                @Override
                public String getCostTypeName() {
                    return "Cost Type Name";
                }

                @Override
                public BigDecimal getUnitPrice() {
                    return BigDecimal.valueOf(10543521L);
                }

                @Override
                public Integer getAmount() {
                    return 5;
                }

                @Override
                public BigDecimal getTotal() {
                    return BigDecimal.valueOf(5315345312L);
                }

                @Override
                public String getProjectName() {
                    return "Project Name";
                }

                @Override
                public String getSupplierName() {
                    return "Supplier Name";
                }

                @Override
                public String getPicName() {
                    return "Pic name";
                }

                @Override
                public String getNote() {
                    return "";
                }

                @Override
                public Long getExpenseId() {
                    return 2L;
                }

                @Override
                public ExpenseStatusCode getStatusCode() {
                    return ExpenseStatusCode.NEW;
                }

                @Override
                public Long getDepartmentId() {
                    return 2L;
                }

                @Override
                public Long getCostTypeId() {
                    return 3L;
                }

                @Override
                public Long getStatusId() {
                    return 1L;
                }

                @Override
                public String getCurrencyName() {
                    return "VND";
                }
            };

            PlanVersionResult planVersionResult = new PlanVersionResult() {
                @Override
                public Long getPlanId() {
                    return 2L;
                }

                @Override
                public Integer getVersion() {
                    return 3;
                }

                @Override
                public String getTermName() {
                    return "Term Name";
                }

                @Override
                public String getDepartmentName() {
                    return "Department Name";
                }
            };

            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userDetailRepository.get(anyLong())).thenReturn(accountantDetail);
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.RE_UPLOAD_PLAN.getValue()));
            when(planRepository.existsById(anyLong())).thenReturn(true);
            when(termRepository.getTermByPlanId(planId)).thenReturn(term);
            when(planRepository.getDepartmentIdByPlanId(planId)).thenReturn(2L);
            when(expenseRepository.getListExpenseByPlanId(planId)).thenReturn(List.of(result));
            when(planRepository.getCurrentVersionByPlanId(planId)).thenReturn(planVersionResult);
            when(planRepository.getReferenceById(planId)).thenReturn(plan);

            planService.convertListExpenseAndMapToPlan(planId, List.of());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, times(1)).getDepartmentIdByPlanId(anyLong());
            verify(expenseRepository, times(1)).getListExpenseByPlanId(anyLong());
            verify(expenseRepository, never()).getLastExpenseCode(anyLong());
            verify(planRepository, times(1)).getCurrentVersionByPlanId(anyLong());
            verify(expenseRepository, never()).getReferenceById(anyLong());
        }

    }

    @Nested
    class getListVersionWithPaginateTest {
        private Long adminId;
        private Long accountantId;
        private Long staffId;
        private UserDetail adminDetail;
        private UserDetail accountantDetail;
        private UserDetail staffDetail;
        private Term term;
        private FinancialPlan plan;

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

            Term term8 = Term.builder()
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

        }

        @Test
        public void getListVersionWithPaginate_Unauthorized() throws Exception {
            long planId = 1L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(adminId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(adminId.intValue());
            when(userDetailRepository.get(adminId)).thenReturn(adminDetail);
            when(userAuthorityRepository.get(adminId)).thenReturn(Set.of());

            Exception exception = assertThrows(UnauthorizedException.class, () -> {
                planService.getListVersionWithPaginate(planId, Pageable.unpaged());
            });

            assertEquals("Unauthorized to view plan", exception.getMessage());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, never()).getListVersionWithPaginate(anyLong(), any(Pageable.class));
            verify(planRepository, never()).getReferenceById(anyLong());
        }

        @Test
        public void getListVersionWithPaginate_Admin_Successful() throws Exception {
            long planId = 1L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userDetailRepository.get(accountantId)).thenReturn(accountantDetail);
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.VIEW_PLAN.getValue()));

            planService.getListVersionWithPaginate(planId, Pageable.unpaged());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, times(1)).getListVersionWithPaginate(anyLong(), any(Pageable.class));
            verify(planRepository, never()).getReferenceById(anyLong());
        }

        @Test
        public void getListVersionWithPaginate_Staff_Successful() throws Exception {
            long planId = 1L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(staffId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(staffId.intValue());
            when(userDetailRepository.get(staffId)).thenReturn(staffDetail);
            when(userAuthorityRepository.get(staffId)).thenReturn(Set.of(AuthorityCode.VIEW_PLAN.getValue()));
            when(planRepository.getReferenceById(planId)).thenReturn(plan);

            planService.getListVersionWithPaginate(planId, Pageable.unpaged());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, times(1)).getListVersionWithPaginate(anyLong(), any(Pageable.class));
            verify(planRepository, times(1)).getReferenceById(anyLong());
        }

    }

    @Nested
    class countDistinctListPlanVersionPagingTest {
        private Long adminId;
        private Long accountantId;
        private Long staffId;
        private UserDetail adminDetail;
        private UserDetail accountantDetail;
        private UserDetail staffDetail;
        private Term term;
        private FinancialPlan plan;

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

            Term term8 = Term.builder()
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

        }

    }

    @Nested
    class getBodyFileExcelXLSTest {
        private Long adminId;
        private Long accountantId;
        private Long staffId;
        private UserDetail adminDetail;
        private UserDetail accountantDetail;
        private UserDetail staffDetail;
        private Term term;
        private FinancialPlan plan;

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

            Term term8 = Term.builder()
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

        }

        @Test
        public void getBodyFileExcelXLS_Unauthorized() throws Exception {
            long fileId = 1L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(adminId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(adminId.intValue());
            when(userDetailRepository.get(adminId)).thenReturn(adminDetail);
            when(userAuthorityRepository.get(adminId)).thenReturn(Set.of());

            Exception exception = assertThrows(UnauthorizedException.class, () -> {
                planService.getBodyFileExcelXLS(fileId);
            });

            assertEquals("Unauthorized to download plan", exception.getMessage());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, never()).getListExpenseByFileId(anyLong());
            verify(departmentRepository, never()).getDepartmentIdByFileId(anyLong());
        }

        @Test
        public void getBodyFileExcelXLS_InvalidDepartment() throws Exception {
            long fileId = 1L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userDetailRepository.get(accountantId)).thenReturn(accountantDetail);
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.DOWNLOAD_PLAN.getValue()));

            Exception exception = assertThrows(UnauthorizedException.class, () -> {
                planService.getBodyFileExcelXLS(fileId);
            });

            assertEquals("User can't download this plan because departmentId of plan not equal with departmentId of user", exception.getMessage());
            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, never()).getListExpenseByFileId(anyLong());
            verify(departmentRepository, times(1)).getDepartmentIdByFileId(anyLong());
        }

        @Test
        public void getBodyFileExcelXLS_ResourceNotFoundException() throws Exception {
            long fileId = 1L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userDetailRepository.get(accountantId)).thenReturn(accountantDetail);
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.DOWNLOAD_PLAN.getValue()));
            when(departmentRepository.getDepartmentIdByFileId(fileId)).thenReturn(2L);

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
                planService.getBodyFileExcelXLS(fileId);
            });

            assertEquals("Not exist file = " + fileId + " or list expenses is empty", exception.getMessage());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, times(1)).getListExpenseByFileId(anyLong());
            verify(departmentRepository, times(1)).getDepartmentIdByFileId(anyLong());
        }

        @Test
        public void getBodyFileExcelXLS_Successful() throws Exception {
            long fileId = 1L;

            ExpenseResult expenseResult = new ExpenseResult() {
                @Override
                public String getExpenseCode() {
                    return null;
                }

                @Override
                public String getExpenseName() {
                    return null;
                }

                @Override
                public LocalDateTime getDate() {
                    return null;
                }

                @Override
                public String getTermName() {
                    return null;
                }

                @Override
                public String getDepartmentName() {
                    return null;
                }

                @Override
                public String getCostTypeName() {
                    return null;
                }

                @Override
                public BigDecimal getUnitPrice() {
                    return null;
                }

                @Override
                public Integer getAmount() {
                    return null;
                }

                @Override
                public BigDecimal getTotal() {
                    return null;
                }

                @Override
                public String getProjectName() {
                    return null;
                }

                @Override
                public String getSupplierName() {
                    return null;
                }

                @Override
                public String getPicName() {
                    return null;
                }

                @Override
                public String getNote() {
                    return null;
                }

                @Override
                public Long getExpenseId() {
                    return null;
                }

                @Override
                public ExpenseStatusCode getStatusCode() {
                    return null;
                }

                @Override
                public Long getDepartmentId() {
                    return null;
                }

                @Override
                public Long getCostTypeId() {
                    return null;
                }

                @Override
                public Long getStatusId() {
                    return null;
                }

                @Override
                public String getCurrencyName() {
                    return null;
                }
            };
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userDetailRepository.get(accountantId)).thenReturn(accountantDetail);
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.DOWNLOAD_PLAN.getValue()));
            when(departmentRepository.getDepartmentIdByFileId(fileId)).thenReturn(2L);
            when(planRepository.getListExpenseByFileId(fileId)).thenReturn(List.of(expenseResult));

            planService.getBodyFileExcelXLS(fileId);

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, times(1)).getListExpenseByFileId(anyLong());
            verify(departmentRepository, times(1)).getDepartmentIdByFileId(anyLong());
        }
    }

    @Nested
    class getBodyFileExcelXLSXTest {
        private Long adminId;
        private Long accountantId;
        private Long staffId;
        private UserDetail adminDetail;
        private UserDetail accountantDetail;
        private UserDetail staffDetail;
        private Term term;
        private FinancialPlan plan;

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

            Term term8 = Term.builder()
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

        }

        @Test
        public void getBodyFileExcelXLSX_Unauthorized() throws Exception {
            long fileId = 1L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(adminId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(adminId.intValue());
            when(userDetailRepository.get(adminId)).thenReturn(adminDetail);
            when(userAuthorityRepository.get(adminId)).thenReturn(Set.of());

            Exception exception = assertThrows(UnauthorizedException.class, () -> {
                planService.getBodyFileExcelXLS(fileId);
            });

            assertEquals("Unauthorized to download plan", exception.getMessage());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, never()).getListExpenseByFileId(anyLong());
            verify(departmentRepository, never()).getDepartmentIdByFileId(anyLong());
        }

    }

    @Nested
    class generateXLSXFileNameTest {
        private Long adminId;
        private Long accountantId;
        private Long staffId;
        private UserDetail adminDetail;
        private UserDetail accountantDetail;
        private UserDetail staffDetail;
        private Term term;
        private FinancialPlan plan;

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

            Term term8 = Term.builder()
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

        }

        @Test
        public void generateXLSXFileName_ResourceNotFound() {
            long fileId = 1L;
            int planId = 3;
            when(planRepository.getPlanIdByFileId(fileId)).thenReturn(planId);
            when(financialPlanFileRepository.generateFileName(planId)).thenReturn(null);

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
                planService.generateXLSXFileName(fileId);
            });

            assertEquals("Not exist file id = " + fileId, exception.getMessage());

            verify(planRepository, times(1)).getPlanIdByFileId(anyLong());
            verify(financialPlanFileRepository, times(1)).generateFileName(anyInt());
        }

        @Test
        public void generateXLSXFileName_Accountant_Successful() {
            long fileId = 1L;
            int planId = 3;
            FileNameResult fileNameResult = new FileNameResult() {
                @Override
                public Long getFileId() {
                    return 1L;
                }

                @Override
                public String getTermName() {
                    return "Term Name";
                }

                @Override
                public String getPlanName() {
                    return "Plan Name";
                }

                @Override
                public String getVersion() {
                    return "2";
                }
            };

            when(planRepository.getPlanIdByFileId(fileId)).thenReturn(planId);
            when(financialPlanFileRepository.generateFileName(planId)).thenReturn(List.of(fileNameResult));

            planService.generateXLSXFileName(fileId);

            verify(planRepository, times(1)).getPlanIdByFileId(anyLong());
            verify(financialPlanFileRepository, times(1)).generateFileName(anyInt());
        }

        @Test
        public void generateXLSXFileName_Staff_Successful() {
            long fileId = 1L;
            int planId = 3;
            FileNameResult fileNameResult = new FileNameResult() {
                @Override
                public Long getFileId() {
                    return 1L;
                }

                @Override
                public String getTermName() {
                    return "Term Name";
                }

                @Override
                public String getPlanName() {
                    return "Plan Name";
                }

                @Override
                public String getVersion() {
                    return "2";
                }
            };

            when(planRepository.getPlanIdByFileId(fileId)).thenReturn(planId);
            when(financialPlanFileRepository.generateFileName(planId)).thenReturn(List.of(fileNameResult));

            planService.generateXLSXFileName(fileId);

            verify(planRepository, times(1)).getPlanIdByFileId(anyLong());
            verify(financialPlanFileRepository, times(1)).generateFileName(anyInt());
        }
    }

    @Nested
    class generateXLSFileNameTest {
        private Long adminId;
        private Long accountantId;
        private Long staffId;
        private UserDetail adminDetail;
        private UserDetail accountantDetail;
        private UserDetail staffDetail;
        private Term term;
        private FinancialPlan plan;

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

            Term term8 = Term.builder()
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

        }

        @Test
        public void generateXLSFileName_ResourceNotFound() {
            long fileId = 1L;
            int planId = 3;
            when(planRepository.getPlanIdByFileId(fileId)).thenReturn(planId);
            when(financialPlanFileRepository.generateFileName(planId)).thenReturn(null);

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
                planService.generateXLSFileName(fileId);
            });

            assertEquals("Not exist file id = " + fileId, exception.getMessage());

            verify(planRepository, times(1)).getPlanIdByFileId(anyLong());
            verify(financialPlanFileRepository, times(1)).generateFileName(anyInt());
        }

    }

    @Nested
    class getLastVersionBodyFileExcelXLSTest {
        private Long adminId;
        private Long accountantId;
        private Long staffId;
        private UserDetail adminDetail;
        private UserDetail accountantDetail;
        private UserDetail staffDetail;
        private Term term;
        private FinancialPlan plan;

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

            Term term8 = Term.builder()
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

        }

        @Test
        public void getLastVersionBodyFileExcelXLS_ResourceNotFound() {
            long fileId = 1L;
            int planId = 3;
            when(planRepository.getPlanIdByFileId(fileId)).thenReturn(planId);
            when(financialPlanFileRepository.generateFileName(planId)).thenReturn(null);

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
                planService.generateXLSFileName(fileId);
            });

            assertEquals("Not exist file id = " + fileId, exception.getMessage());

            verify(planRepository, times(1)).getPlanIdByFileId(anyLong());
            verify(financialPlanFileRepository, times(1)).generateFileName(anyInt());
        }

        @Test
        public void getLastVersionBodyFileExcelXLS_Unauthorized() throws Exception {
            long fileId = 1L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(adminId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(adminId.intValue());
            when(userDetailRepository.get(adminId)).thenReturn(adminDetail);
            when(userAuthorityRepository.get(adminId)).thenReturn(Set.of());

            Exception exception = assertThrows(UnauthorizedException.class, () -> {
                planService.getLastVersionBodyFileExcelXLS(fileId);
            });

            assertEquals("Unauthorized to download plan", exception.getMessage());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, never()).getListExpenseByFileId(anyLong());
            verify(departmentRepository, never()).getDepartmentIdByFileId(anyLong());
        }

    }

    @Nested
    class generateXLSFileNameByPlanIdTest {
        private Long adminId;
        private Long accountantId;
        private Long staffId;
        private UserDetail adminDetail;
        private UserDetail accountantDetail;
        private UserDetail staffDetail;
        private Term term;
        private FinancialPlan plan;

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

            Term term8 = Term.builder()
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

        }

        @Test
        public void generateXLSFileNameByPlanId_ResourceNotFound() {
            long planId = 3;
            when(financialPlanFileRepository.getLastVersionFileName(planId)).thenReturn(null);

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
                planService.generateXLSFileNameByPlanId(planId);
            });

            assertEquals("Not found any file of plan have id = " + planId, exception.getMessage());

            verify(financialPlanFileRepository, times(1)).getLastVersionFileName(anyLong());
        }

        @Test
        public void generateXLSFileNameByPlanId_Successful() {
            long planId = 3;

            FileNameResult fileNameResult = new FileNameResult() {
                @Override
                public Long getFileId() {
                    return 2L;
                }

                @Override
                public String getTermName() {
                    return "Term Name";
                }

                @Override
                public String getPlanName() {
                    return "Plan Name";
                }

                @Override
                public String getVersion() {
                    return "2";
                }
            };

            when(financialPlanFileRepository.getLastVersionFileName(planId)).thenReturn(fileNameResult);

            planService.generateXLSFileNameByPlanId(planId);

            verify(financialPlanFileRepository, times(1)).getLastVersionFileName(anyLong());
        }

    }

    @Nested
    class getLastVersionBodyFileExcelXLSXTest {
        private Long adminId;
        private Long accountantId;
        private Long staffId;
        private UserDetail adminDetail;
        private UserDetail accountantDetail;
        private UserDetail staffDetail;
        private Term term;
        private FinancialPlan plan;

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

        }

        @Test
        public void getLastVersionBodyFileExcelXLSX_Unauthorized() throws Exception {
            long fileId = 1L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(adminId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(adminId.intValue());
            when(userDetailRepository.get(adminId)).thenReturn(adminDetail);
            when(userAuthorityRepository.get(adminId)).thenReturn(Set.of());

            Exception exception = assertThrows(UnauthorizedException.class, () -> {
                planService.getLastVersionBodyFileExcelXLSX(fileId);
            });

            assertEquals("Unauthorized to download plan", exception.getMessage());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, never()).getListExpenseByFileId(anyLong());
            verify(departmentRepository, never()).getDepartmentIdByFileId(anyLong());
        }

        @Test
        public void getLastVersionBodyFileExcelXLSX_ResourceNotFoundException() throws Exception {
            long fileId = 1L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userDetailRepository.get(accountantId)).thenReturn(accountantDetail);
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.DOWNLOAD_PLAN.getValue()));

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
                planService.getLastVersionBodyFileExcelXLSX(fileId);
            });

            assertEquals("Not found any plan have id = " + fileId, exception.getMessage());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, never()).getListExpenseByFileId(anyLong());
            verify(departmentRepository, never()).getDepartmentIdByFileId(anyLong());
        }

        @Test
        public void getLastVersionBodyFileExcelXLSX_Accountant_Successful() throws Exception {
            long fileId = 1L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(accountantId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(accountantId.intValue());
            when(userDetailRepository.get(accountantId)).thenReturn(accountantDetail);
            when(userAuthorityRepository.get(accountantId)).thenReturn(Set.of(AuthorityCode.DOWNLOAD_PLAN.getValue()));
            when(planRepository.existsById(fileId)).thenReturn(true);

            planService.getLastVersionBodyFileExcelXLSX(fileId);


            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, never()).getListExpenseByFileId(anyLong());
            verify(departmentRepository, never()).getDepartmentIdByFileId(anyLong());
        }

        @Test
        public void getLastVersionBodyFileExcelXLSX_Staff_InvalidDepartment() throws Exception {
            long fileId = 1L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(staffId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(staffId.intValue());
            when(userDetailRepository.get(staffId)).thenReturn(staffDetail);
            when(userAuthorityRepository.get(staffId)).thenReturn(Set.of(AuthorityCode.DOWNLOAD_PLAN.getValue()));
            when(planRepository.existsById(fileId)).thenReturn(true);

            Exception exception = assertThrows(UnauthorizedException.class, () -> {
                planService.getLastVersionBodyFileExcelXLSX(fileId);
            });

            assertEquals("User can't download this plan because departmentId of plan not equal with departmentId of user", exception.getMessage());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, never()).getListExpenseByFileId(anyLong());
            verify(departmentRepository, never()).getDepartmentIdByFileId(anyLong());
        }

        @Test
        public void getLastVersionBodyFileExcelXLSX_Staff_Successful() throws Exception {
            long planId = 1L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(staffId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(staffId.intValue());
            when(userDetailRepository.get(staffId)).thenReturn(staffDetail);
            when(userAuthorityRepository.get(staffId)).thenReturn(Set.of(AuthorityCode.DOWNLOAD_PLAN.getValue()));
            when(planRepository.existsById(planId)).thenReturn(true);
            when(departmentRepository.getDepartmentIdByPlanId(planId)).thenReturn(1L);
            when(planRepository.getListExpenseByPlanId(planId)).thenReturn(List.of());

            planService.getLastVersionBodyFileExcelXLSX(planId);

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, never()).getListExpenseByFileId(anyLong());
            verify(departmentRepository, never()).getDepartmentIdByFileId(anyLong());
        }
    }

    @Nested
    class generateXLSXFileNameByPlanIdTest {
        private Long adminId;
        private Long accountantId;
        private Long staffId;
        private UserDetail adminDetail;
        private UserDetail accountantDetail;
        private UserDetail staffDetail;
        private Term term;
        private FinancialPlan plan;

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

        }

        @Test
        public void generateXLSXFileNameByPlanId_ResourceNotFound() {
            long planId = 3;
            when(financialPlanFileRepository.getLastVersionFileName(planId)).thenReturn(null);

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
                planService.generateXLSXFileNameByPlanId(planId);
            });

            assertEquals("Not found any file of plan have id = " + planId, exception.getMessage());

            verify(financialPlanFileRepository, times(1)).getLastVersionFileName(anyLong());
        }

    }

    @Nested
    class getListExpenseWithPaginateTest {
        private Long adminId;
        private Long accountantId;
        private Long staffId;
        private UserDetail adminDetail;
        private UserDetail accountantDetail;
        private UserDetail staffDetail;
        private Term term;
        private FinancialPlan plan;

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

        }

        @Test
        public void getListExpenseWithPaginate_ResourceNotFound() {
            long planId = 3;
            when(financialPlanFileRepository.getLastVersionFileName(planId)).thenReturn(null);

            Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
                planService.generateXLSXFileNameByPlanId(planId);
            });

            assertEquals("Not found any file of plan have id = " + planId, exception.getMessage());

            verify(financialPlanFileRepository, times(1)).getLastVersionFileName(anyLong());
        }

        @Test
        public void getListExpenseWithPaginate_Unauthorized() throws Exception {
            long planId = 2L;
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(adminId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(adminId.intValue());
            when(userDetailRepository.get(adminId)).thenReturn(adminDetail);
            when(userAuthorityRepository.get(adminId)).thenReturn(Set.of());

            Exception exception = assertThrows(UnauthorizedException.class, () -> {
                planService.getListExpenseWithPaginate(planId, "query", 1L, 1L, 1L, 1L, 1L, 1L, null);
            });

            assertEquals("Unauthorized to view plan", exception.getMessage());

            verify(userDetailRepository, times(1)).get(anyLong());
            verify(userAuthorityRepository, times(1)).get(anyLong());
            verify(planRepository, never()).existsById(anyLong());
            verify(planRepository, never()).getDepartmentIdByPlanId(anyLong());
            verify(expenseRepository, never()).getListExpenseWithPaginate(anyLong(), anyString(), anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), any(Pageable.class));
        }
    }

    @Nested
    class countDistinctListExpenseWithPaginateTest {
        private Long adminId;
        private Long accountantId;
        private Long staffId;
        private UserDetail adminDetail;
        private UserDetail accountantDetail;
        private UserDetail staffDetail;
        private Term term;
        private FinancialPlan plan;

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

            Term term8 = Term.builder()
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
        }

        @Test
        public void countDistinctListExpenseWithPaginate_Successful() throws Exception {
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(adminId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(adminId.intValue());
            when(userAuthorityRepository.get(adminId)).thenReturn(Set.of(AuthorityCode.VIEW_PLAN.getValue()));

            when(expenseRepository.countDistinctListExpenseWithPaginate("query", 1L, 1L, 1L, 1L, 1L, 1L)).thenReturn(7L);

            long result = planService.countDistinctListExpenseWithPaginate("query", 1L, 1L, 1L, 1L, 1L, 1L);

            assertEquals(7L, result);

            verify(expenseRepository, times(1)).countDistinctListExpenseWithPaginate(anyString(), anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), anyLong());
        }

        @Test
        public void countDistinctListExpenseWithPaginate_Unauthorized() throws Exception {
            // Mock the SecurityContextHolder to return a valid user ID
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(adminId);

            SecurityContextHolder.setContext(securityContext);

            when(UserHelper.getUserId()).thenReturn(adminId.intValue());
            when(userAuthorityRepository.get(adminId)).thenReturn(Set.of());

            Exception exception = assertThrows(UnauthorizedException.class, () -> {
                long result = planService.countDistinctListExpenseWithPaginate("query", 1L, 1L, 1L, 1L, 1L, 1L);
            });

            assertEquals("Unauthorized to view plan", exception.getMessage());

            verify(expenseRepository, never()).countDistinctListExpenseWithPaginate(anyString(), anyLong(), anyLong(), anyLong(), anyLong(), anyLong(), anyLong());
        }
    }

}
