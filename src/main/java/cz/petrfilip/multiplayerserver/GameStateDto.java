package cz.petrfilip.multiplayerserver;

import java.util.ArrayList;
import java.util.Collection;
import lombok.Data;

@Data
public class GameStateDto<STATE> {

  private Integer tick = 0;

  private GameStateEnum state = GameStateEnum.WAITING;

  private Collection<PlayerDto> players = new ArrayList<>();

  private STATE game;

}
