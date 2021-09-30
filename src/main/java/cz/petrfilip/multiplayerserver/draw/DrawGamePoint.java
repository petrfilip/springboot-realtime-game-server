package cz.petrfilip.multiplayerserver.draw;

import java.io.Serializable;
import lombok.Data;

@Data
public class DrawGamePoint implements Serializable {

  private Integer x;
  private Integer y;
  private String color;

}
