package commands;

import dao.DataBaseDAO;
import dao.RouteDAO;
import interaction.Response;

public class GetAll extends ACommands {
    @Override
    public Response execute(RouteDAO routeDAO, DataBaseDAO dbDAO) {
        response.setCollection(dbDAO.getAll());
        response.setAnimationRouteList(dbDAO.getAnimationRoute());
        response.setRouteArrayList(dbDAO.getArrayListOfRoutes());

        return response;

    }
}
