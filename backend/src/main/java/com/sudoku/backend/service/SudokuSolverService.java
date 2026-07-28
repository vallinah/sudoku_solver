package com.sudoku.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sudoku.backend.dto.Block;
import com.sudoku.backend.dto.SudokuGrid;
import com.sudoku.backend.solver.BacktrackingSolver;

@Service
public class SudokuSolverService {
    private BacktrackingSolver backtrackingSolver;

    public SudokuSolverService(){
        this.backtrackingSolver = new BacktrackingSolver();
    }

    public void getEmptySquaresOfOneNumber(SudokuGrid sudokuGrid){
        List <Block> emptySquares = backtrackingSolver.getEmptyBlocksForOneNumber(sudokuGrid, 9);
        System.out.println("emptySquares");
        for (Block block : emptySquares) {
            
            System.out.println(block.toString());
        }
        backtrackingSolver.generateCell(sudokuGrid);
        backtrackingSolver.solve(sudokuGrid);
        System.out.println(sudokuGrid);
    }
}
