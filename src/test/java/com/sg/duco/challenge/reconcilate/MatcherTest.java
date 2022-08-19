package com.sg.duco.challenge.reconcilate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.contains;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

public class MatcherTest {
    private DataMatcher testTarget = new DataMatcher();

    // @Test
    // void shouldFindMatchingElementsTest() {
    //     // given two list containing same elements
    //     List<Double> sideA = List.of(54d, 11d, 28.33d, 36.2d);
    //     List<Double> sideB = List.of(111d, 36.2d, 11d, 14.4d);
    //     List<Double> expectedMatch = List.of(11d, 36.2d);

    //     // then target should returns matched elements and unmatched

    //     long startTime = System.nanoTime();
    //     ReconcilatedData reconcilated = testTarget.reconcilate(sideA, sideB);
    //     var duration = Duration.ofNanos(System.nanoTime() - startTime);

    //     System.out.println("Reconcilation took:"+duration.toMillis()+ " ms");
    //     List<Double> matches = reconcilated.matches();
    //     List<Double> unMatchA = reconcilated.unMatchA();
    //     List<Double> unMatchB = reconcilated.unMatchB();

    //     assertEquals(expectedMatch.size(), matches.size());
    //     assertEquals(2, unMatchA.size());
    //     assertEquals(2, unMatchB.size());
    // }


    // @Test
    // void shouldFindDuplicatesTest() {
    //     // given two list containing same elements
    //     List<Double> sideA = List.of(54d, 36.2d, 36.2d, 36.2d);
    //     List<Double> sideB = List.of(111d, 36.2d, 36.2d, 14.4d);
    //     List<Double> expectedMatch = List.of(36.2d, 36.2d);

    //     // then target should returns matched elements and unmatched

    //     long startTime = System.nanoTime();
    //     ReconcilatedData reconcilated = testTarget.reconcilate(sideA, sideB);
    //     var duration = Duration.ofNanos(System.nanoTime() - startTime);

    //     System.out.println("Reconcilation took:"+duration.toMillis()+ " ms");

    //     assertEquals(expectedMatch.size(),reconcilated.matches().size());
    //     assertEquals(expectedMatch.get(0),reconcilated.matches().get(0));
    //     assertEquals(expectedMatch.get(1),reconcilated.matches().get(1));

    //     assertEquals(2,reconcilated.unMatchA().size());
    //     assertTrue(reconcilated.unMatchA().containsAll(List.of(54d, 36.2d)));

    //     assertEquals(2, reconcilated.unMatchB().size());
    //     assertTrue(reconcilated.unMatchB().containsAll(List.of(111d, 14.4d)));
    // }

    @Test
    void shouldFindMatchTest() {
        List<ReconcilateTarget> sideA = List.of(
            new ReconcilateTarget(54d, 540, "M20", "EUC"),
            new ReconcilateTarget(36.2d, 110, "M20", "EUC"),
            new ReconcilateTarget(11d, 110, "DUC", "INS")
        );
        
        List<ReconcilateTarget> sideB = List.of(
            new ReconcilateTarget(54d, 540, "M20", "EUC"),
            new ReconcilateTarget(36.3d, 110, "DUC", "INS"),
            new ReconcilateTarget(11d, 110, "M20", "INS")
        );

        ReconcilatedData result = testTarget.reconcilate(sideA, sideB);

        assertEquals(1, result.matches().size());
        assertEquals(1, result.matches().size());

        assertEquals(sideA.get(0), result.matches().get(0));

        assertEquals(2, result.unMatchA().size());
        assertEquals(2, result.unMatchB().size());
        }
}

class DataMatcher {
    ReconcilatedData reconcilate(List<ReconcilateTarget> sideA, List<ReconcilateTarget> sideB) {
        List<ReconcilateTarget> matches = new ArrayList<>();
        List<ReconcilateTarget> unMatchA = new ArrayList<>();
        List<ReconcilateTarget> unMatchB = new ArrayList<>();

        Map<ReconcilateTarget, Long> mapA = sideA.stream()
            .collect(
                Collectors.groupingBy(Function.identity(), Collectors.counting())
            );

        sideB.forEach(elB -> {
            if (mapA.containsKey(elB)) {
                mapA.put(elB, mapA.get(elB) - 1L);
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

record ReconcilateTarget(Double price, Integer quantity, String ticker, String counterparty) {}