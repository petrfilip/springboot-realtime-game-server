package cz.petrfilip.server;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameStateUpdate {

  private String object;

  private Integer objectIndex;

  private GameStateUpdateEnum action;

  private Integer changeIndex;

  private Object changes;

}
