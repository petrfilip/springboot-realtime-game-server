package cz.petrfilip.games.snake;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SnakeBody {

  private Integer x;
  private Integer y;

  public Integer getX() {
    return Math.abs(x);
  }

  public Integer getY() {
    return Math.abs(y);
  }
}
