package com.maarten551.tictactoe_backend.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.maarten551.tictactoe_backend.model.Player;

import java.io.IOException;

public class PlayerToSessionIdSerializer extends StdSerializer<Player> {
    protected PlayerToSessionIdSerializer(Class<Player> t) {
        super(t);
    }

    public PlayerToSessionIdSerializer() {
        this(null);
    }

    @Override
    public void serialize(Player player, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(player.getSessionId());
    }
}
