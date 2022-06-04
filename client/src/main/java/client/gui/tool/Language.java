package client.gui.tool;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class Language {
    public final Type type;
    private String name;
    private static Map<String, Locale> localeMap;
    private static final Language ru;
    private static final Language en;
    private static final Language es;
    private static final Language uk;
    private static final Language sl;

    static {
        ru = new Language(Type.RU);
        sl = new Language(Type.SL);
        es = new Language(Type.ES);
        uk = new Language(Type.UK);
        en = new Language(Type.EN);
        update();
    }

    @FXML
    private static ChoiceBox<String> languageChoice;

    public Language(Type type) {
        this.type = type;
    }

    private ObservableResourse observableResourse;
    public static final String BUNDLE = "ResourseBundle.gui";

    public static void update() {
        ObservableResourse obs = new ObservableResourse();
        //ResourceBundle res = ResourceBundle.getBundle(BUNDLE,localeMap.get(languageChoice.getSelectionModel().getSelectedItem())));
        ResourceBundle res = StringResourse.getBundle();
        ru.name = res.getString("c_ru");
        sl.name = res.getString("c_sl");
        es.name = res.getString("c_es");
        uk.name = res.getString("c_uk");
        en.name = res.getString("c_en");
    }

    public static Set<Language> getLanguages() {
        return FXCollections.observableSet(ru, sl, es, uk, en);
    }

    @Override
    public String toString() {
        return name;
    }

    public static Language get(Type type) {
        switch (type) {
            case ES:
                return es;
            case UK:
                return uk;
            case RU:
                return ru;
            case SL:
                return sl;
            default:
                return en;
        }
    }

    public enum Type {
        RU, ES, EN, UK, SL
    }
}

