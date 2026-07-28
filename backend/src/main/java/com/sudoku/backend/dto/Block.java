package com.sudoku.backend.dto;

public class Block {
    int line;
    int column;
    int number;
    int startingBlockRow;
    int startingBlockCol;
    int endingBlockRow;
    int endingBlockCol;

    public Block(int line, int column, int startingBlockRow, int startingBlockCol, int endingBlockRow,
            int endingBlockCol) {
        this.line = line;
        this.column = column;
        this.startingBlockRow = startingBlockRow;
        this.startingBlockCol = startingBlockCol;
        this.endingBlockRow = endingBlockRow;
        this.endingBlockCol = endingBlockCol;
    }

    public Block(int line, int column, int number, int startingBlockRow, int startingBlockCol, int endingBlockRow,
            int endingBlockCol) {
        this.line = line;
        this.column = column;
        this.number = number;
        this.startingBlockRow = startingBlockRow;
        this.startingBlockCol = startingBlockCol;
        this.endingBlockRow = endingBlockRow;
        this.endingBlockCol = endingBlockCol;
    }

    public Block(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public Block(int line, int column, int number) {
        this.line = line;
        this.column = column;
        this.number = number;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }


    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getStartingBlockRow() {
        return startingBlockRow;
    }

    public void setStartingBlockRow(int startingBlockRow) {
        this.startingBlockRow = startingBlockRow;
    }

    public int getStartingBlockCol() {
        return startingBlockCol;
    }

    public void setStartingBlockCol(int startingBlockCol) {
        this.startingBlockCol = startingBlockCol;
    }

    public int getEndingBlockRow() {
        return endingBlockRow;
    }

    public void setEndingBlockRow(int endingBlockRow) {
        this.endingBlockRow = endingBlockRow;
    }

    public int getEndingBlockCol() {
        return endingBlockCol;
    }

    public void setEndingBlockCol(int endingBlockCol) {
        this.endingBlockCol = endingBlockCol;
    }

    @Override
    public String toString() {
        return "Block [line=" + line + ", column=" + column + ", number=" + number + ", startingBlockRow="
                + startingBlockRow + ", startingBlockCol=" + startingBlockCol + ", endingBlockRow=" + endingBlockRow
                + ", endingBlockCol=" + endingBlockCol + "]";
    }
}
