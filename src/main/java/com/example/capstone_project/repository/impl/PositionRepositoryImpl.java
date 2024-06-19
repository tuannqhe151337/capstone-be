package com.example.capstone_project.repository.impl;


import com.example.capstone_project.entity.Position;
import com.example.capstone_project.repository.CustomPositionRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PositionRepositoryImpl implements CustomPositionRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Position> getPositionWithPagination(String query, Pageable pageable) {
        // HQL query
        String hql = "select position from Position position " +
                "where position.name like :query " +
                "and position.isDelete = false " +
                "order by ";

        // Handling sort by and sort type
        List<Sort.Order> sortOrderList = pageable.getSort().get().toList();
        for (int i = 0; i < sortOrderList.size(); i++) {
            Sort.Order order = sortOrderList.get(i);

            String sortType = order.getDirection().isAscending() ? "asc" : "desc";
            switch (order.getProperty().toLowerCase()) {
                case "name":
                    hql += "position.name " + sortType;
                    break;
                default:
                    hql += "position.id " + sortType;
            }

            if (i != sortOrderList.size() - 1) {
                hql += ", ";
            } else {
                hql += " ";
            }
        }
        EntityGraph<Position> entityGraph = entityManager.createEntityGraph(Position.class);

        // Thêm các thuộc tính cần nạp vào EntityGraph
        entityGraph.addAttributeNodes("id", "name", "isDelete");
        // Run query
        return entityManager.createQuery(hql, Position.class)
                .setParameter("query", "%" + query + "%")
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()) // We can't use pagable.getOffset() since they calculate offset by taking pageNumber * pageSize, we need (pageNumber - 1) * pageSize
                .setMaxResults(pageable.getPageSize())
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
    }
}