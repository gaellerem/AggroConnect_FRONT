package com.aggroconnect.appli.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

@Getter
public class Site {
    private final int id;
    private final StringProperty city;

    public Site(int id, String city) {
        this.id = id;
        this.city = new SimpleStringProperty(city);
    }

    public StringProperty cityProperty() {return city;}
}