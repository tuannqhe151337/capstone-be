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
@Table(schema = "capstone_v2",name = "terms")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Term extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "duration")
    private String duration;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "plan_due_date")
    private LocalDate planDueDate;
    @Column(name = "report_due_date")
    private LocalDate reportDueDate;


    @Column(name = "is_delete",columnDefinition = "bit default 0")
    private Boolean isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private TermStatus status;

    @OneToMany(mappedBy = "term")
    private List<FinancialPlan> financialPlans;
    @OneToMany(mappedBy = "term")
    private List<FinancialReport> financialReports;
}
