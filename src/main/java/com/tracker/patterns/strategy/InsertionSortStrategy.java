package com.tracker.patterns.strategy;

import java.util.Comparator;
import java.util.List;

public class InsertionSortStrategy<T> implements SortStrategy<T> {
    @Override
    public void sort(List<T> list, Comparator<T> comparator) {
        int n = list.size();
        for (int i = 1; i < n; i++) {
            T key = list.get(i);
            int j = i - 1;

            while (j >= 0 && comparator.compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }
}
