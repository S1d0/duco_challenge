package com.sg.duco.challenge.reconcilate;

import java.util.List;

public record ReconcilatedData(List<ReconcilateTarget> matches, List<ReconcilateTarget> unMatchA, List<ReconcilateTarget> unMatchB) {}
