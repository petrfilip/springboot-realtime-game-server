package cz.petrfilip.server.room;

import cz.petrfilip.server.GameState;
import cz.petrfilip.server.ObjectHolder;
import cz.petrfilip.server.controller.PoolingDtoIn;
import cz.petrfilip.server.event.GameEventCallback;
import cz.petrfilip.server.event.GameEventListener;
import cz.petrfilip.server.room.dto.CreateRoomDtoIn;
import cz.petrfilip.server.room.dto.JoinRoomDtoIn;
import cz.petrfilip.server.room.dto.RoomEvent;
import cz.petrfilip.server.room.dto.RoomState;
import cz.petrfilip.server.room.dto.StartGameInRoomDtoIn;
import java.util.Collection;
import java.util.concurrent.ForkJoinPool;
import org.springframework.context.ApplicationEvent;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@CrossOrigin
public class RoomController {

  private final RoomService roomService;
  private final GameEventListener gameEventListener;

  public RoomController(RoomService roomService, GameEventListener gameEventListener) {
    this.roomService = roomService;
    this.gameEventListener = gameEventListener;
  }

  @PostMapping(path = "/room/create")
  @CrossOrigin
  public RoomState createRoom(@RequestBody CreateRoomDtoIn dtoIn) {
    return roomService.create(dtoIn);
  }

  @PostMapping(path = "/room/join")
  @CrossOrigin
  public RoomState joinToRoom(@RequestBody JoinRoomDtoIn dtoIn) {
    return roomService.joinPlayer(dtoIn);
  }

  @PostMapping(path = "/room/startGame")
  @CrossOrigin
  public GameState startGame(@RequestBody StartGameInRoomDtoIn dtoIn) {
    return roomService.startGame(dtoIn);
  }


  @GetMapping(path = "/room/list")
  @CrossOrigin
  public Collection<RoomState> list() {
    return roomService.roomList();
  }

  @PostMapping("room/poll")
  @CrossOrigin
  public DeferredResult<ResponseEntity<RoomState>> getRoomLiveInfo(@RequestBody PoolingDtoIn dtoIn) {
    DeferredResult<ResponseEntity<RoomState>> output = new DeferredResult<>();
    ObjectHolder<RoomState> holder = new ObjectHolder<>();
    ForkJoinPool.commonPool().submit(() -> {
      gameEventListener.onChange(new GameEventCallback() {
        @Override
        public boolean supports(ApplicationEvent tickEvents) {
          return tickEvents.getClass().isAssignableFrom(RoomEvent.class) && ((RoomEvent) tickEvents).getRoomId().equals(dtoIn.getRoomId());
        }

        @Override
        public void callback() {
          holder.set(roomService.getRoomById(dtoIn.getRoomId()));
        }
      });
      output.setResult(ResponseEntity.ok(holder.get()));
    });

    // usually this is not updated very often
    output.onTimeout(() -> output.setResult(ResponseEntity.ok(roomService.getRoomById(dtoIn.getRoomId()))));

    return output;
  }

}
