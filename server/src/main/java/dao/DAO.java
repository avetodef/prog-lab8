package dao;


import utils.Route;
import utils.RouteInfo;

import java.util.Deque;

public interface DAO {
    int create(Route route);
    boolean update(int id, RouteInfo routeInfo);
    boolean delete(int id);
    Deque<Route> getAll();

    //Map<String, String> getDescription();
}
