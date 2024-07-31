package com.example.capstone_project.service.impl;

import com.example.capstone_project.entity.Department;
import com.example.capstone_project.entity.Position;
import com.example.capstone_project.repository.PositionRepository;
import com.example.capstone_project.repository.redis.UserAuthorityRepository;
import com.example.capstone_project.service.PositionService;
import com.example.capstone_project.utils.enums.AuthorityCode;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.helper.UserHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;
    private final UserAuthorityRepository userAuthorityRepository;

    @Override
    public List<Position> getPositions(String query, Pageable pageable) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authority or role
        if (listAuthorities.contains(AuthorityCode.VIEW_POSITION.getValue())
                || listAuthorities.contains(AuthorityCode.CREATE_NEW_USER.getValue())) {
            return positionRepository.getPositionWithPagination(query, pageable);
        } else {
            throw new UnauthorizedException("User unauthorized");
        }
    }

    @Override
    public long countDistinct(String query) {
        return positionRepository.countDistinct(query);
    }

    @Override
    public void createPosition(String positionName) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authority or role
        try {
            if (listAuthorities.contains(AuthorityCode.CREATE_NEW_POSITION.getValue())) {
                positionRepository.save(Position.builder().name(positionName).build());
            } else {
                throw new UnauthorizedException("Unauthorized to create new position");
            }
        } catch (Exception e) {

            throw new DuplicateKeyException("Duplicate name position");
        }
    }

    @Override
    public void deletePosition(Long positionId) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        // Check authority or role
        if (listAuthorities.contains(AuthorityCode.DELETE_POSITION.getValue())) {
            if (!positionRepository.existsById(positionId)) {
                throw new ResourceNotFoundException("Not found any position have Id = " + positionId);
            }

            positionRepository.deleteById(positionId);
        } else {
            throw new UnauthorizedException("Unauthorized to delete position");
        }
    }

    @Override
    public void updatePosition(Position position) {
        Set<String> listAuthorities = userAuthorityRepository.get(UserHelper.getUserId());

        try {
            // Check authority or role
            if (listAuthorities.contains(AuthorityCode.UPDATE_DEPARTMENT.getValue())) {
                if (!positionRepository.existsById(position.getId())) {
                    throw new ResourceNotFoundException("Not found any position have Id = " + position.getId());
                }

                positionRepository.save(position);
            } else {
                throw new UnauthorizedException("Unauthorized to update position");
            }
        } catch (Exception e) {

            throw new DuplicateKeyException("Duplicate name position");
        }
    }
}
