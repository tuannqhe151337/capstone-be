package com.example.capstone_project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(schema = "capstone_v2", name = "supplier")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class Supplier extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = FinancialPlanExpense_.SUPPLIER)
    private List<FinancialPlanExpense> expenses;

    @Column(name = "is_delete", columnDefinition = "bit default 0")
    private boolean isDelete;
}
