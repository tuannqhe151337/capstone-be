package com.example.capstone_project.entity;

import com.fasterxml.jackson.databind.ser.Serializers;
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
@Table(schema = "capstone_v2",name = "cost_types")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CostType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = Report_.COST_TYPE)
    private List<Report> reports;

    @OneToMany(mappedBy = FinancialReportExpense_.COST_TYPE)
    private List<FinancialReportExpense> financialReportExpenses;

    @OneToMany(mappedBy = FinancialPlanExpense_.COST_TYPE)
    private List<FinancialPlanExpense> financialPlanExpenses;

    @Column(name = "is_delete",columnDefinition = "bit default 0")
    private Boolean isDelete;

}
