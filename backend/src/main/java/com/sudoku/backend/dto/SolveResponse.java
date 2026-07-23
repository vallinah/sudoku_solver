package com.sudoku.backend.dto;

import java.util.List;

public class SolveResponse {

    private int[][] solvedGrid;
    private List<SolveStep> steps;


    public SolveResponse(int[][] solvedGrid, List<SolveStep> steps) {
        this.solvedGrid = solvedGrid;
        this.steps = steps;
    }


    public int[][] getSolvedGrid() {
        return solvedGrid;
    }


    public List<SolveStep> getSteps() {
        return steps;
    }
}
