package com.example.capstone_project.service.impl;

import com.example.capstone_project.controller.body.annual.AnnualReportExpenseBody;
import com.example.capstone_project.controller.responses.annualReport.diagram.CostTypeDiagramResponse;
import com.example.capstone_project.entity.AnnualReport;
import com.example.capstone_project.entity.Report;
import com.example.capstone_project.repository.AnnualReportRepository;
import com.example.capstone_project.repository.ReportRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.result.CostTypeDiagramResult;
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
    public List<AnnualReport> getListAnnualReportPaging(Pageable pageable, String year) {
        // Get list authorities of this user
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_ANNUAL_REPORT.getValue())) {
            return annualReportRepository.getListAnnualReportPaging(pageable, year);
        }
        return null;
    }

    @Override
    public long countDistinctListAnnualReportPaging(String year) {
        return annualReportRepository.countDistinctListAnnualReportPaging(year);
    }

    @Override
    public List<Report> getListExpenseWithPaginate(Long annualReportId, Long costTypeId, Long departmentId, Pageable pageable) {
        // Get list authorities of this user
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_ANNUAL_REPORT.getValue())) {
            return annualReportRepository.getListExpenseWithPaginate(annualReportId, costTypeId, departmentId, pageable);
        }
        return null;
    }

    @Override
    public long countDistinctListExpenseWithPaginate(Long annualReportId, Long costTypeId, Long departmentId) {
        return annualReportRepository.countDistinctListExpenseWithPaginate(annualReportId, costTypeId, departmentId);
    }

    @Override
    public List<CostTypeDiagramResult> getAnnualReportCostTypeDiagram(Long annualReportId) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_ANNUAL_REPORT.getValue())) {
            return annualReportRepository.getAnnualReportCostTypeDiagram(annualReportId);
        }
        return null;

    }

    @Override
    public AnnualReport getAnnualReportDetail(Long annualReportBody) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        if (listAuthorities.contains(AuthorityCode.VIEW_ANNUAL_REPORT.getValue())) {
            return annualReportRepository.getReferenceById(annualReportBody);
        }
        return null;

    }
}
