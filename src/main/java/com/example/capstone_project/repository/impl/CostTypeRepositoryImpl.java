package com.example.capstone_project.repository.impl;

import com.example.capstone_project.entity.CostType;
import com.example.capstone_project.entity.Department;
import com.example.capstone_project.repository.CustomCostTypeRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CostTypeRepositoryImpl implements CustomCostTypeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CostType> getListCostTypePaginate(String query, Pageable pageable) {
        // HQL query
        String hql = "select costType from CostType costType " +
                "where costType.name like :query " +
                "and costType.isDelete = false " +
                "order by ";

        // Handling sort by and sort type
        List<Sort.Order> sortOrderList = pageable.getSort().get().toList();
        for (int i = 0; i < sortOrderList.size(); i++) {
            Sort.Order order = sortOrderList.get(i);

            String sortType = order.getDirection().isAscending() ? "asc" : "desc";
            switch (order.getProperty().toLowerCase()) {
                case "name", "costtypename", "cost_type_name":
                    hql += "costType.name " + sortType;
                    break;
                default:
                    hql += "costType.id " + sortType;
            }

            if (i != sortOrderList.size() - 1) {
                hql += ", ";
            } else {
                hql += " ";
            }
        }

        // Run query
        return entityManager.createQuery(hql, CostType.class)
                .setParameter("query", "%" + query + "%")
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()) // We can't use pagable.getOffset() since they calculate offset by taking pageNumber * pageSize, we need (pageNumber - 1) * pageSize
                .setMaxResults(pageable.getPageSize())
                .getResultList();

    }
}
