import commands.ACommands;
import commands.CommandSaver;
import console.Console;
import console.ConsoleOutputer;
import interaction.Request;
import json.JsonConverter;
import utils.RouteInfo;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ReaderSender {
    private final ConsoleOutputer o = new ConsoleOutputer();
    protected void readAndSend(List<String> input, Request request, SocketChannel socketChannel, Console console) throws
            IOException {
        boolean flag = true;

        if (CommandSaver.checkCommand(input)) {

            ACommands command = CommandSaver.getCommand(input);
            if (command.isIdAsker()) {
                if (input.size() != 2 || Integer.parseInt(input.get(1)) < 0 || input.get(1).contains(".") || input.get(1).contains(",")) {
                    o.printRed("введи нормальный айди");
                    flag = false;
                }
            }
            if (flag) {
                if (command.isAsker()) {
                    RouteInfo info = console.info();
                    request.setInfo(info);
                }

                socketChannel.write(StandardCharsets.UTF_8.encode(JsonConverter.ser(request)));
                o.printWhite("sending to the server...");
            } else o.printRed("ну значит не отправлю на сервер твою команду. заново вводи");

        } else
            throw new NullPointerException("Введённой вами команды не существует. Попробуйте ввести другую команду.");

    }

}
