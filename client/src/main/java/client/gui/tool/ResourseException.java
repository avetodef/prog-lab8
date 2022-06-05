package client.gui.tool;

public class ResourseException extends RuntimeException {
    public ResourseException(String key) {
        super("Ресурс не найден: " + key);
    }
}
