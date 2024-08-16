package com.example.capstone_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(schema = "capstone_v2",name = "monthly_report_summaries")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class MonthlyReportSummary extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_expense")
    private BigDecimal totalExpense;

    @Column(name = "biggest_expenditure")
    private BigDecimal biggestExpenditure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annual_report_id")
    private AnnualReport annualReport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cost_type_id")
    private CostType costType;

    @Column(name = "is_delete", columnDefinition = "bit default 0")
    private boolean isDelete;
}
