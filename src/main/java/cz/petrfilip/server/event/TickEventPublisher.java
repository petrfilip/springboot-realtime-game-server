package cz.petrfilip.server.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class TickEventPublisher {
  private final ApplicationEventPublisher applicationEventPublisher;

  public TickEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
    this.applicationEventPublisher = applicationEventPublisher;
  }

  public void tickEvent(final String message) {
    TickEvent customSpringEvent = new TickEvent(this, message);
    applicationEventPublisher.publishEvent(customSpringEvent);
  }
}
