package commands;

import dao.DataBaseDAO;
import dao.RouteDAO;

import java.util.concurrent.Callable;

public class AutoUpdate implements Callable<RouteDAO> {
    @Override
    public RouteDAO call() {
        return new DataBaseDAO().getDAO();
    }
}
