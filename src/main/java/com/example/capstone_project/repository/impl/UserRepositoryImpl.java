package com.example.capstone_project.repository.impl;

import com.example.capstone_project.entity.User;
import com.example.capstone_project.entity.User_;
import com.example.capstone_project.repository.CustomUserRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements CustomUserRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getUserWithPagination(String query, Pageable pageable) {
        // HQL query
        String hql = "select user from User user " +
                "left join user.role " +
                "left join user.department " +
                "left join user.position " +
                "where user.username like :query " +
                "and user.isDelete = false " + // We actually want to get all deactivated users as well
                "order by ";

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
                    hql += "user.role " + sortType;
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
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()) // We can't use pagable.getOffset() since they calculate offset by taking pageNumber * pageSize, we need (pageNumber - 1) * pageSize
                .setMaxResults(pageable.getPageSize())
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
    }
}
