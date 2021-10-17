package cz.petrfilip.server.event;

import org.springframework.context.ApplicationEvent;

public interface GameEventCallback {

  boolean supports(ApplicationEvent tickEvents);

  void callback();

}
