package cz.petrfilip.server.room.dto;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartGameInRoomDtoIn {

  private String roomId;
  private Integer playerId;
  private Map<String, Object> gameParameters = new HashMap<>();

}
