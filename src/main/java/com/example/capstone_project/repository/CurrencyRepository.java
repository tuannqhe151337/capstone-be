package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Currency;
import com.example.capstone_project.repository.result.ExchangeRateResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface CurrencyRepository extends JpaRepository<Currency, Long>, CustomCurrencyRepository {
    @Query(" SELECT count( distinct currency.id) FROM Currency currency " +
            " WHERE currency.name like %:query% AND " +
            " (currency.isDelete = false OR currency.isDelete is null)")
    long countDistinctListCurrencyPaging(String query);

    @Query(" SELECT concat(month(exchangeRate.month),'/',year(exchangeRate.month)) as date, exchangeRate.amount as amount, exchangeRate.currency.id as currencyId FROM CurrencyExchangeRate exchangeRate " +
            " WHERE (exchangeRate.currency.id IN :fromCurrencyId OR exchangeRate.currency.id = :toCurrencyId ) AND " +
            " (year(exchangeRate.month) IN :years AND month(exchangeRate.month) IN :months) AND " +
            " (exchangeRate.isDelete = false OR exchangeRate.isDelete is null)")
    List<ExchangeRateResult> getListExchangeRate(Set<Long> fromCurrencyId, Set<Integer> years, Set<Integer> months, Long toCurrencyId);

    @Query("SELECT currency from Currency currency " +
            "WHERE currency.isDelete = false OR currency.isDelete IS NULL " +
            "ORDER BY currency.id desc")
    List<Currency> findAll();
}
