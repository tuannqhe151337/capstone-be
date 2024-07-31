package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.TermStatus;
import com.example.capstone_project.entity.User;
import com.example.capstone_project.entity.UserDetail;

import com.example.capstone_project.entity.Term;
import com.example.capstone_project.repository.TermRepository;
import com.example.capstone_project.repository.TermStatusRepository;
import com.example.capstone_project.repository.UserRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.repository.redis.UserDetailRepository;
import com.example.capstone_project.service.TermService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.enums.TermCode;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.term.*;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.term.InvalidDateException;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TermServiceImpl implements TermService {
    private final TermRepository termRepository;
    private final UserDetailRepository userDetailRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final UserRepository userRepository;
    private final TermStatusRepository termStatusRepository;

    @Override
    public long countDistinct(String query) throws Exception {
        // Get user detail
        UserDetail userDetail = userDetailRepository.get(UserHelper.getUserId());

        return termRepository.countDistinctListTermWhenCreatePlan(query, TermCode.CLOSED.getValue(), LocalDateTime.now(), userDetail.getDepartmentId());
    }

    @Override
    public long countDistinctListTermWhenCreatePlan(String query) throws Exception {
        // Get user detail
        UserDetail userDetail = userDetailRepository.get(UserHelper.getUserId());

        return termRepository.countDistinctListTermWhenCreatePlan(query, TermCode.CLOSED.getValue(), LocalDateTime.now(), userDetail.getDepartmentId());
    }

    @Override
    public List<Term> getListTermWhenCreatePlan(String query, Pageable pageable) throws Exception {
        // Get userId
        long userId = UserHelper.getUserId();
        // Get user detail
        UserDetail userDetail = userDetailRepository.get(userId);

        if (userAuthorityRepository.get(userId).contains(AuthorityCode.IMPORT_PLAN.getValue())) {
            return termRepository.getListTermWhenCreatePlan(query, pageable, userDetail.getDepartmentId());
        }

        return null;
    }

    @Override

    public Term updateTerm(Term term) throws Exception {

        long userId = UserHelper.getUserId();
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.EDIT_TERM.getValue())) {
            throw new UnauthorizedException("Unauthorized to update term");
        }
        LocalDateTime startDate = term.getStartDate();
        //get current term to extract its status

        Term currentterm = termRepository.findById(term.getId()).
                orElseThrow(() -> new ResourceNotFoundException("Term not exist with id: " + term.getId()));

        LocalDateTime finalEndTermDate ;
        finalEndTermDate = term.getDuration().calculateEndDate(startDate);
        term.setFinalEndTermDate(finalEndTermDate);

        //throw exception if end date is before start date ,
        if (term.getEndDate().isBefore(term.getStartDate()) || term.getEndDate().isAfter(finalEndTermDate)) {
            throw new InvalidEndDateException("End date must be in the future and after start date");
        }
        //throw exception if start reup date is before end date
        if (term.getReuploadStartDate().isBefore(term.getEndDate()) || term.getReuploadStartDate().isAfter(finalEndTermDate)) {
            throw new InvalidStartReupDateException("Re-upload start date must be in the future and after end date");
        }
        //throw ex if end reup date is before start reup date
        if (term.getReuploadEndDate().isBefore(term.getReuploadStartDate()) || term.getReuploadEndDate().isAfter(finalEndTermDate)) {
            throw new InvalidEndReupDateException("Re-upload end date must be in the future and after re-up start date");
        }

        //status
        term.setStatus(currentterm.getStatus());

        //create-by
        User userby = userRepository.findUserById(userId).get();
        term.setUser(userby);

        return termRepository.save(term);
    }

    @Override
    public void deleteTerm(Long id) throws Exception {
        long userId = UserHelper.getUserId();
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.DELETE_TERM.getValue())) {
            throw new UnauthorizedException("Unauthorized");
        }
        Term currentTerm = termRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Term not exist with id: " + id));
        currentTerm.setDelete(true);
        termRepository.save(currentTerm);

    }

    @Override
    public Term findTermById(Long id) throws Exception {
        long userId = UserHelper.getUserId();
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.IMPORT_PLAN.getValue())) {
            throw new UnauthorizedException("Unauthorized to access this resource");
        }
        Term term = termRepository.findTermById(id);
        if (term == null) {
            throw new ResourceNotFoundException("Term not found");
        } else {
            return term;
        }

    }

    @Override
    public void updateTermStatus(Term term, Long statusId) throws Exception {
        TermStatus termStatus = termStatusRepository.getReferenceById(statusId);
        term.setStatus(termStatus);
        termRepository.save(term);
    }

    @Override
    public void startTermManually(Long termId) throws Exception {
        long userId = UserHelper.getUserId();
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.START_TERM.getValue())) {
            throw new UnauthorizedException("Unauthorized to start term");
        }
        Term term = termRepository.findTermById(termId);
        if (term == null) {
            throw new ResourceNotFoundException("Term not found");
        }
        TermStatus termStatus = termStatusRepository.getReferenceById(2L);
        term.setStatus(termStatus);
        term.setStartDate(LocalDateTime.now());
        termRepository.save(term);
    }


    @Override
    public void createTerm(Term term) throws Exception {
        long userId = UserHelper.getUserId();
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.CREATE_TERM.getValue())) {
            throw new UnauthorizedException("Unauthorized to create term");
        }
        LocalDateTime finalEndTermDate ;
        finalEndTermDate = term.getDuration().calculateEndDate(term.getStartDate());
        term.setFinalEndTermDate(finalEndTermDate);


        //throw exception if end date is before start date ,
        if (term.getEndDate().isBefore(term.getStartDate()) || term.getEndDate().isAfter(finalEndTermDate)) {
            throw new InvalidEndDateException("End date must be in the future and after start date");
        }
        //throw exception if start reup date is before end date
        if (term.getReuploadStartDate().isBefore(term.getEndDate()) || term.getReuploadStartDate().isAfter(finalEndTermDate)) {
            throw new InvalidStartReupDateException("Re-upload start date must be in the future and after end date");
        }
        //throw ex if end reup date is before start reup date
        if (term.getReuploadEndDate().isBefore(term.getReuploadStartDate()) || term.getReuploadEndDate().isAfter(finalEndTermDate)) {
            throw new InvalidEndReupDateException("Re-upload end date must be in the future and after re-up start date");
        }

        //status-id , create-by
        TermStatus status = termStatusRepository.getReferenceById(1L);
        term.setStatus(status);

        User user = userRepository.getReferenceById(userId);
        term.setUser(user);


        termRepository.save(term);

    }

    @Override
    public List<Term> getListTermPaging(Long statusId, String query, Pageable pageable) {
        long userId = UserHelper.getUserId();

        if (userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_PLAN.getValue())
                || userAuthorityRepository.get(userId).contains(AuthorityCode.VIEW_TERM.getValue())) {
            return termRepository.getListTermPaging(statusId, query, pageable);

        }

        return null;
    }

    @Override
    public long countDistinctListTermPaging(Long statusId, String query) {
        return termRepository.countDistinctListTermPaging(statusId, query);
    }
    //start term change status of this term


}
