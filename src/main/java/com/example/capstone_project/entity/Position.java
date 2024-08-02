package com.example.capstone_project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(schema = "capstone_v2", name = "positions")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Position extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "position")
    private List<User> user;

    @Column(name = "is_delete", columnDefinition = "bit default 0")
    private boolean isDelete;
}
