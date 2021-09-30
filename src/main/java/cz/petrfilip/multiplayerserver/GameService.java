package cz.petrfilip.multiplayerserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class GameService {

  private final IRule rule;
  private final ObjectMapper objectMapper;
  private Integer counter = 0;

  // gameStates = tick + state in given tick
  private final Map<Integer, GameStateDto> gameStates = new HashMap<>(10);

  private Map<Integer, Object> currentMoves = new HashMap<>();

  private final Map<Integer, PlayerDto> players = new HashMap<>();

  public GameService(IRule rule) {
    this.rule = rule;
    this.objectMapper = new ObjectMapper();
  }

  public void addPlayer(Integer playerId) {
    PlayerDto playerDto = new PlayerDto(playerId);
    // playerDto.setPlayerListener(new PlayerListener() {
    //   @Override
    //   public void listen(GameStateDto gameStateDto) {
    //     // System.out.println(new Date());
    //   }
    // });
    players.put(playerId, playerDto);
  }

  public void removePlayer(Integer playerId) {
    players.remove(playerId);
  }

  public void addPlayerMove(Integer tick, Integer playerId, Object playerMove) {

    // check if player can do something new
    PlayerDto player = this.players.get(playerId);
    if (player.getState().equals(PlayerStateEnum.FINISHED)) {
      return;
    }

    // verify if player is cheating or something gone wrong
    if (!rule.isMoveAllowed(gameStates.get(tick), playerMove)) {
      throw new RuntimeException("Unexpected move");
    }

    currentMoves.put(playerId, playerMove);
  }

  @SneakyThrows
  public GameStateDto getGameState(Object ctx) {

    // get all user inputs

    // calculate new state

    GameStateDto gameStateInCurrentTick = gameStates.getOrDefault(gameStates.size() - 1, rule.init(players.values()));
    GameStateDto gameStateInCurrentTickDeepClone = objectMapper.readValue(objectMapper.writeValueAsString(gameStateInCurrentTick), GameStateDto.class);
    GameStateDto gameStateDto = rule.getNextGameState(gameStateInCurrentTickDeepClone, currentMoves, players.values());

    // save state
    gameStates.put(0, gameStateDto);

    // return state
    // for (PlayerDto value : players.values()) {
    //   value.getPlayerListener().listen(gameStateDto);
    // }

    System.out.println(counter++);

    currentMoves = new HashMap<>();

    return gameStateDto;
  }
}
