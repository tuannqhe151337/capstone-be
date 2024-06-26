package com.example.capstone_project.entity;

import com.example.capstone_project.utils.enums.DepartmentCode;
import com.fasterxml.jackson.databind.ser.Serializers;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(schema = "capstone_v2",name = "departments")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class Department extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    @Enumerated(EnumType.STRING)
    private DepartmentCode code;

    @OneToMany(mappedBy = User_.DEPARTMENT)
    private List<User> users;

    @OneToMany(mappedBy = Report_.DEPARTMENT)
    private List<Report> reports;

    @OneToMany(mappedBy = FinancialPlan_.DEPARTMENT)
    private List<FinancialPlan> plans;

    @Column(name = "is_delete", columnDefinition = "bit default 0")
    private boolean isDelete;
}
