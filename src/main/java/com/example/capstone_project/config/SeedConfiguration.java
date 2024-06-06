package com.example.capstone_project.config;

import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

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
            PlanStatusRepository planStatusRepository
    ) {
        return args -> {
            // Department
            Department softwareDevelopmentDepartment = Department.builder()
                    .name("Software development")
                    .build();

            Department accountingDepartment = Department.builder()
                    .name("Accounting department")
                    .build();

            Department financeDepartment = Department.builder()
                    .name("Finance department")
                    .build();

            departmentRepository.saveAll(List.of(softwareDevelopmentDepartment, accountingDepartment, financeDepartment));

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
                    .code("A-001")
                    .name("Create new user")
                    .build();

            Authority viewListUsers = Authority.builder()
                    .code("A-002")
                    .name("View list users")
                    .build();

            Authority deleteUser = Authority.builder()
                    .code("A-003")
                    .name("Delete user")
                    .build();

            Authority editUser = Authority.builder()
                    .code("A-004")
                    .name("Edit user")
                    .build();

            Authority activateUser = Authority.builder()
                    .code("A-005")
                    .name("Activate user")
                    .build();

            Authority deactivateUser = Authority.builder()
                    .code("A-006")
                    .name("Deactivate user")
                    .build();

            Authority createTerm = Authority.builder()
                    .code("B-001")
                    .name("Create term")
                    .build();

            Authority editTerm = Authority.builder()
                    .code("B-002")
                    .name("Edit term")
                    .build();

            Authority viewTerm = Authority.builder()
                    .code("B-003")
                    .name("View term")
                    .build();

            Authority startTerm = Authority.builder()
                    .code("B-004")
                    .name("Start term")
                    .build();

            Authority deleteTerm = Authority.builder()
                    .code("B-005")
                    .name("Delete term")
                    .build();

            Authority importPlan = Authority.builder()
                    .code("C-001")
                    .name("Import plan")
                    .build();

            Authority reUploadPlan = Authority.builder()
                    .code("C-002")
                    .name("Reupload plan")
                    .build();

            Authority submitPlanForReview = Authority.builder()
                    .code("C-003")
                    .name("Submit plan for review")
                    .build();

            Authority deletePlan = Authority.builder()
                    .code("C-004")
                    .name("Delete plan")
                    .build();

            Authority downloadPlan = Authority.builder()
                    .code("C-005")
                    .name("Download plan")
                    .build();

            Authority approvePlan = Authority.builder()
                    .code("C-006")
                    .name("Approve plan")
                    .build();

            Authority viewReport = Authority.builder() // Monthly, Quarterly, Half-year
                    .code("D-001")
                    .name("View report")
                    .build();

            Authority downloadReport = Authority.builder() // Monthly, Quarterly, Half-year
                    .code("D-002")
                    .name("Download report")
                    .build();

            Authority viewAnnualReport = Authority.builder()
                    .code("E-001")
                    .name("View annual report")
                    .build();

            Authority downloadAnnualReport = Authority.builder()
                    .code("E-002")
                    .name("Download annual report")
                    .build();

            authorityRepository.saveAll(List.of(createUser, viewListUsers, deleteUser, editUser, activateUser, deactivateUser, createTerm, editTerm, viewTerm, startTerm, deleteTerm, importPlan, reUploadPlan, submitPlanForReview, deletePlan, downloadPlan, approvePlan, viewReport, downloadReport, viewAnnualReport, downloadAnnualReport));

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
                    .build();

            User user2 = User.builder()
                    .username("username2")
                    .password(this.passwordEncoder.encode("password"))
                    .role(admin)
                    .department(financeDepartment)
                    .position(techlead)
                    .build();

            User user3 = User.builder()
                    .username("username3")
                    .password(this.passwordEncoder.encode("password"))
                    .role(accountant)
                    .department(financeDepartment)
                    .position(juniorDev)
                    .build();

            User user4 = User.builder()
                    .username("username4")
                    .password(this.passwordEncoder.encode("password"))
                    .role(accountant)
                    .department(softwareDevelopmentDepartment)
                    .position(juniorDev)
                    .build();

            User user5 = User.builder()
                    .username("username5")
                    .password(this.passwordEncoder.encode("password"))
                    .role(financialStaff)
                    .department(softwareDevelopmentDepartment)
                    .position(staff)
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

            roleAuthorityRepository.saveAll(List.of(adminAuthority1, adminAuthority2, adminAuthority3, adminAuthority4, adminAuthority5, adminAuthority6,
                    accountantAuthority1, accountantAuthority2, accountantAuthority3, accountantAuthority4, accountantAuthority5, accountantAuthority6, accountantAuthority7, accountantAuthority8, accountantAuthority9, accountantAuthority10, accountantAuthority11, accountantAuthority12, accountantAuthority13, accountantAuthority14, accountantAuthority15,
                    financialStaffAuthority1, financialStaffAuthority2, financialStaffAuthority3, financialStaffAuthority4, financialStaffAuthority5, financialStaffAuthority6, financialStaffAuthority7, financialStaffAuthority8, financialStaffAuthority9, financialStaffAuthority10, financialStaffAuthority11, financialStaffAuthority12, financialStaffAuthority13, financialStaffAuthority14
            ));

            TermStatus status1 = TermStatus.builder()
                    .id(1L)
                    .name("New")
                    .iconCode("icon_new")
                    .build();

            TermStatus status2 = TermStatus.builder()
                    .id(2L)
                    .name("In-Progress")
                    .iconCode("icon_in_progress")
                    .build();

            TermStatus status3 = TermStatus.builder()
                    .id(3L)
                    .name("Close")
                    .iconCode("icon_close")
                    .build();

            termStatusRepository.saveAll(List.of(status1,status2,status3));

            PlanStatus planStatus1 = PlanStatus.builder()
                    .id(1L)
                    .name("New")
                    .build();

            PlanStatus planStatus2 = PlanStatus.builder()
                    .id(2L)
                    .name("Waiting for reviewed")
                    .build();

            PlanStatus planStatus3 = PlanStatus.builder()
                    .id(3L)
                    .name("Reviewed")
                    .build();

            PlanStatus planStatus4 = PlanStatus.builder()
                    .id(4L)
                    .name("Denied")
                    .build();

            planStatusRepository.saveAll(List.of(planStatus1,planStatus2,planStatus3,planStatus4));

            Term term1 = Term.builder()
                    .id(1L)
                    .name("Spring 2024")
                    .duration("Month")
                    .startDate(LocalDateTime.of(2024, 1, 1, 0, 0))
                    .endDate(LocalDateTime.of(2024, 1, 31, 23, 59))
                    .planDueDate(LocalDateTime.of(2024, 1, 10, 17, 0))
                    .reportDueDate(LocalDateTime.of(2024, 1, 20, 17, 0))
                    .user(user1)
                    .status(status1)
                    .build();

            Term term2 = Term.builder()
                    .id(2L)
                    .name("Summer 2024")
                    .duration("Month")
                    .startDate(LocalDateTime.of(2024, 6, 1, 0, 0))
                    .endDate(LocalDateTime.of(2024, 6, 30, 23, 59))
                    .planDueDate(LocalDateTime.of(2024, 6, 10, 17, 0))
                    .reportDueDate(LocalDateTime.of(2024, 6, 20, 17, 0))
                    .user(user1)
                    .status(status2)
                    .build();

            Term term3 = Term.builder()
                    .id(3L)
                    .name("Fall 2024")
                    .duration("Month")
                    .startDate(LocalDateTime.of(2024, 9, 1, 0, 0))
                    .endDate(LocalDateTime.of(2024, 9, 30, 23, 59))
                    .planDueDate(LocalDateTime.of(2024, 9, 10, 17, 0))
                    .reportDueDate(LocalDateTime.of(2024, 9, 20, 17, 0))
                    .user(user2)
                    .status(status3)
                    .build();

            Term term4 = Term.builder()
                    .id(4L)
                    .name("Winter 2024")
                    .duration("Month")
                    .startDate(LocalDateTime.of(2024, 12, 1, 0, 0))
                    .endDate(LocalDateTime.of(2024, 12, 31, 23, 59))
                    .planDueDate(LocalDateTime.of(2024, 12, 10, 17, 0))
                    .reportDueDate(LocalDateTime.of(2024, 12, 20, 17, 0))
                    .user(user3)
                    .status(status1)
                    .build();

            Term term5 = Term.builder()
                    .id(5L)
                    .name("Spring 2025")
                    .duration("Month")
                    .startDate(LocalDateTime.of(2025, 1, 1, 0, 0))
                    .endDate(LocalDateTime.of(2025, 1, 31, 23, 59))
                    .planDueDate(LocalDateTime.of(2025, 1, 10, 17, 0))
                    .reportDueDate(LocalDateTime.of(2025, 1, 20, 17, 0))
                    .user(user4)
                    .status(status2)
                    .build();

            termRepository.saveAll(List.of(term1,term2,term3,term4,term5));

            FinancialPlan financialPlan1 = FinancialPlan.builder()
                    .id(1L)
                    .name("Financial Plan 1")
                    .term(term1)
                    .status(planStatus1)
                    .build();

            FinancialPlan financialPlan2 = FinancialPlan.builder()
                    .id(2L)
                    .name("Financial Plan 2")
                    .term(term1)
                    .status(planStatus2)
                    .build();

            FinancialPlan financialPlan3 = FinancialPlan.builder()
                    .id(3L)
                    .name("Financial Plan 3")
                    .term(term1)
                    .status(planStatus3)
                    .build();

            FinancialPlan financialPlan4 = FinancialPlan.builder()
                    .id(4L)
                    .name("Financial Plan 4")
                    .term(term1)
                    .status(planStatus4)
                    .build();

            FinancialPlan financialPlan5 = FinancialPlan.builder()
                    .id(5L)
                    .name("Financial Plan 5")
                    .term(term2)
                    .status(planStatus1)
                    .build();

            planRepository.saveAll(List.of(financialPlan1,financialPlan2,financialPlan3,financialPlan4,financialPlan5));
        };
    }
}
