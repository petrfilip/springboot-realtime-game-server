package cz.petrfilip.games.snake;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.impl.StringCollectionSerializer;
import cz.petrfilip.server.Player;
import cz.petrfilip.server.controller.PlayerSerializer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class SnakeBoard {


  @JsonSerialize(keyUsing = PlayerSerializer.class)
  private Map<Player, Snake> snakeList = new HashMap<>();

  private List<Food> foodList = new ArrayList<>();


}
