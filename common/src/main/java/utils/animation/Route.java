package utils.animation;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Route {
    @Getter
    public String author;
    @Getter
    public double fromX;
    @Getter
    public long fromY;
    @Getter
    public int toX;
    @Getter
    public float toY;
    @Getter
    public String color;

//        public Route(String author, double fromX, long fromY, int toX, float toY) {
//            this.author = author;
//            this.fromX = fromX;
//            this.fromY = fromY;
//            this.toX = toX;
//            this.toY = toY;
//        }

    public Route(double fromX, long fromY, int toX, float toY, String color) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.color = color;
    }

    public Route() {
    }

    @Override
    public String toString() {
        return "Route{" +
                "author='" + author + '\'' +
                ", fromX=" + fromX +
                ", fromY=" + fromY +
                ", toX=" + toX +
                ", toY=" + toY +
                ", color='" + color + '\'' +
                '}';
    }
}