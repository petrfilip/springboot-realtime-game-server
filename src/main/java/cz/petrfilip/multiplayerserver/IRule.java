package cz.petrfilip.multiplayerserver;

import cz.petrfilip.multiplayerserver.draw.DrawGamePoint;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IRule<STATE, MOVE> {

  GameStateDto<List<DrawGamePoint>> init(Collection<PlayerDto> players);

  boolean isMoveAllowed(GameStateDto<STATE> gameStateDto, Object playerMove);

  GameStateDto<STATE> getNextGameState(GameStateDto<STATE> newGameState, Map<Integer, MOVE> currentMoves, Collection<PlayerDto> values);
}
