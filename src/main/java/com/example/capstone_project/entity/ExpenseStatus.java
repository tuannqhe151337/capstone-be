package com.example.capstone_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
@Entity
@Table(schema = "capstone_v2",name = "expense_status")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class ExpenseStatus extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = FinancialPlanExpense_.STATUS)
    private List<FinancialPlanExpense> planExpenses;

    @Column(name = "is_delete",columnDefinition = "bit default 0")
    private Boolean isDelete;
}
