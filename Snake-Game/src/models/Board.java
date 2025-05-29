package models;

import exceptions.OutOfBoundException;

public class Board {
    int boardSize;
    Cell[][] cells;

    public Board (int boardSize) {
        this.boardSize = boardSize;
        cells = new Cell[boardSize][boardSize];
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                cells[row][col] = new Cell();
            }
        }
    }

    public Integer getEndPosition() {
        return boardSize*boardSize - 1;
    }

    public Cell getCellAtPosition(int position) {
        if (position >= boardSize*boardSize) {
            throw new OutOfBoundException(String.format("Position %s is out of board", position));
        }
        int row = position / boardSize;
        int col = position % boardSize;
        return cells[row][col];
    }
}
