package service;

import enums.JumperType;
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

    int DEFAULT_NUMBER_OF_SNAKES = 5;
    int DEFAULT_NUMBER_OF_LADDERS = 5;

    public BoardGameService() {

    }

    public void initializeGame(int boardSize, List<String> playerNames) {
        board = new Board(boardSize);
        playerTurn = new LinkedList<>();
        playerIdToPlayer = new HashMap<>();
        playerIdToPlayerPosition = new HashMap<>();
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
            board.getCellAtPosition(snake.getStart()).setJumper(snake);
            System.out.printf("Snake at {%s, %s}\n", snake.getStart(), snake.getEnd());
        }

        List<Jumper> ladders = generateLadder(board, jumperEndpoints, DEFAULT_NUMBER_OF_LADDERS);
        for(Jumper ladder: ladders) {
            jumperEndpoints.add(ladder.getStart());
            jumperEndpoints.add(ladder.getEnd());
            board.getCellAtPosition(ladder.getStart()).setJumper(ladder);
            System.out.printf("Ladder at {%s, %s}\n", ladder.getStart(), ladder.getEnd());
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
            if ((snakeStart <= snakeEnd)
                    || cellWithJumpers.contains(snakeStart)
                    || cellWithJumpers.contains(snakeEnd)
                    || snakeOccupiedCell.contains(snakeStart)
                    || snakeOccupiedCell.contains(snakeEnd)
            ) {
                continue;
            }
            snakes.add(new Jumper(snakeStart, snakeEnd, JumperType.SNAKE));
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
            int ladderStart = RandomUtil.generateRandom(1, boardEndPosition-1);
            int ladderEnd = RandomUtil.generateRandom(1, boardEndPosition-1);
            if((ladderStart >= ladderEnd)
                    || cellWithJumpers.contains(ladderStart)
                    || cellWithJumpers.contains(ladderEnd)
                    || ladderOccupiedCell.contains(ladderStart)
                    || ladderOccupiedCell.contains(ladderEnd)
            ) {
                continue;
            }
            ladders.add(new Jumper(ladderStart, ladderEnd, JumperType.LADDER));
            ladderOccupiedCell.add(ladderStart);
            ladderOccupiedCell.add(ladderEnd);
        }
        return ladders;
    }

    public void startGame() {
        while(playerTurn.size() > 1) {
            System.out.println("Player positions :");
            for(Map.Entry<String, Player> entry: playerIdToPlayer.entrySet()) {
                Player player = entry.getValue();
                System.out.printf("Player %s is at position %d\n", player.getName(), playerIdToPlayerPosition.get(entry.getKey()));
            }
            Player player = playerIdToPlayer.get(playerTurn.poll());
            int newPlayerPosition = getNewPlayerPosition(player);
            playerIdToPlayerPosition.put(player.getId(), newPlayerPosition);
            if(newPlayerPosition == board.getEndPosition()) {
                System.out.printf("%s completed the game. :)\n", player.getName());
            } else {
                playerTurn.add(player.getId());
            }
        }
    }

    private Integer getNewPlayerPosition(Player player) {
        int currentPlayerPosition = playerIdToPlayerPosition.get(player.getId());
        int newPlayerPosition = currentPlayerPosition + diceService.roll();

        if (newPlayerPosition > board.getEndPosition()) {
            System.out.printf("Oops! breached the boundary, Player %s, need to rethrow dice\n", player.getName());
            return currentPlayerPosition;
        }

        Cell cell = board.getCellAtPosition(newPlayerPosition);
        if(cell.getJumper().isPresent()) {
            newPlayerPosition = cell.getJumper().get().getEnd();
            if (cell.getJumper().get().getType() == JumperType.SNAKE) {
                System.out.printf("%s is bitten by snake\n", player.getName());
            } else {
                System.out.printf("%s found a ladder\n", player.getName());
            }
        }

        return newPlayerPosition;
    }
}
