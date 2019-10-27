package com.maarten551.tictactoe_backend.model.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.awt.Color;

import java.io.IOException;

public class ColorToHexStringSerializer extends StdSerializer<Color> {
    protected ColorToHexStringSerializer(Class<Color> t) {
        super(t);
    }

    public ColorToHexStringSerializer() {
        this(null);
    }

    @Override
    public void serialize(Color color, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String hexColorString = String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
        jsonGenerator.writeString(hexColorString);
    }
}
