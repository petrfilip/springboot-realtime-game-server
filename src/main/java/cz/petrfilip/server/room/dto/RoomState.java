package cz.petrfilip.server.room.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import cz.petrfilip.server.GameService;
import cz.petrfilip.server.Player;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomState {

  private String roomId;
  private String roomName;
  private RoomStateEnum state;
  private Player roomOwner;
  private List<Player> connectedPlayers = new ArrayList<>();

  @JsonIgnore
  private GameService game;

}
