package cz.petrfilip.server;

import java.util.Collection;
import java.util.Map;

public interface IRule<STATE, MOVE> {

  Class<MOVE> getMoveClass();

  GameState<STATE> init(GameState<STATE> currentState, Collection<Player> players);

  boolean isMoveAllowed(GameState<STATE> gameState, MOVE playerMove);

  GameState<STATE> getNextGameState(GameState<STATE> newGameState, Map<Player, MOVE> currentMoves, Collection<Player> values);
}
