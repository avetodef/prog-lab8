package commands;


import dao.DataBaseDAO;
import dao.RouteDAO;
import exceptions.EmptyInputException;
import file.FileManager;
import interaction.Response;
import interaction.Status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс команды EXECUTE SCRIPT, предназначенный для чтения и исполнения скрипта из файла
 */

public class ExecuteScript extends ACommands {

    private final FileManager manager = new FileManager();
    private final RouteDAO dao = manager.read();

    public ExecuteScript() {
    }

    public Response execute(RouteDAO routeDAO, DataBaseDAO dbDAO) {

        String nameOfScript = args.get(1); //ok

        if (ExecuteReader.checkNameOfFileInList(nameOfScript)) {

            ExecuteReader.listOfNamesOfScripts.add(nameOfScript);

            try {
                List<String> listOfCommands = Files.readAllLines(Paths.get(nameOfScript + ".txt").toAbsolutePath());

                for (String lineOfFile : listOfCommands
                ) {
                    ACommands commands;
                    String command = lineOfFile.trim();

                    if (command.isEmpty()) {
                        throw new EmptyInputException();
                    }
                    List<String> args = new ArrayList<>(Arrays.asList(command.split(" ")));

                    try {
                        commands = CommandSaver.getCommand(args);
                        commands.execute(dao, dbDAO);

                        response = commands.response;
                    } catch (RuntimeException e) {
                        response.msg("ты норм? в скрипте параша написана, переделывай").
                                status(Status.USER_EBLAN_ERROR);
                    }
                }
            } catch (NoSuchFileException e) {
                response.msg("файл не найден").status(Status.FILE_ERROR);
            } catch (IOException e) {
                response.msg("Все пошло по пизде, чекай мать: " + e.getMessage()).
                        status(Status.UNKNOWN_ERROR);

            }
            ExecuteReader.listOfNamesOfScripts.clear();
        } else {
            response.msg("пу пу пу.... обнаружена рекурсия").status(Status.USER_EBLAN_ERROR);
        }
        return response;
    }
}
