package models;

import exceptions.OutOfBoundException;

public class Board {
    int boardSize;
    Cell[][] cells;

    public Board (int boardSize) {
        this.boardSize = boardSize;
        cells = new Cell[boardSize][boardSize];
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
