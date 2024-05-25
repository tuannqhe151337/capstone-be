package com.example.capstone_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
@Entity
@Table(schema = "capstone_v2",name = "reports")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Report extends BaseEntity{
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



    @Column(name = "is_delete",columnDefinition = "bit default 0")
    private Boolean isDelete;
}
