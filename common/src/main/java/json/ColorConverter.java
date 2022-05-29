package json;

import javafx.scene.paint.Color;

public class ColorConverter {
    public static Color hashCodeToRGB(String hash) {

        if (hash.length() >= 10) {
            return tenChars(hash);
        }
        if (hash.length() == 9) {
            return nineChars(hash);
        }

        return null;
    }

    public static Color color(String hash) {
        int hscde = Integer.parseInt(hash);
        if (hscde < 0)
            hash = hash.substring(1, hash.length() - 1);

        if (hash.length() >= 10)
            return tenChars(hash);
        else switch (hash.length()) {
            case (9):
                return nineChars(hash);
            case (8):
                return eightChars(hash);
            case (7):
                return sevenChars(hash);
            case (6):
                return sixChars(hash);
            case (5):
                return fiveChars(hash);
            case (4):
                return fourChars(hash);
            case (3):
                return threeChars(hash);
            case (2):
                return twoChars(hash);
            case (1):
                return ineChar(hash);
            default:
                return null;
        }

//        int hashCode = Integer.parseInt(hash);
//
//        return Color.hsb(hashCode/((double)Integer.MAX_VALUE), 0.5, 0.5);
    }


    private static Color tenChars(String hashcode) {
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

        return new Color(r, g, b, 1);
    }

    private static Color nineChars(String hashcode) {
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

        return new Color(r, g, b, 1);
    }

    private static Color eightChars(String hashcode) {
        String red = hashcode.substring(0, 3);
        double r = Double.parseDouble(red) / 100;
        if (r > 1)
            r = r / 10;
        String green = hashcode.substring(3, 6);
        double g = Double.parseDouble(green) / 100;
        if (g > 1)
            g = g / 10;

        String blue = hashcode.substring(6, 8);
        double b = Double.parseDouble(blue) / 100;
        if (b > 1)
            b = b / 10;

        return new Color(r, g, b, 1);
    }

    private static Color sevenChars(String hashcode) {
        String red = hashcode.substring(0, 3);
        double r = Double.parseDouble(red) / 100;
        if (r > 1)
            r = r / 10;
        String green = hashcode.substring(3, 5);
        double g = Double.parseDouble(green) / 100;
        if (g > 1)
            g = g / 10;

        String blue = hashcode.substring(5, 7);
        double b = Double.parseDouble(blue) / 100;
        if (b > 1)
            b = b / 10;

        return new Color(r, g, b, 1);
    }

    private static Color sixChars(String hashcode) {
        String red = hashcode.substring(0, 2);
        double r = Double.parseDouble(red) / 10;
        if (r > 1)
            r = r / 10;
        String green = hashcode.substring(2, 4);
        double g = Double.parseDouble(green) / 10;
        if (g > 1)
            g = g / 10;

        String blue = hashcode.substring(4, 6);
        double b = Double.parseDouble(blue) / 10;
        if (b > 1)
            b = b / 10;

        return new Color(r, g, b, 1);
    }

    private static Color fiveChars(String hashcode) {
        String red = hashcode.substring(0, 2);
        double r = Double.parseDouble(red) / 10;
        if (r > 1)
            r = r / 10;
        String green = hashcode.substring(2, 4);
        double g = Double.parseDouble(green) / 10;
        if (g > 1)
            g = g / 10;

        String blue = hashcode.substring(4, 5);
        double b = Double.parseDouble(blue) / 10;
        if (b > 1)
            b = b / 10;

        return new Color(r, g, b, 1);
    }

    private static Color fourChars(String hashcode) {
        String red = hashcode.substring(0, 2);
        double r = Double.parseDouble(red) / 10;
        if (r > 1)
            r = r / 10;
        String green = hashcode.substring(2, 3);
        double g = Double.parseDouble(green) / 10;
        if (g > 1)
            g = g / 10;

        String blue = hashcode.substring(3, 4);
        double b = Double.parseDouble(blue) / 10;
        if (b > 1)
            b = b / 10;

        return new Color(r, g, b, 1);
    }

    private static Color threeChars(String hashcode) {
        String red = hashcode.substring(0, 1);
        double r = Double.parseDouble(red) / 10;
        if (r > 1)
            r = r / 10;
        String green = hashcode.substring(1, 2);
        double g = Double.parseDouble(green) / 10;
        if (g > 1)
            g = g / 10;

        String blue = hashcode.substring(2, 3);
        double b = Double.parseDouble(blue) / 10;
        if (b > 1)
            b = b / 10;

        return new Color(r, g, b, 1);
    }

    private static Color twoChars(String hashcode) {
        String red = hashcode.substring(0, 1);
        double r = Double.parseDouble(red) / 10;
        if (r > 1)
            r = r / 10;
        String green = hashcode.substring(1, 2);
        double g = Double.parseDouble(green) / 10;
        if (g > 1)
            g = g / 10;

        String blue = String.valueOf((Integer.parseInt(red) + Integer.parseInt(green)) / 2);
        double b = Double.parseDouble(blue) / 10;
        if (b > 1)
            b = b / 10;

        return new Color(r, g, b, 1);
    }

    private static Color ineChar(String hash) {
        return new Color(Double.parseDouble(hash) / 10, Double.parseDouble(hash) / 10, Double.parseDouble(hash) / 10, 1);
    }


}
