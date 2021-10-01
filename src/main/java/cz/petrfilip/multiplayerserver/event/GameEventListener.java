package cz.petrfilip.multiplayerserver.event;

import java.util.ArrayDeque;
import java.util.Queue;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class GameEventListener implements ApplicationListener<TickEvent> {

  private final Queue<GameEventCallback> tickEvents = new ArrayDeque<>();

  @Override
  public void onApplicationEvent(TickEvent event) {
    while (!tickEvents.isEmpty()) {
      GameEventCallback peek = tickEvents.poll();
      peek.callback();
    }
  }


  public void onNextStateCalculated(GameEventCallback callback) {
    tickEvents.add(callback);
  }
}
