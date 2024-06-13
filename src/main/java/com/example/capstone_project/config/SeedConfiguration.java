package com.example.capstone_project.config;

import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.*;
import com.example.capstone_project.utils.enums.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class SeedConfiguration {
    private final PasswordEncoder passwordEncoder;

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
            PlanStatusRepository planStatusRepository,
            CostTypeRepository costTypeRepository,
            ExpenseStatusRepository expenseStatusRepository,
            FinancialPlanFileRepository financialPlanFileRepository,
            FinancialPlanFileExpenseRepository financialPlanFileExpenseRepository,
            FinancialPlanExpenseRepository financialPlanExpenseRepository
    ) {
        return args -> {
            // Department
            Department itDepartment = Department.builder()
                    .name("IT")
                    .build();

            Department hrDepartment = Department.builder()
                    .name("HR")
                    .build();

            Department financeDepartment = Department.builder()
                    .name("Finance")
                    .build();

            Department communicationDepartment = Department.builder()
                    .name("Communication")
                    .build();

            Department marketingDepartment = Department.builder()
                    .name("Marketing")
                    .build();

            Department accountingDepartment = Department.builder()
                    .name("Accounting")
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

            Authority viewAnnualReport = Authority.builder()
                    .code(AuthorityCode.VIEW_ANNUAL_REPORT)
                    .name("View annual report")
                    .build();

            Authority downloadAnnualReport = Authority.builder()
                    .code(AuthorityCode.DOWNLOAD_ANNUAL_REPORT)
                    .name("Download annual report")
                    .build();

            authorityRepository.saveAll(List.of(viewPlan, createUser, viewListUsers, deleteUser, editUser, activateUser, deactivateUser, createTerm, editTerm, viewTerm, startTerm, deleteTerm, importPlan, reUploadPlan, submitPlanForReview, deletePlan, downloadPlan, approvePlan, viewReport, downloadReport, viewAnnualReport, downloadAnnualReport));

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
                    .username("username1")
                    .password(this.passwordEncoder.encode("password"))
                    .role(admin)
                    .department(accountingDepartment)
                    .position(techlead)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("mail21@gmail.com")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))

                    .build();

            User user2 = User.builder()
                    .username("username2")
                    .password(this.passwordEncoder.encode("password"))
                    .role(admin)
                    .department(financeDepartment)
                    .position(techlead)

                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("Email23@gmail.com")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .build();

            User user3 = User.builder()
                    .username("username3")
                    .password(this.passwordEncoder.encode("password"))
                    .role(accountant)
                    .department(financeDepartment)
                    .position(juniorDev)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("Email23@gmail.com")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .build();

            User user4 = User.builder()
                    .username("username4")
                    .password(this.passwordEncoder.encode("password"))
                    .role(accountant)
                    .department(itDepartment)
                    .position(juniorDev)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))

                    .email("Email23@gmail.com")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .build();

            User user5 = User.builder()
                    .username("username5")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(itDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("Email23@gmail.com")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .build();

            userRepository.saveAll(List.of(user1, user2, user3, user4, user5));

            // User setting
            UserSetting userSetting1 = UserSetting.builder()
                    .user(user1)
                    .darkMode(true)
                    .theme("blue")
                    .language("vi")
                    .build();

            UserSetting userSetting2 = UserSetting.builder()
                    .user(user2)
                    .darkMode(false)
                    .theme("green")
                    .language("en")
                    .build();

            UserSetting userSetting3 = UserSetting.builder()
                    .user(user3)
                    .darkMode(false)
                    .theme("green")
                    .language("vi")
                    .build();

            UserSetting userSetting4 = UserSetting.builder()
                    .user(user4)
                    .darkMode(true)
                    .theme("teal")
                    .language("en")
                    .build();

            UserSetting userSetting5 = UserSetting.builder()
                    .user(user5)
                    .darkMode(true)
                    .theme("teal")
                    .language("vi")
                    .build();

            userSettingRepository.saveAll(List.of(userSetting1, userSetting2, userSetting3, userSetting4, userSetting5));

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

            RoleAuthority financialStaffAuthority1 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(createTerm)
                    .build();

            RoleAuthority financialStaffAuthority2 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(editTerm)
                    .build();

            RoleAuthority financialStaffAuthority3 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(viewTerm)
                    .build();

            RoleAuthority financialStaffAuthority4 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(startTerm)
                    .build();

            RoleAuthority financialStaffAuthority5 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(deleteTerm)
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

            roleAuthorityRepository.saveAll(List.of(adminAuthority1, adminAuthority2, adminAuthority3, adminAuthority4, adminAuthority5, adminAuthority6,
                    accountantAuthority1, accountantAuthority2, accountantAuthority3, accountantAuthority4, accountantAuthority5, accountantAuthority6, accountantAuthority7, accountantAuthority8, accountantAuthority9, accountantAuthority10, accountantAuthority11, accountantAuthority12, accountantAuthority13, accountantAuthority14, accountantAuthority15, accountantAuthority16,
                    financialStaffAuthority1, financialStaffAuthority2, financialStaffAuthority3, financialStaffAuthority4, financialStaffAuthority5, financialStaffAuthority6, financialStaffAuthority7, financialStaffAuthority8, financialStaffAuthority9, financialStaffAuthority10, financialStaffAuthority11, financialStaffAuthority12, financialStaffAuthority13, financialStaffAuthority14, financialStaffAuthority15
            ));

            TermStatus status1 = TermStatus.builder()
                    .id(1L)
                    .name("New")
                    .code(TermCode.NOT_STARTED)
                    .build();

            TermStatus status2 = TermStatus.builder()
                    .id(2L)
                    .name("In-Progress")
                    .code(TermCode.IN_PROGRESS)
                    .build();

            TermStatus status3 = TermStatus.builder()
                    .id(3L)
                    .name("Close")
                    .code(TermCode.CLOSED)
                    .build();

            termStatusRepository.saveAll(List.of(status1, status2, status3));

            PlanStatus planStatus1 = PlanStatus.builder()
                    .id(1L)
                    .code(PlanStatusCode.NEW)
                    .build();

            PlanStatus planStatus2 = PlanStatus.builder()
                    .id(2L)
                    .code(PlanStatusCode.WAITING_FOR_REVIEW)
                    .build();

            PlanStatus planStatus3 = PlanStatus.builder()
                    .id(3L)
                    .code(PlanStatusCode.REVIEWED)
                    .build();

            PlanStatus planStatus4 = PlanStatus.builder()
                    .id(4L)
                    .code(PlanStatusCode.APPROVED)
                    .build();

            planStatusRepository.saveAll(List.of(planStatus1, planStatus2, planStatus3, planStatus4));

            Term term1 = Term.builder()
                    .id(1L)
                    .name("Spring 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                    .endDate(LocalDateTime.of(2025, 1, 31, 23, 59))
                    .planDueDate(LocalDateTime.of(2025, 1, 10, 17, 0))
                    .user(user1)
                    .status(status1)
                    .build();

            Term term2 = Term.builder()
                    .id(2L)
                    .name("Summer 2024")
                    .duration(TermDuration.QUARTERLY)
                    .startDate(LocalDateTime.of(2025, 6, 1, 0, 0))
                    .endDate(LocalDateTime.of(2025, 6, 30, 23, 59))
                    .planDueDate(LocalDateTime.of(2025, 6, 10, 17, 0))
                    .user(user1)
                    .status(status2)
                    .build();

            Term term3 = Term.builder()
                    .id(3L)
                    .name("Fall 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2025, 9, 1, 0, 0))
                    .endDate(LocalDateTime.of(2025, 9, 30, 23, 59))
                    .planDueDate(LocalDateTime.of(2025, 9, 10, 17, 0))
                    .user(user2)
                    .status(status3)
                    .build();

            Term term4 = Term.builder()
                    .id(4L)
                    .name("Winter 2024")
                    .duration(TermDuration.YEARLY)
                    .startDate(LocalDateTime.of(2025, 12, 1, 0, 0))
                    .endDate(LocalDateTime.of(2025, 12, 31, 23, 59))
                    .planDueDate(LocalDateTime.of(2025, 12, 10, 17, 0))
                    .user(user3)
                    .status(status1)
                    .build();

            Term term5 = Term.builder()
                    .id(5L)
                    .name("Spring 2025")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                    .endDate(LocalDateTime.of(2025, 1, 31, 23, 59))
                    .planDueDate(LocalDateTime.of(2025, 1, 10, 17, 0))
                    .user(user4)
                    .status(status2)
                    .build();

            termRepository.saveAll(List.of(term1, term2, term3, term4, term5));

            FinancialPlan financialPlan1 = FinancialPlan.builder()
                    .name("Financial Plan 1")
                    .term(term1)
                    .department(itDepartment)
                    .status(planStatus1)
                    .build();

            FinancialPlan financialPlan2 = FinancialPlan.builder()
                    .name("Financial Plan 2")
                    .term(term1)
                    .department(itDepartment)
                    .status(planStatus2)
                    .build();

            FinancialPlan financialPlan3 = FinancialPlan.builder()
                    .name("Financial Plan 3")
                    .term(term1)
                    .department(accountingDepartment)
                    .status(planStatus3)
                    .build();

            FinancialPlan financialPlan4 = FinancialPlan.builder()
                    .name("Financial Plan 4")
                    .department(financeDepartment)
                    .term(term1)
                    .status(planStatus4)
                    .build();

            FinancialPlan financialPlan5 = FinancialPlan.builder()
                    .name("Financial Plan 5")
                    .term(term2)
                    .department(accountingDepartment)
                    .status(planStatus1)
                    .build();

            planRepository.saveAll(List.of(financialPlan1, financialPlan2, financialPlan3, financialPlan4, financialPlan5));

            CostType costType1 = CostType.builder()
                    .id(1L)
                    .code(CostTypeCode.ADMINISTRATION_COSTS)
                    .build();

            CostType costType2 = CostType.builder()
                    .id(2L)
                    .code(CostTypeCode.DIRECT_COSTS)
                    .build();

            CostType costType3 = CostType.builder()
                    .id(3L)
                    .code(CostTypeCode.INDIRECT_COSTS)
                    .build();

            CostType costType4 = CostType.builder()
                    .id(4L)
                    .code(CostTypeCode.OPERATING_COSTS)
                    .build();

            CostType costType5 = CostType.builder()
                    .id(5L)
                    .code(CostTypeCode.MAINTENANCE_COSTS)
                    .build();

            CostType costType6 = CostType.builder()
                    .id(6L)
                    .code(CostTypeCode.MANUFACTURING_COSTS)
                    .build();

            costTypeRepository.saveAll(List.of(costType1, costType2, costType3, costType4, costType5, costType6));

            ExpenseStatus expenseStatus1 = ExpenseStatus.builder()
                    .id(1L)
                    .code(ExpenseStatusCode.NEW)
                    .build();

            ExpenseStatus expenseStatus2 = ExpenseStatus.builder()
                    .id(2L)
                    .code(ExpenseStatusCode.WAITING_FOR_APPROVAL)
                    .build();

            ExpenseStatus expenseStatus3 = ExpenseStatus.builder()
                    .id(3L)
                    .code(ExpenseStatusCode.APPROVED)
                    .build();

            ExpenseStatus expenseStatus4 = ExpenseStatus.builder()
                    .id(4L)
                    .code(ExpenseStatusCode.DENIED)
                    .build();

            expenseStatusRepository.saveAll(List.of(expenseStatus1, expenseStatus2, expenseStatus3, expenseStatus4));

            FinancialPlanFile financialPlanFile1_1 = FinancialPlanFile.builder()
                    .id(1L)
                    .name("TERM-NAME1_PLAN-NAME1")
                    .version("v1")
                    .plan(financialPlan1)
                    .user(user1)
                    .build();

            FinancialPlanFile financialPlanFile1_2 = FinancialPlanFile.builder()
                    .id(2L)
                    .name("TERM-NAME1_PLAN-NAME1")
                    .version("v2")
                    .plan(financialPlan1)
                    .user(user1)
                    .build();

            FinancialPlanFile financialPlanFile2_1 = FinancialPlanFile.builder()
                    .id(3L)
                    .name("TERM-NAME2_PLAN-NAME2")
                    .version("v1")
                    .plan(financialPlan2)
                    .user(user2)
                    .build();

            FinancialPlanFile financialPlanFile2_2 = FinancialPlanFile.builder()
                    .id(4L)
                    .name("TERM-NAME2_PLAN-NAME2")
                    .version("v2")
                    .plan(financialPlan2)
                    .user(user2)
                    .build();

            FinancialPlanFile financialPlanFile3_1 = FinancialPlanFile.builder()
                    .id(5L)
                    .name("TERM-NAME3_PLAN-NAME3")
                    .version("v1")
                    .plan(financialPlan3)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile3_2 = FinancialPlanFile.builder()
                    .id(6L)
                    .name("TERM-NAME3_PLAN-NAME3")
                    .version("v2")
                    .plan(financialPlan3)
                    .user(user3)
                    .build();

            financialPlanFileRepository.saveAll(List.of(financialPlanFile1_1, financialPlanFile1_2, financialPlanFile2_1, financialPlanFile2_2, financialPlanFile3_1, financialPlanFile3_2));

            // Get 15 random expense
            List<FinancialPlanExpense> expenseList = new ArrayList<>();
            Random random = new Random();
            String[] pics = {"TuNM", "AnhPTH", "HuyHT", "VyNN"};
            char projectNameChar = 'A';
            char supplierNameChar = 'A';

            for (int i = 1; i <= 15; i++) {
                int randomStatusIndex = random.nextInt(4) + 1;
                int randomCostTypeIndex = random.nextInt(6) + 1;
                int randomPicIndex = random.nextInt(pics.length);

                ExpenseStatus randomExpenseStatus = switch (randomStatusIndex) {
                    case 1 -> expenseStatus1;
                    case 2 -> expenseStatus2;
                    case 3 -> expenseStatus3;
                    case 4 -> expenseStatus4;
                    default -> expenseStatus1; // Default case, should never be reached
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

                FinancialPlanExpense expense = FinancialPlanExpense.builder()
                        .planExpenseKey(financialPlanFile1_2.getName() + "_EXPENSE_CODE_" + i)
                        .name("Expense " + projectNameChar)
                        .unitPrice(BigDecimal.valueOf(random.nextInt(5000000) + 1000000))
                        .amount(random.nextInt(10) + 1)
                        .projectName("Project name " + projectNameChar++)
                        .supplierName("Supplier name " + supplierNameChar++)
                        .pic(pics[randomPicIndex])
                        .status(randomExpenseStatus)
                        .costType(randomCostType)
                        .build();

                expenseList.add(expense);
            }

            financialPlanExpenseRepository.saveAll(expenseList);

            FinancialPlanFileExpense fileExpense1 = FinancialPlanFileExpense.builder()
                    .id(1L)
                    .file(financialPlanFile1_2)
                    .planExpense(expenseList.get(0))
                    .build();

            FinancialPlanFileExpense fileExpense2 = FinancialPlanFileExpense.builder()
                    .id(2L)
                    .file(financialPlanFile1_2)
                    .planExpense(expenseList.get(1))
                    .build();
            FinancialPlanFileExpense fileExpense3 = FinancialPlanFileExpense.builder()
                    .id(3L)
                    .file(financialPlanFile1_2)
                    .planExpense(expenseList.get(2))
                    .build();
            FinancialPlanFileExpense fileExpense4 = FinancialPlanFileExpense.builder()
                    .id(4L)
                    .file(financialPlanFile1_2)
                    .planExpense(expenseList.get(3))
                    .build();
            FinancialPlanFileExpense fileExpense5 = FinancialPlanFileExpense.builder()
                    .id(5L)
                    .file(financialPlanFile1_2)
                    .planExpense(expenseList.get(4))
                    .build();
            FinancialPlanFileExpense fileExpense6 = FinancialPlanFileExpense.builder()
                    .id(6L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(5))
                    .build();
            FinancialPlanFileExpense fileExpense7 = FinancialPlanFileExpense.builder()
                    .id(7L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(6))
                    .build();
            FinancialPlanFileExpense fileExpense8 = FinancialPlanFileExpense.builder()
                    .id(8L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(7))
                    .build();
            FinancialPlanFileExpense fileExpense9 = FinancialPlanFileExpense.builder()
                    .id(9L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(8))
                    .build();
            FinancialPlanFileExpense fileExpense10 = FinancialPlanFileExpense.builder()
                    .id(10L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(9))
                    .build();
            FinancialPlanFileExpense fileExpense11 = FinancialPlanFileExpense.builder()
                    .id(11L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(10))
                    .build();
            FinancialPlanFileExpense fileExpense12 = FinancialPlanFileExpense.builder()
                    .id(12L)
                    .file(financialPlanFile3_2)
                    .planExpense(expenseList.get(11))
                    .build();
            FinancialPlanFileExpense fileExpense13 = FinancialPlanFileExpense.builder()
                    .id(13L)
                    .file(financialPlanFile3_2)
                    .planExpense(expenseList.get(12))
                    .build();
            FinancialPlanFileExpense fileExpense14 = FinancialPlanFileExpense.builder()
                    .id(14L)
                    .file(financialPlanFile3_2)
                    .planExpense(expenseList.get(13))
                    .build();
            FinancialPlanFileExpense fileExpense15 = FinancialPlanFileExpense.builder()
                    .id(15L)
                    .file(financialPlanFile3_2)
                    .planExpense(expenseList.get(14))
                    .build();

            financialPlanFileExpenseRepository.saveAll(List.of(fileExpense1, fileExpense2, fileExpense3, fileExpense4, fileExpense15, fileExpense6, fileExpense7, fileExpense8, fileExpense9, fileExpense10, fileExpense11, fileExpense12, fileExpense13, fileExpense14, fileExpense15));
        };
    }
}

