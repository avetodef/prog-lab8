package client;

import console.ConsoleOutputer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ASCIIArtService {
    private static final ConsoleOutputer o = new ConsoleOutputer();

    public static void printASCII(String msg, String colour, String character){
        try{

            int width = 150;
            int height = 30;

            //BufferedImage image = ImageIO.read(new File("/Users/mkyong/Desktop/logo.jpg"));
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            g.setFont(new Font("Lobster", Font.BOLD, 24));

            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            graphics.drawString(msg, 10, 20);

            //save this image
            //ImageIO.write(image, "png", new File("/users/mkyong/ascii-art.png"));

            for (int y = 0; y < height; y++) {
                StringBuilder sb = new StringBuilder();
                for (int x = 0; x < width; x++) {

                    sb.append(image.getRGB(x, y) == -16777216 ? " " : character);

                }

                if (sb.toString().trim().isEmpty()) {
                    continue;
                }
                switch (colour){
                    case("normal"):{
                        o.printNormal(sb.toString());
                        break;
                    }
                    case("red"):{
                        o.printRed(sb.toString());
                        break;
                    }
                    case("purple"):{
                        o.printPurple(sb.toString());
                        break;
                    }
                    case("cyan"):{
                        o.printCyan(sb.toString());
                        break;
                    }
                }
            }

        }
        catch (RuntimeException e){
            System.out.println("IO");
        }
    }
}
