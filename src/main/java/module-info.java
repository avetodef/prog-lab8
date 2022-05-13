module com.example.laba8 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires org.bouncycastle.provider;
    requires javafx.media;
    requires java.desktop;
    requires java.sql;


    opens com.example.laba8 to javafx.fxml;
    exports com.example.laba8;
}