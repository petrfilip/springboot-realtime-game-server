package cz.petrfilip.server.controller;

import cz.petrfilip.server.GameService;
import cz.petrfilip.server.GameState;
import cz.petrfilip.server.ObjectHolder;
import cz.petrfilip.server.event.GameEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@CrossOrigin
public class SpringPollingController {

  private final ExecutorService consumers = Executors.newFixedThreadPool(200);

  private final Object lock = new Object();

  private final List<ObjectHolder<GameState>> holders = new ArrayList<>();

  private final GameService gameService;
  private final GameEventListener gameEventListener;

  public SpringPollingController(GameService gameService, GameEventListener gameEventListener) {
    this.gameEventListener = gameEventListener;
    // gameService.addPlayer(0);
    this.gameService = gameService;

  }

  @PostMapping("polling/poll")
  @CrossOrigin
  public DeferredResult<Object> publisher(@RequestBody Map<String, Object> ctx) {
    DeferredResult<Object> output = new DeferredResult<>();
    ObjectHolder<GameState> holder = new ObjectHolder<>();
    consumers.execute(() -> {
      try {

        // ctx.get("tick")  gameService.getGameState(0).getTick()

        synchronized (lock) {
          holders.add(holder);
          gameEventListener.onNextStateCalculated(() -> holder.set(gameService.getGameState(0)));
        }
        holder.get();
        output.setResult(gameService.getGameState(0));
      } catch (Exception e) {
       //todo logger
      }
    });
    return output;
  }


  @PostMapping(path = "polling/send")
  @CrossOrigin
  public Object send(@RequestBody Map<String, Object> playerMove) {
    synchronized (lock) {
      gameService.addPlayerMove(0, 0, playerMove);
      for (ObjectHolder<GameState> holder : holders) {
        gameEventListener.onNextStateCalculated(() -> holder.set(gameService.getGameState(0)));
      }
      holders.clear();
    }
    return playerMove;
  }




}
