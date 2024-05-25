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
@Table(schema = "capstone_v2",name = "financial_plan_files")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FinancialPlanFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "version")
    private String version;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDate createdAt;

    @Column(name = "is_delete",columnDefinition = "bit default 0")
    private Boolean isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private FinancialStatus status;

    @OneToMany(mappedBy = "financialPlanFile")
    private List<FinancialPlanExpense> financialPlanExpenses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financial_plan_id")
    private FinancialPlan financialPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User user;
}
