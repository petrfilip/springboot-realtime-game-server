package cz.petrfilip.server;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.petrfilip.server.event.TickEventPublisher;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope(scopeName = SCOPE_PROTOTYPE)
public class GameService {

  private IRule rule;
  private final TickEventPublisher tickEventPublisher;
  private final ObjectMapper objectMapper;

  // gameStates = tick + state in given tick
  private GameState currentState = null;
  private ScheduledFuture<?> scheduledFuture = null;

  private Map<Player, Object> unprocessedMoves = new HashMap<>();

  private final Map<Integer, Player> players = new HashMap<>();

  public GameService(TickEventPublisher tickEventPublisher, ObjectMapper objectMapper) {
    this.tickEventPublisher = tickEventPublisher;
    this.objectMapper = objectMapper;
  }

  public void setGame(IRule rule) {
    if (this.rule != null) {
      throw new IllegalStateException("Game is already set");
    }
    this.rule = rule;
  }

  public GameState addPlayer(Player player) {

    players.put(player.getPlayerId(), player);
    // rule.init(currentState != null ? currentState : new GameState(), players.values());
    return currentState;
  }

  public void removePlayer(Integer playerId) {
    players.remove(playerId);
  }

  public void addPlayerMove(Integer tick, Integer playerId, Object playerMove) {

    // check if player can do something new
    Player player = this.players.get(playerId);
    if (player.getState().equals(PlayerStateEnum.DONE)) {
      return;
    }

    Object move = objectMapper.convertValue(playerMove, rule.getMoveClass());

    unprocessedMoves.put(player, move);
    try {
      System.out.println(new StringBuilder().append("New player move in tick ").append(currentState.getTick()).append(":").append(objectMapper.writeValueAsString(playerMove)));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  public void calculateNextState() {
    GameState gameState = rule.getNextGameState(currentState, unprocessedMoves);
    gameState.setTick(gameState.getTick() + 1);

    // save state
    currentState = gameState;
    try {
      System.out.println("Current tick:" + gameState.getTick() + " :: " + objectMapper.writeValueAsString(currentState));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    if (gameState.getState().equals(GameStateEnum.FINISHED)) {
      scheduledFuture.cancel(false);
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

    scheduledFuture = executor.scheduleAtFixedRate(this::calculateNextState, 0, 100, TimeUnit.MILLISECONDS);
    return currentState;
  }

  @SneakyThrows
  public GameState getGameState() {
    return currentState;
  }
}
