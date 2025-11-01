package org.example;

import org.example.models.Game;
import org.example.models.Player;


public class Demo {
    public static void main(String[] args) {
        demoRowWin();
        demoDiagonalWin();
        demoGameDraw();
    }

    private static void demoRowWin() {
        System.out.println("Demo 1: Row Win by Ajay");
        Player player1 = new Player("ajay");
        Player player2 = new Player("rohit");

        Game game = new Game();
        game.initializeGame(player1, player2);

        game.makeMove(player1, 0, 0);
        game.displayBoard();
        game.makeMove(player2, 2, 0);
        game.displayBoard();
        game.makeMove(player1, 0, 1);
        game.displayBoard();
        game.makeMove(player2, 2, 1);
        game.displayBoard();
        game.makeMove(player1, 0, 2);
        game.displayBoard();
        System.out.println("----------------------------------------\n");
    }

    private static void demoDiagonalWin() {
        System.out.println("Demo 2: Diagonal Win by Priya");
        Player player1 = new Player("arjun");
        Player player2 = new Player("priya");
        
        Game game = new Game();
        game.initializeGame(player1, player2);
        
        game.makeMove(player1, 0, 1);
        game.displayBoard();
        game.makeMove(player2, 0, 0);
        game.displayBoard();
        game.makeMove(player1, 0, 2);
        game.displayBoard();
        game.makeMove(player2, 1, 1);
        game.displayBoard();
        game.makeMove(player1, 1, 0);
        game.displayBoard();
        game.makeMove(player2, 2, 2);
        game.displayBoard();
        System.out.println("----------------------------------------\n");
    }

    private static void demoGameDraw() {
        System.out.println("Demo 3: Game Draw");
        Player alice = new Player("kavya");
        Player bob = new Player("ravi");
        
        Game game = new Game();
        game.initializeGame(alice, bob);
        
        game.makeMove(alice, 0, 0);
        game.displayBoard();
        game.makeMove(bob, 0, 1);
        game.displayBoard();
        game.makeMove(alice, 0, 2);
        game.displayBoard();
        game.makeMove(bob, 1, 1);
        game.displayBoard();
        game.makeMove(alice, 1, 0);
        game.displayBoard();
        game.makeMove(bob, 2, 0);
        game.displayBoard();
        game.makeMove(alice, 1, 2);
        game.displayBoard();
        game.makeMove(bob, 2, 2);
        game.displayBoard();
        game.makeMove(alice, 2, 1);
        game.displayBoard();
        System.out.println("----------------------------------------\n");
    }
}