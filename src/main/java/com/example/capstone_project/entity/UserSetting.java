package com.example.capstone_project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(schema = "capstone_v2", name = "user_setting")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "language")
    private String language; // vi, en

    @Column(name = "theme")
    private String theme;

    @Column(name = "dark_mode")
    private boolean darkMode;

    @OneToOne()
    @JoinColumn(name = "user_id")
    private User user;
}
