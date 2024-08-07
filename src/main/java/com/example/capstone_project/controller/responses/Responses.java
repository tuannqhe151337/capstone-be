package com.example.capstone_project.controller.responses;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Responses<T> {
    private List<T> data = new ArrayList<>();
}