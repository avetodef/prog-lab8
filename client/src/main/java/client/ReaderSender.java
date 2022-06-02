package client;

import client.gui.StartingStage;
import interaction.Request;
import interaction.Response;
import interaction.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import parsing.JsonConverter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

//TODO где то падает весь джавафкс. положить джаву может не каждый джуниор девелопер. но и я не промах
public class ReaderSender {

    public User user;
    public SocketChannel socketChannel;

    public void setUser(User user) {
        this.user = user;
    }


    public void sendToServer(Request request) {
        try {
            request.setUser(user);
            socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 6666));

            socketChannel.write(StandardCharsets.UTF_8.encode(JsonConverter.ser(request)));
        }
        catch (NotYetConnectedException e) {

            try {
                socketChannel.connect(new InetSocketAddress("localhost", 6666));
                if (socketChannel.isConnected())
                    System.out.println("connected and ready to send server messages!");
                else
                    System.out.println("not yet connected...");

            } catch (IOException exception) {
                System.out.println("IO problems in reader sender line 45: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("IO problems in reader sender line 48: " + e.getMessage());
            if (!reconnect())
                serverDied();
        }
    }



    public ReaderSender(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    private ByteBuffer buffer = ByteBuffer.allocate(60000);

    public Response read() {
        try {

            socketChannel.read(buffer);

            buffer.flip();
            String serverResponse = StandardCharsets.UTF_8.decode(buffer).toString().substring(2);

            Response response = JsonConverter.desResponse(serverResponse);

            buffer.clear();
            return response;

        } catch (IOException e) {
            System.out.println("cannot read server response: " + e.getMessage() + " " + e);
        }
        return null;
    }

    public void serverDied() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartingStage.class.getResource("/client/server_die.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOnCloseRequest(event -> System.exit(0)); //TODO вообще надо не систем экзитом а через ноуды
            stage.showAndWait();

        } catch (IOException e) {
            System.out.println("IO problems in reader sender line 97: " + e.getMessage());
        }
    }


    private boolean reconnect() {
        try {
            buffer = ByteBuffer.allocate(60000);

            socketChannel = SocketChannel.open();

            socketChannel.connect(new InetSocketAddress("localhost", 6666));

            if (socketChannel.isConnectionPending()) {
                socketChannel.finishConnect();
                System.out.println("CONNECTED TO SERVER");
                return true;
            }

        } catch (IOException e) {
            System.out.println("cannot reconnect right now: " + e.getMessage());
            return false;
        }
        return false;

    }

}
