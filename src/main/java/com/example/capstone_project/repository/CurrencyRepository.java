package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Currency;
import com.example.capstone_project.repository.result.ExchangeRateResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Long>, CustomCurrencyRepository {
    @Query(" SELECT count( distinct currency.id) FROM Currency currency " +
            " WHERE currency.name like %:query% AND " +
            " (currency.isDelete = false OR currency.isDelete is null)")
    long countDistinctListCurrencyPaging(String query);

    @Query(" SELECT concat(month(exchangeRate.month),'/',year(exchangeRate.month)) as date, exchangeRate.amount as amount, exchangeRate.currency.id as currencyId FROM CurrencyExchangeRate exchangeRate " +
            " WHERE (exchangeRate.id IN :fromCurrencyId OR exchangeRate.id in :toCurrencyId ) AND " +
            " (exchangeRate.isDelete = false OR exchangeRate.isDelete is null)")
    List<ExchangeRateResult> getListExchangeRate(List<Long> fromCurrencyId, Long toCurrencyId);
}
