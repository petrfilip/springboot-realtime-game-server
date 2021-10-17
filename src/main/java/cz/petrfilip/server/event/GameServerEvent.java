package cz.petrfilip.server.event;

import org.springframework.context.ApplicationEvent;

public abstract class GameServerEvent extends ApplicationEvent {

  public GameServerEvent(Object source) {
    super(source);
  }
}
