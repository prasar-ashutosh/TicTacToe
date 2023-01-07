package com.ashutosh.games.web.encoder;

import com.ashutosh.games.web.model.ServerMessage;
import com.google.gson.Gson;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class ServerMessageEncoder implements Encoder.Text<ServerMessage> {

    private static Gson gson = new Gson();

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Custom initialization logic
    }

    @Override
    public void destroy() {
        // Close resources
    }

    @Override
    public String encode(ServerMessage ServerMessage) throws EncodeException {
        String json = gson.toJson(ServerMessage);
        return json;
    }
}
