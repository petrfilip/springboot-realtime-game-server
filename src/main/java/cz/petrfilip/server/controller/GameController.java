package cz.petrfilip.server.controller;

import cz.petrfilip.server.GameService;
import cz.petrfilip.server.GameState;
import java.util.Map;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class GameController {

  private final GameService gameService;

  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @PostMapping(path = "start")
  @CrossOrigin
  public GameState send(@RequestBody Map<String, Object> gameParameters) {
    gameService.startGame(gameParameters);
    return gameService.addPlayer(0);
  }

  @PostMapping(path = "join")
  @CrossOrigin
  public GameState join(@RequestBody Map<String, Object> gameParameters) {
    // return gameService.addPlayer(gameParameters);
    return null;
  }

}
