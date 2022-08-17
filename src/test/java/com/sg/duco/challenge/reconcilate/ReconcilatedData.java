package com.sg.duco.challenge.reconcilate;

import java.util.List;

public class ReconcilatedData {

    private List<Integer> matches;
    private List<Integer> unMatchA;
    private List<Integer> unMatchB;

    public ReconcilatedData(List<Integer> matches, List<Integer> unMatchA, List<Integer> unMatchB) {
        this.matches = matches;
        this.unMatchA = unMatchA;
        this.unMatchB = unMatchB;
    }

    public List<Integer> getMatches() {
        return matches;
    }

    public List<Integer> getUnmatchA() {
        return unMatchA;
    }

    public List<Integer> getUnmatchB() {
        return unMatchB;
    }

}
