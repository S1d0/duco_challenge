package com.sg.duco.challenge.reconcilate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class MatcherTest {
    private DataMatcher testTarget = new DataMatcher();

    @Test
    void shouldFindMatchingElementsTest() {
        // given two list containing same elements
        List<Integer> sideA = List.of(1, 2, 3, 4);
        List<Integer> sideB = List.of(1, 6, 7, 4, 5);
        List<Integer> expectedMatch = List.of(1, 4);

        // then target should returns matched elements and unmatched
        ReconcilatedData reconcilated = testTarget.reconcilate(sideA, sideB);
        List<Integer> matches = reconcilated.getMatches();
        List<Integer> unMatchA = reconcilated.getUnmatchA();
        List<Integer> unMatchB = reconcilated.getUnmatchB();

        assertEquals(expectedMatch.size(), matches.size());
        assertEquals(2, unMatchA.size());
        assertEquals(3, unMatchB.size());
    }
}

class DataMatcher {

    public ReconcilatedData reconcilate(List<Integer> sideA, List<Integer> sideB) {
        List<Integer> matches = new ArrayList<>();
        List<Integer> unMatchA = new ArrayList<>();
        List<Integer> unMatchB = new ArrayList<>();

        Map<Integer, Long> mapA = sideA.stream()
                .collect(
                        Collectors.groupingBy(e -> e, Collectors.counting()));

        sideB.forEach(elB -> {
            if(mapA.containsKey(elB)) {
                mapA.put(elB, mapA.get(elB) -1L);
                matches.add(elB);
            } else {
                unMatchB.add(elB);
            }
        });

        mapA.forEach((k, v) -> {
            if (v > 0) {
                unMatchA.add(k);
            }
        });

        return new ReconcilatedData(matches, unMatchA, unMatchB);
    }

}