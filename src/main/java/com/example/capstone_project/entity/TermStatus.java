package com.example.capstone_project.entity;

import com.example.capstone_project.utils.enums.TermCode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(schema = "capstone_v2",name = "term_status")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class TermStatus extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "icon_code")
    @Enumerated(EnumType.STRING)
    private TermCode code;

    @OneToMany(mappedBy = "status")
    private List<Term> terms;

    @Column(name = "is_delete",columnDefinition = "bit default 0")
    private boolean isDelete;
}
