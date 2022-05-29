package commands;

import dao.DataBaseDAO;
import dao.RouteDAO;
import interaction.Response;
import interaction.Status;
import json.JsonConverter;

public class GetRoutesForAnimation extends ACommands {
    @Override
    public Response execute(RouteDAO routeDAO, DataBaseDAO dbDAO) {
        try {
            //String jsonedRoutes = JsonConverter.serializeRoute(dbDAO.getAnimationRoute());
            response.status(Status.OK).routeList(dbDAO.getAnimationRoute());
        } catch (RuntimeException e) {
            response.msg(e.getMessage()).status(Status.UNKNOWN_ERROR);
            e.printStackTrace();
        }

        return response;
    }
}
