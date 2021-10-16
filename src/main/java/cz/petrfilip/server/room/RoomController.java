package cz.petrfilip.server.room;

import cz.petrfilip.server.GameState;
import cz.petrfilip.server.room.dto.CreateRoomDtoIn;
import cz.petrfilip.server.room.dto.JoinRoomDtoIn;
import cz.petrfilip.server.room.dto.RoomState;
import cz.petrfilip.server.room.dto.StartGameInRoomDtoIn;
import java.util.Collection;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class RoomController {

  private final RoomService roomService;

  public RoomController(RoomService roomService) {
    this.roomService = roomService;
  }

  @PostMapping(path = "/room/create")
  @CrossOrigin
  public RoomState createRoom(@RequestBody CreateRoomDtoIn dtoIn) {
    return roomService.create(dtoIn);
  }

  @PostMapping(path = "/room/join")
  @CrossOrigin
  public RoomState joinToRoom(@RequestBody JoinRoomDtoIn dtoIn) {
    return roomService.join(dtoIn);
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

}
