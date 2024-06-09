package com.example.capstone_project.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(schema = "capstone_v2",name = "terms")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class Term extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name cannot be empty")
    @Column(name = "name")
    private String name;

    @NotNull(message = "Duration cannot be empty")
    @Column(name = "duration")
    @Enumerated(EnumType.STRING)
    private TermDuration duration ;

    @NotNull(message = "Start date cannot be null")
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @NotNull(message = "End date cannot be null")
    @Future(message = "End date must be in the future")
    @Column(name = "end_date")
    private LocalDateTime endDate;

    @NotNull(message = "Plan due date cannot be null")
    @Future(message = "Plan due date must be in the future")
    @Column(name = "plan_due_date")
    private LocalDateTime planDueDate;

    @NotNull(message = "User cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User user;

    @NotNull(message = "Status cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private TermStatus status; //trong day da co isDelete roi

    @OneToMany(mappedBy = "term")
    private List<FinancialPlan> financialPlans;

    @OneToMany(mappedBy = "term")
    private List<FinancialReport> financialReports;

    @Column(name = "is_delete", columnDefinition = "bit default 0")
    private boolean isDelete;

    @AssertTrue(message = "Plan due date must be before end date")
    private boolean isPlanDueDateBeforeEndDate() {
        if (planDueDate == null || endDate == null) {
            return true; // Để @NotNull xử lý null check
        }
        return planDueDate.isBefore(endDate);
    }
}
