package com.example.capstone_project.entity;

import com.example.capstone_project.utils.enums.PlanStatusCode;
import com.example.capstone_project.utils.enums.ReportStatusCode;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(schema = "capstone_v2",name = "report_status")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class ReportStatus extends BaseEntity {
    // https://vladmihalcea.com/postgresql-serial-column-hibernate-identity/
    // https://stackoverflow.com/questions/17780394/hibernate-identity-vs-sequence-entity-identifier-generators
    // GenerationType.IDENTITY will disable batch insert!!!
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name", columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column(name = "code", unique = true)
    @Enumerated(EnumType.STRING)
    private ReportStatusCode code;

    @OneToMany(mappedBy = FinancialReport_.STATUS)
    private List<FinancialReport> financialPlans;

    @Column(name = "is_delete",columnDefinition = "bit default 0")
    private boolean isDelete;
}
