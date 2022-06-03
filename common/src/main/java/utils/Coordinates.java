package utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Coordinates {
    private double coorX;
    private Double coorY; //Значение поля должно быть больше -210, Поле не может быть null

    public Coordinates(double x, Double y) {
        this.coorX = x;
        this.coorY = y;
    }

    public double getCoorX() {
        return coorX;
    }

    ;

    public Double getCoorY() {
        return coorY;
    }

    @Override
    public String toString() {
        return
                "x=" + coorX +
                        ", y=" + coorY;
    }
}