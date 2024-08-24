package com.example.capstone_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
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

    @Column(name = "name", columnDefinition = "NVARCHAR(255)")
    private String name;

    @OneToMany(mappedBy = FinancialPlanFileExpense_.FILE, cascade = CascadeType.ALL)
    private List<FinancialPlanFileExpense> planFileExpenses;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "financial_plan_id")
    private FinancialPlan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User user;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "is_delete", columnDefinition = "bit default 0")
    private boolean isDelete;
}
