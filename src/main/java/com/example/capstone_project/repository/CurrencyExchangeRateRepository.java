package com.example.capstone_project.repository;

import com.example.capstone_project.entity.CurrencyExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CurrencyExchangeRateRepository extends JpaRepository<CurrencyExchangeRate, Long>, CustomCurrencyExchangeRateRepository {
//    @Query( " SELECT count(distinct(exchangeRate)) FROM CurrencyExchangeRate exchangeRate " +
//            " WHERE (year(exchangeRate.month) = :year OR :year is null )" +
//            " GROUP BY year(exchangeRate.month), month(exchangeRate.month)")
//    long countDistinctListExchangePaging(Integer year);
}
