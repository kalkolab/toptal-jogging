package com.toptal.jogging.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.security.auth.Subject;
import java.security.Principal;

/**
 * Created by Artem on 26.06.2017.
 */
public class User implements Principal {
    @JsonProperty
    private final String name;

    @JsonProperty
    private final String role;

    public User(String name) {
        this.name = name;
        this.role = null;
    }

    public User(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public boolean implies(Subject subject) {
        return false;
    }
}
