package dao;

import console.ConsoleOutputer;
import interaction.Response;
import interaction.Status;
import interaction.User;
import lombok.NoArgsConstructor;
import server.ResponseSender;
import utils.Route;
import utils.RouteInfo;
import utils.animation.AnimationRoute;

import java.io.DataOutputStream;
import java.sql.*;
import java.time.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@NoArgsConstructor
public class DataBaseDAO implements DAO {

    static final String url = "jdbc:postgresql://localhost:5432/postgres";
    static final String username = "postgres";
    static final String password = "lterm54201";
    private final static ConsoleOutputer o = new ConsoleOutputer();
    private Connection connection = connect();
    private static DataOutputStream outputStream;

    public DataBaseDAO(DataOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public static Connection connect() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            o.printRed("database sleep. wait");
            ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
            fixedThreadPool.execute(new ResponseSender(outputStream, new Response("database sleep", Status.SERVER_ERROR)));
        }
        if (conn == null)
            o.printRed("not connected to database");

        return conn;
    }

    @Override
    public int create(Route route) {
        //Connection connection = connect();
        String statement1 = "INSERT INTO route(creation_date, distance, username, name )" +
                "VALUES(?,?,?,?) returning id";

        String statement2 = "INSERT INTO coordinates(coord_x,coord_y)" +
                "VALUES (?,?) returning id";

        String statement3 = "INSERT INTO location_from(from_x,from_y,from_name)" +
                "VALUES (?,?,?) returning id";

        String statement4 = "INSERT INTO location_to(to_x,to_y,to_name)" +
                "VALUES(?,?,?) returning id";

        try {

            PreparedStatement pstmt1 = connection.prepareStatement(statement1);

            pstmt1.setTimestamp(1, Timestamp.valueOf(route.getDateForSQL()));
            pstmt1.setInt(2, route.getDistance());
            pstmt1.setString(3, route.getUser().getUsername());
            pstmt1.setString(4, route.getName());

            ResultSet rs1 = pstmt1.executeQuery();
            rs1.next();

            PreparedStatement pstmt2 = connection.prepareStatement(statement2);
            pstmt2.setDouble(1, route.getCoordinates().getCoorX());
            pstmt2.setDouble(2, route.getCoordinates().getCoorY());

            ResultSet rs2 = pstmt2.executeQuery();
            rs2.next();

            PreparedStatement pstmt3 = connection.prepareStatement(statement3);
            pstmt3.setDouble(1, route.getFrom().getFromX());
            pstmt3.setLong(2, route.getFrom().getFromY());
            pstmt3.setString(3, route.getFrom().getName());

            ResultSet rs3 = pstmt3.executeQuery();
            rs3.next();

            PreparedStatement pstmt4 = connection.prepareStatement(statement4);
            pstmt4.setInt(1, route.getTo().getToX());
            pstmt4.setFloat(2, route.getTo().getToY());
            pstmt4.setString(3, route.getTo().getName());

            ResultSet rs4 = pstmt4.executeQuery();
            rs4.next();

            return rs1.getInt("id");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return 0;

    }


    public String getUsernameByRouteId(int routeID) {
        String SQL = "SELECT * FROM route WHERE id=?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setInt(1, routeID);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getString("username");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    @Override
    public boolean update(int id, RouteInfo routeInfo) {
        //Connection connection = connect();


        String statement1 = "UPDATE route SET  name = ?, creation_date = ?, distance = ?"
                + "WHERE id = ?";
        String statement2 = "UPDATE coordinates SET coord_x = ?, coord_y = ?" + "WHERE id = ?";

        String statement3 = "UPDATE location_from SET from_x = ?,from_y = ?, from_name = ?"
                + "WHERE id = ?";
        String statement4 = "UPDATE location_to SET to_x = ?, to_y = ?, to_name = ?" +
                "WHERE id = ?";


        try {
            PreparedStatement pstmt1 = connection.prepareStatement(statement1);
            pstmt1.setTimestamp(2, Timestamp.valueOf(ZonedDateTime.now().toLocalDateTime()));
            pstmt1.setInt(3, routeInfo.distance);
            pstmt1.setString(1, routeInfo.name);
            pstmt1.setInt(4, id);


            PreparedStatement pstmt2 = connection.prepareStatement(statement2);
            pstmt2.setDouble(1, routeInfo.x);
            pstmt2.setDouble(2, routeInfo.y);
            pstmt2.setInt(3, id);


            PreparedStatement pstmt3 = connection.prepareStatement(statement3);
            pstmt3.setDouble(1, routeInfo.fromX);
            pstmt3.setLong(2, routeInfo.fromY);
            pstmt3.setString(3, routeInfo.nameFrom);
            pstmt3.setInt(4, id);


            PreparedStatement pstmt4 = connection.prepareStatement(statement4);
            pstmt4.setInt(1, routeInfo.toX);
            pstmt4.setFloat(2, routeInfo.toY);
            pstmt4.setString(3, routeInfo.nameTo);
            pstmt4.setInt(4, id);

            pstmt1.executeUpdate();
            pstmt2.executeUpdate();
            pstmt3.executeUpdate();
            pstmt4.executeUpdate();
            return true;
        } catch (SQLException w) {
            w.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        //Connection connection = connect();
        String SQL1 = "DELETE FROM route WHERE id = ? ";
        String SQL2 = "DELETE FROM coordinates WHERE id = ?";
        String SQL3 = "DELETE FROM location_from WHERE id =?";
        String SQL4 = "DELETE FROM location_to WHERE id = ?";


        try {
            PreparedStatement pstmt1 = connection.prepareStatement(SQL1);
            pstmt1.setInt(1, id);

            PreparedStatement pstmt2 = connection.prepareStatement(SQL2);
            pstmt2.setInt(1, id);

            PreparedStatement pstmt3 = connection.prepareStatement(SQL3);
            pstmt3.setInt(1, id);

            PreparedStatement pstmt4 = connection.prepareStatement(SQL4);
            pstmt4.setInt(1, id);

            pstmt1.executeUpdate();
            pstmt2.executeUpdate();
            pstmt3.executeUpdate();
            pstmt4.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Deque<Route> getAll() {
        //Connection connection = connect();
        String SQL1 = "SELECT * FROM route";
        String SQL2 = "SELECT * FROM coordinates ";
        String SQL3 = "SELECT * FROM location_from ";
        String SQL4 = "SELECT * FROM location_to ";

        Deque<Route> collection = new ArrayDeque<>();
        try {
            PreparedStatement pstmt1 = connection.prepareStatement(SQL1);
            ResultSet rs1 = pstmt1.executeQuery();

            PreparedStatement pstmt2 = connection.prepareStatement(SQL2);
            ResultSet rs2 = pstmt2.executeQuery();

            PreparedStatement pstmt3 = connection.prepareStatement(SQL3);
            ResultSet rs3 = pstmt3.executeQuery();

            PreparedStatement pstmt4 = connection.prepareStatement(SQL4);
            ResultSet rs4 = pstmt4.executeQuery();

            while (rs1.next() && rs3.next() && rs4.next() && rs2.next()) {
                var route = new Route(rs1.getInt("id"), rs1.getString("name"), rs2.getDouble("coord_x"),
                        rs2.getDouble("coord_y"), rs3.getDouble("from_x"),
                        rs3.getLong("from_y"), rs3.getString("from_name"), rs4.getInt("to_x"),
                        rs4.getFloat("to_y"), rs4.getString("to_name"), rs1.getInt("distance"),
                        getUserByName(rs1.getString("username")));


                LocalDateTime localDateTime = rs1.getObject("creation_date", LocalDateTime.class);

                //Instant instant = rs1.getObject("creation_date", Instant.class);
                ZoneId zoneId = ZoneId.systemDefault();
                Instant instant = localDateTime.atOffset(ZoneOffset.UTC).toInstant();
                ZonedDateTime time = instant.atZone(zoneId);
                route.setCreationDate(time);
                collection.add(route);
            }

            return collection;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new ArrayDeque<Route>();
        } catch (NullPointerException e) {
            System.out.println("пусто в душе и в коллеекции ");
            return new ArrayDeque<Route>();
        }
    }

    public ArrayList<AnimationRoute> getAnimationRoute() {
        //Connection connection = connect();
        String SQL1 = "SELECT * FROM route";
        String SQL3 = "SELECT * FROM location_from ";
        String SQL4 = "SELECT * FROM location_to ";

        ArrayList<AnimationRoute> collection = new ArrayList<>();
        try {
            PreparedStatement pstmt1 = connection.prepareStatement(SQL1);
            ResultSet rs1 = pstmt1.executeQuery();
//            rs1.next();

            PreparedStatement pstmt3 = connection.prepareStatement(SQL3);
            ResultSet rs3 = pstmt3.executeQuery();
//            rs3.next();

            PreparedStatement pstmt4 = connection.prepareStatement(SQL4);
            ResultSet rs4 = pstmt4.executeQuery();
//            rs4.next();


            while (rs1.next() && rs3.next() && rs4.next()) {
                collection.add(new AnimationRoute
                        (rs1.getInt("id"), rs1.getString("username"), rs3.getDouble("from_x"),
                                rs3.getLong("from_y"), rs4.getInt("to_x"),
                                rs4.getFloat("to_y"),
                                String.valueOf(rs1.getString("username").hashCode())));
            }

            //System.out.println("COLLECTION " + collection);

            return collection;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new ArrayList<AnimationRoute>();
        } catch (NullPointerException e) {
            System.out.println("пусто в душе и в коллеекции ");
            return new ArrayList<AnimationRoute>();
        }
    }

    public ArrayList<Route> getArrayListOfRoutes() {
        //Connection connection = connect();
        String SQL1 = "SELECT * FROM route";
        String SQL2 = "SELECT * FROM coordinates ";
        String SQL3 = "SELECT * FROM location_from ";
        String SQL4 = "SELECT * FROM location_to ";

        ArrayList<Route> collection = new ArrayList<>();
        try {
            PreparedStatement pstmt1 = connection.prepareStatement(SQL1);
            ResultSet rs1 = pstmt1.executeQuery();
//            rs1.next();

            PreparedStatement pstmt2 = connection.prepareStatement(SQL2);
            ResultSet rs2 = pstmt2.executeQuery();
//            rs2.next();

            PreparedStatement pstmt3 = connection.prepareStatement(SQL3);
            ResultSet rs3 = pstmt3.executeQuery();
//            rs3.next();

            PreparedStatement pstmt4 = connection.prepareStatement(SQL4);
            ResultSet rs4 = pstmt4.executeQuery();
//            rs4.next();

            while (rs1.next() && rs2.next() && rs3.next() && rs4.next()) {

                var route = new Route(rs1.getInt("id"), rs1.getString("name"), rs2.getDouble("coord_x"),
                        rs2.getDouble("coord_y"), rs3.getDouble("from_x"),
                        rs3.getLong("from_y"), rs3.getString("from_name"), rs4.getInt("to_x"),
                        rs4.getFloat("to_y"), rs4.getString("to_name"), rs1.getInt("distance"),
                        getUserByName(rs1.getString("username")));

                LocalDateTime localDateTime = rs1.getObject("creation_date", LocalDateTime.class);
                //Instant instant = rs1.getObject("creation_date", Instant.class);
                ZoneId zoneId = ZoneId.systemDefault();
                Instant instant = localDateTime.atOffset(ZoneOffset.UTC).toInstant();
                ZonedDateTime time = instant.atZone(zoneId);
                route.setCreationDate(time);
                collection.add(route);
            }
            return collection;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return new ArrayList<Route>();
        } catch (NullPointerException e) {
            System.out.println("пусто в душе и в коллеекции ");
            return new ArrayList<Route>();
        }
    }


    public void removeFirst(RouteDAO dao) {
        //Connection connection = connect();
        String sql1 = "DELETE FROM route WHERE id = ?";
        String sql2 = "DELETE FROM coordinates WHERE id = ?";
        String sql3 = "DELETE FROM location_from WHERE id = ?";
        String sql4 = "DELETE FROM location_to WHERE id = ?";
        try {
            PreparedStatement pstmt1 = connection.prepareStatement(sql1);
            int id = dao.getAll().getFirst().getId();
            pstmt1.setInt(1, id);
            pstmt1.execute();

            PreparedStatement pstmt2 = connection.prepareStatement(sql2);
            pstmt2.setInt(1, id);
            pstmt2.execute();

            PreparedStatement pstmt3 = connection.prepareStatement(sql3);
            pstmt3.setInt(1, id);
            pstmt3.execute();

            PreparedStatement pstmt4 = connection.prepareStatement(sql4);
            pstmt4.setInt(1, id);
            pstmt4.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public RouteDAO getDAO() {
        try {
            Deque<Route> routes = getAll();
            RouteDAO dao = new RouteDAO();
            if (routes.isEmpty()) return new RouteDAO();
            else {
                for (Route route : routes) {
                    dao.create(route);
                }
                return dao;
            }
        } catch (RuntimeException e) {
        }
        return new RouteDAO();
    }

    public void insertUser(User user) {
        String sql = "INSERT INTO users(username, password) VALUES (?,?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkUsername(String username) {
        String sql = "SELECT username FROM users WHERE username=?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            int count = 0;
            while (rs.next()) {
                count++;
            }
            if (count > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean checkPassword(String password) {
        String sql = "SELECT password FROM users WHERE password=?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, password);
            ResultSet rs = pstmt.executeQuery();

            int count = 0;
            while (rs.next()) {
                count++;
            }
            if (count > 0) {
                return true;
            }
            if (count == 0)
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public User getUserByName(String username) {
        String SQL = "SELECT * FROM users WHERE username=?";
        try {
            PreparedStatement pstmt = connection.prepareStatement(SQL);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int user_id = rs.getInt("user_id");
            String password = rs.getString("password");
            return new User(username, password, user_id);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public int getUserID(String username) {
        String sql = "SELECT user_id FROM users WHERE username=?";
        int id = 0;
        try {
            PreparedStatement prstmt = connection.prepareStatement(sql);
            prstmt.setString(1, username);

            ResultSet rs = prstmt.executeQuery();
            rs.next();
            id = rs.getInt("user_id");
            return id;

        } catch (SQLException e) {

        }
        return ++id;
    }


}
