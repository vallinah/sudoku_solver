package com.sudoku.backend.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.sudoku.backend.dto.ValidationResult;

@Service
public class SudokuValidator {


    public ValidationResult validate(int[][] grid) {

        ValidationResult sizeResult = checkSize(grid);

        if (!sizeResult.isValid()) {
            return sizeResult;
        }


        ValidationResult rowResult = checkRows(grid);

        if (!rowResult.isValid()) {
            return rowResult;
        }


        ValidationResult columnResult = checkColumns(grid);

        if (!columnResult.isValid()) {
            return columnResult;
        }


        return checkBlocks(grid);
    }



    private ValidationResult checkSize(int[][] grid) {

        if (grid == null) {
            return new ValidationResult(
                false,
                "La grille ne peut pas être vide"
            );
        }


        if (grid.length != 9) {
            return new ValidationResult(
                false,
                "La grille doit contenir exactement 9 lignes"
            );
        }


        for (int i = 0; i < 9; i++) {

            if (grid[i].length != 9) {
                return new ValidationResult(
                    false,
                    "Chaque ligne doit contenir exactement 9 cases"
                );
            }
        }


        return new ValidationResult(true, "OK");
    }



    private ValidationResult checkRows(int[][] grid) {

        for (int i = 0; i < 9; i++) {

            Set<Integer> numbers = new HashSet<>();

            for (int j = 0; j < 9; j++) {

                int value = grid[i][j];

                if (value != 0 && !numbers.add(value)) {

                    return new ValidationResult(
                        false,
                        "Doublon détecté dans la ligne " + (i + 1)
                        + " : le chiffre " + value + " existe déjà"
                    );
                }
            }
        }

        return new ValidationResult(true, "OK");
    }



    private ValidationResult checkColumns(int[][] grid) {

        for (int col = 0; col < 9; col++) {

            Set<Integer> numbers = new HashSet<>();

            for (int row = 0; row < 9; row++) {

                int value = grid[row][col];

                if (value != 0 && !numbers.add(value)) {

                    return new ValidationResult(
                        false,
                        "Doublon détecté dans la colonne " 
                        + (col + 1)
                        + " : le chiffre " + value + " existe déjà"
                    );
                }
            }
        }

        return new ValidationResult(true, "OK");
    }



    private ValidationResult checkBlocks(int[][] grid) {

        for (int blockRow = 0; blockRow < 9; blockRow += 3) {

            for (int blockCol = 0; blockCol < 9; blockCol += 3) {

                Set<Integer> numbers = new HashSet<>();

                for (int i = 0; i < 3; i++) {

                    for (int j = 0; j < 3; j++) {

                        int value = grid[blockRow+i][blockCol+j];

                        if (value != 0 && !numbers.add(value)) {

                            return new ValidationResult(
                                false,
                                "Doublon détecté dans un bloc 3x3 : le chiffre "
                                + value + " existe déjà"
                            );
                        }
                    }
                }
            }
        }

        return new ValidationResult(true, "OK");
    }
}