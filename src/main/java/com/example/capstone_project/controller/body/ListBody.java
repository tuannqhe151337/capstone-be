package com.example.capstone_project.controller.body;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ListBody<T> {
    private List<T> data = new ArrayList<>();
}