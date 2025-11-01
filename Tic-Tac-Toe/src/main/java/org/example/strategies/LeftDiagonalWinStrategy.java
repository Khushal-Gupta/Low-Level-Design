package org.example.strategies;

import org.example.models.Board;

public class LeftDiagonalWinStrategy implements WinCheckStrategy {
    @Override
    public boolean checkWin(Board board, char symbol) {
        int cntSymbol = 0;
        for (int row = 0, col = 0; row < board.getSize() && col < board.getSize(); row++, col++) {
            cntSymbol += (board.getCells()[row][col] == symbol) ? 1 : 0;
        }
        return cntSymbol == board.getSize();
    }
}
