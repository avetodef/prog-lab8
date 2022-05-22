package client;

import client.gui.StartingStage;
import interaction.Request;
import interaction.Response;
import interaction.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import json.JsonConverter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ReaderSender {

    protected void readAndSend(List<String> input, Request request, SocketChannel socketChannel) {}
    public User user;

    public void setUser(User user) {
        this.user = user;
    }

    public void sendToServer(Request request) {
        try {
            request.setUser(user);
            System.out.println("READER SENDER USER_0: " + user);
            socketChannel.write(StandardCharsets.UTF_8.encode(JsonConverter.ser(request)));
        } catch (IOException e) {
            serverDied();
        }
    }

    private void serverDied(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartingStage.class.getResource("/client/server_die.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        }
        catch (IOException e){
            System.out.println(e.getMessage() );
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
