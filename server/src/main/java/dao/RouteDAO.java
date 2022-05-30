package dao;

import utils.Route;
import utils.RouteInfo;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Optional;

public class RouteDAO implements DAO {

    private  Deque<Route> collection = new ArrayDeque<>();

//    public String initDate = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy : HH.mm.ss"));
public ZonedDateTime initDate = ZonedDateTime.now();

    public int create(Route route) {
        collection.add(route);
        return 1;
    }

    /**
     * Метод, который позволяет обновить элемент коллекции по его id
     *
     * @param id        - id элемента, который пользователь хочет обновить
     * @param routeInfo - характеристики, свойственные элементы коллекции
     */

    public boolean update(int id, RouteInfo routeInfo) {
        for (Route r : collection) {
            if (r.getId() == id) {
                r.update(routeInfo);
                return true;
            }
        }
        return false;
    }

    public boolean delete(int id) {
        return collection.removeIf(r -> r.getId() == id);
    }

    public Route get(int id) {
        for (Route route : collection) {
            if (route.getId() == id) {
                return route;
            }
        }
        return null;
    }

    public Deque<Route> getAll() {
        return new ArrayDeque<>(collection);
    }

    public boolean removeById(int id){
        Optional<Route> route = collection.stream()
                .filter(r->r.getId()==id)
                .findFirst();
        if (route.isPresent()){
            collection.remove(route.get());
            return true;
        }
        return false;
    }


    public String getDescription() {

        StringBuilder out = new StringBuilder();
        for (Route route : collection)
            out.append(route.getDescription()).append(System.lineSeparator());

        return out.toString();
    }
    public String printFirst() {
        return (collection.getFirst().toString());
    }

    /**
     * Метод, который позволяет удалить первый элемнт коллекции
     */
    public void removeFirst() {
        collection.removeIf(route -> route.equals(collection.getFirst()));
    }

    /**
     * Метод, который позволяет вывести всю коллекцию
     *
     * @return collection
     */
    public String getCollection() {
        StringBuilder out = new StringBuilder(" ");
        for (Route route : collection)
            out.append(route.toString()).append(System.lineSeparator());
        return (out.toString());
    }

    @Override
    public String toString() {
        return "Route{" + "type: " + collection.getClass().getSimpleName() + "," + "size: " + collection.size() + "," +
                "initDate: " + initDate + '\'' +
                '}';
    }

    public int getMaxId() {
        if (collection.isEmpty()) return 0;
        return collection.getLast().getId();
    }

    public int getSize() {
        return collection.size();
    }


    public RouteDAO(Deque<Route> collection) {
        this.collection = collection;
    }
    public RouteDAO(){}

}
