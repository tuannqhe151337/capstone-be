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
            ReportStatusRepository reportStatusRepository,
            CostTypeRepository costTypeRepository,
            ExpenseStatusRepository expenseStatusRepository,
            FinancialPlanFileRepository financialPlanFileRepository,
            FinancialPlanFileExpenseRepository financialPlanFileExpenseRepository,
            FinancialPlanExpenseRepository financialPlanExpenseRepository,
            FinancialReportRepository financialReportRepository,
            AnnualReportRepository annualReportRepository,
            ReportRepository reportRepository,
            SupplierRepository supplierRepository,
            ProjectRepository projectRepository,
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
                    .id(2L)
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
                    .id(3L)
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
                    .id(4L)
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
                    .id(5L)
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

            roleAuthorityRepository.saveAll(List.of(adminAuthority1, adminAuthority2, adminAuthority3, adminAuthority4, adminAuthority5, adminAuthority6, adminAuthority7, adminAuthority8, adminAuthority9, adminAuthority10, adminAuthority14, adminAuthority15, adminAuthority16, adminAuthority17,
                    accountantAuthority1, accountantAuthority2, accountantAuthority3, accountantAuthority4, accountantAuthority5, accountantAuthority6, accountantAuthority7, accountantAuthority8, accountantAuthority9, accountantAuthority10, accountantAuthority11, accountantAuthority12, accountantAuthority13, accountantAuthority14, accountantAuthority15, accountantAuthority16, accountantAuthority17, accountantAuthority18, accountantAuthority19, accountantAuthority20, accountantAuthority21, accountantAuthority22, accountantAuthority23, accountantAuthority24, accountantAuthority25, accountantAuthority26, accountantAuthority27, accountantAuthority28, accountantAuthority29, accountantAuthority30, accountantAuthority31, accountantAuthority32, accountantAuthority33, accountantAuthority34, accountantAuthority35, accountantAuthority36,
                    financialStaffAuthority1, financialStaffAuthority2, financialStaffAuthority3, financialStaffAuthority4, financialStaffAuthority5, financialStaffAuthority6, financialStaffAuthority7, financialStaffAuthority8, financialStaffAuthority9, financialStaffAuthority10, financialStaffAuthority11, financialStaffAuthority12, financialStaffAuthority13, financialStaffAuthority14, financialStaffAuthority15, financialStaffAuthority16
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

            Term term1 = Term.builder()
                    .id(1L)
                    .name("Spring 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.now().minusDays(3))
                    .endDate(LocalDateTime.now().minusDays(3).plusDays(5))
                    .reuploadStartDate(LocalDateTime.now().minusDays(3).plusDays(20))
                    .reuploadEndDate(LocalDateTime.now().minusDays(3).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.now()))
                    .user(user1)
                    .status(termStatus2)
                    .build();

            Term term2 = Term.builder()
                    .id(2L)
                    .name("Summer 2024")
                    .duration(TermDuration.QUARTERLY)
                    .startDate(LocalDateTime.now().minusDays(15))
                    .endDate(LocalDateTime.now().minusDays(15).plusDays(5))
                    .reuploadStartDate(LocalDateTime.now().minusDays(15).plusDays(20))
                    .reuploadEndDate(LocalDateTime.now().minusDays(15).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.now()))
                    .user(user1)
                    .status(termStatus2)
                    .build();

            Term term3 = Term.builder()
                    .id(3L)
                    .name("Fall 2024")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusDays(5))
                    .reuploadStartDate(LocalDateTime.now().plusDays(20))
                    .reuploadEndDate(LocalDateTime.now().plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.now()))
                    .user(user2)
                    .status(termStatus3)
                    .build();

            Term term4 = Term.builder()
                    .id(4L)
                    .name("Winter 2024")
                    .duration(TermDuration.HALF_YEARLY)
                    .startDate(LocalDateTime.now().minusDays(2))
                    .endDate(LocalDateTime.now().minusDays(2).plusDays(5))
                    .reuploadStartDate(LocalDateTime.now().minusDays(2).plusDays(20))
                    .reuploadEndDate(LocalDateTime.now().minusDays(2).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.now()))
                    .user(user3)
                    .status(termStatus)
                    .build();

            Term term5 = Term.builder()
                    .id(5L)
                    .name("Spring 2025")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.now().minusDays(1))
                    .endDate(LocalDateTime.now().minusDays(1).plusDays(5))
                    .reuploadStartDate(LocalDateTime.now().minusDays(1).plusDays(20))
                    .reuploadEndDate(LocalDateTime.now().minusDays(1).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.now()))
                    .user(user4)
                    .status(termStatus2)
                    .build();

            Term term6 = Term.builder()
                    .id(6L)
                    .name("Summer 2025")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusDays(5))
                    .reuploadStartDate(LocalDateTime.now().plusDays(20))
                    .reuploadEndDate(LocalDateTime.now().plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.now()))
                    .user(user4)
                    .status(termStatus2)
                    .build();

            Term term7 = Term.builder()
                    .id(7L)
                    .name("Fall 2025")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusDays(5))
                    .reuploadStartDate(LocalDateTime.now().plusDays(20))
                    .reuploadEndDate(LocalDateTime.now().plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.now()))
                    .user(user4)
                    .status(termStatus2)
                    .build();

            Term term8 = Term.builder()
                    .id(8L)
                    .name("Winter 2025")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusDays(5))
                    .reuploadStartDate(LocalDateTime.now().plusDays(20))
                    .reuploadEndDate(LocalDateTime.now().plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.now()))
                    .user(user4)
                    .status(termStatus2)
                    .build();

            Term term9 = Term.builder()
                    .id(9L)
                    .name("Spring 2026")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusDays(5))
                    .reuploadStartDate(LocalDateTime.now().plusDays(20))
                    .reuploadEndDate(LocalDateTime.now().plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.now()))
                    .user(user4)
                    .status(termStatus2)
                    .build();

            Term term10 = Term.builder()
                    .id(10L)
                    .name("Summer 2026")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusDays(5))
                    .reuploadStartDate(LocalDateTime.now().plusDays(20))
                    .reuploadEndDate(LocalDateTime.now().plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.now()))
                    .user(user4)
                    .status(termStatus2)
                    .build();

            Term term11 = Term.builder()
                    .id(11L)
                    .name("Fall 2026")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusDays(5))
                    .reuploadStartDate(LocalDateTime.now().plusDays(20))
                    .reuploadEndDate(LocalDateTime.now().plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.now()))
                    .user(user4)
                    .status(termStatus2)
                    .build();

            Term term12 = Term.builder()
                    .id(12L)
                    .name("Winter 2026")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.now().minusDays(10))
                    .endDate(LocalDateTime.now().minusDays(10).plusDays(5))
                    .reuploadStartDate(LocalDateTime.now().minusDays(10).plusDays(20))
                    .reuploadEndDate(LocalDateTime.now().minusDays(10).plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.now()))
                    .user(user4)
                    .status(termStatus2)
                    .build();

            Term term13 = Term.builder()
                    .id(13L)
                    .name("Spring 2027")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusDays(5))
                    .reuploadStartDate(LocalDateTime.now().plusDays(20))
                    .reuploadEndDate(LocalDateTime.now().plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.now()))
                    .user(user4)
                    .status(termStatus2)
                    .build();

            Term term14 = Term.builder()
                    .id(14L)
                    .name("Summer 2027")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusDays(5))
                    .reuploadStartDate(LocalDateTime.now().plusDays(20))
                    .reuploadEndDate(LocalDateTime.now().plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.now()))
                    .user(user4)
                    .status(termStatus2)
                    .build();

            Term term15 = Term.builder()
                    .id(15L)
                    .name("Autumn 2027")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusDays(5))
                    .reuploadStartDate(LocalDateTime.now().plusDays(20))
                    .reuploadEndDate(LocalDateTime.now().plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.now()))
                    .user(user4)
                    .status(termStatus2)
                    .build();

            Term term16 = Term.builder()
                    .id(16L)
                    .name("Winter 2027")
                    .duration(TermDuration.MONTHLY)
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.now().plusDays(5))
                    .reuploadStartDate(LocalDateTime.now().plusDays(20))
                    .reuploadEndDate(LocalDateTime.now().plusDays(21))
                    .finalEndTermDate(TermDuration.MONTHLY.calculateEndDate(LocalDateTime.now()))
                    .user(user4)
                    .status(termStatus2)
                    .build();

            termRepository.saveAll(List.of(term1, term2, term3, term4, term5, term6, term7, term8, term9, term10, term11, term12, term13, term14, term15, term16));

            FinancialPlan financialPlan1 = FinancialPlan.builder()
                    .name("Financial Plan 1")
                    .term(term1)
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan2 = FinancialPlan.builder()
                    .name("Financial Plan 2")
                    .term(term2)
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan3 = FinancialPlan.builder()
                    .name("Financial Plan 3")
                    .term(term1)
                    .department(accountingDepartment)
                    .build();

            FinancialPlan financialPlan4 = FinancialPlan.builder()
                    .name("Financial Plan 4")
                    .department(financeDepartment)
                    .term(term1)
                    .build();

            FinancialPlan financialPlan5 = FinancialPlan.builder()
                    .name("Financial Plan 5")
                    .term(term2)
                    .department(accountingDepartment)
                    .build();

            FinancialPlan financialPlan6 = FinancialPlan.builder()
                    .name("Financial Plan 6")
                    .term(term3)
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan7 = FinancialPlan.builder()
                    .name("Financial Plan 7")
                    .term(term4)
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan8 = FinancialPlan.builder()
                    .name("Financial Plan 8")
                    .term(term5)
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan9 = FinancialPlan.builder()
                    .name("Financial Plan 9")
                    .term(term6)
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan10 = FinancialPlan.builder()
                    .name("Financial Plan 10")
                    .term(term7)
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan11 = FinancialPlan.builder()
                    .name("Financial Plan 11")
                    .term(term8)
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan12 = FinancialPlan.builder()
                    .name("Financial Plan 12")
                    .term(term9)
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan13 = FinancialPlan.builder()
                    .name("Financial Plan 13")
                    .term(term10)
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan14 = FinancialPlan.builder()
                    .name("Financial Plan 14")
                    .term(term11)
                    .department(itDepartment)
                    .build();

            FinancialPlan financialPlan15 = FinancialPlan.builder()
                    .name("Financial Plan 15")
                    .term(term12)
                    .department(itDepartment)
                    .build();

            planRepository.saveAll(List.of(financialPlan1, financialPlan2, financialPlan3, financialPlan4, financialPlan5, financialPlan6, financialPlan7, financialPlan8, financialPlan9, financialPlan10, financialPlan11, financialPlan12, financialPlan13, financialPlan14, financialPlan15));

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
                    .name("Project Name 1")
                    .build();

            Project project2 = Project.builder()
                    .id(2L)
                    .name("Project Name 2")
                    .build();

            Project project3 = Project.builder()
                    .id(3L)
                    .name("Project Name 3")
                    .build();

            Project project4 = Project.builder()
                    .id(4L)
                    .name("Project Name 4")
                    .build();

            Project project5 = Project.builder()
                    .id(5L)
                    .name("Project Name 5")
                    .build();

            Project project6 = Project.builder()
                    .id(6L)
                    .name("Project Name 6")
                    .build();

            projectRepository.saveAll(List.of(project1, project2, project3, project4, project5, project6));

            Supplier supplier1 = Supplier.builder()
                    .id(1L)
                    .name("Supplier name 1")
                    .build();

            Supplier supplier2 = Supplier.builder()
                    .id(2L)
                    .name("Supplier name 2")
                    .build();

            Supplier supplier3 = Supplier.builder()
                    .id(3L)
                    .name("Supplier name 3")
                    .build();

            Supplier supplier4 = Supplier.builder()
                    .id(4L)
                    .name("Supplier name 4")
                    .build();

            Supplier supplier5 = Supplier.builder()
                    .id(5L)
                    .name("Supplier name 5")
                    .build();

            Supplier supplier6 = Supplier.builder()
                    .id(6L)
                    .name("Supplier name 6")
                    .build();

            supplierRepository.saveAll(List.of(supplier1, supplier2, supplier3, supplier4, supplier5, supplier6));

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

            FinancialPlanFile financialPlanFile6_1 = FinancialPlanFile.builder()
                    .id(33L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan6)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile7_1 = FinancialPlanFile.builder()
                    .id(34L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan7)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile8_1 = FinancialPlanFile.builder()
                    .id(35L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan8)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile9_1 = FinancialPlanFile.builder()
                    .id(36L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan9)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile10_1 = FinancialPlanFile.builder()
                    .id(37L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan10)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile11_1 = FinancialPlanFile.builder()
                    .id(38L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan11)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile12_1 = FinancialPlanFile.builder()
                    .id(39L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan12)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile13_1 = FinancialPlanFile.builder()
                    .id(40L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan13)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile14_1 = FinancialPlanFile.builder()
                    .id(41L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan14)
                    .user(user3)
                    .build();

            FinancialPlanFile financialPlanFile15_1 = FinancialPlanFile.builder()
                    .id(42L)
                    .name("TERM-NAME3_PLAN-NAME5")
                    .plan(financialPlan15)
                    .user(user3)
                    .build();

            financialPlanFileRepository.saveAll(List.of(financialPlanFile1_1, financialPlanFile1_2, financialPlanFile2_1, financialPlanFile2_2, financialPlanFile3_1, financialPlanFile3_2, financialPlanFile4_1, financialPlanFile4_2, financialPlanFile5_1, financialPlanFile5_2, financialPlanFile5_3, financialPlanFile5_4, financialPlanFile5_5, financialPlanFile5_6, financialPlanFile5_7, financialPlanFile5_8, financialPlanFile5_9, financialPlanFile5_10, financialPlanFile5_11, financialPlanFile5_12, financialPlanFile5_13, financialPlanFile5_14, financialPlanFile5_15, financialPlanFile5_16, financialPlanFile5_17, financialPlanFile5_18, financialPlanFile5_19, financialPlanFile5_20, financialPlanFile5_21, financialPlanFile5_22, financialPlanFile5_23, financialPlanFile5_24, financialPlanFile6_1, financialPlanFile7_1, financialPlanFile8_1, financialPlanFile9_1, financialPlanFile10_1, financialPlanFile11_1, financialPlanFile12_1, financialPlanFile13_1, financialPlanFile14_1, financialPlanFile15_1));

            //

            // Get 64 random expense
            List<FinancialPlanExpense> expenseList = new ArrayList<>();
            Random random = new Random();
            char projectNameChar = 'A';
            char supplierNameChar = 'A';

            for (int i = 1; i <= 64; i++) {
                int randomStatusIndex = random.nextInt(4) + 1;
                int randomCostTypeIndex = random.nextInt(6) + 1;
                int randomProjectIndex = random.nextInt(6) + 1;
                int randomSupplierIndex = random.nextInt(6) + 1;
                int randomPicIndex = random.nextInt(5) + 1;

                ExpenseStatus randomExpenseStatus = switch (randomStatusIndex) {
                    case 1 -> expenseStatus1;
                    case 2 -> expenseStatus2;
                    case 3 -> expenseStatus3;
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
                    case 1 -> user1;
                    case 2 -> user2;
                    case 3 -> user3;
                    case 4 -> user4;
                    case 5 -> user5;
                    default -> user1; // Default case, should never be reached
                };

                FinancialPlanExpense expense = FinancialPlanExpense.builder()
                        .planExpenseKey(financialPlanFile1_2.getName() + "_V_" + i)
                        .name("Expense " + projectNameChar)
                        .unitPrice(BigDecimal.valueOf(random.nextInt(5000000) + 10000L))
                        .amount(random.nextInt(10) + 1)
                        .project(randomProject)
                        .supplier(randomSupplier)
                        .pic(randomPic)
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
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(11))
                    .build();
            FinancialPlanFileExpense fileExpense13 = FinancialPlanFileExpense.builder()
                    .id(13L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(12))
                    .build();
            FinancialPlanFileExpense fileExpense14 = FinancialPlanFileExpense.builder()
                    .id(14L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(13))
                    .build();
            FinancialPlanFileExpense fileExpense15 = FinancialPlanFileExpense.builder()
                    .id(15L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(14))
                    .build();
            FinancialPlanFileExpense fileExpense16 = FinancialPlanFileExpense.builder()
                    .id(16L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(15))
                    .build();
            FinancialPlanFileExpense fileExpense17 = FinancialPlanFileExpense.builder()
                    .id(17L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(16))
                    .build();
            FinancialPlanFileExpense fileExpense18 = FinancialPlanFileExpense.builder()
                    .id(18L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(17))
                    .build();
            FinancialPlanFileExpense fileExpense19 = FinancialPlanFileExpense.builder()
                    .id(19L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(18))
                    .build();
            FinancialPlanFileExpense fileExpense20 = FinancialPlanFileExpense.builder()
                    .id(20L)
                    .file(financialPlanFile2_2)
                    .planExpense(expenseList.get(19))
                    .build();
            FinancialPlanFileExpense fileExpense21 = FinancialPlanFileExpense.builder()
                    .id(21L)
                    .file(financialPlanFile3_2)
                    .planExpense(expenseList.get(20))
                    .build();
            FinancialPlanFileExpense fileExpense22 = FinancialPlanFileExpense.builder()
                    .id(22L)
                    .file(financialPlanFile3_2)
                    .planExpense(expenseList.get(21))
                    .build();
            FinancialPlanFileExpense fileExpense23 = FinancialPlanFileExpense.builder()
                    .id(23L)
                    .file(financialPlanFile3_2)
                    .planExpense(expenseList.get(22))
                    .build();
            FinancialPlanFileExpense fileExpense24 = FinancialPlanFileExpense.builder()
                    .id(24L)
                    .file(financialPlanFile3_2)
                    .planExpense(expenseList.get(23))
                    .build();
            FinancialPlanFileExpense fileExpense25 = FinancialPlanFileExpense.builder()
                    .id(25L)
                    .file(financialPlanFile4_2)
                    .planExpense(expenseList.get(24))
                    .build();
            FinancialPlanFileExpense fileExpense26 = FinancialPlanFileExpense.builder()
                    .id(26L)
                    .file(financialPlanFile4_2)
                    .planExpense(expenseList.get(25))
                    .build();
            FinancialPlanFileExpense fileExpense27 = FinancialPlanFileExpense.builder()
                    .id(27L)
                    .file(financialPlanFile4_2)
                    .planExpense(expenseList.get(26))
                    .build();
            FinancialPlanFileExpense fileExpense28 = FinancialPlanFileExpense.builder()
                    .id(28L)
                    .file(financialPlanFile4_2)
                    .planExpense(expenseList.get(27))
                    .build();
            FinancialPlanFileExpense fileExpense29 = FinancialPlanFileExpense.builder()
                    .id(29L)
                    .file(financialPlanFile5_2)
                    .planExpense(expenseList.get(28))
                    .build();
            FinancialPlanFileExpense fileExpense30 = FinancialPlanFileExpense.builder()
                    .id(30L)
                    .file(financialPlanFile5_2)
                    .planExpense(expenseList.get(29))
                    .build();
            FinancialPlanFileExpense fileExpense31 = FinancialPlanFileExpense.builder()
                    .id(31L)
                    .file(financialPlanFile5_2)
                    .planExpense(expenseList.get(30))
                    .build();
            FinancialPlanFileExpense fileExpense32 = FinancialPlanFileExpense.builder()
                    .id(32L)
                    .file(financialPlanFile5_2)
                    .planExpense(expenseList.get(31))
                    .build();
            FinancialPlanFileExpense fileExpense33 = FinancialPlanFileExpense.builder()
                    .id(33L)
                    .file(financialPlanFile5_3)
                    .planExpense(expenseList.get(32))
                    .build();
            FinancialPlanFileExpense fileExpense34 = FinancialPlanFileExpense.builder()
                    .id(34L)
                    .file(financialPlanFile5_4)
                    .planExpense(expenseList.get(33))
                    .build();
            FinancialPlanFileExpense fileExpense35 = FinancialPlanFileExpense.builder()
                    .id(35L)
                    .file(financialPlanFile5_5)
                    .planExpense(expenseList.get(34))
                    .build();
            FinancialPlanFileExpense fileExpense36 = FinancialPlanFileExpense.builder()
                    .id(36L)
                    .file(financialPlanFile5_6)
                    .planExpense(expenseList.get(35))
                    .build();
            FinancialPlanFileExpense fileExpense37 = FinancialPlanFileExpense.builder()
                    .id(37L)
                    .file(financialPlanFile5_7)
                    .planExpense(expenseList.get(36))
                    .build();
            FinancialPlanFileExpense fileExpense38 = FinancialPlanFileExpense.builder()
                    .id(38L)
                    .file(financialPlanFile5_8)
                    .planExpense(expenseList.get(37))
                    .build();
            FinancialPlanFileExpense fileExpense39 = FinancialPlanFileExpense.builder()
                    .id(39L)
                    .file(financialPlanFile5_9)
                    .planExpense(expenseList.get(38))
                    .build();
            FinancialPlanFileExpense fileExpense40 = FinancialPlanFileExpense.builder()
                    .id(40L)
                    .file(financialPlanFile5_10)
                    .planExpense(expenseList.get(39))
                    .build();
            FinancialPlanFileExpense fileExpense41 = FinancialPlanFileExpense.builder()
                    .id(41L)
                    .file(financialPlanFile5_11)
                    .planExpense(expenseList.get(40))
                    .build();
            FinancialPlanFileExpense fileExpense42 = FinancialPlanFileExpense.builder()
                    .id(42L)
                    .file(financialPlanFile5_12)
                    .planExpense(expenseList.get(41))
                    .build();
            FinancialPlanFileExpense fileExpense43 = FinancialPlanFileExpense.builder()
                    .id(43L)
                    .file(financialPlanFile5_13)
                    .planExpense(expenseList.get(42))
                    .build();
            FinancialPlanFileExpense fileExpense44 = FinancialPlanFileExpense.builder()
                    .id(44L)
                    .file(financialPlanFile5_14)
                    .planExpense(expenseList.get(43))
                    .build();
            FinancialPlanFileExpense fileExpense45 = FinancialPlanFileExpense.builder()
                    .id(45L)
                    .file(financialPlanFile5_15)
                    .planExpense(expenseList.get(44))
                    .build();
            FinancialPlanFileExpense fileExpense46 = FinancialPlanFileExpense.builder()
                    .id(46L)
                    .file(financialPlanFile5_16)
                    .planExpense(expenseList.get(45))
                    .build();
            FinancialPlanFileExpense fileExpense47 = FinancialPlanFileExpense.builder()
                    .id(47L)
                    .file(financialPlanFile5_17)
                    .planExpense(expenseList.get(46))
                    .build();
            FinancialPlanFileExpense fileExpense48 = FinancialPlanFileExpense.builder()
                    .id(48L)
                    .file(financialPlanFile5_18)
                    .planExpense(expenseList.get(47))
                    .build();
            FinancialPlanFileExpense fileExpense49 = FinancialPlanFileExpense.builder()
                    .id(49L)
                    .file(financialPlanFile5_19)
                    .planExpense(expenseList.get(48))
                    .build();
            FinancialPlanFileExpense fileExpense50 = FinancialPlanFileExpense.builder()
                    .id(50L)
                    .file(financialPlanFile5_20)
                    .planExpense(expenseList.get(49))
                    .build();
            FinancialPlanFileExpense fileExpense51 = FinancialPlanFileExpense.builder()
                    .id(51L)
                    .file(financialPlanFile5_21)
                    .planExpense(expenseList.get(50))
                    .build();
            FinancialPlanFileExpense fileExpense52 = FinancialPlanFileExpense.builder()
                    .id(52L)
                    .file(financialPlanFile5_22)
                    .planExpense(expenseList.get(51))
                    .build();
            FinancialPlanFileExpense fileExpense53 = FinancialPlanFileExpense.builder()
                    .id(53L)
                    .file(financialPlanFile5_23)
                    .planExpense(expenseList.get(52))
                    .build();
            FinancialPlanFileExpense fileExpense54 = FinancialPlanFileExpense.builder()
                    .id(54L)
                    .file(financialPlanFile5_24)
                    .planExpense(expenseList.get(53))
                    .build();
            FinancialPlanFileExpense fileExpense55 = FinancialPlanFileExpense.builder()
                    .id(55L)
                    .file(financialPlanFile6_1)
                    .planExpense(expenseList.get(54))
                    .build();
            FinancialPlanFileExpense fileExpense56 = FinancialPlanFileExpense.builder()
                    .id(56L)
                    .file(financialPlanFile7_1)
                    .planExpense(expenseList.get(55))
                    .build();
            FinancialPlanFileExpense fileExpense57 = FinancialPlanFileExpense.builder()
                    .id(57L)
                    .file(financialPlanFile8_1)
                    .planExpense(expenseList.get(56))
                    .build();
            FinancialPlanFileExpense fileExpense58 = FinancialPlanFileExpense.builder()
                    .id(58L)
                    .file(financialPlanFile9_1)
                    .planExpense(expenseList.get(57))
                    .build();
            FinancialPlanFileExpense fileExpense59 = FinancialPlanFileExpense.builder()
                    .id(59L)
                    .file(financialPlanFile10_1)
                    .planExpense(expenseList.get(58))
                    .build();
            FinancialPlanFileExpense fileExpense60 = FinancialPlanFileExpense.builder()
                    .id(60L)
                    .file(financialPlanFile11_1)
                    .planExpense(expenseList.get(59))
                    .build();
            FinancialPlanFileExpense fileExpense61 = FinancialPlanFileExpense.builder()
                    .id(61L)
                    .file(financialPlanFile12_1)
                    .planExpense(expenseList.get(60))
                    .build();
            FinancialPlanFileExpense fileExpense62 = FinancialPlanFileExpense.builder()
                    .id(62L)
                    .file(financialPlanFile13_1)
                    .planExpense(expenseList.get(61))
                    .build();
            FinancialPlanFileExpense fileExpense63 = FinancialPlanFileExpense.builder()
                    .id(63L)
                    .file(financialPlanFile14_1)
                    .planExpense(expenseList.get(62))
                    .build();
            FinancialPlanFileExpense fileExpense64 = FinancialPlanFileExpense.builder()
                    .id(64L)
                    .file(financialPlanFile15_1)
                    .planExpense(expenseList.get(63))
                    .build();

            financialPlanFileExpenseRepository.saveAll(List.of(fileExpense1, fileExpense2, fileExpense3, fileExpense4, fileExpense5, fileExpense6, fileExpense7, fileExpense8, fileExpense9, fileExpense10, fileExpense11, fileExpense12, fileExpense13, fileExpense14, fileExpense15, fileExpense16, fileExpense17, fileExpense18, fileExpense19, fileExpense20, fileExpense21, fileExpense22, fileExpense23, fileExpense24, fileExpense25, fileExpense26, fileExpense27, fileExpense28, fileExpense29, fileExpense30, fileExpense31, fileExpense32, fileExpense33, fileExpense34, fileExpense35, fileExpense36, fileExpense37, fileExpense38, fileExpense39, fileExpense40, fileExpense41, fileExpense42, fileExpense43, fileExpense44, fileExpense45, fileExpense46, fileExpense47, fileExpense48, fileExpense49, fileExpense50, fileExpense51, fileExpense52, fileExpense53, fileExpense54, fileExpense55, fileExpense56, fileExpense57, fileExpense58, fileExpense59, fileExpense60, fileExpense61, fileExpense62, fileExpense63, fileExpense64));

            FinancialReport report1 = FinancialReport.builder()
                    .name("Report Name 1")
                    .month(LocalDate.of(2024, 1, 5))
                    .expectedCost(BigDecimal.valueOf(601660487L))
                    .actualCost(BigDecimal.valueOf(216579382))
                    .status(newReportStatus)
                    .term(term1)
                    .build();

            FinancialReport report2 = FinancialReport.builder()
                    .name("Report Name 2")
                    .month(LocalDate.of(2024, 2, 5))
                    .expectedCost(BigDecimal.valueOf(901660487L))
                    .actualCost(BigDecimal.valueOf(516579382))
                    .status(waitingForApprovalReportStatus)
                    .term(term2)
                    .build();

            FinancialReport report3 = FinancialReport.builder()
                    .name("Report Name 3")
                    .month(LocalDate.of(2024, 3, 5))
                    .expectedCost(BigDecimal.valueOf(1001660487L))
                    .actualCost(BigDecimal.valueOf(706579382))
                    .status(waitingForApprovalReportStatus)
                    .term(term3)
                    .build();

            FinancialReport report4 = FinancialReport.builder()
                    .name("Report Name 4")
                    .expectedCost(BigDecimal.valueOf(901660487L))
                    .actualCost(BigDecimal.valueOf(616579382))
                    .month(LocalDate.of(2024, 4, 5))
                    .status(reviewedReportStatus)
                    .term(term4)
                    .build();

            FinancialReport report5 = FinancialReport.builder()
                    .name("Report Name 5")
                    .expectedCost(BigDecimal.valueOf(801660487L))
                    .actualCost(BigDecimal.valueOf(585579382))
                    .month(LocalDate.of(2024, 5, 5))
                    .status(reviewedReportStatus)
                    .term(term5)
                    .build();

            FinancialReport report6 = FinancialReport.builder()
                    .name("Report Name 6")
                    .expectedCost(BigDecimal.valueOf(801660487L))
                    .actualCost(BigDecimal.valueOf(516579382))
                    .month(LocalDate.of(2024, 6, 5))
                    .status(closedReportStatus)
                    .term(term6)
                    .build();

            FinancialReport report7 = FinancialReport.builder()
                    .name("Report Name 7")
                    .expectedCost(BigDecimal.valueOf(1001660487L))
                    .actualCost(BigDecimal.valueOf(616579382))
                    .month(LocalDate.of(2024, 7, 5))
                    .status(newReportStatus)
                    .term(term7)
                    .build();

            FinancialReport report8 = FinancialReport.builder()
                    .name("Report Name 8")
                    .expectedCost(BigDecimal.valueOf(701660487L))
                    .actualCost(BigDecimal.valueOf(616579382))
                    .month(LocalDate.of(2024, 8, 5))
                    .status(waitingForApprovalReportStatus)
                    .term(term8)
                    .build();

            FinancialReport report9 = FinancialReport.builder()
                    .name("Report Name 9")
                    .expectedCost(BigDecimal.valueOf(401660487L))
                    .actualCost(BigDecimal.valueOf(316579382))
                    .month(LocalDate.of(2024, 9, 5))
                    .status(waitingForApprovalReportStatus)
                    .term(term9)
                    .build();

            FinancialReport report10 = FinancialReport.builder()
                    .name("Report Name 10")
                    .expectedCost(BigDecimal.valueOf(901660487L))
                    .actualCost(BigDecimal.valueOf(616579382))
                    .month(LocalDate.of(2024, 10, 5))
                    .status(newReportStatus)
                    .term(term10)
                    .build();

            FinancialReport report11 = FinancialReport.builder()
                    .name("Report Name 11")
                    .expectedCost(BigDecimal.valueOf(131660487L))
                    .actualCost(BigDecimal.valueOf(1216579382))
                    .month(LocalDate.of(2024, 11, 5))
                    .status(closedReportStatus)
                    .term(term11)
                    .build();

            FinancialReport report12 = FinancialReport.builder()
                    .name("Report Name 12")
                    .expectedCost(BigDecimal.valueOf(151660487L))
                    .actualCost(BigDecimal.valueOf(1006579382))
                    .month(LocalDate.of(2024, 12, 5))
                    .status(waitingForApprovalReportStatus)
                    .term(term12)
                    .build();

            financialReportRepository.saveAll(List.of(report1, report2, report3, report4, report5, report6, report7, report8, report9, report10, report11, report12));

            // Get 15 random expense
//            List<FinancialReportExpense> reportExpenseList = new ArrayList<>();
//            projectNameChar = 'A';
//            supplierNameChar = 'A';
//
//            for (int i = 1; i <= 15; i++) {
//                int randomStatusIndex = random.nextInt(4) + 1;
//                int randomCostTypeIndex = random.nextInt(6) + 1;
//                int randomReportIndex = random.nextInt(6) + 1;
//                int randomPicIndex = random.nextInt(pics.length);
//
//                ExpenseStatus randomExpenseStatus = switch (randomStatusIndex) {
//                    case 3 -> expenseStatus3;
//                    case 4 -> expenseStatus4;
//                    default -> expenseStatus3; // Default case, should never be reached
//                };
//
//                CostType randomCostType = switch (randomCostTypeIndex) {
//                    case 1 -> costType1;
//                    case 2 -> costType2;
//                    case 3 -> costType3;
//                    case 4 -> costType4;
//                    case 5 -> costType5;
//                    case 6 -> costType6;
//                    default -> costType1; // Default case, should never be reached
//                };
//
//                FinancialReport randomReport = switch (randomReportIndex) {
//                    case 1 -> report1;
//                    case 2 -> report2;
//                    case 3 -> report3;
//                    case 4 -> report4;
//                    case 5 -> report5;
//                    case 6 -> report6;
//                    default -> report1; // Default case, should never be reached
//                };
//
//                FinancialReportExpense expense = FinancialReportExpense.builder()
//                        .name("Expense " + projectNameChar)
//                        .unitPrice(BigDecimal.valueOf(random.nextInt(5000000) + 1000000))
//                        .amount(random.nextInt(10) + 1)
//                        .financialReport(randomReport)
//                        .projectName("Project name " + projectNameChar++)
//                        .supplierName("Supplier name " + supplierNameChar++)
//                        .pic(pics[randomPicIndex])
//                        .costType(randomCostType)
//                        .status(randomExpenseStatus)
//                        .build();
//
//                reportExpenseList.add(expense);
//            }
//
//            financialReportExpenseRepository.saveAll(reportExpenseList);


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

            Currency currency1 = Currency.builder()
                    .id(1L)
                    .name("VND")
                    .build();

            Currency currency2 = Currency.builder()
                    .id(2L)
                    .name("USD")
                    .build();

            Currency currency3 = Currency.builder()
                    .id(3L)
                    .name("JPY")
                    .build();

            Currency currency4 = Currency.builder()
                    .id(4L)
                    .name("KRW")
                    .build();

            currencyRepository.saveAll(List.of(currency1, currency2, currency3, currency4));

            CurrencyExchangeRate exchangeRate1_1 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 4, 17))
                    .currency(currency1)
                    .amount(BigDecimal.valueOf(20000))
                    .build();

            CurrencyExchangeRate exchangeRate1_2 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 4, 10))
                    .currency(currency2)
                    .amount(BigDecimal.valueOf(1))
                    .build();

            CurrencyExchangeRate exchangeRate1_3 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 4, 11))
                    .currency(currency3)
                    .amount(BigDecimal.valueOf(147))
                    .build();

            CurrencyExchangeRate exchangeRate1_4 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 4, 18))
                    .currency(currency4)
                    .amount(BigDecimal.valueOf(1374))
                    .build();

            CurrencyExchangeRate exchangeRate2_1 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 5, 17))
                    .currency(currency1)
                    .amount(BigDecimal.valueOf(22000))
                    .build();

            CurrencyExchangeRate exchangeRate2_2 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 5, 18))
                    .currency(currency2)
                    .amount(BigDecimal.valueOf(1))
                    .build();

            CurrencyExchangeRate exchangeRate2_3 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 5, 20))
                    .currency(currency3)
                    .amount(BigDecimal.valueOf(153))
                    .build();

            CurrencyExchangeRate exchangeRate2_4 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 5, 25))
                    .currency(currency4)
                    .amount(BigDecimal.valueOf(1542))
                    .build();

            CurrencyExchangeRate exchangeRate3_1 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 6, 17))
                    .currency(currency1)
                    .amount(BigDecimal.valueOf(22300))
                    .build();

            CurrencyExchangeRate exchangeRate3_2 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 6, 18))
                    .currency(currency2)
                    .amount(BigDecimal.valueOf(1))
                    .build();

            CurrencyExchangeRate exchangeRate3_3 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 6, 20))
                    .currency(currency3)
                    .amount(BigDecimal.valueOf(165))
                    .build();

            CurrencyExchangeRate exchangeRate3_4 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 6, 25))
                    .currency(currency4)
                    .amount(BigDecimal.valueOf(1475))
                    .build();


            CurrencyExchangeRate exchangeRate4_1 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 7, 17))
                    .currency(currency1)
                    .amount(BigDecimal.valueOf(25500))
                    .build();

            CurrencyExchangeRate exchangeRate4_2 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 7, 18))
                    .currency(currency2)
                    .amount(BigDecimal.valueOf(1))
                    .build();

            CurrencyExchangeRate exchangeRate4_3 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 7, 20))
                    .currency(currency3)
                    .amount(BigDecimal.valueOf(133))
                    .build();

            CurrencyExchangeRate exchangeRate4_4 = CurrencyExchangeRate.builder()
                    .month(LocalDate.of(2020, 7, 25))
                    .currency(currency4)
                    .amount(BigDecimal.valueOf(1550))
                    .build();

            currencyExchangeRateRepository.saveAll(List.of(exchangeRate1_1, exchangeRate1_2, exchangeRate1_3, exchangeRate1_4, exchangeRate2_1, exchangeRate2_2, exchangeRate2_3, exchangeRate2_4, exchangeRate3_1, exchangeRate3_2, exchangeRate3_3, exchangeRate3_4, exchangeRate4_1, exchangeRate4_2, exchangeRate4_3, exchangeRate4_4));
        };
    }
}