package client;

import client.gui.StartingStage;
import interaction.Request;
import interaction.Response;
import interaction.User;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import json.JsonConverter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NotYetConnectedException;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

//TODO где то падает весь джавафкс. положить джаву может не каждый джуниор девелопер. но и я не промах
public class ReaderSender {

    public User user;
    public  SocketChannel socketChannel;
    public void setUser(User user) {
        this.user = user;
    }


    public void sendToServer(Request request) {
        try {
            request.setUser(user);
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
            serverDied();
//            while(!socketChannel.isConnected()) {
//                reconnect();
//            }
        }
    }

    public void reconnect(){
        synchronized (socketChannel) {
            try {
                socketChannel.wait();
                socketChannel.connect(new InetSocketAddress("localhost", 6666));
                if (socketChannel.isConnected()) {
                    System.out.println("connected!");

                } else
                    System.out.println("not yet connected...");
                socketChannel.notifyAll();
            }
            catch (IOException | InterruptedException e) {
                //exception.printStackTrace();
                System.out.println("cannot connect right now... try again later");
                System.out.println(e+ " " + e.getMessage());
                //e.printStackTrace();
            }
            //socketChannel.notifyAll();
        }
    }



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

        } catch (NotYetConnectedException e) {
            while(!socketChannel.isConnected()) {
                reconnect();
            }
        } catch (ClosedChannelException e) {
            System.out.println("IO problems in reader sender line 145: " + e.getMessage());
            while(!socketChannel.isConnected()) {
                reconnect();
            }
        } catch (IOException e) {
            System.out.println("IO problems in reader sender line 148: " + e.getMessage());
        }
        return null;
    }

    private void serverDied() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(StartingStage.class.getResource("/client/server_die.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            //stage.show();
            //stage.showAndWait();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOnCloseRequest(evt -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Ben сказал что это окно нельзя закрывать.", ButtonType.OK);
                ButtonType result = alert.showAndWait().orElse(ButtonType.OK);
                if(ButtonType.OK.equals(result)){evt.consume();}
            });
            //stage.initOwner(btn1.getScene().getWindow());
            stage.showAndWait();

        }
        catch (IOException e) {
            System.out.println("IO problems in reader sender line 97: " + e.getMessage());
        }
    }
}
