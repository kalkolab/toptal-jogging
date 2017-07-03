package com.toptal.jogging.resources;

import com.toptal.jogging.dao.UsersDAO;
import com.toptal.jogging.model.Representation;
import com.toptal.jogging.model.User;
import io.dropwizard.auth.Auth;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Artem on 26.06.2017.
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersResource {
    private final UsersDAO usersDAO;

    public UsersResource(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }

    @GET
    public User getUser(@Auth User user) {
        return user;
    }

    @POST
    @Path("/new")
    public Representation<User> createUser(@NotNull final User user) {
        User created = usersDAO.createUser(user);
        if (created != null) {
            return new Representation<>(HttpServletResponse.SC_CREATED, user);
        } else {
            return new Representation<>(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, null);
        }
    }
}