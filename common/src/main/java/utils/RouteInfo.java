package utils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RouteInfo {
    public int id;
    public String name;
    public double x;
    public Double y;
    public String creationDate;
    public double fromX;
    public Long fromY;
    public String nameFrom;
    public int toX;
    public float toY;
    public String nameTo;
    public Integer distance;



    public RouteInfo(List<String> data) {
        id = Integer.parseInt(data.get(0));
        name = data.get(1);
        x = Double.parseDouble(data.get(2));
        y = Double.parseDouble(data.get(3));
        creationDate = ZonedDateTime.parse(data.get(4)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
        fromX = Double.parseDouble(data.get(5));
        fromY = Long.parseLong(data.get(6));
        nameFrom = data.get(7);
        toX = Integer.parseInt(data.get(8));
        toY = Float.parseFloat(data.get(9));
        nameTo = data.get(10);
        distance = Integer.parseInt(data.get(11));
    }

    public RouteInfo(int i, String name, double v, double parseDouble, ZonedDateTime parse, double aDouble, long l, String fromName, int parseInt, long parseLong, String toName, int anInt) {}

    public RouteInfo() {}

    @Override
    public String toString() {
        return "RouteInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", creationDate='" + creationDate + '\'' +
                ", fromX=" + fromX +
                ", fromY=" + fromY +
                ", nameFrom='" + nameFrom + '\'' +
                ", toX=" + toX +
                ", toY=" + toY +
                ", nameTo='" + nameTo + '\'' +
                ", distance=" + distance +
                '}';
    }
}
