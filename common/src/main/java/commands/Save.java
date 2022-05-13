package commands;


import dao.RouteDAO;
import file.FileManager;
import interaction.Response;

import java.io.IOException;

/**
 * Класс команды SAVE, предназначенный для сохранения элементов в коллекцию
 */
public class Save {

    public static void execute(RouteDAO routeDAO) {
        Response response = new Response();
        FileManager writer = new FileManager();
            try {
                writer.save(routeDAO);

            } catch (RuntimeException | IOException e) {
                System.out.println(e.getMessage());
            }

        }
    }
