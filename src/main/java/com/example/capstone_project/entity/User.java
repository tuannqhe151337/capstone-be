package com.example.capstone_project.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(schema = "capstone_v2", name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
public class User extends BaseEntity {
    // https://vladmihalcea.com/postgresql-serial-column-hibernate-identity/
    // https://stackoverflow.com/questions/17780394/hibernate-identity-vs-sequence-entity-identifier-generators
    // GenerationType.IDENTITY will disable batch insert!!!
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(name = "password", nullable = false)
    private String password;

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull(message = "Date of birth cannot be null")
    @Past(message = "Date of birth must be in the past")
    @Column(name = "dob")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING)
    private LocalDateTime dob;

    @Column(name = "note")
    private String note;

    @Size(min = 5, max = 100, message = "Full name must be less than 100 characters")
    @Column(name = "full_name", nullable = false, columnDefinition = "NVARCHAR(100)")
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ]+(?: [a-zA-ZÀ-ỹ]+)*$", message = "Full name must contain only letters and spaces")
    private String fullName;

    @Pattern(regexp = "\\d{10,15}", message = "Phone number must be between 10 and 15 digits")
    @Column(name = "phone_number")
    @NotEmpty(message = "Password cannot be empty")
    private String phoneNumber;

    @Size(max = 200, message = "Address must be less than 200 characters")
    @Column(name = "address")
    private String address;

    @NotNull(message = "Position cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    @NotNull(message = "Department cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @NotNull(message = "Role cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @Transient
    private List<Authority> authorities;

    @OneToOne(mappedBy = UserSetting_.USER, fetch = FetchType.LAZY)
    private UserSetting userSetting;

    @OneToMany(mappedBy = Term_.USER, fetch = FetchType.LAZY)
    private List<Term> terms;

    @OneToMany(mappedBy = FCMToken_.USER, fetch = FetchType.LAZY)
    private List<FCMToken> fcmTokens;

    @OneToMany(mappedBy = FinancialPlanFile_.USER, fetch = FetchType.LAZY)
    private List<FinancialPlanFile> financialPlanFiles;

    @OneToMany(mappedBy = FinancialPlanExpense_.PIC)
    private List<FinancialPlanExpense> expenses;

    @Column(name = "is_delete", columnDefinition = "bit default 0")
    private Boolean isDelete = false;
}
