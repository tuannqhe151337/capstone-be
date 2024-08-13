package com.example.capstone_project.repository;
import com.example.capstone_project.entity.User;
import com.example.capstone_project.repository.result.UserDownloadResult;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNullApi;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    Optional<User> findUserByUsername(String username);

    @Query(value = "select user from User user where user.username = :username and (user.isDelete = false or user.isDelete is null)")
    Optional<User> findActiveUserByUsername(String username);

    @Query(value = "select user from User user where user.email = :email and (user.isDelete = false or user.isDelete is null)")
    Optional<User> findActiveUserByEmail(String email);

    @Query(value = "select user from User user " +
            "join fetch user.role " +
            "join fetch user.department " +
            "join fetch user.position " +
            "join fetch user.userSetting " +
            "where user.username = :username and (user.isDelete = false or user.isDelete is null)")
    Optional<User> findUserDetailedByUsername(String username);

    Optional<User> findUserById(Long userId);

    @Query(value = "select user from User user " +
            "join fetch user.role " +
            "join fetch user.department " +
            "join fetch user.position " +
            "join fetch user.userSetting " +
            "where user.id = :userId")
    Optional<User> findUserDetailedById(Long userId);

    @Query(value = "select user from User user " +
            "join fetch user.role " +
            "join fetch user.department " +
            "where user.id = :userId and (user.isDelete = false or user.isDelete is null)"
    )
    Optional<User> findUserWithRoleAndDepartmentById(Long userId);

    @Query(value = "select count(distinct(user.id)) from User user " +
            "where user.username like %:query%")
    long countDistinct(String query);

    @Query(value = "select count(distinct(user.id)) from User user " +
            "where user.username like %:query% AND "+
            " (:roleId IS NULL OR user.role.id = :roleId) AND " +
            " (:departmentId IS NULL OR user.department.id = :departmentId) AND " +
            " (:positionId IS NULL OR user.position.id = :positionId)")
    long countDistinct(@Param("roleId") Long roleId,
                       @Param("departmentId") Long departmentId,
                       @Param("positionId") Long positionId,
                       @Param("query") String query);



    @Query(value = "select user from User user " +
            "join fetch user.role " +
            "join fetch user.department " +
            "join fetch user.position " +
            "join fetch user.userSetting " +
            "where user.id = :userId and (user.isDelete = false or user.isDelete is null)")
    Optional<User> findActiveUserDetailedById(Long userId);

    @Query(value = "select user from User user " +
            "join fetch user.role " +
            "join fetch user.department " +
            "where user.id = :userId and (user.isDelete = false or user.isDelete is null)"
    )
    Optional<User> findActiveUserWithRoleAndDepartmentById(Long userId);

    Optional<User> findUserByEmail(String email);

    @Query(value = "SELECT user.username FROM User user" +
            " WHERE TRIM(TRANSLATE(user.username, '0123456789', '          ')) = ?1" +
            " ORDER BY LENGTH(user.username) DESC, user.username DESC LIMIT 1")
    String getLatestSimilarUsername(String pattern);

    boolean existsByEmail(String email);

    @Query(" SELECT user.id AS userId, user.username AS userName FROM User user " +
            " WHERE user.username IN :listUsername AND " +
            " (user.isDelete = false OR user.isDelete is null )")
    List<UserDownloadResult> checkUsernameExist(List<String> listUsername);
}
