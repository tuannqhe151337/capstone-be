package com.example.capstone_project.repository.result;

import lombok.*;

import java.util.Objects;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PaginateExchange {

    Integer year;
    Integer month;

    @Override
    public final boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof PaginateExchange) {
            return Objects.equals(year, ((PaginateExchange) o).year) && Objects.equals(month, ((PaginateExchange) o).month);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        if (year != null) {
            result = 31 * result + year.hashCode();
        }
        if (month != null) {
            result = 31 * result + month.hashCode();
        }
        return result;
    }
}
