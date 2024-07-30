package com.example.capstone_project.repository.impl;

import com.example.capstone_project.entity.User;
import com.example.capstone_project.entity.User_;
import com.example.capstone_project.repository.CustomUserRepository;
import com.example.capstone_project.repository.result.UpdateUserDataOption;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements CustomUserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getUserWithPagination(Long roleId, Long departmentId, Long positionId, String query, Pageable pageable) {
        // HQL query
        String hql = "select user from User user " +
                " join user.role " +
                " join user.department " +
                " join user.position " +
                "where user.username like :query AND" +
                " (:roleId IS NULL OR user.role.id = :roleId) AND " +
                " (:departmentId IS NULL OR user.department.id = :departmentId) AND " +
                " (:positionId IS NULL OR user.position.id = :positionId) " +
//                "and user.isDelete = false " + // We actually want to get all deactivated users as well
                "ORDER BY ";


        // Handling sort by and sort type
        List<Sort.Order> sortOrderList = pageable.getSort().get().toList();
        for (int i = 0; i < sortOrderList.size(); i++) {
            Sort.Order order = sortOrderList.get(i);

            String sortType = order.getDirection().isAscending() ? "asc" : "desc";
            switch (order.getProperty().toLowerCase()) {
                case "name", "username", "user_name":
                    hql += "user.username " + sortType;
                    break;
                case "role":
                    hql += "user.role.id " + sortType;
                    break;
                case "department":
                    hql += "user.department.id " + sortType;
                    break;
                case "position":
                    hql += "user.position.id " + sortType;
                    break;
                default:
                    hql += "user.id " + sortType;
            }

            if (i != sortOrderList.size() - 1) {
                hql += ", ";
            } else {
                hql += " ";
            }
        }

        // Handling join
        EntityGraph<User> entityGraph = entityManager.createEntityGraph(User.class);
        entityGraph.addAttributeNodes(User_.ROLE);
        entityGraph.addAttributeNodes(User_.DEPARTMENT);
        entityGraph.addAttributeNodes(User_.POSITION);

        // Run query
        return entityManager.createQuery(hql, User.class)
                .setParameter("query", "%" + query + "%")
                .setParameter("roleId", roleId)
                .setParameter("departmentId", departmentId)
                .setParameter("positionId", positionId)
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()) // We can't use pagable.getOffset() since they calculate offset by taking pageNumber * pageSize, we need (pageNumber - 1) * pageSize
                .setMaxResults(pageable.getPageSize())
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
    }

    // Check out: https://stackoverflow.com/a/31186374 and https://stackoverflow.com/a/41203093
    // It seems there's no easy way to dynamically update whatever fields we want without using criteria builder
    @Override
    public void saveUserData(User user, UpdateUserDataOption option) {
        // Administration stuff: create criteria and root
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaUpdate<User> criteriaUpdate = builder.createCriteriaUpdate(User.class);
        Root<User> userRoot = criteriaUpdate.from(User.class);

        // Specify which field to update
        if (!option.isIgnoreUsername()) {
            criteriaUpdate.set(userRoot.get(User_.USERNAME), user.getUsername());
        }

        if (!option.isIgnoreEmail()) {
            criteriaUpdate.set(userRoot.get(User_.EMAIL), user.getEmail());
        }

        criteriaUpdate.set(userRoot.get(User_.DOB), user.getDob());
        criteriaUpdate.set(userRoot.get(User_.NOTE), user.getNote());

        if (!option.isIgnoreFullName()) {
            criteriaUpdate.set(userRoot.get(User_.FULL_NAME), user.getFullName());
        }

        criteriaUpdate.set(userRoot.get(User_.PHONE_NUMBER), user.getPhoneNumber());
        criteriaUpdate.set(userRoot.get(User_.ADDRESS), user.getAddress());

        if (!option.isIgnorePosition()) {
            criteriaUpdate.set(userRoot.get(User_.POSITION), user.getPosition());
        }

        if (!option.isIgnoreDepartment()) {
            criteriaUpdate.set(userRoot.get(User_.DEPARTMENT), user.getDepartment());
        }

        if (!option.isIgnoreRole()) {
            criteriaUpdate.set(userRoot.get(User_.ROLE), user.getRole());
        }

        // Where
        criteriaUpdate.where(builder.equal(userRoot.get(User_.ID), user.getId()));

        // Update
        Query query = entityManager.createQuery(criteriaUpdate);
        query.executeUpdate();
    }
}
