package com.example.capstone_project.utils.enums;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public enum TermDuration {
    MONTHLY {
        @Override
        public LocalDateTime calculateEndDate(LocalDateTime startDate) {
            return startDate.plus(1, ChronoUnit.MONTHS);
        }
    },
    QUARTERLY {
        @Override
        public LocalDateTime calculateEndDate(LocalDateTime startDate) {
            return startDate.plus(3, ChronoUnit.MONTHS);
        }
    },
    YEARLY {
        @Override
        public LocalDateTime calculateEndDate(LocalDateTime startDate) {
            return startDate.plus(1, ChronoUnit.YEARS);
        }
    };
    private String value;


    public abstract LocalDateTime calculateEndDate(LocalDateTime startDate);
}