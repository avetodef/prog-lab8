package commands;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**f
 * Класс, предназначенный для добавления классов команд
 */
public class CommandSaver {
    public static final Map<String, ACommands> commandsMap = new LinkedHashMap<>();
    static {
//        commandsMap.put("help", new Help());
//        commandsMap.put("info", new Info());
//        commandsMap.put("show", new Show());
        commandsMap.put("add", new Add());
        commandsMap.put("update_by_id", new UpdateById());
        commandsMap.put("remove_by_id", new RemoveById());
        commandsMap.put("animation", new GetRoutesForAnimation());
        commandsMap.put("routeinfo", new RouteInfo());
        commandsMap.put("get_all", new GetAll());
//        commandsMap.put("clear", new Clear());
        //commandsMap.put("save", new Save());
//        commandsMap.put("execute_script", new ExecuteScript());
        //commandsMap.put("exit", new Exit()); //done
//        commandsMap.put("remove_first", new RemoveFirst());
//        commandsMap.put("head", new Head());
//        commandsMap.put("add_if_min", new AddIfMin());
//        commandsMap.put("print_unique_distance", new PrintUniqueDistance());
//        commandsMap.put("print_ascending_distance", new PrintAscendingDistance());
//        commandsMap.put("print_descending_distance", new PrintDescendingDistance());
//        commandsMap.put("rzhaka", new Rzhaka());
//        commandsMap.put("mega_rzhaka", new Secret());
        //commandsMap.put("tttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttttt", new Test());

    }
    /**
     * Добавление на консоль команд
     *
     * @return command
     */

    public static ACommands getCommand(List<String> input) {

        ACommands command = commandsMap.get(input.get(0));
        //input.remove(0);
        command.addArgs(input);

        return command;
    }

    public static boolean checkCommand(List<String> s){

        ACommands command = commandsMap.get(s.get(0));

        return command != null;
    }
}

