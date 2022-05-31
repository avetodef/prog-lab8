package commands;

import dao.DataBaseDAO;
import dao.RouteDAO;
import interaction.Response;
import interaction.Status;
import utils.Route;

import java.util.Deque;

public class GetRoutesForAnimation extends ACommands {
    @Override
    public Response execute(RouteDAO routeDAO, DataBaseDAO dbDAO) {
        try {
            //String jsonedRoutes = JsonConverter.serializeRoute(dbDAO.getAnimationRoute());
            Deque<Route> collection = dbDAO.getAll();
            response.setStatus(Status.OK);
            response.setCollection(collection);
            response.routeList(dbDAO.getAnimationRoute());
        } catch (RuntimeException e) {
            response.msg(e.getMessage()).status(Status.UNKNOWN_ERROR);
            e.printStackTrace();
        }

        return response;
    }
}
