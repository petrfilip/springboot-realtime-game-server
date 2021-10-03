package cz.petrfilip.games.snake;

import cz.petrfilip.server.common.Point;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SnakeBody extends Point {

  public SnakeBody(Integer x, Integer y) {
    super(x, y);
  }

  public SnakeBody(Point point) {
    super(point.getX(), point.getY());
  }
}
