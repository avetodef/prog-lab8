package utils;




import com.fasterxml.jackson.annotation.JsonFormat;
import interaction.User;
import lombok.Setter;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


public class Route {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy : HH.mm.ss")
    @Setter
    private ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Location from; //Поле не может быть null
    private utils.loc.Location to; //Поле может быть null
    private Integer distance; //Поле не может быть null, Значение поля должно быть больше 1
    private User user;
//    public static void builder() {
//    }

    public String getDescription() {
        return id + "," + name + "," + coordinates.getCoorX() + "," + coordinates.getCoorY() + "," + creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy : HH.mm.ss")) + "," + from.getFromX() + "," + from.getFromY() + ","
                + from.getName() + "," + to.getToX() + "," + to.getToY() + "," + to.getName() + "," + distance;
    }

    public Route(int id, String name, double coordinatesX, Double coordinatesY, double fromX, Long fromY, String nameFrom, int toX, float toY, String nameTo, Integer distance, User user) {
        this.id = id;
        this.name = name;
        this.coordinates = new Coordinates(coordinatesX, coordinatesY);
        this.from = new Location(fromX, fromY, nameFrom);
        this.to = new utils.loc.Location(toX, toY, nameTo);
        this.distance = distance;
        this.creationDate = ZonedDateTime.now();
        this.user = user;
    }

    public Route(int id, String name, double coordinatesX, Double coordinatesY, double fromX,
                 Long fromY, String nameFrom, int toX,
                 float toY, String nameTo,
                 Integer distance, User user, Date date) {
        this.id = id;
        this.name = name;
        this.coordinates = new Coordinates(coordinatesX, coordinatesY);
        this.from = new Location(fromX, fromY, nameFrom);
        this.to = new utils.loc.Location(toX, toY, nameTo);
        this.distance = distance;
        this.creationDate = ZonedDateTime.parse((CharSequence) date);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Route(RouteInfo information) {
        id = information.id ;
        name = information.name;
        coordinates = new Coordinates(information.x, information.y);
        LocalDateTime date = LocalDateTime.parse(information.creationDate, DateTimeFormatter.ofPattern("dd.MM.yyyy : HH.mm.ss"));
        creationDate = date.atZone(ZoneId.systemDefault());
        from = new Location(information.fromX, information.fromY, information.nameFrom);
        to = new utils.loc.Location(information.toX, information.toY, information.nameTo);
        distance = information.distance;
    }

    public int getId() { return id; }

    public int getDistance(){return distance;}


    @Override
    public String toString() {
        return "AnimationRoute" + System.lineSeparator() + "{" + System.lineSeparator() +
                "id: " + id + System.lineSeparator() +
                "name: '" + name + '\'' + System.lineSeparator() +
                "coordinates: " + coordinates + System.lineSeparator() +
                "creationDate: " + creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy : HH.mm.ss")) + System.lineSeparator() +
                "from: " + from + System.lineSeparator() +
                "to: " + to + System.lineSeparator() +
                "distance: " + distance + System.lineSeparator() +
                "username: " + user.getUsername() + System.lineSeparator() +
                '}';
    }

    public void update(RouteInfo routeInfo){
        name = routeInfo.name;
        coordinates = new Coordinates(routeInfo.x, routeInfo.y);
        from = new Location(routeInfo.fromX, routeInfo.fromY, routeInfo.nameFrom);
        to = new utils.loc.Location(routeInfo.toX, routeInfo.toY, routeInfo.nameTo);
        distance = routeInfo.distance;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public String getCreationDate() {
        return creationDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy : HH.mm.ss"));
    }

    public String getDateForSQL() {
        return creationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public Location getFrom() {
        return from;
    }

    public utils.loc.Location getTo() {
        return to;
    }

    public Route() {
    }
}

