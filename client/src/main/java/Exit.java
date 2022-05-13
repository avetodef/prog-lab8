import exceptions.ExitException;

import java.util.NoSuchElementException;

/**
 * Класс команды EXIT, предназначенный для выхода из выполнения программы
 */
public class Exit  {

    public static String execute() {

        //System.out.println("пока.");
        try {
            System.out.println("             *     ,MMM8&&&.            *\n" +
                    "                  MMMM88&&&&&    .\n" +
                    "                 MMMM88&&&&&&&\n" +
                    "     *           MMM88&&&&&&&&\n" +
                    "                 MMM88&&&&&&&&\n" +
                    "                 'MMM88&&&&&&'\n" +
                    "                   'MMM8&&&'      *    \n" +
                    "           /\\/|_      __/\\\\\n" +
                    "          /    -\\    /-   ~\\  .              '        звезды сегодня красивые...\n" +
                    "          \\    = Y =T_ =   /\n" +
                    "           )==*(`     `) ~ \\                            и лаба красивая...\n" +
                    "          /     \\     /     \\\n" +
                    "          |     |     ) ~   (                                 пока-пока\n" +
                    "         /       \\   /     ~ \\\n" +
                    "         \\       /   \\~     ~/\n" +
                    "  jgs_/\\_/\\__  _/_/\\_/\\__~__/_/\\_/\\_/\\_/\\_/\\_\n" +
                    "  |  |  |  | ) ) |  |  | ((  |  |  |  |  |  |\n" +
                    "  |  |  |  |( (  |  |  |  \\\\ |  |  |  |  |  |\n" +
                    "  |  |  |  | )_) |  |  |  |))|  |  |  |  |  |\n" +
                    "  |  |  |  |  |  |  |  |  (/ |  |  |  |  |  |\n" +
                    "  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |");
            System.exit(0);
            return (" ");

        } catch (NoSuchElementException e) {
            throw new ExitException("пока..............");
        }
    }
}