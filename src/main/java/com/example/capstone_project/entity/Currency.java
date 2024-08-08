package com.example.capstone_project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(schema = "capstone_v2", name = "currency")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class Currency extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_delete", columnDefinition = "bit default 0")
    private boolean isDelete;
}
