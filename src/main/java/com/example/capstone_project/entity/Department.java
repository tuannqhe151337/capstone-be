package com.example.capstone_project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(schema = "capstone_v2",name = "departments")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class Department extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "NVARCHAR(255)")
    private String name;

    @OneToMany(mappedBy = User_.DEPARTMENT)
    private List<User> users;

    @OneToMany(mappedBy = MonthlyReportSummary_.DEPARTMENT)
    private List<MonthlyReportSummary> monthlyReportSummaries;

    @OneToMany(mappedBy = FinancialPlan_.DEPARTMENT)
    private List<FinancialPlan> plans;

    @Column(name = "is_delete", columnDefinition = "bit default 0")
    private boolean isDelete;
}
