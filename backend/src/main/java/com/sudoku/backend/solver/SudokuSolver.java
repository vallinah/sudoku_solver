package com.sudoku.backend.solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sudoku.backend.dto.Block;
import com.sudoku.backend.dto.Cell;
import com.sudoku.backend.dto.SolveStep;
import com.sudoku.backend.dto.SudokuGrid;

public class SudokuSolver {

    public void initialize(SudokuGrid sudokuGrid){
        sudokuGrid.setSteps(new ArrayList<>());
        sudokuGrid.setRemovedFromNotes(new ArrayList<>());
        sudokuGrid.setNotes(new ArrayList<>());
    }

    public void generateCell(SudokuGrid sudokuGrid){
        List<Cell> cells = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(sudokuGrid.getGrid()[i][j] == 0){
                    Cell cell = new Cell(i, j);
                    cells.add(cell);
                }
            }
        }
        sudokuGrid.setNotes(cells);
    }

    public List<Block> getEmptyBlocksOfOneNumber(SudokuGrid sudokuGrid, int number){
        List<Block> emptyBlocks = new ArrayList<>();
        int blockNumber = 0;
        for (int blockRow = 0; blockRow < 3; blockRow++) {
            for (int blockCol = 0; blockCol < 3; blockCol++) {
                boolean had = false;
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
        
                        int r = blockRow * 3 + row;
                        int c = blockCol * 3 + col;
                        if (sudokuGrid.getGrid()[r][c] == number) {
                            had = true;
                        }
                        if(had == false && row == 2 && col == 2){
                            emptyBlocks.add(new Block(r, c, blockNumber));
                        }
                        if(row == 2 && col == 2){
                            blockNumber ++;
                        }
                    }
                }
            }
        }
        return emptyBlocks;
    }

    public boolean  verifiedRow(SudokuGrid sudokuGrid, int row, int number){
        for (int i = 0; i < 9; i++) {
            if (sudokuGrid.getGrid()[row][i] == number) {
                return false;
            }
        }
        return true;
    }

    public boolean  verifiedColumn(SudokuGrid sudokuGrid, int col, int number){
        for (int i = 0; i < 9; i++) {
            if (sudokuGrid.getGrid()[i][col] == number) {
                return false;
            }
        }
        return true;
    }

    public void annotedEmptyBlockOfNumber(SudokuGrid sudokuGrid, int blockNumber, int number){

        // generateCell(sudokuGrid);
        sudokuGrid.setNotes(new ArrayList<>());

        int blockRow = blockNumber / 3;
        int blockCol = blockNumber % 3;
    
        int startRow = blockRow * 3;
        int startCol = blockCol * 3;
    
        for (int row = startRow; row < startRow + 3; row++) {
            for (int col = startCol; col < startCol + 3; col++) {
                if (verifiedRemovedNote(sudokuGrid, row, col, number) && sudokuGrid.getGrid()[row][col] == 0 && verifiedColumn(sudokuGrid, col, number) && verifiedRow(sudokuGrid, row, number) && !theBlockHasTheNumber(sudokuGrid, row, col, number)) {
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

    public void annotedAllEmptyBlockOfNumber(SudokuGrid sudokuGrid, List<Block> blocks, int number){
        sudokuGrid.setNotes(new ArrayList<>());
        for (Block block : blocks) {
            // generateCell(sudokuGrid);
    
            int blockRow = block.getNumber() / 3;
            int blockCol = block.getNumber() % 3;
        
            int startRow = blockRow * 3;
            int startCol = blockCol * 3;
        
            for (int row = startRow; row < startRow + 3; row++) {
                for (int col = startCol; col < startCol + 3; col++) {
                    if (verifiedRemovedNote(sudokuGrid, row, col, number) && sudokuGrid.getGrid()[row][col] == 0 && verifiedColumn(sudokuGrid, col, number) && verifiedRow(sudokuGrid, row, number)) {
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

    }


    public boolean checkSameRowBlockOfNumberAnnoted(SudokuGrid sudokuGrid, int blockNumber, int number){

        boolean change = false;

        int blockRow = blockNumber / 3;
    
        int startRow = blockRow * 3;

        List<Cell> cellToCheck = new ArrayList<>();

        for(Cell cell : sudokuGrid.getNotes()){
            if (isInTheBlock(blockNumber, cell.getRow(), cell.getCol()) && cell.hasCandidate(number)) {
                cellToCheck.add(cell);
            }
        }
        
        for(int row = startRow; row < startRow + 3; row++){
            int nb = 0;
            for(Cell cell : cellToCheck){
                if(row == cell.getRow()){
                    nb ++;
                }
            }
            if(nb == cellToCheck.size()){
                if(removeFromRowNotInTheSameBlock(sudokuGrid, number, row, blockNumber) || checkIfMatchForNumberAnnoted(sudokuGrid, number)){
                    change = true;
                }
            }
        }
    
        return change;
    }

    public boolean checkSameColBlockOfNumberAnnoted(SudokuGrid sudokuGrid, int blockNumber, int number){

        boolean change = false;

        int blockRow = blockNumber / 3;
        int blockCol = blockNumber % 3;
    
        int startRow = blockRow * 3;
        int startCol = blockCol * 3;

        List<Cell> cellToCheck = new ArrayList<>();

        for(Cell cell : sudokuGrid.getNotes()){
            if (cell.getRow() >= startRow && cell.getRow() < startRow + 3 && cell.getCol() >= startCol && cell.getCol() < startCol + 3 && cell.hasCandidate(number)) {
                cellToCheck.add(cell);
            }
        }
        
        for(int col = startCol; col < startCol + 3; col++){
            int nb = 0;
            for(Cell cell : cellToCheck){
                if(col == cell.getCol()){
                    nb ++;
                }
            }
            if(nb == cellToCheck.size()){
                if(removeFromColumnNotInTheSameBlock(sudokuGrid, number, col, blockNumber) || checkIfMatchForNumberAnnoted(sudokuGrid, number)){
                    change = true;
                }
            }
        }
    
        return change;
    }

    public boolean checkIfMatchForNumberAnnoted(SudokuGrid sudokuGrid, int number){

        boolean changed = false;
        for (int i = 0; i < 9; i++) {
    
            List<Cell> cellToCheck = new ArrayList<>();
    
            for(Cell cell : sudokuGrid.getNotes()){
                if (isInTheBlock(i, cell.getRow(), cell.getCol()) && cell.hasCandidate(number)) {
                    cellToCheck.add(cell);
                }
            }
            
            
            if (cellToCheck.size() == 1 && verifiedRow(sudokuGrid, cellToCheck.get(0).getRow(), number) && verifiedColumn(sudokuGrid, cellToCheck.get(0).getCol(), number) && !theBlockHasTheNumber(sudokuGrid, cellToCheck.get(0).getRow(), cellToCheck.get(0).getCol(), number)) {
                int[][] grid = sudokuGrid.getGrid();
                grid[cellToCheck.get(0).getRow()][cellToCheck.get(0).getCol()]= number;
                sudokuGrid.setGrid(grid);  
                sudokuGrid.getNotes().remove(cellToCheck.get(0));
                List<SolveStep> steps = sudokuGrid.getSteps();
                steps.add(new SolveStep(cellToCheck.get(0).getRow(), cellToCheck.get(0).getCol(), number, sudokuGrid.getMethod()));
                changed = true;
            }        
        }
    
        return changed;
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

            if (count.get(number) == 1 && verifiedRow(sudokuGrid, positions.get(number).getRow(), number) && verifiedColumn(sudokuGrid, positions.get(number).getCol(), number) && !theBlockHasTheNumber(sudokuGrid, positions.get(number).getRow(), positions.get(number).getCol(), number)) {
                int[][] grid = sudokuGrid.getGrid();
                grid[positions.get(number).getRow()][positions.get(number).getCol()]= number;
                sudokuGrid.setGrid(grid);  
                sudokuGrid.getNotes().remove(positions.get(number));
                List<SolveStep> steps = sudokuGrid.getSteps();
                steps.add(new SolveStep(positions.get(number).getRow(), positions.get(number).getCol(), number, sudokuGrid.getMethod()));
                return true;             
            }
        }
        return  false;
    
    }


    private boolean isInTheBlock(int blockNumber, int row, int col){
        int blockRow = blockNumber / 3;
        int blockCol = blockNumber % 3;
    
        int startRow = blockRow * 3;
        int startCol = blockCol * 3;

        if (row >= startRow && row < startRow + 3 && col >= startCol && col < startCol + 3) {
            return true;
        }

        return false;
    }

    private boolean removeFromRowNotInTheSameBlock(SudokuGrid sudokuGrid, int number, int row, int block) {
        
        boolean changed = false;
    
        for (Cell cell : sudokuGrid.getNotes()) {

            if(cell.getRow() == row && !isInTheBlock(block, cell.getRow(), cell.getCol()) && cell.hasCandidate(number)) {
    
    
                if(verifiedRemovedNote(sudokuGrid, cell.getRow(), cell.getCol(), number)) {
                    cell.removeCandidate(number);
                    changed = true;
                    Cell removedCell = new Cell(cell.getRow(), cell.getCol());
                    removedCell.addCandidate(number);
                    sudokuGrid.getRemovedFromNotes().add(removedCell);
                }
            }
        }
    
        return changed;
    }

    private boolean addingMissingNumberInRow(SudokuGrid sudokuGrid, int row, int col){
        for(int number = 1; number < 10; number ++){
            boolean hasTheNumber = false;
            for (int j = 0; j < 9; j++) {
                if(sudokuGrid.getGrid()[row][j] == number){
                    hasTheNumber = true;
                    break;
                }
            }
            if(hasTheNumber == false && verifiedRow(sudokuGrid, row, number) && verifiedColumn(sudokuGrid, col, number) && !theBlockHasTheNumber(sudokuGrid, row, col, number)){
                int[][] grid = sudokuGrid.getGrid();
                grid[row][col]= number;
                sudokuGrid.setGrid(grid);
                System.out.println("addingMissingNumber: ["+row+"]["+col+"]="+number);
                List<SolveStep> steps = sudokuGrid.getSteps();
                steps.add(new SolveStep(row, col, number, sudokuGrid.getMethod()));
                return true; 
            }
        }
        return false;
    }

    private boolean addingMissingNumberInColumn(SudokuGrid sudokuGrid, int row,int col){
        for(int number = 1; number < 10; number ++){
            boolean hasTheNumber = false;
            for (int i = 0; i < 9; i++) {
                if(sudokuGrid.getGrid()[i][col] == number){
                    hasTheNumber = true;
                    break;
                }
            }
            if(hasTheNumber == false && verifiedRow(sudokuGrid, row, number) && verifiedColumn(sudokuGrid, col, number) && !theBlockHasTheNumber(sudokuGrid, row, col, number)){
                int[][] grid = sudokuGrid.getGrid();
                grid[row][col]= number;
                sudokuGrid.setGrid(grid);
                System.out.println("addingMissingNumberInColumn: ["+row+"]["+col+"]="+number);
                List<SolveStep> steps = sudokuGrid.getSteps();
                steps.add(new SolveStep(row, col, number, sudokuGrid.getMethod()));
                return true; 
            }
        }
        return false;
    }

    private boolean checkfEmptyOfOneNumber(SudokuGrid sudokuGrid){
        boolean change = false;
        int row = 0;
        int col = 0;
        for (int i = 0; i < 9; i++) {
            int nbFull = 0;
            for (int j = 0; j < 9; j++) {
                if(sudokuGrid.getGrid()[i][j] != 0){
                    nbFull ++;
                }
                if(sudokuGrid.getGrid()[i][j] == 0){
                    col = j;
                }
            }
            if(nbFull == 8){
                if (addingMissingNumberInRow(sudokuGrid, i, col)) {
                    change = true;
                }
            }
        }

        for (int i = 0; i < 9; i++) {
            int nbFull = 0;
            for (int j = 0; j < 9; j++) {
                if(sudokuGrid.getGrid()[j][i] != 0){
                    nbFull ++;
                }
                if(sudokuGrid.getGrid()[j][i] == 0){
                    row = j;
                }
            }
            if(nbFull == 8){
                if (addingMissingNumberInColumn(sudokuGrid, row, i)) {
                    change = true;
                }
            }
        }
        return change;
    }

    private boolean removeFromColumnNotInTheSameBlock(SudokuGrid sudokuGrid, int number, int col, int block) {

        boolean changed = false;
    
        for (Cell cell : sudokuGrid.getNotes()) {
    
            if(cell.getCol() == col  && cell.hasCandidate(number)) {
                // System.out.println(isInTheBlock(block, cell.getRow(), cell.getCol()));
                if (!isInTheBlock(block, cell.getRow(), cell.getCol())) {
                    
                    if(verifiedRemovedNote(sudokuGrid, cell.getRow(), cell.getCol(), number)) {

                        cell.removeCandidate(number);
                        changed = true;
                        Cell removedCell = new Cell(cell.getRow(), cell.getCol());
                        removedCell.addCandidate(number);
                        sudokuGrid.getRemovedFromNotes().add(removedCell);
                    }
                }
                
    
            }
        }
    
        return changed;
    }

    public boolean  verifiedRemovedNote(SudokuGrid sudokuGrid, int row, int col, int number){
        for(Cell cell : sudokuGrid.getRemovedFromNotes()){
            if (cell.getRow() == row && cell.getCol() == col && cell.hasCandidate(number)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasEmptyCase(SudokuGrid sudokuGrid){
        for (int row = 0; row < 9; row++) {
            for (int column = 0; column < 9; column++) {
                if(sudokuGrid.getGrid()[row][column] == 0){
                    return true;
                }
            }
        }
        return false;
    }

    public int getBlockNumber(int row, int col) {
        return (row / 3) * 3 + (col / 3);
    }

    public boolean theRowHasTheNumber(SudokuGrid sudokuGrid, int row, int number){
        for (int col = 0; col < 9; col++) {
            if (sudokuGrid.getGrid()[row][col]==number) {
                return true;
            }
        }
        return false;
    }

    public boolean theColumnHasTheNumber(SudokuGrid sudokuGrid, int col, int number){
        for (int row = 0; row < 9; row++) {
            if (sudokuGrid.getGrid()[row][col]==number) {
                return true;
            }
        }
        return false;
    }

    public boolean theBlockHasTheNumber(SudokuGrid sudokuGrid, int row, int col, int number){
        int blockNumber = getBlockNumber(row, col);
        int blockRow = blockNumber / 3;
        int blockCol = blockNumber % 3;
    
        int startRow = blockRow * 3;
        int startCol = blockCol * 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (sudokuGrid.getGrid()[i][j] == number) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean annotedEmptyRow(SudokuGrid sudokuGrid){
        boolean changed = false;
        sudokuGrid.setNotes(new ArrayList<>());
        for (int i = 0; i < 9; i++) {
            for (int number = 1; number < 10; number++) {
                if (!theRowHasTheNumber(sudokuGrid, i, number)) {
                    for (int j = 0; j < 9; j++) {
                        if (sudokuGrid.getGrid()[i][j] == 0 && !theBlockHasTheNumber(sudokuGrid, i, j, number) && verifiedColumn(sudokuGrid, j, number)) {
                            if (sudokuGrid.getCell(i, j) != null) {
                                sudokuGrid.getCell(i, j).addCandidate(number);
                            }else{
                                Cell cell = new Cell(i, j);
                                cell.addCandidate(number);
                                sudokuGrid.getNotes().add(cell);
                            }
                        }
                    }
                }
            }
            if (checkIfMatchForNumberAnnotedInRow(sudokuGrid, i) || checkIfSupposedMatchForNumberAnnotedInRow(sudokuGrid, i)) {
                System.out.println("one match detected in row");
                changed = true;
            }
        }
        return changed;
    }

    public boolean annotedEmptyColumn(SudokuGrid sudokuGrid){
        boolean changed = false;
        sudokuGrid.setNotes(new ArrayList<>());
        for (int i = 0; i < 9; i++) {
            for (int number = 1; number < 10; number++) {
                if (!theColumnHasTheNumber(sudokuGrid, i, number)) {
                    for (int j = 0; j < 9; j++) {
                        if (sudokuGrid.getGrid()[j][i] == 0 && !theBlockHasTheNumber(sudokuGrid, j, i, number) && verifiedRow(sudokuGrid, j, number)) {
                            if (sudokuGrid.getCell(j, i) != null) {
                                sudokuGrid.getCell(j, i).addCandidate(number);
                            }else{
                                Cell cell = new Cell(j, i);
                                cell.addCandidate(number);
                                sudokuGrid.getNotes().add(cell);
                            }
                        }
                    }
                }
            }
            if (checkIfMatchForNumberAnnotedInColumn(sudokuGrid, i) || checkIfSupposedMatchForNumberAnnotedInColumn(sudokuGrid, i)) {
                System.out.println("one match detected in col");
                changed = true;
            }
        }
        return changed;
    }

    public boolean annotedEmptyBlock(SudokuGrid sudokuGrid){

        boolean changed = false;
        // generateCell(sudokuGrid);
        sudokuGrid.setNotes(new ArrayList<>());
        for(int blockNumber = 0; blockNumber<9; blockNumber++){

            for(int number = 1; number < 10; number++){
                int blockRow = blockNumber / 3;
                int blockCol = blockNumber % 3;
            
                int startRow = blockRow * 3;
                int startCol = blockCol * 3;
            
                for (int row = startRow; row < startRow + 3; row++) {
                    for (int col = startCol; col < startCol + 3; col++) {
                        if (verifiedRemovedNote(sudokuGrid, row, col, number) && sudokuGrid.getGrid()[row][col] == 0 && verifiedColumn(sudokuGrid, col, number) && verifiedRow(sudokuGrid, row, number) && !theBlockHasTheNumber(sudokuGrid, row, col, number)) {
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
            if(checkIfSupposedMatchForNumberAnnotedInBlock(sudokuGrid, blockNumber)){
                changed = true;
            }
        }
        return changed;

    }

    public boolean checkIfMatchForNumberAnnotedInRow(SudokuGrid sudokuGrid, int row){

        boolean changed = false;
        for (int number = 0; number < 10; number++) {
    
            List<Cell> cellToCheck = new ArrayList<>();
    
            for(Cell cell : sudokuGrid.getNotes()){
                if (cell.getRow() == row && cell.hasCandidate(number)) {
                    cellToCheck.add(cell);
                }
            }
            
            
            if (cellToCheck.size() == 1 && verifiedRow(sudokuGrid, cellToCheck.get(0).getRow(), number) && verifiedColumn(sudokuGrid, cellToCheck.get(0).getCol(), number) && !theBlockHasTheNumber(sudokuGrid, cellToCheck.get(0).getRow(), cellToCheck.get(0).getCol(), number)) {
                int[][] grid = sudokuGrid.getGrid();
                grid[cellToCheck.get(0).getRow()][cellToCheck.get(0).getCol()]= number;
                sudokuGrid.setGrid(grid);  
                sudokuGrid.getNotes().remove(cellToCheck.get(0));
                List<SolveStep> steps = sudokuGrid.getSteps();
                steps.add(new SolveStep(cellToCheck.get(0).getRow(), cellToCheck.get(0).getCol(), number, sudokuGrid.getMethod()));
                changed = true;
            }        
        }
    
        return changed;
    }

    public int countEmptyCaseInARow(SudokuGrid sudokuGrid, int row){
        int nb = 0;
        for (int column = 0; column < 9; column++) {
            if(sudokuGrid.getGrid()[row][column] == 0){
                nb++;
            }
        }
        return nb;
    }

    public int countEmptyCaseInColumn(SudokuGrid sudokuGrid, int column){
        int nb = 0;
        for (int row = 0; row < 9; row++) {
            if(sudokuGrid.getGrid()[row][column] == 0){
                nb ++;
            }
        }
        return nb;
    }

    public boolean checkIfSupposedMatchForNumberAnnotedInRow(SudokuGrid sudokuGrid, int row){

        boolean changed = false;
        for (int number = 0; number < 10; number++) {
    
            List<Cell> cellToCheck = new ArrayList<>();
    
            for(Cell cell : sudokuGrid.getNotes()){
                if (cell.getRow() == row && cell.hasCandidate(number)) {
                    cellToCheck.add(cell);
                }
            }

            for(Cell ctc : cellToCheck){
                if (ctc.getCandidates().size() == 1 && verifiedRow(sudokuGrid, ctc.getRow(), number) && verifiedColumn(sudokuGrid, ctc.getCol(), number)  && !theBlockHasTheNumber(sudokuGrid, ctc.getRow(), ctc.getCol(), number)) {
                    int[][] grid = sudokuGrid.getGrid();
                    grid[ctc.getRow()][ctc.getCol()]= number;
                    sudokuGrid.setGrid(grid);  
                    sudokuGrid.getNotes().remove(ctc);
                    List<SolveStep> steps = sudokuGrid.getSteps();
                    steps.add(new SolveStep(ctc.getRow(), ctc.getCol(), number, sudokuGrid.getMethod()));
                    changed = true;
                }      
            }
            
            
        }
    
        return changed;
    }

    public boolean checkIfSupposedMatchForNumberAnnotedInBlock(SudokuGrid sudokuGrid, int blockNumber){

        boolean changed = false;
        for (int number = 0; number < 10; number++) {
    
            List<Cell> cellToCheck = new ArrayList<>();
    
            for(Cell cell : sudokuGrid.getNotes()){
                if (isInTheBlock(blockNumber, cell.getRow(), cell.getCol()) && cell.hasCandidate(number)) {
                    cellToCheck.add(cell);
                }
            }

            for(Cell ctc : cellToCheck){
                if (ctc.getCandidates().size() == 1 && verifiedRow(sudokuGrid, ctc.getRow(), number) && verifiedColumn(sudokuGrid, ctc.getCol(), number) && !theBlockHasTheNumber(sudokuGrid, ctc.getRow(), ctc.getCol(), number)) {
                    int[][] grid = sudokuGrid.getGrid();
                    grid[ctc.getRow()][ctc.getCol()]= number;
                    sudokuGrid.setGrid(grid);  
                    sudokuGrid.getNotes().remove(ctc);
                    List<SolveStep> steps = sudokuGrid.getSteps();
                    steps.add(new SolveStep(ctc.getRow(), ctc.getCol(), number, sudokuGrid.getMethod()));
                    changed = true;
                }      
            }
            
            
        }
    
        return changed;
    }

    

    public boolean checkIfMatchForNumberAnnotedInColumn(SudokuGrid sudokuGrid, int col){

        boolean changed = false;
        for (int number = 0; number < 10; number++) {
    
            List<Cell> cellToCheck = new ArrayList<>();
    
            for(Cell cell : sudokuGrid.getNotes()){
                if (cell.getCol() == col && cell.hasCandidate(number)) {
                    cellToCheck.add(cell);
                }
            }
            
            
            if (cellToCheck.size() == 1 && verifiedRow(sudokuGrid, cellToCheck.get(0).getRow(), number) && verifiedColumn(sudokuGrid, cellToCheck.get(0).getCol(), number) && !theBlockHasTheNumber(sudokuGrid, cellToCheck.get(0).getRow(), cellToCheck.get(0).getCol(), number)) {
                int[][] grid = sudokuGrid.getGrid();
                grid[cellToCheck.get(0).getRow()][cellToCheck.get(0).getCol()]= number;
                sudokuGrid.setGrid(grid);  
                sudokuGrid.getNotes().remove(cellToCheck.get(0));
                List<SolveStep> steps = sudokuGrid.getSteps();
                steps.add(new SolveStep(cellToCheck.get(0).getRow(), cellToCheck.get(0).getCol(), number, sudokuGrid.getMethod()));
                changed = true;
            }        
        }
    
        return changed;
    }

    public boolean checkIfSupposedMatchForNumberAnnotedInColumn(SudokuGrid sudokuGrid, int col){

        boolean changed = false;
        for (int number = 0; number < 10; number++) {
    
            List<Cell> cellToCheck = new ArrayList<>();
    
            for(Cell cell : sudokuGrid.getNotes()){
                if (cell.getCol() == col && cell.hasCandidate(number)) {
                    cellToCheck.add(cell);
                }
            }
            
            for(Cell ctc : cellToCheck){
                if (ctc.getCandidates().size() == 1 && verifiedRow(sudokuGrid, ctc.getRow(), number) && verifiedColumn(sudokuGrid, ctc.getCol(), number) && !theBlockHasTheNumber(sudokuGrid, ctc.getRow(), ctc.getCol(), number)) {
                    int[][] grid = sudokuGrid.getGrid();
                    grid[ctc.getRow()][ctc.getCol()]= number;
                    sudokuGrid.setGrid(grid);  
                    sudokuGrid.getNotes().remove(ctc);
                    List<SolveStep> steps = sudokuGrid.getSteps();
                    steps.add(new SolveStep(ctc.getRow(), ctc.getCol(), number, sudokuGrid.getMethod()));
                    changed = true;
                }     
            }
            
            
        }
    
        return changed;
    }

    public void printGrid(SudokuGrid sudokuGrid) {

        System.out.println("========== GRID ==========");
    
        for (int row = 0; row < 9; row++) {
    
            for (int col = 0; col < 9; col++) {
                System.out.print(
                    sudokuGrid.getGrid()[row][col] + " "
                );
            }
    
            System.out.println();
        }
    
        System.out.println("==========================");
    }

    public void solve(SudokuGrid sudokuGrid){
        System.out.println("Solving....");
        System.out.println("Solving by solveByElimination....");
        boolean changeDetected = false;
        sudokuGrid.setMethod("Annotation des blocs vides par nombre");
        for (int number = 1; number < 10; number++) {
            List<Block> blocks = getEmptyBlocksOfOneNumber(sudokuGrid, number);
            for (Block block : blocks) {
                annotedEmptyBlockOfNumber(sudokuGrid, block.getNumber(), number);
                changeDetected = checkMatchNumberAnnoted(sudokuGrid);
                if(changeDetected){
                    solve(sudokuGrid);
                }
            }

        }
        System.out.println("Solving by solveBySupposition....");

        changeDetected = false;

        sudokuGrid.setMethod("Suppression des annotations par supposition");

        for (int number = 1; number < 10; number++) {
            List<Block> blocks = getEmptyBlocksOfOneNumber(sudokuGrid, number);
            annotedAllEmptyBlockOfNumber(sudokuGrid, blocks, number);
            for (Block block : blocks) {
                if(checkSameRowBlockOfNumberAnnoted(sudokuGrid, block.getNumber(), number) || checkSameColBlockOfNumberAnnoted(sudokuGrid, block.getNumber(), number)){
                    changeDetected = checkIfMatchForNumberAnnoted(sudokuGrid, number);
                    if(changeDetected){
                        solve(sudokuGrid);
                    }
                }
            }
        }

        sudokuGrid.setMethod("Annotation des lignes vides");

        if (annotedEmptyRow(sudokuGrid) || annotedEmptyColumn(sudokuGrid) || annotedEmptyBlock(sudokuGrid)) {
            solve(sudokuGrid);
        }

        sudokuGrid.setMethod("Annotation des colonnes vides");

        if (annotedEmptyColumn(sudokuGrid)) {
            solve(sudokuGrid);
        }

        sudokuGrid.setMethod("Annotation des blocs vides");

        if (annotedEmptyBlock(sudokuGrid)) {
            solve(sudokuGrid);
        }

        sudokuGrid.setMethod("Annotation des cases vides");

        if (checkfEmptyOfOneNumber(sudokuGrid)) {
            solve(sudokuGrid);
        }
        System.out.println("============================");
        System.out.println("Finished");
    }
}
