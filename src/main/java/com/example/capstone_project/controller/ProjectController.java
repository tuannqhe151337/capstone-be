package com.example.capstone_project.controller;

import com.example.capstone_project.controller.body.project.DeleteProjectBody;
import com.example.capstone_project.controller.body.project.NewProjectBody;
import com.example.capstone_project.controller.body.project.UpdateProjectBody;
import com.example.capstone_project.controller.responses.ExceptionResponse;
import com.example.capstone_project.controller.responses.ListPaginationResponse;
import com.example.capstone_project.controller.responses.ListResponse;
import com.example.capstone_project.controller.responses.Pagination;
import com.example.capstone_project.controller.responses.project.ProjectPaginateResponse;
import com.example.capstone_project.entity.Project;
import com.example.capstone_project.service.ProjectService;
import com.example.capstone_project.utils.exception.ResourceNotFoundException;
import com.example.capstone_project.utils.exception.UnauthorizedException;
import com.example.capstone_project.utils.exception.term.InvalidDateException;
import com.example.capstone_project.utils.helper.PaginationHelper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping("/list-paginate")
    public ResponseEntity<ListPaginationResponse<ProjectPaginateResponse>> getListProjectPaging(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String page,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortType
    ) {
        try {
            // Handling page and pageSize
            Integer pageInt = PaginationHelper.convertPageToInteger(page);
            Integer sizeInt = PaginationHelper.convertPageSizeToInteger(size);

            // Handling query
            if (query == null) {
                query = "";
            }

            // Handling pagination
            Pageable pageable = PaginationHelper.handlingPagination(pageInt, sizeInt, sortBy, sortType);

            // Get data
            List<Project> projects = projectService.getListProjectPaging(query, pageable);

            // Response
            ListPaginationResponse<ProjectPaginateResponse> response = new ListPaginationResponse<>();

            long count = 0;

            if (projects != null) {

                // Count total record
                count = projectService.countDistinctListProjectPaging(query);

                projects.forEach(project -> response.getData().add(ProjectPaginateResponse.builder()
                        .projectId(project.getId())
                        .name(project.getName())
                        .createdAt(project.getCreatedAt())
                        .updatedAt(project.getUpdatedAt())
                        .build()));

            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }

            long numPages = PaginationHelper.calculateNumPages(count, sizeInt);

            response.setPagination(Pagination.builder()
                    .totalRecords(count)
                    .page(pageInt)
                    .limitRecordsPerPage(sizeInt)
                    .numPages(numPages)
                    .build());

            return ResponseEntity.ok(response);
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ExceptionResponse> createProject(
            @Valid @RequestBody NewProjectBody newProjectBody, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            // Xử lý lỗi validation và trả về phản hồi lỗi
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ExceptionResponse.builder().field("Validation Error").message(errorMessage).build());
        }

        try {

            projectService.createProject(newProjectBody.getProjectName());

            return ResponseEntity.status(HttpStatus.CREATED).body(ExceptionResponse.builder().field("Create").message("Create successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Unauthorized").build());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Duplicate name project").build());
        }
    }

    @DeleteMapping()
    public ResponseEntity<ExceptionResponse> deleteProject(
            @Valid @RequestBody DeleteProjectBody deleteProjectBody, BindingResult bindingResult) throws Exception {
        try {

            // Delete plan
            projectService.deleteProject(deleteProjectBody.getProjectId());

            return ResponseEntity.status(HttpStatus.OK).body(ExceptionResponse.builder().field("Delete").message("Delete successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Unauthorized").build());
        } catch (DuplicateKeyException | ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Not found any project have id = " + deleteProjectBody.getProjectId()).build());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ExceptionResponse> updateProject(
            @Valid @RequestBody UpdateProjectBody updateProjectBody, BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            // Xử lý lỗi validation và trả về phản hồi lỗi
            String errorMessage = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ExceptionResponse.builder().field("Validation Error").message(errorMessage).build());
        }

        try {

            projectService.updateProject(Project.builder().id(updateProjectBody.getProjectId()).name(updateProjectBody.getProjectName()).build());

            return ResponseEntity.status(HttpStatus.OK).body(ExceptionResponse.builder().field("Update").message("Update successful").build());
        } catch (UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Unauthorized").build());
        } catch (DuplicateKeyException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ExceptionResponse.builder().field("Error exception").message("Duplicate name project").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ExceptionResponse.builder().field("Error exception").message("Not found any project have id = " + updateProjectBody.getProjectId()).build());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<ListResponse<ProjectPaginateResponse>> getListProject() {

        // Get data
        List<Project> projects = projectService.getListProject();

        // Response
        ListResponse<ProjectPaginateResponse> responses = new ListResponse<>();

        if (projects != null) {
            projects.forEach(project -> {
                responses.getData().add(ProjectPaginateResponse.builder()
                        .projectId(project.getId())
                        .name(project.getName())
                        .createdAt(project.getCreatedAt())
                        .updatedAt(project.getUpdatedAt())
                        .build());
            });
        } else {

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        return ResponseEntity.ok(responses);
    }
}
