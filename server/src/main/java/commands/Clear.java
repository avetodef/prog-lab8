package commands;


import dao.DataBaseDAO;
import dao.RouteDAO;
import interaction.Response;
import interaction.Status;
import utils.Route;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Класс команды CLEAR, предназначенный для очистки коллекции
 */
public class Clear extends ACommands {
    static Set<Integer> distanceSet = new HashSet<>();

    public Response execute(RouteDAO routeDAO, DataBaseDAO dbDAO) {

        for (Route route : routeDAO.getAll()) {
            distanceSet.add(route.getDistance());
        }
        for (Route route : routeDAO.getAll()) {
            if (Objects.equals(dbDAO.getUsernameByRouteId(route.getId()), user.getUsername())) {
                routeDAO.delete(route.getId());
                dbDAO.delete(route.getId());
            }
        }

        distanceSet.clear();

        response.msg("ура удалилось")
                .status(Status.OK);
        return response;
    }
}
