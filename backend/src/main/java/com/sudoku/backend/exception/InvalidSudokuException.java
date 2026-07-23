package com.sudoku.backend.exception;

public class InvalidSudokuException extends RuntimeException {

    public InvalidSudokuException(String message) {
        super(message);
    }
}
