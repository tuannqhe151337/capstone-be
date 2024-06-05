package com.example.capstone_project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(schema = "capstone_v2",name = "financial_plan_file_expense")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class FinancialPlanFileExpense extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private FinancialPlanFile file;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id")
    private FinancialPlanExpense planExpense;

    @Column(name = "is_delete",columnDefinition = "bit default 0")
    private Boolean isDelete;
}
