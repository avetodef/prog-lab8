package commands;

import dao.DataBaseDAO;
import dao.RouteDAO;
import interaction.Response;
import interaction.Status;

public class GetLocations extends ACommands {
    @Override
    public Response execute(RouteDAO routeDAO, DataBaseDAO dbDAO) {
        response.status(Status.OK).routeList(dbDAO.getAnimationRoute());
        return response;
    }
}
