package com.example.capstone_project.utils.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RemoveDuplicateHelper {
    public static List<Long> removeDuplicates(List<Long> inputList) {
        if (inputList == null) {
            return new ArrayList<>();
        }
        Set<Long> uniqueElements = new HashSet<>(inputList);
        return new ArrayList<>(uniqueElements);
    }
}
