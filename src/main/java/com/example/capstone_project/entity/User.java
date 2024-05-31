package com.example.capstone_project.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(schema = "capstone_v2", name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "dob")
    private LocalDate dob;

    @Column(name = "note")
    private String note;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Transient
    private List<Authority> authorities;

    @OneToOne(mappedBy = UserSetting_.USER, fetch = FetchType.LAZY)
    private UserSetting userSetting;

    @OneToMany(mappedBy = Term_.USER)
    private List<Term> terms;

    @OneToMany(mappedBy = FinancialPlanFile_.USER)
    private List<FinancialPlanFile> financialPlanFiles;

    @Column(name = "is_delete",columnDefinition = "bit default 0")
    private boolean isDelete;
}
