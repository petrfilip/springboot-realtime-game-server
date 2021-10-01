package cz.petrfilip.server.controller;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import cz.petrfilip.server.Player;
import java.io.IOException;

public class PlayerSerializer extends JsonSerializer<Player> {


  @Override
  public void serialize(Player value, JsonGenerator gen, SerializerProvider provider) throws IOException {
    gen.writeFieldName(String.valueOf(value.getPlayerId()));
  }

}
