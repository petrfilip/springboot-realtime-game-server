package cz.petrfilip.server.controller;

import cz.petrfilip.server.GameManager;
import cz.petrfilip.server.GameService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController("game")
@CrossOrigin
public class GameController {

  private final GameService gameService;
  private final GameManager gameManager;

  public GameController(GameService gameService, GameManager gameManager) {
    this.gameService = gameService;
    this.gameManager = gameManager;
  }

  // @PostMapping(path = "start")
  // @CrossOrigin
  // public GameState send(@RequestBody Map<String, Object> gameParameters) {
  //   return gameService.startGame(gameParameters);
  // }

  // @GetMapping(path = "list")
  // @CrossOrigin
  // public List<Game> send() {
  //   return gameManager.list();
  // }


}
