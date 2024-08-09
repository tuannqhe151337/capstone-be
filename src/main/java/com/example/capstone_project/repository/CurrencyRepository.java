package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Currency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CurrencyRepository extends JpaRepository<Currency, Long>, CustomCurrencyRepository {
    @Query(" SELECT count( distinct currency.id) FROM Currency currency " +
            " WHERE currency.name like %:query% AND " +
            " (currency.isDelete = false OR currency.isDelete is null)")
    long countDistinctListCurrencyPaging(String query);
}
