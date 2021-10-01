package cz.petrfilip.server.event;

import org.springframework.context.ApplicationEvent;

public class TickEvent extends ApplicationEvent {
  private String message;

  public TickEvent(Object source, String message) {
    super(source);
    this.message = message;
  }
  public String getMessage() {
    return message;
  }
}