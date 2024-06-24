package com.example.capstone_project.utils.enums;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public enum TermDuration {
    MONTHLY {
        @Override
        public LocalDateTime calculateEndDate(LocalDateTime startDate) {
            return startDate.plusMonths(1);
        }
    },
    QUARTERLY {
        @Override
        public LocalDateTime calculateEndDate(LocalDateTime startDate) {
            return startDate.plusMonths(6);
        }
    },
    YEARLY {
        @Override
        public LocalDateTime calculateEndDate(LocalDateTime startDate) {
            return startDate.plusYears(1);
        }
    };
    private String value;


    public abstract LocalDateTime calculateEndDate(LocalDateTime startDate);
}