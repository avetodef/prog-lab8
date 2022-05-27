package json;

import javafx.scene.paint.Color;

public class ColorConverter {
    public static Color hashCodeToRGB(int hash) {
        String hashcode = String.valueOf(hash);

        if (hashcode.length() == 10) {
            String red = hashcode.substring(0, 3);
            double r = Double.parseDouble(red) / 100;
            if (r > 1)
                r = r / 10;
            String green = hashcode.substring(3, 6);
            double g = Double.parseDouble(green) / 100;
            if (g > 1)
                g = g / 10;

            String blue = hashcode.substring(6, 9);
            double b = Double.parseDouble(blue) / 100;
            if (b > 1)
                b = b / 10;

            double opacity = Double.parseDouble(hashcode.substring(8, 9)) / 10;

            return new Color(r, g, b, opacity);
        }
        if (hashcode.length() == 9) {
            String red = hashcode.substring(0, 3);
            double r = Double.parseDouble(red) / 100;
            if (r > 1)
                r = r / 10;
            String green = hashcode.substring(3, 6);
            double g = Double.parseDouble(green) / 100;
            if (g > 1)
                g = g / 10;

            String blue = hashcode.substring(6, 9);
            double b = Double.parseDouble(blue) / 100;
            if (b > 1)
                b = b / 10;

            double opacity = Double.parseDouble(hashcode.substring(8, 9)) / 10;

            return new Color(r, g, b, opacity);
        }

        double r = 0.60;
        double g = 0.45;
        double b = 0.2;
        double opacity = 0.9;
        return new Color(r, g, b, opacity);
    }


}
