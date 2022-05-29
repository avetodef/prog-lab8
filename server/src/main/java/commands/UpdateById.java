package commands;


import dao.DataBaseDAO;
import dao.RouteDAO;
import interaction.Response;
import interaction.Status;
import utils.Route;

import java.util.Objects;

/**
 * Класс команды UPDATE BY ID, предназначенный для обновления элемента по его id.
 *
 * @param
 */
public class UpdateById extends ACommands {
    {
        isAsker = true;
        isIdAsker = true;
    }


    public Response execute(RouteDAO routeDAO, DataBaseDAO dbDAO) {

        int idFromConsole = Integer.parseInt(args.get(1));
        if (routeDAO.getAll().size() == 0)
            response.msg("пусто. нечего обновлять").status(Status.COLLECTION_ERROR);

        else {

            if (!checkId(idFromConsole, routeDAO))
                response.status(Status.USER_EBLAN_ERROR).msg("элемента с таким id нет. ведите другой id");

            else {
                try {

                    if (Objects.equals(dbDAO.getUsernameByRouteId(idFromConsole), user.getUsername())) {
                        routeDAO.update(idFromConsole, info);
                        dbDAO.update(idFromConsole, info);
                        response.msg("элемент коллекции обновлен").status(Status.OK);
                    } else response.msg("нет прав на обновление чужих элементов").status(Status.USER_EBLAN_ERROR);

                } catch (IndexOutOfBoundsException e) {
                    response.msg("брат забыл айди ввести походу").status(Status.USER_EBLAN_ERROR);
                } catch (NumberFormatException e) {
                    response.msg("леее почему не int ввел братан").status(Status.USER_EBLAN_ERROR);

                } catch (RuntimeException e) {
                    response.msg("чета проихошло..." + e.getMessage()).status(Status.UNKNOWN_ERROR);
                    e.printStackTrace();

                }

            }

        }
        return response;
    }

    private boolean checkId(int id, RouteDAO routeDAO) {

        for (Route route : routeDAO.getAll()) {
            if (route.getId() == id) {
                return true;
            }
        }
        return false;
    }

}
