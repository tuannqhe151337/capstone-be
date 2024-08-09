package com.example.capstone_project.repository.impl;

import com.example.capstone_project.entity.Department;
import com.example.capstone_project.entity.Supplier;
import com.example.capstone_project.repository.CustomSupplierRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class SupplierRepositoryImpl implements CustomSupplierRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Supplier> getSupplierWithPagination(String query, Pageable pageable) {
        // HQL query
        String hql = " SELECT supplier FROM Supplier supplier " +
                " WHERE supplier.name like :query " +
                " AND supplier.isDelete = false " +
                " ORDER BY ";

        // Handling sort by and sort type
        List<Sort.Order> sortOrderList = pageable.getSort().get().toList();
        for (int i = 0; i < sortOrderList.size(); i++) {
            Sort.Order order = sortOrderList.get(i);

            String sortType = order.getDirection().isAscending() ? "asc" : "desc";
            switch (order.getProperty().toLowerCase()) {
                case "name":
                    hql += "supplier.name " + sortType;
                    break;
                default:
                    hql += "supplier.id " + sortType;
            }

            if (i != sortOrderList.size() - 1) {
                hql += ", ";
            } else {
                hql += " ";
            }
        }

        // Run query
        return entityManager.createQuery(hql, Supplier.class)
                .setParameter("query", "%" + query + "%")
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()) // We can't use pagable.getOffset() since they calculate offset by taking pageNumber * pageSize, we need (pageNumber - 1) * pageSize
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }
}
