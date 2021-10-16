package cz.petrfilip.server.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PoolingDtoIn {

  private String roomId;
  private Integer playerId;
  private Integer tick;
}
