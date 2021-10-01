package cz.petrfilip.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.petrfilip.server.event.TickEventPublisher;
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

  // gameStates = tick + state in given tick
  private GameState currentState = null;
  private ScheduledFuture<?> scheduledFuture = null;

  private Map<Player, Object> unprocessedMoves = new HashMap<>();

  private final Map<Integer, Player> players = new HashMap<>();

  public GameService(IRule rule, TickEventPublisher tickEventPublisher, ObjectMapper objectMapper) {
    this.rule = rule;
    this.tickEventPublisher = tickEventPublisher;
    this.objectMapper = objectMapper;
    startGame(new HashMap<>());
  }

  public GameState addPlayer(Integer playerId) {
    Player player = new Player(playerId);

    players.put(playerId, player);
    rule.init(currentState != null ? currentState : new GameState(), players.values());
    return currentState;
  }

  public void removePlayer(Integer playerId) {
    players.remove(playerId);
  }

  public void addPlayerMove(Integer tick, Integer playerId, Object playerMove) {

    // check if player can do something new
    Player player = this.players.get(playerId);
    if (player.getState().equals(PlayerStateEnum.FINISHED)) {
      return;
    }

    // verify if player is cheating or something gone wrong
    // if (!rule.isMoveAllowed(currentState, playerMove)) {
    //   throw new RuntimeException("Unexpected move");
    // }

    Object move = objectMapper.convertValue(playerMove, rule.getMoveClass());

    unprocessedMoves.put(player, move);
    try {
      System.out.println(new StringBuilder().append("New player move in tick ").append(currentState.getTick()).append(":").append(objectMapper.writeValueAsString(playerMove)).toString());
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  public void calculateNextState() {
    GameState gameStateInCurrentTick = currentState != null ? currentState : rule.init(new GameState(), players.values());
    GameState gameState = rule.getNextGameState(gameStateInCurrentTick, unprocessedMoves, players.values());
    gameState.setTick(gameState.getTick() + 1);

    // save state
    currentState = gameState;
    try {
      System.out.println("Current tick:" + gameState.getTick() + " :: " + objectMapper.writeValueAsString(currentState));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    unprocessedMoves = new HashMap<>();
    tickEventPublisher.tickEvent(new Date().toInstant().toString());
  }

  public GameState startGame(Map<String, Object> gameParameters) {

    if (scheduledFuture != null) {
      scheduledFuture.cancel(true);
    }

    currentState = new GameState();
    currentState.setParams(gameParameters);
    rule.init(currentState, players.values());
    ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    scheduledFuture = executor.scheduleAtFixedRate(this::calculateNextState, 0, 500, TimeUnit.MILLISECONDS);
    return currentState;
  }

  @SneakyThrows
  public GameState getGameState(Object ctx) {
    return currentState;
  }
}
