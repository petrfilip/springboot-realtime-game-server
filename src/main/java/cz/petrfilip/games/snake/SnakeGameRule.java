package cz.petrfilip.games.snake;

import cz.petrfilip.server.GameState;
import cz.petrfilip.server.IRule;
import cz.petrfilip.server.Player;
import java.util.Collection;
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
    currentState.setGame(currentState.getGame() != null ? currentState.getGame() : new SnakeBoard());

    for (Player player : players) {
      if (!currentState.getGame().getSnakeList().containsKey(player)) {
        currentState.getGame().getSnakeList().put(player, new Snake(0, 0, SnakeDirectionEnum.DOWN)); //Todo find good random place
      }
    }

    //todo add new food

    return currentState;
  }

  @Override
  public boolean isMoveAllowed(GameState<SnakeBoard> gameState, SnakeMove playerMove) {
    return true;
  }

  @Override
  public GameState<SnakeBoard> getNextGameState(GameState<SnakeBoard> newGameState, Map<Player, SnakeMove> currentMoves, Collection<Player> players) {

    Integer boardWidth = (Integer) newGameState.getParams().getOrDefault("width", 30);
    Integer boardHeight = (Integer) newGameState.getParams().getOrDefault("height", 30);

    // apply user moves
    Map<Player, Snake> snakeList = newGameState.getGame().getSnakeList();
    for (Player player : players) {

      SnakeBody head = snakeList.get(player).getBody().poll();
      assert head != null;

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

      SnakeBody nextSnakeBody = null;

      switch (snakeNewDirection) {
        case RIGHT:
          nextSnakeBody = new SnakeBody(head.getX() + 1, head.getY());
          break;
        case LEFT:
          nextSnakeBody = new SnakeBody(head.getX() - 1, head.getY());
          break;
        case UP:
          nextSnakeBody = new SnakeBody(head.getX(), head.getY() - 1);
          break;
        case DOWN:
          nextSnakeBody = new SnakeBody(head.getX(), head.getY() + 1);
          break;
      }

      if (snakeNewDirection.equals(SnakeDirectionEnum.LEFT) && nextSnakeBody.getX() <= 0) {
        nextSnakeBody.setX(boardWidth);
      }

      if (snakeNewDirection.equals(SnakeDirectionEnum.RIGHT) && nextSnakeBody.getX() > boardWidth) {
        nextSnakeBody.setX(0);
      }

      if (snakeNewDirection.equals(SnakeDirectionEnum.UP) && nextSnakeBody.getY() <= 0) {
        nextSnakeBody.setY(boardHeight);
      }

      if (snakeNewDirection.equals(SnakeDirectionEnum.DOWN) && nextSnakeBody.getY() > boardHeight) {
        nextSnakeBody.setY(0);
      }



      // check get food or add new

      // check collision

      snakeList.get(player).setDirection(snakeNewDirection);
      snakeList.get(player).getBody().add(nextSnakeBody);
    }

    return newGameState;

  }

}
