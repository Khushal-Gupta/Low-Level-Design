import service.BoardGameService;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        System.out.println("Welcome to the game!");

        BoardGameService boardGameService = new BoardGameService();
        List<String> players = List.of("John", "Stuart", "Jackson", "Eden");
        boardGameService.initializeGame(10, players);
        boardGameService.startGame();
    }
}