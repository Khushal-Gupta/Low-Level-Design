package org.example.models;

import org.example.exceptions.InvalidMoveException;

public class Board {
    private final int size;
    private final char[][] cells;
    private int cntOccupiedCells;

    private char EMPTY = '-';

    public Board() {
        size = 3;
        cells = new char[size][size];
        for(int i=0; i<size; i++) {
            for(int j=0; j<size; j++) {
                cells[i][j] = EMPTY;
            }
        }
        cntOccupiedCells = 0;
    }

    public int getSize() {
        return size;
    }

    public char[][] getCells() {
        return cells;
    }

    boolean isOccupied(int row, int col) {
        return cells[row][col] != EMPTY;
    }

    public void update(int row, int col, char c) throws InvalidMoveException {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new InvalidMoveException("Position out of bounds: (" + row + ", " + col + ")");
        }
        if (isOccupied(row, col)) {
            throw new InvalidMoveException("Position already occupied: (" + row + ", " + col + ")");
        }
        cells[row][col] = c;
        cntOccupiedCells++;
    }

    public boolean hasEmptyCells() {
        return cntOccupiedCells < (size * size);
    }

}
