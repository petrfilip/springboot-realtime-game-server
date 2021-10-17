package cz.petrfilip.server.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

  private final ApplicationEventPublisher applicationEventPublisher;

  public EventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  public void publish(GameServerEvent event) {
    applicationEventPublisher.publishEvent(event);
  }
}
