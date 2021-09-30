package cz.petrfilip.multiplayerserver.draw;

import cz.petrfilip.multiplayerserver.GameStateDto;
import cz.petrfilip.multiplayerserver.IRule;
import cz.petrfilip.multiplayerserver.PlayerDto;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class DrawGameRule implements IRule<List<DrawGamePoint>, DrawGamePoint> {

  @Override
  public GameStateDto<List<DrawGamePoint>> init(Collection<PlayerDto> players) {
    GameStateDto<List<DrawGamePoint>> objectGameStateDto = new GameStateDto<>();
    objectGameStateDto.setGame(new ArrayList<>());
    return objectGameStateDto;
  }

  @Override
  public boolean isMoveAllowed(GameStateDto<List<DrawGamePoint>> gameStateDto, Object playerMove) {
    return true;
  }

  @Override
  public GameStateDto<List<DrawGamePoint>> getNextGameState(GameStateDto<List<DrawGamePoint>> newGameState, Map<Integer, DrawGamePoint> currentMoves, Collection<PlayerDto> players) {
    newGameState.getGame().addAll(currentMoves.values());
    return newGameState;
  }
}
