package com.sudoku.backend.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sudoku.backend.dto.Block;
import com.sudoku.backend.dto.Cell;
import com.sudoku.backend.dto.SolveStep;
import com.sudoku.backend.dto.SudokuGrid;

public class BacktrackingSolver {

    public int getSquareNumber(int lign, int column) {
        if (lign <= 2 && column <= 2) {
            return 1;
        }
        if (lign <= 2 && column >= 3 && column <= 5) {
            return 2;
        }
        if (lign <= 2 && column >= 6 && column <= 8) {
            return 3;
        }
        // 2 eme ligne
        if (lign >= 3 && lign <= 5 && column <= 2) {
            return 4;
        }
        if (lign >= 3 && lign <= 5 && column >= 3 && column <= 5) {
            return 5;
        }
        if (lign >= 3 && lign <= 5 && column >= 6 && column <= 8) {
            return 6;
        }
        // 3 eme ligne 
        if (lign >= 6 && lign <= 8 && column <= 2) {
            return 4;
        }
        if (lign >= 6 && lign <= 8 && column >= 3 && column <= 5) {
            return 5;
        }
        if (lign >= 6 && lign <= 8 && column >= 6 && column <= 8) {
            return 6;
        }
    
        return 0;
    }

    public List<Block> getEmptyBlocksOfOneNumber(int[][] grid, int number) {
        List<Block> emptyBlocks = new ArrayList<>();
        int[] squares = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        for (int lign = 0; lign < 9; lign++) {
            for (int column = 0; column < 9; column++) {
                boolean had = false;
                int squareNumber = 0;
                if (lign <= 2 && column <= 2) {
                    squareNumber = 1;
                    if (grid[lign][column] == number) {
                        had = true;
                        break;
                    }
                }
                if (lign <= 2 && column >= 3 && column <= 5) {
                    squareNumber = 2;
                    if (grid[lign][column] == number) {
                        had = true;
                        break;
                    }
                }
                if (lign <= 2 && column >= 6 && column <= 8) {
                    squareNumber = 3;
                    if (grid[lign][column] == number) {
                        had = true;
                        break;
                    }
                }
                // 2 eme ligne
                if (lign >= 3 && lign <= 5 && column <= 2) {
                    squareNumber = 4;
                    if (grid[lign][column] == number) {
                        had = true;
                        break;
                    }
                }
                if (lign >= 3 && lign <= 5 && column >= 3 && column <= 5) {
                    squareNumber = 5;
                    if (grid[lign][column] == number) {
                        had = true;
                        break;
                    }
                }
                if (lign >= 3 && lign <= 5 && column >= 6 && column <= 8) {
                    squareNumber = 6;
                    if (grid[lign][column] == number) {
                        had = true;
                        break;
                    }
                }
                // 3 eme ligne 
                if (lign >= 6 && lign <= 8 && column <= 2) {
                    squareNumber = 4;
                    if (grid[lign][column] == number) {
                        had = true;
                        break;
                    }
                }
                if (lign >= 6 && lign <= 8 && column >= 3 && column <= 5) {
                    squareNumber = 5;
                    if (grid[lign][column] == number) {
                        had = true;
                        break;
                    }
                }
                if (lign >= 6 && lign <= 8 && column >= 6 && column <= 8) {
                    squareNumber = 6;
                    if (grid[lign][column] == number) {
                        had = true;
                        break;
                    }
                }
                if (!had) {
                    System.out.println("emptySquare:"+lign+" "+column);
                    emptyBlocks.add(new Block(lign, column, squareNumber));
                }
            }
        }
    
        return emptyBlocks;
    }

    public List<Block> takeNotes(int[][] grid, int number) {
        List<Block> emptyBlocks = new ArrayList<>();
        int[] squares = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    
        for (int startRow = 0; startRow < 9; startRow += 3) {
            for (int startCol = 0; startCol < 9; startCol += 3) {
    
                boolean had = false;
    
                for (int i = startRow; i < startRow + 3 && !had; i++) {
                    for (int j = startCol; j < startCol + 3; j++) {
                        if (grid[i][j] == number) {
                            had = true;
                            break;
                        }
                    }
                }
    
                if (!had) {
                    emptyBlocks.add(new Block(startRow, startCol));
                }
            }
        }
    
        return emptyBlocks;
    }

    public void solve(SudokuGrid sudokuGrid) {
        // generateCell(sudokuGrid);
        // sudokuGrid.setNotes(new ArrayList<>());
        boolean changeDetected = false;
        for (int i = 1; i < 10; i++) {
            List<Block> blocks = getEmptyBlocksForOneNumber(sudokuGrid, i);
            for (Block block : blocks) {
                annotedBlocks(sudokuGrid, block.getNumber(), i);
                changeDetected = checkMatchNumberAnnoted(sudokuGrid);
                if(changeDetected){
                    solve(sudokuGrid);
                }
            }
        }
    }

    public List<Block> getEmptyBlocksForOneNumber(SudokuGrid sudokuGrid, int number) {
        List<Block> emptyBlocks = new ArrayList<>();
        int blockNumber = 0;
        for (int blockRow = 0; blockRow < 3; blockRow++) {
            for (int blockCol = 0; blockCol < 3; blockCol++) {
                boolean had = false;
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
        
                        int r = blockRow * 3 + row;
                        int c = blockCol * 3 + col;
                        // System.out.print("" + r + c + " ");
                        if (sudokuGrid.getGrid()[r][c] == number) {
                            had = true;
                            // System.out.print("had the number on: [" + r +"]["+ c + "] ");
                        }
                        if(had == false && row == 2 && col == 2){
                            emptyBlocks.add(new Block(r, c, blockNumber));
                            // System.out.print("The block who doesn't have the number: [" + r +"]["+ c + "] number: "+blockNumber);
                        }
                        if(row == 2 && col == 2){
                            blockNumber ++;
                            // System.out.print("The is the end: [" + r +"]["+ c + "] number: "+blockNumber);
                        }
                        // System.out.println();
                    }
                }
            }
        }
        // System.out.println("blockNumber = " +blockNumber);
        return emptyBlocks;
    }

    public void generateCell(SudokuGrid sudokuGrid) {
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                Cell cell = new Cell(i, j);
                if (sudokuGrid.getGrid()[i][j]==0) {
                    cell.addCandidate(sudokuGrid.getGrid()[i][j]);
                }
                cells.add(cell);
            }
        }
        sudokuGrid.setNotes(cells);
        sudokuGrid.setSteps(new ArrayList<>());
    }

    public void annotedBlocks(SudokuGrid sudokuGrid, int block, int number) {
        sudokuGrid.setNotes(new ArrayList<>());
        // System.out.println("block: "+ block);

        int blockRow = block / 3;
        int blockCol = block % 3;
    
        int startRow = blockRow * 3;
        int startCol = blockCol * 3;
    
        for (int row = startRow; row < startRow + 3; row++) {
            for (int col = startCol; col < startCol + 3; col++) {
                // System.out.println("[row]:" + row+ "[col]:" + col);
                // System.out.println("sudokuGrid.getGrid()[row]" +sudokuGrid.getGrid()[row][col]);
                if (sudokuGrid.getGrid()[row][col] == 0 && verifiedCol(sudokuGrid, col, number) && verifiedRow(sudokuGrid, row, number)) {
                    if (sudokuGrid.getCell(row, col) != null) {
                        sudokuGrid.getCell(row, col).addCandidate(number);
                    }else{
                        Cell cell = new Cell(row, col);
                        cell.addCandidate(number);
                        sudokuGrid.getNotes().add(cell);
                    }
                }
            }
        }
    
    }

    public boolean checkMatchNumberAnnoted(SudokuGrid sudokuGrid) {
        Map<Integer, Integer> count = new HashMap<>();

        Map<Integer, Cell> positions = new HashMap<>();

        for (Cell cell : sudokuGrid.getNotes()) {
            for (Integer number : cell.getCandidates()) {

                count.put(number, count.getOrDefault(number, 0) + 1);
                positions.put(number, cell);
            }
        }

        for (Integer number : count.keySet()) {

            if (count.get(number) == 1) {
                int[][] grid = sudokuGrid.getGrid();
                grid[positions.get(number).getRow()][positions.get(number).getCol()]= number;
                sudokuGrid.setGrid(grid);  
                List<SolveStep> steps = sudokuGrid.getSteps();
                System.out.print("change detected");
                steps.add(new SolveStep(positions.get(number).getRow(), positions.get(number).getCol(), number));
                return true;             
            }
        }
        return  false;
    
    }

    public boolean  verifiedRow(SudokuGrid sudokuGrid, int row, int number){
        for (int i = 0; i < 9; i++) {
            if (sudokuGrid.getGrid()[row][i] == number) {
                return false;
            }
        }
        return true;
    }

    public boolean  verifiedCol(SudokuGrid sudokuGrid, int col, int number){
        for (int i = 0; i < 9; i++) {
            if (sudokuGrid.getGrid()[i][col] == number) {
                return false;
            }
        }
        return true;
    }

    public List<Block> findTheEmptyLineForNumber(int[][] grid, int number){
        boolean had = false;
        List<Block> emptyLines = new ArrayList<>();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(grid[i][j] == number){
                    had = true;
                }
                if(!had && j == 8){
                    emptyLines.add(new Block(i, j));
                }
            }
            had = false;
        }
        return emptyLines;
    }

    public List<Block> findTheEmptyColumnForNumber(int[][] grid, int number){
        boolean had = false;
        List<Block> emptyColumns = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(grid[i][j] == number){
                    had = true;
                }
                if(!had && j == 8){
                    emptyColumns.add(new Block(i, j));
                }
            }
            had = false;
        }
        return emptyColumns;
    }
}
