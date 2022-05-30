package commands;

import dao.DataBaseDAO;
import dao.RouteDAO;
import interaction.Response;
import interaction.Status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RouteInfo extends ACommands {
    @Override
    public Response execute(RouteDAO routeDAO, DataBaseDAO dbDAO) {
        try {
            int id = Integer.parseInt(args.get(1));

            response.msg(routeDAO.get(id).toString()).status(Status.OK);

            return response;
        } catch (RuntimeException e) {
            response.msg("ошибка: " + e.getMessage()).status(Status.UNKNOWN_ERROR).route(null);
        }
        return response;
    }
}
