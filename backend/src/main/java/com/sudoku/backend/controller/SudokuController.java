package com.sudoku.backend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sudoku.backend.dto.SolveRequest;
import com.sudoku.backend.dto.SolveResponse;
import com.sudoku.backend.dto.SolveStep;
import com.sudoku.backend.dto.SudokuGrid;
import com.sudoku.backend.dto.ValidationResult;
import com.sudoku.backend.exception.InvalidSudokuException;
import com.sudoku.backend.service.SudokuSolverService;
import com.sudoku.backend.service.SudokuValidator;


@RestController
@RequestMapping("/api/sudoku")
@CrossOrigin
public class SudokuController {

    private final SudokuValidator validator;
    private final SudokuSolverService sudokuSolverService;


    public SudokuController(SudokuValidator validator, SudokuSolverService sudokuSolverService) {
        this.validator = validator;
        this.sudokuSolverService = sudokuSolverService;
    }

    @PostMapping("/solve")
    public SolveResponse solve(@RequestBody SolveRequest request) {
        
        ValidationResult result =
        validator.validate(request.getGrid());
        
        
        if (!result.isValid()) {
            
            throw new InvalidSudokuException(
                result.getMessage()
            );
        }
        SudokuGrid sudokuGrid = new SudokuGrid();
        sudokuGrid.setGrid(request.getGrid());
        
        int[][] testGrid = {
            {5,3,4,6,7,8,9,1,2},
            {6,7,2,1,9,5,3,4,8},
            {1,9,8,3,4,2,5,6,7},
            {8,5,9,7,6,1,4,2,3},
            {4,2,6,8,5,3,7,9,1},
            {7,1,3,9,2,4,8,5,6},
            {9,6,1,5,3,7,2,8,4},
            {2,8,7,4,1,9,6,3,5},
            {3,4,5,2,8,6,1,7,9}
        };
            
        sudokuSolverService.getEmptySquaresOfOneNumber(sudokuGrid);

        List<SolveStep> steps = List.of(
                new SolveStep(0,2,4),
                new SolveStep(0,3,6)
        );


        return new SolveResponse(sudokuGrid.getGrid(), sudokuGrid.getSteps());
    }
}
