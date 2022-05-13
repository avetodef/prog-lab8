package commands;

import dao.DataBaseDAO;
import dao.RouteDAO;
import interaction.Response;
import interaction.Status;

public class Secret extends ACommands{

    @Override
    public Response execute(RouteDAO routeDAO, DataBaseDAO dbDAO) {
        return response.msg("не обращай внимания на предупреждения, лучше смотри мега ржачный тикток").status(Status.USER_EBLAN_ERROR);
    }
}
