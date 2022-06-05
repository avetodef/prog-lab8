package guice;

public class ConsoleMessager implements SimpleInterface {

    @Override
    public void print(String s) {
        System.out.println(s);

    }
}
