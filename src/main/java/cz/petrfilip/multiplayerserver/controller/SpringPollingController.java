package cz.petrfilip.multiplayerserver.controller;

import cz.petrfilip.multiplayerserver.GameService;
import cz.petrfilip.multiplayerserver.GameStateDto;
import cz.petrfilip.multiplayerserver.ObjectHolder;
import cz.petrfilip.multiplayerserver.event.GameEventCallback;
import cz.petrfilip.multiplayerserver.event.GameEventListener;
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

  private final List<ObjectHolder<GameStateDto>> holders = new ArrayList<>();

  private final GameService gameService;
  private final GameEventListener gameEventListener;

  public SpringPollingController(GameService gameService, GameEventListener gameEventListener) {
    this.gameEventListener = gameEventListener;
    gameService.addPlayer(0);
    this.gameService = gameService;

  }

  @PostMapping("polling/poll")
  @CrossOrigin
  public DeferredResult<Object> publisher(@RequestBody Map<String, Object> ctx) {
    DeferredResult<Object> output = new DeferredResult<>();
    ObjectHolder<GameStateDto> holder = new ObjectHolder<>();
    consumers.execute(() -> {
      try {

        // ctx.get("tick")  gameService.getGameState(0).getTick()

        synchronized (lock) {
          holders.add(holder);
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
  public Object send(@RequestBody Map<String, Object> ctx) {
    synchronized (lock) {
      for (ObjectHolder<GameStateDto> holder : holders) {
        gameService.addPlayerMove(0, 0, ctx);
        gameEventListener.onNextStateCalculated(() -> holder.set(gameService.getGameState(0)));
      }
      holders.clear();
    }
    return ctx;
  }




}
