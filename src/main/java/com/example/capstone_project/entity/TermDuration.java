package com.example.capstone_project.entity;

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


    public abstract LocalDateTime calculateEndDate(LocalDateTime startDate);
}