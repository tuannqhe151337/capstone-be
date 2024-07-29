package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.AnnualReport;
import com.example.capstone_project.repository.AnnualReportRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.service.AnnualReportService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AnnualReportServiceImpl implements AnnualReportService {
    private final UserAuthorityRepository userAuthorityRepository;
    private final AnnualReportRepository annualReportRepository;

    @Override
    public List<AnnualReport> getListAnnualReportPaging(Pageable pageable) {
        // Get list authorities of this user
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_ANNUAL_REPORT.getValue())) {
            return annualReportRepository.getListAnnualReportPaging(pageable);
        }
        return null;
    }

    @Override
    public long countDistinctListAnnualReportPaging() {
        return annualReportRepository.countDistinctListAnnualReportPaging();
    }
}
