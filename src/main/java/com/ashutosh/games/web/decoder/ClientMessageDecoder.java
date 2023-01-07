package com.ashutosh.games.web.decoder;

import com.ashutosh.games.web.model.ClientMessage;
import com.google.gson.Gson;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class ClientMessageDecoder implements Decoder.Text<ClientMessage> {

    private static Gson gson = new Gson();

    @Override
    public ClientMessage decode(String s) throws DecodeException {
        ClientMessage message = gson.fromJson(s, ClientMessage.class);
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
