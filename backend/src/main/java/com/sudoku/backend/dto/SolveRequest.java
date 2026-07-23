package com.sudoku.backend.dto;

public class SolveRequest {

    private int[][] grid;

    public int[][] getGrid() {
        return grid;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }
}