package com.example.capstone_project.controller;
import com.example.capstone_project.controller.body.term.create.CreateTermBody;
import com.example.capstone_project.entity.Term;
import com.example.capstone_project.entity.TermStatus;
import com.example.capstone_project.service.TermService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/term")
public class TermController {
    private final TermService termService;

    @GetMapping
    public ResponseEntity<List<Term>> getAllTerms() {
        return null;
    }
    @GetMapping("/{id}")
    public ResponseEntity<Term> getTermDetailmById(@PathVariable("id") Long id) {

        return null;
    }
    @PostMapping
    public ResponseEntity<String> createTerm(@RequestBody CreateTermBody createTermBody ) {
        return ResponseEntity.status(HttpStatus.OK).body("Created successfully");
    }
    @PutMapping
    public ResponseEntity<String> updateTerm(Term term) {
       // return .save(term);
        return null;
    }

}
