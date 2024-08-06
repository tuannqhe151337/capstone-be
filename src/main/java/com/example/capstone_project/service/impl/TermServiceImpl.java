package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.*;

import com.example.capstone_project.repository.TermIntervalRepository;
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
    private final TermIntervalRepository termIntervalRepository;

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
        } else {
            throw new UnauthorizedException("User authority to create new plan");
        }
    }

    @Override

    public Term updateTerm(Term term) throws Exception {

        long userId = UserHelper.getUserId();
        if (!userAuthorityRepository.get(userId).contains(AuthorityCode.EDIT_TERM.getValue())) {
            throw new UnauthorizedException("Unauthorized to update term");
        }
        checkValidDateOfTerm(term);

        //calculate final end term date
        LocalDateTime startDate = term.getStartDate();
        LocalDateTime finalEndTermDate = term.getDuration().calculateEndDate(startDate);
        term.setFinalEndTermDate(finalEndTermDate);

        Term currentterm = termRepository.findById(term.getId()).
                orElseThrow(() -> new ResourceNotFoundException("Term not exist with id: " + term.getId()));
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

        checkValidDateOfTerm(term);

        LocalDateTime finalEndTermDate;
        finalEndTermDate = term.getDuration().calculateEndDate(term.getStartDate());
        term.setFinalEndTermDate(finalEndTermDate);
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
    //Check valid date of term
    public void checkValidDateOfTerm(Term term) throws Exception {
        LocalDateTime finalEndTermDate;
        finalEndTermDate = term.getDuration().calculateEndDate(term.getStartDate());
        TermInterval termInterval = termIntervalRepository.getReferenceById(1);
        //check start term date must in 25
        if(term.getStartDate().getDayOfMonth() != termInterval.getStartTermDate()) {
            throw new InvalidStartTermDateException("Start date must be in day " + termInterval.getStartTermDate());
        }

        if (finalEndTermDate.isBefore(LocalDateTime.now())) {
            throw new InvalidEndDateException("Final end date must be in the future");
        }

        //throw exception if end date is before start date
        //after 5 days from start day - false
        if (term.getEndDate().isBefore(term.getStartDate()) || term.getEndDate().isAfter(finalEndTermDate)
                || term.getEndDate().isAfter(term.getStartDate().plusDays(termInterval.getEndTermInterval())))
        {
            throw new InvalidEndDateException("End date must be within 5 days from start date");
        }
        //throw exception if start reup date is before end date
        //before 20 days - false
        if (term.getReuploadStartDate().isBefore(term.getEndDate()) || term.getReuploadStartDate().isAfter(finalEndTermDate)
                ||    term.getReuploadStartDate().isBefore(term.getStartDate().plusDays(termInterval.getStartReuploadInterval()))) {
            throw new InvalidStartReupDateException("Re-upload start date must be after 20 days from start date");
        }
        //throw ex if end reup date is before start reup date
        //after 3 days vs start re up date - false
        if (term.getReuploadEndDate().isBefore(term.getReuploadStartDate()) ||
                term.getReuploadEndDate().isAfter(finalEndTermDate) ||
                term.getReuploadEndDate().isAfter(term.getReuploadStartDate().plusDays(termInterval.getEndReuploadInterval())) ) {
            throw new InvalidEndReupDateException("Re-upload end date must be 3 days from start reupload date");
        }
    }


}
