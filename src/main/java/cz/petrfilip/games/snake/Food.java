package cz.petrfilip.games.snake;

import cz.petrfilip.server.common.Point;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Food extends Point {

  public Food(Integer x, Integer y) {
    super(x, y);
  }

  public Food(Point point) {
    super(point.getX(), point.getY());
  }
}
