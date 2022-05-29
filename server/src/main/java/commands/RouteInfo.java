package commands;

import dao.DataBaseDAO;
import dao.RouteDAO;
import interaction.Response;
import interaction.Status;

public class RouteInfo extends ACommands {
    @Override
    public Response execute(RouteDAO routeDAO, DataBaseDAO dbDAO) {
        try {
            int id = Integer.parseInt(args.get(1));

            response.route(routeDAO.get(id)).status(Status.OK);
            return response;
        } catch (RuntimeException e) {
            response.msg("ошибка: " + e.getMessage()).status(Status.UNKNOWN_ERROR).route(null);
        }
        return response;
    }
}
