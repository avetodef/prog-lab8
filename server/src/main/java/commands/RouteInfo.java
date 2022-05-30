package commands;

import dao.DataBaseDAO;
import dao.RouteDAO;
import interaction.Response;
import interaction.Status;
import utils.Route;

import java.util.Deque;


public class RouteInfo extends ACommands {
    @Override
    public Response execute(RouteDAO routeDAO, DataBaseDAO dbDAO) {
        try {

            int id = Integer.parseInt(args.get(1));

            Deque<Route> collection = dbDAO.getAll();
            for (Route r : collection) {
                if (r.getId() == id) {
                    System.out.println(r);
                    response.route(r).status(Status.OK);
                }
            }

            //response.msg(routeDAO.get(id).toString()).status(Status.OK);

            //response.route(routeDAO.get(id)).status(Status.OK);
            return response;
        } catch (RuntimeException e) {
            response.msg("ошибка: " + e.getMessage()).status(Status.UNKNOWN_ERROR).route(null);
        }
        return response;
    }
}
