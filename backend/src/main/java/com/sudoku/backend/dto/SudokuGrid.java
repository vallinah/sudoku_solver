package com.sudoku.backend.dto;

import java.util.Arrays;
import java.util.List;

public class SudokuGrid {
    int[][] grid;
    List<Cell> notes;
    List<SolveStep> steps;
    List<Cell> removedFromNotes;
    public List<Cell> getRemovedFromNotes() {
        return removedFromNotes;
    }
    public void setRemovedFromNotes(List<Cell> removedFromNotes) {
        this.removedFromNotes = removedFromNotes;
    }
    public List<SolveStep> getSteps() {
        return steps;
    }
    public void setSteps(List<SolveStep> steps) {
        this.steps = steps;
    }
    public int[][] getGrid() {
        return grid;
    }
    public void setGrid(int[][] grid) {
        this.grid = grid;
    }
    public List<Cell> getNotes() {
        return notes;
    }
    public void setNotes(List<Cell> notes) {
        this.notes = notes;
    }
    public Cell getCell(int row, int col) {

        for (Cell cell : this.notes) {
            if (cell.getRow() == row && cell.getCol() == col) {
                return cell;
            }
        }
    
        return null;
    }
    @Override
    public String toString() {
        return "SudokuGrid [grid=" + Arrays.toString(grid) + ", notes=" + notes.toString() + "]";
    }
}
