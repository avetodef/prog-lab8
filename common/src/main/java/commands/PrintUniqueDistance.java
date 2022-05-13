package commands;


import dao.DataBaseDAO;
import dao.RouteDAO;
import interaction.Response;
import interaction.Status;

import java.util.HashSet;
import java.util.Set;

/**
 * Класс команды PRINT UNIQUE DISTANCE, предназначенный для вывода значения уникального поля distance
 */
public class PrintUniqueDistance extends ACommands{

    static Set<Integer> distanceSet = new HashSet<>();

    public Response execute(RouteDAO routeDAO, DataBaseDAO dbDAO) {
        if (routeDAO.getAll().size() == 0)
            response.msg("пусто").status(Status.COLLECTION_ERROR);
         else {
            routeDAO.getAll().stream().forEach(r -> distanceSet.add(r.getDistance()));

            response.msg("уникальные значения поля distance: " + distanceSet.toString()).
                    status(Status.OK);

        }

        return response;
    }

}
