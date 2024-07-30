package com.example.capstone_project.repository.impl;

import com.example.capstone_project.entity.Department;

import com.example.capstone_project.repository.CustomDepartmentRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DepartmentRepositoryImpl implements CustomDepartmentRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Department> getDepartmentWithPagination(String query, Pageable pageable) {
        // HQL query
        String hql = "select department from Department department " +
                "where department.name like :query " +
                "and department.isDelete = false " +
                "order by ";

        // Handling sort by and sort type
        List<Sort.Order> sortOrderList = pageable.getSort().get().toList();
        for (int i = 0; i < sortOrderList.size(); i++) {
            Sort.Order order = sortOrderList.get(i);

            String sortType = order.getDirection().isAscending() ? "asc" : "desc";
            switch (order.getProperty().toLowerCase()) {
                case "name":
                    hql += "department.name " + sortType;
                    break;
                default:
                    hql += "department.id " + sortType;
            }

            if (i != sortOrderList.size() - 1) {
                hql += ", ";
            } else {
                hql += " ";
            }
        }

        // Run query
        return entityManager.createQuery(hql, Department.class)
                .setParameter("query", "%" + query + "%")
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()) // We can't use pagable.getOffset() since they calculate offset by taking pageNumber * pageSize, we need (pageNumber - 1) * pageSize
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }
}