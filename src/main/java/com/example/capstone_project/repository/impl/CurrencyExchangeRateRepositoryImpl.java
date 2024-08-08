package com.example.capstone_project.repository.impl;

import com.example.capstone_project.entity.*;
import com.example.capstone_project.repository.CustomCurrencyExchangeRateRepository;
import com.example.capstone_project.repository.result.PaginateExchange;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CurrencyExchangeRateRepositoryImpl implements CustomCurrencyExchangeRateRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PaginateExchange> getExchangeWithPagination(Integer year, Pageable pageable) {
        // HQL query
        String hql = " SELECT new com.example.capstone_project.repository.result.PaginateExchange (year(exchangeRate.month) , month(exchangeRate.month)) FROM CurrencyExchangeRate exchangeRate " +
                " WHERE (year(exchangeRate.month) = :year OR :year is null) " +
                " GROUP BY  month(exchangeRate.month), year(exchangeRate.month) " +
                " ORDER BY month(exchangeRate.month), year(exchangeRate.month) ";

        // Run query
        return entityManager.createQuery(hql, PaginateExchange.class)
                .setParameter("year", year)
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()) // We can't use pagable.getOffset() since they calculate offset by taking pageNumber * pageSize, we need (pageNumber - 1) * pageSize
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }

    @Override
    public List<CurrencyExchangeRate> getListCurrencyExchangeRate(List<PaginateExchange> paginateExchanges) {
        // HQL query
        String hql = "SELECT  exchangeRate FROM CurrencyExchangeRate exchangeRate " +
                " LEFT JOIN exchangeRate.currency currency " +
                " WHERE ";


        for (int i = 0; i < paginateExchanges.size(); i++) {
            hql += " (year(exchangeRate.month) = " + paginateExchanges.get(i).getYear() + " AND month(exchangeRate.month) = " + paginateExchanges.get(i).getMonth() + ")";
            if (i != paginateExchanges.size() - 1) {
                hql += " OR ";
            } else {
                hql += " ORDER BY month(exchangeRate.month) desc, currency.id desc ";
            }
        }

        // Handling join
        EntityGraph<CurrencyExchangeRate> entityGraph = entityManager.createEntityGraph(CurrencyExchangeRate.class);
        entityGraph.addAttributeNodes(CurrencyExchangeRate_.CURRENCY);

        // Run query
        return entityManager.createQuery(hql, CurrencyExchangeRate.class)
                .setHint("jakarta.persistence.fetchgraph", entityGraph)
                .getResultList();
    }

    @Override
    public long countDistinctListExchangePaging(List<PaginateExchange> paginateExchanges) {
        // HQL query
        String hql = "SELECT distinct count(*) FROM CurrencyExchangeRate exchangeRate " +
                " LEFT JOIN exchangeRate.currency currency " +
                " WHERE ";


        for (int i = 0; i < paginateExchanges.size(); i++) {
            hql += " (year(exchangeRate.month) = " + paginateExchanges.get(i).getYear() + " AND month(exchangeRate.month) = " + paginateExchanges.get(i).getMonth() + ")";
            if (i != paginateExchanges.size() - 1) {
                hql += " OR ";
            } else {
                hql += " GROUP BY currency.id ";
            }
        }

        // Run query
        return (long) entityManager.createQuery(hql)
                .getSingleResult();
    }
}
