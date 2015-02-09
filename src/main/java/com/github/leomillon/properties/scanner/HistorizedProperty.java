package com.github.leomillon.properties.scanner;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import static java.util.Objects.*;

public class HistorizedProperty implements Property {

    public enum State {
        UNIQUE,
        OVERRIDEN,
        DUPLICATED
    }

    @Nonnull
    private final String key;
    @Nonnull
    private final Map<PropFile, String> values;
    @Nonnull
    private final List<PropFile> filesOrder;

    public HistorizedProperty(@Nonnull String key, @Nonnull Map<PropFile, String> values,
                              @Nonnull List<PropFile> filesOrder) {
        this.key = requireNonNull(key);
        this.values = requireNonNull(values);
        this.filesOrder = requireNonNull(filesOrder);
    }

    @Override
    @Nonnull
    public String getKey() {
        return key;
    }

    @Nonnull
    public TreeMap<PropFile, String> getHistory() {
        TreeMap<PropFile, String> orderedHistoryMap = new TreeMap<>(fileOrderComparator());
        for (Map.Entry<PropFile, String> propEntry : values.entrySet()) {
            orderedHistoryMap.put(propEntry.getKey(), propEntry.getValue());
        }
        return orderedHistoryMap;
    }

    private Comparator<PropFile> fileOrderComparator() {
        return (o1, o2) -> Integer.compare(filesOrder.indexOf(o1), filesOrder.indexOf(o2));
    }

    public State getState() {
        if (values.size() == 1) {
            return State.UNIQUE;
        }
        for (Map.Entry<PropFile, String> currentHistoryEntry : values.entrySet()) {
            for (Map.Entry<PropFile, String> historyEntry : values.entrySet()) {
                if (hasDuplicatedValue(currentHistoryEntry, historyEntry)) {
                    return State.DUPLICATED;
                }
            }
        }
        return State.OVERRIDEN;
    }

    private static boolean hasDuplicatedValue(Map.Entry<PropFile, String> firstHistoryEntry,
                                              Map.Entry<PropFile, String> secondHistoryEntry) {
        return !firstHistoryEntry.getKey().equals(secondHistoryEntry.getKey())
                && Objects.equals(firstHistoryEntry.getValue(), secondHistoryEntry.getValue());
    }

    @Nullable
    public String getLastValue() {
        return getHistory().lastEntry().getValue();
    }
}
