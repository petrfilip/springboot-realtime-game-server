package cz.petrfilip.games.snake;

import java.util.LinkedList;
import java.util.Queue;
import lombok.Data;

@Data
public class Snake {

  private Queue<SnakeBody> body = new LinkedList<>();

  private SnakeDirectionEnum direction;

  public Snake() {
  }

  public Snake(Integer x, Integer y, SnakeDirectionEnum direction) {
    this.body.add(new SnakeBody(x, y));
    this.direction = direction;
  }


}
