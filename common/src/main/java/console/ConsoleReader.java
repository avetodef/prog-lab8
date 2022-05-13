package console;


import exceptions.EmptyInputException;
import exceptions.ExitException;

import java.util.*;

public class ConsoleReader {

    public List<String> reader() {
        Scanner sc = new Scanner(System.in);
        try {
            String command = sc.nextLine().trim();


        if (command.isEmpty()) {
            throw new EmptyInputException();
        }
        List<String> args = new ArrayList<>(Arrays.asList(command.split(" ")));
        return args;
    }catch (NoSuchElementException e){
            throw new ExitException("пока пока");
        }
    }
}
