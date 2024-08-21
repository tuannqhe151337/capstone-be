package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.MonthlyReportSummary_;
import com.example.capstone_project.entity.Supplier;
import com.example.capstone_project.repository.SupplierRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.service.SupplierService;
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
public class SupplierServiceImpl implements SupplierService {
    private final UserAuthorityRepository userAuthorityRepository;
    private final SupplierRepository supplierRepository;

    @Override
    public List<Supplier> getListSupplierPaging(String query, Pageable pageable) {
        // Get list authorities of this user
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_SUPPLIER.getValue())) {
            return supplierRepository.getSupplierWithPagination(query, pageable);
        } else {
            throw new UnauthorizedException("Unauthorized to view list supplier");
        }
    }

    @Override
    public long countDistinctListSupplierPaging(String query) {
        // Get list authorities of this user
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_SUPPLIER.getValue())) {
            return supplierRepository.countDistinctListSupplierPaging(query);
        } else {
            throw new UnauthorizedException("Unauthorized to view list supplier");
        }
    }

    @Override
    public void createSupplier(String supplierName) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authority or role
        try {
            if (listAuthorities.contains(AuthorityCode.CREATE_NEW_SUPPLIER.getValue())) {
                supplierRepository.save(Supplier.builder().name(supplierName).build());
            } else {
                throw new UnauthorizedException("Unauthorized to create new supplier");
            }
        } catch (Exception e) {
            throw new DuplicateKeyException("Duplicate name supplier");
        }
    }

    @Override
    public void deleteSupplier(Long supplierId) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authority or role
        if (listAuthorities.contains(AuthorityCode.DELETE_SUPPLIER.getValue())) {
            if (!supplierRepository.existsById(supplierId)) {
                throw new ResourceNotFoundException("Not found any supplier have Id = " + supplierId);
            }

            Supplier supplier = supplierRepository.getReferenceById(supplierId);

            supplier.setDelete(true);

            supplierRepository.save(supplier);
        } else {
            throw new UnauthorizedException("Unauthorized to delete supplier");
        }
    }

    @Override
    public void updateSupplier(Supplier supplier) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        try {
            // Check authority or role
            if (listAuthorities.contains(AuthorityCode.UPDATE_SUPPLIER.getValue())) {
                if (!supplierRepository.existsById(supplier.getId())) {
                    throw new ResourceNotFoundException("Not found any supplier have Id = " + supplier.getId());
                }

                supplierRepository.save(supplier);
            } else {
                throw new UnauthorizedException("Unauthorized to update supplier");
            }
        } catch (Exception e) {

            throw new DuplicateKeyException("Duplicate name supplier");
        }
    }

    @Override
    public List<Supplier> getListSupplier() {
        // Get list authorities of this user
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_SUPPLIER.getValue())
                || listAuthorities.contains(AuthorityCode.VIEW_PLAN.getValue())
                || listAuthorities.contains(AuthorityCode.VIEW_REPORT.getValue())) {
            return supplierRepository.findAll(Sort.by(MonthlyReportSummary_.ID).ascending());
        } else {
            throw new UnauthorizedException("Unauthorized to view list supplier");
        }
    }
}
