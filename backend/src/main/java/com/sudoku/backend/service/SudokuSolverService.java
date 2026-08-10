package com.sudoku.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sudoku.backend.dto.Cell;
import com.sudoku.backend.dto.SolveStep;
import com.sudoku.backend.dto.SudokuGrid;
import com.sudoku.backend.solver.BacktrackingSolver;
import com.sudoku.backend.solver.SudokuSolver;

@Service
public class SudokuSolverService {
    private BacktrackingSolver backtrackingSolver;
    private SudokuSolver sudokuSolver;

    public SudokuSolverService(){
        this.backtrackingSolver = new BacktrackingSolver();
        this.sudokuSolver = new SudokuSolver();
    }

    public void getEmptySquaresOfOneNumber(SudokuGrid sudokuGrid){
        sudokuSolver.initialize(sudokuGrid);
        sudokuSolver.solve(sudokuGrid);
        sudokuSolver.printGrid(sudokuGrid);
            
        for (SolveStep step : sudokuGrid.getSteps()) {
            System.out.println(step);
        }
        System.out.println("removed:");
        for (Cell removed : sudokuGrid.getRemovedFromNotes()) {
            System.out.println(removed);
        }
    }
}
