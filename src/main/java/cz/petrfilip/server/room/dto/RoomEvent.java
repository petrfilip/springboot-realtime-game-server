package cz.petrfilip.server.room.dto;

import cz.petrfilip.server.event.GameServerEvent;

public class RoomEvent extends GameServerEvent {

  private final String roomId;

  public RoomEvent(Object source, String roomId) {
    super(source);
    this.roomId = roomId;
  }

  public String getRoomId() {
    return roomId;
  }
}
