package cz.petrfilip.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class GameState<STATE> {

  private Integer tick = 0;

  private GameStateEnum state = GameStateEnum.WAITING;

  private Map<String, Object> params = new HashMap<>();

  private Collection<Player> players = new ArrayList<>();

  private STATE game;

}