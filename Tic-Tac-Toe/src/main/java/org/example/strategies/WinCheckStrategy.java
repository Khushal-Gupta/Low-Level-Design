package org.example.strategies;

import org.example.models.Board;

public interface WinCheckStrategy {
    boolean checkWin(Board board, char symbol);
}
