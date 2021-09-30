// package cz.petrfilip.multiplayerserver.controller;
//
//
// import cz.petrfilip.multiplayerserver.GameService;
// import cz.petrfilip.multiplayerserver.ObjectHolder;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Map;
// import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RestController;
//
// @RestController
// @CrossOrigin
// public class PollingController {
//
//   private final GameService gameService;
//
//   public PollingController(GameService gameService) {
//     gameService.addPlayer(0);
//     this.gameService = gameService;
//
//   }
//
//   private final Object lock = new Object();
//
//   private final List<ObjectHolder<Object>> holders = new ArrayList<>();
//
//
//
//   @PostMapping(path = "polling/poll")
//   @CrossOrigin
//   public Object poll(@RequestBody Map<String, Object> ctx) {
//     ObjectHolder<Object> holder = new ObjectHolder<>();
//     synchronized (lock) {
//       holders.add(holder);
//     }
//     return holder.get();
//   }
//
//   @PostMapping(path = "polling/send")
//   @CrossOrigin
//   public Object send(@RequestBody Map<String, Object> ctx) {
//     synchronized (lock) {
//       for (ObjectHolder<Object> holder : holders) {
//         gameService.addPlayerMove(0, 0, ctx);
//         holder.set(gameService.getGameState(0));
//       }
//       holders.clear();
//     }
//     return ctx;
//   }
//
// }
