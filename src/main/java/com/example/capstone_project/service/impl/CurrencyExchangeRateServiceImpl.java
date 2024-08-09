package com.example.capstone_project.service.impl;

import com.example.capstone_project.controller.body.exchange.ExchangeBody;
import com.example.capstone_project.entity.CurrencyExchangeRate;
import com.example.capstone_project.entity.Report_;
import com.example.capstone_project.repository.CurrencyExchangeRateRepository;
import com.example.capstone_project.repository.CurrencyRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.result.PaginateExchange;
import com.example.capstone_project.service.CurrencyExchangeRateService;
import com.example.capstone_project.service.result.ExchangeResult;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.helper.UserHelper;
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
            List<PaginateExchange> paginateExchanges = currencyExchangeRateRepository.getExchangeWithPagination(year, pageable);

            List<CurrencyExchangeRate> exchangeRateList = currencyExchangeRateRepository.getListCurrencyExchangeRate(paginateExchanges);

            HashMap<String, List<ExchangeResult>> exchangeHashMap = new HashMap<>();

            exchangeRateList.forEach(exchangeRate -> {
                exchangeHashMap.put(exchangeRate.getMonth().getMonth().getValue() + "/" + exchangeRate.getMonth().getYear(), new ArrayList<>());
            });

            exchangeRateList.forEach(exchangeRate -> {
                exchangeHashMap.get(exchangeRate.getMonth().getMonth().getValue() + "/" + exchangeRate.getMonth().getYear())
                        .add(ExchangeResult.builder()
                                .amount(exchangeRate.getAmount())
                                .currency(exchangeRate.getCurrency())
                                .build());
            });

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");

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
        List<PaginateExchange> paginateExchanges = currencyExchangeRateRepository.getExchangeWithPagination(year, pageable);
        return currencyExchangeRateRepository.countDistinctListExchangePaging(paginateExchanges);
    }

    @Override
    public void createExchange(List<CurrencyExchangeRate> exchangeRates) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authority or role
        try {
            if (listAuthorities.contains(AuthorityCode.CREATE_NEW_EXCHANGE.getValue())) {
                exchangeRates.forEach(exchangeRate ->
                {
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
    public void deleteExchange(Long exchangeId) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authority or role
        if (listAuthorities.contains(AuthorityCode.DELETE_EXCHANGE.getValue())) {
            if (!currencyExchangeRateRepository.existsById(exchangeId)) {
                throw new ResourceNotFoundException("Not found any exchange have Id = " + exchangeId);
            }

            CurrencyExchangeRate exchangeRate = currencyExchangeRateRepository.getReferenceById(exchangeId);

            exchangeRate.setDelete(true);

            currencyExchangeRateRepository.save(exchangeRate);
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
}
