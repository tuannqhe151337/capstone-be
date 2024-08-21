package com.example.capstone_project.config;

import com.example.capstone_project.entity.*;
import com.example.capstone_project.entity.Currency;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.repository.result.AnnualReportResult;
import com.example.capstone_project.repository.result.CostStatisticalByCurrencyResult;
import com.example.capstone_project.repository.result.PaginateExchange;
import com.example.capstone_project.repository.result.ReportResult;
import com.example.capstone_project.service.result.CostResult;
import com.example.capstone_project.service.result.TotalCostByCurrencyResult;
import com.example.capstone_project.utils.enums.*;
import com.example.capstone_project.utils.mapper.annual.AnnualReportMapperImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Configuration
@RequiredArgsConstructor
public class SeedConfiguration {
    private final PasswordEncoder passwordEncoder;
    private final CurrencyExchangeRateRepository currencyExchangeRateRepository;
    private final FinancialPlanRepository planRepository;
    private final ReportStatisticRepository reportStatisticRepository;
    private final DepartmentRepository departmentRepository;
    private final CostTypeRepository costTypeRepository;
    private final FinancialReportRepository financialReportRepository;

    @Bean
    public CommandLineRunner commandLineRunner(
            UserRepository userRepository,
            RoleRepository roleRepository,
            AuthorityRepository authorityRepository,
            RoleAuthorityRepository roleAuthorityRepository,
            DepartmentRepository departmentRepository,
            PositionRepository positionRepository,
            UserSettingRepository userSettingRepository,
            FinancialPlanRepository planRepository,
            TermRepository termRepository,
            TermStatusRepository termStatusRepository,
            ReportStatusRepository reportStatusRepository,
            CostTypeRepository costTypeRepository,
            ExpenseStatusRepository expenseStatusRepository,
            FinancialPlanFileRepository financialPlanFileRepository,
            FinancialPlanFileExpenseRepository financialPlanFileExpenseRepository,
            FinancialPlanExpenseRepository financialPlanExpenseRepository,
            FinancialReportRepository financialReportRepository,
            AnnualReportRepository annualReportRepository,
            MonthlyReportSummaryRepository monthlyReportSummaryRepository,
            SupplierRepository supplierRepository,
            ProjectRepository projectRepository,
            TermIntervalRepository termIntervalRepository,
            CurrencyRepository currencyRepository,
            CurrencyExchangeRateRepository currencyExchangeRateRepository

    ) {
        return args -> {
            if (System.getenv("SPRING_PROFILES_ACTIVE") != null && System.getenv("SPRING_PROFILES_ACTIVE").equals("prod")) {
                return;
            }

            //Term Status - fixed code
            TermStatus termStatus = TermStatus.
                    builder()
                    .id(1L).
                    name("New")
                    .code(TermStatusCode.NEW).build();

            //Term Status - fixed code
            TermStatus termStatus2 = TermStatus.
                    builder()
                    .id(2L).
                    name("In progress")
                    .code(TermStatusCode.IN_PROGRESS).build();

            //Term Status - fixed code
            TermStatus termStatus3 = TermStatus.
                    builder()
                    .id(3L).
                    name("Closed")
                    .code(TermStatusCode.CLOSED).build();

            termStatusRepository.saveAll(List.of(termStatus, termStatus2, termStatus3));

            // Term interval date
            TermInterval termInterval = TermInterval
                    .builder()
                    .id(1L)
                    .startTermDate(25)
                    .endTermInterval(5)
                    .startReuploadInterval(20)
                    .endReuploadInterval(3)
                    .build();
            termIntervalRepository.save(termInterval);

            // Department
            Department itDepartment = Department.builder()
                    .id(1L)
                    .name("IT Department")
                    .build();

            Department hrDepartment = Department.builder()
                    .id(2L)
                    .name("HR Department")
                    .build();

            Department financeDepartment = Department.builder()
                    .id(3L)
                    .name("Finance Department")
                    .build();

            Department communicationDepartment = Department.builder()
                    .id(4L)
                    .name("Communication Department")
                    .build();

            Department marketingDepartment = Department.builder()
                    .id(5L)
                    .name("Marketing Department")
                    .build();

            Department accountingDepartment = Department.builder()
                    .id(6L)
                    .name("Accounting Department")
                    .build();

            departmentRepository.saveAll(List.of(itDepartment, hrDepartment, financeDepartment, communicationDepartment, marketingDepartment, accountingDepartment));

            // Position
            Position techlead = Position.builder()
                    .name("Techlead")
                    .build();

            Position staff = Position.builder()
                    .name("Staff")
                    .build();

            Position juniorDev = Position.builder()
                    .name("Junior development")
                    .build();

            positionRepository.saveAll(List.of(techlead, staff, juniorDev));

            // Authority
            Authority createUser = Authority.builder()
                    .code(AuthorityCode.CREATE_NEW_USER)
                    .name("Create new user")
                    .build();

            Authority viewListUsers = Authority.builder()
                    .code(AuthorityCode.VIEW_LIST_USERS)
                    .name("View list users")
                    .build();

            Authority deleteUser = Authority.builder()
                    .code(AuthorityCode.DELETE_USER)
                    .name("Delete user")
                    .build();

            Authority editUser = Authority.builder()
                    .code(AuthorityCode.EDIT_USER)
                    .name("Edit user")
                    .build();

            Authority activateUser = Authority.builder()
                    .code(AuthorityCode.ACTIVATE_USER)
                    .name("Activate user")
                    .build();

            Authority deactivateUser = Authority.builder()
                    .code(AuthorityCode.DEACTIVATE_USER)
                    .name("Deactivate user")
                    .build();

            Authority viewUserDetail = Authority.builder()
                    .code(AuthorityCode.VIEW_USER_DETAILS)
                    .name("View user detail")
                    .build();

            Authority createTerm = Authority.builder()
                    .code(AuthorityCode.CREATE_TERM)
                    .name("Create term")
                    .build();

            Authority editTerm = Authority.builder()
                    .code(AuthorityCode.EDIT_TERM)
                    .name("Edit term")
                    .build();

            Authority viewTerm = Authority.builder()
                    .code(AuthorityCode.VIEW_TERM)
                    .name("View term")
                    .build();

            Authority startTerm = Authority.builder()
                    .code(AuthorityCode.START_TERM)
                    .name("Start term")
                    .build();

            Authority deleteTerm = Authority.builder()
                    .code(AuthorityCode.DELETE_TERM)
                    .name("Delete term")
                    .build();

            Authority importPlan = Authority.builder()
                    .code(AuthorityCode.IMPORT_PLAN)
                    .name("Import plan")
                    .build();

            Authority reUploadPlan = Authority.builder()
                    .code(AuthorityCode.RE_UPLOAD_PLAN)
                    .name("Reupload plan")
                    .build();

            Authority submitPlanForReview = Authority.builder()
                    .code(AuthorityCode.SUBMIT_PLAN_FOR_REVIEW)
                    .name("Submit plan for review")
                    .build();

            Authority deletePlan = Authority.builder()
                    .code(AuthorityCode.DELETE_PLAN)
                    .name("Delete plan")
                    .build();

            Authority downloadPlan = Authority.builder()
                    .code(AuthorityCode.DOWNLOAD_PLAN)
                    .name("Download plan")
                    .build();

            Authority approvePlan = Authority.builder()
                    .code(AuthorityCode.APPROVE_PLAN)
                    .name("Approve plan")
                    .build();

            Authority viewPlan = Authority.builder()
                    .code(AuthorityCode.VIEW_PLAN)
                    .name("View plan")
                    .build();

            Authority viewReport = Authority.builder() // Monthly, Quarterly, Half-year
                    .code(AuthorityCode.VIEW_REPORT)
                    .name("View report")
                    .build();

            Authority downloadReport = Authority.builder() // Monthly, Quarterly, Half-year
                    .code(AuthorityCode.DOWNLOAD_REPORT)
                    .name("Download report")
                    .build();

            Authority deleteReport = Authority.builder()
                    .code(AuthorityCode.DELETE_REPORT)
                    .name("Delete report")
                    .build();

            Authority viewAnnualReport = Authority.builder()
                    .code(AuthorityCode.VIEW_ANNUAL_REPORT)
                    .name("View annual report")
                    .build();

            Authority downloadAnnualReport = Authority.builder()
                    .code(AuthorityCode.DOWNLOAD_ANNUAL_REPORT)
                    .name("Download annual report")
                    .build();

            Authority createNewDepartment = Authority.builder()
                    .code(AuthorityCode.CREATE_NEW_DEPARTMENT)
                    .name("Create new department")
                    .build();

            Authority updateDepartment = Authority.builder()
                    .code(AuthorityCode.UPDATE_DEPARTMENT)
                    .name("Update department")
                    .build();

            Authority deleteDepartment = Authority.builder()
                    .code(AuthorityCode.DELETE_DEPARTMENT)
                    .name("Delete department")
                    .build();

            Authority createNewCostType = Authority.builder()
                    .code(AuthorityCode.CREATE_NEW_COST_TYPE)
                    .name("Create new cost type")
                    .build();

            Authority updateCostType = Authority.builder()
                    .code(AuthorityCode.UPDATE_COST_TYPE)
                    .name("Update cost type")
                    .build();

            Authority deleteCostType = Authority.builder()
                    .code(AuthorityCode.DELETE_COST_TYPE)
                    .name("Delete cost type")
                    .build();

            Authority createNewPosition = Authority.builder()
                    .code(AuthorityCode.CREATE_NEW_POSITION)
                    .name("Create new position")
                    .build();

            Authority updatePosition = Authority.builder()
                    .code(AuthorityCode.UPDATE_POSITION)
                    .name("Update position")
                    .build();

            Authority deletePosition = Authority.builder()
                    .code(AuthorityCode.DELETE_POSITION)
                    .name("Delete position")
                    .build();

            Authority viewPosition = Authority.builder()
                    .code(AuthorityCode.VIEW_POSITION)
                    .name("View position")
                    .build();

            Authority createNewProject = Authority.builder()
                    .code(AuthorityCode.CREATE_NEW_PROJECT)
                    .name("Create new project")
                    .build();

            Authority updateProject = Authority.builder()
                    .code(AuthorityCode.UPDATE_PROJECT)
                    .name("Update project")
                    .build();

            Authority deleteProject = Authority.builder()
                    .code(AuthorityCode.DELETE_PROJECT)
                    .name("Delete project")
                    .build();

            Authority viewProject = Authority.builder()
                    .code(AuthorityCode.VIEW_PROJECT)
                    .name("View project")
                    .build();

            Authority createNewSupplier = Authority.builder()
                    .code(AuthorityCode.CREATE_NEW_SUPPLIER)
                    .name("Create new supplier")
                    .build();

            Authority updateSupplier = Authority.builder()
                    .code(AuthorityCode.UPDATE_SUPPLIER)
                    .name("Update supplier")
                    .build();

            Authority deleteSupplier = Authority.builder()
                    .code(AuthorityCode.DELETE_SUPPLIER)
                    .name("Delete supplier")
                    .build();

            Authority viewSupplier = Authority.builder()
                    .code(AuthorityCode.VIEW_SUPPLIER)
                    .name("View supplier")
                    .build();

            Authority createNewExchange = Authority.builder()
                    .code(AuthorityCode.CREATE_NEW_EXCHANGE)
                    .name("Create new exchange")
                    .build();

            Authority updateExchange = Authority.builder()
                    .code(AuthorityCode.UPDATE_EXCHANGE)
                    .name("Update exchange")
                    .build();

            Authority deleteExchange = Authority.builder()
                    .code(AuthorityCode.DELETE_EXCHANGE)
                    .name("Delete exchange")
                    .build();

            Authority viewExchange = Authority.builder()
                    .code(AuthorityCode.VIEW_EXCHANGE)
                    .name("View exchange")
                    .build();

            Authority createNewCurrency = Authority.builder()
                    .code(AuthorityCode.CREATE_NEW_CURRENCY)
                    .name("Create new currency")
                    .build();

            Authority updateCurrency = Authority.builder()
                    .code(AuthorityCode.UPDATE_CURRENCY)
                    .name("Update exchange")
                    .build();

            Authority deleteCurrency = Authority.builder()
                    .code(AuthorityCode.DELETE_CURRENCY)
                    .name("Delete currency")
                    .build();

            Authority viewCurrency = Authority.builder()
                    .code(AuthorityCode.VIEW_CURRENCY)
                    .name("View currency")
                    .build();

            authorityRepository.saveAll(List.of(viewUserDetail, viewPlan, createUser, viewListUsers, deleteUser, editUser, activateUser, deactivateUser, createTerm, editTerm, viewTerm, startTerm, deleteTerm, importPlan, reUploadPlan, submitPlanForReview, deletePlan, downloadPlan, approvePlan, viewReport, downloadReport, deleteReport, viewAnnualReport, downloadAnnualReport, createNewDepartment, updateDepartment, deleteDepartment, createNewCostType, deleteCostType, updateCostType, viewPosition, createNewPosition, updatePosition, deletePosition, viewProject, createNewProject, updateProject, deleteProject, createNewSupplier, updateSupplier, deleteSupplier, viewSupplier, createNewExchange, updateExchange, deleteExchange, viewExchange, createNewCurrency, updateCurrency, deleteCurrency, viewCurrency));

            // Role
            Role admin = Role.builder()
                    .code("admin")
                    .name("Admin")
                    .build();

            Role accountant = Role.builder()
                    .code("accountant")
                    .name("Accountant")
                    .build();

            Role financialStaff = Role.builder()
                    .code("financial-staff")
                    .name("Financial Staff")
                    .build();

            roleRepository.saveAll(List.of(admin, accountant, financialStaff));

            // User
            User user1 = User.builder()
                    .id(1L)
                    .username("username1")
                    .fullName("Nutalomlok Nunu")
                    .password(this.passwordEncoder.encode("password"))
                    .role(admin)
                    .department(itDepartment)
                    .position(techlead)
                    .email("username1@email.com")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .phoneNumber("0999988877")
                    .build();

            User user2 = User.builder()
                    .id(2L)
                    .username("username2")
                    .fullName("Sitchana Jaejan")
                    .password(this.passwordEncoder.encode("password"))
                    .role(admin)
                    .department(itDepartment)
                    .isDelete(false)
                    .phoneNumber("0999988877")
                    .position(techlead)
                    .dob(LocalDateTime.of(2000, 4, 5, 0, 0))
                    .email("username2@email.com")
                    .address("Da Nang")
                    .build();

            User user3 = User.builder()
                    .id(3L)
                    .username("username3")
                    .fullName("Nguyen The Ngoc")
                    .password(this.passwordEncoder.encode("password"))
                    .role(accountant)
                    .department(financeDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(1998, 4, 2, 2, 3))
                    .email("username3@email.com")
                    .address("Ha Noi ")
                    .phoneNumber("0999988877")
                    .isDelete(false)
                    .build();

            User user4 = User.builder()
                    .id(4L)
                    .username("username4")
                    .fullName("Choi Woo je")
                    .password(this.passwordEncoder.encode("password"))
                    .role(accountant)
                    .department(accountingDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(1986, 12, 20, 0, 0))
                    .isDelete(false)
                    .email("username4@email.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .build();

            User user5 = User.builder()
                    .id(5L)
                    .username("username5")
                    .fullName("Nguyen The Ngoc")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(communicationDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("username5@email.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .build();

            User user6 = User.builder()
                    .id(6L)
                    .username("TuNM46")
                    .fullName("Nguyen Manh Tu")
                    .password(this.passwordEncoder.encode("password"))
                    .role(admin)
                    .department(marketingDepartment)
                    .position(juniorDev)
                    .email("TuNM46@email.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2001, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .build();

            User user7 = User.builder()
                    .id(7L)
                    .username("TuanNQ47")
                    .fullName("Ngo Quoc Tuan")
                    .password(this.passwordEncoder.encode("password"))
                    .role(accountant)
                    .department(itDepartment)
                    .position(techlead)
                    .email("TuanNQ47@email.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2001, 9, 11, 0, 0, 0))
                    .isDelete(false)
                    .build();

            User user8 = User.builder()
                    .id(8L)
                    .username("HoanNB3")
                    .fullName("Nguyen Ba Hoan")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(itDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("HoanNB3@email.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .isDelete(false)
                    .build();

            User user9 = User.builder()
                    .id(9L)
                    .username("BaoNN15")
                    .fullName("Nguyen Ngoc Bao")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(itDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(1997, 4, 2, 2, 3))
                    .email("BaoNN15@email.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .isDelete(false)
                    .build();

            User user10 = User.builder()
                    .id(10L)
                    .username("GiangDV9")
                    .fullName("Dang Van Giang")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(hrDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("GiangDV9@email.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .build();

            User user11 = User.builder()
                    .id(11L)
                    .username("AnhNLV1")
                    .fullName("Nguyen Le Viet Anh")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(hrDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("anhnlv1@fpt.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .build();

            User user12 = User.builder()
                    .id(12L)
                    .username("ThinhTH7")
                    .fullName("Tran Han Thinh")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(communicationDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("thinhth7@fpt.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .build();

            User user13 = User.builder()
                    .id(13L)
                    .username("ThuNH13")
                    .fullName("Nguyen Huyen Thu")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(communicationDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("thunh13@fpt.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .build();

            User user14 = User.builder()
                    .id(14L)
                    .username("HieuNH35")
                    .fullName("Nguyen Hoang Hieu")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(itDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("hieunh35@fpt.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .build();

            User user15 = User.builder()
                    .id(15L)
                    .username("TuanLD20")
                    .fullName("Le Dinh Tuan")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(itDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("tuanld20@fpt.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .build();

            User user16 = User.builder()
                    .id(16L)
                    .username("KienNV23")
                    .fullName("Nguyen Van Kien")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(hrDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("kiennv23@fpt.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .build();

            User user17 = User.builder()
                    .id(17L)
                    .username("MinhNH84")
                    .fullName("Nguyen Huy Minh")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(accountingDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("minhnh84@fpt.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .build();

            User user18 = User.builder()
                    .id(18L)
                    .username("QuanHM14")
                    .fullName("Hoang Minh Quan")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(accountingDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("quanhm14@fpt.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .build();

            User user19 = User.builder()
                    .id(19L)
                    .username("TraMT1")
                    .fullName("Mai Thu Tra")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(itDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("tramt1@fpt.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .build();

            User user20 = User.builder()
                    .id(20L)
                    .username("TrangDP3")
                    .fullName("Duong Phuc Trang")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(itDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("trangdp3@fpt.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .build();

            User user21 = User.builder()
                    .id(21L)
                    .username("TuanVV11")
                    .fullName("Vu Van Tuan")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(hrDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("tuanvv11@fpt.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .build();

            User user22 = User.builder()
                    .id(22L)
                    .username("TungDV21")
                    .fullName("Duong Van Tung")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(marketingDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("tungdv21@fpt.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .build();

            List<User> users = new ArrayList<>(List.of(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10, user11, user12, user13, user14, user15, user16, user17, user18, user19, user20, user21, user22));

            userRepository.saveAll(users);

            List<UserSetting> userSettings = new ArrayList<>();

            Random random = new Random();
            String[] themes = {"blue", "emerald", "teal", "cyan", "purple", "orange", "rose"};
            String[] languages = {"vi", "en", "kr"};
            for (User user : users) {
                int randomThemeIndex = random.nextInt(7) + 1;
                int randomLanguageIndex = random.nextInt(3) + 1;

                String randomTheme = switch (randomThemeIndex) {
                    case 1 -> themes[0];
                    case 2 -> themes[1];
                    case 3 -> themes[2];
                    case 4 -> themes[3];
                    case 5 -> themes[4];
                    case 6 -> themes[5];
                    case 7 -> themes[6];
                    default -> themes[0]; // Default case, should never be reached
                };

                String randomLanguage = switch (randomLanguageIndex) {
                    case 1 -> themes[0];
                    case 2 -> themes[1];
                    case 3 -> themes[2];
                    default -> themes[0]; // Default case, should never be reached
                };

                UserSetting userSetting = UserSetting.builder()
                        .user(user)
                        .darkMode(false)
                        .theme(randomTheme)
                        .language(randomLanguage)
                        .build();

                userSettings.add(userSetting);
            }

            userSettingRepository.saveAll(userSettings);

            // Role authorities
            RoleAuthority adminAuthority1 = RoleAuthority.builder()
                    .role(admin)
                    .authority(createUser)
                    .build();

            RoleAuthority adminAuthority2 = RoleAuthority.builder()
                    .role(admin)
                    .authority(viewListUsers)
                    .build();

            RoleAuthority adminAuthority3 = RoleAuthority.builder()
                    .role(admin)
                    .authority(deleteUser)
                    .build();

            RoleAuthority adminAuthority4 = RoleAuthority.builder()
                    .role(admin)
                    .authority(editUser)
                    .build();

            RoleAuthority adminAuthority5 = RoleAuthority.builder()
                    .role(admin)
                    .authority(activateUser)
                    .build();

            RoleAuthority adminAuthority6 = RoleAuthority.builder()
                    .role(admin)
                    .authority(deactivateUser)
                    .build();

            RoleAuthority adminAuthority7 = RoleAuthority.builder()
                    .role(admin)
                    .authority(viewUserDetail)
                    .build();

            RoleAuthority adminAuthority8 = RoleAuthority.builder()
                    .role(admin)
                    .authority(deleteDepartment)
                    .build();

            RoleAuthority adminAuthority9 = RoleAuthority.builder()
                    .role(admin)
                    .authority(createNewDepartment)
                    .build();

            RoleAuthority adminAuthority10 = RoleAuthority.builder()
                    .role(admin)
                    .authority(updateDepartment)
                    .build();

            RoleAuthority adminAuthority14 = RoleAuthority.builder()
                    .role(admin)
                    .authority(viewPosition)
                    .build();

            RoleAuthority adminAuthority15 = RoleAuthority.builder()
                    .role(admin)
                    .authority(createNewPosition)
                    .build();

            RoleAuthority adminAuthority16 = RoleAuthority.builder()
                    .role(admin)
                    .authority(updatePosition)
                    .build();

            RoleAuthority adminAuthority17 = RoleAuthority.builder()
                    .role(admin)
                    .authority(deletePosition)
                    .build();


            RoleAuthority accountantAuthority1 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(createTerm)
                    .build();

            RoleAuthority accountantAuthority2 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(editTerm)
                    .build();

            RoleAuthority accountantAuthority3 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(viewTerm)
                    .build();

            RoleAuthority accountantAuthority4 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(startTerm)
                    .build();

            RoleAuthority accountantAuthority5 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(deleteTerm)
                    .build();

            RoleAuthority accountantAuthority6 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(importPlan)
                    .build();

            RoleAuthority accountantAuthority7 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(reUploadPlan)
                    .build();

            RoleAuthority accountantAuthority8 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(submitPlanForReview)
                    .build();

            RoleAuthority accountantAuthority9 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(deletePlan)
                    .build();

            RoleAuthority accountantAuthority10 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(downloadPlan)
                    .build();

            RoleAuthority accountantAuthority11 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(approvePlan)
                    .build();

            RoleAuthority accountantAuthority12 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(viewReport)
                    .build();

            RoleAuthority accountantAuthority13 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(downloadReport)
                    .build();

            RoleAuthority accountantAuthority14 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(viewAnnualReport)
                    .build();

            RoleAuthority accountantAuthority15 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(downloadAnnualReport)
                    .build();

            RoleAuthority accountantAuthority16 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(viewPlan)
                    .build();

            RoleAuthority accountantAuthority17 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(deleteReport)
                    .build();

            RoleAuthority accountantAuthority18 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(createNewCostType)
                    .build();

            RoleAuthority accountantAuthority19 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(deleteCostType)
                    .build();

            RoleAuthority accountantAuthority20 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(updateCostType)
                    .build();

            RoleAuthority accountantAuthority21 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(viewProject)
                    .build();

            RoleAuthority accountantAuthority22 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(createNewProject)
                    .build();

            RoleAuthority accountantAuthority23 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(updateProject)
                    .build();

            RoleAuthority accountantAuthority24 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(deleteProject)
                    .build();

            RoleAuthority accountantAuthority25 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(viewSupplier)
                    .build();

            RoleAuthority accountantAuthority26 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(createNewSupplier)
                    .build();

            RoleAuthority accountantAuthority27 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(updateSupplier)
                    .build();

            RoleAuthority accountantAuthority28 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(deleteSupplier)
                    .build();

            RoleAuthority accountantAuthority29 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(viewExchange)
                    .build();

            RoleAuthority accountantAuthority30 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(createNewExchange)
                    .build();

            RoleAuthority accountantAuthority31 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(updateExchange)
                    .build();

            RoleAuthority accountantAuthority32 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(deleteExchange)
                    .build();

            RoleAuthority accountantAuthority33 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(viewCurrency)
                    .build();

            RoleAuthority accountantAuthority34 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(createNewCurrency)
                    .build();

            RoleAuthority accountantAuthority35 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(updateCurrency)
                    .build();

            RoleAuthority accountantAuthority36 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(deleteCurrency)
                    .build();

            RoleAuthority financialStaffAuthority6 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(importPlan)
                    .build();

            RoleAuthority financialStaffAuthority7 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(reUploadPlan)
                    .build();

            RoleAuthority financialStaffAuthority8 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(submitPlanForReview)
                    .build();

            RoleAuthority financialStaffAuthority9 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(deletePlan)
                    .build();

            RoleAuthority financialStaffAuthority10 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(downloadPlan)
                    .build();

            RoleAuthority financialStaffAuthority11 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(viewReport)
                    .build();

            RoleAuthority financialStaffAuthority12 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(downloadReport)
                    .build();

            RoleAuthority financialStaffAuthority13 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(viewAnnualReport)
                    .build();

            RoleAuthority financialStaffAuthority14 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(downloadAnnualReport)
                    .build();

            RoleAuthority financialStaffAuthority15 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(viewPlan)
                    .build();

            RoleAuthority financialStaffAuthority16 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(viewCurrency)
                    .build();

            roleAuthorityRepository.saveAll(List.of(adminAuthority1, adminAuthority2, adminAuthority3, adminAuthority4, adminAuthority5, adminAuthority6, adminAuthority7, adminAuthority8, adminAuthority9, adminAuthority10, adminAuthority14, adminAuthority15, adminAuthority16, adminAuthority17,
                    accountantAuthority1, accountantAuthority2, accountantAuthority3, accountantAuthority4, accountantAuthority5, accountantAuthority6, accountantAuthority7, accountantAuthority8, accountantAuthority9, accountantAuthority10, accountantAuthority11, accountantAuthority12, accountantAuthority13, accountantAuthority14, accountantAuthority15, accountantAuthority16, accountantAuthority17, accountantAuthority18, accountantAuthority19, accountantAuthority20, accountantAuthority21, accountantAuthority22, accountantAuthority23, accountantAuthority24, accountantAuthority25, accountantAuthority26, accountantAuthority27, accountantAuthority28, accountantAuthority29, accountantAuthority30, accountantAuthority31, accountantAuthority32, accountantAuthority33, accountantAuthority34, accountantAuthority35, accountantAuthority36,
                    financialStaffAuthority6, financialStaffAuthority7, financialStaffAuthority8, financialStaffAuthority9, financialStaffAuthority10, financialStaffAuthority11, financialStaffAuthority12, financialStaffAuthority13, financialStaffAuthority14, financialStaffAuthority15, financialStaffAuthority16
            ));

            // Report status
            ReportStatus newReportStatus = ReportStatus.builder()
                    .id(1L)
                    .name(ReportStatusCode.NEW.getValue())
                    .code(ReportStatusCode.NEW)
                    .build();

            ReportStatus waitingForApprovalReportStatus = ReportStatus.builder()
                    .id(2L)
                    .name(ReportStatusCode.WAITING_FOR_APPROVAL.getValue())
                    .code(ReportStatusCode.WAITING_FOR_APPROVAL)
                    .build();

            ReportStatus reviewedReportStatus = ReportStatus.builder()
                    .id(3L)
                    .name(ReportStatusCode.REVIEWED.getValue())
                    .code(ReportStatusCode.REVIEWED)
                    .build();


            ReportStatus closedReportStatus = ReportStatus.builder()
                    .id(4L)
                    .name(ReportStatusCode.CLOSED.getValue())
                    .code(ReportStatusCode.CLOSED)
                    .build();

            reportStatusRepository.saveAll(List.of(newReportStatus, waitingForApprovalReportStatus, reviewedReportStatus, closedReportStatus));

            Term term21_12 = Term.builder()
                    .name("January 2022")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2021, 12, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2021, 12, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2021, 12, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2021, 12, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2021, 12, 25, 0, 0, 0)))
                    .user(user3)
                    .status(termStatus3)
                    .build();

            Term term22_1 = Term.builder()
                    .name("February 2022")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2022, 1, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2022, 1, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2022, 1, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2022, 1, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2022, 1, 25, 0, 0, 0)))
                    .user(user3)
                    .status(termStatus3)
                    .build();

            Term term22_2 = Term.builder()
                    .name("March 2022")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2022, 2, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2022, 2, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2022, 2, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2022, 2, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2022, 2, 25, 0, 0, 0)))
                    .user(user3)
                    .status(termStatus3)
                    .build();

            Term term22_3 = Term.builder()
                    .name("April 2022")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2022, 3, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2022, 3, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2022, 3, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2022, 3, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2022, 3, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term22_4 = Term.builder()
                    .name("May 2022")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2022, 4, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2022, 4, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2022, 4, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2022, 4, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2022, 4, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term22_5 = Term.builder()
                    .name("June 2022")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2022, 5, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2022, 5, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2022, 5, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2022, 5, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2022, 5, 25, 0, 0, 0)))
                    .user(user3)
                    .status(termStatus3)
                    .build();

            Term term22_6 = Term.builder()
                    .name("July 2022")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2022, 6, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2022, 6, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2022, 6, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2022, 6, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2022, 6, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term22_7 = Term.builder()
                    .name("August 2022")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2022, 7, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2022, 7, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2022, 7, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2022, 7, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2022, 7, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term22_8 = Term.builder()
                    .name("September 2022")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2022, 8, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2022, 8, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2022, 8, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2022, 8, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2022, 8, 25, 0, 0, 0)))
                    .user(user3)
                    .status(termStatus3)
                    .build();

            Term term22_9 = Term.builder()
                    .name("October 2022")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2022, 9, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2022, 9, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2022, 9, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2022, 9, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2022, 9, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term22_10 = Term.builder()
                    .name("November 2022")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2022, 10, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2022, 10, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2022, 10, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2022, 10, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2022, 10, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term22_11 = Term.builder()
                    .name("December 2022")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2022, 11, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2022, 11, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2022, 11, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2022, 11, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2022, 11, 25, 0, 0, 0)))
                    .user(user3)
                    .status(termStatus3)
                    .build();

            Term term22_12 = Term.builder()
                    .name("January 2023")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2022, 12, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2022, 12, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2022, 12, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2022, 12, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2022, 12, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            List<Term> term2022List = new ArrayList<>(List.of(term21_12, term22_1, term22_2, term22_3, term22_4, term22_5, term22_6, term22_7, term22_8, term22_9, term22_10, term22_11, term22_12));

            Term term23_1 = Term.builder()
                    .name("February 2023")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 1, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2023, 1, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2023, 1, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2023, 1, 25, 0, 0, 0).plusDays(22))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2023, 1, 25, 0, 0, 0)))
                    .user(user3)
                    .status(termStatus3)
                    .build();

            Term term23_2 = Term.builder()
                    .name("March 2023")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 2, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2023, 2, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2023, 2, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2023, 2, 25, 0, 0, 0).plusDays(22))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2023, 2, 25, 0, 0, 0)))
                    .user(user3)
                    .status(termStatus3)
                    .build();

            Term term23_3 = Term.builder()
                    .name("April 2023")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 3, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2023, 3, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2023, 3, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2023, 3, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2023, 3, 25, 0, 0, 0)))
                    .user(user3)
                    .status(termStatus3)
                    .build();

            Term term23_4 = Term.builder()
                    .name("May 2023")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 4, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2023, 4, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2023, 4, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2023, 4, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2023, 4, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term23_5 = Term.builder()
                    .name("June 2023")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 5, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2023, 5, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2023, 5, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2023, 5, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2023, 5, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term23_6 = Term.builder()
                    .name("July 2023")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 6, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2023, 6, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2023, 6, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2023, 6, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2023, 6, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term23_7 = Term.builder()
                    .name("August 2023")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 7, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2023, 7, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2023, 7, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2023, 7, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2023, 7, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term23_8 = Term.builder()
                    .name("September 2023")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 8, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2023, 8, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2023, 8, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2023, 8, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2023, 8, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term23_9 = Term.builder()
                    .name("October 2023")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 9, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2023, 9, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2023, 9, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2023, 9, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2023, 9, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term23_10 = Term.builder()
                    .name("November 2023")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 10, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2023, 10, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2023, 10, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2023, 10, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2023, 10, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term23_11 = Term.builder()
                    .name("December 2023")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 11, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2023, 11, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2023, 11, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2023, 11, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2023, 11, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term23_12 = Term.builder()
                    .name("January 2023")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0)))
                    .user(user3)
                    .status(termStatus3)
                    .build();

            List<Term> term2023List = new ArrayList<>(List.of(term23_1, term23_2, term23_3, term23_4, term23_5, term23_6, term23_7, term23_8, term23_9, term23_10, term23_11, term23_12));

            Term term24_1 = Term.builder()
                    .name("February 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 1, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 1, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 1, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 1, 25, 0, 0, 0).plusDays(22))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 1, 25, 0, 0, 0)))
                    .user(user3)
                    .status(termStatus3)
                    .build();

            Term term24_2 = Term.builder()
                    .name("March 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 2, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 2, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 2, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 2, 25, 0, 0, 0).plusDays(22))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 2, 25, 0, 0, 0)))
                    .user(user3)
                    .status(termStatus3)
                    .build();

            Term term24_3 = Term.builder()
                    .name("April 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 3, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 3, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 3, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 3, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 3, 25, 0, 0, 0)))
                    .user(user3)
                    .status(termStatus3)
                    .build();

            Term term24_4 = Term.builder()
                    .name("May 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 4, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 4, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 4, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 4, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 4, 25, 0, 0, 0)))
                    .user(user3)
                    .status(termStatus3)
                    .build();

            Term term24_5 = Term.builder()
                    .name("June 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 5, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 5, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 5, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 5, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 5, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term24_6 = Term.builder()
                    .name("July 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 6, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 6, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 6, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 6, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 6, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus3)
                    .build();

            Term term24_7 = Term.builder()
                    .name("August 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 7, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 7, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 7, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 7, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 7, 25, 0, 0, 0)))
                    .user(user4)
                    .allowReupload(true)
                    .status(termStatus2)
                    .build();

            Term term24_8 = Term.builder()
                    .name("September 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 8, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 8, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 8, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 8, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 8, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus2)
                    .build();

            Term term24_9 = Term.builder()
                    .name("October 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 9, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 9, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 9, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 9, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 9, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus)
                    .build();

            Term term24_10 = Term.builder()
                    .name("November 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 10, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 10, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 10, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 10, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 10, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus)
                    .build();

            List<Term> term2024List = new ArrayList<>(List.of(term24_1, term24_2, term24_3, term24_4, term24_5, term24_6, term24_7, term24_8, term24_9, term24_10));


            Term termTester1 = Term.builder()
                    .name("Available To Create New Plan")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus2)
                    .build();

            Term termTester2 = Term.builder()
                    .name("Available To Re-upload Plan")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0).plusDays(5))
                    .allowReupload(true)
                    .reuploadStartDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus2)
                    .build();

            Term termTester3 = Term.builder()
                    .name("Available To Review")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0))
                    .endDate(LocalDateTime.of(2023, 12, 25, 0, 0, 0).plusDays(5))
                    .reuploadStartDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).plusDays(20))
                    .reuploadEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.of(2024, 12, 25, 0, 0, 0)))
                    .user(user4)
                    .status(termStatus2)
                    .build();

            termRepository.saveAll(term2022List);
            termRepository.saveAll(term2023List);
            termRepository.saveAll(term2024List);
            termRepository.saveAll(List.of(termTester1, termTester2, termTester3));

            List<FinancialPlan> plans2022 = new ArrayList<>();

            for (Term term : term2022List) {
                List<Department> departments = new ArrayList<>(List.of(itDepartment, hrDepartment, financeDepartment, accountingDepartment, marketingDepartment, communicationDepartment));

                int randomNumberDepartment = random.nextInt(6) + 1;

                for (int i = 0; i < randomNumberDepartment; i++) {
                    int randomIndexDepartment = random.nextInt(6) + 1;
                    if (randomIndexDepartment >= departments.size()) {
                        randomIndexDepartment = departments.size() - 1;
                    }

                    Department randomDepartment = departments.get(randomIndexDepartment);

                    FinancialPlan financialPlan = FinancialPlan.builder()
                            .name(term.getName() + "_" + randomDepartment.getName() + "_Plan")
                            .term(term)
                            .department(randomDepartment)
                            .expectedCost(BigDecimal.valueOf(random.nextInt(500000000) + 2000000000))
                            .actualCost(BigDecimal.valueOf(random.nextInt(500000000) + 1500000000))
                            .build();

                    departments.removeIf(department -> department.getName().equals(randomDepartment.getName()));

                    plans2022.add(financialPlan);
                }
            }

            List<FinancialPlan> plans2023 = new ArrayList<>();

            for (Term term : term2023List) {
                List<Department> departments = new ArrayList<>(List.of(itDepartment, hrDepartment, financeDepartment, accountingDepartment, marketingDepartment, communicationDepartment));

                int randomNumberDepartment = random.nextInt(6) + 1;

                for (int i = 0; i < randomNumberDepartment; i++) {
                    int randomIndexDepartment = random.nextInt(6) + 1;
                    if (randomIndexDepartment >= departments.size()) {
                        randomIndexDepartment = departments.size() - 1;
                    }

                    Department randomDepartment = departments.get(randomIndexDepartment);

                    FinancialPlan financialPlan = FinancialPlan.builder()
                            .name(term.getName() + "_" + randomDepartment.getName() + "_Plan")
                            .term(term)
                            .department(randomDepartment)
                            .expectedCost(BigDecimal.valueOf(random.nextInt(500000000) + 2000000000))
                            .actualCost(BigDecimal.valueOf(random.nextInt(500000000) + 1500000000))
                            .build();

                    departments.removeIf(department -> department.getName().equals(randomDepartment.getName()));

                    plans2023.add(financialPlan);
                }
            }

            List<FinancialPlan> plans2024 = new ArrayList<>();

            for (Term term : term2024List) {
                List<Department> departments = new ArrayList<>(List.of(itDepartment, hrDepartment, financeDepartment, accountingDepartment, marketingDepartment, communicationDepartment));

                int randomNumberDepartment = random.nextInt(6) + 1;

                for (int i = 0; i < randomNumberDepartment; i++) {
                    int randomIndexDepartment = random.nextInt(6) + 1;
                    if (randomIndexDepartment >= departments.size()) {
                        randomIndexDepartment = departments.size() - 1;
                    }

                    Department randomDepartment = departments.get(randomIndexDepartment);

                    FinancialPlan financialPlan = FinancialPlan.builder()
                            .name(term.getName() + "_" + randomDepartment.getName() + "_Plan")
                            .term(term)
                            .department(randomDepartment)
                            .expectedCost(BigDecimal.valueOf(random.nextInt(500000000) + 2000000000))
                            .actualCost(BigDecimal.valueOf(random.nextInt(500000000) + 1500000000))
                            .build();

                    departments.removeIf(department -> department.getName().equals(randomDepartment.getName()));

                    plans2024.add(financialPlan);
                }
            }


            FinancialPlan planTester1 = FinancialPlan.builder()
                    .name(termTester3.getName() + "_" + hrDepartment.getName() + "_Plan")
                    .term(termTester3)
                    .department(hrDepartment)
                    .actualCost(BigDecimal.valueOf(0))
                    .expectedCost(BigDecimal.valueOf(0))
                    .build();

            FinancialPlan planTester2 = FinancialPlan.builder()
                    .name(termTester3.getName() + "_" + accountant.getName() + "_Plan")
                    .term(termTester3)
                    .actualCost(BigDecimal.valueOf(0))
                    .expectedCost(BigDecimal.valueOf(0))
                    .department(accountingDepartment)
                    .build();

            planRepository.saveAll(plans2022);
            planRepository.saveAll(plans2023);
            planRepository.saveAll(plans2024);
            planRepository.saveAll(List.of(planTester1, planTester2));

            CostType costType1 = CostType.builder()
                    .id(1L)
                    .name("Administration cost")
                    .build();

            CostType costType2 = CostType.builder()
                    .id(2L)
                    .name("Direct costs")
                    .build();

            CostType costType3 = CostType.builder()
                    .id(3L)
                    .name("Indirect cost")
                    .build();

            CostType costType4 = CostType.builder()
                    .id(4L)
                    .name("Operating costs")
                    .build();

            CostType costType5 = CostType.builder()
                    .id(5L)
                    .name("Maintenance costs")
                    .build();

            CostType costType6 = CostType.builder()
                    .id(6L)
                    .name("Manufacturing costs")
                    .build();

            costTypeRepository.saveAll(List.of(costType1, costType2, costType3, costType4, costType5, costType6));

            ExpenseStatus expenseStatus1 = ExpenseStatus.builder()
                    .id(1L)
                    .name("New")
                    .code(ExpenseStatusCode.NEW)
                    .build();

            ExpenseStatus expenseStatus2 = ExpenseStatus.builder()
                    .id(2L)
                    .name("Approved")
                    .code(ExpenseStatusCode.APPROVED)
                    .build();

            ExpenseStatus expenseStatus3 = ExpenseStatus.builder()
                    .id(3L)
                    .name("Denied")
                    .code(ExpenseStatusCode.DENIED)
                    .build();

            expenseStatusRepository.saveAll(List.of(expenseStatus1, expenseStatus2, expenseStatus3));

            Project project1 = Project.builder()
                    .id(1L)
                    .name("Vega Insights")
                    .build();

            Project project2 = Project.builder()
                    .id(2L)
                    .name("Nexus Analytics")
                    .build();

            Project project3 = Project.builder()
                    .id(3L)
                    .name("Orion Data")
                    .build();

            Project project4 = Project.builder()
                    .id(4L)
                    .name("Quantum Solutions")
                    .build();

            Project project5 = Project.builder()
                    .id(5L)
                    .name("Astra Metrics")
                    .build();

            Project project6 = Project.builder()
                    .id(6L)
                    .name("Luna Logic")
                    .build();

            projectRepository.saveAll(List.of(project1, project2, project3, project4, project5, project6));

            Supplier supplier1 = Supplier.builder()
                    .id(1L)
                    .name("VinaPro Co., Ltd")
                    .build();

            Supplier supplier2 = Supplier.builder()
                    .id(2L)
                    .name("SaigonTech Supply")
                    .build();

            Supplier supplier3 = Supplier.builder()
                    .id(3L)
                    .name("Hanoi Materials")
                    .build();

            Supplier supplier4 = Supplier.builder()
                    .id(4L)
                    .name("DongA Trading")
                    .build();

            Supplier supplier5 = Supplier.builder()
                    .id(5L)
                    .name("Mekong Distributors")
                    .build();

            Supplier supplier6 = Supplier.builder()
                    .id(6L)
                    .name("Lotus Supply Chain")
                    .build();

            supplierRepository.saveAll(List.of(supplier1, supplier2, supplier3, supplier4, supplier5, supplier6));

            List<FinancialPlanFile> planFiles2022 = new ArrayList<>();

            for (FinancialPlan plan : plans2022) {
                int randomIndexUser = random.nextInt(6) + 1;
                User randomUser = null;
                if (plan.getDepartment().getName().equals(itDepartment.getName())) {
                    randomUser = switch (randomIndexUser) {
                        case 1 -> user7;
                        case 2 -> user8;
                        case 3 -> user9;
                        case 4 -> user14;
                        case 5 -> user15;
                        case 6 -> user20;
                        default -> user7; // Default case, should never be reached
                    };
                } else if (plan.getDepartment().getName().equals(hrDepartment.getName())) {
                    randomUser = switch (randomIndexUser) {
                        case 1 -> user10;
                        case 2 -> user11;
                        case 3 -> user16;
                        case 4 -> user21;
                        default -> user10; // Default case, should never be reached
                    };
                } else if (plan.getDepartment().getName().equals(accountingDepartment.getName())) {
                    randomUser = switch (randomIndexUser) {
                        case 1 -> user4;
                        case 2 -> user17;
                        case 3 -> user18;
                        default -> user17; // Default case, should never be reached
                    };
                } else if (plan.getDepartment().getName().equals(marketingDepartment.getName())) {
                    randomUser = switch (randomIndexUser) {
                        case 1 -> user22;
                        default -> user22; // Default case, should never be reached
                    };
                } else if (plan.getDepartment().getName().equals(communicationDepartment.getName())) {
                    randomUser = switch (randomIndexUser) {
                        case 1 -> user5;
                        case 2 -> user12;
                        case 3 -> user13;
                        default -> user5; // Default case, should never be reached
                    };
                } else if (plan.getDepartment().getName().equals(financeDepartment.getName())) {
                    randomUser = switch (randomIndexUser) {
                        case 1 -> user3;
                        default -> user3; // Default case, should never be reached
                    };
                }

                FinancialPlanFile financialPlanFile = FinancialPlanFile.builder()
                        .name(plan.getName())
                        .plan(plan)
                        .user(randomUser)
                        .build();

                planFiles2022.add(financialPlanFile);

            }

            List<FinancialPlanFile> planFiles2023 = new ArrayList<>();

            for (FinancialPlan plan : plans2023) {
                int randomIndexUser = random.nextInt(6) + 1;
                User randomUser = null;
                if (plan.getDepartment().getName().equals(itDepartment.getName())) {
                    randomUser = switch (randomIndexUser) {
                        case 1 -> user7;
                        case 2 -> user8;
                        case 3 -> user9;
                        case 4 -> user14;
                        case 5 -> user15;
                        case 6 -> user20;
                        default -> user7; // Default case, should never be reached
                    };
                } else if (plan.getDepartment().getName().equals(hrDepartment.getName())) {
                    randomUser = switch (randomIndexUser) {
                        case 1 -> user10;
                        case 2 -> user11;
                        case 3 -> user16;
                        case 4 -> user21;
                        default -> user10; // Default case, should never be reached
                    };
                } else if (plan.getDepartment().getName().equals(accountingDepartment.getName())) {
                    randomUser = switch (randomIndexUser) {
                        case 1 -> user4;
                        case 2 -> user17;
                        case 3 -> user18;
                        default -> user17; // Default case, should never be reached
                    };
                } else if (plan.getDepartment().getName().equals(marketingDepartment.getName())) {
                    randomUser = switch (randomIndexUser) {
                        case 1 -> user22;
                        default -> user22; // Default case, should never be reached
                    };
                } else if (plan.getDepartment().getName().equals(communicationDepartment.getName())) {
                    randomUser = switch (randomIndexUser) {
                        case 1 -> user5;
                        case 2 -> user12;
                        case 3 -> user13;
                        default -> user5; // Default case, should never be reached
                    };
                } else if (plan.getDepartment().getName().equals(financeDepartment.getName())) {
                    randomUser = switch (randomIndexUser) {
                        case 1 -> user3;
                        default -> user3; // Default case, should never be reached
                    };
                }

                FinancialPlanFile financialPlanFile = FinancialPlanFile.builder()
                        .name(plan.getName())
                        .plan(plan)
                        .user(randomUser)
                        .build();

                planFiles2023.add(financialPlanFile);

            }

            List<FinancialPlanFile> planFiles2024 = new ArrayList<>();

            for (FinancialPlan plan : plans2024) {
                int randomIndexUser = random.nextInt(6) + 1;
                User randomUser = null;
                if (plan.getDepartment().getName().equals(itDepartment.getName())) {
                    randomUser = switch (randomIndexUser) {
                        case 1 -> user7;
                        case 2 -> user8;
                        case 3 -> user9;
                        case 4 -> user14;
                        case 5 -> user15;
                        case 6 -> user20;
                        default -> user7; // Default case, should never be reached
                    };
                } else if (plan.getDepartment().getName().equals(hrDepartment.getName())) {
                    randomUser = switch (randomIndexUser) {
                        case 1 -> user10;
                        case 2 -> user11;
                        case 3 -> user16;
                        case 4 -> user21;
                        default -> user10; // Default case, should never be reached
                    };
                } else if (plan.getDepartment().getName().equals(accountingDepartment.getName())) {
                    randomUser = switch (randomIndexUser) {
                        case 1 -> user4;
                        case 2 -> user17;
                        case 3 -> user18;
                        default -> user17; // Default case, should never be reached
                    };
                } else if (plan.getDepartment().getName().equals(marketingDepartment.getName())) {
                    randomUser = switch (randomIndexUser) {
                        case 1 -> user22;
                        default -> user22; // Default case, should never be reached
                    };
                } else if (plan.getDepartment().getName().equals(communicationDepartment.getName())) {
                    randomUser = switch (randomIndexUser) {
                        case 1 -> user5;
                        case 2 -> user12;
                        case 3 -> user13;
                        default -> user5; // Default case, should never be reached
                    };
                } else if (plan.getDepartment().getName().equals(financeDepartment.getName())) {
                    randomUser = switch (randomIndexUser) {
                        case 1 -> user3;
                        default -> user3; // Default case, should never be reached
                    };
                }

                FinancialPlanFile financialPlanFile = FinancialPlanFile.builder()
                        .name(plan.getName())
                        .plan(plan)
                        .user(randomUser)
                        .build();

                planFiles2024.add(financialPlanFile);
            }

            FinancialPlanFile planTester1_1 = FinancialPlanFile.builder()
                    .name(planTester1.getName())
                    .plan(planTester1)
                    .user(user7)
                    .build();

            FinancialPlanFile planTester1_2 = FinancialPlanFile.builder()
                    .name(planTester1.getName())
                    .plan(planTester1)
                    .user(user6)
                    .build();

            FinancialPlanFile planTester1_3 = FinancialPlanFile.builder()
                    .name(planTester1.getName())
                    .plan(planTester1)
                    .user(user6)
                    .build();

            FinancialPlanFile planTester2_1 = FinancialPlanFile.builder()
                    .name(planTester2.getName())
                    .plan(planTester2)
                    .user(user8)
                    .build();

            FinancialPlanFile planTester2_2 = FinancialPlanFile.builder()
                    .name(planTester2.getName())
                    .plan(planTester2)
                    .user(user6)
                    .build();

            financialPlanFileRepository.saveAll(planFiles2022);
            financialPlanFileRepository.saveAll(planFiles2023);
            financialPlanFileRepository.saveAll(planFiles2024);
            financialPlanFileRepository.saveAll(List.of(planTester1_1, planTester1_2, planTester1_3, planTester2_1, planTester2_2));

            // Create currency data
            Currency vndCurrency = Currency.builder()
                    .id(1L)
                    .name("VNĐ")
                    .symbol("₫")
                    .affix(Affix.SUFFIX)
                    .isDefault(true)
                    .build();

            Currency usdCurrency = Currency.builder()
                    .id(2L)
                    .name("USD")
                    .symbol("$")
                    .affix(Affix.PREFIX)
                    .build();

            Currency jpyCurrency = Currency.builder()
                    .id(3L)
                    .name("JPY")
                    .symbol("¥")
                    .affix(Affix.PREFIX)
                    .build();

            Currency krwCurrency = Currency.builder()
                    .id(4L)
                    .name("KRW")
                    .symbol("₩")
                    .affix(Affix.PREFIX)
                    .build();

            Currency eurCurrency = Currency.builder()
                    .id(5L)
                    .name("EUR")
                    .symbol("€")
                    .affix(Affix.PREFIX)
                    .build();

            currencyRepository.saveAll(List.of(vndCurrency, usdCurrency, jpyCurrency, krwCurrency, eurCurrency));

            // Get 512 random expenses vnd, krw, jpy for plan 2022
            List<FinancialPlanExpense> expenseList = new ArrayList<>();
            String[] listExpenseName = {
                    "Travel Expenses",
                    "Accommodation",
                    "Meals and Entertainment",
                    "Office Supplies",
                    "Transportation",
                    "Software Subscriptions",
                    "Marketing Expenses",
                    "Training and Development",
                    "Utilities",
                    "Internet Services",
                    "Insurance Premiums",
                    "Consulting Fees",
                    "Maintenance and Repairs",
                    "Rent",
                    "Salaries and Wages",
                    "Legal Fees",
                    "Advertising Costs",
                    "Client Gifts",
                    "Conference Fees",
                    "Health and Safety Equipment"
            };

            for (int i = 1; i <= 128; i++) {
                int randomStatusIndex = random.nextInt(4) + 1;
                int randomExpenseNameIndex = random.nextInt(listExpenseName.length);
                int randomCostTypeIndex = random.nextInt(6) + 1;
                int randomProjectIndex = random.nextInt(6) + 1;
                int randomSupplierIndex = random.nextInt(6) + 1;
                int randomPicIndex = random.nextInt(5) + 1;
                int randomCurrencyIndex = random.nextInt(3) + 1;

                ExpenseStatus randomExpenseStatus = switch (randomStatusIndex) {
                    case 2 -> expenseStatus2;
                    case 3 -> expenseStatus3;
                    default -> expenseStatus2; // Default case, should never be reached
                };

                CostType randomCostType = switch (randomCostTypeIndex) {
                    case 1 -> costType1;
                    case 2 -> costType2;
                    case 3 -> costType3;
                    case 4 -> costType4;
                    case 5 -> costType5;
                    case 6 -> costType6;
                    default -> costType1; // Default case, should never be reached
                };

                Project randomProject = switch (randomProjectIndex) {
                    case 1 -> project1;
                    case 2 -> project2;
                    case 3 -> project3;
                    case 4 -> project4;
                    case 5 -> project5;
                    case 6 -> project6;
                    default -> project1; // Default case, should never be reached
                };

                Supplier randomSupplier = switch (randomSupplierIndex) {
                    case 1 -> supplier1;
                    case 2 -> supplier2;
                    case 3 -> supplier3;
                    case 4 -> supplier4;
                    case 5 -> supplier5;
                    case 6 -> supplier6;
                    default -> supplier1; // Default case, should never be reached
                };

                User randomPic = switch (randomPicIndex) {
                    case 1 -> user6;
                    case 2 -> user7;
                    case 3 -> user8;
                    case 4 -> user9;
                    case 5 -> user10;
                    default -> user6; // Default case, should never be reached
                };

                Currency randomCurrency = switch (randomCurrencyIndex) {
                    case 1 -> vndCurrency;
                    case 2 -> krwCurrency;
                    case 3 -> jpyCurrency;
                    default -> vndCurrency; // Default case, should never be reached
                };

                FinancialPlanExpense expense = FinancialPlanExpense.builder()
                        .name(listExpenseName[randomExpenseNameIndex])
                        .unitPrice(BigDecimal.valueOf(random.nextInt(500000) + 10000L))
                        .amount(random.nextInt(10) + 1)
                        .project(randomProject)
                        .supplier(randomSupplier)
                        .pic(randomPic)
                        .status(randomExpenseStatus)
                        .costType(randomCostType)
                        .currency(randomCurrency)
                        .build();

                expenseList.add(expense);
            }


            // Mapping expense to plan
            int index = 0;
            List<FinancialPlanFileExpense> fileExpenses = new ArrayList<>();
            for (FinancialPlanExpense expense : expenseList) {
                int randomFilePlanIndex = random.nextInt(planFiles2022.size());
                FinancialPlanFile randomFile = planFiles2022.get(randomFilePlanIndex);

                if (expense.getStatus().getCode().equals(ExpenseStatusCode.APPROVED)) {
                    String prefixCode = randomFile.getPlan().getTerm().getName().replace(" ", "_");
                    expense.setPlanExpenseKey(prefixCode + "_Report_" + ++index);
                }

                fileExpenses.add(FinancialPlanFileExpense.builder()
                        .file(randomFile)
                        .planExpense(expense)
                        .build());

            }

            financialPlanExpenseRepository.saveAll(expenseList);
            financialPlanFileExpenseRepository.saveAll(fileExpenses);

            // Get 512 random expense usd and euro for plan 2022
            expenseList = new ArrayList<>();

            for (int i = 1; i <= 512; i++) {
                int randomStatusIndex = random.nextInt(4) + 1;
                int randomExpenseNameIndex = random.nextInt(listExpenseName.length);
                int randomCostTypeIndex = random.nextInt(6) + 1;
                int randomProjectIndex = random.nextInt(6) + 1;
                int randomSupplierIndex = random.nextInt(6) + 1;
                int randomPicIndex = random.nextInt(5) + 1;
                int randomCurrencyIndex = random.nextInt(2) + 1;

                ExpenseStatus randomExpenseStatus = switch (randomStatusIndex) {
                    case 2 -> expenseStatus2;
                    case 3 -> expenseStatus3;
                    default -> expenseStatus2; // Default case, should never be reached
                };

                CostType randomCostType = switch (randomCostTypeIndex) {
                    case 1 -> costType1;
                    case 2 -> costType2;
                    case 3 -> costType3;
                    case 4 -> costType4;
                    case 5 -> costType5;
                    case 6 -> costType6;
                    default -> costType1; // Default case, should never be reached
                };

                Project randomProject = switch (randomProjectIndex) {
                    case 1 -> project1;
                    case 2 -> project2;
                    case 3 -> project3;
                    case 4 -> project4;
                    case 5 -> project5;
                    case 6 -> project6;
                    default -> project1; // Default case, should never be reached
                };

                Supplier randomSupplier = switch (randomSupplierIndex) {
                    case 1 -> supplier1;
                    case 2 -> supplier2;
                    case 3 -> supplier3;
                    case 4 -> supplier4;
                    case 5 -> supplier5;
                    case 6 -> supplier6;
                    default -> supplier1; // Default case, should never be reached
                };

                User randomPic = switch (randomPicIndex) {
                    case 1 -> user6;
                    case 2 -> user7;
                    case 3 -> user8;
                    case 4 -> user9;
                    case 5 -> user10;
                    default -> user6; // Default case, should never be reached
                };

                Currency randomCurrency = switch (randomCurrencyIndex) {
                    case 1 -> usdCurrency;
                    case 2 -> eurCurrency;
                    default -> usdCurrency; // Default case, should never be reached
                };

                FinancialPlanExpense expense = FinancialPlanExpense.builder()
                        .name(listExpenseName[randomExpenseNameIndex])
                        .unitPrice(BigDecimal.valueOf(random.nextInt(1000) + 900))
                        .amount(random.nextInt(10) + 1)
                        .project(randomProject)
                        .supplier(randomSupplier)
                        .pic(randomPic)
                        .status(randomExpenseStatus)
                        .costType(randomCostType)
                        .currency(randomCurrency)
                        .build();
                expenseList.add(expense);
            }

            // Mapping expense to plan
            fileExpenses = new ArrayList<>();
            for (FinancialPlanExpense expense : expenseList) {
                int randomFilePlanIndex = random.nextInt(planFiles2022.size());
                FinancialPlanFile randomFile = planFiles2022.get(randomFilePlanIndex);

                if (expense.getStatus().getCode().equals(ExpenseStatusCode.APPROVED)) {
                    String prefixCode = randomFile.getPlan().getTerm().getName().replace(" ", "_");
                    expense.setPlanExpenseKey(prefixCode + "_Report_" + ++index);
                }

                fileExpenses.add(FinancialPlanFileExpense.builder()
                        .file(randomFile)
                        .planExpense(expense)
                        .build());

            }

            financialPlanExpenseRepository.saveAll(expenseList);
            financialPlanFileExpenseRepository.saveAll(fileExpenses);

            // Get 512 random expenses vnd, krw, jpy for plan 2023
            expenseList = new ArrayList<>();
            for (int i = 1; i <= 512; i++) {
                int randomStatusIndex = random.nextInt(4) + 1;
                int randomExpenseNameIndex = random.nextInt(listExpenseName.length);
                int randomCostTypeIndex = random.nextInt(6) + 1;
                int randomProjectIndex = random.nextInt(6) + 1;
                int randomSupplierIndex = random.nextInt(6) + 1;
                int randomPicIndex = random.nextInt(5) + 1;
                int randomCurrencyIndex = random.nextInt(3) + 1;

                ExpenseStatus randomExpenseStatus = switch (randomStatusIndex) {
                    case 2 -> expenseStatus2;
                    case 3 -> expenseStatus3;
                    default -> expenseStatus2; // Default case, should never be reached
                };

                CostType randomCostType = switch (randomCostTypeIndex) {
                    case 1 -> costType1;
                    case 2 -> costType2;
                    case 3 -> costType3;
                    case 4 -> costType4;
                    case 5 -> costType5;
                    case 6 -> costType6;
                    default -> costType1; // Default case, should never be reached
                };

                Project randomProject = switch (randomProjectIndex) {
                    case 1 -> project1;
                    case 2 -> project2;
                    case 3 -> project3;
                    case 4 -> project4;
                    case 5 -> project5;
                    case 6 -> project6;
                    default -> project1; // Default case, should never be reached
                };

                Supplier randomSupplier = switch (randomSupplierIndex) {
                    case 1 -> supplier1;
                    case 2 -> supplier2;
                    case 3 -> supplier3;
                    case 4 -> supplier4;
                    case 5 -> supplier5;
                    case 6 -> supplier6;
                    default -> supplier1; // Default case, should never be reached
                };

                User randomPic = switch (randomPicIndex) {
                    case 1 -> user6;
                    case 2 -> user7;
                    case 3 -> user8;
                    case 4 -> user9;
                    case 5 -> user10;
                    default -> user6; // Default case, should never be reached
                };

                Currency randomCurrency = switch (randomCurrencyIndex) {
                    case 1 -> vndCurrency;
                    case 2 -> krwCurrency;
                    case 3 -> jpyCurrency;
                    default -> vndCurrency; // Default case, should never be reached
                };

                FinancialPlanExpense expense = FinancialPlanExpense.builder()
                        .name(listExpenseName[randomExpenseNameIndex])
                        .unitPrice(BigDecimal.valueOf(random.nextInt(500000) + 10000L))
                        .amount(random.nextInt(10) + 1)
                        .project(randomProject)
                        .supplier(randomSupplier)
                        .pic(randomPic)
                        .status(randomExpenseStatus)
                        .costType(randomCostType)
                        .currency(randomCurrency)
                        .build();

                expenseList.add(expense);
            }


            // Mapping expense to plan
            index = 0;
            fileExpenses = new ArrayList<>();
            for (FinancialPlanExpense expense : expenseList) {
                int randomFilePlanIndex = random.nextInt(planFiles2023.size());
                FinancialPlanFile randomFile = planFiles2023.get(randomFilePlanIndex);

                if (expense.getStatus().getCode().equals(ExpenseStatusCode.APPROVED)) {
                    String prefixCode = randomFile.getPlan().getTerm().getName().replace(" ", "_");
                    expense.setPlanExpenseKey(prefixCode + "_Report_" + ++index);
                }

                fileExpenses.add(FinancialPlanFileExpense.builder()
                        .file(randomFile)
                        .planExpense(expense)
                        .build());

            }

            financialPlanExpenseRepository.saveAll(expenseList);
            financialPlanFileExpenseRepository.saveAll(fileExpenses);

            // Get 512 random expense usd and euro for plan 2023
            expenseList = new ArrayList<>();

            for (int i = 1; i <= 512; i++) {
                int randomStatusIndex = random.nextInt(4) + 1;
                int randomExpenseNameIndex = random.nextInt(listExpenseName.length);
                int randomCostTypeIndex = random.nextInt(6) + 1;
                int randomProjectIndex = random.nextInt(6) + 1;
                int randomSupplierIndex = random.nextInt(6) + 1;
                int randomPicIndex = random.nextInt(5) + 1;
                int randomCurrencyIndex = random.nextInt(2) + 1;

                ExpenseStatus randomExpenseStatus = switch (randomStatusIndex) {
                    case 2 -> expenseStatus2;
                    case 3 -> expenseStatus3;
                    default -> expenseStatus2; // Default case, should never be reached
                };

                CostType randomCostType = switch (randomCostTypeIndex) {
                    case 1 -> costType1;
                    case 2 -> costType2;
                    case 3 -> costType3;
                    case 4 -> costType4;
                    case 5 -> costType5;
                    case 6 -> costType6;
                    default -> costType1; // Default case, should never be reached
                };

                Project randomProject = switch (randomProjectIndex) {
                    case 1 -> project1;
                    case 2 -> project2;
                    case 3 -> project3;
                    case 4 -> project4;
                    case 5 -> project5;
                    case 6 -> project6;
                    default -> project1; // Default case, should never be reached
                };

                Supplier randomSupplier = switch (randomSupplierIndex) {
                    case 1 -> supplier1;
                    case 2 -> supplier2;
                    case 3 -> supplier3;
                    case 4 -> supplier4;
                    case 5 -> supplier5;
                    case 6 -> supplier6;
                    default -> supplier1; // Default case, should never be reached
                };

                User randomPic = switch (randomPicIndex) {
                    case 1 -> user6;
                    case 2 -> user7;
                    case 3 -> user8;
                    case 4 -> user9;
                    case 5 -> user10;
                    default -> user6; // Default case, should never be reached
                };

                Currency randomCurrency = switch (randomCurrencyIndex) {
                    case 1 -> usdCurrency;
                    case 2 -> eurCurrency;
                    default -> usdCurrency; // Default case, should never be reached
                };

                FinancialPlanExpense expense = FinancialPlanExpense.builder()
                        .name(listExpenseName[randomExpenseNameIndex])
                        .unitPrice(BigDecimal.valueOf(random.nextInt(1000) + 900))
                        .amount(random.nextInt(10) + 1)
                        .project(randomProject)
                        .supplier(randomSupplier)
                        .pic(randomPic)
                        .status(randomExpenseStatus)
                        .costType(randomCostType)
                        .currency(randomCurrency)
                        .build();
                expenseList.add(expense);
            }

            // Mapping expense to plan
            fileExpenses = new ArrayList<>();
            for (FinancialPlanExpense expense : expenseList) {
                int randomFilePlanIndex = random.nextInt(planFiles2023.size());
                FinancialPlanFile randomFile = planFiles2023.get(randomFilePlanIndex);

                if (expense.getStatus().getCode().equals(ExpenseStatusCode.APPROVED)) {
                    String prefixCode = randomFile.getPlan().getTerm().getName().replace(" ", "_");
                    expense.setPlanExpenseKey(prefixCode + "_Report_" + ++index);
                }

                fileExpenses.add(FinancialPlanFileExpense.builder()
                        .file(randomFile)
                        .planExpense(expense)
                        .build());

            }

            financialPlanExpenseRepository.saveAll(expenseList);
            financialPlanFileExpenseRepository.saveAll(fileExpenses);


            // Get 512 random expenses vnd, krw, jpy for plan 2024
            expenseList = new ArrayList<>();
            for (int i = 1; i <= 512; i++) {
                int randomStatusIndex = random.nextInt(4) + 1;
                int randomExpenseNameIndex = random.nextInt(listExpenseName.length);
                int randomCostTypeIndex = random.nextInt(6) + 1;
                int randomProjectIndex = random.nextInt(6) + 1;
                int randomSupplierIndex = random.nextInt(6) + 1;
                int randomPicIndex = random.nextInt(5) + 1;
                int randomCurrencyIndex = random.nextInt(3) + 1;

                ExpenseStatus randomExpenseStatus = switch (randomStatusIndex) {
                    case 2 -> expenseStatus2;
                    case 3 -> expenseStatus3;
                    default -> expenseStatus2; // Default case, should never be reached
                };

                CostType randomCostType = switch (randomCostTypeIndex) {
                    case 1 -> costType1;
                    case 2 -> costType2;
                    case 3 -> costType3;
                    case 4 -> costType4;
                    case 5 -> costType5;
                    case 6 -> costType6;
                    default -> costType1; // Default case, should never be reached
                };

                Project randomProject = switch (randomProjectIndex) {
                    case 1 -> project1;
                    case 2 -> project2;
                    case 3 -> project3;
                    case 4 -> project4;
                    case 5 -> project5;
                    case 6 -> project6;
                    default -> project1; // Default case, should never be reached
                };

                Supplier randomSupplier = switch (randomSupplierIndex) {
                    case 1 -> supplier1;
                    case 2 -> supplier2;
                    case 3 -> supplier3;
                    case 4 -> supplier4;
                    case 5 -> supplier5;
                    case 6 -> supplier6;
                    default -> supplier1; // Default case, should never be reached
                };

                User randomPic = switch (randomPicIndex) {
                    case 1 -> user6;
                    case 2 -> user7;
                    case 3 -> user8;
                    case 4 -> user9;
                    case 5 -> user10;
                    default -> user6; // Default case, should never be reached
                };

                Currency randomCurrency = switch (randomCurrencyIndex) {
                    case 1 -> vndCurrency;
                    case 2 -> krwCurrency;
                    case 3 -> jpyCurrency;
                    default -> vndCurrency; // Default case, should never be reached
                };

                FinancialPlanExpense expense = FinancialPlanExpense.builder()
                        .name(listExpenseName[randomExpenseNameIndex])
                        .unitPrice(BigDecimal.valueOf(random.nextInt(500000) + 10000L))
                        .amount(random.nextInt(10) + 1)
                        .project(randomProject)
                        .supplier(randomSupplier)
                        .pic(randomPic)
                        .status(randomExpenseStatus)
                        .costType(randomCostType)
                        .currency(randomCurrency)
                        .build();

                expenseList.add(expense);
            }


            // Mapping expense to plan
            index = 0;
            fileExpenses = new ArrayList<>();
            for (FinancialPlanExpense expense : expenseList) {
                int randomFilePlanIndex = random.nextInt(planFiles2024.size());
                FinancialPlanFile randomFile = planFiles2024.get(randomFilePlanIndex);

                if (expense.getStatus().getCode().equals(ExpenseStatusCode.APPROVED)) {
                    String prefixCode = randomFile.getPlan().getTerm().getName().replace(" ", "_");
                    expense.setPlanExpenseKey(prefixCode + "_Report_" + ++index);
                }

                fileExpenses.add(FinancialPlanFileExpense.builder()
                        .file(randomFile)
                        .planExpense(expense)
                        .build());

            }

            financialPlanExpenseRepository.saveAll(expenseList);
            financialPlanFileExpenseRepository.saveAll(fileExpenses);

            // Get 512 random expense usd and euro for plan 2024
            expenseList = new ArrayList<>();

            for (int i = 1; i <= 512; i++) {
                int randomStatusIndex = random.nextInt(4) + 1;
                int randomExpenseNameIndex = random.nextInt(listExpenseName.length);
                int randomCostTypeIndex = random.nextInt(6) + 1;
                int randomProjectIndex = random.nextInt(6) + 1;
                int randomSupplierIndex = random.nextInt(6) + 1;
                int randomPicIndex = random.nextInt(5) + 1;
                int randomCurrencyIndex = random.nextInt(2) + 1;

                ExpenseStatus randomExpenseStatus = switch (randomStatusIndex) {
                    case 2 -> expenseStatus2;
                    case 3 -> expenseStatus3;
                    default -> expenseStatus2; // Default case, should never be reached
                };

                CostType randomCostType = switch (randomCostTypeIndex) {
                    case 1 -> costType1;
                    case 2 -> costType2;
                    case 3 -> costType3;
                    case 4 -> costType4;
                    case 5 -> costType5;
                    case 6 -> costType6;
                    default -> costType1; // Default case, should never be reached
                };

                Project randomProject = switch (randomProjectIndex) {
                    case 1 -> project1;
                    case 2 -> project2;
                    case 3 -> project3;
                    case 4 -> project4;
                    case 5 -> project5;
                    case 6 -> project6;
                    default -> project1; // Default case, should never be reached
                };

                Supplier randomSupplier = switch (randomSupplierIndex) {
                    case 1 -> supplier1;
                    case 2 -> supplier2;
                    case 3 -> supplier3;
                    case 4 -> supplier4;
                    case 5 -> supplier5;
                    case 6 -> supplier6;
                    default -> supplier1; // Default case, should never be reached
                };

                User randomPic = switch (randomPicIndex) {
                    case 1 -> user6;
                    case 2 -> user7;
                    case 3 -> user8;
                    case 4 -> user9;
                    case 5 -> user10;
                    default -> user6; // Default case, should never be reached
                };

                Currency randomCurrency = switch (randomCurrencyIndex) {
                    case 1 -> usdCurrency;
                    case 2 -> eurCurrency;
                    default -> usdCurrency; // Default case, should never be reached
                };

                FinancialPlanExpense expense = FinancialPlanExpense.builder()
                        .name(listExpenseName[randomExpenseNameIndex])
                        .unitPrice(BigDecimal.valueOf(random.nextInt(1000) + 900))
                        .amount(random.nextInt(10) + 1)
                        .project(randomProject)
                        .supplier(randomSupplier)
                        .pic(randomPic)
                        .status(randomExpenseStatus)
                        .costType(randomCostType)
                        .currency(randomCurrency)
                        .build();
                expenseList.add(expense);
            }

            // Mapping expense to plan
            fileExpenses = new ArrayList<>();
            for (FinancialPlanExpense expense : expenseList) {
                int randomFilePlanIndex = random.nextInt(planFiles2024.size());
                FinancialPlanFile randomFile = planFiles2024.get(randomFilePlanIndex);

                if (expense.getStatus().getCode().equals(ExpenseStatusCode.APPROVED)) {
                    String prefixCode = randomFile.getPlan().getTerm().getName().replace(" ", "_");
                    expense.setPlanExpenseKey(prefixCode + "_Report_" + ++index);
                }

                fileExpenses.add(FinancialPlanFileExpense.builder()
                        .file(randomFile)
                        .planExpense(expense)
                        .build());

            }

            financialPlanExpenseRepository.saveAll(expenseList);
            financialPlanFileExpenseRepository.saveAll(fileExpenses);

            // Get 64 random expenses vnd, krw, jpy for test plan
            expenseList = new ArrayList<>();
            for (int i = 1; i <= 64; i++) {
                int randomStatusIndex = random.nextInt(4) + 1;
                int randomExpenseNameIndex = random.nextInt(listExpenseName.length);
                int randomCostTypeIndex = random.nextInt(6) + 1;
                int randomProjectIndex = random.nextInt(6) + 1;
                int randomSupplierIndex = random.nextInt(6) + 1;
                int randomPicIndex = random.nextInt(5) + 1;
                int randomCurrencyIndex = random.nextInt(3) + 1;

                ExpenseStatus randomExpenseStatus = switch (randomStatusIndex) {
                    case 1 -> expenseStatus1;
                    case 2 -> expenseStatus2;
                    case 3 -> expenseStatus3;
                    default -> expenseStatus2; // Default case, should never be reached
                };

                CostType randomCostType = switch (randomCostTypeIndex) {
                    case 1 -> costType1;
                    case 2 -> costType2;
                    case 3 -> costType3;
                    case 4 -> costType4;
                    case 5 -> costType5;
                    case 6 -> costType6;
                    default -> costType1; // Default case, should never be reached
                };

                Project randomProject = switch (randomProjectIndex) {
                    case 1 -> project1;
                    case 2 -> project2;
                    case 3 -> project3;
                    case 4 -> project4;
                    case 5 -> project5;
                    case 6 -> project6;
                    default -> project1; // Default case, should never be reached
                };

                Supplier randomSupplier = switch (randomSupplierIndex) {
                    case 1 -> supplier1;
                    case 2 -> supplier2;
                    case 3 -> supplier3;
                    case 4 -> supplier4;
                    case 5 -> supplier5;
                    case 6 -> supplier6;
                    default -> supplier1; // Default case, should never be reached
                };

                User randomPic = switch (randomPicIndex) {
                    case 1 -> user6;
                    case 2 -> user7;
                    case 3 -> user8;
                    case 4 -> user9;
                    case 5 -> user10;
                    default -> user6; // Default case, should never be reached
                };

                Currency randomCurrency = switch (randomCurrencyIndex) {
                    case 1 -> vndCurrency;
                    case 2 -> krwCurrency;
                    case 3 -> jpyCurrency;
                    default -> vndCurrency; // Default case, should never be reached
                };

                FinancialPlanExpense expense = FinancialPlanExpense.builder()
                        .name(listExpenseName[randomExpenseNameIndex])
                        .unitPrice(BigDecimal.valueOf(random.nextInt(500000) + 10000L))
                        .amount(random.nextInt(10) + 1)
                        .project(randomProject)
                        .supplier(randomSupplier)
                        .pic(randomPic)
                        .status(randomExpenseStatus)
                        .costType(randomCostType)
                        .currency(randomCurrency)
                        .build();

                expenseList.add(expense);
            }


            // Mapping expense to plan
            index = 0;
            fileExpenses = new ArrayList<>();
            for (FinancialPlanExpense expense : expenseList) {
                int randomFilePlanIndex = random.nextInt(5) + 1;
                FinancialPlanFile randomFile = switch (randomFilePlanIndex) {
                    case 1 -> planTester1_1;
                    case 2 -> planTester1_2;
                    case 3 -> planTester1_3;
                    case 4 -> planTester2_1;
                    case 5 -> planTester2_2;
                    default -> planTester1_1; // Default case, should never be reached
                };

                if (expense.getStatus().getCode().equals(ExpenseStatusCode.APPROVED)) {
                    String prefixCode = randomFile.getPlan().getTerm().getName().replace(" ", "_");
                    expense.setPlanExpenseKey(prefixCode + "_Report_" + ++index);
                }

                fileExpenses.add(FinancialPlanFileExpense.builder()
                        .file(randomFile)
                        .planExpense(expense)
                        .build());

            }

            financialPlanExpenseRepository.saveAll(expenseList);
            financialPlanFileExpenseRepository.saveAll(fileExpenses);

            // Get 64 random expense usd and euro for test plan
            expenseList = new ArrayList<>();

            for (int i = 1; i <= 64; i++) {
                int randomStatusIndex = random.nextInt(4) + 1;
                int randomExpenseNameIndex = random.nextInt(listExpenseName.length);
                int randomCostTypeIndex = random.nextInt(6) + 1;
                int randomProjectIndex = random.nextInt(6) + 1;
                int randomSupplierIndex = random.nextInt(6) + 1;
                int randomPicIndex = random.nextInt(5) + 1;
                int randomCurrencyIndex = random.nextInt(2) + 1;

                ExpenseStatus randomExpenseStatus = switch (randomStatusIndex) {
                    case 2 -> expenseStatus2;
                    case 3 -> expenseStatus3;
                    default -> expenseStatus2; // Default case, should never be reached
                };

                CostType randomCostType = switch (randomCostTypeIndex) {
                    case 1 -> costType1;
                    case 2 -> costType2;
                    case 3 -> costType3;
                    case 4 -> costType4;
                    case 5 -> costType5;
                    case 6 -> costType6;
                    default -> costType1; // Default case, should never be reached
                };

                Project randomProject = switch (randomProjectIndex) {
                    case 1 -> project1;
                    case 2 -> project2;
                    case 3 -> project3;
                    case 4 -> project4;
                    case 5 -> project5;
                    case 6 -> project6;
                    default -> project1; // Default case, should never be reached
                };

                Supplier randomSupplier = switch (randomSupplierIndex) {
                    case 1 -> supplier1;
                    case 2 -> supplier2;
                    case 3 -> supplier3;
                    case 4 -> supplier4;
                    case 5 -> supplier5;
                    case 6 -> supplier6;
                    default -> supplier1; // Default case, should never be reached
                };

                User randomPic = switch (randomPicIndex) {
                    case 1 -> user6;
                    case 2 -> user7;
                    case 3 -> user8;
                    case 4 -> user9;
                    case 5 -> user10;
                    default -> user6; // Default case, should never be reached
                };

                Currency randomCurrency = switch (randomCurrencyIndex) {
                    case 1 -> usdCurrency;
                    case 2 -> eurCurrency;
                    default -> usdCurrency; // Default case, should never be reached
                };

                FinancialPlanExpense expense = FinancialPlanExpense.builder()
                        .name(listExpenseName[randomExpenseNameIndex])
                        .unitPrice(BigDecimal.valueOf(random.nextInt(1000) + 900))
                        .amount(random.nextInt(10) + 1)
                        .project(randomProject)
                        .supplier(randomSupplier)
                        .pic(randomPic)
                        .status(randomExpenseStatus)
                        .costType(randomCostType)
                        .currency(randomCurrency)
                        .build();
                expenseList.add(expense);
            }

            // Mapping expense to plan
            fileExpenses = new ArrayList<>();
            for (FinancialPlanExpense expense : expenseList) {

                int randomFilePlanIndex = random.nextInt(5) + 1;

                FinancialPlanFile randomFile = switch (randomFilePlanIndex) {
                    case 1 -> planTester1_1;
                    case 2 -> planTester1_2;
                    case 3 -> planTester1_3;
                    case 4 -> planTester2_1;
                    case 5 -> planTester2_2;
                    default -> planTester1_1; // Default case, should never be reached
                };
                if (expense.getStatus().getCode().equals(ExpenseStatusCode.APPROVED)) {
                    String prefixCode = randomFile.getPlan().getTerm().getName().replace(" ", "_");
                    expense.setPlanExpenseKey(prefixCode + "_Report_" + ++index);
                }

                fileExpenses.add(FinancialPlanFileExpense.builder()
                        .file(randomFile)
                        .planExpense(expense)
                        .build());

            }

            financialPlanExpenseRepository.saveAll(expenseList);
            financialPlanFileExpenseRepository.saveAll(fileExpenses);

            List<FinancialReport> reports2022 = new ArrayList<>();

            for (Term term : term2022List) {
                FinancialReport report = FinancialReport.builder()
                        .name(term.getName() + "_Report")
                        .month(term.getFinalEndTermDate().toLocalDate())
                        .expectedCost(BigDecimal.valueOf(random.nextLong(500000000) + 3000000000L))
                        .actualCost(BigDecimal.valueOf(random.nextLong(500000000) + 2000000000L))
                        .status(closedReportStatus)
                        .term(term)
                        .build();

                reports2022.add(report);
            }

            List<FinancialReport> reports2023 = new ArrayList<>();

            for (Term term : term2023List) {
                FinancialReport report = FinancialReport.builder()
                        .name(term.getName() + "_Report")
                        .month(term.getFinalEndTermDate().toLocalDate())
                        .expectedCost(BigDecimal.valueOf(random.nextLong(500000000) + 2500000000L))
                        .actualCost(BigDecimal.valueOf(random.nextLong(500000000) + 2000000000L))
                        .status(closedReportStatus)
                        .term(term)
                        .build();

                reports2023.add(report);
            }

            List<FinancialReport> reports2024 = new ArrayList<>();

            for (Term term : term2024List) {
                FinancialReport report = FinancialReport.builder()
                        .name(term.getName() + "_Report")
                        .month(term.getFinalEndTermDate().toLocalDate())
                        .expectedCost(BigDecimal.valueOf(random.nextLong(500000000) + 2500000000L))
                        .actualCost(BigDecimal.valueOf(random.nextLong(500000000) + 2000000000L))
                        .status(closedReportStatus)
                        .term(term)
                        .build();

                if (term.getFinalEndTermDate().getMonthValue() > 8) {
                    report.setStatus(newReportStatus);
                    report.setExpectedCost(BigDecimal.ZERO);
                    report.setActualCost(BigDecimal.ZERO);
                } else if (term.getFinalEndTermDate().getMonthValue() == 8) {
                    report.setStatus(reviewedReportStatus);
                    report.setExpectedCost(BigDecimal.ZERO);
                    report.setActualCost(BigDecimal.ZERO);
                }

                reports2024.add(report);
            }

            FinancialReport reportTester1 = FinancialReport.builder()
                    .name(termTester1.getName() + "_Report")
                    .expectedCost(BigDecimal.valueOf(901660487L))
                    .actualCost(BigDecimal.valueOf(616579382))
                    .month(termTester1.getFinalEndTermDate().toLocalDate())
                    .status(newReportStatus)
                    .term(termTester1)
                    .build();

            FinancialReport reportTester2 = FinancialReport.builder()
                    .name(termTester2.getName() + "_Report")
                    .expectedCost(BigDecimal.valueOf(131660487L))
                    .actualCost(BigDecimal.valueOf(1216579382))
                    .month(termTester2.getFinalEndTermDate().toLocalDate())
                    .status(reviewedReportStatus)
                    .term(termTester2)
                    .build();

            FinancialReport reportTester3 = FinancialReport.builder()
                    .name(termTester3.getName() + "_Report")
                    .expectedCost(BigDecimal.valueOf(151660487L))
                    .actualCost(BigDecimal.valueOf(1006579382))
                    .month(termTester3.getFinalEndTermDate().toLocalDate())
                    .status(reviewedReportStatus)
                    .term(termTester3)
                    .build();


            financialReportRepository.saveAll(reports2022);
            financialReportRepository.saveAll(reports2023);
            financialReportRepository.saveAll(reports2024);
            financialReportRepository.saveAll(List.of(reportTester1, reportTester2, reportTester3));

            List<CurrencyExchangeRate> exchangeRates2022 = new ArrayList<>();

            for (int i = 0; i < 12; i++) {
                CurrencyExchangeRate exchangeRate1 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2022, i + 1, 23))
                        .currency(vndCurrency)
                        .amount(BigDecimal.valueOf(1))
                        .build();

                CurrencyExchangeRate exchangeRate2 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2022, i + 1, 23))
                        .currency(usdCurrency)
                        .amount(BigDecimal.valueOf(random.nextInt(2200) + 22635))
                        .build();

                CurrencyExchangeRate exchangeRate3 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2022, i + 1, 23))
                        .currency(jpyCurrency)
                        .amount(BigDecimal.valueOf(random.nextInt(37) + 162))
                        .build();

                CurrencyExchangeRate exchangeRate4 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2022, i + 1, 23))
                        .currency(krwCurrency)
                        .amount(BigDecimal.valueOf(random.nextDouble(2.6) + 16.567))
                        .build();

                CurrencyExchangeRate exchangeRate5 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2022, i + 1, 23))
                        .currency(eurCurrency)
                        .amount(BigDecimal.valueOf(random.nextInt(3320) + 22780))
                        .build();

                exchangeRates2022.addAll(List.of(exchangeRate1, exchangeRate2, exchangeRate3, exchangeRate4, exchangeRate5));
            }

            currencyExchangeRateRepository.saveAll(exchangeRates2022);

            List<CurrencyExchangeRate> exchangeRates2023 = new ArrayList<>();

            for (int i = 0; i < 12; i++) {
                CurrencyExchangeRate exchangeRate1 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2023, i + 1, 23))
                        .currency(vndCurrency)
                        .amount(BigDecimal.valueOf(1))
                        .build();

                CurrencyExchangeRate exchangeRate2 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2023, i + 1, 23))
                        .currency(usdCurrency)
                        .amount(BigDecimal.valueOf(random.nextInt(1200) + 23430))
                        .build();

                CurrencyExchangeRate exchangeRate3 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2023, i + 1, 23))
                        .currency(jpyCurrency)
                        .amount(BigDecimal.valueOf(random.nextDouble(23) + 160))
                        .build();

                CurrencyExchangeRate exchangeRate4 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2023, i + 1, 23))
                        .currency(krwCurrency)
                        .amount(BigDecimal.valueOf(random.nextDouble(1.8) + 17.471))
                        .build();

                CurrencyExchangeRate exchangeRate5 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2023, i + 1, 23))
                        .currency(eurCurrency)
                        .amount(BigDecimal.valueOf(random.nextInt(3300) + 24710))
                        .build();

                exchangeRates2023.addAll(List.of(exchangeRate1, exchangeRate2, exchangeRate3, exchangeRate4, exchangeRate5));
            }

            currencyExchangeRateRepository.saveAll(exchangeRates2023);

            List<CurrencyExchangeRate> exchangeRates2024 = new ArrayList<>();

            for (int i = 0; i < 12; i++) {
                CurrencyExchangeRate exchangeRate1 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2024, i + 1, 23))
                        .currency(vndCurrency)
                        .amount(BigDecimal.valueOf(1))
                        .build();

                CurrencyExchangeRate exchangeRate2 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2024, i + 1, 23))
                        .currency(usdCurrency)
                        .amount(BigDecimal.valueOf(random.nextInt(1200) + 24269))
                        .build();

                CurrencyExchangeRate exchangeRate3 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2024, i + 1, 23))
                        .currency(jpyCurrency)
                        .amount(BigDecimal.valueOf(random.nextInt(17) + 157))
                        .build();

                CurrencyExchangeRate exchangeRate4 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2024, i + 1, 23))
                        .currency(krwCurrency)
                        .amount(BigDecimal.valueOf(random.nextDouble(0.76) + 18.126))
                        .build();

                CurrencyExchangeRate exchangeRate5 = CurrencyExchangeRate.builder()
                        .month(LocalDate.of(2024, i + 1, 23))
                        .currency(eurCurrency)
                        .amount(BigDecimal.valueOf(random.nextInt(1600) + 26159))
                        .build();
                exchangeRates2024.addAll(List.of(exchangeRate1, exchangeRate2, exchangeRate3, exchangeRate4, exchangeRate5));
            }

            currencyExchangeRateRepository.saveAll(exchangeRates2024);


//            //START TERM
//            List<Term> terms = termRepository.getListTermNeedToStartForSeed(LocalDateTime.now());
//            //change status to 2 (IN_PROGRESS)
//            if (terms != null) {
//                for (Term term : terms) {
//                    TermStatus inProgressStatus = termStatusRepository.findByCode(TermStatusCode.IN_PROGRESS);
//                    term.setStatus(inProgressStatus);
//                    termRepository.save(term);
//
//                    ReportStatus newStatus = reportStatusRepository.findByCode(ReportStatusCode.NEW);
//
//                    // Create new report
//                    FinancialReport report = FinancialReport.builder()
//                            .name(term.getName() + " Report")
//                            .month(term.getFinalEndTermDate().toLocalDate())
//                            .term(term)
//                            .status(closedReportStatus)
//                            .build();
//
//                    // Generate report
//                    financialReportRepository.save(report);
//
//                }
//            }


            //END TERM
            List<Term> terms = termRepository.getListTermNeedToCloseForSeed(LocalDateTime.now());
            terms.removeIf(term -> term.getName().equals(termTester1.getName()));
            terms.removeIf(term -> term.getName().equals(termTester2.getName()));
            terms.removeIf(term -> term.getName().equals(termTester3.getName()));
            //change status to CLOSED
            if (terms != null) {
                for (Term term : terms) {

                    TermStatus closedStatus = termStatusRepository.findByCode(TermStatusCode.CLOSED);
                    term.setStatus(closedStatus);
                    termRepository.save(term);

                    List<FinancialPlanExpense> listExpenseNeedToClose = new ArrayList<>();

                    ExpenseStatus denyStatus = expenseStatusRepository.findByCode(ExpenseStatusCode.DENIED);

                    // Denied expenses not approval
                    planRepository.findAllByTermId(term.getId()).forEach(plan -> {
                        // Get list expense have status waiting
                        financialPlanExpenseRepository.getListExpenseNeedToCloseByPlanId(plan.getId(), ExpenseStatusCode.NEW).forEach(expense -> {

                            // Change expense status from waiting to deny
                            expense.setStatus(denyStatus);
                            listExpenseNeedToClose.add(expense);
                        });

                        // Change expense status
                        financialPlanExpenseRepository.saveAll(listExpenseNeedToClose);

                    });

                    Currency defaultCurrency = currencyRepository.getDefaultCurrency();
                    generateActualCostAndExpectedCostForPlan(term.getId(), defaultCurrency);
                    generateActualCostAndExpectedCostForReport(term.getId(), defaultCurrency);
                    generateReportStatistical(term.getId(), defaultCurrency);
                }
            }

            // Generate annual 2022 report
            AnnualReportResult annualReportResult2022 = annualReportRepository.getAnnualReport(LocalDate.of(2022, 12, 25));
            if (annualReportResult2022 != null) {
                AnnualReport annualReport = new AnnualReportMapperImpl().mapToAnnualReportMapping(annualReportResult2022);

                // Generate report for annual report
                List<ReportResult> reportResults = annualReportRepository.generateReport(LocalDate.of(2022, 12, 25));

                long totalCostOfYear = 0L;

                for (ReportResult reportResult : reportResults) {
                    totalCostOfYear += reportResult.getTotalExpense().longValue();
                }

                List<MonthlyReportSummary> monthlyReportSummaries = new ArrayList<>();
                reportResults.forEach(reportResult -> {
                    MonthlyReportSummary monthlyReportSummary = new AnnualReportMapperImpl().mapToReportMapping(reportResult);
                    monthlyReportSummary.setAnnualReport(annualReport);
                    monthlyReportSummaries.add(monthlyReportSummary);
                });

                annualReport.setMonthlyReportSummaries(monthlyReportSummaries);
                annualReport.setTotalExpense(BigDecimal.valueOf(totalCostOfYear));

                annualReportRepository.save(annualReport);
                monthlyReportSummaryRepository.saveAll(monthlyReportSummaries);
            }

            // Generate annual 2023 report
            AnnualReportResult annualReportResult2023 = annualReportRepository.getAnnualReport(LocalDate.of(2023, 12, 25));
            if (annualReportResult2023 != null) {
                AnnualReport annualReport = new AnnualReportMapperImpl().mapToAnnualReportMapping(annualReportResult2023);

                // Generate report for annual report
                List<ReportResult> reportResults = annualReportRepository.generateReport(LocalDate.of(2023, 12, 25));

                long totalCostOfYear = 0L;

                for (ReportResult reportResult : reportResults) {
                    totalCostOfYear += reportResult.getTotalExpense().longValue();
                }

                List<MonthlyReportSummary> monthlyReportSummaries = new ArrayList<>();
                reportResults.forEach(reportResult -> {
                    MonthlyReportSummary monthlyReportSummary = new AnnualReportMapperImpl().mapToReportMapping(reportResult);
                    monthlyReportSummary.setAnnualReport(annualReport);
                    monthlyReportSummaries.add(monthlyReportSummary);
                });

                annualReport.setMonthlyReportSummaries(monthlyReportSummaries);
                annualReport.setTotalExpense(BigDecimal.valueOf(totalCostOfYear));

                annualReportRepository.save(annualReport);
                monthlyReportSummaryRepository.saveAll(monthlyReportSummaries);
            }
        };
    }

    private void generateActualCostAndExpectedCostForPlan(Long termId, Currency defaultCurrency) {
        List<FinancialPlan> plans = planRepository.getReferenceByTermId(termId);
        List<FinancialPlan> savePlan = new ArrayList<>();
        if (plans != null) {
            plans.forEach(plan -> {
                plan.setExpectedCost(calculateCost(planRepository.calculateCostByPlanId(plan.getId(), null), defaultCurrency).getCost());

                plan.setActualCost(calculateCost(planRepository.calculateCostByPlanId(plan.getId(), ExpenseStatusCode.APPROVED), defaultCurrency).getCost());
                savePlan.add(plan);
            });

            planRepository.saveAll(plans);
        }
    }


    private void generateActualCostAndExpectedCostForReport(Long termId, Currency defaultCurrency) {
        FinancialReport report = financialReportRepository.getReferenceByTermId(termId);
        if (report != null) {
            financialReportRepository.calculateCostByReportIdAndStatus(report.getId(), null);
            ;

            report.setExpectedCost(calculateCost(financialReportRepository.calculateCostByReportIdAndStatus(report.getId(), null), defaultCurrency).getCost());

            report.setActualCost(calculateCost(financialReportRepository.calculateCostByReportIdAndStatus(report.getId(), ExpenseStatusCode.APPROVED), defaultCurrency).getCost());
            financialReportRepository.save(report);
        }
    }

    private void generateReportStatistical(Long termId, Currency defaultCurrency) {
        FinancialReport report = financialReportRepository.getReferenceByTermId(termId);
        if (report != null) {

            List<CostStatisticalByCurrencyResult> costStaticalByCurrencies = reportStatisticRepository.getListCostStatistical(report.getId(), ExpenseStatusCode.APPROVED);

            if (costStaticalByCurrencies != null) {
                List<ReportStatistical> reportStatistics = new ArrayList<>();

                // Inner hashmap: map by currency id
                HashMap<String, HashMap<Long, List<CostStatisticalByCurrencyResult>>> fromCurrencyIdHashMap = new HashMap<>();

                Set<PaginateExchange> monthYearSet = new HashSet<>();

                costStaticalByCurrencies.forEach(costStatisticalByCurrency -> {
                    fromCurrencyIdHashMap.putIfAbsent(costStatisticalByCurrency.getDepartmentId() + "_" + costStatisticalByCurrency.getCostTypeId(), new HashMap<>());
                    monthYearSet.add(PaginateExchange.builder()
                            .month(costStatisticalByCurrency.getMonth())
                            .year(costStatisticalByCurrency.getYear())
                            .build());
                });

                costStaticalByCurrencies.forEach(costStatisticalByCurrency -> {
                    fromCurrencyIdHashMap.get(costStatisticalByCurrency.getDepartmentId() + "_" + costStatisticalByCurrency.getCostTypeId())
                            .putIfAbsent(costStatisticalByCurrency.getCurrencyId(), new ArrayList<>());
                });

                costStaticalByCurrencies.forEach(costStatisticalByCurrency -> {
                    fromCurrencyIdHashMap.get(costStatisticalByCurrency.getDepartmentId() + "_" + costStatisticalByCurrency.getCostTypeId())
                            .get(costStatisticalByCurrency.getCurrencyId()).add(costStatisticalByCurrency);
                });

                // Get list exchange rates
                Set<Long> currencyIds = new HashSet<>();
                for (String depIdCostId : fromCurrencyIdHashMap.keySet()) {
                    currencyIds.addAll(fromCurrencyIdHashMap.get(depIdCostId).keySet());
                }

                currencyIds.add(defaultCurrency.getId());

                // Get list exchange rates
                List<CurrencyExchangeRate> exchangeRates = currencyExchangeRateRepository.getListCurrencyExchangeRateByMonthYear(monthYearSet.stream().toList(), currencyIds.stream().toList());

                // Outer hashmap: map by string (department id + cost type id), date, currency id
                HashMap<String, HashMap<String, HashMap<Long, BigDecimal>>> exchangeRateHashMap = new HashMap<>();

                costStaticalByCurrencies.forEach(costStatisticalByCurrencyResult -> {
                    exchangeRateHashMap.putIfAbsent(costStatisticalByCurrencyResult.getDepartmentId() + "_" + costStatisticalByCurrencyResult.getCostTypeId(), new HashMap<>());
                });

                exchangeRateHashMap.keySet().forEach(depIdCostId -> {
                    exchangeRates.forEach(exchangeRate -> {
                        exchangeRateHashMap.get(depIdCostId).putIfAbsent(exchangeRate.getMonth().format(DateTimeFormatter.ofPattern("M/yyyy")), new HashMap<>());
                    });
                });

                exchangeRateHashMap.keySet().forEach(depIdCostId -> {
                    exchangeRates.forEach(exchangeRate -> {
                        exchangeRateHashMap.get(depIdCostId).get(exchangeRate.getMonth().format(DateTimeFormatter.ofPattern("M/yyyy"))).put(exchangeRate.getCurrency().getId(), exchangeRate.getAmount());
                    });
                });

                for (String depIdCostId : fromCurrencyIdHashMap.keySet()) {
                    BigDecimal totalCost = BigDecimal.valueOf(0);
                    BigDecimal formAmount = null;
                    BigDecimal toAmount = null;
                    BigDecimal biggestCost = null;
                    BigDecimal maxBiggestCost = BigDecimal.valueOf(0);
                    HashMap<Long, List<CostStatisticalByCurrencyResult>> depIdCostIdHashMap = fromCurrencyIdHashMap.get(depIdCostId);

                    for (Long fromCurrencyId : depIdCostIdHashMap.keySet()) {

                        for (CostStatisticalByCurrencyResult costStatisticalByCurrency : depIdCostIdHashMap.get(fromCurrencyId)) {

                            formAmount = exchangeRateHashMap.get(costStatisticalByCurrency.getDepartmentId() + "_" + costStatisticalByCurrency.getCostTypeId())
                                    .get(costStatisticalByCurrency.getMonth() + "/" + costStatisticalByCurrency.getYear())
                                    .get(fromCurrencyId);

                            toAmount = exchangeRateHashMap.get(costStatisticalByCurrency.getDepartmentId() + "_" + costStatisticalByCurrency.getCostTypeId())
                                    .get(costStatisticalByCurrency.getMonth() + "/" + costStatisticalByCurrency.getYear())
                                    .get(defaultCurrency.getId());

                            if (costStatisticalByCurrency.getCost() != null && toAmount != null && formAmount != null) {
                                totalCost = totalCost.add(costStatisticalByCurrency.getCost().multiply(formAmount).divide(toAmount, 2, RoundingMode.CEILING));
                            }

                            if (costStatisticalByCurrency.getBiggestCost() != null && toAmount != null && formAmount != null) {
                                biggestCost = costStatisticalByCurrency.getBiggestCost().multiply(formAmount).divide(toAmount, 2, RoundingMode.CEILING);
                            }

                            if (biggestCost != null) {
                                if (maxBiggestCost.longValue() < biggestCost.longValue()) {
                                    maxBiggestCost = biggestCost;
                                }
                            }
                        }
                    }
                    // Split the string using the "_" delimiter
                    String[] parts = depIdCostId.split("_");

                    // Parse the parts to Long
                    Long departmentId = Long.parseLong(parts[0]);
                    Long costTypeId = Long.parseLong(parts[1]);


                    reportStatistics.add(
                            ReportStatistical.builder()
                                    .report(report)
                                    .totalExpense(totalCost)
                                    .biggestExpenditure(maxBiggestCost)
                                    .department(departmentRepository.getReferenceById(departmentId))
                                    .costType(costTypeRepository.getReferenceById(costTypeId))
                                    .build());
                }
                reportStatisticRepository.saveAll(reportStatistics);
            }
        }
    }

    private CostResult calculateCost(List<TotalCostByCurrencyResult> costByCurrencyResults, Currency defaultCurrency) {
        if (costByCurrencyResults == null) {
            return CostResult.builder().cost(BigDecimal.valueOf(0))
                    .currency(defaultCurrency)
                    .build();
        }

        // Inner hashmap: map by currency id
        HashMap<Long, List<TotalCostByCurrencyResult>> fromCurrencyIdHashMap = new HashMap<>();

        Set<PaginateExchange> monthYearSet = new HashSet<>();

        costByCurrencyResults.forEach(costByCurrency -> {
            fromCurrencyIdHashMap.putIfAbsent(costByCurrency.getCurrencyId(), new ArrayList<>());
        });

        costByCurrencyResults.forEach(costByCurrency -> {
            fromCurrencyIdHashMap.get(costByCurrency.getCurrencyId()).add(costByCurrency);
            monthYearSet.add(PaginateExchange.builder()
                    .month(costByCurrency.getMonth())
                    .year(costByCurrency.getYear())
                    .build());
        });

        // Get list exchange rates
        List<Long> currencyIds = new ArrayList<>(fromCurrencyIdHashMap.keySet().stream().toList());
        currencyIds.add(defaultCurrency.getId());

        // Get list exchange rates
        List<CurrencyExchangeRate> exchangeRates = currencyExchangeRateRepository.getListCurrencyExchangeRateByMonthYear(monthYearSet.stream().toList(), currencyIds);

        // Outer hashmap: map by date
        HashMap<String, HashMap<Long, BigDecimal>> exchangeRateHashMap = new HashMap<>();

        exchangeRates.forEach(exchangeRate -> {
            exchangeRateHashMap.putIfAbsent(exchangeRate.getMonth().format(DateTimeFormatter.ofPattern("M/yyyy")), new HashMap<>());
        });

        exchangeRates.forEach(exchangeRate -> {
            exchangeRateHashMap.get(exchangeRate.getMonth().format(DateTimeFormatter.ofPattern("M/yyyy"))).put(exchangeRate.getCurrency().getId(), exchangeRate.getAmount());
        });

        BigDecimal actualCost = BigDecimal.valueOf(0);

        for (Long fromCurrencyId : fromCurrencyIdHashMap.keySet()) {
            for (TotalCostByCurrencyResult costByCurrency : fromCurrencyIdHashMap.get(fromCurrencyId)) {
                BigDecimal formAmount = BigDecimal.valueOf(exchangeRateHashMap.get(costByCurrency.getMonth() + "/" + costByCurrency.getYear()).get(fromCurrencyId).longValue());
                BigDecimal toAmount = BigDecimal.valueOf(exchangeRateHashMap.get(costByCurrency.getMonth() + "/" + costByCurrency.getYear()).get(defaultCurrency.getId()).longValue());
                actualCost = actualCost.add(costByCurrency.getTotalCost().multiply(formAmount).divide(toAmount, 2, RoundingMode.CEILING));
            }
        }
        return CostResult.builder().cost(actualCost).currency(defaultCurrency).build();
    }
}