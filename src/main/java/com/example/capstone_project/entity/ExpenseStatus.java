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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", columnDefinition = "NVARCHAR(255)")
    private String name;

    @Column(name = "code")
    @Enumerated(EnumType.STRING)
    private ExpenseStatusCode code;

    @Column(name = "is_delete",columnDefinition = "bit default 0")
    private boolean isDelete;
}
