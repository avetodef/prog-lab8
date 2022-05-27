package commands;


import dao.DataBaseDAO;
import dao.RouteDAO;
import interaction.Response;
import interaction.Status;

import java.util.Objects;

/**
 * Класс команды REMOVE BY ID, предназначенный для удаления элемента по его id
 *
 * @param
 */
public class RemoveById extends ACommands {
    {
        isIdAsker = true;
    }
    public Response execute(RouteDAO routeDAO, DataBaseDAO dbDAO) {
        if (routeDAO.getAll().size() == 0) {
            response.setMsg("коллекция пустая. нечего удалять");
            response.setStatus(Status.COLLECTION_ERROR);
        } else {
            try {
                int id = Integer.parseInt(args.get(1));

                if(Objects.equals(dbDAO.getUsernameByRouteId(id), user.getUsername())) {

                    if (!routeDAO.delete(id) && !dbDAO.delete(id))
                        response.msg("нет элемента с таким id. введите команду заново с правильным id").
                                status(Status.USER_EBLAN_ERROR);
                    else {
                        response.msg("ура удалилось").status(Status.OK);
                    }
                }
                else response.status(Status.USER_EBLAN_ERROR).msg("нет прав на удаление чужих элементов");

            }
            catch (IndexOutOfBoundsException e){
                response.status(Status.USER_EBLAN_ERROR).msg("брат забыл айди ввести походу ");
            }
            catch (NumberFormatException e){
                response.msg("леее почему не int ввел братан").status(Status.USER_EBLAN_ERROR);

            }
            catch (RuntimeException e) {
                response.msg("непредвиденная ошибка в классе команды: " + e.getMessage())
                        .status(Status.UNKNOWN_ERROR);
            }

        }
        return response;
    }

}
