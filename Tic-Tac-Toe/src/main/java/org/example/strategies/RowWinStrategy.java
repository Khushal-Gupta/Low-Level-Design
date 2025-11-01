package org.example.strategies;

import org.example.models.Board;

public class RowWinStrategy implements WinCheckStrategy {
    @Override
    public boolean checkWin(Board board, char symbol) {
        for (int row = 0; row < board.getSize(); row++) {
            int cntSymbol = 0;
            for (int col = 0; col < board.getSize(); col++) {
                cntSymbol += (board.getCells()[row][col] == symbol) ? 1 : 0;
            }
            if (cntSymbol == board.getSize()) {
                return true;
            }
        }
        return false;
    }
}
