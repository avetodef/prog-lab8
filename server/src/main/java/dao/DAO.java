package dao;


import com.google.inject.AbstractModule;
import utils.Route;
import utils.RouteInfo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public interface DAO {
    int create(Route route);

    void update(int id, RouteInfo routeInfo);

    void delete(int id);

    Deque<Route> getAll();


    class DAOModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(Deque.class).to(ArrayDeque.class);
            bind(List.class).to(ArrayList.class);
        }
    }
}
