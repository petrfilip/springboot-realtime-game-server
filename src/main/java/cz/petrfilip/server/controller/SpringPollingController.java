package cz.petrfilip.server.controller;

import cz.petrfilip.server.GameState;
import cz.petrfilip.server.ObjectHolder;
import cz.petrfilip.server.event.GameEvent;
import cz.petrfilip.server.event.GameEventCallback;
import cz.petrfilip.server.event.GameEventListener;
import cz.petrfilip.server.room.RoomService;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import org.springframework.context.ApplicationEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@CrossOrigin
public class SpringPollingController {

  private final RoomService roomService;
  private final GameEventListener gameEventListener;

  public SpringPollingController(RoomService roomService, GameEventListener gameEventListener) {
    this.roomService = roomService;
    this.gameEventListener = gameEventListener;
  }


  @PostMapping("polling/poll")
  @CrossOrigin
  public DeferredResult<ResponseEntity<?>> handleReqDefResult(@RequestBody PoolingDtoIn dtoIn) {
    DeferredResult<ResponseEntity<?>> output = new DeferredResult<>();
    ObjectHolder<GameState> holder = new ObjectHolder<>();
    ForkJoinPool.commonPool().submit(() -> {
      gameEventListener.onChange(new GameEventCallback() {
        @Override
        public boolean supports(ApplicationEvent tickEvents) {
          return tickEvents.getClass().isAssignableFrom(GameEvent.class) && ((GameEvent) tickEvents).getRoomId().equals(dtoIn.getRoomId());
        }

        @Override
        public void callback() {
          holder.set(roomService.getGameState(dtoIn.getRoomId(), dtoIn.getPlayerId(), dtoIn.getTick()));
        }
      });
      output.setResult(ResponseEntity.ok(holder.get()));
    });

    output.onTimeout(() -> output.setResult(ResponseEntity.ok(roomService.getRoomById(dtoIn.getRoomId()))));

    return output;
  }

  @PostMapping(path = "polling/send")
  @CrossOrigin
  public void send(@RequestBody Map<String, Object> playerMove) {
    roomService.addPlayerMove(playerMove);
  }


}
