package cz.petrfilip.server;

import java.util.Objects;
import lombok.Data;

@Data
public class Player {

  private Integer playerId;

  private PlayerStateEnum state = PlayerStateEnum.WAITING;

  public Player() {
  }

  public Player(Integer playerId) {
    this.playerId = playerId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Player player = (Player) o;
    return Objects.equals(playerId, player.playerId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(playerId);
  }
}
