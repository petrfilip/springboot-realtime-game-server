package cz.petrfilip.games.draw;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class DrawGamePallet {

  private List<DrawGamePoint> points = new ArrayList<>();

}
