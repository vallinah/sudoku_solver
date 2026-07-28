package com.sudoku.backend.dto;

import java.util.HashSet;
import java.util.Set;

public class Cell {
    private int row;
    private int col;
    private Set<Integer> candidates;
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.candidates = new HashSet<>();
    }
    public Cell(int row, int col, Set<Integer> candidates) {
        this.row = row;
        this.col = col;
        this.candidates = candidates;
    }
    public int getRow() {
        return row;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public int getCol() {
        return col;
    }
    public void setCol(int col) {
        this.col = col;
    }
    public Set<Integer> getCandidates() {
        return candidates;
    }
    public void setCandidates(Set<Integer> candidates) {
        this.candidates = candidates;
    }
    public void addCandidate(int number) {
        candidates.add(number);
    }

    public boolean hasCandidate(int number) {
        return candidates.contains(number);
    }
    @Override
    public String toString() {
        return "Cell [row=" + row + ", col=" + col + ", candidates=" + candidates + "]";
    }
}
