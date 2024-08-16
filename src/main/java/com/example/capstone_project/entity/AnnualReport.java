package com.example.capstone_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(schema = "capstone_v2",name = "annual_reports")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class AnnualReport extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year", unique = true)
    private Integer year;

    @Column(name = "total_term")
    private Integer totalTerm;

    @Column(name = "total_expense")
    private BigDecimal totalExpense;

    @Column(name = "total_department")
    private Integer totalDepartment;

    @Column(name = "is_delete",columnDefinition = "bit default 0")
    private boolean isDelete;

    @OneToMany(mappedBy = MonthlyReportSummary_.ANNUAL_REPORT)
    private List<MonthlyReportSummary> monthlyReportSummaries;
}