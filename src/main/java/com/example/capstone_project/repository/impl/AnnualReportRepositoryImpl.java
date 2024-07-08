package com.example.capstone_project.repository.impl;

import com.example.capstone_project.entity.AnnualReport;
import com.example.capstone_project.repository.CustomAnnualReportRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class AnnualReportRepositoryImpl implements CustomAnnualReportRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<AnnualReport> getListAnnualReportPaging(Pageable pageable, String year) {
        String hql = "SELECT annualReport FROM AnnualReport annualReport " +
                " WHERE (annualReport.year = :year OR :year IS NULL) AND " +
                " annualReport.isDelete = false OR annualReport.isDelete is null " +
                " ORDER BY ";

        // Handling sort by and sort type
        List<Sort.Order> sortOrderList = pageable.getSort().get().toList();
        for (int i = 0; i < sortOrderList.size(); i++) {
            Sort.Order order = sortOrderList.get(i);

            String sortType = order.getDirection().isAscending() ? "asc" : "desc";
            switch (order.getProperty().toLowerCase()) {
                case "term", "totalterm", "annualreport.totalterm" :
                    hql += " annualReport.totalTerm " + sortType;
                    break;
                case "department", "totaldepartment", "annualreport.totaldepartment" :
                    hql += " annualReport.totalDepartment " + sortType;
                    break;
                case "expense", "totalexpense", "annualreport.totalexpense" :
                    hql += " annualReport.totalExpense " + sortType;
                    break;
                case "id", "annualreport_id", "annualreport.id" :
                    hql += " annualReport.id " + sortType;
                    break;
                default:
                    hql += " annualReport.createdAt desc";
            }
            if (i != sortOrderList.size() - 1) {
                hql += ", ";
            } else {
                hql += " ";
            }
        }

        return entityManager.createQuery(hql, AnnualReport.class)
                .setParameter("year",year)
                .setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize()) // We can't use pagable.getOffset() since they calculate offset by taking pageNumber * pageSize, we need (pageNumber - 1) * pageSize
                .setMaxResults(pageable.getPageSize())
                .getResultList();
    }
}
