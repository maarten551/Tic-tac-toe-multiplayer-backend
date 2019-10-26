package com.maarten551.tictactoe_backend.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.maarten551.tictactoe_backend.model.Player;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class PlayerColorSerializer extends StdSerializer<HashMap<Player, Color>> {
    protected PlayerColorSerializer(Class<HashMap<Player, Color>> t) {
        super(t);
    }

    public PlayerColorSerializer() {
        this(null);
    }

    @Override
    public void serialize(HashMap<Player, Color> playerColorHashMap, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        HashMap<String, String> translatedFields = new HashMap<>();

        playerColorHashMap.forEach((player, color) -> {
            String colorFormat = String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
            translatedFields.put(player.getSessionId(), colorFormat);
        });

        jsonGenerator.writeObject(translatedFields);
    }
}
