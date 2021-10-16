package cz.petrfilip.server;

import java.util.Collection;
import java.util.Map;

public interface IRule<STATE, MOVE> {

  Class<MOVE> getMoveClass();

  GameState<STATE> init(GameState<STATE> currentState, Collection<Player> players);

  GameState<STATE> getNextGameState(GameState<STATE> newGameState, Map<Player, MOVE> unprocessedMoves);
}
