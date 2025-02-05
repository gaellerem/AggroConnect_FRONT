module com.aggroconnect.appli {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires okhttp3;
    requires org.json;

    opens com.aggroconnect.appli to javafx.fxml;
    exports com.aggroconnect.appli;
}