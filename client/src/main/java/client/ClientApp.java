package client;

import console.ConsoleOutputer;
import console.ConsoleReader;
import exceptions.ExitException;
import interaction.Request;
import interaction.Response;
import interaction.Status;
import interaction.User;
import json.JsonConverter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class ClientApp{

    private final ConsoleReader consoleReader = new ConsoleReader();
    private final ConsoleOutputer o = new ConsoleOutputer();
    private final Scanner sc = new Scanner(System.in);
    private final ByteBuffer buffer = ByteBuffer.allocate(60_000);

    private User user;
    private boolean isAuth = false;


    Selector selector;

    {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    SocketChannel socketChannel;
    int serverPort = 6666;

    {
        try {
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("localhost", serverPort));
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final ReaderSender readerSender = new ReaderSender(socketChannel);

    protected void mainClientLoop() {
        try {
            if (!isAuth) {
                authorize(selector, socketChannel);
            } else {
                go(selector, socketChannel, user);
            }
        } catch (UnknownHostException e) {
            o.printRed("неизвестный хост. порешай там в коде что нибудь ок?");

        } catch (IOException exception) {
            o.printRed("Сервер пока недоступен. Закончить работу клиента? (напишите {yes} или {no})?");
            String answer;
            try {
                while (!(answer = sc.nextLine()).equals("no")) {
                    switch (answer) {
                        case "":
                            break;
                        case "yes":
                            System.exit(0);
                            break;
                        default:
                            o.printNormal("скажи пожалуйста.... yes или no");
                    }
                }
                o.printNormal("жди...");
            } catch (NoSuchElementException e) {
                throw new ExitException("poka");
            }
        }
    }

    private User authorize(Selector selector, SocketChannel socketChannel) throws IOException {

        Request userRequest = new Request();
        List<String> args = new ArrayList<>(List.of("new user"));
        userRequest.setArgs(args);

        while (true) {
            selector.select();
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();

            while (it.hasNext()) {
                SelectionKey selectionKey = it.next();
                it.remove();
                SocketChannel clientChannel = (SocketChannel) selectionKey.channel();
                clientChannel.register(selector, SelectionKey.OP_CONNECT);

                if (selectionKey.isConnectable()) {
                    connect(clientChannel);
                    clientChannel.register(selector, SelectionKey.OP_WRITE);
                    continue;
                }
                if (selectionKey.isWritable()) {

                    //System.out.println("username");
                    //String username = sc.nextLine().trim();


                    //System.out.println("pass  word");
                    //String password = sc.nextLine().trim();


//                    userRequest.setUser(user);
//                    readerSender.send(socketChannel, userRequest);
                    clientChannel.register(selector, SelectionKey.OP_READ);
                    continue;
                }

                if (selectionKey.isReadable()) {
                    socketChannel.read(buffer);
                    buffer.flip();

                    String serverResponse = StandardCharsets.UTF_8.decode(buffer).toString().substring(2);

                    Response response = JsonConverter.desResponse(serverResponse);
                    printPrettyResponse(response);
                    buffer.clear();

                    if (response.status.equals(Status.OK)) {
                        go(selector, socketChannel, user);
                        clientChannel.register(selector, SelectionKey.OP_WRITE);
                    } else {
                        isAuth = true;
                        return user;
                    }
                }
            }
        }
    }

    private void go(Selector selector, SocketChannel socketChannel, User user) throws IOException {

        while (true) {

            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

            while (iterator.hasNext()) {

                SelectionKey key = iterator.next();
                iterator.remove();
                SocketChannel client = (SocketChannel) key.channel();

                if (key.isConnectable()) {
                    connect(client);
                    client.register(selector, SelectionKey.OP_WRITE);
                    continue;

                }

                if (key.isWritable()) {
                    //try {
                    send();
//                    }
//                    catch (NumberFormatException e) {
//                        o.printRed("int введи");
//                        continue;
//                    } catch (NullPointerException e) {
//                        o.printRed("Введённой вами команды не существует. Попробуйте ввести другую команду.");
//                        continue;
//                    } catch (EmptyInputException e) {
//                        o.printRed(e.getMessage());
//                        continue;
//                    } catch (IndexOutOfBoundsException e) {
//                        o.printRed("брат забыл айди ввести походу");
//                        continue;
//                    }
                    client.register(selector, SelectionKey.OP_READ);
                    continue;
                }

                if (key.isReadable()) {
                    read();
                    client.register(selector, SelectionKey.OP_WRITE);
                }

            }
        }
    }

    public void connect(SocketChannel client) {
        if (client.isConnectionPending()) {
            try {
                client.finishConnect();
                o.printWhite("connection established");
            } catch (IOException e) {
                System.out.println("no connection to server");
            }
        }
    }

    public void send() {
        List<String> input = consoleReader.reader();
        Request request = new Request(input,null, user);
//        readerSender.readAndSend(input, request, socketChannel);
    }

    public Response read() {
        try {
            socketChannel.read(buffer);
            buffer.flip();

            String serverResponse = StandardCharsets.UTF_8.decode(buffer).toString().substring(2);

            Response response = JsonConverter.desResponse(serverResponse);
            printPrettyResponse(response);
            buffer.clear();
            return response;

        } catch (IOException e) {
            System.out.println("CLIENT APP " + e.getMessage());
        }
        return null;
    }

    public void runClient() {
        while (true) {
            try {
                mainClientLoop();
            } catch (ExitException e) {
                o.printRed("heheheha");
                break;
            } catch (RuntimeException e) {
                o.printRed("ошибка.....: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }

    private void printPrettyResponse(Response r) {
        switch (r.status) {
            case OK: {
                o.printNormal(r.msg);
                break;
            }
            case FILE_ERROR: {
                o.printBlue(r.msg);
                break;
            }
            case UNKNOWN_ERROR: {
                o.printRed(r.msg);
                break;
            }
            case COLLECTION_ERROR: {
                o.printYellow(r.msg);
                break;
            }
            case USER_EBLAN_ERROR: {
                o.printPurple(r.msg);
                break;
            }
            case SERVER_ERROR: {
                o.printRed(r.msg);
            }
        }
    }
}