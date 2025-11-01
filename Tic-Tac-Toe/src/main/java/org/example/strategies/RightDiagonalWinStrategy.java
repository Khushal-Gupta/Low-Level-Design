package org.example.strategies;

import org.example.models.Board;

public class RightDiagonalWinStrategy implements WinCheckStrategy {
    @Override
    public boolean checkWin(Board board, char symbol) {
        int cntSymbol = 0;
        for (int row = 0, col = board.getSize() - 1; row < board.getSize() && col >= 0; row++, col--) {
            cntSymbol += (board.getCells()[row][col] == symbol) ? 1 : 0;
        }
        return cntSymbol == board.getSize();
    }
}
