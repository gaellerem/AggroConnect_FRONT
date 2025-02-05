module com.aggroconnect.appli {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires okhttp3;
    requires org.json;
    requires static lombok;

    opens com.aggroconnect.appli.controller to javafx.fxml;
    opens com.aggroconnect.appli.model to javafx.fxml;
    opens com.aggroconnect.appli.service to javafx.fxml;
    exports com.aggroconnect.appli;
}