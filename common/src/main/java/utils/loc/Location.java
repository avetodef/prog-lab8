package utils.loc;

import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Location {
    private int x;
    private float y;
    private String name; //Поле может быть null

    public Location(int toX, float toY, String name) {
        this.x = toX;
        this.y = toY;
        this.name = name; //зачем написала не знаю
    }

    public int getToX() {
        return x;
    }

    public float getToY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return
                "x=" + x +
                        ", y=" + y +
                        ", name='" + name + '\'';
    }
}