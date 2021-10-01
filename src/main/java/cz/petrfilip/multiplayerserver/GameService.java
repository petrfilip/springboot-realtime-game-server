package cz.petrfilip.multiplayerserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.petrfilip.multiplayerserver.event.TickEventPublisher;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class GameService {

  private final IRule rule;
  private final TickEventPublisher tickEventPublisher;
  private final ObjectMapper objectMapper;
  private Integer counter = 0;

  // gameStates = tick + state in given tick
  private GameStateDto currentState = null;

  private Map<Integer, Object> unprocessedMoves = new HashMap<>();

  private final Map<Integer, PlayerDto> players = new HashMap<>();

  public GameService(IRule rule, TickEventPublisher tickEventPublisher, ObjectMapper objectMapper) {
    this.rule = rule;
    this.tickEventPublisher = tickEventPublisher;
    this.objectMapper = objectMapper;
    startGame();
  }

  public void addPlayer(Integer playerId) {
    PlayerDto playerDto = new PlayerDto(playerId);

    players.put(playerId, playerDto);
    rule.init(currentState != null ? currentState : new GameStateDto(), players.values());

  }

  public void removePlayer(Integer playerId) {
    players.remove(playerId);
  }

  @SneakyThrows
  public void addPlayerMove(Integer tick, Integer playerId, Object playerMove) {

    // check if player can do something new
    PlayerDto player = this.players.get(playerId);
    if (player.getState().equals(PlayerStateEnum.FINISHED)) {
      return;
    }

    // verify if player is cheating or something gone wrong
    if (!rule.isMoveAllowed(currentState, playerMove)) {
      throw new RuntimeException("Unexpected move");
    }

    System.out.println("New player move");
    unprocessedMoves.put(playerId, playerMove);
    System.out.println(objectMapper.writeValueAsString(playerMove));
  }

  public void calculateNextState() {
    GameStateDto gameStateInCurrentTick = currentState != null ? currentState : rule.init(new GameStateDto(), players.values());
    GameStateDto gameStateDto = rule.getNextGameState(gameStateInCurrentTick, unprocessedMoves, players.values());
    gameStateDto.setTick(gameStateDto.getTick() + 1);

    // save state
    currentState = gameStateDto;

    System.out.println(gameStateDto.getTick());

    unprocessedMoves = new HashMap<>();
    tickEventPublisher.tickEvent(new Date().toInstant().toString());
  }

  public void startGame() {

    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
    ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(this::calculateNextState, 0, 10000, TimeUnit.MILLISECONDS);
  }

  @SneakyThrows
  public GameStateDto getGameState(Object ctx) {
    return currentState;
  }
}
