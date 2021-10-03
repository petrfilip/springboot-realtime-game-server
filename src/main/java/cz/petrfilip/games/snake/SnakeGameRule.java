package cz.petrfilip.games.snake;

import cz.petrfilip.server.GameState;
import cz.petrfilip.server.IRule;
import cz.petrfilip.server.Player;
import cz.petrfilip.server.PlayerStateEnum;
import cz.petrfilip.server.Utils;
import cz.petrfilip.server.common.BoardUtils;
import cz.petrfilip.server.common.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class SnakeGameRule implements IRule<SnakeBoard, SnakeMove> {

  @Override
  public Class<SnakeMove> getMoveClass() {
    return SnakeMove.class;
  }

  @Override
  public GameState<SnakeBoard> init(GameState<SnakeBoard> currentState, Collection<Player> players) {

    //init players
    players.forEach(player -> player.setState(PlayerStateEnum.ACTIVE));
    currentState.getPlayers().addAll(players);

    currentState.setGame(currentState.getGame() != null ? currentState.getGame() : new SnakeBoard());

    Integer boardWidth = (Integer) currentState.getParams().getOrDefault("width", 30);
    Integer boardHeight = (Integer) currentState.getParams().getOrDefault("height", 30);

    for (Player player : players) {
      if (!currentState.getGame().getSnakeList().containsKey(player)) {
        currentState.getGame().getSnakeList()
            .put(player, new Snake(BoardUtils.generatePoint(boardWidth, boardHeight), Utils.randomEnum(SnakeDirectionEnum.class)));
      }
    }

    if (currentState.getGame().getFoodList().size() < players.size()) {
      currentState.getGame().getFoodList()
          .add(new Food(Utils.getRandomNumber(0, boardWidth), Utils.getRandomNumber(0, boardHeight)));
    }

    //todo add new food

    return currentState;
  }

  @Override
  public boolean isMoveAllowed(GameState<SnakeBoard> gameState, SnakeMove playerMove) {
    return true;
  }

  @Override
  public GameState<SnakeBoard> getNextGameState(GameState<SnakeBoard> newGameState, Map<Player, SnakeMove> currentMoves) {

    Integer boardWidth = (Integer) newGameState.getParams().getOrDefault("width", 30);
    Integer boardHeight = (Integer) newGameState.getParams().getOrDefault("height", 30);

    Map<Player, Snake> snakeList = newGameState.getGame().getSnakeList();
    List<Food> eatenFoods = new ArrayList<>();
    // apply user moves
    for (Player player : newGameState.getPlayers()) {

      if (player.getState().equals(PlayerStateEnum.DONE)) {
        continue;
      }

      SnakeDirectionEnum snakeNewDirection = getSnakeDirection(currentMoves, snakeList, player);

      SnakeBody head = snakeList.get(player).getBody().getLast();
      SnakeBody nextHead = calculateNextHead(head, snakeNewDirection);
      nextHead = calculateGoThroughWall(boardWidth, boardHeight, snakeNewDirection, nextHead);

      // check get food or add new
      int eatenFoodIndex = BoardUtils.indexOfPoint(newGameState.getGame().getFoodList(), nextHead);
      if (eatenFoodIndex >= 0) {
        eatenFoods.add(newGameState.getGame().getFoodList().get(eatenFoodIndex));
      } else {
        SnakeBody tail = snakeList.get(player).getBody().poll();
        assert tail != null;
      }

      List<Point> allPoints = new ArrayList<>();
      for (Snake value : newGameState.getGame().getSnakeList().values()) {
        allPoints.addAll(value.getBody());
      }

      // check collision
      if (BoardUtils.indexOfPoint(allPoints, nextHead) >= 0) {
        player.setState(PlayerStateEnum.DONE);
      }

      snakeList.get(player).setDirection(snakeNewDirection);
      snakeList.get(player).getBody().add(nextHead);
    }


    newGameState.getGame().getFoodList().removeAll(eatenFoods);

    for (int i = 0; i < eatenFoods.size(); i++) {
      newGameState.getGame().getFoodList().add(new Food(BoardUtils.generatePoint(boardWidth, boardHeight)));
    }

    return newGameState;

  }

  private SnakeBody calculateNextHead(SnakeBody head, SnakeDirectionEnum snakeNewDirection) {
    SnakeBody nextHead = null;

    switch (snakeNewDirection) {
      case RIGHT:
        nextHead = new SnakeBody(head.getX() + 1, head.getY());
        break;
      case LEFT:
        nextHead = new SnakeBody(head.getX() - 1, head.getY());
        break;
      case UP:
        nextHead = new SnakeBody(head.getX(), head.getY() - 1);
        break;
      case DOWN:
        nextHead = new SnakeBody(head.getX(), head.getY() + 1);
        break;
    }
    return nextHead;
  }

  private SnakeDirectionEnum getSnakeDirection(Map<Player, SnakeMove> currentMoves, Map<Player, Snake> snakeList, Player player) {
    SnakeDirectionEnum snakeOriginDirection = snakeList.get(player).getDirection();
    SnakeDirectionEnum snakeNewDirection =
        (currentMoves.containsKey(player) && currentMoves.get(player).getDirection() != null) ?
            currentMoves.get(player).getDirection() : snakeOriginDirection;

    if (snakeOriginDirection.equals(SnakeDirectionEnum.RIGHT) && snakeNewDirection.equals(SnakeDirectionEnum.LEFT) ||
        snakeOriginDirection.equals(SnakeDirectionEnum.UP) && snakeNewDirection.equals(SnakeDirectionEnum.DOWN) ||
        snakeOriginDirection.equals(SnakeDirectionEnum.LEFT) && snakeNewDirection.equals(SnakeDirectionEnum.RIGHT) ||
        snakeOriginDirection.equals(SnakeDirectionEnum.DOWN) && snakeNewDirection.equals(SnakeDirectionEnum.UP)) {
      snakeNewDirection = snakeOriginDirection;
    }
    return snakeNewDirection;
  }

  private SnakeBody calculateGoThroughWall(Integer boardWidth, Integer boardHeight, SnakeDirectionEnum snakeNewDirection, SnakeBody nextHead) {
    if (snakeNewDirection.equals(SnakeDirectionEnum.LEFT) && nextHead.getX() < 0) {
      nextHead.setX(boardWidth);
    }

    if (snakeNewDirection.equals(SnakeDirectionEnum.RIGHT) && nextHead.getX() > boardWidth) {
      nextHead.setX(0);
    }

    if (snakeNewDirection.equals(SnakeDirectionEnum.UP) && nextHead.getY() < 0) {
      nextHead.setY(boardHeight);
    }

    if (snakeNewDirection.equals(SnakeDirectionEnum.DOWN) && nextHead.getY() > boardHeight) {
      nextHead.setY(0);
    }

    return nextHead;
  }

}
