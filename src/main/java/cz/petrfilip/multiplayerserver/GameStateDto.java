package cz.petrfilip.multiplayerserver;

import lombok.Data;

@Data
public class GameStateDto<STATE> {

  private Integer tick;

  private GameStateEnum state;

  private STATE game;

}
