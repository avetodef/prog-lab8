package client;

import interaction.Request;
import interaction.Response;
import json.JsonConverter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ReaderSender {

    protected void readAndSend(List<String> input, Request request, SocketChannel socketChannel) {}

    public void sendToServer(Request request) {
        try {
            socketChannel.write(StandardCharsets.UTF_8.encode(JsonConverter.ser(request)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    SocketChannel socketChannel;

    public ReaderSender(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    ByteBuffer buffer = ByteBuffer.allocate(40000);

    public Response read() {
        try {
            socketChannel.read(buffer);

            buffer.flip();
            String serverResponse = StandardCharsets.UTF_8.decode(buffer).toString().substring(2);

            Response response = JsonConverter.desResponse(serverResponse);

            buffer.clear();
            return response;

        } catch (IOException e) {
            System.out.println("IO ");
        }
        return null;
    }
}
