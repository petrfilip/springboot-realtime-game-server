package cz.petrfilip.server.event;

public class GameEvent extends GameServerEvent {

  private final String roomId;

  public GameEvent(Object source, String roomId) {
    super(source);
    this.roomId = roomId;
  }

  public String getRoomId() {
    return roomId;
  }
}
