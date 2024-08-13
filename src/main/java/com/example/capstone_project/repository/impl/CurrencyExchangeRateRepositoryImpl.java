package com.example.capstone_project.repository.impl;

import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.CustomCurrencyExchangeRateRepository;
import com.example.capstone_project.repository.result.PaginateExchange;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CurrencyExchangeRateRepositoryImpl implements CustomCurrencyExchangeRateRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PaginateExchange> getMonthYearPaginated(Integer year, Pageable pageable) {
        // HQL query
        String hql = " SELECT new com.example.capstone_project.repository.result.PaginateExchange (year(exchangeRate.month) , month(exchangeRate.month)) FROM CurrencyExchangeRate exchangeRate " +
                " WHERE (year(exchangeRate.month) = :year OR :year is null) " +
                " AND (exchangeRate.isDelete = false OR exchangeRate.isDelete IS NULL)" +
                " GROUP BY month(exchangeRate.month), year(exchangeRate.month) " +
                " ORDER BY year(exchangeRate.month) desc, month(exchangeRate.month) desc";

        // Run query
        return entityManager.createQuery(hql, PaginateExchange.class)
                .setParameter("year", year)
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()) // We can't use pagable.getOffset() since they calculate offset by taking pageNumber * pageSize, we need (pageNumber - 1) * pageSize
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public List<CurrencyExchangeRate> getListCurrencyExchangeRateByMonthYear(List<PaginateExchange> paginateExchanges) {
        // HQL query
        String hql = "SELECT  exchangeRate FROM CurrencyExchangeRate exchangeRate " +
                " LEFT JOIN exchangeRate.currency currency " +
                " WHERE (exchangeRate.isDelete = false OR exchangeRate.isDelete IS NULL) ";

        if (!paginateExchanges.isEmpty()) {
            hql += " AND (";
        }

        for (int i = 0; i < paginateExchanges.size(); i++) {
            hql += " (year(exchangeRate.month) = :year" + i + " AND month(exchangeRate.month) = :month" + i + ")";
            if (i != paginateExchanges.size() - 1) {
                hql += " OR ";
            }
        }

        hql += ") ORDER BY month(exchangeRate.month) desc, currency.id asc ";

        // Handling join
        EntityGraph<CurrencyExchangeRate> entityGraph = entityManager.createEntityGraph(CurrencyExchangeRate.class);
        entityGraph.addAttributeNodes(CurrencyExchangeRate_.CURRENCY);

        // Run query
        TypedQuery<CurrencyExchangeRate> typedQuery = entityManager.createQuery(hql, CurrencyExchangeRate.class);

        for (int i = 0; i < paginateExchanges.size(); i++) {
            typedQuery.setParameter("year" + i, paginateExchanges.get(i).getYear());
            typedQuery.setParameter("month" + i, paginateExchanges.get(i).getMonth());
        }

        return typedQuery.setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
    }

    @Override
    public List<CurrencyExchangeRate> getListCurrencyExchangeRateByMonthYear(List<PaginateExchange> paginateExchanges, List<Long> currencyIds) {
        // HQL query
        String hql = "SELECT  exchangeRate FROM CurrencyExchangeRate exchangeRate " +
                " LEFT JOIN exchangeRate.currency currency " +
                " WHERE (exchangeRate.isDelete = false OR exchangeRate.isDelete IS NULL) " +
                " AND currency.id IN (:currencyIds) ";

        if (!paginateExchanges.isEmpty()) {
            hql += " AND ";
        }

        for (int i = 0; i < paginateExchanges.size(); i++) {
            hql += " (year(exchangeRate.month) = :year" + i + " AND month(exchangeRate.month) = :month" + i + ")";
            if (i != paginateExchanges.size() - 1) {
                hql += " OR ";
            }
        }

        hql += " ORDER BY month(exchangeRate.month) desc, currency.id asc ";

        // Handling join
        EntityGraph<CurrencyExchangeRate> entityGraph = entityManager.createEntityGraph(CurrencyExchangeRate.class);
        entityGraph.addAttributeNodes(CurrencyExchangeRate_.CURRENCY);

        // Run query
        TypedQuery<CurrencyExchangeRate> typedQuery = entityManager.createQuery(hql, CurrencyExchangeRate.class);

        for (int i = 0; i < paginateExchanges.size(); i++) {
            typedQuery.setParameter("year" + i, paginateExchanges.get(i).getYear());
            typedQuery.setParameter("month" + i, paginateExchanges.get(i).getMonth());
        }

        typedQuery.setParameter("currencyIds", currencyIds);

        return typedQuery.setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
    }

    @Override
    public long countDistinctListExchangePaging(Integer year) {
        // HQL query
        String hql = "SELECT COUNT(DISTINCT(CONCAT(MONTH(currencyExchangeRate.month), '/', YEAR(currencyExchangeRate.month)))) " +
                " FROM CurrencyExchangeRate currencyExchangeRate" +
                " WHERE (currencyExchangeRate.isDelete = false OR currencyExchangeRate.isDelete IS NULL) AND " +
                " (YEAR(currencyExchangeRate.month) = :year OR :year is null)";

        // Run query
        return (Long) entityManager.createQuery(hql)
                .setParameter("year", year)
                .getSingleResult();
    }
}
