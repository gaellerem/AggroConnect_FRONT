package com.aggroconnect.appli.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Getter;

@Getter
public class Employee {
    private final int id;
    private final StringProperty name;
    private final StringProperty email;
    private final StringProperty landline;
    private final StringProperty cellphone;
    private final Department department;
    private final Site site;

    public Employee(int id, String name, String email, String landline, String cellphone, Department department, Site site) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.landline = new SimpleStringProperty(landline);
        this.cellphone = new SimpleStringProperty(cellphone);
        this.department = department;
        this.site = site;
    }

    public StringProperty nameProperty() { return name; }
    public StringProperty emailProperty() { return email; }
    public StringProperty landlineProperty() { return landline; }
    public StringProperty cellphoneProperty() { return cellphone; }
}
