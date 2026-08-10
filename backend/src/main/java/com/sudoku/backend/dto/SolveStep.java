package com.sudoku.backend.dto;

public class SolveStep {

    private int row;
    private int col;
    private int value;
    private String method;

    public SolveStep(int row, int col, int value, String method) {
        this.row = row;
        this.col = col;
        this.value = value;
        this.method = method;
    }


    public SolveStep(int row, int col, int value) {
        this.row = row;
        this.col = col;
        this.value = value;
    }


    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getValue() {
        return value;
    }


    @Override
    public String toString() {
        return "SolveStep [row=" + row + ", col=" + col + ", value=" + value + "]";
    }


    public void setRow(int row) {
        this.row = row;
    }


    public void setCol(int col) {
        this.col = col;
    }


    public void setValue(int value) {
        this.value = value;
    }


    public String getMethod() {
        return method;
    }


    public void setMethod(String method) {
        this.method = method;
    }
}
