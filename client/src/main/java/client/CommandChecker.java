//package client;
//
//import commands.CommandSaver;
//import commands.ExecuteReader;
//import console.ConsoleOutputer;
//import exceptions.EmptyInputException;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.NoSuchFileException;
//import java.nio.file.Paths;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class CommandChecker {
//    static ConsoleOutputer output = new ConsoleOutputer();
//
//    public static List<String> ifExecuteScript(List<String> inp) {
//
//        String nameOfScript = inp.get(1);
//
//        if (ExecuteReader.checkNameOfFileInList(nameOfScript)) {
//
//            ExecuteReader.listOfNamesOfScripts.add(nameOfScript);
//
//            try {
//                List<String> listOfCommands = Files.readAllLines(Paths.get(nameOfScript + ".txt").toAbsolutePath());
//
//                for (String lineOfFile : listOfCommands
//                ) {
//
//                    String command = lineOfFile.trim();
//
//                    if (command.isEmpty()) {
//                        throw new EmptyInputException();
//                    }
//                    List<String> args = new ArrayList<>(Arrays.asList(command.split(" ")));
//
//                    try {
//
//                        if (CommandSaver.checkCommand(args))
//                            return args;
//                        else {
//                            output.printPurple("в скрипте параша написана, переделывай");
//                        }
//                    } catch (RuntimeException e) {
//                        output.printPurple("в скрипте параша написана, переделывай"
//                        );
//                    }
//                }
//            } catch (NoSuchFileException e) {
//                output.printBlue("нет такого файла");
//
//
//            } catch (IOException e) {
//                output.printRed("что то произошло не так...");
//                e.printStackTrace();
//
//            }
//            ExecuteReader.listOfNamesOfScripts.clear();
//        } else {
//            output.printPurple("рекурсия... интересно кто бы мог решить сделать нам рекурсию....");
//        }
//        return null;
//    }
//
//}
