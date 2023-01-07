package com.ashutosh.games.web.decoder;

import com.ashutosh.games.web.model.ServerMessage;
import com.google.gson.Gson;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class ServerMessageDecoder implements Decoder.Text<ServerMessage> {

    private static Gson gson = new Gson();

    @Override
    public ServerMessage decode(String s) throws DecodeException {
        ServerMessage message = gson.fromJson(s, ServerMessage.class);
        return message;
    }

    @Override
    public boolean willDecode(String s) {
        return (s != null);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Custom initialization logic
    }

    @Override
    public void destroy() {
        // Close resources
    }
}
