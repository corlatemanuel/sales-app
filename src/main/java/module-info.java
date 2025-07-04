module com.ubb.vanzari {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens com.ubb.vanzari to javafx.fxml;
    exports com.ubb.vanzari;

    opens GUI to javafx.fxml;
    exports GUI;

    opens Entity to javafx.base;
}