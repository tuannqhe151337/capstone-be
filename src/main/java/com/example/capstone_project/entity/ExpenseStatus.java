package com.example.capstone_project.entity;

import com.example.capstone_project.utils.enums.ExpenseStatusCode;
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
    // https://vladmihalcea.com/postgresql-serial-column-hibernate-identity/
    // https://stackoverflow.com/questions/17780394/hibernate-identity-vs-sequence-entity-identifier-generators
    // GenerationType.IDENTITY will disable batch insert!!!
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name", columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column(name = "code")
    @Enumerated(EnumType.STRING)
    private ExpenseStatusCode code;

    @Column(name = "is_delete",columnDefinition = "bit default 0")
    private boolean isDelete;
}
