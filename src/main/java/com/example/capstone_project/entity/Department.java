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
@Table(schema = "capstone_v2",name = "departments")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Department extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = User_.DEPARTMENT)
    private List<User> users;

    @OneToMany(mappedBy = Report_.DEPARTMENT)
    private List<Report> reports;


    @Column(name = "is_delete", columnDefinition = "bit default 0")
    private Boolean isDelete;
}
