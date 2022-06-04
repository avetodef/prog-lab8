package client.gui.tool;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

public class ObservableResourse {

    private final ObjectProperty<ResourceBundle> resources = new SimpleObjectProperty<>();


    public ObjectProperty<ResourceBundle> resourcesProperty() {
        return resources;
    }


    public final ResourceBundle getResources() {
        return resourcesProperty().get();
    }


    public final void setResources(ResourceBundle resources) {
        resourcesProperty().set(resources);
    }


    public StringBinding getStringBinding(String key) {
        return new StringBinding() {
            {
                bind(resourcesProperty());
            }

            @Override
            public String computeValue() {
                return getResources().getString(key);
            }
        };
    }

    public String getString(String key) {
        String res;
        if (key.contains(" ") && key.contains("[") && key.contains("]")) {
            String[] seq = key.split(" ");
            List<String> list = new LinkedList<>();
            for (String e : seq) {
                if (e.length() >= 3 && e.charAt(0) == '[' && e.charAt(e.length() - 1) == ']') {
                    list.add(e.substring(1, e.length() - 1));
                }
            }
            if (list.size() == 0) throw new ResourseException(key);
            key = list.get(0);
            list.remove(0);

            Object[] args = list.toArray();
            res = MessageFormat.format(getRawString(key), args);
        } else if (key.length() >= 3 && key.charAt(0) == '[' && key.charAt(key.length() - 1) == ']') {
            res = getRawString(key.substring(1, key.length() - 1));
        } else {
            throw new ResourseException(key);
        }

        return res;
    }

    public String getRawString(String key) {
        if (!getResources().containsKey(key)) throw new ResourseException(key);
        return getResources().getString(key);
    }
}
