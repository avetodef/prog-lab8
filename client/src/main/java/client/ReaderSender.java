package client;

import client.gui.StartingStage;
import interaction.Request;
import interaction.Response;
import interaction.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import json.JsonConverter;

import java.io.IOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;


public class ReaderSender {

    public User user;

    public void setUser(User user) {
        this.user = user;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public void sendToServer(Request request) {
        try {

            request.setUser(user);
            //System.out.println("READER SENDER USER_0: " + user);
            socketChannel.write(StandardCharsets.UTF_8.encode(JsonConverter.ser(request)));
        }
        catch (SocketException e){
            System.out.println(e.getMessage());
            serverDied();
        }
        catch (IOException e) {
            System.out.println(e.getMessage() + " " + e + " READER SENDER 44");
            //serverDied();
        }
    }

    private void serverDied(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartingStage.class.getResource("/client/server_die.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            //stage.show();
            //stage.showAndWait();
            stage.initModality(Modality.APPLICATION_MODAL);
            //stage.initOwner(btn1.getScene().getWindow());
            stage.showAndWait();
        }
        catch (IOException e){
            System.out.println(e.getMessage() );
        }
    }
    SocketChannel socketChannel;

    public ReaderSender(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }
    ByteBuffer buffer = ByteBuffer.allocate(60000);

    public Response read() {
        try {
            socketChannel.read(buffer);

            buffer.flip();
            String serverResponse = StandardCharsets.UTF_8.decode(buffer).toString().substring(2);

            Response response = JsonConverter.desResponse(serverResponse);

            buffer.clear();
            return response;

        }

        catch (ClosedChannelException e){
            //System.out.println("response где-то потерялся " + e.getMessage());
            //serverDied();
            e.printStackTrace();
        }
        catch (IOException e) {
            //serverDied();
            System.out.println(e.getMessage() + " READER SENDER " + e);
        }
        return null;
    }
}
