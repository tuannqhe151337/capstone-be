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
    // https://vladmihalcea.com/postgresql-serial-column-hibernate-identity/
    // https://stackoverflow.com/questions/17780394/hibernate-identity-vs-sequence-entity-identifier-generators
    // GenerationType.IDENTITY will disable batch insert!!!
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
