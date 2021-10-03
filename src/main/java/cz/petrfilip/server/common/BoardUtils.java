package cz.petrfilip.server.common;

import cz.petrfilip.server.Utils;
import java.util.List;
import java.util.Objects;

public class BoardUtils {

  private BoardUtils() {
  }

  public static Point generatePoint(Integer maxX, Integer maxY) {
    return new Point(Utils.getRandomNumber(0, maxX), Utils.getRandomNumber(0, maxY));
  }


  public static int indexOfPoint(List<? extends Point> occupiedPoints, Point searchedPoint) {
    for (int i = 0; i < occupiedPoints.size(); i++) {
      if (
          Objects.equals(occupiedPoints.get(i).getX(), searchedPoint.getX()) &&
          Objects.equals(occupiedPoints.get(i).getY(), searchedPoint.getY())
      ) {
        return i;
      }
    }
    return -1;
  }


}
