package com.example.capstone_project.utils.helper;

import com.example.capstone_project.controller.responses.CustomSort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.*;

import java.util.List;


public class PaginationHelper {
    public static Integer convertPageToInteger(String page) {
        Integer pageInt = null;
        try {
            pageInt = Integer.parseInt(page);
            if (pageInt <= 0) {
                pageInt = 1;
            }
        } catch (Exception e) {
            pageInt = 1;
        }

        return pageInt;
    }

    public static Integer convertPageSizeToInteger(String pageSize) {
        Integer pagesize = null;
        try {
            pagesize = Integer.parseInt(pageSize);
            if (pagesize <= 0) {
                pagesize = 10;
            }
        } catch (Exception e) {
            pagesize = 10;
        }

        return pagesize;
    }

    public static long calculateNumPages(Long count, Integer pageSize) {
        if (count != null && pageSize != null && pageSize > 0) {
            return (long) Math.ceil((double) count / pageSize);
        } else {
            return 0L;
        }
    }

    public static Pageable handlingPagination(Integer page, Integer size, String sortBy, String sortType){

        // Handling page and pageSize
        if (page == null || page <= 0) {
            page = 1;
        }

        if (size == null || size <= 0) {
            size = 10;
        }

        // Handling sortBy and sortType
        if (sortBy == null) {
            sortBy = "";
        }

        if (sortType == null) {
            sortType = "";
        }

        if (sortBy.isBlank() && sortType.isBlank()) {
           return PageRequest.of(page, size, Sort.by(SortDefault.ID).descending());

        } else {
            sortBy = sortBy.isBlank() ? SortDefault.ID : sortBy;
            return switch (sortType.toLowerCase()) {
                case "desc", "descending" -> PageRequest.of(page, size, Sort.by(sortBy).descending());
                default -> PageRequest.of(page, size, Sort.by(sortBy).ascending());
            };
        }
    }

    public static Pageable handlingPaginationWithMultiSort(Integer page, Integer size, List<CustomSort> sorts){

        // Handling page and pageSize
        if (page == null || page <= 0) {
            page = 1;
        }

        if (size == null || size <= 0) {
            size = 10;
        }

        if (sorts.isEmpty()) {
            return handlingPagination(page, size, "","");
        } else {
            return PageRequest.of(page, size, Sort.by(
                    sorts.stream().map(sort-> switch (sort.getSortType().toLowerCase()){
                        case "desc", "descending" -> Sort.Order.desc(sort.getSortBy());
                        default -> Sort.Order.asc(sort.getSortBy());
                    }).toList()
            ));
        }
    }
    //Convert a LIST to a PAGE
    public static <T> Page<T> createPage(List<T> list, PageRequest pageRequest) {
        return new PageImpl<>(list, pageRequest, list.size());
    }

}
