package parsing;

import javafx.scene.paint.Color;

public class ColorConverter {

    public static Color color(String hash) {
        int hscde = Integer.parseInt(hash);
        if (hscde < 0)
            hash = hash.substring(1, hash.length() - 1);

        if (hash.length() >= 10)
            return tenChars(hash, 1);
        else switch (hash.length()) {
            case (9):
                return nineChars(hash, 1);
            case (8):
                return eightChars(hash, 1);
            case (7):
                return sevenChars(hash, 1);
            case (6):
                return sixChars(hash, 1);
            case (5):
                return fiveChars(hash, 1);
            case (4):
                return fourChars(hash, 1);
            case (3):
                return threeChars(hash, 1);
            case (2):
                return twoChars(hash, 1);
            case (1):
                return ineChar(hash, 1);
            default:
                return null;
        }
    }


    private static Color tenChars(String hashcode, int opacity) {
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

        return new Color(r, g, b, opacity);
    }

    private static Color nineChars(String hashcode, int opacity) {
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

        return new Color(r, g, b, opacity);
    }

    private static Color eightChars(String hashcode, int o) {
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

        return new Color(r, g, b, o);
    }

    private static Color sevenChars(String hashcode, int o) {
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

        return new Color(r, g, b, o);
    }

    private static Color sixChars(String hashcode, int o) {
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

        return new Color(r, g, b, o);
    }

    private static Color fiveChars(String hashcode, int o) {
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

        return new Color(r, g, b, o);
    }

    private static Color fourChars(String hashcode, int o) {
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

        return new Color(r, g, b, o);
    }

    private static Color threeChars(String hashcode, int o) {
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

        return new Color(r, g, b, o);
    }

    private static Color twoChars(String hashcode, int o) {
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

        return new Color(r, g, b, o);
    }

    private static Color ineChar(String hash, int o) {
        return new Color(Double.parseDouble(hash) / 10, Double.parseDouble(hash) / 10, Double.parseDouble(hash) / 10, o);
    }


    public static Color transparentColor(String hash) {
        int hscde = Integer.parseInt(hash);
        if (hscde < 0)
            hash = hash.substring(1, hash.length() - 1);

        if (hash.length() >= 10)
            return tenChars(hash, 0);
        else switch (hash.length()) {
            case (9):
                return nineChars(hash, 0);
            case (8):
                return eightChars(hash, 0);
            case (7):
                return sevenChars(hash, 0);
            case (6):
                return sixChars(hash, 0);
            case (5):
                return fiveChars(hash, 0);
            case (4):
                return fourChars(hash, 0);
            case (3):
                return threeChars(hash, 0);
            case (2):
                return twoChars(hash, 0);
            case (1):
                return ineChar(hash, 0);
            default:
                return null;
        }
    }


}
