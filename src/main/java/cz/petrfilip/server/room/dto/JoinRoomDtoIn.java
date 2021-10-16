package cz.petrfilip.server.room.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinRoomDtoIn {

  private String roomId;
  private Integer playerId;
}
