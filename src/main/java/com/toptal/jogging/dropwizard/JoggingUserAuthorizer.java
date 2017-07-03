package com.toptal.jogging.dropwizard;

import com.toptal.jogging.model.User;
import io.dropwizard.auth.Authorizer;

/**
 * Created by Artem on 26.06.2017.
 */
public class JoggingUserAuthorizer implements Authorizer<User> {
    @Override
    public boolean authorize(User principal, String role) {
        return role != null && principal.getRole() != null && role.equals(principal.getRole());
    }
}
