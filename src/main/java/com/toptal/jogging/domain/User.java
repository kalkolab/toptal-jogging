package com.toptal.jogging.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.security.auth.Subject;
import java.security.Principal;

/**
 * Created by Artem on 26.06.2017.
 */
public class User implements Principal {

    public enum Role {
        USER, MANAGER, ADMIN
    }

    @JsonProperty
    private final int id;

    @JsonProperty
    private final String name;

    @JsonIgnore
    private final String password;

    @JsonProperty
    private final Role role;

    public User(@JsonProperty("name") String name, @JsonProperty("password")  String password) {
        this.id = 0;
        this.name = name;
        this.password = password;
        this.role = Role.USER;
    }

    public User(int id, String name, String password, Role role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public boolean implies(Subject subject) {
        return false;
    }
}
