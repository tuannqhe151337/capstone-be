package com.example.capstone_project.repository.impl;

import com.example.capstone_project.entity.Term;
import com.example.capstone_project.repository.CustomTermRepository;
import com.example.capstone_project.utils.enums.TermCode;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public class TermRepositoryImpl implements CustomTermRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Term> getListTermWhenCreatePlan(String query, Pageable pageable) {
        // HQL query
        String hql = "SELECT term FROM Term term " +
                " LEFT JOIN term.status " +
                " WHERE term.name LIKE :query AND " +
                " term.status.name != :close AND " +
                " term.planDueDate >= :now AND " +
                " term.isDelete = false " +
                " ORDER BY ";

        // Handling sort by and sort type
        List<Sort.Order> sortOrderList = pageable.getSort().get().toList();
        for (int i = 0; i < sortOrderList.size(); i++) {
            Sort.Order order = sortOrderList.get(i);

            String sortType = order.getDirection().isAscending() ? "asc" : "desc";
            switch (order.getProperty().toLowerCase()) {
                case "name", "term-name", "term_name":
                    hql += "term.name " + sortType;
                    break;
                case "status", "term_status":
                    hql += "term.status " + sortType;
                    break;
                case "duration", "term_duration":
                    hql += "term.duration " + sortType;
                    break;
                case "start_date", "start":
                    hql += "term.startDate " + sortType;
                    break;
                case "end_date", "end":
                    hql += "term.endDate " + sortType;
                    break;
                default:
                    hql += "term.id " + sortType;
            }

            if (i != sortOrderList.size() - 1) {
                hql += ", ";
            } else {
                hql += " ";
            }
        }

        // Handling join
        EntityGraph<Term> entityGraph = entityManager.createEntityGraph(Term.class);

        // Run query
        return entityManager.createQuery(hql, Term.class)
                .setParameter("query", "%" + query + "%")
                .setParameter("close", TermCode.CLOSED.getValue())
                .setParameter("now", LocalDateTime.now())
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()) // We can't use pagable.getOffset() since they calculate offset by taking pageNumber * pageSize, we need (pageNumber - 1) * pageSize
                .setMaxResults(pageable.getPageSize())
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
    }

    @Override
    public List<Term> getListTermPaging(String query, Pageable pageable) {
        // HQL query
        String hql = " SELECT term FROM Term term " +
                " LEFT JOIN term.status " +
                " WHERE term.name LIKE :query AND " +
                " term.isDelete = false " +
                " ORDER BY ";

        // Handling sort by and sort type
        List<Sort.Order> sortOrderList = pageable.getSort().get().toList();
        for (int i = 0; i < sortOrderList.size(); i++) {
            Sort.Order order = sortOrderList.get(i);

            String sortType = order.getDirection().isAscending() ? "asc" : "desc";
            switch (order.getProperty().toLowerCase()) {
                case "name", "term-name", "term_name":
                    hql += "term.name " + sortType;
                    break;
                case "status", "term_status":
                    hql += "term.status " + sortType;
                    break;
                case "duration", "term_duration":
                    hql += "term.duration " + sortType;
                    break;
                case "start_date", "start":
                    hql += "term.startDate " + sortType;
                    break;
                case "end_date", "end":
                    hql += "term.endDate " + sortType;
                    break;
                default:
                    hql += "term.id " + sortType;
            }

            if (i != sortOrderList.size() - 1) {
                hql += ", ";
            } else {
                hql += " ";
            }
        }

        // Handling join
        EntityGraph<Term> entityGraph = entityManager.createEntityGraph(Term.class);

        // Run query
        return entityManager.createQuery(hql, Term.class)
                .setParameter("query", "%" + query + "%")
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()) // We can't use pagable.getOffset() since they calculate offset by taking pageNumber * pageSize, we need (pageNumber - 1) * pageSize
                .setMaxResults(pageable.getPageSize())
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
    }
}
