package com.example.capstone_project.repository;

import com.example.capstone_project.entity.Currency;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomCurrencyRepository {
    List<Currency> getCurrencyWithPagination(String query, Pageable pageable);
}
