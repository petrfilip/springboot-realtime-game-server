package cz.petrfilip.games.draw;

import cz.petrfilip.server.GameState;
import cz.petrfilip.server.IRule;
import cz.petrfilip.server.Player;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

// @Service
public class DrawGameRule implements IRule<List<DrawGamePoint>, DrawGamePoint> {

  @Override
  public Class<DrawGamePoint> getMoveClass() {
    return DrawGamePoint.class;
  }

  @Override
  public GameState<List<DrawGamePoint>> init(GameState<List<DrawGamePoint>> currentState, Collection<Player> players) {
    currentState.setGame(currentState.getGame() != null ? currentState.getGame() : new ArrayList<>());
    currentState.setPlayers(players);
    return currentState;
  }

  @Override
  public boolean isMoveAllowed(GameState<List<DrawGamePoint>> gameState, DrawGamePoint playerMove) {
    return true;
  }

  @Override
  public GameState<List<DrawGamePoint>> getNextGameState(GameState<List<DrawGamePoint>> newGameState, Map<Player, DrawGamePoint> currentMoves, Collection<Player> players) {
    newGameState.getGame().addAll(currentMoves.values());
    return newGameState;
  }
}
