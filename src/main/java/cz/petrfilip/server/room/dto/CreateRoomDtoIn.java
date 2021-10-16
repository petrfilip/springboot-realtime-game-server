package cz.petrfilip.server.room.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoomDtoIn {

  private String roomName;
  private String gameName;
  private Integer roomOwner;
}
