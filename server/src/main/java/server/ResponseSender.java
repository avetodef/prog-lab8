package server;

import interaction.Response;
import parsing.JsonConverter;

import java.io.DataOutputStream;
import java.io.IOException;

public class ResponseSender implements Runnable {
    private final DataOutputStream dataOutputStream;
    private final Response response;

    public ResponseSender(DataOutputStream dataOutputStream, Response response) {
        this.dataOutputStream = dataOutputStream;
        this.response = response;
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {

            System.out.println(response);

            dataOutputStream.writeUTF(JsonConverter.serResponse(response));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
