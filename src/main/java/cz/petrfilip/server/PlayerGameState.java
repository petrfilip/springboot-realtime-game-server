package cz.petrfilip.server;

import lombok.Data;

@Data
public class PlayerGameState<STATE> {

  private Player player;
  private STATE state;


}
