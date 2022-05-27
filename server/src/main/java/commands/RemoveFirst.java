package commands;


import dao.DataBaseDAO;
import dao.RouteDAO;
import interaction.Response;
import interaction.Status;

/**
 * Класс команды REMOVE FIRST, предназначенный для удаления первого элемента из коллекции
 */
public class RemoveFirst extends ACommands{

    public Response execute(RouteDAO routeDAO, DataBaseDAO dbDAO) {
        if (routeDAO.getAll().size() == 0)
            response.status(Status.COLLECTION_ERROR).msg("коллекция пустая. нечего удалять");
            else {
            routeDAO.removeFirst();
            dbDAO.removeFirst(routeDAO);
            response.msg("первый элемент удалился ура")
                            .status(Status.OK);

        }
        return response;
    }

}
