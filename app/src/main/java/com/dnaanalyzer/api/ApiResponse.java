package com.dnaanalyzer.api;

import java.util.ArrayList;
import java.util.List;

public class ApiResponse {
    private List<String> Chromosome;
    private List<Integer> Position;
    private List<String> Result;
    private List<String> Rsid;

    public String toString(int index) {
        return Rsid.get(index) + " " +
               Chromosome.get(index) + " " +
               Position.get(index) + " " +
               Result.get(index);
    }

    public int getSize() {
        return Chromosome.size();
    }

    public List<String> toStringList(int maxCount) {
        int count = getSize();
        List<String> res = new ArrayList<>(count);
        for (int i = 0; i < count && i < maxCount; i++) {
            res.add(toString(i));
        }
        return res;
    }
}
