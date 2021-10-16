package cz.petrfilip.server.room;

import cz.petrfilip.server.GameService;
import cz.petrfilip.server.GameState;
import cz.petrfilip.server.IRule;
import cz.petrfilip.server.Player;
import cz.petrfilip.server.room.dto.CreateRoomDtoIn;
import cz.petrfilip.server.room.dto.JoinRoomDtoIn;
import cz.petrfilip.server.room.dto.RoomState;
import cz.petrfilip.server.room.dto.RoomStateEnum;
import cz.petrfilip.server.room.dto.StartGameInRoomDtoIn;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

  private final Map<String, RoomState> rooms = new HashMap<>();

  private final ApplicationContext applicationContext;

  public RoomService(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public RoomState create(CreateRoomDtoIn dtoIn) {
    RoomState room = new RoomState();
    room.setRoomId(generatedIdentifier());
    room.setRoomName(dtoIn.getRoomName());
    room.setRoomOwner(new Player(dtoIn.getRoomOwner()));
    room.setGame(createGameInstance(dtoIn.getGameName()));
    room.setState(RoomStateEnum.WAITING);
    rooms.put(room.getRoomId(), room);
    return room;
  }

  public void addPlayerMove(Map<String, Object> playerMove) {
    String roomId = String.valueOf(playerMove.getOrDefault("roomId", null));
    Integer playerId = Integer.parseInt(String.valueOf(playerMove.getOrDefault("playerId", null)));
    Integer tick = Integer.parseInt(String.valueOf(playerMove.getOrDefault("tick", null)));
    rooms.get(roomId).getGame().addPlayerMove(tick, playerId, playerMove);
  }

  public RoomState join(JoinRoomDtoIn dtoIn) {
    if (!rooms.containsKey(dtoIn.getRoomId())) {
      throw new IllegalArgumentException("Room does not exists!");
    }

    RoomState room = rooms.get(dtoIn.getRoomId());

    room.getConnectedPlayers().add(new Player(dtoIn.getPlayerId()));
    return room;
  }

  public GameState startGame(StartGameInRoomDtoIn dtoIn) {
    if (!rooms.containsKey(dtoIn.getRoomId())) {
      throw new IllegalArgumentException("Room does not exists!");
    }

    RoomState room = rooms.get(dtoIn.getRoomId());
    GameService game = room.getGame();

    if (!dtoIn.getPlayerId().equals(room.getRoomOwner().getPlayerId())) {
      throw new IllegalAccessError("Game can be started only by room owner");
    }

    room.setState(RoomStateEnum.ACTIVE);
    for (Player connectedPlayer : room.getConnectedPlayers()) {
      game.addPlayer(connectedPlayer);
    }

    return game.startGame(dtoIn.getGameParameters());
  }

  public Collection<RoomState> roomList() {
    return rooms.values();
  }

  private GameService createGameInstance(String gameName) {
    try {
      GameService gameServiceBean = applicationContext.getBean(GameService.class);
      gameServiceBean.setGame(applicationContext.getBean(gameName, IRule.class));
      return gameServiceBean;
    } catch (BeansException e) {
      throw new IllegalArgumentException("Game " + gameName + " not found!");
    }
  }

  private String generatedIdentifier() {
    return UUID.randomUUID().toString().replaceAll("-", "");
  }

  public GameState getGameState(String roomId, Integer playerId, Integer tick) {
    return rooms.get(roomId).getGame().getGameState();
  }
}
