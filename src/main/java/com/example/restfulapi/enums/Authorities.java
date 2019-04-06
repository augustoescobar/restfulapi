package com.example.restfulapi.enums;

public enum Authorities {

    ADMIN("ADMIN");

    private final String name;

    private Authorities(String name) {

        this.name = name;
    }
}
