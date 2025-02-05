package com.aggroconnect.appli.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

@Getter
public class Department {
    private final int id;
    private final StringProperty name;

    public Department(int id, String name) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
    }

    public StringProperty nameProperty() {return name;}

}
