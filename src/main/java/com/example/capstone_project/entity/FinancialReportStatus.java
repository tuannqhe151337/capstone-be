package com.example.capstone_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Table(schema = "capstone_v2",name = "financial_report_status")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class FinancialReportStatus extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = FinancialReportExpense_.STATUS)
    private List<FinancialReportExpense> reportExpenses;

    @Column(name = "is_delete",columnDefinition = "bit default 0")
    private Boolean isDelete;
}
