package com.toptal.dropwizard;

import com.google.common.collect.ImmutableSet;
import com.toptal.model.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Optional;

/**
 * Created by Artem on 26.06.2017.
 */
public class JoggingAuthenticator implements Authenticator<BasicCredentials, User> {
    public Optional<User> authenticate(BasicCredentials basicCredentials) throws AuthenticationException {
        if (basicCredentials.getUsername().equals("artem") && basicCredentials.getPassword().equals("pass")) {
            return Optional.of(new User("artem", "ADMIN"));
        }
        return Optional.empty();
    }
}
