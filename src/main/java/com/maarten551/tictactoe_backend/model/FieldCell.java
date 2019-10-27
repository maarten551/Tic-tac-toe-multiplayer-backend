package com.maarten551.tictactoe_backend.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.maarten551.tictactoe_backend.model.serializer.ColorToHexStringSerializer;
import com.maarten551.tictactoe_backend.model.serializer.PlayerToSessionIdSerializer;

import java.awt.*;

public class FieldCell {
    @JsonSerialize(using = PlayerToSessionIdSerializer.class)
    public Player selectedByPlayer;
    @JsonSerialize(using = ColorToHexStringSerializer.class)
    public Color colorOfPlayer = Color.WHITE;
}
