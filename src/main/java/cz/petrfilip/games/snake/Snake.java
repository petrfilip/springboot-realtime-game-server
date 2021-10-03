package cz.petrfilip.games.snake;

import cz.petrfilip.server.common.Point;
import java.util.LinkedList;
import lombok.Data;

@Data
public class Snake {

  private LinkedList<SnakeBody> body = new LinkedList<>();

  private SnakeDirectionEnum direction;

  public Snake() {
  }

  public Snake(Point point, SnakeDirectionEnum direction) {
    this.body.add(new SnakeBody(point));
    this.direction = direction;
  }

  public Snake(Integer x, Integer y, SnakeDirectionEnum direction) {
    this.body.add(new SnakeBody(x, y));
    this.direction = direction;
  }


}
