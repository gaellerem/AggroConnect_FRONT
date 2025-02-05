package com.aggroconnect.appli.model;

public class Site {
    private int id;
    private String city;

    public Site(int id, String city) {
        this.id = id;
        this.city = city;
    }

    @Override
    public String toString() {
        return "Site{id=" + id + ", city='" + city + "'}";
    }
}