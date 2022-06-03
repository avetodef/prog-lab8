package utils.animation;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class AnimationRoute {
    @Getter
    private int id;
    @Getter
    private String author;
    @Getter
    private double fromX;
    @Getter
    private long fromY;
    @Getter
    private int toX;
    @Getter
    private float toY;
    @Getter
    private String color;

    public AnimationRoute(double fromX, long fromY, int toX, float toY, String color) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.color = color;
    }

    @Override
    public String toString() {
        return "AnimationRoute{" +
                "author='" + author + '\'' +
                ", fromX=" + fromX +
                ", fromY=" + fromY +
                ", toX=" + toX +
                ", toY=" + toY +
                ", color='" + color + '\'' +
                '}';
    }

}