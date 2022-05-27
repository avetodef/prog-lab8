package commands;


import dao.DataBaseDAO;
import dao.RouteDAO;
import interaction.Response;
import interaction.Status;

/**
 * Класс команды SHOW, предназначенный для вывода коллекции на консоль
 */
public class Show extends ACommands {

    @Override
    public Response execute(RouteDAO routeDAO, DataBaseDAO dbDAO) {
        if (routeDAO.getAll().size() == 0) {
            response.msg("пусто").status(Status.COLLECTION_ERROR);

        }
        else
        response.status(Status.OK).msg(routeDAO.getCollection());

        return response;
    }

}