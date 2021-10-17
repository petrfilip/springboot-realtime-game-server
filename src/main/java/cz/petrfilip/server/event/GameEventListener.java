package cz.petrfilip.server.event;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class GameEventListener implements ApplicationListener<GameServerEvent> {

  private Set<GameEventCallback> callbacks = new HashSet<>();

  private final Object lock = new Object();

  @Override
  public void onApplicationEvent(GameServerEvent event) {
    synchronized (lock) {
      Set<GameEventCallback> copy = new CopyOnWriteArraySet<>(callbacks);
      callbacks = new HashSet<>();

      Set<GameEventCallback> unsupportedCallbacks = copy.stream().filter(callback -> !callback.supports(event)).collect(Collectors.toSet());
      callbacks.addAll(unsupportedCallbacks);
      copy.stream().filter(callback -> callback.supports(event)).forEach(GameEventCallback::callback);
    }

  }

  public void onChange(GameEventCallback callback) {
    callbacks.add(callback);
  }
}
