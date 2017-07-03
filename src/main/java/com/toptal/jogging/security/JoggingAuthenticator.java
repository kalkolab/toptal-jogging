package com.toptal.jogging.security;

import com.toptal.jogging.domain.User;
import com.toptal.jogging.domain.service.UsersService;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Optional;

/**
 * Created by Artem on 26.06.2017.
 */
public class JoggingAuthenticator implements Authenticator<BasicCredentials, User> {

    private final UsersService usersService;

    public JoggingAuthenticator(UsersService usersService) {
        this.usersService = usersService;
    }


    public Optional<User> authenticate(BasicCredentials basicCredentials) throws AuthenticationException {
        try {
            User user = usersService.getUser(basicCredentials.getUsername());
            if(user.getPassword().equals(basicCredentials.getPassword())) {
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
