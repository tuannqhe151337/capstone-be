package com.example.capstone_project.repository;

import com.example.capstone_project.entity.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingRepository extends JpaRepository<UserSetting, Long> {
    @Override
    <S extends UserSetting> S save(S entity);
}
