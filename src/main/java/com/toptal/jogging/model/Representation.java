package com.toptal.jogging.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.ws.rs.core.Response;

/**
 * Created by Artem on 29.06.2017.
 *
 * Wrapper class to contain response from getters with HTTP error code
 */
public class Representation<T> {
    private Response.Status code;

    @Length(max = 3)
    private T data;

    public Representation() {}

    public Representation(Response.Status code, T data) {
        this.code = code;
        this.data = data;
    }

    @JsonProperty
    public Response.Status getCode() {
        return code;
    }

    @JsonProperty
    public T getData() {
        return data;
    }
}
