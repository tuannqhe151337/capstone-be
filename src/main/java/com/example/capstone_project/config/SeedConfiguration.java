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
import java.time.LocalDate;
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
            FinancialPlanExpenseRepository financialPlanExpenseRepository,
            FinancialReportRepository financialReportRepository,
            FinancialReportExpenseRepository financialReportExpenseRepository,
            AnnualReportRepository annualReportRepository,
            ReportRepository reportRepository
    ) {
        return args -> {
            if (System.getenv("SPRING_PROFILES_ACTIVE") != null && System.getenv("SPRING_PROFILES_ACTIVE").equals("prod")) {
                return;
            }

            //Term Status - fixed code
            TermStatus termStatus = TermStatus.
                    builder()
                    .id(1L).
                    name("Not started")
                    .code(TermCode.NEW).build();

            //Term Status - fixed code
            TermStatus termStatus2 = TermStatus.
                    builder()
                    .id(2L).
                    name("In progress")
                    .code(TermCode.IN_PROGRESS).build();

            //Term Status - fixed code
            TermStatus termStatus3 = TermStatus.
                    builder()
                    .id(3L).
                    name("Closed")
                    .code(TermCode.CLOSED).build();

            termStatusRepository.saveAll(List.of(termStatus, termStatus2, termStatus3));
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
//view user detail missing
            Authority viewUserDetail = Authority.builder()
                    .code(AuthorityCode.VIEW_USER_DETAILS)
                    .name("view user detail")
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

            authorityRepository.saveAll(List.of(viewUserDetail, viewPlan, createUser, viewListUsers, deleteUser, editUser, activateUser, deactivateUser, createTerm, editTerm, viewTerm, startTerm, deleteTerm, importPlan, reUploadPlan, submitPlanForReview, deletePlan, downloadPlan, approvePlan, viewReport, downloadReport, deleteReport, viewAnnualReport, downloadAnnualReport, createNewDepartment, updateDepartment, deleteDepartment, createNewCostType, deleteCostType, updateCostType));

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
                    .username("Anurakk")
                    .fullName("Nutalomlok Nunu")
                    .password(this.passwordEncoder.encode("password"))
                    .role(admin)
                    .department(accountingDepartment)
                    .position(techlead)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("giangdvhe163178@fpt.edu.vn")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .phoneNumber("0999988877")
                    .build();

            User user2 = User.builder()
                    .username("username2")
                    .fullName("Sitchana Jaejan")
                    .password(this.passwordEncoder.encode("password"))
                    .role(admin)
                    .department(financeDepartment)
                    .isDelete(false)
                    .phoneNumber("0999988877")
                    .position(techlead)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("Emoaihl23@gmail.com")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .build();

            User user3 = User.builder()
                    .username("username3")
                    .fullName("Nguyen The Ngoc")
                    .password(this.passwordEncoder.encode("password"))
                    .role(accountant)
                    .department(financeDepartment)
                    .position(juniorDev)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("Emailh23@gmail.com")
                    .address("Ha Noi ")
                    .phoneNumber("0999988877")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .isDelete(false)
                    .build();

            User user4 = User.builder()
                    .username("username4")
                    .fullName("Choi Woo je")
                    .password(this.passwordEncoder.encode("password"))
                    .role(accountant)
                    .department(itDepartment)
                    .position(juniorDev)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .isDelete(false)
                    .email("Emaifl2h3@gmail.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .build();

            User user5 = User.builder()
                    .username("username5")
                    .fullName("Nguyen The Ngoc")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(itDepartment)
                    .position(staff)
                    .dob(LocalDateTime.of(2000, 4, 2, 2, 3))
                    .email("Email23u@gmail.com")
                    .phoneNumber("0999988877")
                    .address("Ha Noi ")
                    .dob(LocalDateTime.of(2002, 11, 11, 0, 0, 0))
                    .isDelete(false)
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

            RoleAuthority adminAuthority11 = RoleAuthority.builder()
                    .role(admin)
                    .authority(createNewCostType)
                    .build();

            RoleAuthority adminAuthority12 = RoleAuthority.builder()
                    .role(admin)
                    .authority(deleteCostType)
                    .build();

            RoleAuthority adminAuthority13 = RoleAuthority.builder()
                    .role(admin)
                    .authority(updateCostType)
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

            RoleAuthority accountantAuthority17 = RoleAuthority.builder()
                    .role(accountant)
                    .authority(deleteReport)
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

            RoleAuthority financialStaffAuthority16 = RoleAuthority.builder()
                    .role(financialStaff)
                    .authority(deleteReport)
                    .build();

            roleAuthorityRepository.saveAll(List.of(adminAuthority1, adminAuthority2, adminAuthority3, adminAuthority4, adminAuthority5, adminAuthority6, adminAuthority7, adminAuthority8, adminAuthority9, adminAuthority10, adminAuthority11, adminAuthority12, adminAuthority13,
                    accountantAuthority1, accountantAuthority2, accountantAuthority3, accountantAuthority4, accountantAuthority5, accountantAuthority6, accountantAuthority7, accountantAuthority8, accountantAuthority9, accountantAuthority10, accountantAuthority11, accountantAuthority12, accountantAuthority13, accountantAuthority14, accountantAuthority15, accountantAuthority16, accountantAuthority17,
                    financialStaffAuthority1, financialStaffAuthority2, financialStaffAuthority3, financialStaffAuthority4, financialStaffAuthority5, financialStaffAuthority6, financialStaffAuthority7, financialStaffAuthority8, financialStaffAuthority9, financialStaffAuthority10, financialStaffAuthority11, financialStaffAuthority12, financialStaffAuthority13, financialStaffAuthority14, financialStaffAuthority15, financialStaffAuthority16
            ));

            // Plan status
            PlanStatus planStatus1 = PlanStatus.builder()
                    .id(1L)
                    .name("New")
                    .code(PlanStatusCode.NEW)
                    .build();

            PlanStatus planStatus2 = PlanStatus.builder()
                    .id(2L)
                    .name("Waiting for reviewed")
                    .code(PlanStatusCode.WAITING_FOR_REVIEWED)
                    .build();

            PlanStatus planStatus3 = PlanStatus.builder()
                    .id(3L)
                    .name("Reviewed")
                    .code(PlanStatusCode.REVIEWED)
                    .build();

            PlanStatus planStatus4 = PlanStatus.builder()
                    .id(4L)
                    .name("Approved")
                    .code(PlanStatusCode.APPROVED)
                    .build();

            planStatusRepository.saveAll(List.of(planStatus1, planStatus2, planStatus3, planStatus4));

            Term term1 = Term.builder()
                    .id(1L)
                    .name("Spring 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2024, 12, 1, 0, 0))
                    .endDate(LocalDateTime.of(2024, 12, 31, 23, 59))
                    .planDueDate(LocalDateTime.of(2024, 12, 10, 17, 0))
                    .user(user1)
                    .status(termStatus2)
                    .build();

            Term term2 = Term.builder()
                    .id(2L)
                    .name("Summer 2024")
                    .duration(TermDuration.QUARTERLY)
                    .startDate(LocalDateTime.of(2025, 6, 1, 0, 0))
                    .endDate(LocalDateTime.of(2025, 6, 30, 23, 59))
                    .planDueDate(LocalDateTime.of(2025, 6, 10, 17, 0))
                    .user(user1)
                    .status(termStatus2)
                    .build();

            Term term3 = Term.builder()
                    .id(3L)
                    .name("Fall 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2025, 9, 1, 0, 0))
                    .endDate(LocalDateTime.of(2025, 9, 30, 23, 59))
                    .planDueDate(LocalDateTime.of(2025, 9, 10, 17, 0))
                    .user(user2)
                    .status(termStatus3)
                    .build();

            Term term4 = Term.builder()
                    .id(4L)
                    .name("Winter 2024")
                    .duration(TermDuration.HALF_YEARLY)
                    .startDate(LocalDateTime.of(2025, 12, 1, 0, 0))
                    .endDate(LocalDateTime.of(2025, 12, 31, 23, 59))
                    .planDueDate(LocalDateTime.of(2025, 12, 10, 17, 0))
                    .user(user3)
                    .status(termStatus)
                    .build();

            Term term5 = Term.builder()
                    .id(5L)
                    .name("Spring 2025")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                    .endDate(LocalDateTime.of(2025, 1, 31, 23, 59))
                    .planDueDate(LocalDateTime.of(2025, 1, 10, 17, 0))
                    .user(user4)
                    .status(termStatus2)
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
                    .term(term2)
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
                    .name("Administration cost")
                    .build();

            CostType costType2 = CostType.builder()
                    .name("Direct costs")
                    .build();

            CostType costType3 = CostType.builder()
                    .name("Indirect cost")
                    .build();

            CostType costType4 = CostType.builder()
                    .name("Operating costs")
                    .build();

            CostType costType5 = CostType.builder()
                    .name("Maintenance costs")
                    .build();

            CostType costType6 = CostType.builder()
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
                    .name("Waiting for approval")
                    .code(ExpenseStatusCode.WAITING_FOR_APPROVAL)
                    .build();

            ExpenseStatus expenseStatus3 = ExpenseStatus.builder()
                    .id(3L)
                    .name("Approved")
                    .code(ExpenseStatusCode.APPROVED)
                    .build();

            ExpenseStatus expenseStatus4 = ExpenseStatus.builder()
                    .id(4L)
                    .name("Denied")
                    .code(ExpenseStatusCode.DENIED)
                    .build();

            expenseStatusRepository.saveAll(List.of(expenseStatus1, expenseStatus2, expenseStatus3, expenseStatus4));

            FinancialPlanFile financialPlanFile1_1 = FinancialPlanFile.builder()
                    .id(1L)
                    .name("TERM-NAME1_PLAN-NAME1")
                    .plan(financialPlan1)
                    .user(user1)
                    .build();

            FinancialPlanFile financialPlanFile1_2 = FinancialPlanFile.builder()
                    .id(2L)
                    .name("TERM-NAME1_PLAN-NAME1")
                    .plan(financialPlan1)
                    .user(user1)
                    .build();

            FinancialPlanFile financialPlanFile2_1 = FinancialPlanFile.builder()
                    .id(3L)
                    .name("TERM-NAME2_PLAN-NAME2")
                    .plan(financialPlan2)
                    .user(user2)
                    .build();

            FinancialPlanFile financialPlanFile2_2 = FinancialPlanFile.builder()
                    .id(4L)
                    .name("TERM-NAME2_PLAN-NAME2")
                    .plan(financialPlan2)
                    .user(user2)
                    .build();

            FinancialPlanFile financialPlanFile3_1 = FinancialPlanFile.builder()
                    .id(5L)
                    .name("TERM-NAME3_PLAN-NAME3")
                    .plan(financialPlan3)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile3_2 = FinancialPlanFile.builder()
                    .id(6L)
                    .name("TERM-NAME3_PLAN-NAME3")
                    .plan(financialPlan3)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile4_1 = FinancialPlanFile.builder()
                    .id(7L)
                    .name("TERM-NAME3_PLAN-NAME4")
                    .plan(financialPlan4)
                    .user(user4)
                    .build();

            FinancialPlanFile financialPlanFile4_2 = FinancialPlanFile.builder()
                    .id(8L)
                    .name("TERM-NAME3_PLAN-NAME3")
                    .plan(financialPlan4)
                    .user(user4)
                    .build();

            FinancialPlanFile financialPlanFile5_1 = FinancialPlanFile.builder()
                    .id(9L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user4)
                    .build();

            FinancialPlanFile financialPlanFile5_2 = FinancialPlanFile.builder()
                    .id(10L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_3 = FinancialPlanFile.builder()
                    .id(11L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_4 = FinancialPlanFile.builder()
                    .id(12L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_5 = FinancialPlanFile.builder()
                    .id(13L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_6 = FinancialPlanFile.builder()
                    .id(14L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_7 = FinancialPlanFile.builder()
                    .id(15L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_8 = FinancialPlanFile.builder()
                    .id(16L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_9 = FinancialPlanFile.builder()
                    .id(17L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_10 = FinancialPlanFile.builder()
                    .id(18L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_11 = FinancialPlanFile.builder()
                    .id(19L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_12 = FinancialPlanFile.builder()
                    .id(20L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_13 = FinancialPlanFile.builder()
                    .id(21L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_14 = FinancialPlanFile.builder()
                    .id(22L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_15 = FinancialPlanFile.builder()
                    .id(23L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_16 = FinancialPlanFile.builder()
                    .id(24L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_17 = FinancialPlanFile.builder()
                    .id(25L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_18 = FinancialPlanFile.builder()
                    .id(26L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_19 = FinancialPlanFile.builder()
                    .id(27L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_20 = FinancialPlanFile.builder()
                    .id(28L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_21 = FinancialPlanFile.builder()
                    .id(29L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_22 = FinancialPlanFile.builder()
                    .id(30L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_23 = FinancialPlanFile.builder()
                    .id(31L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile5_24 = FinancialPlanFile.builder()
                    .id(32L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan5)
                    .user(user3)
                    .build();

            financialPlanFileRepository.saveAll(List.of(financialPlanFile1_1, financialPlanFile1_2, financialPlanFile2_1, financialPlanFile2_2, financialPlanFile3_1, financialPlanFile3_2, financialPlanFile4_1, financialPlanFile4_2, financialPlanFile5_1, financialPlanFile5_2, financialPlanFile5_3, financialPlanFile5_4, financialPlanFile5_5, financialPlanFile5_6, financialPlanFile5_7, financialPlanFile5_8, financialPlanFile5_9, financialPlanFile5_10, financialPlanFile5_11, financialPlanFile5_12, financialPlanFile5_13, financialPlanFile5_14, financialPlanFile5_15, financialPlanFile5_16, financialPlanFile5_17, financialPlanFile5_18, financialPlanFile5_19, financialPlanFile5_20, financialPlanFile5_21, financialPlanFile5_22, financialPlanFile5_23, financialPlanFile5_24));

            // Get 45 random expense
            List<FinancialPlanExpense> expenseList = new ArrayList<>();
            Random random = new Random();
            String[] pics = {"TuNM", "AnhPTH", "HuyHT", "VyNN"};
            char projectNameChar = 'A';
            char supplierNameChar = 'A';

            for (int i = 1; i <= 45; i++) {
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
                        .planExpenseKey(financialPlanFile1_2.getName() + "_V_" + i)
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
                    .file(financialPlanFile1_1)
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
            FinancialPlanFileExpense fileExpense16 = FinancialPlanFileExpense.builder()
                    .id(16L)
                    .file(financialPlanFile4_2)
                    .planExpense(expenseList.get(11))
                    .build();
            FinancialPlanFileExpense fileExpense17 = FinancialPlanFileExpense.builder()
                    .id(17L)
                    .file(financialPlanFile4_2)
                    .planExpense(expenseList.get(12))
                    .build();
            FinancialPlanFileExpense fileExpense18 = FinancialPlanFileExpense.builder()
                    .id(18L)
                    .file(financialPlanFile4_2)
                    .planExpense(expenseList.get(13))
                    .build();
            FinancialPlanFileExpense fileExpense19 = FinancialPlanFileExpense.builder()
                    .id(19L)
                    .file(financialPlanFile4_2)
                    .planExpense(expenseList.get(14))
                    .build();
            FinancialPlanFileExpense fileExpense20 = FinancialPlanFileExpense.builder()
                    .id(20L)
                    .file(financialPlanFile5_2)
                    .planExpense(expenseList.get(11))
                    .build();
            FinancialPlanFileExpense fileExpense21 = FinancialPlanFileExpense.builder()
                    .id(21L)
                    .file(financialPlanFile5_2)
                    .planExpense(expenseList.get(12))
                    .build();
            FinancialPlanFileExpense fileExpense22 = FinancialPlanFileExpense.builder()
                    .id(22L)
                    .file(financialPlanFile5_2)
                    .planExpense(expenseList.get(13))
                    .build();
            FinancialPlanFileExpense fileExpense23 = FinancialPlanFileExpense.builder()
                    .id(23L)
                    .file(financialPlanFile5_2)
                    .planExpense(expenseList.get(14))
                    .build();
            FinancialPlanFileExpense fileExpense24 = FinancialPlanFileExpense.builder()
                    .id(24L)
                    .file(financialPlanFile5_3)
                    .planExpense(expenseList.get(15))
                    .build();
            FinancialPlanFileExpense fileExpense25 = FinancialPlanFileExpense.builder()
                    .id(25L)
                    .file(financialPlanFile5_4)
                    .planExpense(expenseList.get(16))
                    .build();
            FinancialPlanFileExpense fileExpense26 = FinancialPlanFileExpense.builder()
                    .id(26L)
                    .file(financialPlanFile5_5)
                    .planExpense(expenseList.get(17))
                    .build();
            FinancialPlanFileExpense fileExpense27 = FinancialPlanFileExpense.builder()
                    .id(27L)
                    .file(financialPlanFile5_6)
                    .planExpense(expenseList.get(18))
                    .build();
            FinancialPlanFileExpense fileExpense28 = FinancialPlanFileExpense.builder()
                    .id(28L)
                    .file(financialPlanFile5_7)
                    .planExpense(expenseList.get(19))
                    .build();
            FinancialPlanFileExpense fileExpense29 = FinancialPlanFileExpense.builder()
                    .id(29L)
                    .file(financialPlanFile5_8)
                    .planExpense(expenseList.get(20))
                    .build();
            FinancialPlanFileExpense fileExpense30 = FinancialPlanFileExpense.builder()
                    .id(30L)
                    .file(financialPlanFile5_9)
                    .planExpense(expenseList.get(21))
                    .build();
            FinancialPlanFileExpense fileExpense31 = FinancialPlanFileExpense.builder()
                    .id(31L)
                    .file(financialPlanFile5_10)
                    .planExpense(expenseList.get(22))
                    .build();
            FinancialPlanFileExpense fileExpense32 = FinancialPlanFileExpense.builder()
                    .id(32L)
                    .file(financialPlanFile5_11)
                    .planExpense(expenseList.get(23))
                    .build();
            FinancialPlanFileExpense fileExpense33 = FinancialPlanFileExpense.builder()
                    .id(33L)
                    .file(financialPlanFile5_12)
                    .planExpense(expenseList.get(24))
                    .build();
            FinancialPlanFileExpense fileExpense34 = FinancialPlanFileExpense.builder()
                    .id(34L)
                    .file(financialPlanFile5_13)
                    .planExpense(expenseList.get(25))
                    .build();
            FinancialPlanFileExpense fileExpense35 = FinancialPlanFileExpense.builder()
                    .id(35L)
                    .file(financialPlanFile5_14)
                    .planExpense(expenseList.get(26))
                    .build();
            FinancialPlanFileExpense fileExpense36 = FinancialPlanFileExpense.builder()
                    .id(36L)
                    .file(financialPlanFile5_15)
                    .planExpense(expenseList.get(27))
                    .build();
            FinancialPlanFileExpense fileExpense37 = FinancialPlanFileExpense.builder()
                    .id(37L)
                    .file(financialPlanFile5_16)
                    .planExpense(expenseList.get(28))
                    .build();
            FinancialPlanFileExpense fileExpense38 = FinancialPlanFileExpense.builder()
                    .id(38L)
                    .file(financialPlanFile5_17)
                    .planExpense(expenseList.get(29))
                    .build();
            FinancialPlanFileExpense fileExpense39 = FinancialPlanFileExpense.builder()
                    .id(39L)
                    .file(financialPlanFile5_18)
                    .planExpense(expenseList.get(30))
                    .build();
            FinancialPlanFileExpense fileExpense40 = FinancialPlanFileExpense.builder()
                    .id(40L)
                    .file(financialPlanFile5_19)
                    .planExpense(expenseList.get(31))
                    .build();
            FinancialPlanFileExpense fileExpense41 = FinancialPlanFileExpense.builder()
                    .id(41L)
                    .file(financialPlanFile5_20)
                    .planExpense(expenseList.get(32))
                    .build();
            FinancialPlanFileExpense fileExpense42 = FinancialPlanFileExpense.builder()
                    .id(42L)
                    .file(financialPlanFile5_21)
                    .planExpense(expenseList.get(33))
                    .build();
            FinancialPlanFileExpense fileExpense43 = FinancialPlanFileExpense.builder()
                    .id(43L)
                    .file(financialPlanFile5_22)
                    .planExpense(expenseList.get(34))
                    .build();
            FinancialPlanFileExpense fileExpense44 = FinancialPlanFileExpense.builder()
                    .id(44L)
                    .file(financialPlanFile5_23)
                    .planExpense(expenseList.get(35))
                    .build();
            FinancialPlanFileExpense fileExpense45 = FinancialPlanFileExpense.builder()
                    .id(45L)
                    .file(financialPlanFile5_24)
                    .planExpense(expenseList.get(36))
                    .build();

            financialPlanFileExpenseRepository.saveAll(List.of(fileExpense1, fileExpense2, fileExpense3, fileExpense4, fileExpense5, fileExpense6, fileExpense7, fileExpense8, fileExpense9, fileExpense10, fileExpense11, fileExpense12, fileExpense13, fileExpense14, fileExpense15, fileExpense16, fileExpense17, fileExpense18, fileExpense19, fileExpense20, fileExpense21, fileExpense22, fileExpense23, fileExpense24, fileExpense25, fileExpense26, fileExpense27, fileExpense28, fileExpense29, fileExpense30, fileExpense31, fileExpense32, fileExpense33, fileExpense34, fileExpense35, fileExpense36, fileExpense37, fileExpense38, fileExpense39, fileExpense40, fileExpense41, fileExpense42, fileExpense43, fileExpense44, fileExpense45));

            FinancialReport report1 = FinancialReport.builder()
                    .name("Report Name 1")
                    .month(LocalDate.now())
                    .version("v3")
                    .status(planStatus1)
                    .department(accountingDepartment)
                    .term(term1)
                    .user(user1)
                    .build();

            FinancialReport report2 = FinancialReport.builder()
                    .name("Report Name 2")
                    .month(LocalDate.now())
                    .version("v2")
                    .status(planStatus2)
                    .department(accountingDepartment)
                    .term(term2)
                    .user(user2)
                    .build();

            FinancialReport report3 = FinancialReport.builder()
                    .name("Report Name 3")
                    .month(LocalDate.now())
                    .version("v3")
                    .status(planStatus1)
                    .department(itDepartment)
                    .term(term1)
                    .user(user1)
                    .build();

            FinancialReport report4 = FinancialReport.builder()
                    .name("Report Name 4")
                    .month(LocalDate.now())
                    .version("v3")
                    .status(planStatus4)
                    .department(accountingDepartment)
                    .term(term1)
                    .user(user3)
                    .build();

            FinancialReport report5 = FinancialReport.builder()
                    .name("Report Name 5")
                    .month(LocalDate.now())
                    .version("v1")
                    .status(planStatus2)
                    .department(accountingDepartment)
                    .term(term3)
                    .user(user4)
                    .build();

            FinancialReport report6 = FinancialReport.builder()
                    .name("Report Name 6")
                    .month(LocalDate.now())
                    .version("v3")
                    .status(planStatus2)
                    .department(itDepartment)
                    .term(term2)
                    .user(user1)
                    .build();

            financialReportRepository.saveAll(List.of(report1, report2, report3, report4, report5, report6));

            // Get 15 random expense
            List<FinancialReportExpense> reportExpenseList = new ArrayList<>();
            projectNameChar = 'A';
            supplierNameChar = 'A';

            for (int i = 1; i <= 15; i++) {
                int randomStatusIndex = random.nextInt(4) + 1;
                int randomCostTypeIndex = random.nextInt(6) + 1;
                int randomReportIndex = random.nextInt(6) + 1;
                int randomPicIndex = random.nextInt(pics.length);

                ExpenseStatus randomExpenseStatus = switch (randomStatusIndex) {
                    case 3 -> expenseStatus3;
                    case 4 -> expenseStatus4;
                    default -> expenseStatus3; // Default case, should never be reached
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

                FinancialReport randomReport = switch (randomReportIndex) {
                    case 1 -> report1;
                    case 2 -> report2;
                    case 3 -> report3;
                    case 4 -> report4;
                    case 5 -> report5;
                    case 6 -> report6;
                    default -> report1; // Default case, should never be reached
                };

                FinancialReportExpense expense = FinancialReportExpense.builder()
                        .name("Expense " + projectNameChar)
                        .unitPrice(BigDecimal.valueOf(random.nextInt(5000000) + 1000000))
                        .amount(random.nextInt(10) + 1)
                        .financialReport(randomReport)
                        .projectName("Project name " + projectNameChar++)
                        .supplierName("Supplier name " + supplierNameChar++)
                        .pic(pics[randomPicIndex])
                        .costType(randomCostType)
                        .status(randomExpenseStatus)
                        .build();

                reportExpenseList.add(expense);
            }

            financialReportExpenseRepository.saveAll(reportExpenseList);


            AnnualReport annualReport1 = AnnualReport.builder()
                    .year(2020)
                    .totalTerm(12)
                    .totalExpense(BigDecimal.valueOf(1232212125))
                    .totalDepartment(18)
                    .build();

            AnnualReport annualReport2 = AnnualReport.builder()
                    .year(2021)
                    .totalTerm(12)
                    .totalExpense(BigDecimal.valueOf(461321564))
                    .totalDepartment(11)
                    .build();

            AnnualReport annualReport3 = AnnualReport.builder()
                    .year(2022)
                    .totalTerm(22)
                    .totalExpense(BigDecimal.valueOf(1231313213))
                    .totalDepartment(8)
                    .build();

            AnnualReport annualReport4 = AnnualReport.builder()
                    .year(2023)
                    .totalTerm(12)
                    .totalExpense(BigDecimal.valueOf(905135545))
                    .totalDepartment(9)
                    .build();

//            AnnualReport annualReport5 = AnnualReport.builder()
//                    .year(2024)
//                    .totalTerm(9)
//                    .totalExpense(BigDecimal.valueOf(843513112))
//                    .totalDepartment(18)
//                    .build();

            AnnualReport annualReport6 = AnnualReport.builder()
                    .year(2025)
                    .totalTerm(6)
                    .totalExpense(BigDecimal.valueOf(354564895))
                    .totalDepartment(12)
                    .build();

            annualReportRepository.saveAll(List.of(annualReport1, annualReport2, annualReport3, annualReport4, annualReport6));

            List<Report> reports = new ArrayList<>();
            random = new Random();
            projectNameChar = 'A';
            supplierNameChar = 'A';

            for (int i = 1; i <= 15; i++) {
                int randomAnnualReportIndex = random.nextInt(6) + 1;
                int randomCostTypeIndex = random.nextInt(6) + 1;
                int randomDepartmentIndex = random.nextInt(3) + 1;

                AnnualReport randomAnnualReport = switch (randomAnnualReportIndex) {
                    case 1 -> annualReport1;
                    case 2 -> annualReport2;
                    case 3 -> annualReport3;
                    case 4 -> annualReport4;
                    case 6 -> annualReport6;
                    default -> annualReport1; // Default case, should never be reached
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

                Department randomDepartment = switch (randomDepartmentIndex) {
                    case 1 -> accountingDepartment;
                    case 2 -> financeDepartment;
                    case 3 -> itDepartment;
                    default -> accountingDepartment; // Default case, should never be reached
                };

                Report report = Report.builder()
                        .totalExpense(BigDecimal.valueOf(random.nextInt(5000000) + 2000000))
                        .biggestExpenditure(BigDecimal.valueOf(random.nextInt(1500000) + 100000))
                        .annualReport(randomAnnualReport)
                        .department(randomDepartment)
                        .costType(randomCostType)
                        .build();

                reports.add(report);
            }

            reportRepository.saveAll(reports);
        };
    }
}