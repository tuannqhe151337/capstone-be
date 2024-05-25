package com.example.capstone_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(schema = "capstone_v2",name = "financial_reports")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FinancialReport extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "month")
    private LocalDate month;



    @Column(name = "is_delete", columnDefinition = "bit default 0")
    private Boolean isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id")
    private Term term;

    @OneToMany(mappedBy = FinancialReportExpense_.FINANCIAL_REPORT)
    private List<FinancialReportExpense> financialReportExpenses;
}
