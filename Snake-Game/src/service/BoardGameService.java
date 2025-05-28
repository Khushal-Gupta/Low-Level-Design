package service;

import models.Board;
import models.Cell;
import models.Jumper;
import models.Player;
import util.RandomUtil;

import java.util.*;

public class BoardGameService {
    Board board;
    Map<String, Player> playerIdToPlayer;
    Map<String, Integer> playerIdToPlayerPosition;
    Queue<String> playerTurn;
    DiceService diceService;

    int DEFAULT_NUMBER_OF_SNAKES = 10;
    int DEFAULT_NUMBER_OF_LADDERS = 10;

    public BoardGameService() {

    }

    public void initializeGame(int boardSize, List<String> playerNames) {
        board = new Board(boardSize);
        for(int i=0; i<playerNames.size(); i++) {
            Player player = new Player(playerNames.get(i));
            playerTurn.add(player.getId());
            playerIdToPlayer.put(player.getId(), player);
            playerIdToPlayerPosition.put(player.getId(), 0);
        }

        Set<Integer> jumperEndpoints = new HashSet<Integer>();
        List<Jumper> snakes = generateSnakes(board, jumperEndpoints, DEFAULT_NUMBER_OF_SNAKES);
        for(Jumper snake: snakes) {
            jumperEndpoints.add(snake.getStart());
            jumperEndpoints.add(snake.getEnd());
        }

        List<Jumper> ladders = generateLadder(board, jumperEndpoints, DEFAULT_NUMBER_OF_LADDERS);
        for(Jumper snake: snakes) {
            jumperEndpoints.add(snake.getStart());
            jumperEndpoints.add(snake.getEnd());
        }
        diceService = new DiceService(1);

    }


    List<Jumper> generateSnakes(Board board, Set<Integer> cellWithJumpers, int numberOfSnakes) {
        int boardEndPosition = board.getEndPosition();
        List<Jumper> snakes = new ArrayList<>();
        Set<Integer> snakeOccupiedCell = new HashSet<>();
        while(snakes.size() < numberOfSnakes)  {
            int snakeStart = RandomUtil.generateRandom(1, boardEndPosition-1);
            int snakeEnd = RandomUtil.generateRandom(1, boardEndPosition-1);
            if((snakeStart <= snakeEnd)
                    || cellWithJumpers.contains(snakeStart)
                    || cellWithJumpers.contains(snakeEnd)
                    || snakeOccupiedCell.contains(snakeStart)
                    || snakeOccupiedCell.contains(snakeEnd)
            ) {
                continue;
            }
            snakes.add(new Jumper(snakeStart, snakeEnd));
            snakeOccupiedCell.add(snakeStart);
            snakeOccupiedCell.add(snakeEnd);
        }
        return snakes;
    }


    List<Jumper> generateLadder(Board board, Set<Integer> cellWithJumpers, int numberOfLadders) {
        int boardEndPosition = board.getEndPosition();
        List<Jumper> ladders = new ArrayList<>();
        Set<Integer> ladderOccupiedCell = new HashSet<>();
        while(ladders.size() < numberOfLadders)  {
            int snakeStart = RandomUtil.generateRandom(1, boardEndPosition-1);
            int snakeEnd = RandomUtil.generateRandom(1, boardEndPosition-1);
            if((snakeStart <= snakeEnd)
                    || cellWithJumpers.contains(snakeStart)
                    || cellWithJumpers.contains(snakeEnd)
                    || ladderOccupiedCell.contains(snakeStart)
                    || ladderOccupiedCell.contains(snakeEnd)
            ) {
                continue;
            }
            ladders.add(new Jumper(snakeStart, snakeEnd));
            ladderOccupiedCell.add(snakeStart);
            ladderOccupiedCell.add(snakeEnd);
        }
        return ladders;
    }

    public void startGame() {
        while(playerTurn.size() > 1) {
            Player player = playerIdToPlayer.get(playerTurn.poll());
            int currentPlayerPosition = playerIdToPlayerPosition.get(player.getId());
            int newPlayerPosition = currentPlayerPosition + diceService.roll();



            if (newPlayerPosition > board.getEndPosition()) {
                System.out.println(String.format("Oops! breached the boundary, Player %s, need to rethrow dice", player.getName()));
                playerTurn.add(player.getId());
                continue;
            }

            Cell cell = board.getCellAtPosition(newPlayerPosition);
            cell.

        }
    }
}
