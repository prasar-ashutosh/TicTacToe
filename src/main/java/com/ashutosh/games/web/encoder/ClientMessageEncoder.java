package com.ashutosh.games.web.encoder;

import com.ashutosh.games.web.model.ClientMessage;
import com.google.gson.Gson;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class ClientMessageEncoder implements Encoder.Text<ClientMessage> {

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
    public String encode(ClientMessage clientMessage) throws EncodeException {
        String json = gson.toJson(clientMessage);
        return json;
    }
}
