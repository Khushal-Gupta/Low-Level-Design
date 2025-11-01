package org.example.models;

import org.example.enums.PlayerSymbol;
import org.example.enums.GameState;
import org.example.exceptions.InvalidMoveException;
import org.example.strategies.WinCheckStrategy;
import org.example.strategies.RowWinStrategy;
import org.example.strategies.ColumnWinStrategy;
import org.example.strategies.LeftDiagonalWinStrategy;
import org.example.strategies.RightDiagonalWinStrategy;

import java.util.*;

public class Game {
    private Board board;
    private GameState gameState;
    private Map<String, Player> playerMap;
    private Map<String, PlayerSymbol> playerIdToPieceMap;
    private Queue<String> playerChanceQueue;
    private Player playerWon;
    private List<WinCheckStrategy> winCheckStrategies;

    public Game() {
    }

    public void initializeGame(Player p1, Player p2) {
        System.out.println("Starting new game :)");
        gameState = GameState.IN_PROGRESS;
        playerMap = new HashMap<>();
        playerMap.put(p1.getId(), p1);
        playerMap.put(p2.getId(), p2);
        board = new Board();
        playerChanceQueue = new LinkedList<>();
        playerChanceQueue.add(p1.getId());
        playerChanceQueue.add(p2.getId());
        playerIdToPieceMap = new HashMap<>();
        playerIdToPieceMap.put(p1.getId(), PlayerSymbol.X);
        playerIdToPieceMap.put(p2.getId(), PlayerSymbol.O);
        initializeWinStrategies();
    }


    public void makeMove(Player p, int posX, int posY) {
        if (gameState != GameState.IN_PROGRESS) {
            System.out.println("Game already ended");
            return;
        }
        if (!p.getId().equals(playerChanceQueue.poll())) {
            System.out.println("Sorry, this is not your chance");
            return;
        }
        
        try {
            playerChanceQueue.offer(p.getId());
            board.update(posX, posY, playerIdToPieceMap.get(p.getId()).getChar());
            if (checkWin(p)) {
                gameState = GameState.COMPLETED_WITH_WIN;
                playerWon = p;
                System.out.printf("%s has won the match!%n", p.getName());
            } else if (checkGameDraw()) {
                gameState = GameState.COMPLETED_WITH_DRAW;
                System.out.println("The game ended in draw");
            }
        } catch (InvalidMoveException e) {
            System.out.println("Invalid move: " + e.getMessage());
        }
    }


    private void initializeWinStrategies() {
        winCheckStrategies = new ArrayList<>();
        winCheckStrategies.add(new RowWinStrategy());
        winCheckStrategies.add(new ColumnWinStrategy());
        winCheckStrategies.add(new LeftDiagonalWinStrategy());
        winCheckStrategies.add(new RightDiagonalWinStrategy());
    }

    private boolean checkWin(Player p) {
        char symbol = playerIdToPieceMap.get(p.getId()).getChar();
        
        for (WinCheckStrategy strategy : winCheckStrategies) {
            if (strategy.checkWin(board, symbol)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkGameDraw() {
        return !board.hasEmptyCells();
    }

    public void displayBoard() {
        printHorizontalBorder();
        for(int row=0; row<board.getSize(); row++) {
            for(int col=0; col<board.getSize(); col++) {
                if(col == board.getSize() - 1) {
                    System.out.print(" " + board.getCells()[row][col]);
                } else {
                    System.out.print(" " + board.getCells()[row][col] + " | ");
                }
            }
            System.out.print("\n");
            printHorizontalBorder();
        }
        System.out.println("------------------------------------------>");
    }
    
    private void printHorizontalBorder() {
        for(int i=0; i<board.getSize(); i++) {
            System.out.print("----");
        }
        System.out.println();
    }
}
