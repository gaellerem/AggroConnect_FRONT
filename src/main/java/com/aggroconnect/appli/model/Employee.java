package com.aggroconnect.appli.model;

public class Employee {
    private int id;
    private String name;
    private String email;
    private String landline;
    private String cellphone;
    private Department department;
    private Site site;

    public Employee(int id, String name, String email, String landline, String cellphone, Department department, Site site) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.landline = landline;
        this.cellphone = cellphone;
        this.department = department;
        this.site = site;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", landline='" + landline + '\'' +
                ", cellphone='" + cellphone + '\'' +
                ", department=" + department +
                ", site=" + site +
                '}';
    }
}