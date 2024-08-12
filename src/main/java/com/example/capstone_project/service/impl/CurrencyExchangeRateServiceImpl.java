package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.CurrencyExchangeRate;
import com.example.capstone_project.entity.Report_;
import com.example.capstone_project.repository.CurrencyExchangeRateRepository;
import com.example.capstone_project.repository.CurrencyRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.result.PaginateExchange;
import com.example.capstone_project.service.CurrencyExchangeRateService;
import com.example.capstone_project.service.result.ExchangeResult;
import com.example.capstone_project.service.result.MonthYearResult;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.helper.UserHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeRateServiceImpl implements CurrencyExchangeRateService {
    private final UserAuthorityRepository userAuthorityRepository;
    private final CurrencyExchangeRateRepository currencyExchangeRateRepository;
    private final CurrencyRepository currencyRepository;

    @Override
    public TreeMap<String, List<ExchangeResult>> getListExchangePaging(String query, Integer year, Pageable pageable) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authority or role
        if (listAuthorities.contains(AuthorityCode.VIEW_EXCHANGE.getValue())) {
            // Get month and year
            List<PaginateExchange> paginateExchanges = currencyExchangeRateRepository.getMonthYearPaginated(year, pageable);

            if (paginateExchanges == null || paginateExchanges.isEmpty()) {
                return new TreeMap<>();
            }

            // Get exchange rate list by month and year
            List<CurrencyExchangeRate> exchangeRateList = currencyExchangeRateRepository.getListCurrencyExchangeRateByMonthYear(paginateExchanges);

            HashMap<String, List<ExchangeResult>> exchangeHashMap = new HashMap<>();

            exchangeRateList.forEach(exchangeRate -> {
                exchangeHashMap.put(exchangeRate.getMonth().getMonth().getValue() + "/" + exchangeRate.getMonth().getYear(), new ArrayList<>());
            });

            exchangeRateList.forEach(exchangeRate -> {
                exchangeHashMap.get(exchangeRate.getMonth().getMonth().getValue() + "/" + exchangeRate.getMonth().getYear())
                        .add(ExchangeResult.builder()
                                .exchangeRateId(exchangeRate.getId())
                                .amount(exchangeRate.getAmount())
                                .currency(exchangeRate.getCurrency())
                                .build());
            });

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");

            // Sorting
            TreeMap<String, List<ExchangeResult>> sortedMap = new TreeMap<>((key1, key2) -> {
                try {

                    return dateFormat.parse(key2).compareTo(dateFormat.parse(key1));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            });

            sortedMap.putAll(exchangeHashMap);

            return sortedMap;

        } else {
            throw new UnauthorizedException("User unauthorized");
        }
    }

    @Override
    public long countDistinctListExchangePaging(Integer year, Pageable pageable) {
        List<PaginateExchange> paginateExchanges = currencyExchangeRateRepository.getMonthYearPaginated(year, pageable);
        return currencyExchangeRateRepository.countDistinctListExchangePaging(year);
    }

    @Override
    @Transactional
    public void createExchange(String month, List<CurrencyExchangeRate> exchangeRates) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authority or role
        try {
            if (listAuthorities.contains(AuthorityCode.CREATE_NEW_EXCHANGE.getValue())) {
                // Delete all old monthly exchange rate if it's exists
                this.deleteExchange(month);

                // Convert month string to LocalDate
                MonthYearResult monthYearResult = this.splitMonthYearStr(month);
                LocalDate localDate = LocalDate.of(monthYearResult.getYear(), monthYearResult.getMonth(), 1);

                // Create new monthly exchange rate
                exchangeRates.forEach(exchangeRate ->
                {
                    exchangeRate.setMonth(localDate);
                    exchangeRate.setCurrency(currencyRepository.getReferenceById(exchangeRate.getCurrency().getId()));
                });

                currencyExchangeRateRepository.saveAll(exchangeRates);
            } else {
                throw new UnauthorizedException("Unauthorized to create new exchange");
            }
        } catch (Exception e) {

            throw new DuplicateKeyException("Duplicate name exchange");
        }
    }

    @Override
    @Transactional
    public void deleteExchange(String monthYear) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authority or role
        if (listAuthorities.contains(AuthorityCode.DELETE_EXCHANGE.getValue())) {
            // Split month and year by character '/'
            MonthYearResult monthYearResult = this.splitMonthYearStr(monthYear);

            // Delete all currency exchange rate in that month
            currencyExchangeRateRepository.deleteCurrencyExchangeRateIdByMonth(monthYearResult.getMonth(), monthYearResult.getYear());
        } else {
            throw new UnauthorizedException("Unauthorized to delete exchange");
        }
    }

    @Override
    public void updateExchange(CurrencyExchangeRate exchangeRate) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        try {
            // Check authority or role
            if (listAuthorities.contains(AuthorityCode.UPDATE_EXCHANGE.getValue())) {
                if (!currencyExchangeRateRepository.existsById(exchangeRate.getId())) {
                    throw new ResourceNotFoundException("Not found any exchange have Id = " + exchangeRate.getId());
                }

                CurrencyExchangeRate updateExchange = currencyExchangeRateRepository.getReferenceById(exchangeRate.getId());

                updateExchange.setAmount(exchangeRate.getAmount());

                currencyExchangeRateRepository.save(updateExchange);
            } else {
                throw new UnauthorizedException("Unauthorized to update exchange");
            }
        } catch (Exception e) {
            throw new DuplicateKeyException("Duplicate name exchange");
        }
    }

    @Override
    public List<CurrencyExchangeRate> getListExchange() {
        // Get list authorities of this user
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_EXCHANGE.getValue())) {
            return currencyExchangeRateRepository.findAll(Sort.by(Report_.ID).ascending());
        } else {
            throw new UnauthorizedException("Unauthorized to view list exchange");
        }
    }

    // Verify that string monthYear is like "02/2024"
    private MonthYearResult splitMonthYearStr(String monthYear) throws IllegalArgumentException {
        String[] parts = monthYear.split("/");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid month year");
        }

        MonthYearResult monthYearResult = new MonthYearResult();

        try {
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]);

            monthYearResult.setMonth(month);
            monthYearResult.setYear(year);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid month year");
        }

        return monthYearResult;
    }
}
