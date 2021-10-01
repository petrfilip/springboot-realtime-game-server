package cz.petrfilip.multiplayerserver;

import lombok.Data;

@Data
public class PlayerDto {

  private Integer playerId;

  private PlayerStateEnum state = PlayerStateEnum.WAITING;

  public PlayerDto() {
  }

  public PlayerDto(Integer playerId) {
    this.playerId = playerId;
  }
}
