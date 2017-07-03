package com.toptal.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

/**
 * Created by Artem on 29.06.2017.
 *
 * Wrapper class to contain response from getters with HTTP error code
 */
public class Representation<T> {
    private long code;

    @Length(max = 3)
    private T data;

    public Representation() {}

    public Representation(long code, T data) {
        this.code = code;
        this.data = data;
    }

    @JsonProperty
    public long getCode() {
        return code;
    }

    @JsonProperty
    public T getData() {
        return data;
    }
}
