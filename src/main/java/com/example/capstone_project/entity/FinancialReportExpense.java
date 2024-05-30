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
@Table(schema = "capstone_v2",name = "financial_report_expenses")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FinancialReportExpense extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "supplier_name")
    private String supplierName;

    @Column(name = "pic")
    private String pic;

    @Column(name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financial_report_id")
    private FinancialReport financialReport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cost_type_id")
    private CostType costType;

    @Column(name = "is_delete", columnDefinition = "bit default 0")
    private Boolean isDelete;
}
