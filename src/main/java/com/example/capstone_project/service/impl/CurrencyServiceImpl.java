package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.Currency;
import com.example.capstone_project.entity.Currency_;
import com.example.capstone_project.repository.CurrencyRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.service.CurrencyService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final UserAuthorityRepository userAuthorityRepository;
    private final CurrencyRepository currencyRepository;

    @Override
    public List<Currency> getListCurrencyPaging(String query, Pageable pageable) {
        // Get list authorities of this user
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_CURRENCY.getValue())) {
            return currencyRepository.getCurrencyWithPagination(query, pageable);
        } else {
            throw new UnauthorizedException("Unauthorized to view list currency");
        }
    }

    @Override
    public long countDistinctListCurrencyPaging(String query) {
        // Get list authorities of this user
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_CURRENCY.getValue())) {
            return currencyRepository.countDistinctListCurrencyPaging(query);
        } else {
            throw new UnauthorizedException("Unauthorized to view list currency");
        }
    }

    @Override
    public void createCurrency(Currency currency) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authority or role
        try {
            if (listAuthorities.contains(AuthorityCode.CREATE_NEW_CURRENCY.getValue())) {
                currencyRepository.save(Currency.builder()
                        .name(currency.getName())
                        .symbol(currency.getSymbol())
                        .affix(currency.getAffix())
                        .build());
            } else {
                throw new UnauthorizedException("Unauthorized to create new currency");
            }
        } catch (Exception e) {

            throw new DuplicateKeyException("Duplicate name currency");
        }
    }

    @Override
    public void deleteCurrency(Long currencyId) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authority or role
        if (listAuthorities.contains(AuthorityCode.DELETE_CURRENCY.getValue())) {
            if (!currencyRepository.existsById(currencyId)) {
                throw new ResourceNotFoundException("Not found any currency have Id = " + currencyId);
            }

            Currency currency = currencyRepository.getReferenceById(currencyId);

            currency.setDelete(true);

            currencyRepository.save(currency);
        } else {
            throw new UnauthorizedException("Unauthorized to delete currency");
        }
    }

    @Override
    public void updateCurrency(Currency currency) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        try {
            // Check authority or role
            if (listAuthorities.contains(AuthorityCode.UPDATE_CURRENCY.getValue())) {
                if (!currencyRepository.existsById(currency.getId())) {
                    throw new ResourceNotFoundException("Not found any currency have Id = " + currency.getId());
                }

                currencyRepository.save(currency);
            } else {
                throw new UnauthorizedException("Unauthorized to update currency");
            }
        } catch (Exception e) {

            throw new DuplicateKeyException("Duplicate name currency");
        }
    }

    @Override
    public List<Currency> getListCurrency() {
        // Get list authorities of this user
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_CURRENCY.getValue())) {
            return currencyRepository.findAll(Sort.by(Currency_.ID).ascending());
        } else {
            throw new UnauthorizedException("Unauthorized to view list currency");
        }
    }
}
