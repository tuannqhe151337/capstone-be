package com.example.capstone_project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(schema = "capstone_v2", name = "term_intervals")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Builder
public class TermInterval extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Number cannot be null")
    @Positive(message = "Number must be positive and larger than 0")
    private int startTermDate;

    @NotNull(message = "Number cannot be null")
    @Positive(message = "Number must be positive and larger than 0")
    private int endTermInterval;

    @NotNull(message = "Number cannot be null")
    @Positive(message = "Number must be positive and larger than 0")
    private int startReuploadInterval;

    @NotNull(message = "Number cannot be null")
    @Positive(message = "Number must be positive and larger than 0")
    private int endReuploadInterval;

}
